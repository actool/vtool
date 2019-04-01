/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.Constants;

/**
 *Class Automaton_
 * @author Camila
 */
public class Automaton_ extends LTS {
	
	private List<State_> finalStates;

	/***
	 * Empty constructor initializes final state list
	 */
	public Automaton_() {
		finalStates = new ArrayList<State_>();
	}

	/**
	 * Returns the final states of the automaton
	 * @return finalStates
	 */
	public List<State_> getFinalStates() {
		return finalStates;
	}

	/** Alter the final states of the automaton
	 * @param finalStatesthe set of final states
	 *            
	 */
	public void setFinalStates(List<State_> finalStates) {
		this.finalStates = finalStates;
	}

	/***
	 * Add the state to the list of final states, checks whether the state has already been added
	 * @param state the state to be added in the list of final states
	 * 
	 */
	public void addFinalStates(State_ state) {
		//checks whether the state has already been added, based on the state name
		if (!this.finalStates.contains(state)) {
			this.finalStates.add(state);
		}
	}

	
	/***
	 * Returns the states reached from the received parameter state with the epsilon label
	 * @param state
	 * @return the states reached with epsilon transition
	 */
	public List<State_> reachableStatesWithEpsilon(State_ state) {
		//states reached with epsilon
		List<State_> reachedStates = new ArrayList<State_>();
		//auxiliary list to check stopping condition
		List<State_> aux = new ArrayList<State_>();
		//add the state received as parameter in the list to be the first to be visited
		aux.add(state);
		Result r;
		//to explore list
		State_ current;
		
		//while there are states to be explored in the list
		while (aux.size() > 0) {
			//the current one receives the first element from the list
			current = aux.remove(0);
			//if the transition from the current state with epsilon label exists
			r = transitionExists(current.getNome(), Constants.EPSILON);

			//transition with epsilon was found
			if (r.getFound()) {
				// reachedStates are the states reached with the epsilon transition from the current state
				// the current state is added in the list of states reached by epsilon, since epsilon can remain in the same state
				r.getReachedStates().add(current);
			} else {
				// if no state is reached by epsilon
				// add the current state as being reached by epsilon
				r.setReachedStates(new ArrayList<State_>(Arrays.asList(current)));
			}
			//in the list contains only the states that are not in "reachedStates"
			ArrayList<State_> distinctStates = new ArrayList<State_>(r.getReachedStates());
			distinctStates.removeAll(reachedStates);
			
			//add in the auxiliary list the states reached by the current state with epsilon
			aux = Stream.concat(distinctStates.stream(), aux.stream()).distinct().collect(Collectors.toList());

			//add in the reachedStates list the states reached by the current state with epsilon 
			reachedStates = Stream.concat(distinctStates.stream(), reachedStates.stream()).distinct()
					.collect(Collectors.toList());
		}

		return reachedStates;
	}

	/***
	 * Returns the states that have epsilon transition starting from it
	 * @return list of states containing epsilon transition
	 */
	public List<State_> getStatesWithEpsilonTransition() {		
		List<State_> statesWithEpsilonTransition = new ArrayList<State_>();
		//visit all transitions of automaton
		for (Transition_ transition : getTransitions()) {
			//if the transition has an epsilon label
			if(transition.getRotulo().equals(Constants.EPSILON)) {
				//add transition to return
				statesWithEpsilonTransition.add(transition.getEstadoIni());
			}
		}
		
		return statesWithEpsilonTransition;
	}

	/***
	 * verify whether the automaton is deterministic based on its transitions
	 * 
	 * @return whether the automaton is deterministic or not
	 */
	public boolean isDeterministic() {
		// counter of repeated transitions
		int cont = 0;
		
		if (alphabet.contains(Constants.EPSILON)) {
			// if the alphabet contains epsilon then the automato is not deterministic
			return false;
		} else {
			// visit all transitions
			for (Transition_ currentTransition : transitions) {
				// for each transition the transition counter starts with 0
				cont = 0;
				// visit all transitions
				for (Transition_ t : transitions) {
					// verifies that the current transition is equal to some LTS transition, based on the
					// initial state and label of both transitions
					if (t.getEstadoIni().getNome().equals(currentTransition.getEstadoIni().getNome())
							&& t.getRotulo().equals(currentTransition.getRotulo())) {
						cont++;
					}

					// if there is more than one (itself and any other) transition equal to the current transition
					if (cont > 1) {
						// it is not deterministic
						return false;
					}
				}

			}
			// if no repeated transition was found then it is deterministic
			return true;
		}

	}
	
	/***
	 * Overwriting the automato's toString method to list the attributes contained in the LTS and the automato's final states
	 * @return returns the string describing the automaton
	 */
	@Override
	public String toString() {
		//toString da classe LTS
		String s = super.toString();
		//descrição referente aos estados finais do automato
		s += ("##############################\n");
		s += ("           Final States \n");
		s += ("##############################\n");
		s += ("Quantidade: " + this.finalStates.size() + "\n");
		for (State_ e : this.finalStates) {
			s += ("[" + e.getNome() + "] - ");
		}
		return s;
	}

}
