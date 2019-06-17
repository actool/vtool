/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;

import util.Constants;

/**
 * Class IOLTS
 * 
 * @author Camila
 */
public class IOLTS extends LTS implements Cloneable {
	private List<String> inputs;
	private List<String> outputs;

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/***
	 * Empty constructor
	 */
	public IOLTS() {
	}

	/***
	 * Constructor receives an LTS and changes the attributes of the IOLTS
	 * 
	 * @param lts
	 */
	public IOLTS(LTS lts) {
		// calls super-class methods to initialize the parameters
		this.setInitialState(lts.getInitialState());
		this.setStates(lts.getStates());
		this.setTransitions(lts.getTransitions());
		this.setAlphabet(lts.getAlphabet());
	}

	/**
	 * Returns the inputs label
	 * 
	 * @return input label
	 */
	public List<String> getInputs() {
		return inputs;
	}

	/**
	 * set input labels
	 * 
	 * @param input
	 *            set of input labels
	 */
	public void setInputs(List<String> input) {
		this.inputs = input;
	}

	/**
	 * return the set of output label
	 * 
	 * @return outputs the set of output labels
	 */
	public List<String> getOutputs() {
		return outputs;
	}

	/**
	 * Alter the set of output labels
	 * 
	 * @param outputs
	 *            the set of output labels
	 */
	public void setOutputs(List<String> rotulosSaida) {
		this.outputs = rotulosSaida;
	}

	/***
	 * builds the underlying LTS from IOLTS
	 * 
	 * @return the underlying LTS
	 */
	public LTS toLTS() {
		// in automato there is no distinction between input and output labels, so the
		// input and output labels are joined to form the alphabet
		List<String> alfabeto = ListUtils.union(this.inputs, this.outputs);
		// Instances an LTS with IOLTS atributtes
		LTS lts = new LTS(this.getStates(), this.getInitialState(), alfabeto, this.getTransitions());
		return lts;
	}

	/***
	 * builds the underlying automaton from IOLTS
	 * 
	 * @return the underlying automaton
	 */
	public Automaton_ ioltsToAutomaton() {
		// build automato from LTS
		return toLTS().ltsToAutomaton();
	}

	/***
	 * Overwriting the toString method, with the separation between the input and
	 * output label
	 * 
	 * @return the string describing the IOLTS
	 */
	@Override
	public String toString() {
		// calls LTS toString
		String s = super.toString();
		// add description of input labels
		s += ("##############################\n");
		s += ("           Inputs \n");
		s += ("##############################\n");
		s += ("length: " + this.getInputs().size() + "\n");
		for (String e : this.getInputs()) {
			s += ("[" + e + "] - ");
		}
		// add description of output labels
		s += ("\n##############################\n");
		s += ("           Outputs \n");
		s += ("##############################\n");
		s += ("length: " + this.getOutputs().size() + "\n");
		for (String e : this.getOutputs()) {
			s += ("[" + e + "] - ");
		}
		return s;
	}

	/***
	 * Add the quiescent transition
	 * 
	 * @param transition
	 */
	public void addQuiescentTransitions() {// quiescent
		this.addToAlphabet(Constants.DELTA);
		
		for (State_ s : this.states) {
			if (isQuiescent(s) && s != null) {
				this.addTransition(new Transition_(s, Constants.DELTA, s));
				this.outputs.add(Constants.DELTA);				
			}
		}
	}

	/***
	 * return list of output labels from state received per parameter
	 * 
	 * @param e
	 *            state
	 * @return list of string containing output labels
	 */
	public boolean isQuiescent(State_ e) { // quiescent
		if (e != null) {
			for (Transition_ t : transitions) {
				// checks whether the transition contains the initial state of the transition
				// and the
				// output label
				if (t.getIniState().getName().toString().equals(e.getName().toString())
						&& outputs.contains(t.getLabel())) {
					return false;
				}
			}
		}

		return true;
	}

	/***
	 * return list of output labels from state received per parameter
	 * 
	 * @param e
	 *            state
	 * @return list of string containing output labels
	 */
	public List<String> outputsOfState(State_ e) {
		List<String> label = new ArrayList<String>();

		if (e != null) {
			for (Transition_ t : transitions) {
				// checks whether the transition contains the initial state of the transition
				// and the
				// output label
				if (t.getIniState().getName().toString().equals(e.getName().toString())
						&& ((outputs.contains(t.getLabel())) || t.getLabel().equals(Constants.DELTA))) {
					label.add(t.getLabel());
				}
			}
		}

		return label;
	}

}
