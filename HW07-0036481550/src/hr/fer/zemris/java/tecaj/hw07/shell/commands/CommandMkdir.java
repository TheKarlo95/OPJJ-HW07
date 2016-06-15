package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
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
 * {@code CommandMkdir} class represent shell command that creates the
 * appropriate directory structure.
 * <p>
 * If you want to call {@code mkdir} command in {@link MyShell} you must type to
 * shell:
 * <ul>
 * <li>{@code mkdir [DIRECTORY]}
 * </ul>
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see ShellCommand
 */
public class CommandMkdir implements ShellCommand {

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

		COMMAND_DESCRIPTION.add("'mkdir' command makes new directories");

		COMMAND_DESCRIPTION.add("General syntax is:  mkdir [DIRECTORY]");

		COMMAND_DESCRIPTION.add(
				"Argument DIRECTORY indicates path to directory which"
						+ " user wants to make and is mandatory.");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (!checkArguments(env, arguments)) {
			return ShellStatus.CONTINUE;
		}

		String[] args = arguments.trim().split("\\s+");
		Path path = Paths.get(Paths.get(args[0]).toFile().getAbsolutePath());

		if (!isDirectoryPath(path)) {
			try {
				env.writeln(
						"Error: you muss pass an argument indicating path to directory!");
			} catch (IOException ignorable) {
			}
			
			return ShellStatus.CONTINUE;
		}

		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			try {
				env.writeln("Error: some I/O error occured!");
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
						"Error: wrong number of arguments for command 'mkdir'!");
			} catch (IOException ignorable) {
			}

			return false;
		}

		String[] args = arguments.trim().split("\\s+");

		if (args.length != 1) {
			try {
				env.writeln(
						"Error: wrong number of arguments for command 'mkdir'");
			} catch (IOException ignorable) {
			}

			return false;
		}

		return true;
	}
	
	/**
	 * Checks is given path a path to directory. This method will work the same
	 * if path exists or doesn't.
	 * 
	 * @param path
	 *            the path
	 * @return {@code true} if path is a directory path; {@code false} otherwise
	 */
	private boolean isDirectoryPath(Path path) {
	    if (!Files.exists(path)) {
	        return path.getFileName().toString().lastIndexOf('.') == -1;
	    } else {
	        return Files.isDirectory(path);
	    }
	}
}
