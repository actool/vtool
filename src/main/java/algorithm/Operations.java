/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Optional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import javafx.util.Pair;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import model.Automaton_;
import model.State_;
import model.IOLTS;
import model.LTS;
import model.Transition_;
import util.Constants;
import util.ModelImageGenerator;

/**
 * Class Operations contain complement, determinism, union, intersection
 * 
 * @author Camila
 */
public class Operations {

	/***
	 * completes the automaton received by parameter
	 * 
	 * @param Q
	 *            automaton
	 * @return acomp complet automaton
	 */
	public static Automaton_ complement(Automaton_ Q) {
		Automaton_ acomp = new Automaton_();
		// define the initial state
		acomp.setInitialState(Q.getInitialState());
		// define o alphabet
		acomp.setAlphabet(Q.getAlphabet());
		// completes the received automaton by parameter
		State_ scomp = new State_("complement");
		// add states to automato, gives "new" to not paste reference
		acomp.setStates(new ArrayList<State_>(Q.getStates()));

		// add self loop in scomp acceptance state, with all labels
		for (String a : acomp.getAlphabet()) {
			acomp.addTransition(new Transition_(scomp, a, scomp));
		}

		// add the state to be used to complete
		acomp.addState(scomp);
		acomp.addFinalStates(scomp);

		// reverses who was final state cases to be, who was was not becomes final
		// state
		for (State_ e : acomp.getStates()) {
			// checks whether the state is already in the list of end states
			if (!Q.getFinalStates().contains(e)) {
				acomp.addFinalStates(e);
			}
		}

		List<State_> result;

		// completes states that are not input / output complete
		for (State_ e : Q.getStates()) {
			for (String l : acomp.getAlphabet()) {
				// checks whether there is a transition from state "e" with the label "l"
				result = Q.transitionExists(e.getName(), l);
				// if there are no transitions, you must complete by creating a new transition
				// from state "e" to state "scomp" with the label "l"
				if (result.size() == 0) {
					acomp.addTransition(new Transition_(e, l, scomp));
				} else {
					// when no deterministic, so starting from "e" with "l" can
					// reach more than one end state, so a transition for each found state must be
					// created
					for (State_ estadoFim : result) {
						acomp.addTransition(new Transition_(e, l, estadoFim));
					}
				}
			}

		}

		return acomp;
	}

	/***
	 * Converts TAU transitions into EPSILON transition
	 * 
	 * @param transitions
	 * 
	 * @return as transitions without the TAU label
	 */
	public static List<Transition_> processTauTransition(List<Transition_> transitions) {
		List<Transition_> newTransitions = new ArrayList<Transition_>();
		// treats transitions when TAU in LTS converts to EPSILON in automato
		for (Transition_ t : transitions) {
			if (t.getLabel().equals(Constants.TAU)) {
				newTransitions.add(new Transition_(t.getIniState(), Constants.EPSILON, t.getEndState()));
			} else {
				newTransitions.add(t);
			}
		}
		return newTransitions;
	}

