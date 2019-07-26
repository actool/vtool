package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

			// System.out.println(aut);
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
		//BufferedReader reader;
		String thisLine = null;
		String[] split;
		int randNum;
		//int transitionsPercentageToAdd, percentageAdd = 0;
		//int percentageToAdd = 5;
		
		State_ sourceState, targetState;
		IOLTS iolts = new IOLTS();
		try {
			//reader = new BufferedReader(new FileReader(pathModelBase));
			int totalTransitions = 0;

			iolts = ImportAutFile.autToIOLTS(pathModelBase, false, null, null);
			iolts.setAlphabet(ListUtils.union(iolts.getInputs(), iolts.getOutputs()));

			// System.out.println("ORIGINAL >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			// System.out.println(iolts);
			totalTransitions = iolts.getTransitions().size();

			//define max num transition to add, 
			//transitionsPercentageToAdd = (totalTransitions*percentageToAdd)/100;
			
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
			if (iolts.getInputs().contains(t.getLabel())) {
				aut += "(" + t.getIniState() + "," + Constants.INPUT_TAG + t.getLabel() + "," + t.getEndState() + ")"
						+ newline;
			}

			if (iolts.getOutputs().contains(t.getLabel())) {
				aut += "(" + t.getIniState() + "," + Constants.OUTPUT_TAG + t.getLabel() + "," + t.getEndState() + ")"
						+ newline;
			}

		}

		return aut;
	}

	public static void main(String[] args) throws Exception {
		 ////GENERATE <ONE> RANDOM MODEL
//		 int numberOfStates = 50;// 2000;
//		 List<String> labels = Arrays.asList("?a", "?b", "?c", "!x", "!y");// , "?c"
//		 boolean inputEnabled = true;
//		 String tag = "g";
//		 String path = "C:\\Users\\camil\\Desktop\\";
//		 generate(numberOfStates, labels, inputEnabled, tag, path, "spec" +
//		 numberOfStates + "states");

		// GENERATE <ONE> - PERCENTAGE MODEL
		// generateByPercentage("C:\\Users\\camil\\Desktop\\model10states.aut",
		// "C:\\Users\\camil\\Desktop\\", "impl10",10);

		// GENERATE <IN LOTE> - NUM State
		// int totalModels = 50;// 500;
		// int constDivision = 5;
		// int minStates = 20;
		// int maxStates = 40;
		// boolean inputEnabled = true;
		// String tag = "g";
		// String rootPath = "C:\\Users\\camil\\Desktop\\aut - teste de desempenho";
		// String iutAutPath = "C:\\Users\\camil\\Desktop\\model30states.aut";
		// IOLTS ioltsModel = ImportAutFile.autToIOLTS(iutAutPath, false, null, null);
		// List<String> labels = new ArrayList<>();
		// for (String l : ioltsModel.getInputs()) {
		// labels.add(Constants.INPUT_TAG + l);
		// }
		// for (String l : ioltsModel.getOutputs()) {
		// labels.add(Constants.OUTPUT_TAG + l);
		// }
		// generateAutInLot_NumStates(totalModels, constDivision, minStates, maxStates,
		// inputEnabled, tag, rootPath,
		// labels);

////		 GENERATE <IN LOTE> - PERCENTAGE
		int totalModels = 500;// 500;
		String rootPath = "C:\\Users\\camil\\Desktop\\teste desempenho - Everest\\iut50-specPercentage\\spec";
		String iutAutPath = "C:\\Users\\camil\\Desktop\\teste desempenho - Everest\\iut50-specPercentage\\iut50states.aut";
		generateAutInLot_PercentageStates(totalModels, rootPath, iutAutPath);
	}

	public static void generateAutInLot_PercentageStates(int totalModels, String rootPath, String iutAutPath)
			throws IOException {
		int[] percentageVariation = { 20, 40, 60, 80, 100 };
		// String currentFolder;
		int constDivision = (totalModels / percentageVariation.length);
		int count = 0;
		for (int i = 0; i < percentageVariation.length; i++) {
			// new folder
			// currentFolder = rootPath + "/" + percentageVariation[i] + "percent";
			// Files.createDirectories(Paths.get(currentFolder));

			// aut per group
			for (int j = 0; j < constDivision; j++) {
				generateByPercentage(iutAutPath, rootPath, percentageVariation[i] + "pct_spec" + "_" + count,
						percentageVariation[i]);// currentFolder
				count++;
			}
		}

	}

	public static void generateAutInLot_NumStates(int totalModels, int constDivision, int minStates, int maxStates,
			boolean inputEnabled, String tag, String rootPath, List<String> labels) throws IOException {
		// int quantityGroups = totalModels / constDivision;
		int variationNumStates = (maxStates - minStates) / constDivision;
		int countStates = minStates;
		int randomNumStates;

		int residual = ((maxStates - minStates) % constDivision) + 1;
		int count = 0;
		// String currentFolder = "";

		// group (limit num states)
		for (int i = 0; i < constDivision; i++) {
			// new folder
			// if (residual != 0 && i == constDivision - 1) {
			// currentFolder = rootPath + "/" + countStates + "-" + (countStates +
			// variationNumStates - 1 + residual)
			// + "states";
			// } else {
			// currentFolder = rootPath + "/" + countStates + "-" + (countStates +
			// variationNumStates - 1) + "states";
			// }
			// Files.createDirectories(Paths.get(currentFolder));

			// aut per group
			for (int j = 0; j < (totalModels / constDivision); j++) {
				if (residual != 0 && i == constDivision - 1) {
					randomNumStates = getRandomNumberInRange(countStates,
							countStates + variationNumStates - 1 + residual);

				} else {
					randomNumStates = getRandomNumberInRange(countStates, countStates + variationNumStates - 1);
				}

				generate(randomNumStates, labels, inputEnabled, tag, rootPath,
						randomNumStates + "states_spec" + "_" + count + ".aut");// currentFolder

				count++;
			}

			countStates += variationNumStates;
		}
	}

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();

	}
}
