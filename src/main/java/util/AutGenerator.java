package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AutGenerator {
	public static void main(String[] args) {
		int qtStates = 5;
		List<String> labels = Arrays.asList("?a", "?b", "?c", "!x", "!y");
		int qtTransition = 0;
		String transitions = "";
		String tag = "g";
		String directory = "C:\\Users\\camil\\Desktop\\";
		File file = new File(directory, "model" + qtStates + "states.aut");
		List<String> notVisited = new ArrayList<String>();
		int countState = 0;
		String endState = "", iniState = "";
		boolean complete = false;

		notVisited.add(tag + countState);

		Random rand = new Random();
		BufferedWriter writer = null;
		String newline = System.getProperty("line.separator");
		boolean teraTransicao;
		boolean transicaoProxEstado = false;
		boolean peloMenosUmaTransicao = false;

		iniState = notVisited.remove(0);

		while (iniState != null && (Integer.parseInt(iniState.replace(tag, "")) < (qtStates))) {// enquanto não haver a
																								// quantidade de estados

			transicaoProxEstado = false;
			peloMenosUmaTransicao = false;

			for (String l : labels) {

				teraTransicao = complete;

				if (!teraTransicao && rand.nextInt(2) == 1) {// ´rá ter transição com este rótulo
					teraTransicao = true;
				}

				if (teraTransicao || !peloMenosUmaTransicao) {// ´rá ter transição com este rótulo
					if (!transicaoProxEstado) {
						transicaoProxEstado = true;
						countState++;
						endState = tag + countState;
						notVisited.add(endState);
					} else {
						if (countState > 0) {// ||
							// transicaoProxEstado && rand.nextInt(2) == 1 && countState + 1 >= qtStates
							endState = tag + rand.nextInt(countState);
						} /*
							 * else {
							 * 
							 * 
							 * countState++; endState = tag + countState; notVisited.add(endState); }
							 */
					}

					transitions += "(" + iniState + ", " + l + ", " + endState + ")" + newline;
					qtTransition++;

					peloMenosUmaTransicao = true;
				}
			}

			if (notVisited.size() == 0 && countState < qtStates) {
				countState++;
				notVisited.add(tag + countState);
			}
			if (notVisited.size() > 0) {
				iniState = notVisited.remove(0);
			} else {
				iniState = null;
			}

		}

		try {
			String header = "des(" + tag + "0," + qtTransition + ", " + qtStates + ")" + newline;
			String aut = header + transitions;

			writer = new BufferedWriter(new FileWriter(file));
			writer.write(aut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}

	}

}
