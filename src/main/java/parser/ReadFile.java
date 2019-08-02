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

import model.IOLTS;
import model.LTS;
import model.State_;
import model.Transition_;
import util.Constants;

public class ReadFile extends Thread {
	private int iniLine;
	private int endLine;
	private String file;

	private static IOLTS iolts = new IOLTS();

	private static String msg = "";

	public void init(int iniLine, int endLine, String file) {
		this.iniLine = iniLine;
		this.endLine = endLine;
		this.file = file;

	}

	public void run() {
		List<Transition_> transitions_ = new ArrayList<>();
		List<State_> states_ = new ArrayList<>();
		List<String> inputs_ = new ArrayList<>();
		List<String> outputs_ = new ArrayList<>();

		List<Transition_> transitions = new ArrayList<>();
		List<State_> states = new ArrayList<>();
		List<String> inputs = new ArrayList<>();
		List<String> outputs = new ArrayList<>();

		int count = this.iniLine;
		String line;
		Supplier<Stream<String>> streamSupplier;
		boolean inconsistentLine;

		int msg_cont = 0;
		State_ iniState, endState;
		Transition_ transition;
		String label;

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

		
		while (count < this.endLine) {
			
			// reads the line with the transition
			// each transition is configured as follows: (<from-state>, <label>,
			// <to-state>)
			line = streamSupplier.get().skip(count + 1).findFirst().get();
			if (!line.isEmpty()) {
				// System.out.println("line" + (count + 1));
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
					if (label.charAt(0) == Constants.OUTPUT_TAG) {
						transition = new Transition_(iniState, label.substring(1, label.length()), endState);
						outputs_.add(label.substring(1, label.length()));
					} else {
						if (label.charAt(0) == Constants.INPUT_TAG) {
							transition = new Transition_(iniState, label.substring(1, label.length()), endState);
							inputs_.add(label.substring(1, label.length()));
						} else {
							transition = new Transition_(iniState, label, endState);
						}

					}

					states_.add(iniState);
					states_.add(endState);
					transitions_.add(transition);

				}
			}
			count++;

		}

		states = iolts.getStates();
		states.addAll(states_);
		HashSet hashSet_s_ = new LinkedHashSet<>(states);
		states = new ArrayList<>(hashSet_s_);
		iolts.setStates(states);

		transitions = iolts.getTransitions();
		transitions.addAll(transitions_);
		hashSet_s_ = new LinkedHashSet<>(transitions);
		transitions = new ArrayList<>(hashSet_s_);
		iolts.setTransitions(transitions);

		outputs = iolts.getOutputs();
		outputs.addAll(outputs_);
		hashSet_s_ = new LinkedHashSet<>(outputs);
		outputs = new ArrayList<>(hashSet_s_);
		iolts.setOutputs(outputs);

		inputs = iolts.getInputs();
		inputs.addAll(inputs_);
		hashSet_s_ = new LinkedHashSet<>(inputs);
		inputs = new ArrayList<>(hashSet_s_);
		iolts.setInputs(inputs);

	}

	public String getMsg() {
		return msg;
	}

	public IOLTS getIolts() {
		return this.iolts;
	}

	public void initIolts() {
		this.iolts = new IOLTS();
	}

}
