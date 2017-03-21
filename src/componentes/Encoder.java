package componentes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import principal.Computador;
import utils.Constantes;

public class Encoder {

	private String instrucao;

	public void pullInstructionsFromParser() {
		// s� pode pegar essa instru��o se o CI do buffer for -1
		// ou a informa��o que tiver no espa�o buffer[BufferCI] for diferente de
		// 0 e 1
		instrucao = Computador.parser.instrucoes.get(Computador.parser.instrucaoAtual);
		printInstrucao();
		Computador.parser.sendDataToEncoder();
	}

	public void encoderInstrucao() {
		Pattern p1 = Pattern.compile(Constantes.RE_add_mov);
		Pattern p2 = Pattern.compile(Constantes.RE_inc);
		Pattern p3 = Pattern.compile(Constantes.RE_imul);
		long code[];
		Matcher type1, type2, type3;

		type1 = p1.matcher(instrucao);
		type2 = p2.matcher(instrucao);
		type3 = p3.matcher(instrucao);

		if (type1.matches() || type2.matches() || type3.matches()) {
			Pattern r = Pattern.compile(Constantes.RE_register);
			Pattern m = Pattern.compile(Constantes.RE_memory);

			if (type1.matches()) {
				String type, x, y;
				type = type1.group(1);
				x = type1.group(2);
				y = type1.group(3);

				if (type.equalsIgnoreCase("mov")) {
					code = encoderMovInstruction(r, m, x, y);
				} else {
					code = encoderAddInstruction(r, m, x, y);
				}
				System.out.println("Instru��o codificada em longs: " + code[0] + " " + code[1] + " " + code[2]);
			} else if (type2.matches()) {
				String x = type2.group(2);
				
				code = encoderIncInstruction(r, x);
				System.out.println("Instru��o codificada em longs: " + code[0] + " " + code[1]);
			} else {
				System.out.println("imul");
			}
		} else {
			System.out.println("Programa encerrado. Error line: " + Computador.parser.instrucaoAtual);
		}
	}

	long[] encoderMovInstruction(Pattern r, Pattern m, String x, String y) {
		long code[] = new long[3];
		Matcher matcher;

		matcher = r.matcher(x);
		if (matcher.matches()) {
			// mov register
			code[1] = encoderRegister(x);
			matcher = r.matcher(y);
			if (matcher.matches()) {
				// mov register, register
				code[0] = Constantes.VALUE_mov_r_from_r;
				code[2] = encoderRegister(y);
			} else {
				matcher = m.matcher(y);
				if (matcher.matches()) {
					// mov register, memory
					code[0] = Constantes.VALUE_mov_r_from_m;
					code[2] = encoderMemory(y);
				} else {
					// mov register, immediate
					code[0] = Constantes.VALUE_mov_r_from_i;
					code[2] = Long.valueOf(y).longValue();
				}
			}

		} else {
			// mov memory
			code[1] = encoderMemory(x);
			matcher = r.matcher(y);
			if (matcher.matches()) {
				// mov memory, register
				code[0] = Constantes.VALUE_mov_m_from_r;
				code[2] = encoderRegister(y);
			} else {
				matcher = m.matcher(y);
				if (!matcher.matches()) {
					// mov memory, immediate
					code[0] = Constantes.VALUE_mov_m_from_i;
					code[2] = Long.parseLong(y);
				} else {
					// mov memory, memory
					code[0] = Constantes.VALUE_mov_m_from_m;
					code[2] = encoderMemory(y);
				}
			}
		}
		return code;
	}

	long[] encoderAddInstruction(Pattern r, Pattern m, String x, String y) {
		long code[] = new long[3];
		Matcher matcher;

		matcher = r.matcher(x);
		if (matcher.matches()) {
			// add register
			code[1] = encoderRegister(x);
			matcher = r.matcher(y);
			if (matcher.matches()) {
				// add register, register
				code[0] = Constantes.VALUE_add_r_from_r;
				code[2] = encoderRegister(y);
			} else {
				matcher = m.matcher(y);
				if (matcher.matches()) {
					// add register, memory
					code[0] = Constantes.VALUE_add_r_from_m;
					code[2] = encoderMemory(y);
				} else {
					// add register, immediate
					code[0] = Constantes.VALUE_add_r_from_i;
					code[2] = Long.valueOf(y).longValue();
				}
			}

		} else {
			// add memory
			code[1] = encoderMemory(x);
			matcher = r.matcher(y);
			if (matcher.matches()) {
				// add memory, register
				code[0] = Constantes.VALUE_add_m_from_r;
				code[2] = encoderRegister(y);
			} else {
				matcher = m.matcher(y);
				if (!matcher.matches()) {
					// add memory, immediate
					code[0] = Constantes.VALUE_add_m_from_i;
					code[2] = Long.parseLong(y);
				} else {
					// add memory, memory
					code[0] = Constantes.VALUE_add_m_from_m;
					code[2] = encoderMemory(y);
				}
			}
		}
		return code;
	}

	long[] encoderIncInstruction(Pattern r, String x) {
		long code[] = new long[2];
		Matcher matcher = r.matcher(x);
		if (matcher.matches()) {
			// inc register
			code[0] = Constantes.VALUE_inc_r;
			code[1] = encoderRegister(x);
		} else {
			// inc memory
			code[0] = Constantes.VALUE_inc_m;
			code[1] = encoderMemory(x);
		}
		return code;
	}

	long encoderRegister(String register) {
		if (register.equalsIgnoreCase("a")) {
			return Constantes.VALUE_register_A;
		} else if (register.equalsIgnoreCase("b")) {
			return Constantes.VALUE_register_B;
		} else if (register.equalsIgnoreCase("c")) {
			return Constantes.VALUE_register_C;
		} else {
			return Constantes.VALUE_register_D;
		}

	}

	long encoderMemory(String memory) {
		return Long.valueOf(memory.substring(2)).longValue();
	}

	public void sendInstructionsToESBuffer() {

	}

	public void printInstrucao() {
		System.out.println("Instru��o atual: " + instrucao);
	}

	public String getInstrucao() {
		return instrucao;
	}

	public void setInstrucao(String instrucao) {
		this.instrucao = instrucao;
	}

}
