package parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import model.LTS;
import model.State_;
import model.Transition_;
import util.Constants;

public class ReadFile extends Thread {
	private int iniLine;
	private int endLine;
	String file;
	String id;
	// private static LTS lts= new LTS();
	private static List<Transition_> transitions = new ArrayList<>();
	private static List<State_> states = new ArrayList<>();
	private static List<String> inputs = new ArrayList<>();
	private static List<String> outputs = new ArrayList<>();
	
	private static String msg = "";

	private List<Transition_> transitions_ = new ArrayList<>();
	private List<State_> states_ = new ArrayList<>();
	private List<String> inputs_ = new ArrayList<>();
	private List<String> outputs_ = new ArrayList<>();

	public void init(int iniLine, int endLine, String file, String id) {
		this.iniLine = iniLine;
		this.endLine = endLine;
		this.file = file;
		this.id = id;
		// this.transitions = new ArrayList<>();
		// this.states_ = new ArrayList<>();
	}

	public void run() {
		int count = this.iniLine;
		String line;
		Supplier<Stream<String>> streamSupplier;
		boolean inconsistentLine;
		
		int msg_cont = 0;
		State_ iniState, endState;
		Transition_ transition;
		String label;
		

		while (count < this.endLine) {
			Path path = Paths.get(this.file);
			streamSupplier = () -> {
				try {
					return Files.lines(path, StandardCharsets.ISO_8859_1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			};

			// reads the line with the transition
			// each transition is configured as follows: (<from-state>, <label>,
			// <to-state>)
			line = streamSupplier.get().skip(count + 1).findFirst().get();
			//System.out.println("line" + (count + 1));
			inconsistentLine = false;

			// checks for '(' on the read line
			int ini = line.indexOf("(");
			if (ini < 0) {
				msg += ("line [" + count + "] is invalid absence of '(' " + "\n");
				inconsistentLine = true;
				msg_cont += 1;
			}

			// checks for ')' on the read line
			line = line.replaceAll("\\s+$", "");
			int fim = line.length() - 1;
			if (line.charAt(fim) != ')') {
				msg += ("line [" + count + "] is invalid absence of ')' " + "\n");
				inconsistentLine = true;
				msg_cont += 1;
			}

			// checks to see if there are 3 parameters (<from-state>, <label>, <to-state>)
			line = line.substring(ini + 1, fim);
			String[] val = line.split(",");
			if (val.length != 3) {
				msg += ("line [" + count + "] should have been passed 3 parameters separated by commas" + "\n");
				inconsistentLine = true;
				msg_cont += 1;
			}

			// if the transition line is complete, without inconsistency
			if (!inconsistentLine) {
				// creates states and transitions
				iniState = new State_(val[0].trim());
				endState = new State_(val[2].trim());
				label = val[1].trim();
				if(label.charAt(0) == Constants.OUTPUT_TAG) {
					transition = new Transition_(iniState, label.substring(1, label.length()) , endState);	
					outputs_.add(label.substring(1, label.length()));
				}else {
					if(label.charAt(0) == Constants.INPUT_TAG) {
						transition = new Transition_(iniState, label.substring(1, label.length()) , endState);	
						inputs_.add(label.substring(1, label.length()));
					}else {
						transition = new Transition_(iniState, label, endState);	
					}
					
				}
				
				// //System.out.println(transition);
				// // assigns the attributes to the LTS
				// lts.addState(iniState);
				// lts.addState(endState);
				// lts.addTransition(transition);

				states_.add(iniState);
				states_.add(endState);
				transitions_.add(transition);
				// transitions.add(transition);
				// System.out.println(transitions_.size());
				// System.out.println("ADD");
			}

			count++;
		}

		
		states.addAll(states_);		
		transitions.addAll(transitions_);
		inputs.addAll(inputs_);
		outputs.addAll(outputs_);

	}

	public String getMsg() {
		return msg;
	}
	
	public List<State_> getStates() {
		return states;
	}

	public List<Transition_> getTransitions() {
		return this.transitions;
	}
	
	public List<String> getInputs() {
		return this.inputs;
	}
	
	public List<String> getOutputs() {
		return this.outputs;
	}

	public void initStaticVariables() {
		this.states = new ArrayList<>();
		this.transitions = new ArrayList<>();
		this.inputs = new ArrayList<>();
		this.outputs = new ArrayList<>();
	}

}
