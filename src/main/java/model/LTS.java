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
 * Class LTS
 * 
 * @author Camila
 */
public class LTS {
	// list of states
	protected List<State_> states;
	// initial state
	protected State_ initialState;
	// list of transitions
	protected List<Transition_> transitions;
	// list of alphabet
	protected List<String> alphabet;

	/***
	 * Constructor with all parameters
	 * 
	 * @param states
	 * @param initialState
	 * @param alphabet
	 * @param transitions
	 */
	public LTS(List<State_> states, State_ initialState, List<String> alphabet, List<Transition_> transitions) {
		this.states = states;
		this.transitions = transitions;
		this.alphabet = alphabet;
		this.initialState = initialState;
	}

	/***
	 * Empty contrutor
	 */
	public LTS() {
		this.states = new ArrayList<State_>();
		this.transitions = new ArrayList<Transition_>();
		this.alphabet = new ArrayList<String>();
		this.initialState = null;
	}

	/**
	 * returns the set of states
	 * 
	 * @return states set of state
	 */
	public List<State_> getStates() {
		return states;
	}

	/**
	 * Alter set of states
	 * 
	 * @param states 
	 *            set of states
	 * 
	 */
	public void setStates(List<State_> states) {
		this.states = states;
	}

	/**
	 * return the initial states
	 * 
	 * @return initialState the initial state
	 */
	public State_ getInitialState() {
		return initialState;
	}

	/**
	 * Alter the initial state
	 * 
	 * @param initialState
	 *            
	 * 
	 */
	public void setInitialState(State_ initialState) {
		this.initialState = initialState;
	}

	/**
	 * Return all transitions
	 * 
	 * @return transitions list of transition
	 */
	public List<Transition_> getTransitions() {
		return transitions;
	}

	/**
	 * Alter list of transition
	 * 
	 * @param transitions list of transition
	 *            
	 */
	public void setTransitions(List<Transition_> transitions) {
		this.transitions = transitions;
	}

	/**
	 * Return alphabet
	 * 
	 * @return alphabet
	 */
	public List<String> getAlphabet() {
		return alphabet;
	}

	/**
	 * Alter alphabet
	 * 
	 * @param alphabet
	 *            
	 */
	public void setAlphabet(List<String> alphabet) {
		this.alphabet = alphabet;
	}

	/***
	 * Add the received parameter state to the LTS state list
	 * checking first whether the state already exists
	 * 
	 * @param state
	 *            
	 */
	public void addState(State_ state) {
		// verifies whether the state already exists in the set of LTS states
		if (!this.states.contains(state)) {
			this.states.add(state);
		}
	}

	/***
	 * Add the transition to the transition list, add the
	 * transition in alphabet list
	 * 
	 * @param transition
	 */
	public void addTransition(Transition_ transition) {
		// verifies that the transition already exists in the transition list
		if (!this.transitions.contains(transition)) {
			this.transitions.add(transition);
		}

		// add the label in the alphabet list
		this.addToAlphabet(transition.getRotulo());
	}

	/***
	 * Add letter to alphabet
	 * 
	 * @param letter
	 *            
	 */
	public void addToAlphabet(String letter) {
		// verifies whether letter already exists in the alphabet
		if (!this.alphabet.contains(letter)) {
			this.alphabet.add(letter);
		}
	}

	

