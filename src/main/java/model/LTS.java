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
	// lista de transições
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
	 * Retorna a lista de transições do LTS
	 * 
	 * @return lista de transições
	 */
	public List<Transition_> getTransitions() {
		return transitions;
	}

	/**
	 * Altera a lista de transições do LTS
	 * 
	 * @param lista
	 *            de transições
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
	 * verificando antes se o estado já existe
	 * 
	 * @param estado
	 *            a ser adicionado
	 */
	public void addState(State_ estado) {
		// verifica se o estado já existe no conjunto de estados do LTS
		if (!this.states.contains(estado)) {
			this.states.add(estado);
		}
	}

	/***
	 * Adiciona a transição a lista de transição, adiciona tambem o rótulo da
	 * transição na lista de alfabeto
	 * 
	 * @param transicao
	 */
	public void addTransition(Transition_ transicao) {
		// verifica se a transição já existe na lista de transição
		if (!this.transitions.contains(transicao)) {
			this.transitions.add(transicao);
		}

		// adiciona o rótulo na lista de alfabeto
		this.addToAlphabet(transicao.getRotulo());
	}

	/***
	 * Adiciona string ao alfabeto
	 * 
	 * @param simbolo
	 *            string a adicionar no alfabeto
	 */
	public void addToAlphabet(String simbolo) {
		// verifica se "a" já existe no alfabeto
		if (!this.alphabet.contains(simbolo)) {
			this.alphabet.add(simbolo);
		}
	}

	

	/***
	 * Verifica se existe transição partindo do estado inicial e rótulo recebidos de
	 * parametro, e retorna todos os estado alcançados por essas transições
	 * 
	 * @param rotuloEstadoIni
	 * @param rotuloTransicao
	 * @return resultado, se existe transição e quais são os estados alcançados por
	 *         essas transições
	 */
	public Result transitionExists(String rotuloEstadoIni, String rotuloTransicao) {
		// resultado final a ser retornado
		Result resultado = new Result();
		// lista de estados alcançados
		List<State_> estadosFim = new ArrayList<State_>();
		// percorre todas as transições
		for (Transition_ t : transitions) {
			// verifica se a transição contem o mesmo o estado inicial da transição e o
			// rótulo passados de parametro
			if (t.getEstadoIni().getNome().toString().equals(rotuloEstadoIni.toString())
					&& t.getRotulo().toString().equals(rotuloTransicao.toString())) {
				// define que encontrou transição
				resultado.setFound(true);
				// adiciona o estado alcançado
				estadosFim.add(t.getEstadoFim());
			}
		}

		// altera os estados alcançados
		resultado.setReachedStates(estadosFim);

		// se nenhum estado foi alcançado partindo do estado e do rótulo passado de
		// parametro
		if (estadosFim.size() <= 0) {
			// define que não encontrou nenhum estado
			resultado.setFound(false);
			// os estados finais como nulo
			resultado.reachedStates = null;
		}

		return resultado;
	}

	/***
	 * Recupera as transições que partem do estado passado por parametro
	 * 
	 * @param estado
	 * @return lista de transições que partem do estado passado de parametro
	 */
	public List<Transition_> transitionsByIniState(State_ estado) {
		// inicia lista
		List<Transition_> transicoesDoEstado = new ArrayList<Transition_>();
		// percorre transições
		for (Transition_ t : transitions) {
			// verifica se a transição parte do estado passado de parametro
			if (t.getEstadoIni().getNome().equals(estado.getNome())) {
				// adiciona a transição a lista
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
	 * Sobreescrita do método toString
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

		// as transições do LTS
		s += ("\n\n##############################\n");
		s += ("         Transições\n");
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
	 * Classe Resultado utilizada na verificação de que transições existem partindo
	 * de determinado estado e rótulo
	 * 
	 * @author camil
	 *
	 */
	public class Result {
		// se encontrou transição
		private boolean found;
		// os estados alcançados pela transição
		private List<State_> reachedStates;

		/***
		 * Alterar se encontrou transição
		 * 
		 * @param encontrou
		 *            transição
		 */
		public void setFound(boolean encontrou) {
			this.found = encontrou;
		}

		/***
		 * Retorna a lista de estados alcançados
		 * 
		 * @return lista de estados alcançados
		 */
		public List<State_> getReachedStates() {
			return reachedStates;
		}

		/***
		 * Altera a lista de estados alcançados
		 * 
		 * @param lista
		 *            de estados alcançados
		 */
		public void setReachedStates(List<State_> estadosFim) {
			this.reachedStates = estadosFim;
		}

		/***
		 * Retorna se encontrou transição
		 * 
		 * @return encontrou transição
		 */
		public boolean getFound() {
			return found;
		}
		
	
	}

}
