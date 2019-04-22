package server.database;

import server.user.User;

import java.sql.*;

public class DBController {

	private static final String DB_URL = "jdbc:sqlite:" + System.getProperty("user.dir") + "/db/database.db";

	public static void initDatabase() {
		String sql = "CREATE TABLE IF NOT EXISTS users (\n"
				+ "	userid      integer PRIMARY KEY AUTOINCREMENT,\n"
				+ "	username    CHARACTER(20) NOT NULL UNIQUE ,\n"
				+ "	password    CHARACTER(20) NOT NULL,\n"
				+ "	email       CHARACTER(20) NOT NULL UNIQUE ,\n"
				+ "	firstName   CHARACTER(20) NOT NULL,\n"
				+ "	lastName    CHARACTER(20) NOT NULL,\n"
				+ "	phone       CHARACTER(20) NOT NULL,\n"
				+ "	company     CHARACTER(20) NOT NULL\n"
				+ ");";

		try (Connection conn = DriverManager.getConnection(DBController.DB_URL);
		     Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static User getUser(String query) {
		String sql = "SELECT * FROM users WHERE username = ?";
		try (Connection conn = DriverManager.getConnection(DBController.DB_URL);
		     PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, query);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
//				for (int i = 1; i <= 8; i++) {
//					System.out.printf("%s %s\n", rs.getMetaData().getColumnLabel(i), rs.getString(i));
//				}
				int id = rs.getInt(1);
				String username = rs.getString(2);
				String password = rs.getString(3);
				String email = rs.getString(4);
				String firstName = rs.getString(5);
				String lastName = rs.getString(6);
				String phone = rs.getString(7);
				String company = rs.getString(8);
				return new User(id, username, password, email, firstName, lastName, phone, company);
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void addUser(User user) {
		String sql = "INSERT INTO users (username, password, email, firstname, lastname, phone, company) \n" +
				"VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(DBController.DB_URL);
		     PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getEmail());
			statement.setString(4, user.getFirstName());
			statement.setString(5, user.getLastName());
			statement.setString(6, user.getPhone());
			statement.setString(7, user.getCompany());
			statement.execute();
		} catch (SQLException e) {
			switch (e.getErrorCode()) {
				case 19:
					System.out.println(e.getMessage());
					break;
				default:
					System.out.println(e.getErrorCode());
					e.printStackTrace();

			}
		}
	}
}
