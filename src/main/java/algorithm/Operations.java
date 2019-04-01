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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import model.Automaton_;
import model.State_;
import model.IOLTS;
import model.LTS;
import model.Transition_;
import model.LTS.Result;
import util.Constants;

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
		State_ scomp = new State_("complemento");
		// add states to automato, gives "new" to not paste reference
		acomp.setStates(new ArrayList<State_>(Q.getStates()));

		// add self loop in scomp acceptance state, with all labels
		for (String a : acomp.getAlphabet()) {
			acomp.addTransition(new Transition_(scomp, a, scomp));
		}

		// add the state to be used to complete
		acomp.addState(scomp);

		// reverses who was final state ceases to be, who was was not becomes final state
		for (State_ e : acomp.getStates()) {
			// checks whether the state is already in the list of end states
			if (!Q.getFinalStates().contains(e)) {
				acomp.addFinalStates(e);
			}
		}

		// completes states that are not input / output complete
		for (State_ e : Q.getStates()) {
			for (String l : acomp.getAlphabet()) {
				// checks whether there is a transition from state "e" with the label "l"
				Result result = Q.transitionExists(e.getNome(), l);
				// if there are no transitions, you must complete by creating a new transition from state "e" to state "scomp" with the label "l"
				if (!result.getFound()) {
					acomp.addTransition(new Transition_(e, l, scomp));
				} else {
					// when no deterministic, so starting from "e" with "l" can 
					// reach more than one end state, so a transition for each found state must be created
					for (State_ estadoFim : result.getReachedStates()) {
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
			if (t.getRotulo().equals(Constants.TAU)) {
				newTransitions.add(new Transition_(t.getEstadoIni(), Constants.EPSILON, t.getEstadoFim()));
			} else {
				newTransitions.add(t);
			}
		}
		return newTransitions;
	}

	/***
	 * It transforms the nondeterministic automaton into deterministic, 
	 * removing the EPSILON transitions and transitions starting from the same state with the same label
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
			//the initial state of the deterministic automaton are all states reached from the initial state of the 
			//automaton received by parameter with the transition epsilon, the name of the initial state is therefore the 
			//union of all states reached separated by separator1
			String stateName = automaton.reachableStatesWithEpsilon(automaton.getInitialState()).stream()
					.map(x -> x.getNome()).collect(Collectors.joining(Constants.SEPARATOR));

			// define the initial state of deterministic automaton
			deteministic.setInitialState(new State_(stateName.replace(Constants.SEPARATOR, "")));
			// null state, used when no state is reached
			State_ nullState = new State_(Constants.EMPTY + Constants.SEPARATOR);
			// list which defines the stop condition and serves to go through the states of the automato
			List<String> aux = new ArrayList<String>(Arrays.asList(stateName));
			// copy of the aux list, since elements are removed from the aux list, 
			// and therefore a list is needed to store the states already visited
			List<String> auxCopy = new ArrayList<String>(aux);
			// list of states reached
			List<State_> auxState = new ArrayList<State_>();
			// used to know which states make each state:
			// (state1@state2) a state composed of two synchronized states
			String[] stringState;
			Result result;
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
					//initialize aux list
					auxState = new ArrayList<State_>();
					// for each state that is composed the state. ex: (state1 @ state2) in this case there are two states
					for (int i = 0; i < stringState.length; i++) {
						// check if the transition exists from the stringState[i] with the word "alphabet"
						result = automaton.transitionExists(stringState[i], alphabet);
						// if there is a transition
						if (result.getFound()) {
							// for each state reached by the transitions found
							for (State_ estadosFim : result.getReachedStates()) {
								// add the reachable states with EPSILON from the states
								//found (result.getFetStates ())
								auxState.addAll(automaton.reachableStatesWithEpsilon(estadosFim));
							}
						}
					}

					// if there are states reached from stateString with the label "alphabet"
					if (auxState.size() > 0) {
						// remove possible duplicate states
						Set<String> set = new HashSet<>(auxState.size());
						auxState.removeIf(p -> !set.add(p.getNome()));
						// orders the states reached so that there is no possibility of considering, for example, 
						// that the state "ab" is different from "ba"
						auxState.sort(Comparator.comparing(State_::getNome));
						// the name of the state reached is the union of all states reached separated by the separator
						stateName = auxState.stream().map(x -> x.getNome())
								.collect(Collectors.joining(Constants.SEPARATOR));

						// verifies if the state reached is a final state, if one of the states
						// reached is final state in the original automaton received by parameter
						if (!Collections.disjoint(automaton.getFinalStates(), auxState)
								&& !finalStates.contains(stateName.replaceAll(Constants.SEPARATOR, ""))) {
							finalStates.add(stateName.replaceAll(Constants.SEPARATOR, ""));
						}
					} else {
						// if no state is reached from stateString with the label "alphabet" 
						//then consider that starting from stateString with the label "alphabet" arrives in the "nullState" state
						stateName = nullState.getNome();
					}
					// creates the state reached
					state = new State_(stateName);
					// add this state to automaton
					deteministic.addState(new State_(state.getNome().replaceAll(Constants.SEPARATOR, "")));
					// adds the new transition from the "StateString" with the "alphabet" label to the "state"
					deteministic.addTransition(new Transition_(new State_(String.join("", stringState)), alphabet,
							new State_(state.getNome().replaceAll(Constants.SEPARATOR, ""))));

					// if the reached state is not in the list auxCopy should add it to explore later
					if (!auxCopy.contains(state.getNome())) {
						aux.add(stateName);
						auxCopy.add(stateName);
					}
				}
			}

			// adds the final states to the deterministic automaton
			for (String nomeEstadoFinal : finalStates) {
				deteministic.addFinalStates(new State_(nomeEstadoFinal));
			}

			return deteministic;
		}

		// when the automaton received by parameter is already deterministic, it returns itself
		return automaton;
	}

	/***
	 * receives as parameter two automata and returns the automaton resulting from the intersection between them.
	 * 
	 * @param Q
	 *            automaton
	 * @param S
	 *            automaton
	 * @return  automaton result of the intersection between the automata Q and S
	 */
	public static Automaton_ intersection(Automaton_ Q, Automaton_ S) {
		// intersection automato
		Automaton_ Ar = new Automaton_();
		// add alphabet to automaton
		Ar.setAlphabet(Q.getAlphabet());
		// creates and defines the initial state of the automaton as the synchronization of the initial states of S and Q
		String nameSyncState = S.getInitialState().getNome() + Constants.SEPARATOR
				+ Q.getInitialState().getNome();
		// remove the name of the state
		State_ r0 = new State_(nameSyncState.replaceAll(Constants.SEPARATOR, ""));
		// used for split
		r0.setInfo(nameSyncState);
		// set initial state
		Ar.setInitialState(r0);
		// add initial state to list of state
		Ar.addState(r0);

		// list of synchronized states that will be visited and removed while the algorithm runs
		List<State_> synchronizedStates = new ArrayList<State_>();
		// add state r0 to list of states
		synchronizedStates.add(r0);
		// variables for synchronization of the states of S and Q
		String s = null, q = null;
		State_ removed = null, synchronized_ = null;
		String[] part;
		Result sTransitions,qTransitions ;

		// while there are states to be synchronized
		while (synchronizedStates.size() > 0) {
			// emoves the first element from the list of states to be synchronized
			removed = synchronizedStates.remove(0);

			// as the state name is (s, q),  splits the string to find the 's' and 'q'
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
				if (sTransitions.getFound() && qTransitions.getFound()) {
					// synchronized states in S, there may be more than one in case of no determinism
					for (State_ endStateS : sTransitions.getReachedStates()) {
						// synchronized states in Q, there may be more than one in case of non-determinism
						for (State_ endStateQ : qTransitions.getReachedStates()) {
							// creates a new state that matches what was synced
							nameSyncState = endStateS.getNome() + Constants.SEPARATOR + endStateQ.getNome();
							//the state name is without the separator
							synchronized_ = new State_(nameSyncState.replaceAll(Constants.SEPARATOR, ""));
							// description of the state with the separator
							synchronized_.setInfo(nameSyncState);
							if (!Ar.getStates().contains(synchronized_)) {
								synchronizedStates.add(new State_(synchronized_));
							}

							// adds the created state to the automaton
							synchronized_.setNome(synchronized_.getNome().replaceAll(Constants.SEPARATOR, ""));
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
			//verifies whether the synchronized states were final states in Q and S
			if (finalStateS.contains(new State_(s)) && finalStateQ.contains(new State_(q))) {
				Ar.addFinalStates(state);
			}
		}
		return Ar;
	}

	/***
	 * receives as parameter two automata and returns the automaton resulting from the union between them.
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

		// the transitions of the union automaton, is the union of the transitions of Q and S
		Set<Transition_> transicoes = new HashSet<Transition_>();
		transicoes.addAll(Q.getTransitions());
		transicoes.addAll(S.getTransitions());
		Ay.setTransitions(new ArrayList<Transition_>(transicoes));

		// the states of the union automata, is the union of the states of Q and S
		Set<State_> estados = new HashSet<State_>();
		estados.addAll(Q.getStates());
		estados.addAll(S.getStates());
		Ay.setStates(new ArrayList<State_>(estados));

		// the final states of the union automaton, is the union of the final states of Q and S
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
		// add the transitions from the initial state with the label EPSILON to the initial state of S and of Q
		Ay.addTransition(new Transition_(estado, Constants.EPSILON, S.getInitialState()));
		Ay.addTransition(new Transition_(estado, Constants.EPSILON, Q.getInitialState()));

		return convertToDeterministicAutomaton(Ay);
	}

	/***
	 * Using the brics library converts the regex into Automaton
	 * 
	 * @param regex
	 * @param tag
	 *            string that will serve to name the states, to distinguish the states
	 *			  of D and F because the library creates states named "1", "2", "3"
	 * 			  and therefore there may be conflict going forward
	 * @return automaton which accepts the regex received by parameter
	 */
	public static Automaton_ regexToAutomaton(String linguagemRegular, String tag) {
		// regex to automaton
		RegExp regExp = new RegExp(linguagemRegular);
		Automaton automaton = regExp.toAutomaton();
		return AutomatonBricsInAutomaton_(automaton, tag);
	}

	/***
	 * Converts the Automaton object generated by the brics library in the Automato object of this project
	 * 
	 * @param a
	 *             brics library automaton
	 * @param tag
	 *            string that will serve to name the states, to distinguish the states
	 *			  of D and F because the library creates states named "1", "2", "3"
	 * 			  and therefore there may be conflict going forward
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
	 * Retrieves Automaton transitions based on the iniState, 
	 * the brics library does not allow direct access to the transition label so the value passed through the toString
	 * 
	 * @param iniState
	 * @param transition
	 * @param tag
	 *            used to name the state
	 * @return  transitions list of iniState
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
	 * Retrieves the state given the parameter State, 
	 * because the brics library does not allow to directly access the label of the state, 
	 * then used here the value passed by the toString
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

	// /***
	// * Recebe um automato e retorna uma palavra alcançada apartir de cada estado
	// * final do automato
	// *
	// * @return palavraFinal conjunto de palavras que levam aos estados finais do
	// * automato
	// */
	// // provisório para fins de teste
	// static Estado atual;
	//
	// private static List<String> getPalavrasAutomato(Automato a) {
	// String palavra = "";
	// // String palavraFinal = "";
	// Transicao t_atual = null;
	// List<String> palavras = new ArrayList<String>();
	//
	// // percorre todos os estados finais
	// for (Estado estado : a.getEstadosFinais()) {
	// Estado estadoFinalErro = estado;
	// atual = estadoFinalErro;
	// palavra = "";
	//
	// // percorre de tras para frente até alcançar o estado inicial
	// while (!atual.equals(a.getEstadoInicial())) {
	// // recupera uma transição cujo "estadoFim" é igual ao estado "atual"
	// t_atual = a.getTransicoes().stream().filter(y ->
	// y.getEstadoFim().getNome().equals(atual.getNome()))
	// .findFirst().get();
	//
	// // adiciona o rótulo a palavra
	// if (t_atual.getRotulo().charAt(0) == Constantes.TAG_ENTRADA
	// || t_atual.getRotulo().charAt(0) == Constantes.TAG_SAIDA) {
	// // trata quando tem rótulo de entrada e saída
	// palavra += t_atual.getRotulo().substring(1, t_atual.getRotulo().length())
	// + t_atual.getRotulo().charAt(0) + " >- ";
	// } else {
	// palavra += t_atual.getRotulo() + " >- ";
	// }
	//
	// // move o estado atual para o estadoIni da transição
	// atual = t_atual.getEstadoIni();
	// }
	//
	// if (!palavra.replaceAll(" ", "").equals("")) {
	// palavras.add((new StringBuilder(palavra.substring(0, palavra.length() -
	// 1)).reverse().toString()).substring(3,palavra.length()-1));
	// }
	//
	// // adiciona a palavra encontrada invertida pois a palavra é descoberta de
	// tras
	// // pra frente
	// // palavraFinal += new StringBuilder(palavra).reverse().toString() + " - ";
	// }
	//
	// return palavras; // palavraFinal;
	// }

	static volatile State_ current;

	private static List<String> getWordsFromAutomaton(Automaton_ a) {
		String word = "";
		String tagWord = " , ";
		String tagLetter = " -> ";
		List<State_> stateToVisit = new ArrayList<State_>();
		stateToVisit.add(a.getInitialState());
		a.getStates().forEach(p -> p.setInfo(null));
		a.getStates().forEach(p -> p.setVisitado(false));

		State_ endState, iniState;
		String[] aux;
		List<String> words = new ArrayList<String>();
		List<String> news = new ArrayList<String>();

		while (!stateToVisit.isEmpty()) {
			current = stateToVisit.remove(0);
			current = a.getStates().stream().filter(x -> x.equals(current)).findFirst().orElse(null);
			if (!current.isVisitado()) {
				current.setVisitado(true);
				List<Transition_> transicoes = a.transitionsByIniState(current);
				for (Transition_ t : transicoes) {
					news = new ArrayList<String>();
					iniState = a.getStates().stream().filter(x -> x.equals(t.getEstadoIni())).findFirst()
							.orElse(null);
					endState = a.getStates().stream().filter(x -> x.equals(t.getEstadoFim())).findFirst()
							.orElse(null);
					word = "";
					if (iniState.getInfo() != null) {
						aux = iniState.getInfo().split(tagWord);
						if (aux.length > 0) {
							for (int i = 0; i < aux.length; i++) {
								word = aux[i] + tagLetter + t.getRotulo() + tagWord;
								news.add(word);
							}
						} else {
							word = iniState.getInfo() + tagLetter + t.getRotulo() + tagWord;
							news.add(word);
						}

					} else {
						word = t.getRotulo() + tagWord;
						news.add(word);
					}

					for (String p : news) {						
						word = (endState.getInfo() != null ? (endState.getInfo()) : "") + p;
						endState.setInfo(word);
					}

					if (!endState.isVisitado()) {
						stateToVisit.add(endState);
					}

				}
			}
		}

		for (State_ e : a.getStates()) {
			if (a.getFinalStates().contains(e)) {
				aux = e.getInfo().split(tagWord);
				for (int i = 0; i < aux.length; i++) {
					words.add(aux[i]);
				}
			}
		}

		return words;
	}

	/***
	 * Checks the conformance of the automaton and when it does not conform returns a word that shows the nonconformity
	 * 
	 * @param a
	 *            automaton
	 * @return whether it conforms or not and the word that proves the non-conformity
	 */
	public static String veredict(Automaton_ a) {
		if (a.getFinalStates().size() > 0) {
			return "Doesn't conforms! " ;
		} else {
			return "Is conforms! ";
		}
	}

	/***
	 * 
	 * @param S
	 *            specification model
	 * @param I
	 *            implementation model
	 * @param fault model
	 *            containing words that detect implementation failure
	 * @return the paths covered by the test cases by implementation and specification
	 */
	public static String path(LTS S, LTS I, Automaton_ faultModel) {
		List<String> testCases = getWordsFromAutomaton(faultModel);

		State_ currentState_s = S.getInitialState();
		State_ currentState_i = I.getInitialState();
		Result result_s, result_i;
		int stop_s = 0;
		int stop_i = 0;

		String path = "", path_i = "", path_s = "";

		for (String letter : testCases) {
			currentState_s = S.getInitialState();
			currentState_i = I.getInitialState();
			path_i = path_s = "";
			path += "Test case: " + letter + "\n";
			path_s += currentState_s + " -> ";
			path_i += currentState_i + " -> ";
			stop_s = 0;
			stop_i = 0;
			
			
			
			for (String p : letter.split(" -> ")) {

				if (currentState_s != null) {
					result_s = S.transitionExists(currentState_s.getNome(), p);

					if (result_s.getFound()) {
						currentState_s = result_s.getReachedStates().get(0);
					} else {
						currentState_s = null;
						stop_s++;
					}
				} else {
					stop_s++;
				}

				if (currentState_i != null) {
					result_i = I.transitionExists(currentState_i.getNome(), p);
					if (result_i.getFound()) {
						currentState_i = result_i.getReachedStates().get(0);
					} else {
						currentState_i = null;
						stop_i++;
					}
				} else {
					stop_i++;
				}

				if (stop_s <= 1) {
					path_s += (stop_s == 1 ? " [there are no transitions of "
							+ path_s.split(" -> ")[path_s.split(" -> ").length - 1] + " with label " + p + "]"
							: currentState_s) + " -> ";
				}

				if (stop_i <= 1) {
					path_i += (stop_i == 1 ? " [there are no transitions of "
							+ path_i.split(" -> ")[path_i.split(" -> ").length - 1] + " with label " + p + "]"
							: currentState_i) + " -> ";
				}
			}

			path += "Implementation: \n path: " + path_i;
			path += "\nModel: \n path:" + path_s;
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
	 * @param fault model
	 *            containing words that detect implementation failure
	 * @return the paths covered by the test cases by implementation and specification*/
	public static String path(IOLTS S, IOLTS I, Automaton_ fault) {
		List<String> testCases = getWordsFromAutomaton(fault);

		State_ currentState_s = S.getInitialState();
		State_ currentState_i = I.getInitialState();
		Result result_s, result_i;
		int stop_s = 0;
		int stop_i = 0;

		String path = "", path_i = "", path_s = "";

		for (String letter : testCases) {
			path_i = path_s = "";
			path += "Test case: \n\t\t" + letter + "\n";
			path_s += currentState_s + " -> ";
			path_i += currentState_i + " -> ";
			stop_s = 0;
			stop_i = 0;

			for (String p : letter.split(" -> ")) {

				if (currentState_s != null) {
					result_s = S.transitionExists(currentState_s.getNome(), p);

					if (result_s.getFound()) {
						currentState_s = result_s.getReachedStates().get(0);
					} else {
						currentState_s = null;
						stop_s++;
					}
				} else {
					stop_s++;
				}

				if (currentState_i != null) {
					result_i = I.transitionExists(currentState_i.getNome(), p);
					if (result_i.getFound()) {
						currentState_i = result_i.getReachedStates().get(0);
					} else {
						currentState_i = null;
						stop_i++;
					}
				} else {
					stop_i++;
				}

				if (stop_s <= 1) {
					path_s += (stop_s == 1 ? " [there are no transitions of "
							+ path_s.split(" -> ")[path_s.split(" -> ").length - 1] + " with label " + p + "]"
							: currentState_s) + " -> ";
				}

				if (stop_i <= 1) {
					path_i += (stop_i == 1 ? " [there are no transitions of "
							+ path_i.split(" -> ")[path_i.split(" -> ").length - 1] + " with label " + p + "]"
							: currentState_i) + " -> ";
				}

			}

			path += "Implementation: \n\t path: " + path_i + "\n\t output: " + I.statesOutputs(currentState_i);
			path += "\nModel: \n\t path:" + path_s + "\n\t output: " + S.statesOutputs(currentState_s);
			path += "\n################################################################## \n";

		}
		return path;
	}

}
