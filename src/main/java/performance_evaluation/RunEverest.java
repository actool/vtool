package performance_evaluation;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.sikuli.script.SikuliException;

import com.sikulix.tigervnc.Sikulix;

import model.IOLTS;
import parser.ImportAutFile_WithoutThread;
import performance_evaluation.RunJTorx.TimeOut;

public class RunEverest {
	public static void main(String[] args) throws Exception {
		try
		{
			String root_img = new File("src/main/java/performance_evaluation/everet-img").getCanonicalPath() + "\\";
			String batchFileEverest = "C:\\Users\\camil\\Desktop\\everest.bat";
			List<String> headerCSV = Arrays.asList(new String[] { "tool", "model", "iut", "statesModel", "statesIut",
					"transitionsModel", "transitionsIut", "ntestCases", "conform", "variation", "variationType", "time",
					"unity", "memory", "unit", "pathTSSaved" });
		
			// String pathCsv = "C:\\Users\\camil\\Desktop\\Nova pasta\\test1.csv";
			// String pathSaveTS = "C:\\Users\\camil\\Desktop\\Nova pasta\\testSuite.csv";
			//
			// String pathAutSpec = "C:\\Users\\camil\\Desktop\\Nova pasta
			// (2)\\+1000\\1000pct_spec.aut";
			// String pathAutIUT = "C:\\Users\\camil\\Desktop\\Nova pasta
			// (2)\\+1000\\1000pct_spec.aut";

			// String pathAutSpec =
			// "C:\\Users\\camil\\Documents\\aut-modelos\\iolts-spec.aut";// iolts-spec
			// String pathAutIUT =
			// "C:\\Users\\camil\\Documents\\aut-modelos\\iolts-impl-q.aut";// iolts-impl

			// String pathAutSpec = "C:\\Users\\camil\\Desktop\\Nova pasta
			// (2)\\-1000\\250-500\\1555states_spec.aut";
			// String pathAutIUT = "C:\\Users\\camil\\Desktop\\Nova pasta
			// (2)\\-1000\\250-500\\1555states_spec.aut";

			// boolean stateVariation = true;

			
//			 String pathAutSpec = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\modelos2000states\\2050states_spec_49.aut";
//			 String pathAutIUT = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\modelos2000states\\2050states_spec_49.aut";
//			 String pathSaveTS = "C:\\Users\\camil\\Desktop\\Nova pasta\\";
//			 String pathCsv = "C:\\Users\\camil\\Desktop\\Nova pasta\\teste.csv";
//			 boolean stateVariation = true;
//			run(batchFileEverest, root_img, pathAutSpec, pathAutIUT, pathSaveTS, headerCSV,
//			 pathCsv, stateVariation);
			
//			boolean stateVariation = true;// state or percentage
//			String rootPathModels = "C:\\Users\\camil\\Desktop\\models\\aut\\";
//			String rootPathSaveTS = "C:\\Users\\camil\\Desktop\\models\\result\\testSuite.csv";
//			String pathCsv = "C:\\Users\\camil\\Desktop\\models\\result\\everest.csv";
//
//			String errorFolder = rootPathModels + "\\error\\";
//			Path errorPath = Paths.get(errorFolder);
//			String successFolder = rootPathModels + "\\success\\";
//			Path successPath = Paths.get(successFolder);
//			if (!Files.exists(errorPath)) {
//				Files.createDirectory(errorPath);
//			}
//			if (!Files.exists(successPath)) {
//				Files.createDirectory(successPath);
//			}
//
//			File folder = new File(rootPathModels);
//			File[] listOfFiles = folder.listFiles();
//			String pathSaveTS;
//
//			String pathModel;
//			int count = 0;
//			for (File file : listOfFiles) {
//				if (file.getName().indexOf(".") != -1
//						&& file.getName().substring(file.getName().indexOf(".")).equals(".aut")) {
//					pathModel = rootPathModels + file.getName();
//					pathSaveTS = rootPathSaveTS + count + "_" + file.getName().replace(".aut", "") + "\\";
//					count++;
//					Future<String> control = Executors.newSingleThreadExecutor().submit(new TimeOut(batchFileEverest,
//							root_img, pathModel,
//							
//							pathModel, pathSaveTS, headerCSV, pathCsv, stateVariation));
//
//					try {
//						int limitTime = 3;
//						control.get(limitTime, TimeUnit.MINUTES);
//						
//						
//						Thread.sleep(500);
//						
//						Files.move(Paths.get(pathModel), Paths.get(successFolder + file.getName()));
//					} catch (Exception e) {// TimeoutException
//						// mover arquivo para pasta de erro
//						e.printStackTrace();
//						
//						Files.move(Paths.get(pathModel), Paths.get(errorFolder + file.getName()));
//					}
//				}
//			}
			
			
			

			boolean stateVariation = true;// state or percentage
			String rootPathIUTs = "C:\\Users\\camil\\Desktop\\teste\\iut\\";
			String pathAutSpec = "C:\\Users\\camil\\Desktop\\teste\\spec.aut";
			String rootPathSaveTS = "C:\\Users\\camil\\Desktop\\teste\\";
			String pathCsv = "C:\\Users\\camil\\Desktop\\teste\\everest.csv";
			
//			String rootPathModels = "C:\\Users\\camil\\Desktop\\novos\\spec\\";
//			String pathAutIUT = "C:\\Users\\camil\\Desktop\\models-30\\30states_iut.aut";
//			String rootPathSaveTS = "C:\\Users\\camil\\Desktop\\novos\\";
//			String pathCsv = "C:\\Users\\camil\\Desktop\\novos\\everest.csv";

			String errorFolder = rootPathIUTs + "\\error\\";
			Path errorPath = Paths.get(errorFolder);
			String successFolder = rootPathIUTs + "\\success\\";
			Path successPath = Paths.get(successFolder);
			if (!Files.exists(errorPath)) {
				Files.createDirectory(errorPath);
			}
			if (!Files.exists(successPath)) {
				Files.createDirectory(successPath);
			}

			File folder = new File(rootPathIUTs);
			File[] listOfFiles = folder.listFiles();
			String pathSaveTS;

			System.out.println(Arrays.asList(listOfFiles));
			String pathIUT;
			int count = 0;
			for (File file : listOfFiles) {
				if (file.getName().indexOf(".") != -1
						&& file.getName().substring(file.getName().indexOf(".")).equals(".aut")) {
					pathIUT = rootPathIUTs + file.getName();
					pathSaveTS = rootPathSaveTS + "testSuite.csv";
					count++;
					Future<String> control = Executors.newSingleThreadExecutor().submit(new TimeOut(batchFileEverest,
							root_img, pathIUT, pathAutSpec, pathSaveTS, headerCSV, pathCsv, stateVariation));

					try {
						int limitTime = 40;
						control.get(limitTime, TimeUnit.SECONDS);
						
						Thread.sleep(500);
						Files.move(Paths.get(pathIUT), Paths.get(successFolder + file.getName()));
					} catch (Exception e) {// TimeoutException
																	
						Runtime.getRuntime().exec("TASKKILL /F /IM java.exe");
						Thread.sleep(500);
						// mover arquivo para pasta de erro
						Files.move(Paths.get(pathIUT), Paths.get(errorFolder + file.getName()));
						
						control.cancel(true);
						
						System.exit(0);//arranjar um jeito de parar a execução do sikuli (os comandos continuam mesmo depois da exception)
						
						
						e.printStackTrace();
						

					}
					

				}
				
				
			}

		} catch (Exception e) {
			Runtime.getRuntime().exec("TASKKILL /F /IM java.exe");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static class TimeOut implements Callable<String> {
		String batchFileEverest, root_img, pathAutSpec, pathAutIUT, pathSaveTS, pathCsv;
		boolean stateVariation;
		List<String> headerCSV;

		public TimeOut(String batchFileEverest, String root_img, String pathIUT, String pathSpec,
				String pathSaveTS, List<String> headerCSV, String pathCsv, boolean stateVariation) throws Exception {

			this.batchFileEverest = batchFileEverest;
			this.root_img = root_img;
			this.pathAutSpec = pathSpec;
			this.pathAutIUT = pathIUT;
			this.pathSaveTS = pathSaveTS;
			this.headerCSV = headerCSV;
			this.pathCsv = pathCsv;
			this.stateVariation = stateVariation;
		}

		@Override
		public String call() throws Exception {
			run(batchFileEverest, root_img, pathAutSpec, pathAutIUT, pathSaveTS, headerCSV, pathCsv, stateVariation);
			return "";
		}

	}
	
	
	static Screen s = new Screen();
	public static void run(String batchFileEverest, String root_img, String pathAutSpec, String pathAutIUT,
			String pathSaveTS, List<String> headerCSV, String pathCsv, boolean stateVariation) throws Exception {

		// open jar
		Desktop d = Desktop.getDesktop();
		d.open(new File(batchFileEverest));

		// wait for open
		Thread.sleep(2000);

		// type spec model
		
		s.type(root_img + "inp-model.PNG", pathAutSpec);
		s.type(Key.ENTER);

		// type iut model
		s.type(root_img + "inp-iut.PNG", pathAutIUT);
		s.type(Key.ENTER);

		// model type (IOLTS)
		s.click(root_img + "cb-modelType.PNG");
		s.type(Key.DOWN);
		s.type(Key.ENTER);

		// label (?in !out)
		s.click(root_img + "cb-label.PNG");
		s.type(Key.DOWN);
		s.type(Key.ENTER);

		// menu ioco
		s.click(root_img + "item-menu-ioco.PNG");

		// wait until open ioco view
		while (true) {
			try {
				System.currentTimeMillis();
				s.find(new Pattern(root_img + "img-processing.PNG").similar(1.0f));
			} catch (FindFailed e) {
				break;
			}
		}

		// verify ioco
		s.click(root_img + "btn-verify.PNG");

		double time_ini, time_end, total_seconds;
		time_ini = System.nanoTime();;
		long t0 = 0;
		// wait until finish verification
		while (true) {
			try {
				t0 = System.nanoTime();
				s.find(new Pattern(root_img + "img-processing.PNG").similar(1.0f));// "lbl-verdict.PNG"
			} catch (FindFailed e) {
				time_end = System.nanoTime();
				break;
			}

		}

		
		total_seconds = ((time_end - (time_end - t0)) - time_ini)/1000000;//nano para mili
//		if (total_seconds == 0) {
//			total_seconds = time_end - time_ini;
//		}

		System.err.println("FINISHED: " + total_seconds + " milliseconds");

		// get memory consumption (cmd)
		String s_ = "";
		Process p = Runtime.getRuntime().exec("TASKLIST /FI \"IMAGENAME eq java.exe\"");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String memory = "", unityMemory = "";
		while ((s_ = stdInput.readLine()) != null) {
			s_ = s_.replaceAll("\\s{2,}", " ").trim();
			String array[] = s_.split(" ");
			if (array.length == 6 && array[0].equals("java.exe")) {
				System.err.println("memory: " + array[4] + " measure: " + array[5]);
				memory = array[4];
				unityMemory = array[5];
			}
		}

		String testSuite = "";
		boolean conform = false;
		// get veredict by label
		if (s.exists(root_img + "lbl-fail-veredict1.PNG") != null) {
			System.err.println("IOCO DOESN'T CONFORM");
			// get test suite
			s.type(Key.TAB);
			s.type("a", KeyModifier.CTRL);
			s.type("c", KeyModifier.CTRL);
			s.find(root_img + "btn-verify.PNG");
			testSuite = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} else {
			if (s.exists(root_img + "lbl-conform-veredict.PNG") != null) {
				System.err.println("IOCO CONFORM");
				conform = true;
			}
		}

		saveOnCSVFile(pathCsv, pathAutSpec, pathAutIUT, conform, total_seconds, "milliseconds", memory, unityMemory,
				stateVariation, headerCSV, pathSaveTS, testSuite);

		// close everest
		s.click(root_img + "img-close.PNG");
		Runtime.getRuntime().exec("TASKKILL /F /IM java.exe");
	}

	static String delimiterCSV = "*";

	public static void saveOnCSVFile(String pathCsv, String pathModel, String pathIUT, boolean conform, double time,
			String unitTime, String memory, String unityMemory, boolean stateVariation, List<String> headerCSV,
			String pathSaveTS, String testSuite) {

		try {
			String variationType = "";
			String variation = "";
			// String testCasesCSV = pathCsv.substring(0, pathCsv.lastIndexOf("\\")) +
			// "\\testCases.csv";
			int iniSubstring = pathModel.lastIndexOf("\\") + 1;
			int endSubstring;

			int numStatesIut, numStatesModel, numTransitionsIut, numTransitionsModel;
			IOLTS iolts_spec = ImportAutFile_WithoutThread.autToIOLTS(pathModel, false, null, null);
			numStatesModel = iolts_spec.getStates().size();
			numTransitionsModel = iolts_spec.getTransitions().size();

			IOLTS iolts_iut = ImportAutFile_WithoutThread.autToIOLTS(pathIUT, false, null, null);
			numStatesIut = iolts_iut.getStates().size();
			numTransitionsIut = iolts_iut.getTransitions().size();

			if (stateVariation) {
				endSubstring = pathModel.indexOf("states_spec");
				variationType = "numStates";
				if (endSubstring < 0) {
					endSubstring = pathModel.indexOf("pct_spec");
					variationType = "percentage";
				}
				if (endSubstring > 0) {
					variation = pathModel.substring(iniSubstring, endSubstring);
				} else {
					variationType = "";
					variation = "";
				}

			}

			List<String> testCases = new ArrayList<>();
			if (!testSuite.isEmpty()) {
				String[] lines = testSuite.split("\n\n");

				for (String s : lines) {
					if (s.contains("Test case:")) {
						testCases.add(s.replace("Test case: ", "").replaceAll("\n", "").replaceAll("#", ""));
					}
				}
			}

			ArrayList<String> row = new ArrayList<String>();
			row.add("everest");
			row.add(pathModel);
			row.add(pathIUT);
			row.add(Objects.toString(numStatesModel));
			row.add(Objects.toString(numStatesIut));
			row.add(Objects.toString(numTransitionsModel));
			row.add(Objects.toString(numTransitionsIut));
			row.add(Objects.toString(testCases.size()));
			row.add(Objects.toString(conform));
			row.add(variation);
			row.add(variationType);
			row.add(String.format("%.5f",time));
			row.add(unitTime);
			row.add(Objects.toString(memory));
			row.add(unityMemory);
			row.add(pathSaveTS);

			FileWriter csvWriter;

			File file = new File(pathCsv);
			if (!file.exists()) {
				file.createNewFile();
				
			}

			// first record
			if (new File(pathCsv).length() == 0) {
				csvWriter = new FileWriter(pathCsv);

				for (String header : headerCSV) {
					csvWriter.append(header);
					csvWriter.append(delimiterCSV);
				}
				csvWriter.append("\n");
			} else {
				csvWriter = new FileWriter(pathCsv, true);
			}

			csvWriter.append(String.join(delimiterCSV, row));
			csvWriter.append("\n");

			csvWriter.flush();
			csvWriter.close();

			file = new File(pathSaveTS);
			if (!file.exists()) {
				file.createNewFile();
			}

			// TEST CASE CSV
			if (new File(pathSaveTS).length() == 0) {
				csvWriter = new FileWriter(pathSaveTS);
				csvWriter.append("model");
				csvWriter.append(delimiterCSV);
				csvWriter.append("iut");
				csvWriter.append(delimiterCSV);
				csvWriter.append("testCases");
				csvWriter.append(delimiterCSV);
				csvWriter.append("path");
				csvWriter.append("\n");
			} else {
				csvWriter = new FileWriter(pathSaveTS, true);
			}

			row = new ArrayList<String>() {
				{
					add(pathModel);
					add(pathIUT);
					add(Objects.toString(testCases));
					add(Objects.toString(testSuite));
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

}
