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
	 * L� e valida a primeira linha do arquivo .aut, na primeira linha do aquivo
	 * consta (<initial-state>, <number-of-transitions>, <number-of-states>)
	 * 
	 * @param diretorio
	 *            o diret�rio do arquivo
	 * @return as configura��es presentes na primeira linha
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
			// remove o espa�o que possa ter ao final da string de configura��o
			linhaConfiguracao = linhaConfiguracao.replaceAll("\\s+$", "");
			// indice do fim parentese
			int indiceFimParentese = linhaConfiguracao.length() - 1;

			String msg = "";
			// verifica se h� o parentese final
			if (linhaConfiguracao.charAt(indiceFimParentese) != ')') {
				msg += ("primeira linha do arquivo inv�lida! aus�ncia do ')' " + "\n");
			}

			// indice do inicio do parentese
			int indiceIniParentese = linhaConfiguracao.indexOf("(");
			if (indiceIniParentese < 0) {
				msg += ("primeira linha do arquivo inv�lida! aus�ncia do '(' " + "\n");
			}

			// se tiver achado o inicio e fim do parentese
			if (msg == "") {
				// substring com os dados da configura��o
				linhaConfiguracao = linhaConfiguracao.substring(indiceIniParentese + 1, indiceFimParentese);
			}
			// pega as configura��es que esta na linha
			configs = linhaConfiguracao.split(",");
			// se h� menos de 3 parametros
			if (configs.length < 3) {
				msg += ("deveriam ter sido passados 3 par�metros separados por virgula na primeira linha" + "\n");
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
		// criar uma c�pia do array com mais uma posi��o
		arr = Arrays.copyOf(arr, N + 1);
		// adicionar o elemento na ultima posi��o do array
		arr[N] = element;
		return arr;
	}

	/***
	 * Converte o iolts do arquivo .aut em objeto IOLTS
	 * 
	 * @param diretorio
	 *            diret�rio do arquivo
	 * @param temListaRotulos
	 *            se as entradas e sa�das est�o diferenciadas pelos simbolos ?/!
	 * @param entradas
	 *            o alfabeto de entrada
	 * @param saidas
	 *            o alfabeto de sa�da
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

			// altera o conjunto de entradas e sa�das
			// se os r�tulos de entrada e sa�da s�o diferenciados pelos simbolos ?/!
			if (!temListaRotulos) {
				// percorre todo o alfabeto do LTS
				for (String a : lts.getAlphabet()) {
					// se come�a com ! ent�o � simbolo de saida
					if (a.charAt(0) == Constants.OUTPUT_TAG) {
						// s.add(a);
						s.add(a.substring(1, a.length()));
					}

					// se come�a com ? ent�o � simbolo de entrada
					if (a.charAt(0) == Constants.INPUT_TAG) {
						// e.add(a);
						e.add(a.substring(1, a.length()));
					}
				}

				// adicionar entradas e sa�das no IOLTS
				iolts.setInputs(e);
				iolts.setOutputs(s);

				// remover das transi��es os r�tulos ! ?
				ArrayList<Transition_> transicoes = new ArrayList<Transition_>();
				for (Transition_ t : iolts.getTransitions()) {
					transicoes.add(new Transition_(t.getEstadoIni(), t.getRotulo().substring(1, t.getRotulo().length()),
							t.getEstadoFim()));
				}

				iolts.setTransitions(transicoes);
			} else {// se os simbolos !/? n�o diferenciam
				// o conjunto de entrada e sa�da s�o os passados por parametro
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
		// l� os parametros de configura��o da primeira linha do arquivo
		String[] configs = ImportAutFile.lerParametros(diretorio);

		// mensagem se houve algum erro/inconsistencia na leitura da primeira linha
		String msg = configs[3];

		int msg_cont = 0;

		// se h� inconsist�ncia na primeira linha do arquivo
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
			// defini��o do estado inicial com base na 1� linha do arquivo (configura��o)
			lts.setInitialState(new State_(configs[0]));
			int nTransicoes = Integer.parseInt(configs[1].replaceAll("\\s+", ""));
			int nEstados = Integer.parseInt(configs[2].replaceAll("\\s+", ""));

			// contador de linha come�a da linha 2 porque a linha 1 � a linha de
			// configura��o
			int cont = 2;
			try {
				File file = new File(diretorio);
				Scanner sc = new Scanner(file);
				// pula a primeira linha de configura��o
				sc.nextLine();
				// se h� linha do arquivo a ser lida
				while (sc.hasNextLine()) {
					linhaInconsistente = false;
					// le a linha com a transi��o
					// cada transi��o � configurada da seguinte forma: (<from-state>, <label>,
					// <to-state>)
					linha = sc.nextLine();

					// verifica se h� o '(' na linha lida
					int ini = linha.indexOf("(");
					if (ini < 0) {
						msg += ("linha [" + cont + "] inv�lida aus�ncia do '(' " + "\n");
						linhaInconsistente = true;
						msg_cont += 1;
					}

					// verifica se h� o ')' na linha lida
					linha = linha.replaceAll("\\s+$", "");
					int fim = linha.length() - 1;
					if (linha.charAt(fim) != ')') {
						msg += ("linha inv�lida [" + cont + "] aus�ncia do ')' " + "\n");
						linhaInconsistente = true;
						msg_cont += 1;
					}

					// verifica se h� os 3 parametros
					// (<from-state>, <label>, <to-state>)
					linha = linha.substring(ini + 1, fim);
					String[] val = linha.split(",");
					if (val.length != 3) {
						msg += ("linha [" + cont + "] deveriam ter sido passados 3 par�metros separados por virgula"
								+ "\n");
						linhaInconsistente = true;
						msg_cont += 1;
					}

					// se a linha da transi��o esta completa, sem inconsistencia
					if (!linhaInconsistente) {
						// cria estados e transi��es
						estadoIni = new State_(val[0].trim());
						estadoFim = new State_(val[2].trim());
						transicao = new Transition_(estadoIni, val[1].trim(), estadoFim);

						// faz a atribui��o dos atributos ao LTS
						lts.addState(estadoIni);
						lts.addState(estadoFim);
						lts.addTransition(transicao);
					}
					// quantidade de transi��es
					cont++;
				}

				// porque se existir mensagem de inconsistencia ele n�o adiciona os valores no
				// LTS
				// logo a quantidade de estados e transi��es estar� divergente
				if (msg.equals("")) {
					// se n�o houver a quantidade de transi��es que consta definido na primeira
					// linha
					if (nTransicoes != lts.getTransitions().size()) {
						msg += "Quantidade de transi��es divergente do valor passado na 1� linha \n";
					}

					// se n�o houver a quantidade de estados que consta definido na primeira linha
					if (nEstados != lts.getStates().size()) {
						msg += "Quantidade de estados divergente do valor passado na 1� linha \n";
					}
				}

			} catch (Exception e) {
				throw new Exception("Erro na convers�o do arquivo para LTS");
//				System.err.println("Erro na convers�o do arquivo para LTS");
//				System.err.println(e);
			}

			// se n�o h� nenhuma inconsistencia na leitura das transi��es, n�o precisa
			// validar a se qt de transi��es e estados batem com a configura��o (JTorx)
			if (msg_cont == 0) {// if (msg.equals("")) {
				return lts;
			} else {
				msg = ("Inconsist�ncias na leitura do arquivo .aut! \n" + "Diret�rio: " + diretorio + "\n"
						+ "Mensagem: \n" + msg);

				throw new Exception(msg);
				// System.out.println(msg);
				// return null;
			}

		}
	}

}
