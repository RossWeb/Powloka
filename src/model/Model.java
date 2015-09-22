package model;

/**
 * Najwazniejszy element wzorca projektowego MVC odpowiada za obliczenia i logike biznesowa aplikacji
 * zawiera wszelkie polecenia ujete w klasy. Klasa glowna model zarzadza wszelkimi innymi i klasy z innych
 * pakietow nie maja dostepu do innych klas pakietu poza modelem co jest zalecane. Oddzielamy w ten 
 * sposob cala logike od reszty co pozwala wygodnie podmieniac moduly
 * Ciekawostka jest system obslugi polecen tworzac klase bazowa Command otrzymalem interfejs ktory umozliwia
 * dostep do kazdego wymyslonego polecenia dzieki rozszerzeniu tejze klasy. By ostatecznie nowe polecenie
 * bylo widoczne w programie wystarczy dodac je w enumie CmdEnum 
 * Oczywiscie by umozliwic aktualizacje widoku model jest obserwatorem i posiada liste obserwujacych go widokow
 * ktora powiadamia przy zmianach
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import view.Observer;


public class Model implements Observable {

	private String textArea = ""; //pole wygladu konsoli
	private String console = ""; // tekst odpowiedzialny za wyglad konsoli
	private String command = ""; //tekst polecenia
	private static int historyCounter = 0; //licznik historii polecen
	private List<String> history = new ArrayList<String>(); //historia polecen
	private List<Observer> listObserver; // lista obserwujacych

	private boolean help = false; // czy pomoc
	private static String errorCode = null; // kody bledu

	public Model() {

		listObserver = new ArrayList<Observer>();

		this.setConsole();
		this.setTextArea("", true);
		this.notifyObservers();

	}

	private void setConsole() { // ustawienie tekstu konsoli
		this.console = Path.getPath() + "    $: ";

	}

	public void setHelp() { // ustawienie pomocy 
		this.help = true;
		notifyObservers();
	}

	public boolean isHelp() { //sprawdzenie czy pomoc wlaczona
		return help;
	}

	public String getCommand() { //pobierz polecenie
		return command;
	}

	public void useCounterHistory(int value) { //metoda uzycia i wyswietlenia historii polecen
		if (value > 0 && historyCounter < history.size() - 1) {
			historyCounter++;
			this.command = history.get(historyCounter);
		} else if (value == 0) {
			historyCounter = history.size();
			return;
		} else if (value < 0 && historyCounter > 0) {
			historyCounter--;
			this.command = history.get(historyCounter);
		}
		notifyObservers();

	}

	private void setHistory(String cmd) { //dodajemy polecenie do historii

		if (history.size() == 20)
			history.remove(0);
		history.add(cmd);
		historyCounter = history.size();

	}

	public String getTextArea() { //pobieramy tekst konsoli
		String tempTextArea = this.textArea;
		this.textArea = "";
		return tempTextArea;
	}

	private void setTextArea(String text, boolean showConsole) { //ustawiamy tekst konsoli wraz z konsola lub nie
		if (showConsole)
			this.textArea += this.console;

		this.textArea += text + "\n";
	}

	private String checkCommand(String consoleCommand) { //sprawdzamy polecenie
		String[] params = null;
		String func = null;
		String text = null;
		

		StringTokenizer st = new StringTokenizer(consoleCommand, " ");

		if (st.hasMoreTokens()) {
			params = new String[st.countTokens() - 1];
			if (st.hasMoreTokens())
				func = st.nextToken();
			int counter = 0;
			while (st.hasMoreTokens()) {

				params[counter] = st.nextToken();
				counter++;
			}
		}

		if (func != null) {
			String cmdScore = null;
			if (func.equals("quit"))
				System.exit(0);
			else {

				for (CmdEnum c : CmdEnum.values()) {

					if (func.toLowerCase().equals(c.getName())) {
						cmdScore = c.use(params);
						errorCode = c.getError();
						if (errorCode != null)
							break;
					}

					if (cmdScore != null) {
						text = cmdScore;

						break;
					} else
						errorCode = "Niepoprawne polecenie!";

				}
			}

		}

		if (errorCode != null) {
			setTextArea(errorCode, false);

		}
		return text;
	}

	public void checkText(String consoleText) { // sprawdzamy wprowadzone dane do wiersza polecen

		String mainText = null;
		this.command = ""; // zeruje console
		this.setConsole(); // ustawia prompter

		setHistory(consoleText);
		if (Beam.getInstance().isInCommand() == false)
			setTextArea(consoleText, true);

		if (Pattern.compile(".*\\s>{1,2}\\s.*").matcher(consoleText).matches() || Beam.getInstance().isInUse()) {
			String commandFirst = null;
			String commandLast = null;
			if (Beam.getInstance().isInCommand() == false) {
				Beam.getInstance().checkStream(consoleText);

				commandFirst = Beam.getInstance().getCommandFirst();
			} else

				commandFirst = Beam.getInstance().getCommandInner(consoleText);
			commandLast = Beam.getInstance().getCommandLast();
			String tempText = null;
			if (commandFirst != null) {
				
				tempText = checkCommand(commandFirst);

				if (Beam.getInstance().isInCommand() == true)

					mainText = tempText;

			}
			if (commandLast != null
					&& Beam.getInstance().isInCommand() == false) {

				Beam.getInstance().setTextToSave(tempText);
				mainText = checkCommand("> " + commandLast);

			}

		} else {
			String commandFirst = null;
			if (Beam.getInstance().isInCommand() == false) {
				Beam.getInstance().setCommandFirst(consoleText);

				commandFirst = Beam.getInstance().getCommandFirst();
			} else

				commandFirst = Beam.getInstance().getCommandInner(consoleText);
			mainText = checkCommand(commandFirst);

		}

		if (mainText != null)
			setTextArea(mainText, false);

		this.notifyObservers();

	}

	public void setTextAreaCommandDescription() { //wyswietlamy polecenia
		for (CmdEnum c : CmdEnum.values()) {
			setTextArea(c.getCommandDescription(false), false);
		}
		this.notifyObservers();
	}

	public String getHelpCommand() { //zwracamy tekst pomocy
		String text = "";
		this.help = false;
		for (CmdEnum c : CmdEnum.values()) {
			text += c.getCommandDescription(true) + "\n";
		}
		return text;
	}

	public void checkConsole(String commandConsole) { //sprawdzamy tekst konsoli przed wyslaniem do uzycia

		Pattern pattern;
		Matcher matcher;
		StringTokenizer st = new StringTokenizer(commandConsole, " ");
		String func = null;
		String params = "";

		if (st.hasMoreTokens())
			func = st.nextToken();
		while (st.hasMoreTokens())
			params += st.nextToken() + " ";

		params = params.trim();
		if (params == "") {
			List<String> checkCommand = new ArrayList<String>();
			pattern = Pattern.compile(commandConsole + ".*");
			for (CmdEnum c : CmdEnum.values()) {

				matcher = pattern.matcher(c.getName());
				if (matcher.matches()) {
					checkCommand.add(c.getName());
				}

			}

			if (checkCommand.size() > 1) {
				for (String cS : checkCommand) {
					setTextArea(cS, false);
				}
				this.command = commandConsole;
			} else if (checkCommand.size() == 1) {
				this.command = (checkCommand.get(0));
			} else
				this.command = (commandConsole);

		} else {
			List<String> passFile = new ArrayList<String>();
			Command cmdPassed = null;
			for (CmdEnum c : CmdEnum.values()) {

				if (func.equals(c.getName())) {
					cmdPassed = c.getCommand();
					break;
				}
			}
			if (cmdPassed != null) {
				
				pattern = Pattern.compile(params + ".*");

				List<String> listDirectory = Ls.getInstance().getDirectory(
						cmdPassed.getTypeDirectory());
				for (String s : listDirectory) {

					matcher = pattern.matcher(s);
					if (matcher.matches()) {
						passFile.add(s);

					}
				}
				if (passFile.size() > 1) {
					for (String sl : passFile) {
						setTextArea(sl, false);
					}
					this.command = (cmdPassed.getName() + " " + params);

				} else if (passFile.size() == 1)
					this.command = (cmdPassed.getName() + " " + passFile
							.get(0));
				else {
					this.command = (commandConsole);

				}
			}
		}
		this.notifyObservers();
	}

	@Override
	public void addObserver(Observer o) { //dodajemy obserwatora
		listObserver.add(o);
	}

	@Override
	public void removeObserver(Observer o) { //usuwamy obserwatora
		listObserver.remove(o);

	}

	@Override
	public void notifyObservers() { //powiadamiamy obserwatorow

		for (Observer o : listObserver)
			o.update(this);

	}

}
