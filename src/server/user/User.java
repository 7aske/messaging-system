package server.user;

import server.message.Message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
	private int id;
	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private String phone;
	private String company;
	private List<String> classes;
	private static final String[] fieldList = {"username", "password", "firstName", "lastName", "email", "phone", "company"};
	private final static byte[] HASH_SALT = "1106e5f6ead07f89f65a4060724ed88d124f17b8".getBytes();

	private List<Message> pendingMessages = new ArrayList<>();

	private User() {
	}

	public User(int id, String username, String password, String email, String firstName, String lastName, String phone, String company) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.company = company;
	}

	public User(String username, String password, String email, String firstName, String lastName, String phone, String company) {
		this.id = 0;
		this.username = username;
		this.password = User.getSHA1Hash(password, User.HASH_SALT);
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.company = company;
	}

	public static User fromForm(HashMap<String, String> form) {
		User user = new User();
		for (Map.Entry<String, String> kv : form.entrySet()) {
			switch (kv.getKey()) {
				case "username":
					user.username = kv.getValue();
					break;
				case "firstName":
					user.firstName = kv.getValue();
					break;
				case "lastName":
					user.lastName = kv.getValue();
					break;
				case "email":
					user.email = kv.getValue().replaceAll("%40", "@");
					break;
				case "phone":
					user.phone = kv.getValue();
					break;
				case "company":
					user.company = kv.getValue();
					break;
				case "password":
					user.password = User.getSHA1Hash(kv.getValue(), User.HASH_SALT);
					break;
			}
		}
		return user;
	}

	public static boolean isFormValid(HashMap<String, String> form){
		for (String f:User.fieldList){
			if (!form.containsKey(f)){
				return false;
			}
		}
		return true;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	public List<Message> getPendingMessages() {
		return pendingMessages;
	}

	public void setPendingMessages(List<Message> pendingMessages) {
		this.pendingMessages = pendingMessages;
	}

	public boolean checkPassword(String passToCheck) {
		String hash = User.getSHA1Hash(passToCheck, User.HASH_SALT);
		return hash.equals(this.password);
	}

	private static String getSHA1Hash(String passwordToHash, byte[] salt) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(salt);
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	@Override
	public String toString() {
		return "User{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", phone='" + phone + '\'' +
				", company='" + company + '\'' +
				'}';
	}
}
