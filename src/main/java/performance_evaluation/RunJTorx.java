package performance_evaluation;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

import algorithm.Operations;
import model.Automaton_;
import model.IOLTS;
import model.State_;
import model.Transition_;
import parser.ImportAutFile;
import parser.ImportAutFile_WithoutThread;
import util.Constants;

import org.sikuli.script.*;

import com.android.dx.util.FileUtils;

public class RunJTorx {
	
	public static void main(String[] args) {
		try {
			String batchFileJTorx = "C:\\Users\\camil\\Google Drive\\UEL\\jtorx\\jtorx-1.11.2-win\\jtorx.bat";
			// String root_aut = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\";
			// String root_aut = "C:\\Users\\camil\\Documents\\aut-modelos\\";
			// String root_aut = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\+1000\\";

			// spec
			// s.type(root_img + "inp-model.PNG", root_aut +
			// "vending-machine-spec-nconf.aut");// iolts-spec.aut
			// s.type(root_img + "inp-model.PNG", root_aut + "iolts-spec.aut");
			// s.type(root_img + "inp-model.PNG", root_aut + "iut1000states.aut");

			// iut
			// s.type(root_aut + "vending-machine-iut.aut");// iolts-impl-r.aut
			// s.type(root_aut + "iolts-impl-r.aut");//
			// s.type(root_aut + "iut1000states.aut");

//			String pathAutSpec = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\+1000\\iut1000states.aut";
//			String pathAutIUT = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\+1000\\iut1000states.aut";
//			 String pathAutSpec =
//			 "C:\\Users\\camil\\Documents\\aut-modelos\\iolts-spec.aut";
//			 String pathAutIUT =
//			 "C:\\Users\\camil\\Documents\\aut-modelos\\iolts-impl-r.aut";
			
			 String pathAutSpec =
					 "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\-1000\\250-500\\1555states_iut_0.aut";
					 String pathAutIUT =
					 "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\-1000\\250-500\\1555states_iut_0.aut";

			String root_img = new File("src/main/java/performance_evaluation/jtorx-img").getCanonicalPath() + "\\";
			String pathSaveTS = "C:\\Users\\camil\\Desktop\\Nova pasta\\";

			List<String> headerCSV = Arrays.asList(new String[]{"model","iut","statesModel","statesIut","transitionsModel","transitionsIut","ntestCases","conform","variation","variationType","time","unity","memory","unit"});
			String pathCsv= "";
			boolean stateVariation = false;// state or percentage
			
			run(batchFileJTorx, root_img, pathAutSpec, pathAutIUT, pathSaveTS,headerCSV, pathCsv, stateVariation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static String delimiterCSV = "*";
	
	public static void run(String batchFileJTorx, String root_img, String pathAutSpec, String pathAutIUT,
			String pathSaveTS, List<String> headerCSV, String pathCsv,boolean stateVariation) throws Exception {

		//run JTorex
		Desktop d = Desktop.getDesktop();
		
		d.open(new File(batchFileJTorx));
		//wait until open Jtorx
		Thread.sleep(500);

		//type spec model
		Screen s = new Screen();
		s.type(root_img + "inp-model.PNG", pathAutSpec);
		
		
		for (int i = 0; i < 8; i++) {
			s.type(Key.TAB);
		}

		//type iut model
		s.type(pathAutIUT);

		//?in !out
		s.click(root_img + "cb-interpretation.PNG");
		s.type(Key.DOWN);
		s.type(Key.DOWN);
		s.click();

		//Strace
		s.type(Key.TAB);
		s.type(Key.DOWN);

		//ioco menu click
		s.click(root_img + "item-menu-ioco.PNG");

		//check button
		s.click(root_img + "btn-check.PNG");
		
		
		long time_ini = System.currentTimeMillis();
		long time_end, t0 = 0;
		//wait until verify
		while (true) {
			try {
				t0 = System.currentTimeMillis();
				 s.find(new Pattern(root_img + "lbl-result.PNG").similar(1.0f));
			} catch (FindFailed e) {
				time_end = System.currentTimeMillis();
				break;
			}
		}

		long total_seconds = ((time_end - (time_end - t0)) - time_ini); // / 1000;

		System.err.println("FINISHED: " + total_seconds + " milliseconds");

		//get memory consumption (cmd)
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

		boolean conform = false;
		int count = 0;
		//get veredict by label
		if (s.exists(root_img + "lbl-conform-veredict.PNG") != null) {
			System.err.println("IOCO CONFORM");
			conform = true;
		} else {
			if (s.exists(root_img + "lbl-fail-veredict.PNG") != null) {

				System.err.println("IOCO DOESN'T CONFORM");

				//save test cases
				String nameTestCaseAutFile = "";
				//create folder to save
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd__HH_mm_ss");
				if (!Files.exists(Paths.get(pathSaveTS))) {
					Files.createDirectory(Paths.get(pathSaveTS));
				}
				// first line (testcases)
				for (int j = 0; j < 4; j++) {
					s.type(Key.TAB);
				}
				s.type(Key.DOWN);
				s.type(Key.UP);
				//save test case
				s.click(root_img + "btn-save.PNG");
				nameTestCaseAutFile = pathSaveTS + "1_" + dtf.format(LocalDateTime.now()) + ".aut";
				s.type(nameTestCaseAutFile);
				s.type(Key.ENTER);

				// second line
				s.type(Key.TAB);
				s.type(Key.DOWN);
				Scanner scanner = null;

				Thread.sleep(1500);

				scanner = new Scanner(new File(nameTestCaseAutFile));
				String previous = scanner.useDelimiter("\\Z").next();
				scanner.close();
				String aux = "";

				// other lines
				count = 1;
				while (true) {
					count++;
					nameTestCaseAutFile = pathSaveTS + count + "_" + dtf.format(LocalDateTime.now()) + ".aut";
					s.click(root_img + "btn-save.PNG");
					s.type(nameTestCaseAutFile);
					s.type(Key.ENTER);
					Thread.sleep(1500);
					scanner = new Scanner(new File(nameTestCaseAutFile));
					aux = scanner.useDelimiter("\\Z").next();
					scanner.close();

					if (aux.equals(previous)) {
						Files.delete(Paths.get(nameTestCaseAutFile));
						count--;
						break;
					} else {
						previous = aux;
						s.type(Key.TAB);
						s.type(Key.DOWN);
					}
				}

			}
		}
		
		
		saveOnCSVFile(pathCsv, pathAutSpec, pathAutIUT, conform, total_seconds, "seconds", memory, unityMemory, stateVariation, headerCSV, "",count);

		//close JTorx
		s.click(root_img + "img-close.PNG");
	}
	
	public static void saveOnCSVFile(String pathCsv, String pathModel, String pathIUT, boolean conform, long time,
			String unitTime, String memory, String unityMemory,  boolean stateVariation, List<String> headerCSV, String failPath, int nTestCase) {

		try {

			String testCasesCSV = pathCsv.substring(0, pathCsv.lastIndexOf("\\")) + "\\testCases.csv";
			int iniSubstring = pathModel.lastIndexOf("\\") + 1;
			int endSubstring;
			String variationType;
			String variation;
			
			int numStatesIut,  numStatesModel, numTransitionsIut, numTransitionsModel;
			IOLTS iolts_spec = ImportAutFile.autToIOLTS(pathModel, false, null, null);
			numStatesModel= iolts_spec.getStates().size();
			numTransitionsModel = iolts_spec.getTransitions().size();
			
			IOLTS iolts_iut = ImportAutFile.autToIOLTS(pathIUT, false, null, null);
			numStatesIut = iolts_iut.getStates().size();
			numTransitionsIut = iolts_iut.getTransitions().size();
			
			
			if (stateVariation) {
				endSubstring = pathModel.indexOf("states_spec");
				variationType = "numStates";
			} else {
				endSubstring = pathModel.indexOf("pct_spec");
				variationType = "percentage";
			}

			variation = pathModel.substring(iniSubstring, endSubstring);			

		
			

			ArrayList<String> row = new ArrayList<String>() {
				{
					add(pathModel);
					add(pathIUT);
					add(Objects.toString(numStatesModel));
					add(Objects.toString(numStatesIut));
					add(Objects.toString(numTransitionsModel));
					add(Objects.toString(numTransitionsIut));
					add(Objects.toString(nTestCase));
					add(Objects.toString(conform));
					add(variation);
					add(variationType);
					add(Objects.toString(time));
					add(unitTime);
					add(Objects.toString(memory));
					add(unityMemory);					
				}
			};

			FileWriter csvWriter;

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

		} catch (Exception e)

		{

		}
	}



}