	/***
	 * Checks whether there is a transition from the initial state and label received from
	 * parameter, and returns all state reached by these transitions
	 * 
	 * @param labelIniState
	 * @param labelTransition
	 * @return resultado, se existe transi��o e quais s�o os estados alcan�ados por
	 *         essas transi��es
	 */
	public Result transitionExists(String labelIniState, String labelTransition) {		
		Result result = new Result();
		// list of reached states
		List<State_> endStates = new ArrayList<State_>();
	
		for (Transition_ t : transitions) {
			// verifies whether the transition contains the iniState of the transition and the
			// label passed parameter
			if (t.getEstadoIni().getNome().toString().equals(labelIniState.toString())
					&& t.getRotulo().toString().equals(labelTransition.toString())) {
				// defines that it has found
				result.setFound(true);
				// adds the status reached
				endStates.add(t.getEstadoFim());
			}
		}

		// changes the achieved states
		result.setReachedStates(endStates);

		// if no state has been reached starting from the state and the last label of
		// parameter
		if (endStates.size() <= 0) {
			// define that did not find any status
			result.setFound(false);
			// the endStates as null
			result.reachedStates = null;
		}

		return result;
	}

	/***
	 * Retrieves the transitions that depart from the state passed by parameter
	 * 
	 * @param state
	 * @return transitionsOfState 
	 */
	public List<Transition_> transitionsByIniState(State_ state) {
		List<Transition_> transitionsOfState = new ArrayList<Transition_>();
		for (Transition_ t : transitions) {
			// verifies that the transition starts from the parameter state
			if (t.getEstadoIni().getNome().equals(state.getNome())) {
				// add transition to list
				transitionsOfState.add(t);
			}
		}

		return transitionsOfState;
	}

	/***
	 * Constructs the deterministic automaton underlying the LTS
	 * 
	 * @return the automaton underlying the LTS
	 */
	public Automaton_ ltsToAutomaton() {
		// create automaton
		Automaton_ as = new Automaton_();
		// changes attributes based on LTS
		as.setStates(this.states);
		as.setInitialState(this.initialState);
		as.setAlphabet(this.alphabet);
		as.setFinalStates(this.states);
		as.setTransitions(Operations.processTauTransition(this.transitions));

		// convert to deterministic
		return Operations.convertToDeterministicAutomaton(as);
	}

	/***
	 * ToString Method Overwrite
	 * 
	 * @return the string describing the LTS
	 */
	@Override
	public String toString() {
		String s = "";
		// initial state
		s += ("##############################\n");
		s += ("           Initial State \n");
		s += ("##############################\n");
		s += ("[" + initialState.getNome() +"]"+ "\n\n");

		// states
		s += ("##############################\n");
		s += ("           States \n");
		s += ("##############################\n");
		s += ("Length: " + this.states.size() + "\n");
		for (State_ e : this.states) {
			s += ("[" + e.getNome() + "]-");
		}

		// transitions
		s += ("\n\n##############################\n");
		s += ("         Transitions\n");
		s += ("##############################\n");
		s += ("Length: " + this.transitions.size() + "\n");
		for (Transition_ t : this.transitions) {
			s += (t.getEstadoIni().getNome() + " - " + t.getRotulo() + " - " + t.getEstadoFim().getNome() + "\n");
		}

		// alphabet
		s += ("\n##############################\n");
		s += ("         Alphabet\n");
		s += ("##############################\n");
		s+="[";
		for (String t : this.alphabet) {
			s += (t + " - ");
		}
		s += "]\n";

		return s;
	}

	/***
	 * Class Result used to verify that transitions exist starting from
	 * of certain state and label
	 * 
	 * @author camila
	 *
	 */
	public class Result {
		// if found transition
		private boolean found;
		// the states achieved by the transition
		private List<State_> reachedStates;

		/***
		 * Alter found
		 * 
		 * @param found
		 *            
		 */
		public void setFound(boolean found) {
			this.found = found;
		}

		/***
		 * Return list of reached states 
		 * 
		 * @return reachedStates
		 */
		public List<State_> getReachedStates() {
			return reachedStates;
		}

		/***
		 * Alter list of reached states 
		 * 
		 * @param reachedStates
		 *            
		 */
		public void setReachedStates(List<State_> reachedStates) {
			this.reachedStates = reachedStates;
		}

		/***
		 * Return whether found
		 * 
		 * @return found 
		 */
		public boolean getFound() {
			return found;
		}
		
	
	}

}
