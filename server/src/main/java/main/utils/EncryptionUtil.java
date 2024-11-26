package main.utils;

import main.models.dto.Pair;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class EncryptionUtil {
	public static <T> Pair<String, String> encrypt(T data) {
		Double seed = new Random().nextDouble(7);
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(seed.toString());

		String encryptedData = encryptor.encrypt(data.toString());
		return new Pair<>(encryptedData, seed.toString());
	}

	public static String decrypt(String data, String seed) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(seed);
		return encryptor.decrypt(data);
	}

	// cipher method
	public static String hashData(String message) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Exception in Hash.hashData\nCannot getInstance of SHA-256");
			e.printStackTrace();
		}
		byte[] resultByteArray = messageDigest.digest(message.getBytes());
		return (new BigInteger(1, resultByteArray)).toString(16);
	}

	public static int[] convertByteArrayToIntArray(byte[] byteArray) {
		int[] intArray = new int[byteArray.length];
		for (int i = 0; i < byteArray.length; i++) {
			intArray[i] = byteArray[i] & 0xFF;
		}
		return intArray;
	}
}
