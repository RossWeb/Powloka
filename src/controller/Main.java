package controller;

import model.Model;
import view.View;

public class Main {
	
	View view;
	Model model;
	Controller controller;
	
	public void init(){
		view = new View();
		model = new Model();
		
		controller = new Controller(view, model);
		
		
		
		
		
	}
	
	
	public static void main(String[] args){
		
		Main main = new Main();
		
		main.init();
		
		
	}
	
	

}
