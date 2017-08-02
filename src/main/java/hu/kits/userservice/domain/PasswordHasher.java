package hu.kits.userservice.domain;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {

	private static final int ITERATIONS = 100_000;
	private static final int KEY_LENGTH = 256;
	
	public static String createNewPasswordHash(String password) {

		try {
			byte[] salt = createSalt();
			byte[] passwordHash = createPasswordHash(password.toCharArray(), salt);
			String saltBase64String = new String(Base64.getEncoder().encode(salt));
			String passwordHashBase64String = new String(Base64.getEncoder().encode(passwordHash));
			return saltBase64String + ":" + passwordHashBase64String;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static boolean checkPassword(String passwordHash, String candidatePassword) {
		
		String[] parts = passwordHash.split(":");
		String saltBase64String = parts[0];
		String passwordHashBase64String = parts[1];
		
		byte[] salt = Base64.getDecoder().decode(saltBase64String.getBytes());
		try {
			byte[] candidatePasswordHash = createPasswordHash(candidatePassword.toCharArray(), salt);
			return Arrays.equals(Base64.getDecoder().decode(passwordHashBase64String.getBytes()), candidatePasswordHash);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static byte[] createPasswordHash(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
		long start = System.currentTimeMillis();
		SecretKey key = skf.generateSecret(spec);
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		byte[] res = key.getEncoded();
		return res;
	}

	private static byte[] createSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}

}
