package parser;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import model.State_;
import model.IOLTS;
import model.LTS;
import model.Transition_;
import util.Constants;

/***
 * Classe que importa e converte arquivo do tipo .aut em objeto LTS e IOLTS
 * 
 * @author camil
 *
 */
public class ImportAutFile {

	/***
	 * Lê e valida a primeira linha do arquivo .aut, na primeira linha do aquivo
	 * consta (<initial-state>, <number-of-transitions>, <number-of-states>)
	 * 
	 * @param diretorio
	 *            o diretório do arquivo
	 * @return as configurações presentes na primeira linha
	 */
	public static String[] lerParametros(String diretorio) {
		String[] configs = new String[4];
		String linhaConfiguracao = "";
		try {
			File file = new File(diretorio);
			Scanner sc = new Scanner(file);
			// primeira linha do arquivo
			// des(<initial-state>, <number-of-transitions>, <number-of-states>)
			// String linhaConfiguracao = "des ( 1, 2, 3) ";
			linhaConfiguracao = sc.nextLine();
			// remove o espaço que possa ter ao final da string de configuração
			linhaConfiguracao = linhaConfiguracao.replaceAll("\\s+$", "");
			// indice do fim parentese
			int indiceFimParentese = linhaConfiguracao.length() - 1;

			String msg = "";
			// verifica se há o parentese final
			if (linhaConfiguracao.charAt(indiceFimParentese) != ')') {
				msg += ("primeira linha do arquivo inválida! ausência do ')' " + "\n");
			}

			// indice do inicio do parentese
			int indiceIniParentese = linhaConfiguracao.indexOf("(");
			if (indiceIniParentese < 0) {
				msg += ("primeira linha do arquivo inválida! ausência do '(' " + "\n");
			}

			// se tiver achado o inicio e fim do parentese
			if (msg == "") {
				// substring com os dados da configuração
				linhaConfiguracao = linhaConfiguracao.substring(indiceIniParentese + 1, indiceFimParentese);
			}
			// pega as configurações que esta na linha
			configs = linhaConfiguracao.split(",");
			// se há menos de 3 parametros
			if (configs.length < 3) {
				msg += ("deveriam ter sido passados 3 parâmetros separados por virgula na primeira linha" + "\n");
				// se esta faltando algum parametro atribui vazio
				for (int i = configs.length; i < 3; i++) {
					configs = append(configs, "");
				}
			}
			// se tiver mais de 3 parametros considera os 3 primeiros
			if (configs.length > 3) {
				configs = new String[4];
				// se esta faltando algum parametro atribui vazio
				for (int i = 0; i < 3; i++) {
					configs = append(configs, "");
				}
			}
			configs = append(configs, msg);
		} catch (FileNotFoundException e) {
			System.err.println("Erro na leitura do arquivo:");
			System.err.println(e);
		}

		return configs;
	}

	/***
	 * Adiciona elemento no array[]
	 * 
	 * @param arr
	 *            array que recebera novo elemento
	 * @param element
	 *            elemento a ser adicionado ao array
	 * @return array apos adicionado o elemento
	 */
	static <T> T[] append(T[] arr, T element) {
		// tamanho do array recebido
		final int N = arr.length;
		// criar uma cópia do array com mais uma posição
		arr = Arrays.copyOf(arr, N + 1);
		// adicionar o elemento na ultima posição do array
		arr[N] = element;
		return arr;
	}

	/***
	 * Converte o iolts do arquivo .aut em objeto IOLTS
	 * 
	 * @param diretorio
	 *            diretório do arquivo
	 * @param temListaRotulos
	 *            se as entradas e saídas estão diferenciadas pelos simbolos ?/!
	 * @param entradas
	 *            o alfabeto de entrada
	 * @param saidas
	 *            o alfabeto de saída
	 * @return IOLTS adjacente ao que consta no .aut
	 */
	public static IOLTS autToIOLTS(String diretorio, boolean temListaRotulos, ArrayList<String> entradas,
			ArrayList<String> saidas) throws Exception {

		try {
			// converte o .aut em LTS
			LTS lts = autToLTS(diretorio);
			// cria um novo IOLTS com base no LTS
			IOLTS iolts = new IOLTS(lts);

			ArrayList<String> e = new ArrayList<String>();
			ArrayList<String> s = new ArrayList<String>();

			// altera o conjunto de entradas e saídas
			// se os rótulos de entrada e saída são diferenciados pelos simbolos ?/!
			if (!temListaRotulos) {
				// percorre todo o alfabeto do LTS
				for (String a : lts.getAlphabet()) {
					// se começa com ! então é simbolo de saida
					if (a.charAt(0) == Constants.OUTPUT_TAG) {
						// s.add(a);
						s.add(a.substring(1, a.length()));
					}

					// se começa com ? então é simbolo de entrada
					if (a.charAt(0) == Constants.INPUT_TAG) {
						// e.add(a);
						e.add(a.substring(1, a.length()));
					}
				}

				// adicionar entradas e saídas no IOLTS
				iolts.setInputs(e);
				iolts.setOutputs(s);

				// remover das transições os rótulos ! ?
				ArrayList<Transition_> transicoes = new ArrayList<Transition_>();
				for (Transition_ t : iolts.getTransitions()) {
					transicoes.add(new Transition_(t.getEstadoIni(), t.getRotulo().substring(1, t.getRotulo().length()),
							t.getEstadoFim()));
				}

				iolts.setTransitions(transicoes);
			} else {// se os simbolos !/? não diferenciam
				// o conjunto de entrada e saída são os passados por parametro
				iolts.setInputs(entradas);
				iolts.setOutputs(saidas);
			}

			return iolts;
		} catch (Exception e) {
			throw e;
		}
	}

