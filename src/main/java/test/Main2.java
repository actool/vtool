package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import com.tinkerpop.blueprints.Graph;

import model.IOLTS;
import parser.ImportGraphmlFile;
import util.AutGenerator;

public class Main2 {

	public static void main(String[] args) throws Exception {
		// \iocoCheckerExamples
		// "C:\\Users\\camil\\Desktop\\modelos-jtorx\\iocoCheckerExamples\\VendingMachine\\r2.graphml"
		// "C:\\Users\\camil\\Desktop\\modelos-jtorx\\iocoCheckerExamples\\Newspaper\\spec.graphml"
		//"C:\Users\camil\Desktop\modelos-jtorx\riverCrossing04\models"
		IOLTS iolts = ImportGraphmlFile.graphToIOLTS(
				"C:\\Users\\camil\\Desktop\\modelos-jtorx\\riverCrossing04\\models - Copia\\s2.graphml", false, null,
				null);
		//System.out.println(iolts);
		String aut = AutGenerator.ioltsToAut(iolts);

		String aut_file = "C:\\Users\\camil\\Desktop\\modelos-jtorx\\_graphmlToAut\\riverCrossing\\s2_.aut";
		BufferedWriter writer = new BufferedWriter(new FileWriter(aut_file));
		writer.write(aut);
		writer.close();

	}

}
