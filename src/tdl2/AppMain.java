package tdl2;

import java.io.IOException;

import tdl2.controller.Controller;

public class AppMain {

	public static void main(String[] args) {
		try {
			Controller c = new Controller();
			c.run();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}
