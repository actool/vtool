package parser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

import model.State_;
import model.IOLTS;
import model.LTS;
import model.Transition_;
import util.Constants;

/***
 * Classe que importa e converte arquivo do tipo .graphml(software Yed) em
 * objeto LTS e IOLTS, utiliza a biblioteca tinkerpop
 * 
 * @author camil
 *
 */
public class ImportGraphmlFile {
	/***
	 * m�todo que usa a biblioteca tinkerpop para ler o arquivo graphml e converte
	 * em Graph
	 * 
	 * @param path
	 *            diret�rio do arquivo
	 * @return graph grafo gerado apartir do arquivo .graphml
	 */
	public static Graph readGraphmlFile(String path) {
		Graph graph = new TinkerGraph();
		GraphMLReader reader = new GraphMLReader(graph);
		InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(path));
			reader.inputGraph(is);
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo n�o encontrado");
		} catch (IOException e) {
			System.err.println("Erro no processamento do arquivo");
		}
		return graph;
	}

	/***
	 * Converte o arquivo .graphml em LTS, para definir o estado inicial:(i) �
	 * necess�rio criar um estado que aponta para o estado inicial e a transi��o
	 * deve ser sem r�tulo; (ii) o nome do estado inicial deve ser "1". 
	 * OBS: ao utilizar a ferramenta yed, a cada estado e transi��o o nome deve ser definido no campo "description" 
	 * 
	 * @param filePath
	 * @return o LTS subjacente ao que consta no arquivo
	 */
	public static LTS graphToLTS(String filePath) {// (Estado estadoI, String filePath)
		// l� o arquivo .graphml e converte em Graph(objeto que esta na biblioteca
		// tinkerpop)
		Graph graph = ImportGraphmlFile.readGraphmlFile(filePath);

		LTS lts = new LTS();
		// estado "invisivel" que aponta para o estado inicial, pq o yed n�o permite
		// criar uma aresta sem estado inicio e fim
		State_ estadoInvisivel = null;

		// percorre as transi��es
		for (Edge edge : graph.getEdges()) {
			String rotulo = edge.getProperty("description").toString();
			State_ estadoIni = new State_(edge.getVertex(Direction.OUT).getProperty("description").toString());
			State_ estadoFim = new State_(edge.getVertex(Direction.IN).getProperty("description").toString());

			// cria uma nova transi��o com os dados obtidos com a biblioteca de leitura de
			// graphml
			Transition_ transicao = new Transition_(edge.getId().toString(), estadoIni, rotulo, estadoFim);
			// adiciona os estados
			lts.addState(estadoIni);
			lts.addState(estadoFim);

			// verifica se o r�tulo tem informa��o (se n�o � o edge que aponta para o estado
			// inicial)
			// adiciona a transi��o
			if (!rotulo.equals("")) {
				lts.addTransition(transicao);
			}

			// adiciona o alfabeto
			if (!lts.getAlphabet().contains(rotulo) && !rotulo.equals("")) {
				lts.addToAlphabet(rotulo);
			}

			// edge sem rotulo que aponta para o estado inicial
			if (rotulo.equals("")) {
				// guarda o estado a ser removido, pois � o estado invisivel que aponta para o
				// estado inicial
				estadoInvisivel = estadoIni;
				// define o estado inicial, como o que � apontado pela transi��o sem r�tulo
				lts.setInitialState(estadoFim);
			}

		}

		// se o grafo n�o usa uma seta pra indicar o estado inicial
		// ele usa o r�tulo "1" como nome do estado
		if (lts.getInitialState() == null) {
			// verifica dentre os n�s qual � o estado inicial
			for (Vertex vertex : graph.getVertices()) {
				State_ e = new State_(vertex.getProperty("description").toString());// , vertex.getId().toString()
				if (e.getNome().equals("1")) {
					lts.setInitialState(e);
				}
			}
		} else {
			// se o grafo foi criado com a seta apontando para o estado inicial
			// remove-se o estado invisivel
			lts.getStates().remove(estadoInvisivel);
		}

		return lts;
	}
	
	/***
	 * Converte o iolts do arquivo .graphml em objeto IOLTS
	 * @param pathFile diret�rio do arquivo
	 * @param parametroEntradaSaida se as entradas e sa�das est�o diferenciadas pelos simbolos ?/!
	 * @param entradas o alfabeto de entrada
	 * @param saidas o alfabeto de sa�da
	 * @return IOLTS adjacente ao que consta no .aut
	 */
	public static IOLTS graphToIOLTS(String pathFile, boolean parametroEntradaSaida, ArrayList<String> entradas,
			ArrayList<String> saidas) {
		//converte o .graphml em LTS
		LTS lts = graphToLTS(pathFile);
		//cria um novo IOLTS com base no LTS
		IOLTS iolts = new IOLTS(lts);

		ArrayList<String> e = new ArrayList<String>();
		ArrayList<String> s = new ArrayList<String>();

		//altera o conjunto de entradas e sa�das
		//se os r�tulos de entrada e sa�da s�o diferenciados pelos simbolos ?/!
		if (!parametroEntradaSaida) {
			//percorre todo o alfabeto do LTS
			for (String a : lts.getAlphabet()) {
				//se come�a com ! ent�o � simbolo de saida
				if (a.charAt(0) == Constants.OUTPUT_TAG) {
					s.add(a);
				}
				
				//se come�a com ? ent�o � simbolo de entrada
				if (a.charAt(0) == Constants.INPUT_TAG) {
					e.add(a);
				}
			}
			
			//adicionar entradas e sa�das no IOLTS
			iolts.setInputs(e);
			iolts.setOutputs(s);
		} else {//se os simbolos !/? n�o diferenciam
			//o conjunto de entrada e sa�da s�o os passados por parametro
			iolts.setInputs(entradas);
			iolts.setOutputs(saidas);
		}

		return iolts;
	}
}
