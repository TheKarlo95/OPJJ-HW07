package hr.fer.zemris.java.tecaj.hw07.crypto;

/**
 * {@code Messages} class is a class that contains string constants used as
 * messages in {@link Crypto} class.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class Messages {

	/**
	 * Invalid input message.
	 */
	public static final String INVALID_INPUT = "You need to give arguments:%n"
			+ "\t\t- checksha <filepath>%n"
			+ "\t\t- encrypt <filepath> <encrypted_filepath>%n"
			+ "\t\t- decrypt <encrypted_filepath> <filepath>%n";

	// encryption/decryption messages

	/**
	 * Password message.
	 */
	public static final String PASSWORD_MESSAGE = "Please provide password as "
			+ "hex-encoded text (16 bytes, i.e. 32 hex-digits):";
	
	/**
	 * Password format error message.
	 */
	public static final String PASSWORD_FORMAT_ERROR = "Given password is not of"
			+ " right format!";

	/**
	 * Initial vector message.
	 */
	public static final String INITIAL_VECTOR_MESSAGE = "Please provide "
			+ "initialization vector as hex-encoded text (32 hex-digits):";
	
	/**
	 * Initial vector format error message.
	 */
	public static final String INITIAL_VECTOR_FORMAT_ERROR = "Given initial "
			+ "vector is not of right format!";

	/**
	 * Encryption success message.
	 */
	public static final String ENCRYPTION_SUCCESS_MESSAGE = "Encryption "
			+ "completed. Generated file %s based on file %s.";
	
	/**
	 * Decryption success message.
	 */
	public static final String DECRYPTION_SUCCESS_MESSAGE = "Decryption "
			+ "completed. Generated file %s based on file %s.";

	// checksum messages

	/**
	 * Checksum message
	 */
	public static final String CHECKSUM_MESSAGE = "Please provide expected "
			+ "sha-256 digest for %s:";
	
	/**
	 * Checksum format error message
	 */
	public static final String CHECKSUM_FORMAT_ERROR = "Digestion error. Given "
			+ "digest is not of right format!";
	
	/**
	 * Checksum success message
	 */
	public static final String CHECKSUM_SUCCESS_MESSAGE = "Digesting completed."
			+ " Digest of %s matches expected digest.";
	
	/**
	 * Checksum fail message
	 */
	public static final String CHECKSUM_FAIL_MESSAGE = "Digesting completed. "
			+ "Digest of %s does not match the expected digest. Digest was: %s";
}