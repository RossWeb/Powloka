package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class Beam extends Command {

	private static Beam instance;
	private final String description = ">> / > - przekierowanie strumienia ";
	private final String additionalDescription = "> [nazwa pliku] - utworzenie pliku \n>> [nazwa pliku] - nadpisanie pliku\n"
			+ "[polecenie] >>/> [nazwa pliku] - zapisanie danych wyświetlanych na ekranie do pliku";
	private final String name = ">";
	private String savedText = null;

	private String commandFirst = null;
	private String commandLast = null;
	private boolean appendFile = false;
	private boolean inCommand = false;
	private boolean inUse = false;

	private Beam() {
		super.setText(name, description, additionalDescription, ' ');
	}

	void setTextToSave(String text) {

		this.savedText = text;
	}

	@Override
	String execute(String[] params) {
		String error = null;

		if (params.length == 1) {
			error = "";
			try {
				writeToFile(params[0]);
				error = "Zapisano!";
			} catch (IOException e) {
				setError("Brak pliku lub odmowa dostępu");

				e.printStackTrace();

			}
		}
		resetValue();
		this.commandLast = null;
		this.inUse = false;
		this.appendFile = false;
		return error;
	}

	void resetValue() {
		this.savedText = null;

		this.commandFirst = null;

		this.inCommand = false;

	}

	String getCommandInner(String addText) {
		String tempCommand = this.commandFirst;
		resetValue();
		return tempCommand + addText;

	}

	boolean isInCommand() {
		return inCommand;
	}

	void setInCommand(boolean inCommand) {
		this.inCommand = inCommand;
	}

	void checkStream(String streamText) {
		String[] token = null;
		this.inUse = true;
		String separator = " > ";
		if (Pattern.compile(".*\\s>{2}\\s.*").matcher(streamText).matches()) {
			separator = " >> ";
			appendFile = true;
		}
		token = streamText.split(separator);

		commandFirst = token[0].trim();
		commandLast = token[1].trim();

	}

	boolean isInUse() {
		return inUse;
	}

	void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	void setCommandFirst(String commandFirst) {
		if (commandFirst.contains(">>")) {
			appendFile = true;
			commandFirst = commandFirst.replace(">>", ">");
		}

		this.commandFirst = commandFirst;
	}

	String getCommandFirst() {
		return commandFirst;
	}

	String getCommandLast() {
		return commandLast;
	}

	private void writeToFile(String nameFile) throws IOException {
		String pathFile = "";
		if (nameFile.contains((":"))) {
			pathFile = nameFile;
		} else
			pathFile = Path.getPath().toString() + "/" + nameFile;
		FileWriter fW = new FileWriter(pathFile, this.appendFile);
		BufferedWriter bW = new BufferedWriter(fW);
		if (savedText != null) {

			bW.write(savedText);
		}

		bW.close();
		fW.close();
	}

	static Beam getInstance() {

		if (instance == null)
			instance = new Beam();
		return instance;
	}

}
