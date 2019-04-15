package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import model.State_;
import model.IOLTS;
import model.LTS;
import model.Transition_;
import util.Constants;

/***
 * Classe que importa e converte arquivo do tipo .aut em objeto LTS e IOLTS
 * 
 * @author camil
 *
 */
public class ImportAutFile {

	/***
	 * Reads and validates the first line of the .aut file, in the first line of the
	 * file des(<initial-state>, <number-of-transitions>, <number-of-states>)
	 * 
	 * @param path
	 *            the file directory
	 * @return the settings in the first line
	 */
	public static String[] headerParameters(String path) {
		String[] configs = new String[4];
		String lineConfig = "";
		try {
			File file = new File(path);
			Scanner sc = new Scanner(file);
			// first line of file
			// des (<initial-state>, <number-of-transitions>, <number-of-states>)
			// String lineConfiguration = "des (1, 2, 3)";
			lineConfig = sc.nextLine();
			// removes the space that you may have at the end of the configuration string
			lineConfig = lineConfig.replaceAll("\\s+$", "");
			// index of the end bracket
			int idxEndBracket = lineConfig.length() - 1;

			String msg = "";
			// check for final bracket
			if (lineConfig.charAt(idxEndBracket) != ')') {
				msg += ("first line of the file invalid! absence of ')' " + "\n");
			}

			// index of the beginning of bracket
			int idxIniBracket = lineConfig.indexOf("(");
			if (idxIniBracket < 0) {
				msg += ("first line of the file invalid! absence of '(' " + "\n");
			}

			// if you have found the beginning and end of the bracket
			if (msg == "") {
				// substring with the configuration data
				lineConfig = lineConfig.substring(idxIniBracket + 1, idxEndBracket);
			}
			// take the settings that are on the line
			configs = lineConfig.split(",");
			// if there are less than 3 parameters
			if (configs.length < 3) {
				msg += ("3 comma-separated parameters should have been passed in the first line" + "\n");
				// if any parameter is missing, assign empty
				for (int i = configs.length; i < 3; i++) {
					configs = append(configs, "");
				}
			}
			// if you have more than 3 parameters consider the first 3
			if (configs.length > 3) {
				configs = new String[4];
				// if it is missing some parameter assigns empty
				for (int i = 0; i < 3; i++) {
					configs = append(configs, "");
				}
			}
			configs = append(configs, msg);
		} catch (FileNotFoundException e) {
			System.err.println("Error reading file:");
			System.err.println(e);
		}

		return configs;
	}

	/***
	 * Add element to array[]
	 * 
	 * @param arr
	 *            array which will receive new element
	 * @param element
	 *            element to be added to array
	 * @return array after adding the element
	 */
	static <T> T[] append(T[] arr, T element) {
		final int N = arr.length;
		// create a copy of the array with one more position
		arr = Arrays.copyOf(arr, N + 1);
		// add the element in the last position of the array
		arr[N] = element;
		return arr;
	}

	/***
	 * Converts the iolts from the .aut file to an IOLTS object
	 * 
	 * @param path
	 *            file directory
	 * @param hasLabelList
	 *            if the inputs and outputs are differentiated by the symbols? /!
	 * @param inputs
	 *            the input alphabet
	 * @param outputs
	 *            the output alphabet
	 * @return IOLTS underlying the .aut
	 */
	public static IOLTS autToIOLTS(String path, boolean hasLabelList, ArrayList<String> inputs,
			ArrayList<String> outputs) throws Exception {

		try {
			// converts .aut to LTS
			LTS lts = autToLTS(path);
			// creates a new LTS-based IOLTS
			IOLTS iolts = new IOLTS(lts);

			ArrayList<String> e = new ArrayList<String>();
			ArrayList<String> s = new ArrayList<String>();

			// changes the set of inputs and outputs
			// if the input and output labels are differentiated by the symbols? /!
			if (!hasLabelList) {
				// runs through the entire LTS alphabet
				for (String a : lts.getAlphabet()) {
					// if it starts with ! so it's an output symbol
					if (a.charAt(0) == Constants.OUTPUT_TAG) {
						s.add(a.substring(1, a.length()));
					}

					// if it starts with ? so it's an input symbol
					if (a.charAt(0) == Constants.INPUT_TAG) {
						e.add(a.substring(1, a.length()));
					}
				}

				// add IOLTS inputs and outputs
				iolts.setInputs(e);
				iolts.setOutputs(s);

				ArrayList<Transition_> transicoes = new ArrayList<Transition_>();
				String label = "";

				
				for (Transition_ t : iolts.getTransitions()) {
					// remove the transitions from the labels ! ?
					label = t.getLabel().substring(1, t.getLabel().length());
					//check whether the labels with !/? were defined in the file
					if (e.contains(label) || s.contains(label)) {
						transicoes.add(new Transition_(t.getIniState(), label, t.getEndState()));
					}
				}

				iolts.setTransitions(transicoes);
			} else {// if the symbols !/? do not differentiate
				// the set of input and output are those passed by parameter
				iolts.setInputs(inputs);
				iolts.setOutputs(outputs);
			}

			return iolts;
		} catch (Exception e) {
			throw e;
		}
	}

