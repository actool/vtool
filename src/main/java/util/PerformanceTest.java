package util;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import algorithm.IocoConformance;
import algorithm.LanguageBasedConformance;
import algorithm.Operations;
import model.Automaton_;
import model.IOLTS;
import model.LTS;
import parser.ImportAutFile;

public class PerformanceTest {

	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}

	public static IOLTS getModel(String path) throws Exception {
		return ImportAutFile.autToIOLTS(path, false, new ArrayList<String>(), new ArrayList<String>());
	}

	public static void getPerformanceExecutionInLot(String pathIUT, String rootPathModels, String pathCsv, boolean ioco, String D, String F) {
		Automaton_ complianceAutomaton;
		String failPath;
		LTS S_, I_;
		String pathModel;
		
		File folder = new File(rootPathModels);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			pathModel = rootPathModels+file.getName();
			try {
				// Models IOLTS inp/out(?/!)
				IOLTS S = getModel(pathModel);
				S.addQuiescentTransitions();

				IOLTS I = getModel(pathIUT);
				I.addQuiescentTransitions();

				long startTime = System.nanoTime();
				// Get the Java runtime
				Runtime runtime = Runtime.getRuntime();
				// Run the garbage collector
				runtime.gc();

				// IOCO
				if (ioco) {
					complianceAutomaton = IocoConformance.verifyIOCOConformance(S, I);
					failPath = Operations.path(S, I, complianceAutomaton, true);
				} else {
					// LANGUAGE BASED
					S_ = S.toLTS();
					I_ = I.toLTS();

					if (D.isEmpty() && F.isEmpty()) {
						D = "(";
						List<String> alphabets = new ArrayList<>();
						alphabets.addAll(S_.getAlphabet());
						alphabets.addAll(I_.getAlphabet());
						for (String l : new ArrayList<>(new LinkedHashSet<>(alphabets))) {
							D += l + "|";
						}
						D = D.substring(0, D.length() - 1);
						D += ")*";
					}

					complianceAutomaton = LanguageBasedConformance.verifyLanguageConformance(S_, I_, D, F);
					failPath = Operations.path(S_, I_, complianceAutomaton, false);
				}

				// RUNTIME
				long endTime = System.nanoTime();
				long totalTime = endTime - startTime;
				long convert = TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS);

				// MEMORY CONSUMPTION AND RUNTIME
				// Calculate the used memory
				long memory = runtime.totalMemory() - runtime.freeMemory();

				if (convert > 0) {
					saveOnCSVFile(pathCsv, pathIUT, pathModel, failPath, convert, "seconds", memory);

				} else {
					saveOnCSVFile(pathCsv, pathIUT, pathModel, failPath, totalTime, "nano", memory);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		
	}
	
	public static void main(String[] args) {

		String pathIUT = "C:\\Users\\camil\\Desktop\\impl10.aut";
		String rootPathModels = "C:\\Users\\camil\\Desktop\\aut-percentage\\";
		String pathCsv = "C:\\Users\\camil\\Desktop\\xx.csv";
		boolean ioco = true;
		String D = "";
		String F = "";

		getPerformanceExecutionInLot(pathIUT, rootPathModels, pathCsv, ioco, D, F);
		
	}

	public static void saveOnCSVFile(String pathCsv, String pathModel, String pathIUT, String failPath, long time,
			String unitOfMeasure, long memoryBytes) {
		try {

			String veredict;

			if (!failPath.equals("")) {
				veredict = Constants.MSG_NOT_CONFORM;
			} else {
				veredict = Constants.MSG_CONFORM;
			}

			String[] lines = failPath.split("\n\n");
			List<String> testCases = new ArrayList<>();
			for (String s : lines) {
				if (s.contains("Test case: ")) {
					testCases.add(s.replace("Test case: 	", "").replaceAll("\n", "").replaceAll("#", ""));
				}
			}
			

			ArrayList<String> row = new ArrayList<String>() {
				{
					add(pathModel);
					add(pathIUT);
					add(Objects.toString(testCases));
					//add(failPath);
					add(veredict);
					add(Objects.toString(time));
					add(unitOfMeasure);
					add(Objects.toString(memoryBytes));
					add(Objects.toString(bytesToMegabytes(memoryBytes)));
				}
			};

			FileWriter csvWriter;
			String delimiter = "*";
			// first record
			if (new File(pathCsv).length() == 0) {
				csvWriter = new FileWriter(pathCsv);
				csvWriter.append("model");
				csvWriter.append(delimiter);
				csvWriter.append("iut");
				csvWriter.append(delimiter);
				csvWriter.append("testCases");
				csvWriter.append(delimiter);
				//csvWriter.append("failPath");
				//csvWriter.append(delimiter);
				csvWriter.append("veredict");
				csvWriter.append(delimiter);
				csvWriter.append("time");
				csvWriter.append(delimiter);
				csvWriter.append("unity");
				csvWriter.append(delimiter);
				csvWriter.append("memoryBytes");
				csvWriter.append(delimiter);
				csvWriter.append("memoryMegaBytes");
				//csvWriter.append(delimiter);
				csvWriter.append("\n");
			} else {
				csvWriter = new FileWriter(pathCsv, true);
			}

			csvWriter.append(String.join(delimiter, row));
			csvWriter.append("\n");

			csvWriter.flush();
			csvWriter.close();

		} catch (Exception e)

		{

		}
	}
}
