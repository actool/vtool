package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class AutGenerator {
	public static void main(String[] args) {		
		int qtStates = 10;
		List<String> labels = Arrays.asList("?a", "?b", "?c", "!x", "!y");
		int qtTransition = 0;
		String transitions = "";
		String tag = "g";
		String directory = "C:\\Users\\camil\\Desktop\\";
		File file = new File(directory, "model"+qtStates+"states.aut");

				
		Random rand = new Random();
		BufferedWriter writer = null;
		String newline = System.getProperty("line.separator");
		try {			
			for (int i = 0; i < qtStates; i++) {
				for (String l : labels) {				
					if (rand.nextInt(2) == 1){
						transitions += "("+tag+i+", "+l+", "+tag+rand.nextInt(qtStates)+")"+newline;
						qtTransition++;
					}
				}
			}
			
			String header = "des("+tag+"0,"+qtTransition+", "+qtStates+")" + newline;
			String aut = header + transitions;
			
			//System.out.println(aut);
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(aut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}

	}
	// public static void main(String[] args) {
	// Automaton_ S = new Automaton_();
	// State_ s0 = new State_("s0");
	// State_ s1 = new State_("s1");
	// State_ s2 = new State_("s2");
	//
	// S.addState(s0);
	// S.addState(s1);
	// S.addState(s2);
	//
	// S.addFinalStates(s2);
	//
	// S.addTransition(new Transition_(s0, "a", s2));
	// S.addTransition(new Transition_(s0, "b", s1));
	// S.addTransition(new Transition_(s1, "a", s2));
	// S.addTransition(new Transition_(s1, "b", s2));
	// S.addTransition(new Transition_(s2, "a", s2));
	//
	// S.setInitialState(s0);
	//
	//
	// Automaton_ Q = new Automaton_();
	// State_ q0 = new State_("q0");
	// State_ q1 = new State_("q1");
	//
	// Q.addState(q0);
	// Q.addState(q1);
	//
	// Q.setInitialState(q0);
	//
	// Q.addFinalStates(q1);
	//
	// Q.addTransition(new Transition_(q0, "a", q1));
	// Q.addTransition(new Transition_(q1, "a", q0));
	// S.addTransition(new Transition_(q1, "b", q1));
	//
	// System.out.println(Operations.intersection(S, Q));
	// }

}