	/***
	 * It transforms the nondeterministic automaton into deterministic, removing the
	 * EPSILON transitions and transitions starting from the same state with the
	 * same label
	 * 
	 * @param automaton
	 *            a ser determinizado
	 * @return automato deterministico
	 */
	public static Automaton_ convertToDeterministicAutomaton(Automaton_ automaton) {

		// verifies whether the automaton is already deterministic
		if (!automaton.isDeterministic()) {
			// create new deterministic automaton
			Automaton_ deteministic = new Automaton_();
			// the initial state of the deterministic automaton are all states reached from
			// the initial state of the
			// automaton received by parameter with the transition epsilon, the name of the
			// initial state is therefore the
			// union of all states reached separated by separator1
			String stateName = automaton.reachableStatesWithEpsilon(automaton.getInitialState()).stream()
					.map(x -> x.getName()).collect(Collectors.joining(Constants.SEPARATOR));

			// define the initial state of deterministic automaton
			deteministic.setInitialState(new State_(stateName.replace(Constants.SEPARATOR, "")));
			// null state, used when no state is reached
			State_ nullState = new State_(Constants.EMPTY + Constants.SEPARATOR);
			// list which defines the stop condition and serves to go through the states of
			// the automato
			List<String> aux = new ArrayList<String>(Arrays.asList(stateName));
			// copy of the aux list, since elements are removed from the aux list,
			// and therefore a list is needed to store the states already visited
			List<String> auxCopy = new ArrayList<String>(aux);
			// list of states reached
			List<State_> auxState = new ArrayList<State_>();
			// used to know which states make each state:
			// (state1@state2) a state composed of two synchronized states
			String[] stringState;
			List<State_> result;
			State_ state;
			List<String> finalStates = new ArrayList<String>();

			// checks whether the initial state is final state
			stringState = stateName.split(Constants.SEPARATOR);
			for (String s : stringState) {
				auxState.add(new State_(s));
			}

			if (!Collections.disjoint(automaton.getFinalStates(), auxState)) {
				finalStates.add(stateName.replaceAll(Constants.SEPARATOR, ""));
			}

			// remove EPSILON from the alphabet
			automaton.getAlphabet().remove(Constants.EPSILON);
			// while there are states to be covered
			while (aux.size() > 0) {
				// current state is the first list state
				stringState = aux.remove(0).split(Constants.SEPARATOR);
				// add the state removed from the auxiliary list to the list of automato states
				deteministic.addState(new State_(String.join("", stringState)));

				for (String alphabet : automaton.getAlphabet()) {
					// initialize aux list
					auxState = new ArrayList<State_>();
					// for each state that is composed the state. ex: (state1 @ state2) in this case
					// there are two states
					for (int i = 0; i < stringState.length; i++) {
						// check if the transition exists from the stringState[i] with the word
						// "alphabet"
						result = automaton.transitionExists(stringState[i], alphabet);
						// if there is a transition
						if (result.size() > 0) {
							// for each state reached by the transitions found
							for (State_ estadosFim : result) {
								// add the reachable states with EPSILON from the states
								// found (result.getFetStates ())
								auxState.addAll(automaton.reachableStatesWithEpsilon(estadosFim));
							}
						}
					}

					// if there are states reached from stateString with the label "alphabet"
					if (auxState.size() > 0) {
						// remove possible duplicate states
						Set<String> set = new HashSet<>(auxState.size());
						auxState.removeIf(p -> !set.add(p.getName()));
						// orders the states reached so that there is no possibility of considering, for
						// example,
						// that the state "ab" is different from "ba"
						auxState.sort(Comparator.comparing(State_::getName));
						// the name of the state reached is the union of all states reached separated by
						// the separator
						stateName = auxState.stream().map(x -> x.getName())
								.collect(Collectors.joining(Constants.SEPARATOR));

						// verifies if the state reached is a final state, if one of the states
						// reached is final state in the original automaton received by parameter
						if (!Collections.disjoint(automaton.getFinalStates(), auxState)
								&& !finalStates.contains(stateName.replaceAll(Constants.SEPARATOR, ""))) {
							finalStates.add(stateName.replaceAll(Constants.SEPARATOR, ""));
						}
//					} else {
//						// if no state is reached from stateString with the label "alphabet"
//						// then consider that starting from stateString with the label "alphabet"
//						// arrives in the "nullState" state
//						stateName = nullState.getName();
//					}
					// creates the state reached
					state = new State_(stateName);
					// add this state to automaton
					deteministic.addState(new State_(state.getName().replaceAll(Constants.SEPARATOR, "")));
					// adds the new transition from the "StateString" with the "alphabet" label to
					// the "state"
					deteministic.addTransition(new Transition_(new State_(String.join("", stringState)), alphabet,
							new State_(state.getName().replaceAll(Constants.SEPARATOR, ""))));

					// if the reached state is not in the list auxCopy should add it to explore
					// later
					if (!auxCopy.contains(state.getName())) {
						aux.add(stateName);
						auxCopy.add(stateName);
					}
				}
				}
			}

			// adds the final states to the deterministic automaton
			for (String nomeEstadoFinal : finalStates) {
				deteministic.addFinalStates(new State_(nomeEstadoFinal));
			}

			return deteministic;
		}

		// when the automaton received by parameter is already deterministic, it returns
		// itself
		return automaton;
	}

