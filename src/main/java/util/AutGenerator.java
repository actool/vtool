package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.Operation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.ListUtils;

import algorithm.Operations;
import model.IOLTS;
import model.State_;
import model.Transition_;
import parser.ImportAutFile;
import parser.ImportAutFile_WithoutThread;

public class AutGenerator {

	public static void generate(int numberOfStates, List<String> labels, boolean inputEnabled, String tag, String path,
			String autFileName) {
		int qtTransition = 0;
		String transitions = "";
		File file = new File(path, autFileName + ".aut");
		List<String> notVisited = new ArrayList<String>();
		int countState = 0;
		String endState = "", iniState = "";

		notVisited.add(tag + countState);

		Random rand = new Random();
		BufferedWriter writer = null;
		String newline = System.getProperty("line.separator");
		boolean teraTransicao;

		int idx = 0;

		iniState = notVisited.remove(0);

		while (iniState != null && (countState != (numberOfStates))) {// enquanto não haver a quantidade de estados

			idx = rand.nextInt(labels.size());

			for (String l : labels) {

				if (l.charAt(0) == Constants.OUTPUT_TAG) {// output label
					teraTransicao = rand.nextInt(2) == 1 ? true : false;

				} else {
					teraTransicao = inputEnabled;// input complete
					if (!teraTransicao && rand.nextInt(2) == 1) {// ter transição com este rótulo
						teraTransicao = true;
					}
				}

				if (labels.indexOf(l) == idx || teraTransicao) {// ter transição com este rótulo

					if (countState + 1 != (numberOfStates) && labels.indexOf(l) == idx) {
						countState++;
						endState = tag + countState;
						notVisited.add(endState);
					} else {

						if (countState > 0) {
							endState = tag + rand.nextInt(countState);
						} else {
							endState = iniState;
						}

					}

					transitions += "(" + iniState + ", " + l + ", " + endState + ")" + newline;
					qtTransition++;

				}

			}
			if (notVisited.size() == 0 && countState < numberOfStates) {
				countState++;
				notVisited.add(tag + countState);
			}
			if (notVisited.size() > 0) {
				iniState = notVisited.remove(0);
			} else {
				iniState = null;
			}

		}

		try {
			String header = "des(" + tag + "0," + qtTransition + ", " + numberOfStates + ")" + newline;
			String aut = header + transitions;

			writer = new BufferedWriter(new FileWriter(file));
			writer.write(aut);

			System.out.println(aut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	public static void generateByPercentage(String pathModelBase, String pathNewFile, String autFileName,
			double percentage) {
		BufferedReader reader;
		String thisLine = null;
		String[] split;
		int randNum;

		State_ sourceState, targetState;
		IOLTS iolts = new IOLTS();
		try {
			reader = new BufferedReader(new FileReader(pathModelBase));
			int totalTransitions = 0;

			iolts = ImportAutFile.autToIOLTS(pathModelBase, false, null, null);
			iolts.setAlphabet(ListUtils.union(iolts.getInputs(), iolts.getOutputs()));
			// System.out.println("ORIGINAL >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			// System.out.println(iolts);
			totalTransitions = iolts.getTransitions().size();

			// define number of transitions to modify
			List<Integer> lines = new ArrayList<>();
			int numberLinesToChange = (int) (((totalTransitions) * percentage) / 100);
			int line = 0;

			Random rand = new Random();
			// choose lines to change
			while (lines.size() < numberLinesToChange) {
				line = rand.nextInt(totalTransitions);
				if (!lines.contains(line))
					lines.add(line);
			}

			int numberOfStates = iolts.getStates().size();
			List<Transition_> transitionsToRemove = new ArrayList<>();
			Transition_ transition;
			State_ randState;
			// modify transitions
			for (int i = 0; i < lines.size(); i++) {
				// 0)remove, 1)add or 2)alter transition
				randNum = rand.nextInt(3);

				// remove transition
				if (randNum == 0) {
					transitionsToRemove.add(iolts.getTransitions().get(lines.get(i)));
				} else {
					// add transition
					if (randNum == 1) {
						iolts.addTransition(new Transition_(iolts.getStates().get(rand.nextInt(numberOfStates)),
								iolts.getAlphabet().get(rand.nextInt(iolts.getAlphabet().size())),
								iolts.getStates().get(rand.nextInt(numberOfStates))));
					} else {
						// alter transition
						// 0)souce state, 1)label or 2)target state
						randNum = rand.nextInt(3);
						transition = iolts.getTransitions().get(lines.get(i));
						randState = iolts.getStates().get(rand.nextInt(numberOfStates));
						if (randNum == 0) {// alter source state
							iolts.getTransitions().set(lines.get(i),
									new Transition_(randState, transition.getLabel(), transition.getEndState()));
						} else {
							if (randNum == 1) {// alter label
								iolts.getTransitions().set(lines.get(i),
										new Transition_(transition.getIniState(),
												iolts.getAlphabet().get(rand.nextInt(iolts.getAlphabet().size())),
												transition.getEndState()));
							} else {// alter target state
								iolts.getTransitions().set(lines.get(i),
										new Transition_(transition.getIniState(), transition.getLabel(), randState));
							}
						}
					}
				}
			}

			for (Transition_ t : transitionsToRemove) {
				iolts.getTransitions().remove(t);

			}
			
			
			File file = new File(pathNewFile, autFileName + ".aut");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(ioltsToAut(iolts));
			writer.close();
			// System.out.println("ALTERADO >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			// System.out.println(iolts);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String ioltsToAut(IOLTS iolts) {
		String newline = System.getProperty("line.separator");
		String aut = "des(" + iolts.getInitialState() + "," + iolts.getTransitions().size() + ","
				+ iolts.getStates().size() + ")" + newline;

		for (Transition_ t : iolts.getTransitions()) {
			aut += "(" + t.getIniState() + "," + t.getLabel() + "," + t.getEndState() + ")" + newline;
		}

		return aut;
	}

	public static void main(String[] args) {
		// int numberOfStates = 10;// 2000;
		// List<String> labels = Arrays.asList("?a", "?b", "!x", "!y");// , "?c"
		// boolean inputEnabled = true;
		// String tag = "g";
		// String path = "C:\\Users\\camil\\Desktop\\";
		// generate(numberOfStates, labels, inputEnabled, tag, path, "model" +
		// numberOfStates + "states");

		generateByPercentage("C:\\Users\\camil\\Desktop\\model10states.aut", "C:\\Users\\camil\\Desktop\\", "impl10",
				10);

	}
}
