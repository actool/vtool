/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.collections.ListUtils;
import model.Automaton_;
import model.State_;
import model.IOLTS;
import model.Transition_;
import util.Constants;
import util.ModelImageGenerator;

/**
 * Class IOCO Conformance
 * 
 * @author Camila
 */
public class IocoConformance {

	/***
	 * Verify if a implementation is IOCO conforms to specification, returns the
	 * resultant automaton of this verification
	 * 
	 * @param S
	 *            IOLTS model of specification
	 * @param I
	 *            IOLTS implementation
	 * @return
	 */
	public static Automaton_ verifyIOCOConformance(IOLTS S, IOLTS I) {
		List<String> alphabet = new ArrayList();
		alphabet.addAll(I.getAlphabet());
		alphabet.addAll(S.getAlphabet());
		HashSet hashSet_s_ = new LinkedHashSet<>(alphabet);
		alphabet = new ArrayList<>(hashSet_s_);
		S.setAlphabet(alphabet);

		// build the fault model, containing all fail behaviors based on specification
		// model
		//System.out.println("at" + new Date());
		Automaton_ at = faultModelIoco(S);
		//System.out.println("ai" + new Date());
		// automaton underlying the implementation
		Automaton_ ai = I.ioltsToAutomaton();
		//System.out.println("ab" + new Date());
		
		//System.out.println(at.getTransitions().size()+"  -   "+ai.getTransitions().size());
		// intersection between the implementation and failure model to find fault
		Automaton_ ab = Operations.intersection(at, ai);

		//System.out.println("fim ab:"+ new Date());
		/*
		 * System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
		 * ; System.out.
		 * println("<<<<<<<<<<<<<<<<<<<< verification IOCO conformance >>>>>>>>>>>>>>>>>>>>>"
		 * );
		 * System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
		 * ;
		 * System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		 * System.out.println("Fault model");
		 * System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n")
		 * ; System.out.println(at);
		 * System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		 * System.out.println("implementation");
		 * System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n")
		 * ; System.out.println(ai);
		 * System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		 * System.out.println("Intersection [Fault model X Implementation]");
		 * System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n")
		 * ; System.out.println(ab);
		 */
		// System.out.println(ab);

		return ab;
	}

	/***
	 * Build the fault model based on specification model
	 * 
	 * @param S
	 *            specification model
	 * 
	 * @return the fault model automaton
	 */
	private static Automaton_ faultModelIoco(IOLTS S) {
		//System.out.println("as" + new Date());
		// automaton underlying the specification IOLTS S
		Automaton_ as = S.ioltsToAutomaton();
		// automaton complement of specification
		//System.out.println("aCompS" + new Date());
		Automaton_ aCompS = Operations.complement(as);
		// automaton D with the desired behaviors
		//System.out.println("ad" + new Date());
		Automaton_ ad = modelD(S);

		//System.out.println("fim ad" + new Date());

		/*
		 * System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
		 * ;
		 * System.out.println("<<<<<<<<<<<<<<<<<<<< modeloDeFalha>>>>>>>>>>>>>>>>>>>>>"
		 * );
		 * System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
		 * ;
		 * System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		 * System.out.println("Automato Especificação");
		 * System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n")
		 * ; System.out.println(as);
		 * System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		 * System.out.println("Automato Complemento Especificação");
		 * System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n")
		 * ; System.out.println(aCompS);
		 * System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		 * System.out.println("Automato D");
		 * System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n")
		 * ; System.out.println(ad);
		 */

		// intersection between the desirable behavior and the complement of the
		// specification (which is not in the specification)
		return Operations.intersection(ad, aCompS);
	}

	/***
	 * Construct the automaton D with the desired behaviors based on the
	 * specification received by parameter
	 * 
	 * @param S
	 *            automaton underlying the specification
	 * 
	 * @return automaton D with the desirable behaviors
	 */
	private static Automaton_ modelD(IOLTS S) {
		// automaton D
		Automaton_ as = new Automaton_();
		// define the initial state
		as.setInitialState(S.getInitialState());
		// define the states
		as.setStates(S.getStates());
		// create new fail state
		State_ d = new State_("fail");
		// define the alphabet like the union beteen inputs and outputs
		as.setAlphabet(ListUtils.union(S.getInputs(), S.getOutputs()));
		// add transitions, process when have TAU
		as.setTransitions(Operations.processTauTransition(S.getTransitions()));
		// define the fail state like the unique final state
		as.addFinalStates(d);

		for (State_ e : S.getStates()) {
			for (String l : as.getAlphabet()) {
				// if it is an output label and there is no transition starting from "e" with
				// the label "l"
				// then a transition is created pointing to the failure state
				if (!as.transitionExists(e.getName(), l) && (S.getOutputs().contains(l))) {
					as.addTransition(new Transition_(e, l, d));
				}
			}
		}

		// add fault state in the list,
		// added here for this state have no transitions
		return Operations.convertToDeterministicAutomaton(as);
	}

}