	/***
	 * receives as parameter two automata and returns the automaton resulting from
	 * the intersection between them.
	 * 
	 * @param Q
	 *            automaton
	 * @param S
	 *            automaton
	 * @return automaton result of the intersection between the automata Q and S
	 */
	public static Automaton_ intersection(Automaton_ Q, Automaton_ S) {
		// intersection automato
		Automaton_ Ar = new Automaton_();
		// add alphabet to automaton
		Ar.setAlphabet(Q.getAlphabet());
		// creates and defines the initial state of the automaton as the synchronization
		// of the initial states of S and Q
		String nameSyncState = S.getInitialState().getName() + Constants.SEPARATOR + Q.getInitialState().getName();
		// remove the name of the state
		State_ r0 = new State_(nameSyncState.replaceAll(Constants.SEPARATOR, ""));
		// used for split
		r0.setInfo(nameSyncState);
		// set initial state
		Ar.setInitialState(r0);
		// add initial state to list of state
		Ar.addState(r0);

		// list of synchronized states that will be visited and removed while the
		// algorithm runs
		List<State_> synchronizedStates = new ArrayList<State_>();
		// add state r0 to list of states
		synchronizedStates.add(r0);
		// variables for synchronization of the states of S and Q
		String s = null, q = null;
		State_ removed = null, synchronized_ = null;
		String[] part;
		List<State_> sTransitions, qTransitions;

		// while there are states to be synchronized
		while (synchronizedStates.size() > 0) {
			// emoves the first element from the list of states to be synchronized
			removed = synchronizedStates.remove(0);

			// as the state name is (s, q), splits the string to find the 's' and 'q'
			part = removed.getInfo().split(Constants.SEPARATOR);
			s = part[0];
			q = part[1];

			// check for transitions in S and Q that synchronize
			for (String l : Ar.getAlphabet()) {
				// if there exists in S transitions starting from state "s" with the label "l"
				sTransitions = S.transitionExists(s, l);
				// if there exists in Q transitions from state "q" with the label "l"
				qTransitions = Q.transitionExists(q, l);
				// synchronizes in Q and S
				if (sTransitions.size() > 0 && qTransitions.size() > 0) {
					// synchronized states in S, there may be more than one in case of no
					// determinism
					for (State_ endStateS : sTransitions) {
						// synchronized states in Q, there may be more than one in case of
						// non-determinism
						for (State_ endStateQ : qTransitions) {
							// creates a new state that matches what was synced
							nameSyncState = endStateS.getName() + Constants.SEPARATOR + endStateQ.getName();
							// the state name is without the separator
							synchronized_ = new State_(nameSyncState.replaceAll(Constants.SEPARATOR, ""));
							// description of the state with the separator
							synchronized_.setInfo(nameSyncState);
							if (!Ar.getStates().contains(synchronized_)) {
								synchronizedStates.add(new State_(synchronized_));
							}

							// adds the created state to the automaton
							synchronized_.setName(synchronized_.getName().replaceAll(Constants.SEPARATOR, ""));
							Ar.addState(synchronized_);
							// adds the transition that corresponds to synchronization
							Ar.addTransition(new Transition_(removed, l, synchronized_));
						}
					}
				}
			}
		}
		part = null;
		s = "";
		q = "";
		List<State_> finalStateQ = new ArrayList<State_>(Q.getFinalStates());
		List<State_> finalStateS = new ArrayList<State_>(S.getFinalStates());

		// synchronized states to check which end states
		for (State_ state : Ar.getStates()) {
			// from the getInfo it is possible to separate the synchronized states
			part = state.getInfo().split(Constants.SEPARATOR);
			s = part[0];
			q = part[1];
			// verifies whether the synchronized states were final states in Q and S
			if (finalStateS.contains(new State_(s)) && finalStateQ.contains(new State_(q))) {
				Ar.addFinalStates(state);
			}
		}
		return Ar;
	}

