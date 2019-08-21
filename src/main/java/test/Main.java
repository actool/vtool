package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

import algorithm.Operations;
import model.Automaton_;
import model.State_;
import model.Transition_;
import util.Constants;

import org.sikuli.script.*;

public class Main {

	public static void main(String[] args) {
		/*
		 * Automaton_ a = new Automaton_(); State_ s0= new State_("s0"); State_ s1= new
		 * State_("s1"); State_ s2= new State_("s2"); State_ s3= new State_("s3");
		 * 
		 * a.addState(s0); a.addState(s1); a.addState(s2); a.addState(s3);
		 * 
		 * a.setInitialState(s0); a.addFinalStates(s3);
		 * 
		 * a.addTransition(new Transition_(s0,"a",s1)); a.addTransition(new
		 * Transition_(s0,"b",s2)); a.addTransition(new Transition_(s1,"b",s3));
		 * a.addTransition(new Transition_(s1,"b", s2)); a.addTransition(new
		 * Transition_(s2,"a",s3)); a.addTransition(new Transition_(s3, "a", s3));
		 * 
		 * System.out.println(Operations.getWordsFromAutomaton(a, false));
		 */
		// String failPath = "Test case: b\r\n" +
		// "\r\n" +
		// "Model outputs: [δ]\r\n" +
		// "\r\n" +
		// " path:s0 -> s3 ->\r\n" +
		// " output: [δ]\r\n" +
		// "\r\n" +
		// "\r\n" +
		// "Implementation outputs: [x]\r\n" +
		// "\r\n" +
		// " path:q0 -> q3 ->\r\n" +
		// " output: [x]\r\n" +
		// "\r\n" +
		// "################################################################## \r\n" +
		// "Test case: δ -> b\r\n" +
		// "\r\n" +
		// "Model outputs: [δ]\r\n" +
		// "\r\n" +
		// " path:s0 -> s0 ->s3 ->\r\n" +
		// " output: [δ]\r\n" +
		// "\r\n" +
		// "\r\n" +
		// "Implementation outputs: [x]\r\n" +
		// "\r\n" +
		// " path:q0 -> q0 ->q3 ->\r\n" +
		// " output: [x]\r\n" +
		// "\r\n" +
		// "################################################################## \r\n" +
		// "Test case: b -> b -> b\r\n" +
		// "\r\n" +
		// "Model outputs: [δ]\r\n" +
		// "\r\n" +
		// " path:s0 -> s3 ->s0 ->s3 ->\r\n" +
		// " output: [δ]\r\n" +
		// "\r\n" +
		// "\r\n" +
		// "Implementation outputs: [x]\r\n" +
		// "\r\n" +
		// " path:q0 -> q3 ->q0 ->q3 ->\r\n" +
		// " output: [x]\r\n" +
		// "\r\n" +
		// "################################################################## \r\n" +
		// "Test case: a -> a -> b -> b\r\n" +
		// "\r\n" +
		// "Model outputs: [δ]\r\n" +
		// "\r\n" +
		// " path:s0 -> s1 ->s3 ->s0 ->s3 ->\r\n" +
		// " output: [δ]\r\n" +
		// "\r\n" +
		// "\r\n" +
		// "Implementation outputs: [x]\r\n" +
		// "\r\n" +
		// " path:q0 -> q1 ->q3 ->q0 ->q3 ->\r\n" +
		// " output: [x]\r\n" +
		// "\r\n" +
		// "################################################################## \r\n" +
		// "Test case: a -> a\r\n" +
		// "\r\n" +
		// "Model outputs: [δ]\r\n" +
		// "\r\n" +
		// " path:s0 -> s1 ->s3 ->\r\n" +
		// " output: [δ]\r\n" +
		// "\r\n" +
		// "\r\n" +
		// "Implementation outputs: [x]\r\n" +
		// "\r\n" +
		// " path:q0 -> q1 ->q3 ->\r\n" +
		// " output: [x]\r\n" +
		// "\r\n" +
		// "################################################################## \r\n" +
		// "Test case: b -> a\r\n" +
		// "\r\n" +
		// "Model outputs: [δ]\r\n" +
		// "\r\n" +
		// " path:s0 -> s3 ->s3 ->\r\n" +
		// " output: [δ]\r\n" +
		// "\r\n" +
		// "\r\n" +
		// "Implementation outputs: [x]\r\n" +
		// "\r\n" +
		// " path:q0 -> q3 ->q3 ->\r\n" +
		// " output: [x]\r\n" +
		// "\r\n" +
		// "################################################################## \r\n" +
		// "Test case: a -> a -> a\r\n" +
		// "\r\n" +
		// "Model outputs: [δ]\r\n" +
		// "\r\n" +
		// " path:s0 -> s1 ->s3 ->s3 ->\r\n" +
		// " output: [δ]\r\n" +
		// "\r\n" +
		// "\r\n" +
		// "Implementation outputs: [x]\r\n" +
		// "\r\n" +
		// " path:q0 -> q1 ->q3 ->q3 ->\r\n" +
		// " output: [x]\r\n" +
		// "\r\n" +
		// "################################################################## \r\n" +
		// "Test case: a -> b\r\n" +
		// "\r\n" +
		// "Model outputs: [x]\r\n" +
		// "\r\n" +
		// " path:s0 -> s1 ->s2 ->\r\n" +
		// " output: [x]\r\n" +
		// "\r\n" +
		// "\r\n" +
		// "Implementation outputs: [δ]\r\n" +
		// "\r\n" +
		// " path:q0 -> q1 ->q2 ->\r\n" +
		// " output: [δ]\r\n" +
		// "\r\n" +
		// "################################################################## \r\n" +
		// "Test case: a -> x\r\n" +
		// "\r\n" +
		// "Model outputs: [x]\r\n" +
		// "\r\n" +
		// " path:s0 -> s1 ->s2 ->\r\n" +
		// " output: [x]\r\n" +
		// "\r\n" +
		// "\r\n" +
		// "Implementation outputs: [δ]\r\n" +
		// "\r\n" +
		// " path:q0 -> q1 ->q2 ->\r\n" +
		// " output: [δ]\r\n" +
		// "\r\n" +
		// "################################################################## \r\n" +
		// "Test case: a -> b -> b\r\n" +
		// "\r\n" +
		// "Model outputs: [x]\r\n" +
		// "\r\n" +
		// " path:s0 -> s1 ->s2 ->s2 ->\r\n" +
		// " output: [x]\r\n" +
		// "\r\n" +
		// "\r\n" +
		// "Implementation outputs: [δ]\r\n" +
		// "\r\n" +
		// " path:q0 -> q1 ->q2 ->q2 ->\r\n" +
		// " output: [δ]\r\n" +
		// "\r\n" +
		// "################################################################## \r\n" +
		// "Test case: a -> x -> b\r\n" +
		// "\r\n" +
		// "Model outputs: [x]\r\n" +
		// "\r\n" +
		// " path:s0 -> s1 ->s2 ->s2 ->\r\n" +
		// " output: [x]\r\n" +
		// "\r\n" +
		// "\r\n" +
		// "Implementation outputs: [δ]\r\n" +
		// "\r\n" +
		// " path:q0 -> q1 ->q2 ->q2 ->\r\n" +
		// " output: [δ]\r\n" +
		// "\r\n" +
		// "################################################################## \r\n" +
		// "";
		// String[] lines = failPath.split("\r\n");
		// List<String> testCases = new ArrayList<>();
		// for (String s : lines) {
		// if (s.contains("Test case:")) {
		// testCases.add(s.replace("Test case: ", "").replaceAll("\b\r\n",
		// "").replaceAll("#", "").replace(" -> ", ""));
		// }
		// }
		//
		// System.out.println(testCases);

		String path = "C:\\Users\\camil\\Google Drive\\UEL\\jtorx\\jtorx-1.11.2-win\\jtorx.bat";
		String txtFile = "C:\\Users\\camil\\Desktop\\teste.txt";
		String root_aut = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\";// "C:\\Users\\camil\\Desktop\\Nova pasta
																		// (2)\\+1000\\"

		boolean parar = false;
		long total_seconds = 0;
		long time_ini, time_end;
		ProcessBuilder processBuilder = new ProcessBuilder(path);

		int count = 0;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {

			Process process = processBuilder.start();

			// Thread.sleep(1500);// até abrir o jtorx

			String root_img = "C:\\Users\\camil\\Desktop\\jtorx-img\\";
			Screen s = new Screen();
			// spec
			s.type(root_img + "inp-model.PNG", root_aut + "vending-machine-spec-nconf.aut");// iut1000states.aut

			for (int i = 0; i < 8; i++) {
				s.type(Key.TAB);
			}
			// iut
			s.type(root_aut + "vending-machine-iut.aut");// iut1000states.aut
			s.click(root_img + "cb-interpretation.PNG");
			s.type(Key.DOWN);
			s.type(Key.DOWN);// ?in !out
			s.click();

			s.type(Key.TAB);// second cb
			s.type(Key.DOWN);// Strace
			

			s.click(root_img + "item-menu-ioco.PNG");

			time_ini = System.currentTimeMillis();

			s.click(root_img + "btn-check.PNG");
			
			s.waitVanish(new Pattern(root_img + "lbl-result.PNG").similar(0.9f));

			InputStream i = process.getInputStream();
			BufferedReader reader0 = new BufferedReader(new InputStreamReader(i));
			BufferedReader reader = new BufferedReader(new InputStreamReader(i));// process.getInputStream()
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

			String line = "";
			while (reader.ready() && (line = reader.readLine()) != null) {// (line = reader.readLine()) != null
				count++;
				line = line + " time: " + formatter.format(new Date(System.currentTimeMillis())) + "\n"; //
				writer.write(line);
				writer.newLine();
				time_end = System.currentTimeMillis();
				total_seconds = (time_end - time_ini) / 1000;
			}

			if (s.exists(root_img + "lbl-conform-veredict.PNG") != null) {
				System.err.println("CONF");
			} else {
				if (s.exists(root_img + "lbl-fail-veredict.PNG") != null) {
					System.err.println("NAO CONF");
				}
			}
			System.err.println("TERMINOU: " + total_seconds + " segundos");

			s.click(root_img + "img-close.PNG");

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				writer.close();
				System.exit(0);
			}

		} catch (Exception e) {
			System.out.println("------EXCEPTION");
			e.printStackTrace();
		}

		// String path = "C:\\Users\\camil\\Google
		// Drive\\UEL\\jtorx\\jtorx-1.11.2-win\\jtorx.bat";
		// String txtFile = "C:\\Users\\camil\\Desktop\\teste.txt";
		//
		// ProcessBuilder processBuilder = new ProcessBuilder(path);
		// try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {
		//
		// Process process = processBuilder.start();
		//
		// Thread.sleep(2000);// até abrir o jtorx
		//
		// // StringBuilder output = new StringBuilder();
		//
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(process.getInputStream()));
		//
		// SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		//
		// String line;
		// while ((line = reader.readLine()) != null) {
		// line = line + " time: " + formatter.format(new
		// Date(System.currentTimeMillis())) + "\n";
		// // output.append(line);
		// writer.write(line);
		// writer.newLine();
		// }
		//
		// int exitVal = process.waitFor();
		// if (exitVal == 0) {
		// // System.out.println(output);
		// writer.close();
		// System.exit(0);
		// } else {
		//
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

}