	/***
	 * Converts the lts from the .aut file to the LTS object
	 * 
	 * @param path
	 * @return LTS underlying the .aut
	 */
	public static LTS autToLTS(String path) throws Exception {
		// reads the configuration parameters from the first line of the file
		String[] configs = ImportAutFile.headerParameters(path);

		// message if there was an error / inconsistency in reading the first line
		String msg = configs[3];

		int msg_cont = 0;

		// if there is inconsistency in the first line of the file
		if (msg != "") {
			msg += ("expected format:" + "\n");
			msg += ("'des(<initial-state>, <number-of-transitions>, <number-of-states>)'" + "\n");
			System.out.println(msg);

			throw new Exception(msg);
		} else {
			// error messages on inconsistencies with each line
			msg = "";
			State_ iniState = null;
			State_ endState = null;
			Transition_ transition = null;

			// read file line
			String line = "";
			boolean inconsistentLine = false;

			// new lts that will be built based on the file
			LTS lts = new LTS();
			// Parameters read from the first line of the file
			// definition of the initial state based on the 1st line of the file
			// (configuration)
			lts.setInitialState(new State_(configs[0]));
			int nTransitions = Integer.parseInt(configs[1].replaceAll("\\s+", ""));
			int nStates = Integer.parseInt(configs[2].replaceAll("\\s+", ""));

			// line counter starts from line 2 because line 1 is the line of
			// configuration
			int count = 2;
			try {
				File file = new File(path);
				Scanner sc = new Scanner(file);
				// skip the first line of configuration
				sc.nextLine();
				// if there is line of the file to be read
				while (sc.hasNextLine()) {
					inconsistentLine = false;
					// reads the line with the transition
					// each transition is configured as follows: (<from-state>, <label>,
					// <to-state>)
					line = sc.nextLine();

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
						transition = new Transition_(iniState, val[1].trim(), endState);

						// assigns the attributes to the LTS
						lts.addState(iniState);
						lts.addState(endState);
						lts.addTransition(transition);
					}
					// number of transitions
					count++;
				}

				if (msg.equals("")) {
					// if there is not the amount of transitions that is defined in the first
					// line
					if (nTransitions != lts.getTransitions().size()) {
						msg += "Amount of transitions divergent from the value passed in the 1st row \n";
					}

					// if there is not the amount of states that is defined in the first line
					if (nStates != lts.getStates().size()) {
						msg += "Number of states divergent from the value passed in the 1st line \n";
					}
				}

			} catch (Exception e) {
				throw new Exception("Error converting file to LTS");
			}

			// if there is no inconsistency in reading the transitions, you do not need
			// validate if qt of transitions and states beat with configuration (JTorx)
			if (msg_cont == 0) {// if (msg.equals("")) {
				return lts;
			} else {
				msg = ("inconsistencies in reading the .aut file! \n" + "Path: " + path + "\n" + "Message: \n" + msg);

				throw new Exception(msg);
			}

		}
	}

}