	/***
	 * receives as parameter two automata and returns the automaton resulting from
	 * the union between them.
	 * 
	 * @param Q
	 *            automaton
	 * @param S
	 *            automaton
	 * @return automaton result of the union between the automata Q and S
	 */
	public static Automaton_ union(Automaton_ Q, Automaton_ S) {
		// automato union
		Automaton_ Ay = new Automaton_();

		// the transitions of the union automaton, is the union of the transitions of Q
		// and S
		Set<Transition_> transicoes = new HashSet<Transition_>();
		transicoes.addAll(Q.getTransitions());
		transicoes.addAll(S.getTransitions());
		Ay.setTransitions(new ArrayList<Transition_>(transicoes));

		// the states of the union automata, is the union of the states of Q and S
		Set<State_> estados = new HashSet<State_>();
		estados.addAll(Q.getStates());
		estados.addAll(S.getStates());
		Ay.setStates(new ArrayList<State_>(estados));

		// the final states of the union automaton, is the union of the final states of
		// Q and S
		Set<State_> estadosFinais = new HashSet<State_>();
		estadosFinais.addAll(Q.getFinalStates());
		estadosFinais.addAll(S.getFinalStates());
		Ay.setFinalStates(new ArrayList<State_>(estadosFinais));

		// the alphabet of the union automata, is the union of the alphabet of Q and S
		Set<String> alfabeto = new HashSet<String>(Q.getAlphabet());
		alfabeto.addAll(S.getAlphabet());
		Ay.setAlphabet(new ArrayList<String>(alfabeto));

		// to union the automaton is created a new state that will be the initial state
		State_ estado = new State_("init");
		// add the inital state to list of states
		Ay.addState(estado);
		// definir as initial state
		Ay.setInitialState(estado);
		// add the transitions from the initial state with the label EPSILON to the
		// initial state of S and of Q
		Ay.addTransition(new Transition_(estado, Constants.EPSILON, S.getInitialState()));
		Ay.addTransition(new Transition_(estado, Constants.EPSILON, Q.getInitialState()));

		return convertToDeterministicAutomaton(Ay);
	}

	/***
	 * Using the brics library converts the regex into Automaton
	 * 
	 * @param regex
	 * @param tag
	 *            string that will serve to name the states, to distinguish the
	 *            states of D and F because the library creates states named "1",
	 *            "2", "3" and therefore there may be conflict going forward
	 * @return automaton which accepts the regex received by parameter
	 */
	public static Automaton_ regexToAutomaton(String linguagemRegular, String tag) {
		// regex to automaton
		RegExp regExp = new RegExp(linguagemRegular, RegExp.ALL);
		Automaton automaton = regExp.toAutomaton();
		return AutomatonBricsInAutomaton_(automaton, tag);
	}

	/***
	 * Converts the Automaton object generated by the brics library in the Automato
	 * object of this project
	 * 
	 * @param a
	 *            brics library automaton
	 * @param tag
	 *            string that will serve to name the states, to distinguish the
	 *            states of D and F because the library creates states named "1",
	 *            "2", "3" and therefore there may be conflict going forward
	 * @return Automaton built according to parameter Automaton
	 */
	public static Automaton_ AutomatonBricsInAutomaton_(Automaton a, String tag) {
		// automaton of return
		Automaton_ automaton = new Automaton_();

		// states of brics Automaton
		Set<State> states = a.getStates();

		for (State state : states) {
			// ini state
			State_ iniState = getBricsState(state, tag);
			// add state to automaton_
			automaton.addState(iniState);
			// get all transitions from state
			Set<Transition> transitions = state.getTransitions();
			for (Transition t : transitions) {
				// add transition to automaton_
				for (Transition_ transition : getBricsTransition(iniState, t, tag)) {
					automaton.addTransition(transition);
				}
			}
		}

		// final states
		for (State finalState : a.getAcceptStates()) {
			automaton.addFinalStates(getBricsState(finalState, tag));
		}

		// define the initial state
		automaton.setInitialState(getBricsState(a.getInitialState(), tag));

		return automaton;

	}

