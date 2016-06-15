package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.MyShell;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * {@code CommandCat} class represent shell command that opens given file and
 * writes its content to standard output.
 * <p>
 * If you want to call {@code cat} command in {@link MyShell} you must type to
 * shell:
 * <ul>
 * <li>{@code cat [FILE] [CHARSET]}
 * </ul>
 * <br>
 * Note that [CHARSET] argument is optional. If not provided, a default platform
 * charset should be used;
 * <p>
 * Example output:
 * <p>
 * This is my text file.<br>
 * I write my plans here.<br>
 * - I plan to finish MyShell program<br>
 * <p>
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see ShellCommand
 */
public class CommandCat implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String COMMAND_NAME = "cat";

	/**
	 * Command description and manual.
	 */
	private static List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();

		COMMAND_DESCRIPTION.add(
				"'cat' command reads the contents of file and"
						+ " displays it to standard output.");

		COMMAND_DESCRIPTION.add("General syntax is:  cat [FILE] [CHARSET]");

		COMMAND_DESCRIPTION.add(
				"Argument FILE indicates path to file which"
						+ " user wants to read and is mandatory.");

		COMMAND_DESCRIPTION.add(
				"Second argument CHARSET tells the command"
						+ " which charset to use to decode content");

		COMMAND_DESCRIPTION.add(
				"of the file. If not specified command will use"
						+ " default charset.");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (!checkArguments(env, arguments)) {
			return ShellStatus.CONTINUE;
		}

		String[] args = arguments.trim().split("\\s+");

		Charset cs = selectCharset(args);

		Path path = Paths.get(args[0]);

		try {
			String str = new String(Files.readAllBytes(path), cs);

			env.write(str);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Error: I/O error occured while opening the file!");
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
						"Error: wrong number of arguments for command 'mkdir'!");
			} catch (IOException ignorable) {
			}

			return false;
		}

		String[] args = arguments.trim().split("\\s+");

		if (args.length != 1 || args.length != 2) {
			try {
				env.writeln(
						"Error: wrong number of arguments for command 'mkdir'!");
			} catch (IOException ignorable) {
			}

			return false;
		}

		return true;
	}

	/**
	 * Returns the {@link Charset} specified in the string array at the index 1
	 * if length is 2 or returns default {@code Charset} if length or array is
	 * 1.
	 * 
	 * @param args
	 *            arguments of the 'cat' command
	 * @return the {@link Charset} specified in the string array at the index 1
	 *         if length is 2 or returns default {@code Charset} if length or
	 *         array is 1.
	 * @throws IllegalArgumentException if args array length isn't 1 or 2
	 */
	private static Charset selectCharset(String[] args) {
		if (args.length == 2) {
			return Charset.forName(args[1]);
		} else if (args.length == 1) {
			return Charset.defaultCharset();
		} else {
			throw new IllegalArgumentException(
					"Error: wrong number of arguments for command 'cat'");
		}
	}
}
