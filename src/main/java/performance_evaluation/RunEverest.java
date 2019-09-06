package performance_evaluation;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import model.IOLTS;
import parser.ImportAutFile_WithoutThread;

public class RunEverest {
	public static void main(String[] args) throws Exception {
		String root_img = new File("src/main/java/performance_evaluation/everet-img").getCanonicalPath() + "\\";
		String batchFileEverest = "C:\\Users\\camil\\Desktop\\everest.bat";
		List<String> headerCSV = Arrays.asList(new String[] { "tool", "model", "iut", "statesModel", "statesIut",
				"transitionsModel", "transitionsIut", "ntestCases", "conform", "variation", "variationType", "time",
				"unity", "memory", "unit", "pathTSSaved" });
		String pathCsv = "C:\\Users\\camil\\Desktop\\Nova pasta\\test1.csv";
		String pathSaveTS = "C:\\Users\\camil\\Desktop\\Nova pasta\\testSuite.csv";
		
		String pathAutSpec = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\+1000\\1000pct_spec.aut";
		String pathAutIUT = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\+1000\\1000pct_spec.aut";

		//String pathAutSpec = "C:\\Users\\camil\\Documents\\aut-modelos\\iolts-spec.aut";// iolts-spec
		//String pathAutIUT = "C:\\Users\\camil\\Documents\\aut-modelos\\iolts-impl-q.aut";// iolts-impl

		//String pathAutSpec = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\-1000\\250-500\\1555states_spec.aut";
		//String pathAutIUT = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\-1000\\250-500\\1555states_spec.aut";

		
		boolean stateVariation = true;

		run(batchFileEverest, root_img, pathAutSpec, pathAutIUT, pathSaveTS, headerCSV, pathCsv, stateVariation);

	}

	public static void run(String batchFileEverest, String root_img, String pathAutSpec, String pathAutIUT,
			String pathSaveTS, List<String> headerCSV, String pathCsv, boolean stateVariation) throws Exception {

		// open jar
		Desktop d = Desktop.getDesktop();
		d.open(new File(batchFileEverest));

		// wait for open
		Thread.sleep(1000);

		// type spec model
		Screen s = new Screen();
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

		long time_ini, time_end, total_seconds;
		time_ini = System.currentTimeMillis();
		long t0 = 0;
		// wait until finish verification
		while (true) {
			try {
				t0 = System.currentTimeMillis();
				s.find(new Pattern(root_img + "img-processing.PNG").similar(1.0f));// "lbl-verdict.PNG"
			} catch (FindFailed e) {
				time_end = System.currentTimeMillis();
				break;
			}

		}

		total_seconds = ((time_end - (time_end - t0)) - time_ini);
		if (total_seconds == 0) {
			total_seconds = time_end - time_ini;
		}

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
			 testSuite = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
					.getData(DataFlavor.stringFlavor);
		} else {
			if (s.exists(root_img + "lbl-conform-veredict.PNG") != null) {
				System.err.println("IOCO CONFORM");
				conform = true;
			}
		}

		saveOnCSVFile(pathCsv, pathAutSpec, pathAutIUT, conform, total_seconds, "milliseconds", memory, unityMemory,
				stateVariation, headerCSV,  pathSaveTS, testSuite);

		// close everest
		s.click(root_img + "img-close.PNG");
	}

	static String delimiterCSV = "*";

	public static void saveOnCSVFile(String pathCsv, String pathModel, String pathIUT, boolean conform, long time,
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
			row.add(Objects.toString(time));
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

				csvWriter.append("\n");
			} else {
				csvWriter = new FileWriter(pathSaveTS, true);
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

}
