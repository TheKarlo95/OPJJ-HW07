package hr.fer.zemris.java.tecaj.hw07.shell;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import hr.fer.zemris.java.tecaj.hw07.shell.commands.CompositeCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.ShellEnvironment;

/**
 * {@code MyShell} is class with entry point to this program.
 * <p>
 * {@code MyShell} program accepts user commands and executes them. Currently
 * supported commands are:
 * <ul>
 * <li>{@code cat [FILE] [CHARSET]}
 * <li>{@code charsets}
 * <li>{@code copy [SRC] [DEST]}
 * <li>{@code hexdump [FILE]}
 * <li>{@code ls [DIRECTORY]}
 * <li>{@code mkdir [DIRECTORY]}
 * <li>{@code tree [DIRECTORY]}
 * <li>{@code symbol PROMPT [CHAR]}
 * <li>{@code symbol MORELINES[CHAR]}
 * </ul>
 * <br>
 * For more information about using these commands look up their documentation.
 * <p>
 * This programs support multiple shells. If you want to change shell, either
 * existing one or new one, type "{@code symbol PROMPT [CHAR]}". Default
 * character indicating prompt is {@value ShellEnvironment#DEFAULT_PROMPT} but
 * can be changed with command "{@code symbol PROMPT [CHAR]}" . If [CHAR]
 * argument is left out shell will display current symbol for {@code PROMPT}.
 * <p>
 * {@code MyShell} can accept user input two modes: {@code ONELINE} and
 * {@code MORELINES} command input is supported. Default character indicating
 * that command is going to be written in {@code MORELINES} mode is
 * {@value ShellEnvironment#DEFAULT_MORELINE} but can be changed with command
 * "{@code symbol MORELINES [CHAR]}". If [CHAR] argument is left out shell will
 * display current symbol for {@code MORELINES}.
 * <p>
 * When in {@code MORELINES} mode lies after first doesn't start with
 * {@code PROMPT} symbol but with {@code MULTILINE} symbol. Default character
 * indicating that shell is in {@code MORELINES} mode is
 * {@value ShellEnvironment#DEFAULT_MULTILINE} but can be changed with command
 * "{@code symbol MULTILINES [CHAR]}".
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class MyShell {

	/**
	 * Current environment used by this shell.
	 */
	private static Environment env;

	/**
	 * Map of all environments
	 */
	private static Map<Character, Environment> environments;

	/**
	 * The status of the shell.
	 */
	private static ShellStatus shellStatus;

	static {
		shellStatus = ShellStatus.CONTINUE;

		env = new ShellEnvironment();

		environments = new HashMap<>(4);
		environments.put(env.getPromptSymbol(), env);
	}

	/**
	 * Starting point of a program.
	 * 
	 * @param args
	 *            Command-line argument
	 */
	public static void main(String[] args) {
		try {
			env.writeln("Welcome to MyShell v 1.0");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		do {
			try {
				env.write(env.getPromptSymbol() + " ");
				String input = handleInput();

				shellStatus = executeCommand(input);
			} catch (IOException e) {
				try {
					env.writeln("Error: input/output exception occurred!");
				} catch (IOException ignorable) {
				}
			} catch (RuntimeException e) {
			}
		} while (shellStatus.equals(ShellStatus.CONTINUE));
	}

	/**
	 * Handles all the input from user.
	 * 
	 * @return string which user inputed to shell
	 * @throws IOException
	 *             if i/o exception occurred
	 */
	private static String handleInput() throws IOException {
		StringBuilder sb = new StringBuilder();

		sb.append(env.readLine());

		while (sb.toString().trim().endsWith(
				env.getMorelinesSymbol().toString())) {
			env.writeln(env.getMultilineSymbol() + " ");
			sb.append(env.readLine());
		}

		return sb.toString().trim().replace(
				env.getMorelinesSymbol().toString(),
				"");
	}

	/**
	 * Executes all the command.
	 * 
	 * @param cmd
	 *            command name and arguments
	 * @return status of the shell after command has been executed
	 * @throws IOException
	 *             if i/o exception occurred
	 */
	private static ShellStatus executeCommand(String cmd) throws IOException {
		cmd = cmd.trim();

		if (cmd.isEmpty()) {
			return ShellStatus.CONTINUE;
		}

		if (cmd.matches("^symbol\\s+PROMPT\\s*.?$")) {
			return handleSymbolPrompt(cmd);
		} else {
			String[] args = cmd.split("\\s+", 2);

			ShellCommand command = null;
			try {
				command = CompositeCommand.get(args[0]);
			} catch (IllegalArgumentException e) {
				env.writeln(
						String.format(
								"Error: '%s' is not recognized as a command!",
								args[0]));
			}

			if (args.length == 2) {
				return command.executeCommand(env, args[1]);
			} else if (args.length == 1) {
				return command.executeCommand(env, null);
			}
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Handles 'symbol PROMPT' command. Basically same as other symbol commands.
	 * 
	 * @param cmd
	 *            arguments of the command
	 * @return status of the shell after command has been executed
	 */
	private static ShellStatus handleSymbolPrompt(String cmd) {
		String[] args = cmd.split("\\s+");

		if (args.length == 2) {
			try {
				env.writeln(
						String.format(
								"Symbol for PROMPT is '%c'",
								env.getMultilineSymbol()));
			} catch (IOException ignorable) {
			}
			return ShellStatus.CONTINUE;
		} else if (args.length == 3) {

			Character newPrompt = args[2].charAt(0);

			environments.put(env.getPromptSymbol(), env);

			// if shell with same prompt exists change current environment
			// to that and if it doesn't exist create new environment
			if (environments.containsKey(newPrompt)) {
				env = environments.get(newPrompt);
			} else {
				env = new ShellEnvironment(newPrompt);
			}

			try {
				env.writeln(
						String.format(
								"Symbol for PROMPT changed from '%c' to '%c'",
								env.getMultilineSymbol(),
								newPrompt));
			} catch (IOException ignorable) {
			}
		} else {
			try {
				env.writeln(
						"Error: Invalid arguments for the command 'symbol'!");
			} catch (IOException ignorable) {
			}
		}

		return ShellStatus.CONTINUE;
	}
}
