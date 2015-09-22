package view;

import model.Observable;

public interface Observer {

	public void update(Observable o);
}
