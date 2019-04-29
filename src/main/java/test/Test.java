package test;

import algorithm.Operations;
import model.Automaton_;
import model.State_;
import model.Transition_;
import util.Constants;

public class Test {

	public static void main(String[] args) {
//		Automaton_ s = new Automaton_();
//		State_ s0 = new State_("s0");
//		State_ s1 = new State_("s1");
//		State_ s2 = new State_("s2");		
//		s.setInitialState(s0);
//		s.addFinalStates(s2);		
//		s.addTransition(new Transition_(s0,"a",s2));
//		s.addTransition(new Transition_(s0,"b",s1));
//		s.addTransition(new Transition_(s1,"a",s2));
//		s.addTransition(new Transition_(s1,"b",s2));
//		s.addTransition(new Transition_(s2,"a",s2));
//		s.addTransition(new Transition_(s2,"b",s1));
//		
//		Automaton_ q = new Automaton_();
//		State_ q0 = new State_("q0");
//		State_ q1 = new State_("q1");
//		q.setInitialState(q0);
//		q.addFinalStates(q1);
//		q.addTransition(new Transition_(q0,"a",q1));
//		q.addTransition(new Transition_(q1,"a",q0));
//		q.addTransition(new Transition_(q1,"b",q1));
//		
//		System.out.println(Operations.intersection(q,s));
		
		
		Automaton_ s = new Automaton_();
		State_ s0 = new State_("s0");
		State_ s1 = new State_("s1");
		State_ s2 = new State_("s2");		
		s.setInitialState(s0);
		s.addFinalStates(s2);
		s.addTransition(new Transition_(s0,Constants.EPSILON,s2));
		s.addTransition(new Transition_(s0,"b",s1));
		s.addTransition(new Transition_(s1,"b",s1));
		s.addTransition(new Transition_(s1,Constants.EPSILON,s2));
		s.addTransition(new Transition_(s2,"a",s2));
		s.addTransition(new Transition_(s2,Constants.EPSILON,s0));
		
		System.out.println(Operations.convertToDeterministicAutomaton(s));
		
		

	}

}
