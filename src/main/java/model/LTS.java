/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

import algorithm.Operations;


/**
 * Classe LTS
 * 
 * @author Camila
 */
public class LTS {
	// lista de estados
	protected List<State_> states;
	// estado inicial
	protected State_ initialState;
	// lista de transi��es
	protected List<Transition_> transitions;
	// lista que corresponde ao alfabeto
	protected List<String> alphabet;

	/***
	 * Construtor com todos os parametros
	 * 
	 * @param estados
	 * @param estadoInicial
	 * @param alfabeto
	 * @param transicoes
	 */
	public LTS(List<State_> estados, State_ estadoInicial, List<String> alfabeto, List<Transition_> transicoes) {
		this.states = estados;
		this.transitions = transicoes;
		this.alphabet = alfabeto;
		this.initialState = estadoInicial;
	}

	/***
	 * Contrutor sem parametros que inicializa as listas e o estado inicial
	 */
	public LTS() {
		this.states = new ArrayList<State_>();
		this.transitions = new ArrayList<Transition_>();
		this.alphabet = new ArrayList<String>();
		this.initialState = null;
	}

	/**
	 * Retorna o conjunto de estados do LTS
	 * 
	 * @return o conjunto de estados
	 */
	public List<State_> getStates() {
		return states;
	}

	/**
	 * Altera o conjunto de estados
	 * 
	 * @param o
	 *            conjunto de estados
	 * 
	 */
	public void setStates(List<State_> estados) {
		this.states = estados;
	}

	/**
	 * Retorna o estado inicial do LTS
	 * 
	 * @return o estado inicial do LTS
	 */
	public State_ getInitialState() {
		return initialState;
	}

	/**
	 * Altera o estado inicial do LTS
	 * 
	 * @param estado
	 *            inicial do LTS
	 * 
	 */
	public void setInitialState(State_ estadoInicial) {
		this.initialState = estadoInicial;
	}

	/**
	 * Retorna a lista de transi��es do LTS
	 * 
	 * @return lista de transi��es
	 */
	public List<Transition_> getTransitions() {
		return transitions;
	}

	/**
	 * Altera a lista de transi��es do LTS
	 * 
	 * @param lista
	 *            de transi��es
	 */
	public void setTransitions(List<Transition_> transicoes) {
		this.transitions = transicoes;
	}

	/**
	 * Retorna o alfabeto do LTS
	 * 
	 * @return o alfabeto
	 */
	public List<String> getAlphabet() {
		return alphabet;
	}

	/**
	 * Altera o alfabeto do LTS
	 * 
	 * @param o
	 *            alfabeto
	 */
	public void setAlphabet(List<String> alfabeto) {
		this.alphabet = alfabeto;
	}

	/***
	 * Adiciona o estado recebido de parametro na lista de estados do LTS
	 * verificando antes se o estado j� existe
	 * 
	 * @param estado
	 *            a ser adicionado
	 */
	public void addState(State_ estado) {
		// verifica se o estado j� existe no conjunto de estados do LTS
		if (!this.states.contains(estado)) {
			this.states.add(estado);
		}
	}

	/***
	 * Adiciona a transi��o a lista de transi��o, adiciona tambem o r�tulo da
	 * transi��o na lista de alfabeto
	 * 
	 * @param transicao
	 */
	public void addTransition(Transition_ transicao) {
		// verifica se a transi��o j� existe na lista de transi��o
		if (!this.transitions.contains(transicao)) {
			this.transitions.add(transicao);
		}

		// adiciona o r�tulo na lista de alfabeto
		this.addToAlphabet(transicao.getRotulo());
	}

	/***
	 * Adiciona string ao alfabeto
	 * 
	 * @param simbolo
	 *            string a adicionar no alfabeto
	 */
	public void addToAlphabet(String simbolo) {
		// verifica se "a" j� existe no alfabeto
		if (!this.alphabet.contains(simbolo)) {
			this.alphabet.add(simbolo);
		}
	}

	

