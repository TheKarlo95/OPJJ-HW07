package hr.fer.zemris.java.tecaj.hw07.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * {@code Crypto} is a class that uses AES algorithm for encryption and
 * decryption of files and can check validity of file by checking their digest
 * using SHA-256 algorithm.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class Crypto {

	/**
	 * Default buffer size.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 4096;

	/**
	 * Keyword for checksha command.
	 */
	private static final String KEYWORD_CHECKSHA = "checksha";

	/**
	 * Keyword for decryption command.
	 */
	private static final String KEYWORD_DECRYPT = "decrypt";

	/**
	 * Keyword for encryption command.
	 */
	private static final String KEYWORD_ENCRYPT = "encrypt";

	/**
	 * Starting point of a program.
	 * 
	 * @param args
	 *            Command-line argument
	 */
	public static void main(String[] args) {
		if (args.length == 3) {
			if (args[0].equals(KEYWORD_ENCRYPT)) {
				encryptOrDecrypt(args[1], args[2], true);
			} else if (args[0].equals(KEYWORD_DECRYPT)) {
				encryptOrDecrypt(args[1], args[2], false);
			} else {
				printErrorMessageAndExit(-2);
			}
		} else if (args.length == 2) {
			if (args[0].equals(KEYWORD_CHECKSHA)) {
				checksha(args[1]);
			}
		} else {
			printErrorMessageAndExit(-1);
		}
	}

	/**
	 * Encrypts or decrypts file at {@code src} and places result at
	 * {@code dest}.
	 * 
	 * @param src
	 *            the path of source file
	 * @param dest
	 *            the path of destination file
	 * @param encrypt
	 *            if set to {@code true} this method will encrypt source file
	 *            and if set to {@code false} it will decrypt source file
	 */
	private static void encryptOrDecrypt(String src, String dest,
			boolean encrypt) {
		checkIfNullOrEmpty(src, -1);
		checkIfNullOrEmpty(dest, -1);

		System.out.println(Messages.PASSWORD_MESSAGE);

		Scanner scan = new Scanner(System.in);

		System.out.printf(">");
		String password = scan.nextLine();

		System.out.println(Messages.INITIAL_VECTOR_MESSAGE);

		System.out.printf(">");
		String initVector = scan.nextLine();
		scan.close();

		if (!password.matches("^[A-Fa-f0-9]{32}$")) {
			System.err.println(Messages.PASSWORD_FORMAT_ERROR);
			System.exit(-3);
		}

		if (!initVector.matches("^[A-Fa-f0-9]{32}$")) {
			System.err.println(Messages.INITIAL_VECTOR_FORMAT_ERROR);
			System.exit(-3);
		}

		SecretKeySpec keySpec = new SecretKeySpec(hexToByte(password), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(
				hexToByte(initVector));

		Cipher cipher = null;

		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(
					encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
					keySpec,
					paramSpec);
		} catch (GeneralSecurityException e) {
			System.err.println("Security exception: " + e.getMessage());
			System.exit(-9);
		}

		try (InputStream inStream = new BufferedInputStream(
				Files.newInputStream(Paths.get(src), StandardOpenOption.READ));
				OutputStream outStream = new BufferedOutputStream(
						Files.newOutputStream(
								Paths.get(dest),
								StandardOpenOption.CREATE,
								StandardOpenOption.WRITE))) {
			byte[] buffer = null;
			byte[] cipherBuffer = new byte[DEFAULT_BUFFER_SIZE];
			int size = 0;

			while ((size = inStream.read(cipherBuffer)) > 0) {

				buffer = cipher.update(cipherBuffer, 0, size);

				if (inStream != null) {
					outStream.write(buffer);
				}
			}

			buffer = cipher.doFinal();
			outStream.write(buffer);

		} catch (IOException e) {
			System.err.printf("IO exception: %s%n", e.getMessage());
			e.printStackTrace();
			System.exit(-7);
		} catch (IllegalBlockSizeException e) {
			System.err.printf(
					"Illegal block size exception: %s%n",
					e.getMessage());
			System.exit(-7);
		} catch (BadPaddingException e) {
			System.err.printf("Bad padding exception: %s%n", e.getMessage());
			System.exit(-7);
		}

		if (encrypt) {
			System.out.printf(
					Messages.ENCRYPTION_SUCCESS_MESSAGE + "%n",
					dest,
					src);
		} else {
			System.out.printf(
					Messages.DECRYPTION_SUCCESS_MESSAGE + "%n",
					dest,
					src);
		}
	}

	/**
	 * Asks user for digest and then proceeds to validating that digest with
	 * real digest of specified file.
	 * 
	 * @param filePath
	 *            the path of file
	 */
	private static void checksha(String filePath) {
		checkIfNullOrEmpty(filePath, -1);

		System.out.printf(Messages.CHECKSUM_MESSAGE + "%n", filePath);

		System.out.print(">");

		Scanner scan = new Scanner(System.in);
		String digestExpected = scan.nextLine();
		scan.close();

		if (!digestExpected.matches("^[A-Fa-f0-9]{64}$")) {
			System.err.println(Messages.CHECKSUM_FORMAT_ERROR);
			System.exit(-3);
		}

		MessageDigest sha256 = null;

		try {
			sha256 = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			System.err.printf("Security exception: %s%n", e.getMessage());
			System.exit(-4);
		}

		try (InputStream br = Files
				.newInputStream(Paths.get(filePath), StandardOpenOption.READ)) {
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

			while (true) {
				int r = br.read(buffer);

				if (r < 1) {
					break;
				}

				sha256.update(Arrays.copyOfRange(buffer, 0, r));
			}

		} catch (IOException e) {
			System.err.printf("IO exception: %s%n", e.getMessage());
			System.exit(-7);
		}

		String digestActual = byteToHex(sha256.digest());

		if (digestExpected.toLowerCase().equals(digestActual.toLowerCase())) {
			System.out
					.printf(Messages.CHECKSUM_SUCCESS_MESSAGE + "%n", filePath);
		} else {
			System.out.printf(
					Messages.CHECKSUM_FAIL_MESSAGE + "%n",
					filePath,
					digestActual.toLowerCase());
		}
	}

	/**
	 * CHecks if string is null. If it's null {@link IllegalArgumentException}
	 * is thrown and if it's empty program exits with specified exit status.
	 * 
	 * @param str
	 *            string to be checked
	 * @param status
	 *            exit status
	 */
	private static void checkIfNullOrEmpty(String str, int status) {
		if (str == null) {
			throw new IllegalArgumentException("File path cannot be null!");
		}

		if (str.isEmpty()) {
			System.err.println("File path cannot be empty string!");
			System.exit(status);
		}
	}

	/**
	 * Convert byte to hex.
	 * 
	 * @param bytes
	 *            array of bytes
	 * @return string containing hex values
	 */
	private static String byteToHex(byte[] bytes) {
		return DatatypeConverter.printHexBinary(bytes);
	}

	/**
	 * Convert hex to byte.
	 * 
	 * @param hex
	 *            string containing hex values
	 * @return array of bytes
	 */
	private static byte[] hexToByte(String hex) {
		return DatatypeConverter.parseHexBinary(hex);
	}

	/**
	 * Prints message informing user about invalid input and exits the program
	 * with specified exit status.
	 * 
	 * @param status
	 *            exit status
	 */
	private static void printErrorMessageAndExit(int status) {
		System.err.printf(Messages.INVALID_INPUT);

		System.exit(status);
	}
}
