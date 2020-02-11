package performance_evaluation;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.rmi.server.Operation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import com.tinkerpop.blueprints.Graph;

import algorithm.Operations;
import algorithm.TestGeneration;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import model.Automaton_;
import model.IOLTS;
import model.State_;
import model.Transition_;
import parser.ImportAutFile;
import parser.ImportGraphmlFile;
import util.AutGenerator;
import util.Constants;

public class Generation_Run {

	public static void main(String[] args) throws Exception {
		List<Integer> m_array = Arrays.asList(5, 15, 25, 35, 45, 55, 65, 75, 85, 95, 105);

		List<Integer> spec_states = Arrays.asList(5, 15, 25, 35, 45);//, 55
		List<Integer> n_experimentos = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		IOLTS S, multigraph;
		String multigraphName;
		double time_ini, time_end = 0, total_seconds;
		// Get the Java runtime
		Runtime runtime = Runtime.getRuntime();
		double memory;

		String root = "C:\\Users\\camil\\Google Drive\\UEL\\svn\\ferramenta\\teste desempenho\\teste-geração\\1pct\\";

		end: for (int spec_state : spec_states) {
			for (int iut_state : m_array) {
				for (int experiment : n_experimentos) {

					File folder = new File(
							root
									+ spec_state + "states\\alfabeto10\\iut" + iut_state + "\\experimento" + experiment
									+ "\\" + spec_state + "states_spec.aut");
					S = ImportAutFile.autToIOLTS(folder.getAbsolutePath(), false, new ArrayList<>(), new ArrayList<>());
					for (int m : m_array) {
						// Get the Java runtime
						runtime = Runtime.getRuntime();
						// Run the garbage collector
						runtime.gc();
						
						
						// generate multigraph
						time_ini = System.nanoTime();
						multigraph = TestGeneration.multiGraphD(S, m).toIOLTS(S.getInputs(), S.getOutputs());
						time_end = System.nanoTime();
					
						total_seconds = (time_end - time_ini);
						total_seconds = (total_seconds / 1e9);//1e6 milisegundos
						 memory = (runtime.totalMemory() - runtime.freeMemory());
						
						multigraph
								.setInitialState(new State_(multigraph.getInitialState().getName().replace(",", "_")));
						multigraphName = saveMultigraphFile(root
								+ spec_state + "states\\alfabeto10\\iut" + iut_state + "\\experimento" + experiment
								+ "\\", m, folder.getAbsolutePath(), multigraph, S);
						
						saveOnCSVFile(root+"performance.csv", "everest", folder.getAbsolutePath(), S.getStates().size(), S.getTransitions().size(), multigraphName, multigraph.getStates().size(), multigraph.getTransitions().size(), total_seconds,
								"seconds",  memory, "bytes", S.getAlphabet().size(),Objects.toString(experiment));
						
						
						
					}
					//break end;
					
				}

			}
		}

		// String rootPath = "C:\\Users\\camil\\Google
		// Drive\\UEL\\svn\\ferramenta\\teste
		// desempenho\\10-50states\\result\\i-o\\ioco-n-conf-2pct\\10inp-2out\\";
		// String tool = "everest";
		// String path = rootPath+tool+".csv";
		// String COMMA_DELIMITER = ",";
		// boolean states;
		// String alfabeto, experimento;
		// List<String> values;
		// String header = "";
		//
		// List<List<String>> records = new ArrayList<>();
		// try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		// String line;
		// header = br.readLine();// pula cabeçalho
		// header += "alphabet" + COMMA_DELIMITER + "experiment";
		// records.add(Arrays.asList(header.split(COMMA_DELIMITER)));
		// while ((line = br.readLine()) != null) {
		// alfabeto = "";
		// experimento = "";
		// values = new ArrayList<String>();
		// values.addAll(Arrays.asList(line.split(COMMA_DELIMITER)));
		//
		//
		//
		// //values.add("");//jtorx ioco
		// for (String l : Arrays.asList(values.get(1).replace("\\", "@").split("@"))) {
		// if (l.contains("alfabeto")) {
		// String s = (l.replace("alfabeto", ""));
		// values.add(s);
		// }
		//
		// if (l.contains("experimento")) {
		// values.add(l.replace("experimento", ""));
		// }
		// }
		// records.add(values);
		//
		// }
		// }
		//
		// FileWriter csvWriter = new FileWriter(rootPath+"new-"+tool+".csv");
		// for (List<String> rowData : records) {
		// csvWriter.append(String.join(COMMA_DELIMITER, rowData));
		// csvWriter.append("\n");
		// }
		//
		// csvWriter.flush();
		// csvWriter.close();

	}

	public static String saveMultigraphFile(String folder, int m, String pathSpecification, IOLTS multigraph, IOLTS S) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

			String fileContent = "";
			// save m
			fileContent += Constants.MAX_IUT_STATES + m + "] \n";
			// save spec
			// fileContent += AutGenerator.ioltsToAut(new IOLTS(S.getStates(),
			// S.getInitialState(), S.getAlphabet(),
			// S.getTransitions(), S.getInputs(), S.getOutputs()).removeDeltaTransitions());

			fileContent += AutGenerator.ioltsToAut(
					ImportAutFile.autToIOLTS(pathSpecification, false, new ArrayList<>(), new ArrayList<>()));
			// save multigraph
			fileContent += Constants.SEPARATOR_MULTIGRAPH_FILE;
			fileContent += AutGenerator.ioltsToAut(new IOLTS(multigraph.getStates(), multigraph.getInitialState(),
					multigraph.getAlphabet(), multigraph.getTransitions(), S.getInputs(), S.getOutputs()));
			String fileName = "spec-multigraph_" + dateFormat.format(new Date()) + " - m" + m + ".aut";
			File file = new File(folder, fileName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(fileContent);
			writer.close();
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void saveOnCSVFile(String pathCsv, String tool, String pathModel, int statesModel, int transitionsModels, String pathMultigraph, int statesMultigraph, int transitionsMultigraph, double time,
			String unitTime, double memory, String unityMemory, int alphabet, String experiment) {

		List<String> headerCSV = Arrays.asList("tool", "model", "statesModel", "transitionsModels", "multigraph", "statesMultigraph",
				"transitionsMultigraph", "time", "unit", "memory", "unit", "alphabet", "experiment");
		String delimiterCSV = ",";
		try {
		
			ArrayList<String> row = new ArrayList<String>();
			row.add(tool);
			row.add(pathModel);
			row.add(Objects.toString(statesModel));
			row.add(Objects.toString(transitionsModels));
			row.add(pathMultigraph);
			row.add(Objects.toString(statesMultigraph));
			row.add(Objects.toString(transitionsMultigraph));
			row.add(String.format("%.2f", time).replace(",", "."));
			row.add(unitTime);
			row.add(Objects.toString(memory));
			row.add(unityMemory);
			row.add(Objects.toString(alphabet));
			row.add(experiment);
		
			
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

		} catch (Exception e)

		{
			e.printStackTrace();
		}
	}

}
