package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.Model;
import view.View;

/**
 * Klasa kontrolera we wzorcu MVC, ma dostep do obiektow modelu i widoku.
 * Z widoku pobiera dane by przekazac je do modelu.
 *
 */
public class Controller implements KeyListener, ActionListener{

	View view;
	Model model;
	public Controller(View v, Model m){
		this.view = v;
		this.model = m;
		model.addObserver(view);
		view.setListener(this);
		model.notifyObservers();
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) { //obsluga klawiszy
		
		if(e.getKeyCode() == 10){ //enter
			
			this.model.checkText(view.getTextPanelCommand().trim());
			
		
		}else if(e.getKeyCode() == 9){ //tabulator
			
			if(!view.getTextPanelCommand().isEmpty()){
				this.model.checkConsole(view.getTextPanelCommand());
				
			}else
				this.model.setTextAreaCommandDescription();
		
		}else if(e.getKeyCode() == 38){// strzalka w gore
			
			this.model.useCounterHistory(1);
		}else if(e.getKeyCode() == 40){// strzalka w dol
			this.model.useCounterHistory(-1);
		}else
			this.model.useCounterHistory(0);
		
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == view.getHelp()) // klikniecie w przycisk pomocy
			model.setHelp();
		
	}

}
