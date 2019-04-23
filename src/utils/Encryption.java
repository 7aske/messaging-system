package utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
	private static String key = "z%C*F-JaNdRgUkXp";
	private static Key aesKey = null;

	public static String encrypt(String text) {
		if (Encryption.aesKey == null) {
			Encryption.aesKey = new SecretKeySpec(key.getBytes(), "AES");
		}
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(text.getBytes());
			return new String(Base64.getEncoder().encode(encrypted));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String decrypt(String encrypted) {
		if (Encryption.aesKey == null) {
			Encryption.aesKey = new SecretKeySpec(key.getBytes(), "AES");
		}
		try {
			Cipher cipher;
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
			return new String(decrypted);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}
}