	/***
	 * Retrieves Automaton transitions based on the iniState, the brics library does
	 * not allow direct access to the transition label so the value passed through
	 * the toString
	 * 
	 * @param iniState
	 * @param transition
	 * @param tag
	 *            used to name the state
	 * @return transitions list of iniState
	 */
	public static List<Transition_> getBricsTransition(State_ iniState, Transition transition, String tag) {
		String string = transition.toString();
		String[] parts = string.split("->");
		String[] transicoes = parts[0].split("-");
		List<Transition_> listaTransicoes = new ArrayList<Transition_>();
		for (String t : transicoes) {
			listaTransicoes.add(new Transition_(iniState, t.replaceAll("\\s", ""),
					new State_(tag + parts[1].replaceAll("\\s+", ""))));
		}

		return listaTransicoes;
	}

	/***
	 * Retrieves the state given the parameter State, because the brics library does
	 * not allow to directly access the label of the state, then used here the value
	 * passed by the toString
	 * 
	 * @param state
	 * @param tag
	 *            used to name the state
	 * @return state
	 */
	public static State_ getBricsState(State state, String tag) {
		String stringState = state.toString();
		int idxInitial = stringState.indexOf("state") + "state".length();
		int idxFinal = stringState.indexOf('[');
		stringState = stringState.substring(idxInitial, idxFinal).replaceAll("\\s+", "");
		return new State_(tag + stringState);
	}

	static volatile State_ current;

	public static List<String> getWordsFromAutomaton(Automaton_ a, boolean ioco) {

		String word = "";
		String tagWord = " , ";
		String tagLetter = " -> ";
		List<State_> stateToVisit = new ArrayList<State_>();
		stateToVisit.add(a.getInitialState());
		a.getStates().forEach(p -> p.setInfo(null));
		a.getStates().forEach(p -> p.setVisited(false));

		State_ endState, iniState;
		String[] aux;
		List<String> words = new ArrayList<String>();
		List<String> news = new ArrayList<String>();

		while (!stateToVisit.isEmpty()) {
			current = stateToVisit.remove(0);
			current = a.getStates().stream().filter(x -> x.equals(current)).findFirst().orElse(null);
			if (!current.isVisited()) {
				current.setVisited(true);
				List<Transition_> transicoes = a.transitionsByIniState(current);
				for (Transition_ t : transicoes) {
					news = new ArrayList<String>();
					iniState = a.getStates().stream().filter(x -> x.equals(t.getIniState())).findFirst().orElse(null);
					endState = a.getStates().stream().filter(x -> x.equals(t.getEndState())).findFirst().orElse(null);
					word = "";
					if (iniState.getInfo() != null) {
						aux = iniState.getInfo().split(tagWord);
						if (aux.length > 0) {
							for (int i = 0; i < aux.length; i++) {
								word = aux[i] + tagLetter + t.getLabel() + tagWord;
								news.add(word);
							}
						} else {
							word = iniState.getInfo() + tagLetter + t.getLabel() + tagWord;
							news.add(word);
						}

					} else {
						word = t.getLabel() + tagWord;
						news.add(word);
					}

					for (String p : news) {
						word = (endState.getInfo() != null ? (endState.getInfo()) : "") + p;
						endState.setInfo(word);
					}

					if (!endState.isVisited()) {
						stateToVisit.add(endState);
					}

				}
			}
		}

		for (State_ e : a.getStates()) {
			if (a.getFinalStates().contains(e)) {
				if (e.getInfo() != null) {
					aux = e.getInfo().split(tagWord);
					for (int i = 0; i < aux.length; i++) {
						words.add(aux[i]);
					}
				}
			}
		}

		return words;
	}

	/***
	 * Checks the conformance of the automaton and when it does not conform returns
	 * a word that shows the nonconformity
	 * 
	 * @param a
	 *            automaton
	 * @return whether it conforms or not and the word that proves the
	 *         non-conformity
	 */
	public static String veredict(Automaton_ a) {
		if (a.getFinalStates().size() > 0) {
			return Constants.MSG_NOT_CONFORM;
		} else {
			return Constants.MSG_CONFORM;
		}
	}

	public static State_ getStateByName(IOLTS S, String stateName) {
		return S.getStates().stream().filter(x -> x.getName().equals(stateName)).findFirst().orElse(new State_());
	}

