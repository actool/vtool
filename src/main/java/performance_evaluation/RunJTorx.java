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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

import algorithm.Operations;
import model.Automaton_;
import model.State_;
import model.Transition_;
import util.Constants;

import org.sikuli.script.*;

import com.android.dx.util.FileUtils;

public class RunJTorx {
	public static void run(String batchFileJTorx, String root_img, String pathAutSpec, String pathAutIUT,
			String pathSaveTS) throws Exception {

		long total_seconds = 0;
		long time_ini, time_end;

		// SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

		int count = 0;
		try {

			Desktop d = Desktop.getDesktop();
			d.open(new File(batchFileJTorx));
			// Runtime.getRuntime().exec(command);
			Thread.sleep(500);// até abrir o jtorx

			Screen s = new Screen();

			s.type(root_img + "inp-model.PNG", pathAutSpec);// spec
			System.err.println(root_img + "inp-model.PNG");
			for (int i = 0; i < 8; i++) {
				s.type(Key.TAB);
			}

			s.type(pathAutIUT);

			s.click(root_img + "cb-interpretation.PNG");
			s.type(Key.DOWN);
			s.type(Key.DOWN);// ?in !out
			s.click();

			s.type(Key.TAB);// second cb
			s.type(Key.DOWN);// Strace

			s.click(root_img + "item-menu-ioco.PNG");

			time_ini = System.currentTimeMillis();
			s.click(root_img + "btn-check.PNG");

			long t0 = 0;

			while (true) {
				try {
					t0 = System.currentTimeMillis();
					Object a = s.find(new Pattern(root_img + "lbl-result.PNG").similar(1.0f));
					// System.out.println("TRY .. " + t0);

				} catch (FindFailed e) {
					time_end = System.currentTimeMillis();
					// System.out.println("CATCH ... " +System.currentTimeMillis());
					break;
				}

				// System.out.println("WHILE..."+formatter.format(new
				// Date(System.currentTimeMillis())));

			}

			total_seconds = ((time_end - (time_end - t0)) - time_ini); // / 1000;

			// total_seconds = ( time_end - time_ini) - (time_end - t0); // / 1000;
			// System.out.println("total: " + total_seconds);
			// System.out.println("after waitVanish: " + total_seconds + " milisegundos "
			// + formatter.format(new Date(System.currentTimeMillis())));

			if (s.exists(root_img + "lbl-conform-veredict.PNG") != null) {
				System.err.println("IOCO CONFORM");
			} else {
				if (s.exists(root_img + "lbl-fail-veredict.PNG") != null) {

					System.err.println("IOCO DOESN'T CONFORM");

					String nameTestCaseAutFile = "";
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
							break;
						} else {
							previous = aux;
							s.type(Key.TAB);
							s.type(Key.DOWN);
						}
					}

				}
			}
			System.err.println("TERMINOU: " + total_seconds + " milisegundos");

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

			s.click(root_img + "img-close.PNG");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

			String pathAutSpec = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\+1000\\iut1000states.aut";
			String pathAutIUT = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\+1000\\iut1000states.aut";

			String root_img = new File("src/main/java/performance_evaluation/jtorx-img").getCanonicalPath() + "\\";
			String pathSaveTS = "C:\\Users\\camil\\Desktop\\Nova pasta\\";

			run(batchFileJTorx, root_img, pathAutSpec, pathAutIUT, pathSaveTS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
