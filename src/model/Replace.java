package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Replace extends Command {

	private static Replace instance;
	private String description = "replace - zmiana nazwy pliku";
	private String additionalDescription = "replace [nazwa pliku] [nowa nazwa] - zmienia nazwę pliku";
	private String name = "replace";
	private final char typeDirectory = 'f';

	Replace() {
		super.setText(name, description, additionalDescription, typeDirectory);
	}

	@Override
	String execute(String[] params) {
		String output = null;

		if (params.length == 2) {

			try {
				output = renameFile(params[0], params[1]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return output;
	}

	static Replace getInstance() {

		if (instance == null)
			instance = new Replace();
		return instance;
	}

	private String renameFile(String in, String out) throws IOException{
		File fileIn = new File(Path.getPath().toString() + "/" + in);
		File fileOut = new File(Path.getPath().toString() + "/" + out);
		
		if(!fileIn.exists()){
			setError("Plik nie istnieje!");
			return null;
		}else{
			if(!fileIn.isFile()){
				setError("Podana nazwa nie dotyczy pliku!");
				return null;
			}
			if(fileOut.exists()){
				setError("Nazwa pliku już istnieje!");
				return null;
			}
		
		BufferedInputStream bIn = new BufferedInputStream(new FileInputStream(fileIn));
		BufferedOutputStream bOut = new BufferedOutputStream(new FileOutputStream(fileOut));
		
		int numByte = bIn.available();
		int byteBuf;
		while(numByte != 0){
			byteBuf =  bIn.read();
			bOut.write(byteBuf);
			numByte--;
		}
		
			
		bIn.close();
		bOut.close();
		fileIn.delete();
		
		return "Wykonano";
	
		}
	}
}