	public static Object[] path(IOLTS S, boolean ioco, String testCase, String model) {
		List<State_> end = new ArrayList<>();
		List<State_> stateList = new ArrayList<>();
		String path_aux = "";
		List<String> specOut;
		IOLTS S_ = null;
		List<State_> previous_states = new ArrayList<State_>();
		List<State_> current_states = new ArrayList<State_>();

		Map<State_, String> r_list = new HashMap<>();
		String r;
		Map<State_, String> up = new HashMap<>();

		try {
			S_ = (IOLTS) S.clone();
			getStateByName(S_, S_.getInitialState().getName()).setInfo(S_.getInitialState().getName() + " -> ");
		} catch (Exception e) {
		}

		Map<String, List<State_>> map = new HashMap<String, List<State_>>();

		List<String> alphabet = new ArrayList();
		alphabet.addAll(S.getOutputs());
		alphabet.addAll(S.getInputs());
		HashSet hashSet_s_ = new LinkedHashSet<>(alphabet);
		alphabet = new ArrayList<>(hashSet_s_);
		for (State_ s : S_.getStates()) {
			for (String l : alphabet) {// S_.getAlphabet()
				map.put(s + Constants.SEPARATOR + l, S_.transitionExists(s.getName(), l));
			}
		}

		boolean break_conditional = false;
		previous_states = new ArrayList<State_>();

		S_.getStates().stream().forEach(x -> x.setInfo(null));
		getStateByName(S_, S_.getInitialState().getName()).setInfo(S_.getInitialState().getName() + " -> ");
		previous_states.add(getStateByName(S_, S_.getInitialState().getName()));

		// break_conditional = false;
		for (String p : testCase.split(" -> ")) {

			up = new HashMap<>();

			if (!r_list.containsKey(S_.getInitialState())) {
				r_list.put(S_.getInitialState(), S_.getInitialState().toString() + " -> ");
			}

			for (State_ state_ : previous_states) {

				current_states = map.get(state_ + Constants.SEPARATOR + p);

				if (current_states == null || current_states.size() == 0) {// current_states == null ||

					if (!r_list.get(state_).contains("there are no transitions")) {
						r = r_list.get(state_) + "[there are no transitions of " + state_.getName() + " with label " + p
								+ " ] -> ";
						up.put(state_, r);
					} else {
						up.put(state_, r_list.get(state_));
					}

					// break_conditional = true;
				} else {

					for (State_ s : current_states) {
						if (!r_list.get(state_).contains("there are no transitions")) {
							stateList = new ArrayList<>();
							stateList.addAll(current_states);
							if (r_list.containsKey(state_)) {
								r = r_list.get(state_) + s.toString() + " ->";
								up.put(s, r);
							} else {
								r = s.toString() + " ->";
								up.put(s, r);
							}

						} else {
							up.put(state_, r_list.get(state_));
						}
					}
				}
			}

			end = new ArrayList<>();
			for (Map.Entry<State_, String> pair : up.entrySet()) {
				r_list.put(pair.getKey(), pair.getValue());
				end.add(pair.getKey());
			}

			previous_states = end;

		}

		String a = "";
		List<String> outputs = new ArrayList<>();

		for (State_ s : end) {
			a += "\n\t path:" + r_list.get(s);
			if (ioco && !r_list.get(s).contains("there are no transitions")) {
				specOut = S_.outputsOfState(s);
				a += "\n\t output: " + specOut + "\n";
				outputs.addAll(specOut);
			} /*
				 * else { if (r_list.get(s).contains("there are no transitions")) { a +=
				 * "\n\t output: \n"; } }
				 */
		}

		if (ioco) {
			hashSet_s_ = new LinkedHashSet<>(outputs);
			outputs = new ArrayList<>(hashSet_s_);

			path_aux += "\n" + model + " outputs: " + outputs + "\n";
		} else {
			path_aux += "\n" + model;
		}
		path_aux += a;

		return new Object[] { end, path_aux, outputs };// new Pair<>(end, path_aux);// path_aux;
	}