	/***
	 * Verifica se existe transi��o partindo do estado inicial e r�tulo recebidos de
	 * parametro, e retorna todos os estado alcan�ados por essas transi��es
	 * 
	 * @param rotuloEstadoIni
	 * @param rotuloTransicao
	 * @return resultado, se existe transi��o e quais s�o os estados alcan�ados por
	 *         essas transi��es
	 */
	public Result transitionExists(String rotuloEstadoIni, String rotuloTransicao) {
		// resultado final a ser retornado
		Result resultado = new Result();
		// lista de estados alcan�ados
		List<State_> estadosFim = new ArrayList<State_>();
		// percorre todas as transi��es
		for (Transition_ t : transitions) {
			// verifica se a transi��o contem o mesmo o estado inicial da transi��o e o
			// r�tulo passados de parametro
			if (t.getEstadoIni().getNome().toString().equals(rotuloEstadoIni.toString())
					&& t.getRotulo().toString().equals(rotuloTransicao.toString())) {
				// define que encontrou transi��o
				resultado.setFound(true);
				// adiciona o estado alcan�ado
				estadosFim.add(t.getEstadoFim());
			}
		}

		// altera os estados alcan�ados
		resultado.setReachedStates(estadosFim);

		// se nenhum estado foi alcan�ado partindo do estado e do r�tulo passado de
		// parametro
		if (estadosFim.size() <= 0) {
			// define que n�o encontrou nenhum estado
			resultado.setFound(false);
			// os estados finais como nulo
			resultado.reachedStates = null;
		}

		return resultado;
	}

	/***
	 * Recupera as transi��es que partem do estado passado por parametro
	 * 
	 * @param estado
	 * @return lista de transi��es que partem do estado passado de parametro
	 */
	public List<Transition_> transitionsByIniState(State_ estado) {
		// inicia lista
		List<Transition_> transicoesDoEstado = new ArrayList<Transition_>();
		// percorre transi��es
		for (Transition_ t : transitions) {
			// verifica se a transi��o parte do estado passado de parametro
			if (t.getEstadoIni().getNome().equals(estado.getNome())) {
				// adiciona a transi��o a lista
				transicoesDoEstado.add(t);
			}
		}

		return transicoesDoEstado;
	}

	/***
	 * Constroi o automato deterministico subjacente ao LTS
	 * 
	 * @return o automato subjacente ao LTS
	 */
	public Automaton_ ltsToAutomaton() {
		// cria automato
		Automaton_ as = new Automaton_();
		// altera os atributos com base no LTS
		as.setStates(this.states);
		as.setInitialState(this.initialState);
		as.setAlphabet(this.alphabet);
		as.setFinalStates(this.states);
		as.setTransitions(Operations.processTauTransition(this.transitions));

		// converte o automato em deterministico
		return Operations.convertToDeterministicAutomaton(as);
	}

	/***
	 * Sobreescrita do m�todo toString
	 * 
	 * @return a string que descreve o LTS
	 */
	@Override
	public String toString() {
		String s = "";
		// o estado inicial
		s += ("##############################\n");
		s += ("           Estado Inicial \n");
		s += ("##############################\n");
		s += ("[" + initialState.getNome() +"]"+ "\n\n");

		// os estados do LTS
		s += ("##############################\n");
		s += ("           Estados \n");
		s += ("##############################\n");
		s += ("Quantidade: " + this.states.size() + "\n");
		for (State_ e : this.states) {
			s += ("[" + e.getNome() + "]-");
		}

		// as transi��es do LTS
		s += ("\n\n##############################\n");
		s += ("         Transi��es\n");
		s += ("##############################\n");
		s += ("Quantidade: " + this.transitions.size() + "\n");
		for (Transition_ t : this.transitions) {
			s += (t.getEstadoIni().getNome() + " - " + t.getRotulo() + " - " + t.getEstadoFim().getNome() + "\n");
		}

		// o alfabeto do LTS
		s += ("\n##############################\n");
		s += ("         Alfabeto\n");
		s += ("##############################\n");
		s+="[";
		for (String t : this.alphabet) {
			s += (t + " - ");
		}
		s += "]\n";

		return s;
	}

	/***
	 * Classe Resultado utilizada na verifica��o de que transi��es existem partindo
	 * de determinado estado e r�tulo
	 * 
	 * @author camil
	 *
	 */
	public class Result {
		// se encontrou transi��o
		private boolean found;
		// os estados alcan�ados pela transi��o
		private List<State_> reachedStates;

		/***
		 * Alterar se encontrou transi��o
		 * 
		 * @param encontrou
		 *            transi��o
		 */
		public void setFound(boolean encontrou) {
			this.found = encontrou;
		}

		/***
		 * Retorna a lista de estados alcan�ados
		 * 
		 * @return lista de estados alcan�ados
		 */
		public List<State_> getReachedStates() {
			return reachedStates;
		}

		/***
		 * Altera a lista de estados alcan�ados
		 * 
		 * @param lista
		 *            de estados alcan�ados
		 */
		public void setReachedStates(List<State_> estadosFim) {
			this.reachedStates = estadosFim;
		}

		/***
		 * Retorna se encontrou transi��o
		 * 
		 * @return encontrou transi��o
		 */
		public boolean getFound() {
			return found;
		}
		
	
	}

}
