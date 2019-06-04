package parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ReadFile extends Thread {
	private int iniLine;
	private int endLine;
	String file;
	String id;

	public void init(int iniLine, int endLine, String file, String id) {
		this.iniLine = iniLine;
		this.endLine = endLine;
		this.file = file;
		this.id = id;
	}

	public void run() {
		int count = this.iniLine;
		String line;
		Supplier<Stream<String>> streamSupplier;

		while (count < this.endLine) {
			Path path = Paths.get(this.file);
			streamSupplier = () -> {
				try {
					return Files.lines(path, StandardCharsets.ISO_8859_1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			};
			
			//System.out.println(count+1);
			line = streamSupplier.get().skip(count+1).findFirst().get();
			System.out.println(id + " - " +count+ " <-- " + line);
			
			count++;
		}
	}

}
