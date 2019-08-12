package test;

import java.util.ArrayList;
import java.util.List;

import algorithm.Operations;
import model.Automaton_;
import model.State_;
import model.Transition_;

public class Main {
	
	public static void main(String[] args) {
		/*Automaton_ a = new Automaton_();
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
		
		System.out.println(Operations.getWordsFromAutomaton(a, false));*/
		String failPath = "Test case: 	b\r\n" + 
				"\r\n" + 
				"Model outputs: [δ]\r\n" + 
				"\r\n" + 
				"	 path:s0 -> s3 ->\r\n" + 
				"	 output: [δ]\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Implementation outputs: [x]\r\n" + 
				"\r\n" + 
				"	 path:q0 -> q3 ->\r\n" + 
				"	 output: [x]\r\n" + 
				"\r\n" + 
				"################################################################## \r\n" + 
				"Test case: 	δ -> b\r\n" + 
				"\r\n" + 
				"Model outputs: [δ]\r\n" + 
				"\r\n" + 
				"	 path:s0 -> s0 ->s3 ->\r\n" + 
				"	 output: [δ]\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Implementation outputs: [x]\r\n" + 
				"\r\n" + 
				"	 path:q0 -> q0 ->q3 ->\r\n" + 
				"	 output: [x]\r\n" + 
				"\r\n" + 
				"################################################################## \r\n" + 
				"Test case: 	b -> b -> b\r\n" + 
				"\r\n" + 
				"Model outputs: [δ]\r\n" + 
				"\r\n" + 
				"	 path:s0 -> s3 ->s0 ->s3 ->\r\n" + 
				"	 output: [δ]\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Implementation outputs: [x]\r\n" + 
				"\r\n" + 
				"	 path:q0 -> q3 ->q0 ->q3 ->\r\n" + 
				"	 output: [x]\r\n" + 
				"\r\n" + 
				"################################################################## \r\n" + 
				"Test case: 	a -> a -> b -> b\r\n" + 
				"\r\n" + 
				"Model outputs: [δ]\r\n" + 
				"\r\n" + 
				"	 path:s0 -> s1 ->s3 ->s0 ->s3 ->\r\n" + 
				"	 output: [δ]\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Implementation outputs: [x]\r\n" + 
				"\r\n" + 
				"	 path:q0 -> q1 ->q3 ->q0 ->q3 ->\r\n" + 
				"	 output: [x]\r\n" + 
				"\r\n" + 
				"################################################################## \r\n" + 
				"Test case: 	a -> a\r\n" + 
				"\r\n" + 
				"Model outputs: [δ]\r\n" + 
				"\r\n" + 
				"	 path:s0 -> s1 ->s3 ->\r\n" + 
				"	 output: [δ]\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Implementation outputs: [x]\r\n" + 
				"\r\n" + 
				"	 path:q0 -> q1 ->q3 ->\r\n" + 
				"	 output: [x]\r\n" + 
				"\r\n" + 
				"################################################################## \r\n" + 
				"Test case: 	b -> a\r\n" + 
				"\r\n" + 
				"Model outputs: [δ]\r\n" + 
				"\r\n" + 
				"	 path:s0 -> s3 ->s3 ->\r\n" + 
				"	 output: [δ]\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Implementation outputs: [x]\r\n" + 
				"\r\n" + 
				"	 path:q0 -> q3 ->q3 ->\r\n" + 
				"	 output: [x]\r\n" + 
				"\r\n" + 
				"################################################################## \r\n" + 
				"Test case: 	a -> a -> a\r\n" + 
				"\r\n" + 
				"Model outputs: [δ]\r\n" + 
				"\r\n" + 
				"	 path:s0 -> s1 ->s3 ->s3 ->\r\n" + 
				"	 output: [δ]\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Implementation outputs: [x]\r\n" + 
				"\r\n" + 
				"	 path:q0 -> q1 ->q3 ->q3 ->\r\n" + 
				"	 output: [x]\r\n" + 
				"\r\n" + 
				"################################################################## \r\n" + 
				"Test case: 	a -> b\r\n" + 
				"\r\n" + 
				"Model outputs: [x]\r\n" + 
				"\r\n" + 
				"	 path:s0 -> s1 ->s2 ->\r\n" + 
				"	 output: [x]\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Implementation outputs: [δ]\r\n" + 
				"\r\n" + 
				"	 path:q0 -> q1 ->q2 ->\r\n" + 
				"	 output: [δ]\r\n" + 
				"\r\n" + 
				"################################################################## \r\n" + 
				"Test case: 	a -> x\r\n" + 
				"\r\n" + 
				"Model outputs: [x]\r\n" + 
				"\r\n" + 
				"	 path:s0 -> s1 ->s2 ->\r\n" + 
				"	 output: [x]\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Implementation outputs: [δ]\r\n" + 
				"\r\n" + 
				"	 path:q0 -> q1 ->q2 ->\r\n" + 
				"	 output: [δ]\r\n" + 
				"\r\n" + 
				"################################################################## \r\n" + 
				"Test case: 	a -> b -> b\r\n" + 
				"\r\n" + 
				"Model outputs: [x]\r\n" + 
				"\r\n" + 
				"	 path:s0 -> s1 ->s2 ->s2 ->\r\n" + 
				"	 output: [x]\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Implementation outputs: [δ]\r\n" + 
				"\r\n" + 
				"	 path:q0 -> q1 ->q2 ->q2 ->\r\n" + 
				"	 output: [δ]\r\n" + 
				"\r\n" + 
				"################################################################## \r\n" + 
				"Test case: 	a -> x -> b\r\n" + 
				"\r\n" + 
				"Model outputs: [x]\r\n" + 
				"\r\n" + 
				"	 path:s0 -> s1 ->s2 ->s2 ->\r\n" + 
				"	 output: [x]\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Implementation outputs: [δ]\r\n" + 
				"\r\n" + 
				"	 path:q0 -> q1 ->q2 ->q2 ->\r\n" + 
				"	 output: [δ]\r\n" + 
				"\r\n" + 
				"################################################################## \r\n" + 
				"";
		String[] lines = failPath.split("\r\n");
		List<String> testCases = new ArrayList<>();
		for (String s : lines) {
			if (s.contains("Test case:")) {
				testCases.add(s.replace("Test case: 	", "").replaceAll("\b\r\n", "").replaceAll("#", "").replace(" -> ", ""));
			}
		}
		
		System.out.println(testCases);
	}
	

}
