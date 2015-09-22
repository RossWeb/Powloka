package model;

import java.io.File;

import javax.swing.filechooser.FileSystemView;



/**
 * Klasa odpowiedzialna za strukture systemu plikow posiada metody ustawiajace oraz zmieniajace dany 
 * dysk, folder, plik badz cala sciezke
 *
 */
final class Path {

	private static String drive = File.listRoots()[0].toString();
	private static File path = new File(drive);

	static File getPath() {
		if (path.exists())
			return path;
		else
			return null;

	}

	static String[] getDriveLetterList() {
		String[] driveLetterList = new String[File.listRoots().length];
		int i = 0;
		for (File f : File.listRoots()) {
			driveLetterList[i] = f.toString().substring(0, 1);
			i++;
		}
		return driveLetterList;
	}

	static String setDrive(char letter) {
		String error = "";
		String tempDrive = drive;
		String newDrive = letter + drive.substring(1, drive.length());

		drive = newDrive;
		clearPath();

		if (!path.exists()) {
			drive = tempDrive;
			clearPath();
			error = "Urządzenie nie gotowe!";
		} else
			error = path.toString();
		return error;
	}

	static String getRootList() {

		String rootList = "";
		FileSystemView fsv = FileSystemView.getFileSystemView();
		for (File f : File.listRoots())
			rootList += "Drive : " + f.toString().substring(0, 1) + " "
					+ fsv.getSystemTypeDescription(f) + "\n";

		return rootList;
	}

	static String setPath(String newPath) {
		File tempPath = null;
		if (newPath != null)
			tempPath = new File(newPath);
		if (tempPath.exists()) {
			path = tempPath;
			return path.toString();
		} else
			return null;
	}

	static String addPath(String addPath) {
		File tempPath = null;
		if (addPath != null){
			if (addPath.length() > 3 && addPath.substring(1, 2).contains(":"))
				return setPath(addPath);
			else {
				tempPath = new File(path.toString() + addPath);
				if (tempPath.exists()) {
					path = tempPath;
					return path.toString();
				} else
					return null;

			}
		}
		return null;
	}

	static String clearPath() {
		path = new File(drive);
		return path.toString();
	}

}
