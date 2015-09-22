package model;

/**
 * enum warunkujacy uzycie polecenia w aplikacji
 *
 */
enum CmdEnum {

	cd(Cd.getInstance()), ls(Ls.getInstance()), cat(Cat.getInstance()), beam(
			Beam.getInstance()), replace(Replace.getInstance()), remove(Remove.getInstance()), quit(null);

	private Command command;
	private final String additionalComments = "klawisz tab - lista poleceń \nlitera + klawisz tab - lista dostępnych poleceń \n"
			+ "[polecenie] + litera(ciąg znaków) + klawisz tab - pokazuje listę dostępnych elementów \n"
			+ "strzałki góra dół - historia poleceń (20 ostatnich)\n";

	private CmdEnum(Command c) {

		this.command = c;

	}

	public String use(String[] params) {
		return command.execute(params);
	}

	public Command getCommand() {
		return command;
	}

	public String getName() {
		if (command != null)
			return command.getName();
		return "quit";
	}
	public String getError(){
		return command.getError();
	}

	public String getCommandDescription(boolean full) {
		String desc = "";
		String line = "\n --------";
		if (command != null) {
			desc = command.getDescription();
			if (full){
				desc += "\n\n" + command.getAdditionalDescription() + line;
				
			}
			
		}else{
			if(full)
				desc += this.additionalComments;
			desc += "quit - wyjście z programu";
		}
		return desc;
	}

}
