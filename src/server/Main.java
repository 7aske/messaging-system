package server;

import server.database.DBController;
import server.user.User;

import java.io.*;

public class Main {
	public static void main(String[] args) {
		User u0 = new User("7aske", "password123",  "ntasic@gmail.com", "Nikola", "Tasic", "0038100554433", "Metropoliten");
		DBController.initDatabase();
		DBController.addUser(u0);
		User u1 = DBController.getUser("7aske");
		assert u1 != null;
		System.out.println(u1.getPassword());
		System.out.println(u1.checkPassword("password123"));
		try {
			Server.start(8000);
		} catch (
				IOException e) {
			e.printStackTrace();
		}
	}
}
