package model;

/**
 *Klasa bedaca podstawa dla nowych polecen. 
 *
 */
abstract class Command {
	
 
	private String description = "Opis polecenia";
	private String additionalDescription = "Szczegółowy opis polecenia";
	private String name = "nazwa polecenia";
	private char typeDirectory = ' ';
	private String error = null;
	Command(){
		
	}
	
	protected void setText(String name, String description, String addDescription, char type){
		this.description = description;
		this.additionalDescription = addDescription;
		this.name = name;
		this.typeDirectory = type;
	}
	
	public char getTypeDirectory() {
		return typeDirectory;
	}

	abstract String execute(String[] params);
	
	public String getAdditionalDescription() {
		return additionalDescription;
	}

	String getDescription(){
		return description;
	}
	String getName(){
		return name;
	}

	String getError() {
		String outError = this.error;
		this.error = null;
		return outError;
	}

	void setError(String error) {
		this.error = error;
	}
	
}
