package performance_evaluation;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.rmi.server.Operation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tinkerpop.blueprints.Graph;

import algorithm.Operations;
import model.Automaton_;
import model.IOLTS;
import model.State_;
import parser.ImportAutFile;
import parser.ImportGraphmlFile;
import util.AutGenerator;

public class Main2 {

	public static void main(String[] args) throws Exception {

		String path = "C:\\Users\\camil\\Documents\\aut-modelos\\iolts-impl-q.aut";
		
		IOLTS iolts = ImportAutFile.autToIOLTS(path, false, null, null);
		
		
		Automaton_ a = iolts.ioltsToAutomaton();
		List<State_> states = new ArrayList<>();
		states.add(new State_("q0"));
		states.add(new State_("q3"));
		a.setFinalStates(states);
		
		//System.out.println(Operations.getWordsFromAutomaton(a));//true
		
		
		
		
		
		
		// // \iocoCheckerExamples
		// //
		// "C:\\Users\\camil\\Desktop\\modelos-jtorx\\iocoCheckerExamples\\VendingMachine\\r2.graphml"
		// //
		// "C:\\Users\\camil\\Desktop\\modelos-jtorx\\iocoCheckerExamples\\Newspaper\\spec.graphml"
		// //"C:\Users\camil\Desktop\modelos-jtorx\riverCrossing04\models"
		// IOLTS iolts = ImportGraphmlFile.graphToIOLTS(
		// "C:\\Users\\camil\\Desktop\\modelos-jtorx\\riverCrossing04\\models -
		// Copia\\s2.graphml", false, null,
		// null);
		// //System.out.println(iolts);
		// String aut = AutGenerator.ioltsToAut(iolts);
		//
		// String aut_file =
		// "C:\\Users\\camil\\Desktop\\modelos-jtorx\\_graphmlToAut\\riverCrossing\\s2_.aut";
		// BufferedWriter writer = new BufferedWriter(new FileWriter(aut_file));
		// writer.write(aut);
		// writer.close();

	}

}
