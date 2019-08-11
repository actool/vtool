package util;

import algorithm.Operations;
import model.Automaton_;
import model.State_;
import model.Transition_;

public class Main {
	
	public static void main(String[] args) {
		Automaton_ a = new Automaton_();
		State_ s0= new State_("s0");
		State_ s1= new State_("s1");
		State_ s2= new State_("s2");
		State_ s3= new State_("s3");
		
		a.addState(s0);
		a.addState(s1);
		a.addState(s2);
		a.addState(s3);
		
		a.setInitialState(s0);
		a.addFinalStates(s3);
		
		a.addTransition(new Transition_(s0,"a",s1));
		a.addTransition(new Transition_(s0,"b",s2));
		a.addTransition(new Transition_(s1,"b",s3));
		a.addTransition(new Transition_(s1,"b", s2));
		a.addTransition(new Transition_(s2,"a",s3));
		a.addTransition(new Transition_(s3, "a", s3));
		
		System.out.println(Operations.getWordsFromAutomaton(a, false));
	}
	

}
