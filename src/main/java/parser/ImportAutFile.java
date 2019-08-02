package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
			IOLTS iolts = createModelFromFile(path);

			if (hasLabelList) {
				// the set of input and output are those passed by parameter
				iolts.setInputs(inputs);
				iolts.setOutputs(outputs);
			}

			// System.out.println(iolts);
			return iolts;
		} catch (Exception e) {
			throw e;
		}
	}

	public static IOLTS createModelFromFile(String path) throws Exception {
		// reads the configuration parameters from the first line of the file
		String[] configs = ImportAutFile_WithoutThread.headerParameters(path);

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
			IOLTS iolts = new IOLTS();
			// Parameters read from the first line of the file
			// definition of the initial state based on the 1st line of the file
			// (configuration)
			iolts.setInitialState(new State_(configs[0]));
			int nTransitions = Integer.parseInt(configs[1].replaceAll("\\s+", ""));
			int nStates = Integer.parseInt(configs[2].replaceAll("\\s+", ""));

			// line counter starts from line 2 because line 1 is the line of
			// configuration
			int count = 2;

			// read file with Thread
			// try {
			long startTime = System.nanoTime();

			// count line of file (number of transitions)
			BufferedReader reader = new BufferedReader(new FileReader(path));
			int totalLines = 0;
			String linee = "";
			while ( linee != null) {
				linee = reader.readLine();
				if(linee!= null && !linee.isEmpty()) {
					totalLines++;
				}				
			}
				
			reader.close();

			totalLines -= 1;// remove header line from count

			int lineForThread = totalLines;
			int constLinesThread = 500;// 500

			if (lineForThread > constLinesThread) {// if have more than 500 transitions use thread
				lineForThread = constLinesThread;
			}

			ReadFile r1 = new ReadFile();

			List<State_> states;
			List<Transition_> transitions;
			List<Transition_> transitions_;
			List<ReadFile> list = new ArrayList<>();

			r1.initIolts();
			while (r1.getIolts().getTransitions().size() != (totalLines)) {
				r1.initIolts();
				list = new ArrayList<>();
				iolts = new IOLTS();
				
				try {

					int ini = 0;
					// create threads
					for (int i = 0; i < (int) totalLines / lineForThread; i++) {
						r1 = new ReadFile();
						r1.init(ini, ini + lineForThread, path);
						ini += lineForThread;
						r1.start();
						list.add(r1);
					}

					// if has rest of division or if will has just one thread
					if ((totalLines) % lineForThread != 0) {
						r1 = new ReadFile();
						r1.init(ini, ini + (totalLines % lineForThread), path);
						ini += lineForThread;
						r1.start();
						list.add(r1);
					}

					// to thread run parallel
					for (ReadFile readFile : list) {
						readFile.join();
					}

					
					iolts = r1.getIolts();
					iolts.setInitialState(new State_(configs[0]));
					
					
//					// get states and transitions
//					states = r1.getStates();
//					transitions = r1.getTransitions();
//
//					// add LTS states/transitions
//					HashSet hashSet_s_ = new LinkedHashSet<>(states);
//					states = new ArrayList<>(hashSet_s_);
//					iolts.setTransitions(transitions);
//					iolts.setStates(states);
//
//					// remove all null from list
//					transitions.removeAll(Collections.singletonList(null));
//
//					// set alphabet
//					List<String> alphabet = transitions.stream().map(Transition_::getLabel)
//							.collect(Collectors.toList());
//					hashSet_s_ = new LinkedHashSet<>(alphabet);
//					alphabet = new ArrayList<>(hashSet_s_);
//					iolts.setAlphabet(alphabet);
//
//					// set inputs
//					hashSet_s_ = new LinkedHashSet<>(r1.getInputs());
//					alphabet = new ArrayList<>(hashSet_s_);
//					alphabet.removeAll(Collections.singletonList(null));
//					iolts.setInputs(alphabet);
//
//					// set outputs
//					hashSet_s_ = new LinkedHashSet<>(r1.getOutputs());
//					alphabet = new ArrayList<>(hashSet_s_);
//					alphabet.removeAll(Collections.singletonList(null));
//					iolts.setOutputs(alphabet);

				} catch (InterruptedException e) {
					// if exception occur run again, initialize static variables
					r1.initIolts();
					e.printStackTrace();
				}

			}

			msg = r1.getMsg();

			if (msg.equals("")) {
				// if there is not the amount of transitions that is defined in the first
				// line
				if (nTransitions != iolts.getTransitions().size()) {
					msg += "Amount of transitions divergent from the value passed in the 1st row \n";
				}

				// if there is not the amount of states that is defined in the first line
				if (nStates != iolts.getStates().size()) {
					msg += "Number of states divergent from the value passed in the 1st line \n";
				}
			}

			// if there is no inconsistency in reading the transitions, you do not need
			// validate if qt of transitions and states beat with configuration (JTorx)
			if (msg_cont == 0) {// if (msg.equals("")) {
				return iolts;
			} else {
				msg = ("inconsistencies in reading the .aut file! \n" + "Path: " + path + "\n" + "Message: \n" + msg);

				throw new Exception(msg);
			}

		}

	}

	/***
	 * Converts the lts from the .aut file to the LTS object
	 * 
	 * @param path
	 * @return LTS underlying the .aut
	 */
	public static LTS autToLTS(String path) throws Exception {
		return createModelFromFile(path).toLTS();
	}


}
