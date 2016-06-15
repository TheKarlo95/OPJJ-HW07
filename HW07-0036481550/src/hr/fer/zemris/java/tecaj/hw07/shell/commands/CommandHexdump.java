package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.MyShell;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * {@code CommandHexdump} class represent shell command that produces
 * hex-output.
 * <p>
 * If you want to call {@code ls} command in {@link MyShell} you must type to
 * shell:
 * <ul>
 * <li>{@code hexdump [FILE]}
 * </ul>
 * <p>
 * Example output:
 * 
 * <pre>
 * 00000000: 31 2E 20 4F 62 6A 65 63|74 53 74 61 63 6B 20 69|1. ObjectStack i
 * 00000010: 6D 70 6C 65 6D 65 6E 74|61 63 69 6A 61 OD OA 32|mplementacija..2
 * 00000020: 2E 20 4D 6F 64 65 6C 2D|4C 69 73 74 65 6E 65 72|. Model-Listener
 * 00000030: 20 69 6D 70 6C 65 6D 65|6E 74 61 63 69 6A 61 OD| implementacija. 
 * 00000040: OA                     |                       |.
 * </pre>
 * 
 * On the right side of the image only a standard subset of characters is shown;
 * for all other characters a '.' is printed instead (i.e. replace all bytes
 * whose value is less than 32 or greater than 127 with '.').
 * 
 * <p>
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see ShellCommand
 */
public class CommandHexdump implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String COMMAND_NAME = "hexdump";

	/**
	 * Command description and manual.
	 */
	private static List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();

		COMMAND_DESCRIPTION.add(
				"'hexdump' command reads the contents of file and"
						+ " displays it to standard output as hex.");

		COMMAND_DESCRIPTION.add("General syntax is:  hexdump [FILE]");

		COMMAND_DESCRIPTION.add(
				"Argument FILE indicates path to file which"
						+ " user wants to read and is mandatory.");

		COMMAND_DESCRIPTION.add(
				"Firstly cardinal number of first byte is "
						+ "written, then next 16 bytes and then");

		COMMAND_DESCRIPTION.add(
				"characters. For all bytes less than 32 and"
						+ " greater than 127 '.' is printed.");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (!checkArguments(env, arguments)) {
			return ShellStatus.CONTINUE;
		}

		arguments = arguments.trim();

		byte[] bytes = null;

		try {
			bytes = Files.readAllBytes(Paths.get(arguments));
		} catch (IOException e) {

		}

		for (int i = 0; i < bytes.length; i += 16) {

			byte[] arr = null;
			if (i + 16 < bytes.length) {
				arr = Arrays.copyOfRange(bytes, i, i + 16);
			} else {
				arr = Arrays.copyOfRange(bytes, i, bytes.length);
			}

			try {
				env.writeln(format(i, arr));
			} catch (IOException ignorable) {
			}
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		return COMMAND_DESCRIPTION;
	}

	/**
	 * Checks if arguments are valid
	 * 
	 * @param env
	 *            environment of the shell
	 * @param arguments
	 *            arguments of this command
	 * @return {@code true} if arguments are valid; {code false} otherwise
	 */
	private boolean checkArguments(Environment env, String arguments) {
		if (env == null) {
			System.err.println("Environment cannot be null reference!");
			return false;
		}

		if (arguments == null) {
			try {
				env.writeln(
						"Error: wrong number of arguments for command 'hexdump'!");
			} catch (IOException ignorable) {
			}

			return false;
		}

		String[] args = arguments.trim().split("\\s+");

		if (args.length != 1) {
			try {
				env.writeln(
						"Error: wrong number of arguments for command 'hexdump'");
			} catch (IOException ignorable) {
			}

			return false;
		}

		return true;
	}

	/**
	 * Formats given line counter and bytes to specific formatted string.
	 * <p>
	 * Example of output:
	 * {@code 00000000: 31 2E 20 4F 62 6A 65 63|74 53 74 61 63 6B 20 69|1. ObjectStack i}
	 * 
	 * @param lineCount
	 *            the number which represents number of bytes currently output
	 * @param bytes
	 *            bytes that needs to be output
	 * @return formatted string
	 */
	private String format(int lineCount, byte[] bytes) {
		if (lineCount < 0) {
			throw new IllegalArgumentException(
					"Line counter must be greater or equal to zero!");
		}

		if (bytes == null) {
			throw new NullPointerException("Bytes cannot be a null reference!");
		}

		StringBuilder sb = new StringBuilder();

		sb.append(String.format("%08x|", lineCount));

		for (int i = 0; i < 16; i++) {
			if (i == 8) {
				sb.deleteCharAt(sb.length() - 1);
				sb.append("|");
			}

			if (i < bytes.length)
				sb.append(String.format("%02x ", bytes[i]));
			else
				sb.append("   ");
		}

		sb.deleteCharAt(sb.length() - 1);
		sb.append("|");

		for (byte b : bytes) {
			if (b < 31 || b > 127) {
				sb.append(".");
			} else {
				sb.append((char) b);
			}
		}

		return sb.toString();
	}

}
