package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bridj.util.Pair;

import model.Automaton_;
import model.IOLTS;
import model.State_;
import model.Transition_;
import parser.ImportAutFile_WithoutThread;
import util.Constants;

public class TestGeneration {

	public static void main(String[] args) {
		String path = "C:\\Users\\camil\\Documents\\aut-separados\\iolts-spec.aut";
		try {
			IOLTS iolts = ImportAutFile_WithoutThread.autToIOLTS(path, false, null, null);

			List<State_> states = new ArrayList<>();
			Automaton_ multgraph = MultiGraphD(iolts, 4);
//			// remover estados inicialmente não conectados
//			for (State_ s : multgraph.getStates()) {
//				if (s.getName().contains("s0") && !s.getName().equals("s0,0")) {
//					states.add(s);
//				}
//			}
//			
//			List<Transition_> transitions = new ArrayList<>();
//			for (Transition_ t : multgraph.getTransitions()) {
//				if(states.contains(t.getIniState()) || states.contains(t.getEndState())) {
//					transitions.add(t);
//				}				
//			}
//
//			multgraph.getStates().removeAll(states);
//			multgraph.getTransitions().removeAll(transitions);
//			List<State_> alcancavel ;
//			List<State_> toVisit ;
//			
//			for (State_	s  : multgraph.getStates()) {
//				alcancavel = new ArrayList<>();
//				toVisit= new ArrayList<>();
//				do {
//					toVisit = multgraph.transitionsByIniState(s);
//					
//				}while(alcancavel.contains(multgraph.getInitialState()));
//			}
			
			
			states = new ArrayList<>();
			for (Transition_ t : multgraph.getTransitions()) {
				if (multgraph.getFinalStates().contains(t.getEndState())) {
					states.add(t.getIniState());
				}
			}
			multgraph.setFinalStates(states);

			System.out.println(states.size());
			
			System.out.println(Operations.getWordsFromAutomaton(multgraph, false, Integer.MAX_VALUE));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * 
	 * @param S
	 *            the specification model
	 * @param m
	 *            max states of specification
	 * @return
	 */
	public static Automaton_ MultiGraphD(IOLTS S, int m) {
		int level = 0;
		IOLTS D = new IOLTS();
		State_ iniState = new State_(S.getInitialState() + Constants.COMMA + level);
		D.setInitialState(iniState);
		D.addState(iniState);
		State_ current;

		List<State_> toVisit = new ArrayList<>();
		toVisit.add(S.getInitialState());

		List<State_> toVisit_aux = new ArrayList<>(toVisit);

		int totalLevels = S.getStates().size() * m + 1;

		List<String> L = S.getInputs();
		L.addAll(S.getOutputs());

		D.setAlphabet(L);

		List<Pair<String, String>> nextLevel = new ArrayList<>();
		State_ d;

		// construct first level
		while (toVisit.size() > 0) {
			current = toVisit.remove(0);// remove from the beginning of queue
			for (Transition_ t : S.transitionsByIniState(current)) {
				if (!toVisit_aux.contains(t.getEndState())) {
					toVisit.add(t.getEndState());
					toVisit_aux.add(t.getEndState());
				}

				if (current.equals(t.getEndState()) || nextLevel.contains(new Pair(current, t.getEndState()))) {
					d = new State_(t.getEndState() + Constants.COMMA + "1");
				} else {
					for (Transition_ t2 : S.transitionsByIniState(t.getEndState())) {
						if (t2.getEndState().equals(current)) {
							nextLevel.add(new Pair(t.getEndState(), current));
						}
					}
					d = new State_(t.getEndState() + Constants.COMMA + "0");
				}

				D.addState(d);
				D.addTransition(new Transition_(new State_(current + Constants.COMMA + "0"), t.getLabel(), d));
			}
		}

		int leveld, leveld2 = 0;
		String named, named2;
		State_ ini, end;
		List<Transition_> transition = new ArrayList<>();
		List<State_> states = new ArrayList<>();

		// construct rest of levels
		for (Transition_ t : D.getTransitions()) {
			named = t.getIniState().getName().split(Constants.COMMA)[0];
			named2 = t.getEndState().getName().split(Constants.COMMA)[0];
			leveld = Integer.parseInt(t.getIniState().getName().split(Constants.COMMA)[1]);
			leveld2 = Integer.parseInt(t.getEndState().getName().split(Constants.COMMA)[1]);

			while (leveld2 + 1 < totalLevels) {
				leveld++;
				leveld2++;

				ini = new State_(named + Constants.COMMA + leveld);
				end = new State_(named2 + Constants.COMMA + leveld2);
				transition.add(new Transition_(ini, t.getLabel(), end));
				states.add(ini);
				states.add(end);
			}
		}
		D.getTransitions().addAll(transition);
		D.getStates().addAll(new ArrayList<>(new HashSet<>(states)));

		State_ fail = new State_("fail");
		D.addState(fail);
		for (String l : S.getOutputs()) {
			for (State_ s : D.getStates()) {
				if (D.reachedStates(s.getName(), l).size() == 0) {
					D.addTransition(new Transition_(s, l, fail));
				}
			}
		}

		Automaton_ a = D.ltsToAutomaton();
		a.setFinalStates(Arrays.asList(new State_[] { fail }));

		return a;
	}
}
