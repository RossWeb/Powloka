package model;

import java.io.File;

public class Remove extends Command {

	private static Remove instance;
	private String description = "remove - usunięcie pliku lub katalogu";
	private String additionalDescription = "remove [nazwa pliku lub katalogu] - usuwa dany plik lub katalog";
	private String name = "remove";
	private final char typeDirectory = 'a'; 

	Remove() {
		super.setText(name, description, additionalDescription, typeDirectory);
	}

	@Override
	String execute(String[] params) {
		String output = null;

		if (params.length == 1) {

		output = remove(params[0]);
		}

		return output;
	}

	static Remove getInstance() {

		if (instance == null)
			instance = new Remove();
		return instance;
	}

	private String remove(String name){
		
		String execute = "";
		File file = new File(Path.getPath().toString() + "/" + name);
		if(file.exists()){
			file.delete();
			execute = "Wykonano";
		}else
			setError("Plik nie istnieje!");
			
		return execute;
	}
}
