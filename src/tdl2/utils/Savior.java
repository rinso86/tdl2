package tdl2.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import tdl2.model.Task;

public class Savior {

	public void saveTree(Task baseNode, String filename) throws IOException {
		saveToFile(baseNode, filename);
	}
	
	public Task loadTree(String filename) throws ClassNotFoundException, IOException {
		return loadFromFile(filename);
	}

	private Task loadFromFile(String filename) throws IOException, ClassNotFoundException {
		InputStream file = new FileInputStream(filename);
		InputStream buffer = new BufferedInputStream(file);
		ObjectInput input = new ObjectInputStream (buffer);
		Task tree = (Task)input.readObject();
		input.close();
		return tree;
	}

	private void saveToFile(Task baseNode, String filename) throws IOException {
		OutputStream file = new FileOutputStream(filename);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);
		output.writeObject(baseNode);
		output.close();
	}
}
