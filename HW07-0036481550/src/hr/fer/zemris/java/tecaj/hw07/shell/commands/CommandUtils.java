package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * {@code COmmandUtils} is a helper class for various uses in implementation of
 * {@link ShellCommand}.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class CommandUtils {

	/**
	 * Checks if arguments of commands 'ls' are valid.
	 * 
	 * @param env
	 *            environment of the shell
	 * @param arguments
	 *            arguments of this command
	 * @return {@code true} if arguments are valid; {code false} otherwise
	 */
	public static boolean checkArgumentsLs(Environment env, String arguments) {
		return checkArgumentsLsTree(env, arguments, "ls");
	}

	/**
	 * Checks if arguments of commands 'tree' are valid.
	 * 
	 * @param env
	 *            environment of the shell
	 * @param arguments
	 *            arguments of this command
	 * @return {@code true} if arguments are valid; {code false} otherwise
	 */
	public static boolean checkArgumentsTree(Environment env,
			String arguments) {
		return checkArgumentsLsTree(env, arguments, "tree");
	}

	/**
	 * Checks if arguments of commands 'cat' are valid.
	 * 
	 * @param env
	 *            environment of the shell
	 * @param arguments
	 *            arguments of this command
	 * @return {@code true} if arguments are valid; {code false} otherwise
	 */
	public static boolean checkArgumentsCat(Environment env, String arguments) {
		return checkArgumentsCatMkdir(env, arguments, "cat");
	}

	/**
	 * Checks if arguments of commands 'mkdir' are valid.
	 * 
	 * @param env
	 *            environment of the shell
	 * @param arguments
	 *            arguments of this command
	 * @return {@code true} if arguments are valid; {code false} otherwise
	 */
	public static boolean checkArgumentsMkdir(Environment env,
			String arguments) {
		return checkArgumentsCatMkdir(env, arguments, "cat");
	}

	/**
	 * Checks if arguments of commands 'ls' and 'tree' are valid.
	 * 
	 * @param env
	 *            environment of the shell
	 * @param arguments
	 *            arguments of this command
	 * @param cmd
	 *            name of the command
	 * @return {@code true} if arguments are valid; {code false} otherwise
	 */
	private static boolean checkArgumentsLsTree(Environment env,
			String arguments, String cmd) {
		if (env == null) {
			System.err.println("Environment cannot be null reference!");
			return false;
		}

		if (arguments == null) {
			arguments = "./";
		}

		String[] args = arguments.trim().split("\\s+");

		if (args.length != 1) {
			try {
				env.writeln(
						String.format(
								"Error: wrong number of arguments for"
										+ " command '%s'!",
								cmd));
			} catch (IOException ignorable) {
			}

			return false;
		}

		Path path = Paths.get(args[0]);

		if (!Files.exists(path)) {
			try {
				env.writeln("Error: path doesn't exist!");
			} catch (IOException ignorable) {
			}

			return true;
		}

		if (!Files.isDirectory(path)) {
			try {
				env.writeln("Error: path isn't directory!");
			} catch (IOException ignorable) {
			}
		}

		return true;
	}

	/**
	 * Checks if arguments are valid
	 * 
	 * @param env
	 *            environment of the shell
	 * @param arguments
	 *            arguments of this command
	 * @param cmd
	 *            name of the command
	 * @return {@code true} if arguments are valid; {code false} otherwise
	 */
	private static boolean checkArgumentsCatMkdir(Environment env,
			String arguments, String cmd) {
		if (env == null) {
			System.err.println("Environment cannot be null reference!");
			return false;
		}

		if (arguments == null) {
			try {
				env.writeln(
						String.format(
								"Error: wrong number of arguments for"
										+ " command '%s'!",
								cmd));
			} catch (IOException ignorable) {
			}

			return false;
		}

		String[] args = arguments.trim().split("\\s+");

		if (args.length != 1 || args.length != 2) {
			try {
				env.writeln(
						String.format(
								"Error: wrong number of arguments for"
										+ " command '%s'!",
								cmd));
			} catch (IOException ignorable) {
			}

			return false;
		}

		return true;
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
	public static boolean checkArgumentsSymbol(Environment env, String arguments) {
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

		if (args.length != 1 && args.length != 2) {
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
	 * Checks if arguments of command 'charsets' are valid
	 * 
	 * @param env
	 *            environment of the shell
	 * @param arguments
	 *            arguments of this command
	 * @return {@code true} if arguments are valid; {code false} otherwise
	 */
	public static boolean checkArgumentsCharsets(Environment env, String arguments) {
		if (env == null) {
			System.err.println("Environment cannot be null reference!");
			return false;
		}

		if (arguments != null) {
			try {
				env.writeln(
						"Error: wrong number of arguments for command 'charset'!");
			} catch (IOException ignorable) {
			}

			return false;
		}

		return true;
	}
	
}
