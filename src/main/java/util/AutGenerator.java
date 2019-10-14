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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.net.io.SocketOutputStream;

import algorithm.Operations;
import model.IOLTS;
import model.State_;
import model.Transition_;
import parser.ImportAutFile;
import parser.ImportAutFile_WithoutThread;

public class AutGenerator {
	public static void main(String[] args) throws Exception {
		//// GENERATE <ONE> RANDOM MODEL
		//int nState = 3;// 2000;
		// List<String> labels = Arrays.asList("?a", "?b", "?c", "?d", "?e", "!x", "!y",
		// "!z", "!w", "!k");// , "?c"
		// boolean inputEnabled = true;
		// String tag = "g";
		// String path = "C:\\Users\\camil\\Desktop\\";
		// generate(nState, labels, inputEnabled, tag, path,
		// nState + "states_spec", System.currentTimeMillis());

		// GENERATE <ONE> - PERCENTAGE MODEL
		// generateByPercentage("C:\\Users\\camil\\Desktop\\model10states.aut",
		// "C:\\Users\\camil\\Desktop\\", "impl10",10);

		// GENERATE <IN LOTE> - NUM State
		// int totalModels = 50;// 500;
		// int constDivision = 5;
		// int minStates = 1950;
		// int maxStates = 2050;
		// boolean inputEnabled = false;
		// String tag = "g";
		// String rootPath = "C:\\Users\\camil\\Desktop\\models\\";
		// String iutAutPath = "C:\\Users\\camil\\Desktop\\Nova pasta
		// (2)\\versao3-iut30-specPercentage\\iut30states.aut";
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

		//// GENERATE <IN LOTE> - PERCENTAGE
		// int percentage = 60;
		// long seed = System.currentTimeMillis();
		// boolean inputEnabled = true;
		//
		// for (int j = 0; j < 10; j++) {
		// generateByPercentage("C:\\Users\\camil\\Desktop\\25-100\\3\\" + nState +
		// "states_spec.aut",
		// "C:\\Users\\camil\\Desktop\\25-100\\" + nState + "\\iut", percentage +
		// "pct_iut_" + j, percentage,
		// "g", seed, inputEnabled);
		// }

		//// GENERATE <IN LOTE> - PERCENTAGE (models ioco conf)
//		String nState = "3000";
//		String rootPathIUTs = "C:\\Users\\camil\\Google Drive\\UEL\\svn\\ferramenta\\teste desempenho\\models25-3000states-ioco-perc-dif\\"+nState+"\\iut\\";
//		String rootSpec = "C:\\Users\\camil\\Google Drive\\UEL\\svn\\ferramenta\\teste desempenho\\models250-3000states-ioco-conf\\aut\\spec\\";
//		double percentage = 1;
//		String tag = "g";
//		File folder = new File(rootPathIUTs);
//		File[] listOfFiles = folder.listFiles();
//		boolean inputEnabled = true;
//
//		for (File file : listOfFiles) {
//			if (file.getName().indexOf(".") != -1
//					&& file.getName().substring(file.getName().indexOf(".")).equals(".aut")) {
//				generateByPercentageModelsIocoConf(rootPathIUTs + file.getName(), rootSpec,
//						nState+"states"+file.getName().replace("iut", "spec").replace(".aut", ""), percentage, tag,
//						System.currentTimeMillis(), inputEnabled);
//
//			}
//			
//		}

		
		// print feature models
//		String pathIUT, rootPathIUTs = "C:\\Users\\camil\\Desktop\\aa\\" + nState + "\\iut\\";
//		File folder = new File(rootPathIUTs);
//		File[] listOfFiles = folder.listFiles();
//
//		for (File file : listOfFiles) {
//			if (file.getName().indexOf(".") != -1
//					&& file.getName().substring(file.getName().indexOf(".")).equals(".aut")) {
//				pathIUT = rootPathIUTs + file.getName();
//
//				IOLTS iolts = ImportAutFile_WithoutThread.autToIOLTS(pathIUT, false, null, null);
//				IOLTS iolts_ = ImportAutFile_WithoutThread.autToIOLTS(
//						"C:\\Users\\camil\\Desktop\\aa\\" + nState + "\\" + nState + "states_spec.aut", false, null,
//						null);
//				iolts_.setAlphabet(ListUtils.union(iolts.getInputs(), iolts.getOutputs()));
//				System.out.println("n-transicao: " + iolts.getTransitions().size() + " iguais: "
//						+ iolts_.equalsTransitions(iolts).size() + " - diferentes: "
//						+ iolts_.numberDistinctTransitions(iolts) + " - inp Enab: " + iolts.isInputEnabled()
//						+ " - determin: " + iolts.ioltsToAutomaton().isDeterministic() + " > " + file);
//			}
//		}

	}

