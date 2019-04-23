package server.user;

import server.database.DBController;
import utils.Encryption;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;

public class UserUtils {
	public static boolean verifyLoginForm(HashMap<String, String> form) {
		if (form.containsKey("username") && form.containsKey("password")) {
			User user = DBController.getUser(form.get("username"));
			if (user != null) {
				return user.checkPassword(form.get("password"));
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static String generateToken(User user) {
		long unixTime = Instant.now().getEpochSecond();
		String token = String.format("expiresAt=%d&issuer=server&user=%s", unixTime + 86400, user.getUsername());
		return Encryption.encrypt(token);
	}

	public static boolean validateToken(User user, String token) {
		return validateToken(user.getUsername(), token);
	}

	public static boolean validateToken(String user, String token) {
		if (DBController.getUser(user) == null) {
			return false;
		}
		long unixTime = Instant.now().getEpochSecond();
		String dToken = Encryption.decrypt(token);
		if (dToken != null) {
			String[] fields = dToken.split("&");
			for (String f : fields) {
				String[] parts = f.split("=");
				switch (parts[0]) {
					case "expiresAt":
						if (Integer.parseInt(parts[1]) - unixTime <= 0) {
							return false;
						}
						break;
					case "issuer":
						if (!parts[1].equals("server"))
							return false;
						break;
					case "user":
						if (!user.equals(parts[1]))
							return false;
						break;
					default:
						return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public static User fromResponseString(String response) {
		String[] pairs = response.split("\n");
		HashMap<String, String> form = new HashMap<>();
		for (String pair : pairs) {
			String[] kv = pair.split("=");
			if (kv.length == 2) {
				form.put(kv[0], kv[1]);
			}
		}
		return User.fromForm(form);
	}
}
