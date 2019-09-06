package performance_evaluation;

import java.awt.image.ReplicateScaleFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.ext.JGraphXAdapter;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;

import algorithm.IocoConformance;
import algorithm.LanguageBasedConformance;
import algorithm.Operations;
import model.Automaton_;
import model.IOLTS;
import model.LTS;
import parser.ImportAutFile;
import util.ModelImageGenerator.TimeOut;

public class PerformanceTest {
	static String delimiterCSV = "*";
	private static final long MEGABYTE = 1024L * 1024L;

	public static void main(String[] args) throws IOException {

		String pathIUT = "C:\\Users\\camil\\Desktop\\teste desempenho - Everest\\iut30-specNumStates2\\iut30states.aut";
		String rootPathModels = "C:\\Users\\camil\\Desktop\\teste desempenho - Everest\\iut30-specNumStates2\\spec\\";
		String pathCsv = "C:\\Users\\camil\\Desktop\\teste desempenho - Everest\\iut30-specNumStates2\\iut30-specNumStates.csv";
		boolean ioco = true;
		String D = "";
		String F = "";
		boolean stateVariation = true;// state or percentage

		getPerformanceExecutionInLot(pathIUT, rootPathModels, pathCsv, ioco, D, F, stateVariation);

		// String arquivoCSV = "C:\\Users\\camil\\Desktop\\teste desempenho -
		// Everest\\iut30-specPercentage\\iut30-specPercentage.csv";
		// String novo_arquivoCSV = "C:\\Users\\camil\\Desktop\\teste desempenho -
		// Everest\\iut30-specPercentage\\novo_iut30-specPercentage.csv";
		//
		// BufferedReader br = null;
		// String linha = "";
		//
		// try {
		//
		// br = new BufferedReader(new FileReader(arquivoCSV));
		// linha = br.readLine();
		//
		// while ((linha = br.readLine()) != null) {
		// String[] d = linha.replaceAll("\\*", "#").split("#");
		// String[] testCases = d[6].replace("[", "").replace("]", "").split(",");
		// saveOnCSVFile(novo_arquivoCSV, d[0], d[1], d[6], Long.parseLong(d[10]),
		// d[11], Long.parseLong(d[12]), Integer.parseInt(d[3]), Integer.parseInt(d[2]),
		// Integer.parseInt(d[5]), Integer.parseInt(d[4]), false,
		// Arrays.asList(testCases));
		// }
		//
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// if (br != null) {
		// try {
		// br.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }
	}

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}

	public static IOLTS getModel(String path) throws Exception {
		return ImportAutFile.autToIOLTS(path, false, new ArrayList<String>(), new ArrayList<String>());
	}

	public static void getPerformanceExecutionInLot(String pathIUT, String rootPathModels, String pathCsv, boolean ioco,
			String D, String F, boolean stateVariation) throws IOException {
		Automaton_ complianceAutomaton;
		String failPath;
		LTS S_, I_;
		String pathModel;
		String errorFolder = rootPathModels + "\\error\\";
		Path errorPath = Paths.get(errorFolder);
		String successFolder = rootPathModels + "\\success\\";
		Path successPath = Paths.get(successFolder);
		if (!Files.exists(errorPath)) {
			Files.createDirectory(errorPath);
		}
		if (!Files.exists(successPath)) {
			Files.createDirectory(successPath);
		}

		File folder = new File(rootPathModels);
		File[] listOfFiles = folder.listFiles();

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

		for (File file : listOfFiles) {

			if (file.getName().indexOf(".") != -1
					&& file.getName().substring(file.getName().indexOf(".")).equals(".aut")) {
				pathModel = rootPathModels + file.getName();
				System.out.println(file.getName() + " - " + dateFormat.format(new Date()));
				try {
					// Models IOLTS inp/out(?/!)
					IOLTS S = getModel(pathModel);
					S.addQuiescentTransitions();

					IOLTS I = getModel(pathIUT);
					I.addQuiescentTransitions();

					
					// Get the Java runtime
					Runtime runtime = Runtime.getRuntime();
					// Run the garbage collector
					runtime.gc();

					Future<String> control = Executors.newSingleThreadExecutor().submit(new TimeOut(ioco, S, I, D, F));
					try {
						int limitTime = 50;
						control.get(limitTime, TimeUnit.MINUTES);

						// RUNTIME
						
						//long convert = TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS);

						// MEMORY CONSUMPTION AND RUNTIME
						// Calculate the used memory
						long memory = runtime.totalMemory() - runtime.freeMemory();

						// if (convert > 0) {
						// saveOnCSVFile(pathCsv, pathIUT, pathModel, failPath, convert, "seconds",
						// memory, I.getStates().size(), S.getStates().size(),
						// I.getTransitions().size(), S.getTransitions().size());

						// } else {

						// path + nstates complianceAutomaton + ntransition complianceAutomaton +
						// nfinalStates complianceAutomaton
						String[] complianceInfo = control.get().split("\\"+delimiterCSV);
																					
						saveOnCSVFile(pathCsv, pathModel, pathIUT, complianceInfo[0], Long.parseLong(complianceInfo[4]),Long.parseLong(complianceInfo[5]), "nano", memory,
								I.getStates().size(), S.getStates().size(), I.getTransitions().size(),
								S.getTransitions().size(), stateVariation, Integer.parseInt(complianceInfo[1]),
								Integer.parseInt(complianceInfo[2]), Integer.parseInt(complianceInfo[3]));
						// }
						Files.move(Paths.get(pathModel), Paths.get(successFolder + file.getName()));
					} catch (Exception e) {// TimeoutException
						// mover arquivo para pasta de erro
						 e.printStackTrace();

						Files.move(Paths.get(pathModel), Paths.get(errorFolder + file.getName()));
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

	}

	public static void saveOnCSVFile(String pathCsv, String pathModel, String pathIUT, String failPath, long timeVerify, long timeGenerateTS,
			String unitOfMeasure, long memoryBytes, int numStatesIut, int numStatesModel, int numTransitionsIut,
			int numTransitionsModel, boolean stateVariation, int nstatesTSautomaton, int ntransitionTSautomaton,
			int nfinalStatesTSautomaton) {

		try {

			String testCasesCSV = pathCsv.substring(0, pathCsv.lastIndexOf("\\")) + "\\testCases.csv";
			int iniSubstring = pathModel.lastIndexOf("\\") + 1;
			int endSubstring;
			String variationType;
			String variation;
			if (stateVariation) {
				endSubstring = pathModel.indexOf("states_spec");
				variationType = "numStates";
			} else {
				endSubstring = pathModel.indexOf("pct_spec");
				variationType = "percentage";
			}

			variation = pathModel.substring(iniSubstring, endSubstring);

			String veredict;

			if (!failPath.equals("")) {
				veredict = "false";
			} else {
				veredict = "true";
			}

			String[] lines = failPath.split("\n\n");
			List<String> testCases = new ArrayList<>();
			for (String s : lines) {
				if (s.contains("Test case:")) {
					testCases.add(s.replace("Test case: 	", "").replaceAll("\n", "").replaceAll("#", ""));
				}
			}

			ArrayList<String> row = new ArrayList<String>() {
				{
					add(pathModel);
					add(pathIUT);
					add(Objects.toString(numStatesModel));
					add(Objects.toString(numStatesIut));
					add(Objects.toString(numTransitionsModel));
					add(Objects.toString(numTransitionsIut));
					// add(Objects.toString(testCases));
					add(Objects.toString(testCases.size()));
					// add(failPath);
					add(veredict);
					add(variation);
					add(variationType);
					add(Objects.toString(timeVerify));
					add(Objects.toString(timeGenerateTS));
					add(unitOfMeasure);
					add(Objects.toString(memoryBytes));
					add(Objects.toString(bytesToMegabytes(memoryBytes)));
					add(Objects.toString(nstatesTSautomaton));
					add(Objects.toString(ntransitionTSautomaton));
					add(Objects.toString(nfinalStatesTSautomaton));
				}
			};

			FileWriter csvWriter;

			// first record
			if (new File(pathCsv).length() == 0) {
				csvWriter = new FileWriter(pathCsv);
				csvWriter.append("model");
				csvWriter.append(delimiterCSV);
				csvWriter.append("iut");
				csvWriter.append(delimiterCSV);
				csvWriter.append("statesModel");
				csvWriter.append(delimiterCSV);
				csvWriter.append("statesIut");
				csvWriter.append(delimiterCSV);
				csvWriter.append("transitionsModel");
				csvWriter.append(delimiterCSV);
				csvWriter.append("transitionsIut");
				csvWriter.append(delimiterCSV);
				// csvWriter.append("testCases");
				// csvWriter.append(delimiterCSV);
				csvWriter.append("ntestCases");
				csvWriter.append(delimiterCSV);
				// csvWriter.append("failPath");
				// csvWriter.append(delimiter);
				csvWriter.append("conform");
				csvWriter.append(delimiterCSV);
				csvWriter.append("variation");
				csvWriter.append(delimiterCSV);
				csvWriter.append("variationType");
				csvWriter.append(delimiterCSV);
				csvWriter.append("timeVerify");
				csvWriter.append(delimiterCSV);
				csvWriter.append("timeGenerateTS");
				csvWriter.append(delimiterCSV);
				csvWriter.append("unity");
				csvWriter.append(delimiterCSV);
				csvWriter.append("memoryBytes");
				csvWriter.append(delimiterCSV);
				csvWriter.append("memoryMegaBytes");
				 csvWriter.append(delimiterCSV);
				 csvWriter.append("tsNstates");
				 csvWriter.append(delimiterCSV);
				 csvWriter.append("tsNtransitions");
				 csvWriter.append(delimiterCSV);
				 csvWriter.append("tsNfinalstates");
				csvWriter.append("\n");
			} else {
				csvWriter = new FileWriter(pathCsv, true);
			}

			csvWriter.append(String.join(delimiterCSV, row));
			csvWriter.append("\n");

			csvWriter.flush();
			csvWriter.close();

			// TEST CASE CSV
			if (new File(testCasesCSV).length() == 0) {
				csvWriter = new FileWriter(testCasesCSV);
				csvWriter.append("model");
				csvWriter.append(delimiterCSV);
				csvWriter.append("iut");
				csvWriter.append(delimiterCSV);
				csvWriter.append("testCases");

				csvWriter.append("\n");
			} else {
				csvWriter = new FileWriter(testCasesCSV, true);
			}

			row = new ArrayList<String>() {
				{
					add(pathModel);
					add(pathIUT);
					add(Objects.toString(testCases));
				}
			};

			csvWriter.append(String.join(delimiterCSV, row));
			csvWriter.append("\n");

			csvWriter.flush();
			csvWriter.close();

		} catch (Exception e)

		{
			e.printStackTrace();
		}
	}

	public static class TimeOut implements Callable<String> {
		boolean ioco;
		String returnInfo, D, F;
		IOLTS S, I;

		public TimeOut(boolean ioco, IOLTS S, IOLTS I, String D, String F) {
			this.ioco = ioco;
			this.S = S;
			this.I = I;
			this.D = D;
			this.F = F;

		}

		/*
		 * public String getfailPath() { return failPath; }
		 */

		@Override
		public String call() throws Exception {
			Automaton_ complianceAutomaton;
			
			long startTime = System.nanoTime();
			long endTimeVerify = 0, endTimeGenerateTS = 0;
			
			// IOCO
			if (ioco) {
				complianceAutomaton = IocoConformance.verifyIOCOConformance(S, I);
				endTimeVerify = System.nanoTime() - startTime;
				startTime = System.nanoTime();
				System.out.println("  > verificou");
				returnInfo = Operations.path(S, I, complianceAutomaton, true, false);
				endTimeGenerateTS = System.nanoTime() - startTime;
				System.out.println("  > gerou ts");
			} else {
				// LANGUAGE BASED
				LTS S_ = S.toLTS();
				LTS I_ = I.toLTS();

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

				endTimeVerify = System.nanoTime() - startTime;
				startTime = System.nanoTime();
				returnInfo = Operations.path(S_, I_, complianceAutomaton, false, false);
				endTimeGenerateTS = System.nanoTime() - startTime;

			}

			
			
			// path + nstates complianceAutomaton + ntransition complianceAutomaton +
			// nfinalStates complianceAutomaton
			returnInfo += delimiterCSV + complianceAutomaton.getStates().size() + delimiterCSV
					+ complianceAutomaton.getTransitions().size() + delimiterCSV +complianceAutomaton.getFinalStates().size()
					+ delimiterCSV + endTimeVerify+delimiterCSV+endTimeGenerateTS
					;

			return returnInfo;
		}
	}
}