	// criar specs com base na iut
	public static void generateByPercentageModelsIocoConf(String pathIUTlBase, String pathNewFile, String autFileName,
			double percentage, String tag, long seed, boolean inputEnabled) {
		try {
			Random rand = new Random();
			rand.setSeed(seed * System.currentTimeMillis());
			IOLTS iolts = ImportAutFile_WithoutThread.autToIOLTS(pathIUTlBase, false, null, null);

			int numberTransitionsToAdd = (int) ((Math.ceil(iolts.getTransitions().size()) * percentage) / 100);

			if (inputEnabled) {
				numberTransitionsToAdd = (int) Math.ceil(numberTransitionsToAdd / iolts.getInputs().size());
				numberTransitionsToAdd = numberTransitionsToAdd==0?iolts.getInputs().size():numberTransitionsToAdd;
			}
			

			int contState = iolts.getStates().size();
			State_ iniState, endState;
			List<Transition_> transitions = new ArrayList<>();

			for (int i = 0; i < numberTransitionsToAdd; i++) {
				iniState = new State_(tag + Objects.toString(contState));
				iolts.addState(iniState);

				for (String l : iolts.getAlphabet()) {
					endState = new State_(tag + Objects.toString(rand.nextInt(iolts.getStates().size() - 1)));
					if (inputEnabled) {
						if (iolts.getInputs().contains(l)) {
							transitions.add(new Transition_(iniState, l, endState));
						}
					} else {

						transitions.add(new Transition_(iniState, l, endState));

					}

				}
				
				if(numberTransitionsToAdd == transitions.size()) {
					break;
				}
				contState++;
			}

			for (Transition_ transition : transitions) {
				iolts.addTransition(transition);
			}

			File file = new File(pathNewFile, autFileName + ".aut");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(ioltsToAut(iolts));
			writer.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateByPercentage(String pathModelBase, String pathNewFile, String autFileName,
			double percentage, String tag, long seed, boolean inputEnabled) {
		// BufferedReader reader;
		String thisLine = null;
		String[] split;
		int randNum;
		boolean runAgain;
		// int transitionsPercentageToAdd, percentageAdd = 0;
		// int percentageToAdd = 5;

		State_ sourceState, targetState;
		IOLTS iolts = new IOLTS();
		IOLTS iolts_base = new IOLTS();
		try {

			int totalTransitions = 0;

			iolts = ImportAutFile.autToIOLTS(pathModelBase, false, null, null);
			iolts.setAlphabet(ListUtils.union(iolts.getInputs(), iolts.getOutputs()));

			iolts_base = ImportAutFile.autToIOLTS(pathModelBase, false, null, null);
			iolts_base.setAlphabet(ListUtils.union(iolts.getInputs(), iolts.getOutputs()));

			totalTransitions = iolts.getTransitions().size();

			// define max num transition to add,
			// transitionsPercentageToAdd = (totalTransitions*percentageToAdd)/100;

			// define number of transitions to modify
			List<Integer> lines = new ArrayList<>();
			int numberLinesToChange = (int) (((totalTransitions) * percentage) / 100);
			int line = 0;

			Random rand = new Random();
			rand.setSeed(seed * System.currentTimeMillis());
			// choose lines to change
			// while (lines.size() < totalTransitions) {
			// line = rand.nextInt(totalTransitions);
			// if (!lines.contains(line))
			// lines.add(line);
			// }

			int numberOfStates = iolts.getStates().size();
			List<Integer> transitionsToRemove = new ArrayList<>();
			Transition_ transition;

			State_ randState;
			// int j = 0;
			State_ s_aux;
			List<String> l_aux = null;
			List<Transition_> addTransitions = new ArrayList<>();
			int sortedLine = -1;

			// modify transitions
			while (iolts.numberDistinctTransitions(iolts_base) < numberLinesToChange) {
				// System.out.println("numberDistinctTransitions: " +
				// iolts.numberDistinctTransitions(iolts_base) + " numberLinesToChange:
				// "+numberLinesToChange);

				transitionsToRemove = new ArrayList<>();
				lines = new ArrayList<>();
				for (Transition_ t : iolts.equalsTransitions(iolts_base)) {
					for (int i = 0; i < iolts.getTransitions().size(); i++) {
						if (t.equals(iolts.getTransitions().get(i))) {
							lines.add(i);
							lines = lines.stream().distinct().collect(Collectors.toList());

						}
					}
				}

				lines = new ArrayList<>(lines);

				// 0)remove, 1)add or 2)alter transition
				// randNum = rand.nextInt(2)+1;//without remove
				// randNum = rand.nextInt(3);

				// // remove transition
				// if (randNum == 0) {
				//
				// do {
				//
				// sortedLine = rand.nextInt(lines.size() - 1);
				//
				// transition = iolts.getTransitions().get(lines.get(sortedLine));
				// } while (Integer
				// .parseInt(transition.getEndState().toString().replace(tag,
				// "")) == (Integer.parseInt(transition.getIniState().toString().replace(tag,
				// "")) + 1)
				// || transitionsToRemove.contains(lines.get(sortedLine)));
				//
				// transitionsToRemove.add(lines.get(sortedLine));
				// lines.remove(sortedLine);
				//
				// } else {
				// // add transition
				// if (randNum == 1) {
				//
				// int count = 0;
				// // dont add transition deterministic
				// do {
				// runAgain = false;
				// s_aux = iolts.getStates().get(rand.nextInt(numberOfStates));
				// l_aux = iolts.labelNotDefinedOnState(s_aux.getName());
				//
				// if (l_aux.size() == 0) {
				// runAgain = true;
				// break;
				// } else {
				// for (Transition_ t : addTransitions) {
				// if (t.getIniState().equals(s_aux)) {
				// if (l_aux.contains(t.getLabel())) {
				// l_aux.remove(t.getLabel());
				// if (l_aux.size() == 0) {
				// runAgain = true;
				// break;
				// }
				// }
				// }
				// }
				// }
				//
				// count++;
				//
				// } while (runAgain && count < 15);
				//
				// if (!runAgain) {
				// String label = l_aux.get(rand.nextInt(l_aux.size()));
				// addTransitions.add(new Transition_(s_aux, label.substring(1, label.length()),
				// iolts.getStates().get(rand.nextInt(numberOfStates))));
				// }

				// } else {
				// alter transition
				// 0)souce state, 1)label or 2)target state
				randNum = rand.nextInt(3);

				if (lines.size() - 1 > 0) {
					sortedLine = rand.nextInt(lines.size() - 1);
				} else {
					sortedLine = rand.nextInt(lines.size());
				}

				// System.out.println("n-transição: " + iolts.getTransitions().size() + "
				// sorted-line: " + sortedLine + " n-lines: " + lines.size() + " line-get: " +
				// lines.get(sortedLine));
				transition = iolts.getTransitions().get(lines.get(sortedLine));

				if (randNum == 0) {// alter source state
					do {
						randState = iolts.getStates().get(rand.nextInt(numberOfStates));

					} while (randState.equals(transition.getIniState()));

					iolts.getTransitions().set(lines.get(sortedLine),
							new Transition_(randState, transition.getLabel(), transition.getEndState()));
					lines.remove(sortedLine);
				} else {
					// dont alter transition that keeps iolts initially connected
					while (Integer.parseInt(transition.getEndState().toString().replace(tag,
							"")) == (Integer.parseInt(transition.getIniState().toString().replace(tag, "")) + 1)) {

						sortedLine = rand.nextInt(lines.size() - 1);
						transition = iolts.getTransitions().get(lines.get(sortedLine));
					}
					do {
						randState = iolts.getStates().get(rand.nextInt(numberOfStates));

					} while (randState.equals(transition.getEndState()));
					iolts.getTransitions().set(lines.get(sortedLine),
							new Transition_(transition.getIniState(), transition.getLabel(), randState));

					lines.remove(sortedLine);
				}

				// }
				// }

				// Collections.sort(transitionsToRemove, Collections.reverseOrder());
				//
				// // remove sorted transitions
				// for (Integer i : transitionsToRemove) {
				// Transition_ t = iolts.getTransitions().get(i);// (i > 0 ? i - 1 : i);
				// iolts.getTransitions().remove(t);
				// }

				int addInp = 0;
				if (inputEnabled) {
					for (String l : iolts.getInputs()) {
						for (State_ s : iolts.getStates()) {
							if (iolts.reachedStates(s.getName(), l).size() == 0) {
								iolts.getTransitions().add(new Transition_(s, l, iolts.getStates()
										.get(getRandomNumberInRange(0, iolts.getStates().size() - 1, seed))));
								addInp++;
							}
						}
					}
				}

				if (addInp < addTransitions.size()) {
					// add added transitions
					iolts.getTransitions().addAll(addTransitions.subList(addInp, addTransitions.size()));
				}

				int removeToDet = 0;
				// if non deterministic
				for (int i = 0; i < iolts.getTransitions().size(); i++) {
					transition = iolts.getTransitions().get(i);
					if (iolts.reachedStates(transition.getIniState().getName(), transition.getLabel()).size() > 1) {
						iolts.getTransitions().remove(transition);
						removeToDet++;
					}
				}

			}

			// System.out.println(iolts.getTransitions());
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

	public static void generate(int numberOfStates, List<String> labels, boolean inputEnabled, String tag, String path,
			String autFileName, long seed) throws Exception {

		if (!Files.exists(Paths.get(path))) {
			Files.createDirectory(Paths.get(path));
		}

		int qtTransition = 0;
		String transitions = "";
		File file = new File(path, autFileName + ".aut");
		List<String> notVisited = new ArrayList<String>();
		int countState = 0;
		String endState = "", iniState = "";

		notVisited.add(tag + countState);

		Random rand = new Random();
		rand.setSeed(seed * System.currentTimeMillis());
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

	// public static void generateByPercentage(String pathModelBase, String
	// pathNewFile, String autFileName,
	// double percentage) {
	// // BufferedReader reader;
	// String thisLine = null;
	// String[] split;
	// int randNum;
	// // int transitionsPercentageToAdd, percentageAdd = 0;
	// // int percentageToAdd = 5;
	//
	// State_ sourceState, targetState;
	// IOLTS iolts = new IOLTS();
	// try {
	// // reader = new BufferedReader(new FileReader(pathModelBase));
	// int totalTransitions = 0;
	//
	// iolts = ImportAutFile.autToIOLTS(pathModelBase, false, null, null);
	// iolts.setAlphabet(ListUtils.union(iolts.getInputs(), iolts.getOutputs()));
	//
	// // System.out.println("ORIGINAL >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	// // System.out.println(iolts);
	// totalTransitions = iolts.getTransitions().size();
	//
	// // define max num transition to add,
	// // transitionsPercentageToAdd = (totalTransitions*percentageToAdd)/100;
	//
	// // define number of transitions to modify
	// List<Integer> lines = new ArrayList<>();
	// int numberLinesToChange = (int) (((totalTransitions) * percentage) / 100);
	// int line = 0;
	//
	// Random rand = new Random();
	// // choose lines to change
	// while (lines.size() < numberLinesToChange) {
	// line = rand.nextInt(totalTransitions);
	// if (!lines.contains(line))
	// lines.add(line);
	// }
	//
	// int numberOfStates = iolts.getStates().size();
	// List<Transition_> transitionsToRemove = new ArrayList<>();
	// Transition_ transition;
	// State_ randState;
	// // modify transitions
	// for (int i = 0; i < lines.size(); i++) {
	// // 0)remove, 1)add or 2)alter transition
	// randNum = rand.nextInt(3);
	//
	// // remove transition
	// if (randNum == 0) {
	// transitionsToRemove.add(iolts.getTransitions().get(lines.get(i)));
	// } else {
	// // add transition
	// if (randNum == 1) {
	// iolts.addTransition(new
	// Transition_(iolts.getStates().get(rand.nextInt(numberOfStates)),
	// iolts.getAlphabet().get(rand.nextInt(iolts.getAlphabet().size())),
	// iolts.getStates().get(rand.nextInt(numberOfStates))));
	// } else {
	// // alter transition
	// // 0)souce state, 1)label or 2)target state
	// randNum = rand.nextInt(3);
	// transition = iolts.getTransitions().get(lines.get(i));
	// randState = iolts.getStates().get(rand.nextInt(numberOfStates));
	// if (randNum == 0) {// alter source state
	// iolts.getTransitions().set(lines.get(i),
	// new Transition_(randState, transition.getLabel(), transition.getEndState()));
	// } else {
	// if (randNum == 1) {// alter label
	// iolts.getTransitions().set(lines.get(i),
	// new Transition_(transition.getIniState(),
	// iolts.getAlphabet().get(rand.nextInt(iolts.getAlphabet().size())),
	// transition.getEndState()));
	// } else {// alter target state
	// iolts.getTransitions().set(lines.get(i),
	// new Transition_(transition.getIniState(), transition.getLabel(), randState));
	// }
	// }
	// }
	// }
	// }
	//
	// for (Transition_ t : transitionsToRemove) {
	// iolts.getTransitions().remove(t);
	//
	// }
	//
	// File file = new File(pathNewFile, autFileName + ".aut");
	// BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	// writer.write(ioltsToAut(iolts));
	// writer.close();
	// // System.out.println("ALTERADO >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	// // System.out.println(iolts);
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	public static String ioltsToAut(IOLTS iolts) {
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

	public static void generateAutInLot_PercentageStates(int totalModels, String rootPath, String iutAutPath, long seed,
			boolean inputEnabled) throws IOException {
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
						percentageVariation[i], "g", seed, inputEnabled);// currentFolder
				count++;
			}
		}

	}

	public static void generateAutInLot_NumStates(int totalModels, int constDivision, int minStates, int maxStates,
			boolean inputEnabled, String tag, String rootPath, List<String> labels, long seed) throws Exception {
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
							countStates + variationNumStates - 1 + residual, seed);

				} else {
					randomNumStates = getRandomNumberInRange(countStates, countStates + variationNumStates - 1, seed);
				}

				generate(randomNumStates, labels, inputEnabled, tag, rootPath,
						randomNumStates + "states_spec" + "_" + count, seed);// currentFolder

				count++;
			}

			countStates += variationNumStates;
		}
	}

	private static int getRandomNumberInRange(int min, int max, long seed) {

		Random r = new Random();
		r.setSeed(seed * System.currentTimeMillis());
		return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();

	}
}
