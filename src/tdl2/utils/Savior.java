package tdl2.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import tdl2.model.Task;

public class Savior {

	public void saveTree(Task baseNode, String filename) throws IOException {
		saveToFile(baseNode, filename);
	}
	
	public Task loadTree(String filename) throws ClassNotFoundException, IOException {
		return loadFromFile(filename);
	}

	private Task loadFromFile(String filename) throws IOException, ClassNotFoundException {
		Task tree = null;
		InputStream file;
		try {
			file = new FileInputStream(filename);
		}catch(FileNotFoundException e) {
			System.out.println(filename + " not found. Creating a new empty task-tree.");
			Task baseTask = new Task("Base task");
			saveToFile(baseTask, filename);
			file = new FileInputStream(filename);
		}
		InputStream buffer = new BufferedInputStream(file);
		ObjectInput input = new ObjectInputStream(buffer);
		tree = (Task) input.readObject();
		input.close();
		checkIntegrity(tree);
		return tree;
	}

	private void saveToFile(Task baseNode, String filename) throws IOException {
		OutputStream file = new FileOutputStream(filename);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);
		output.writeObject(baseNode);
		output.close();
	}

	private void checkIntegrity(Task tree) {
		if(tree.getAttachments() == null) {
			tree.setAttachments(new ArrayList<File>());
		}
		for(Task child : tree.getChildren()) {
			checkIntegrity(child);
		}
	}
}
