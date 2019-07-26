package util;

import java.io.File;
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

	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}

	public static IOLTS getModel(String path) throws Exception {				
		return ImportAutFile.autToIOLTS(path, false, new ArrayList<String>(), new ArrayList<String>());
	}

	public static void getPerformanceExecutionInLot(String pathIUT, String rootPathModels, String pathCsv, boolean ioco, String D, String F, boolean stateVariation) throws IOException {
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
			
			if(file.getName().indexOf(".") != -1 && file.getName().substring(file.getName().indexOf(".")).equals(".aut")) {			
			pathModel = rootPathModels+file.getName();
			System.out.println(file.getName() + " - " + dateFormat.format(new Date()));
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

				Future<String> control = Executors.newSingleThreadExecutor().submit(new TimeOut(ioco, S,I,D,F));				
				try {
					int limitTime = 30;
					control.get(limitTime, TimeUnit.MINUTES);
					
					// RUNTIME
					long endTime = System.nanoTime();
					long totalTime = endTime - startTime;
					//long convert = TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS);

					// MEMORY CONSUMPTION AND RUNTIME
					// Calculate the used memory
					long memory = runtime.totalMemory() - runtime.freeMemory();

					//if (convert > 0) {
						//saveOnCSVFile(pathCsv, pathIUT, pathModel, failPath, convert, "seconds", memory, I.getStates().size(), S.getStates().size(), I.getTransitions().size(), S.getTransitions().size());

					//} else {
						saveOnCSVFile(pathCsv, pathModel, pathIUT, control.get(), totalTime, "nano", memory, I.getStates().size(), S.getStates().size(), I.getTransitions().size(), S.getTransitions().size(), stateVariation);
					//}
						Files.move(Paths.get(pathModel), Paths.get(successFolder+file.getName()));
				} catch (Exception e) {// TimeoutException
					//mover arquivo para pasta de erro
					//e.printStackTrace();
					
					Files.move(Paths.get(pathModel), Paths.get(errorFolder+file.getName()));
				}

				

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		}

		
	}
	
	public static void main(String[] args) throws IOException {

		String pathIUT = "C:\\Users\\camil\\Desktop\\teste desempenho - Everest\\iut30-specPercentage\\iut30states.aut";
		String rootPathModels = "C:\\Users\\camil\\Desktop\\teste desempenho - Everest\\iut30-specPercentage\\spec\\";
		String pathCsv = "C:\\Users\\camil\\Desktop\\teste desempenho - Everest\\iut30-specPercentage\\iut30-specPercentage.csv";
		boolean ioco = true;
		String D = "";
		String F = "";
		boolean stateVariation = false;//state or percentage

		getPerformanceExecutionInLot(pathIUT, rootPathModels, pathCsv, ioco, D, F,stateVariation);
		
	}

	public static void saveOnCSVFile(String pathCsv, String pathModel, String pathIUT, String failPath, long time,
			String unitOfMeasure, long memoryBytes, int numStatesIut, int numStatesModel, int numTransitionsIut, int numTransitionsModel, boolean stateVariation) {
		try {
			
		    int iniSubstring = pathModel.lastIndexOf("\\")+1;
		    int endSubstring;
		    String variationType;
		    String variation;
			if(stateVariation) {
				endSubstring = pathModel.indexOf("states_spec");
				variationType = "numStates";
			}else {
				endSubstring = pathModel.indexOf("pct_spec");
				variationType = "percentage";
			}
			
			variation = pathModel.substring(iniSubstring,endSubstring);
			
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
					add(Objects.toString(testCases));
					//add(failPath);
					add(veredict);
					add(variation);
					add(variationType);
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
				csvWriter.append("statesModel");
				csvWriter.append(delimiter);
				csvWriter.append("statesIut");
				csvWriter.append(delimiter);
				csvWriter.append("transitionsModel");
				csvWriter.append(delimiter);
				csvWriter.append("transitionsIut");
				csvWriter.append(delimiter);
				csvWriter.append("testCases");
				csvWriter.append(delimiter);
				//csvWriter.append("failPath");
				//csvWriter.append(delimiter);
				csvWriter.append("conform");
				csvWriter.append(delimiter);
				csvWriter.append("variation");
				csvWriter.append(delimiter);
				csvWriter.append("variationType");
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
	
	public static class TimeOut implements Callable<String> {
		boolean ioco;
		String failPath, D, F;
		IOLTS S,I;
		

		public TimeOut(boolean ioco, IOLTS S, IOLTS I, String D, String F) {
			this.ioco = ioco;
			this.S = S;
			this.I = I;
			this.D = D;
			this.F = F;
			
		}
		
		/*public String getfailPath() {
			return failPath;
		}*/

		@Override
		public String call() throws Exception {	
			Automaton_ complianceAutomaton;
			
			
			// IOCO
			if (ioco) {
				complianceAutomaton = IocoConformance.verifyIOCOConformance(S, I);
				System.out.println("  > verificou");
				failPath = Operations.path(S, I, complianceAutomaton, true);
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
				
				failPath = Operations.path(S_, I_, complianceAutomaton, false);					
				
			}
			return failPath;
		}
	}
}
