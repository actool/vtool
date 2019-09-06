package performance_evaluation;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

public class RunEverest {
	public static void main(String[] args) throws Exception {
		String root_img = new File("src/main/java/performance_evaluation/everet-img").getCanonicalPath() + "\\";
//		String pathAutSpec = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\+1000\\iut1000states.aut";
//		String pathAutIUT = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\+1000\\iut1000states.aut";
		
//		 String pathAutSpec =
//		 "C:\\Users\\camil\\Documents\\aut-modelos\\iolts-spec.aut";
//		 String pathAutIUT =
//		 "C:\\Users\\camil\\Documents\\aut-modelos\\iolts-impl-r.aut";
		
		 String pathAutSpec =
				 "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\-1000\\250-500\\1555states_iut_0.aut";
				 String pathAutIUT =
				 "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\-1000\\250-500\\1555states_iut_0.aut";

		String batchFileEverest = "C:\\Users\\camil\\Desktop\\everest.bat";

		run(batchFileEverest, root_img, pathAutSpec, pathAutIUT);

	}
	
	
	public static void run(String batchFileEverest, String root_img, String pathAutSpec, String pathAutIUT) throws Exception {
		
		//open jar
		Desktop d = Desktop.getDesktop();
		d.open(new File(batchFileEverest));

		//wait for open
		Thread.sleep(1000);

		//type spec model
		Screen s = new Screen();
		s.type(root_img + "inp-model.PNG", pathAutSpec);
		s.type(Key.ENTER);

		//type iut model
		s.type(root_img + "inp-iut.PNG", pathAutIUT);
		s.type(Key.ENTER);

		//model type (IOLTS)
		s.click(root_img + "cb-modelType.PNG");
		s.type(Key.DOWN);
		s.type(Key.ENTER);

		// label (?in !out)
		s.click(root_img + "cb-label.PNG");
		s.type(Key.DOWN);
		s.type(Key.ENTER);

		//menu ioco
		s.click(root_img + "item-menu-ioco.PNG");

		//wait until open ioco view
		while (true) {
			try {
				System.currentTimeMillis();
				s.find(new Pattern(root_img + "img-processing.PNG").similar(1.0f));
			} catch (FindFailed e) {
				break;
			}
		}

		//verify ioco
		s.click(root_img + "btn-verify.PNG");

		long time_ini, time_end, total_seconds;
		time_ini = System.currentTimeMillis();
		long t0 = 0;
		//wait until finish verification
		while (true) {
			try {				
				t0 = System.currentTimeMillis();
				s.find(new Pattern(root_img + "img-processing.PNG").similar(1.0f));//"lbl-verdict.PNG"			
			} catch (FindFailed e) {
				time_end = System.currentTimeMillis();
				break;
			}

		}
		
		
		total_seconds = ((time_end - (time_end - t0)) - time_ini);
		if(total_seconds == 0) {
			total_seconds = time_end - time_ini;
		}
		
		System.err.println("FINISHED: " + total_seconds + " milliseconds");

		//get memory consumption (cmd)
		String s_ = "";
		Process p = Runtime.getRuntime().exec("TASKLIST /FI \"IMAGENAME eq java.exe\"");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while ((s_ = stdInput.readLine()) != null) {
			s_ = s_.replaceAll("\\s{2,}", " ").trim();
			String array[] = s_.split(" ");
			if (array.length == 6 && array[0].equals("java.exe")) {
				System.err.println("memory: " + array[4] + " measure: " + array[5]);
			}
		}

		
		//get veredict by label
		if (s.exists(root_img + "lbl-fail-veredict1.PNG") != null) {
			System.err.println("IOCO DOESN'T CONFORM");
			//get test suite
			s.type(Key.TAB);
			s.type("a", KeyModifier.CTRL);
			s.type("c", KeyModifier.CTRL);
			s.find(root_img + "btn-verify.PNG");
			String testSuite = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
					.getData(DataFlavor.stringFlavor);
		} else {
			if (s.exists(root_img + "lbl-conform-veredict.PNG") != null) {
				System.err.println("IOCO CONFORM");
			}
		}
		//close everest
		 s.click(root_img + "img-close.PNG");
	}
	


}
