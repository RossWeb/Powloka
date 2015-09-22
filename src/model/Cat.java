package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Cat extends Command {

	private String fileName = null;
	private static Cat instance;
	private final String description = "cat - wyświetla zawartość pliku tekstowego lub wpisany tekst w konsoli";
	private final String additionalDescription = "cat [nazwa pliku lub ścieżki] - wczytanie tekstu z podanego pliku\n"
			+ "cat - po zapisaniu tekstu wyświetla na ekranie\n"
			+ "cat [tekst] - wyświetla wpisany tekst na ekran\n"
			+ "cat [nazwa pliku lub ścieżki] -r [łańcuch szukany] [łańcuch zastępujący] - zastępuje dany łańcuch w odczytanym tekscie";
	private final String name = "cat";
	private final char typeDirectory = 'f';

	private Cat() {
		super.setText(name, description, additionalDescription, typeDirectory);

	}

	@Override
	String execute(String[] params) {
		String output = null;

		if (params.length == 0) {
			output = "pisz tekst: ";
			Beam.getInstance().setCommandFirst("cat ");
			Beam.getInstance().setInCommand(true);

		} else if (params.length > 0) {

			if (params.length == 4 && params[1].equals("-r")) {
				fileName = params[0];
				if (new File(Path.getPath().toString() + "/" + fileName)
						.exists())

					try {
						output = loadText(params[2], params[3]);
						if (output.isEmpty())
							setError("Plik pusty!");
					} catch (IOException e) {
						e.printStackTrace();
						setError("Brak pliku lub odmowa dostępu");

					}

			} else {

				fileName = params[0];
				if (new File(Path.getPath().toString() + "/" + fileName)
						.exists())

					try {
						output = loadText();
						if (output.isEmpty())
							setError("Plik pusty!");
					} catch (IOException e) {
						e.printStackTrace();
						setError("Brak pliku lub odmowa dostępu");

					}
				else {
					output = "";
					for (String s : params)
						output += s + " ";
					if (Beam.getInstance().isInUse() == false)
						output = "->" + output;

				}
			}

		}
		return output;
	}

	private String loadText() throws IOException {

		StringBuilder text = new StringBuilder();
		String temp;
		FileReader fR = new FileReader(Path.getPath().toString() + "/"
				+ fileName);

		BufferedReader bR = new BufferedReader(fR);
		while ((temp = bR.readLine()) != null)
			text.append(temp + "\r\n");
		bR.close();
		fR.close();

		return text.toString();
	}

	private String loadText(String in, String out) throws IOException {

		StringBuilder text = new StringBuilder();
		String temp;
		FileReader fR = new FileReader(Path.getPath().toString() + "/"
				+ fileName);

		BufferedReader bR = new BufferedReader(fR);
		while ((temp = bR.readLine()) != null) {
			temp = temp.replace(in, out);
			
			text.append(temp + "\r\n");
		}
		bR.close();
		fR.close();

		return text.toString();
	}

	static Cat getInstance() {

		if (instance == null)
			instance = new Cat();
		return instance;
	}
}