	public static List<String> getTestCases(Automaton_ faultModel, boolean ioco, IOLTS iolts_s) {
		List<String> testCases_testSuit = getWordsFromAutomaton(faultModel, ioco);
		List<State_> tc = new ArrayList<>();
		List<State_> fs = new ArrayList<>();
		for (String t : testCases_testSuit) {
			tc.addAll((List<State_>) path(iolts_s, ioco, t, "\nModel")[0]);
		}
		HashSet hashSet_s_ = new LinkedHashSet<>(tc);
		tc = new ArrayList<>(hashSet_s_);
		
		// create automaton
		Automaton_ automaton_s = new Automaton_();
		// changes attributes based on LTS
		automaton_s.setStates(iolts_s.getStates());
		automaton_s.setInitialState(iolts_s.getInitialState());
		automaton_s.setAlphabet(iolts_s.getAlphabet());
		
		automaton_s.setTransitions(Operations.processTauTransition(iolts_s.getTransitions()));
		automaton_s.setFinalStates(tc);
		
	
		hashSet_s_ = new LinkedHashSet<>(getWordsFromAutomaton(automaton_s, ioco));
		return new ArrayList<>(hashSet_s_);

	}

	/***
	 * 
	 * @param S
	 *            specification model
	 * @param I
	 *            implementation model
	 * @param fault
	 *            model containing words that detect implementation failure
	 * @return the paths covered by the test cases by implementation and
	 *         specification
	 */
	public static String path(LTS S, LTS I, Automaton_ faultModel, boolean ioco) {

		IOLTS iolts_s = new IOLTS(S);
		IOLTS iolts_i = new IOLTS(I);

		iolts_s.setInputs(iolts_s.getAlphabet());
		iolts_s.setOutputs(new ArrayList<String>());
		iolts_i.setInputs(iolts_i.getAlphabet());
		iolts_i.setOutputs(new ArrayList<String>());

		List<String> testCases = getWordsFromAutomaton(faultModel, ioco);
		// List<String> testCases = getTestCases(faultModel, ioco, iolts_s);

		String path = "";
		for (String t : testCases) {
			path += "Test case: \t" + t;
			path += path(iolts_s, ioco, t, "\nModel")[1];
			path += path(iolts_i, ioco, t, "\nImplementation")[1];
			path += "\n################################################################## \n";
		}

		return path;
	}

	/***
	 * 
	 * @param S
	 *            specification model
	 * @param I
	 *            implementation model
	 * @param fault
	 *            model containing words that detect implementation failure
	 * @return the paths covered by the test cases by implementation and
	 *         specification
	 */
	public static String path(IOLTS S, IOLTS I, Automaton_ fault, boolean ioco) {
		// List<String> testCases = getWordsFromAutomaton(fault, ioco);
		List<String> testCases = getTestCases(fault, ioco, S);
		State_ currentState_s;
		State_ currentState_i;

		String path = "";

		List<String> implOut, specOut;

		// verify if initial states results in non ioco, with test case ''
		if (ioco) {
			currentState_s = S.getInitialState();
			currentState_i = I.getInitialState();
			implOut = I.outputsOfState(currentState_i);
			specOut = S.outputsOfState(currentState_s);

			if (!specOut.containsAll(implOut) && !(ioco && specOut.size() == 0)) {// (ioco && specOut.size() == 0) ->
																					// when the test case can not be run
																					// completely
				path = "Test case: \t" + "" + "\n";
				path += "Implementation output: " + implOut + " \n\t path: " + currentState_i.getName()
						+ "\n\t output: " + I.outputsOfState(currentState_i);
				path += "\nModel output: " + specOut + " \n\t path:" + currentState_s.getName() + "\n\t output: "
						+ S.outputsOfState(currentState_s);
				path += "\n################################################################## \n";

			}
		}

		for (String t : testCases) {
			Object[] result_s = path(S, ioco, t, "\nModel");
			Object[] result_i = path(I, ioco, t, "\nImplementation");
			ArrayList<String> out_s = (ArrayList<String>) result_s[2];
			ArrayList<String> out_i = (ArrayList<String>) result_i[2];

			if (!out_s.containsAll(out_i) && !(ioco && out_s.size() == 0)) {
				path += "Test case: \t" + t;
				path += result_s[1];
				path += result_i[1];
				path += "\n################################################################## \n";
			}
		}

		return path;
	}

}
