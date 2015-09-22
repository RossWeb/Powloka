package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

final class Ls extends Command {

	private static Ls instance;

	private final String description = "ls - polecenie wyświetlenia plików i katalogów";
	private final String additionalDescription = "ls - wczytanie plików i katalogów \n"
			+ "ls [ścieżka] - wczytanie plików i katalogów z podanej ścieżki \n"
			+ "ls -fhd - wczytanie filtrowane parametrem : f - plik, h - plik ukryty, d - katalog\n" 
			+ "ls -[fhd] [*nazwa*.rozszerzenie] - filtrowanie plików";
	private final String name = "ls";
	private final char typeDirectory = 'd';

	private List<String> listFile = new ArrayList<String>();

	private Ls() {
		super.setText(name, description, additionalDescription, typeDirectory);
	}

	@Override
	String execute(String[] params) {
		boolean file = false;
		boolean hidden = false;
		boolean directory = false;
		String paramPath = null;
		String paramCommand = null;
		String paramCriteria = null;
		String tempList = null;
		File old = Path.getPath();

		if (params.length == 0) { // wyswietla zdefiniowana liste bez zadnego parametru
			file = true;
			directory = true;
			hidden = false;

		} else if (params.length == 1) { // w przypadku jednego parametru
			if (params[0].charAt(0) == '-')  // jesli paramter pierwszy zawiera -jest argumentem
				paramCommand = params[0];
			else
				paramPath = params[0]; // inaczej jest sciezka dla polecenia
		} else if (params.length == 2) { // w przypadku dwoch paramterow
			if (params[1].charAt(0) == '-') { // jesli paramter drugi jest argumentem
				paramCommand = params[1];
				paramPath = params[0];
			} else if (params[0].charAt(0) == '-') { // jesli paramter pierwszy jest argumentem
				paramCommand = params[0];
				paramCriteria = params[1];
			}

		} else if (params[1].charAt(0) == '-' && params.length == 3) { // przy rozpatrywaniu sciezki, paramteru i wyszukiwania
			paramCommand = params[1];
			paramPath = params[0];
			paramCriteria = params[2];
		}

		if (paramCommand != null) { // ustawienie argumentu
			if (paramCommand.contains("f"))
				file = true;
			if (paramCommand.contains("h"))
				hidden = true;
			if (paramCommand.contains("d"))
				directory = true;
		}
		if (paramPath != null) {
			if (Cd.getInstance().getToFolder(paramPath) != null){
				if (paramCommand == null) {
					file = true;
					directory = true;
					hidden = false;
				}
			}else{
				tempList = "";
				setError("Ścieżka niepoprawna!");
			}
		}

		
		if (hidden != false || file != false || directory != false) {
			setListFile(file, directory, hidden, paramCriteria);
			hidden = file = directory = false;
			paramCommand = null;
			paramPath = null;
			paramCriteria = null;

			if (listFile.isEmpty()){
				tempList = "";
				setError("Brak plików lub katalogów");
			}else {
				tempList = "";
				for (String s : listFile) {

					tempList += s + "\r\n";
				}
			}
		} 
		Path.setPath(old.toString());

		return tempList;
	}

	private void setListFile(boolean file, boolean directory, boolean hidden,
			String paramCriteria) {
		listFile.clear();
		String nameFile = null;
		File[] files = Path.getPath().listFiles();
		if (files != null) {
			for (File f : files) {
				nameFile = f.getName().toLowerCase();
				if (paramCriteria != null){
			
			
					if (paramCriteria.charAt(0) == '*'){
						
						paramCriteria = ".+" + paramCriteria.substring(1, paramCriteria.length());
					}else if(paramCriteria.charAt(paramCriteria.length()-1) == '*'){
						
						paramCriteria = paramCriteria.substring(0, paramCriteria.length()-1) + ".+"; 
						
					}else if(paramCriteria.contains("*")){
						String[] sp = paramCriteria.split("\\*");
						
						paramCriteria = sp[0] + ".+" + sp[1];
					}
					
					if (!Pattern.compile(paramCriteria).matcher(nameFile)
							.matches())
						nameFile = null;
				}
				if (nameFile != null) {
					if (f.isDirectory() && directory)
						listFile.add(f.getName() + "/");
					else if (f.isFile() && file)
						listFile.add(f.getName());
					else if (f.isHidden() && hidden)
						listFile.add("." + f.getName());

				}
			}
		}

	}

	List<String> getDirectory(char type) {
		if (type == 'd')
			setListFile(false, true, false, null);
		else if (type == 'f')
			setListFile(true, false, false, null);
		else if(type == 'a') //directory i file
			setListFile(true, true, false, null);
		return listFile;
	}

	static Ls getInstance() {

		if (instance == null)
			instance = new Ls();
		return instance;
	}

}
