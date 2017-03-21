package principal;

import componentes.Cpu;
import componentes.Encoder;
import componentes.ModuloES;
import componentes.Parser;
import componentes.Ram;

public class Computador {

	public static final String TAG = "Computador.class";

	public static Parser parser = new Parser();
	public static Encoder encoder = new Encoder();
	public static Cpu cpu = new Cpu();
	public static Ram ram = new Ram();
	public static ModuloES es = new ModuloES();

	public static void main(String[] args) {
		

		while (parser.instrucaoAtual < parser.instrucoes.size()) {
			System.out.println("+-------------------------------------------------------------------------+");
			System.out.println(
					"Instrução atual: " + parser.instrucaoAtual + " QTDE Instruções: " + parser.instrucoes.size());
			System.out.println("+-------------------------------------------------------------------------+");
//			printToHelp();
			encoder.pullInstructionsFromParser();
			encoder.encoderInstrucao();
			
			System.out.println("\n \n");
		}

	}

	private static void printToHelp(){
		System.out.println(cpu.toString());
		System.out.println(ram.toString());
		System.out.println(es.toString());
		System.out.println("Instruções:");
		parser.printInstructions();
		parser.sendDataToEncoder();
	}

}
