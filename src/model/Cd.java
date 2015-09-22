package model;


final class Cd extends Command {

	private static Cd instance;
	private final String description = "cd - polecenie zmiany katalogu lub litery dysku";
	private final String additionalDescription = "cd [nazwa folderu lub ścieżki] - otwarcie danego folderu\n"
			+ "cd .. - powrót do nadrzędnego folderu\ncd /? - lista dostępnych dysków\n"
			+ "cd /[duża litera dysku] - zmiana dysku ";
	private final String name = "cd";
	private final char typeDirectory = 'd';

	private Cd() {
		super.setText(name, description, additionalDescription, typeDirectory);
	}

	@Override
	String execute(String[] params) {

		String value = null;
		if (params.length > 0) { // polecenie wykonuje sie tylko z parametrem
			char firstChar = params[0].charAt(0); // pierwszy znak warunkuje
													// dalsze rozpatrywanie
			switch (firstChar) {

			case '/':
				if (params[0].length() == 1)
					value = Path.clearPath(); // powrót do roota dysku
				else if (params[0].length() == 2) {
					if (params[0].charAt(1) == '?')
						value = Path.getRootList(); // wyswietlenie listy dyskow
					else {
						String[] dl = Path.getDriveLetterList();

						for (String s : dl) {
							if (params[0].charAt(1) == s.charAt(0)) {
								value = Path.setDrive(params[0].charAt(1)); //ustalenie litery dysku i wyswietlenie na ekran
								break;
							}
						}
						if(value == null){
							setError("Niepoprawna litera dysku!");
							value = "";
						}
					}

				}
				break;
			default:
				switch (params[0]) {

				case "..":
					if(Path.getPath().getParent() != null){
					value = Path.setPath(Path.getPath().getParent());
					}
					break;

				default:
					String complete = "";
					for( String s : params){
						complete += s + " ";
					}
					
					value = getToFolder(complete.trim());
					
					
				}

			}

		}
		if(value == null)
			setError("Ścieżka niepoprawna!");
		return value;
	}

	static Cd getInstance() {

		if (instance == null)
			instance = new Cd();

		return instance;
	}

	String getToFolder(String nameFolder) {

		String path = null;

		path = Path.addPath(nameFolder);
		if (path != null && Path.getPath().isDirectory()){
			
			return Path.getPath().toString();
			
		}else 
						
			return null;
		}

	

}