	/***
	 * Converte o lts do arquivo .aut em objeto LTS
	 * 
	 * @param diretorio
	 * @return LTS adjacente ao que consta no .aut
	 */
	public static LTS autToLTS(String diretorio) throws Exception {
		// lê os parametros de configuração da primeira linha do arquivo
		String[] configs = ImportAutFile.lerParametros(diretorio);

		// mensagem se houve algum erro/inconsistencia na leitura da primeira linha
		String msg = configs[3];

		int msg_cont = 0;

		// se há inconsistência na primeira linha do arquivo
		if (msg != "") {
			msg += ("formato esperado:" + "\n");
			msg += ("'des(<initial-state>, <number-of-transitions>, <number-of-states>)'" + "\n");
			System.out.println(msg);
			//return null;
			throw new Exception(msg);
		} else {
			// mensagens de erro, sobre as inconsistencias a cada linha
			msg = "";
			State_ estadoIni = null;
			State_ estadoFim = null;
			Transition_ transicao = null;

			// linha lida do arquivo
			String linha = "";
			boolean linhaInconsistente = false;

			// novo lts que sera construido com base no arquivo
			LTS lts = new LTS();
			// parametros lidos da primeira linha do arquivo
			// definição do estado inicial com base na 1ª linha do arquivo (configuração)
			lts.setInitialState(new State_(configs[0]));
			int nTransicoes = Integer.parseInt(configs[1].replaceAll("\\s+", ""));
			int nEstados = Integer.parseInt(configs[2].replaceAll("\\s+", ""));

			// contador de linha começa da linha 2 porque a linha 1 é a linha de
			// configuração
			int cont = 2;
			try {
				File file = new File(diretorio);
				Scanner sc = new Scanner(file);
				// pula a primeira linha de configuração
				sc.nextLine();
				// se há linha do arquivo a ser lida
				while (sc.hasNextLine()) {
					linhaInconsistente = false;
					// le a linha com a transição
					// cada transição é configurada da seguinte forma: (<from-state>, <label>,
					// <to-state>)
					linha = sc.nextLine();

					// verifica se há o '(' na linha lida
					int ini = linha.indexOf("(");
					if (ini < 0) {
						msg += ("linha [" + cont + "] inválida ausência do '(' " + "\n");
						linhaInconsistente = true;
						msg_cont += 1;
					}

					// verifica se há o ')' na linha lida
					linha = linha.replaceAll("\\s+$", "");
					int fim = linha.length() - 1;
					if (linha.charAt(fim) != ')') {
						msg += ("linha inválida [" + cont + "] ausência do ')' " + "\n");
						linhaInconsistente = true;
						msg_cont += 1;
					}

					// verifica se há os 3 parametros
					// (<from-state>, <label>, <to-state>)
					linha = linha.substring(ini + 1, fim);
					String[] val = linha.split(",");
					if (val.length != 3) {
						msg += ("linha [" + cont + "] deveriam ter sido passados 3 parâmetros separados por virgula"
								+ "\n");
						linhaInconsistente = true;
						msg_cont += 1;
					}

					// se a linha da transição esta completa, sem inconsistencia
					if (!linhaInconsistente) {
						// cria estados e transições
						estadoIni = new State_(val[0].trim());
						estadoFim = new State_(val[2].trim());
						transicao = new Transition_(estadoIni, val[1].trim(), estadoFim);

						// faz a atribuição dos atributos ao LTS
						lts.addState(estadoIni);
						lts.addState(estadoFim);
						lts.addTransition(transicao);
					}
					// quantidade de transições
					cont++;
				}

				// porque se existir mensagem de inconsistencia ele não adiciona os valores no
				// LTS
				// logo a quantidade de estados e transições estará divergente
				if (msg.equals("")) {
					// se não houver a quantidade de transições que consta definido na primeira
					// linha
					if (nTransicoes != lts.getTransitions().size()) {
						msg += "Quantidade de transições divergente do valor passado na 1ª linha \n";
					}

					// se não houver a quantidade de estados que consta definido na primeira linha
					if (nEstados != lts.getStates().size()) {
						msg += "Quantidade de estados divergente do valor passado na 1ª linha \n";
					}
				}

			} catch (Exception e) {
				throw new Exception("Erro na conversão do arquivo para LTS");
//				System.err.println("Erro na conversão do arquivo para LTS");
//				System.err.println(e);
			}

			// se não há nenhuma inconsistencia na leitura das transições, não precisa
			// validar a se qt de transições e estados batem com a configuração (JTorx)
			if (msg_cont == 0) {// if (msg.equals("")) {
				return lts;
			} else {
				msg = ("Inconsistências na leitura do arquivo .aut! \n" + "Diretório: " + diretorio + "\n"
						+ "Mensagem: \n" + msg);

				throw new Exception(msg);
				// System.out.println(msg);
				// return null;
			}

		}
	}

}
