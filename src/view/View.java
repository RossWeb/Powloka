package view;

/**
 * Klasa widoku odpowiedzialna za wyglad aplikacji oraz dynamiczne ustawianie tekstu,
 * posiada podpiety sluchacz zdarzen dzieki czemu mozliwe jest wykonywanie dodatkowych operacji
 * takich jak podglad pomocy, polecen czy akcji w trakcie pisania
 * Dzieku uzyciu wzorca projektowego MVC oraz observer klasa widoku samodzielnie ustawia 
 * wartosci poszczegolnych pol na podstawie danych z obserwatora (tutaj modelu) co
 * pozwala bardziej uszczelnic zmiany zachodzace pomiedzy klasami jak w samej klasie (widok tylko przekazuje swoje wartosci do kontrolera)
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.EventListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.Model;
import model.Observable;

public class View extends JPanel implements Observer {

	
	private static final long serialVersionUID = 1L;
	private JFrame windowMain;
	private JTextArea textArea;
	private JTextField panelCommand;
	private final int WIDTH = 600;
	private final int HEIGHT = 600;
	private JButton help;
	private Model model;
	private final String welcome = "Witaj w programie \"Powłoka\" wersja 1.5\n"
			+ "Program pozwala poruszać się po danych na dyskach, "
			+ "wyświetlać i zapisywać dane do pliku za pomocą ustalonych komend,"
			+ " filtrować treści, zmieniać tekst w pliku, usuwać oraz zmieniać nazwę plików lub katalogów "
			+ "jak również łączyć polecenia w strumienie. Strzałki umożliwiają przegląd ostatnich 20 poleceń, "
			+ "tabulator umożliwia wykonywanie akcji w poleceniach (pogląd plików lub katalogów)\n"
			+ "Lista możliwych poleceń - klawisz TAB\nPomoc - przycisk \"?\"\n"
			+ "Autor : Konrad Marciniak 3470 Informatyka semetr III gr. 14/1\n "
			+ "\n\n";

	public View() { // wszystko w jednym watku GUI SWING
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				createGUI();
				
			}
		});
		
		
	}
	private void createGUI(){ //tworzymy okno aplikacji
		setLayout(new BorderLayout());
		textArea = new JTextArea(welcome);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		panelCommand = new JTextField();
		panelCommand.setPreferredSize(new Dimension(new Double(
				this.WIDTH * 0.93).intValue(), new Double(this.HEIGHT * 0.05)
				.intValue()));
		help = new JButton("?");
		help.setPreferredSize(new Dimension(new Double(this.WIDTH * 0.07)
				.intValue(), new Double(this.HEIGHT * 0.05).intValue()));
		JScrollPane scrollPane = new JScrollPane(getBaseTextArea());

		scrollPane.setPreferredSize(new Dimension(this.WIDTH, new Double(
				this.HEIGHT * 0.95).intValue()));
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.NORTH);
		JPanel bottom = new JPanel();

		bottom.add(panelCommand);
		bottom.add(help);
		add(bottom, BorderLayout.SOUTH);
		textArea.setVisible(true);
		panelCommand.setVisible(true);
		panelCommand.setFocusTraversalKeysEnabled(false);
		windowMain = new JFrame();
		windowMain.setTitle("Powłoka");
		windowMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windowMain.setResizable(false);
		windowMain.add(this);
		windowMain.pack();
		windowMain.setVisible(true);
		panelCommand.requestFocus();

	}
	private void refresh() { //odswiezanie okna aplikacji
		revalidate();
		repaint();
		panelCommand.setText("");
		panelCommand.requestFocus();
	}

	public JButton getHelp() { //zwraca przycisk
		return help;
	}

	public void setListener(final EventListener e) {//ustawia sluchacza zdarzen
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				View.this.panelCommand.addKeyListener((KeyListener) e);
				View.this.help.addActionListener((ActionListener) e);

				
			}
		});
		
	}

	public String getTextPanelCommand() { //zwraca tekst z panela polecen
		return panelCommand.getText();
	}

	private JPanel getBaseTextArea() { // ustawia ekran konsoli
		JPanel panelShow = new JPanel();
		panelShow.setLayout(new BorderLayout());
		panelShow.setBackground(textArea.getBackground());

		panelShow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
		textArea.setBorder(null);

		panelShow.add(textArea, BorderLayout.SOUTH);

		return panelShow;
	}

	private void showMessageHelp(String text) { // wyswietla pomoc MODALNIE!

		JTextArea tArea = new JTextArea();
		tArea.setLineWrap(true);
		tArea.setWrapStyleWord(true);
		tArea.setText(text);
		tArea.setEditable(false);
		tArea.setCaretPosition(0);
		
		JScrollPane scroll = new JScrollPane(tArea);
		scroll.setPreferredSize(new Dimension(300,350));

		JOptionPane.showMessageDialog(this, scroll, "Pomoc", JOptionPane.INFORMATION_MESSAGE);
		
	}
	private void setText(){// ustawia tekst konsoli
		
		textArea.append(model.getTextArea());
	}
	@Override
	public void update(final Observable o) { //metoda wzorca observer sluzy aktualizacji danych widoku
		
		SwingUtilities.invokeLater(new Runnable() {
			boolean checkRefresh = true;	
			@Override
			public void run() {
				View.this.panelCommand.requestFocus();
				if (o instanceof Model) {
					model = (Model) o;
					setText();
					if (!model.getCommand().isEmpty()) {
						checkRefresh = false;
						View.this.panelCommand.setText(model.getCommand());

					}
					if (model.isHelp() == true)
						View.this.showMessageHelp(model.getHelpCommand());
				}

				View.this.textArea.setCaretPosition(View.this.textArea.getDocument().getLength());
				if (checkRefresh) {
					View.this.refresh();

				}

			}
		});

	}

}
