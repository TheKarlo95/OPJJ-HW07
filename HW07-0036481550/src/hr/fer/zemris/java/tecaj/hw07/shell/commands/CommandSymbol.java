package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.MyShell;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * {@code CommandSymbol} class represent shell command that changes MORELINES
 * and MULTILINE characters.
 * <p>
 * If you want to call {@code exit} command in {@link MyShell} you must type to
 * shell:
 * <ul>
 * <li>{@code symbol MORELINES [CHARACTER]}
 * <li>{@code symbol MULTILINE [CHARACTER]}
 * </ul>
 * <p>
 * Argument CHARACTER isn't mandatory. If left out command will print current
 * symbol with appropriate message.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see ShellCommand
 */
public class CommandSymbol implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String COMMAND_NAME = "symbol";

	/**
	 * Command description and manual.
	 */
	private static List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();

		COMMAND_DESCRIPTION.add(
				"'symbol' command hanges MORELINES and MULTILINE characters.");

		COMMAND_DESCRIPTION.add(
				"General syntax is:  symbol MORELINES/MULTILINE [CHARACTER]");
		
		COMMAND_DESCRIPTION.add("Argument CHARACTER isn't mandatory. If left"
				+ " out command will print current");
		
		COMMAND_DESCRIPTION.add("current symbol with appropriate message.");
		
		COMMAND_DESCRIPTION = Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (!CommandUtils.checkArgumentsSymbol(env, arguments)) {
			return ShellStatus.CONTINUE;
		}

		if (arguments.matches("^MORELINES\\s*.?$")) {
			changeSymbol(env, "MORELINES", arguments);
		} else if (arguments.matches("^MULTILINE\\s*.?$")) {
			changeSymbol(env, "MULTILINE", arguments);
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
	 * 
	 * 
	 * @param env
	 *            environment of the shell
	 * @param symbol
	 *            indicates whether it's MORELINES or MULTILINE you want to
	 *            change
	 * @param str
	 *            all arguments
	 */
	private static void changeSymbol(Environment env, String symbol,
			String str) {
		if (symbol == null) {
			throw new NullPointerException("Argument symbol cannot be null!");
		}
		if (str == null) {
			throw new NullPointerException("Argument str cannot be null!");
		}

		String[] args = str.split("\\s+");

		if (!symbol.equals("MORELINES") && !symbol.equals("MULTILINE")) {
			try {
				env.writeln(
						"Error: Invalid arguments for the command 'symbol'!");
			} catch (IOException ignorable) {
			}
		}

		if (args.length == 1) {
			try {
				env.writeln(
						String.format(
								"Symbol for %s is '%c'",
								symbol,
								env.getMultilineSymbol()));
			} catch (IOException ignorable) {
			}
			return;
		} else if (args.length == 2) {
			Character newCharacter = str.charAt(0);
			try {
				env.writeln(
						String.format(
								"Symbol for %s changed from '%c' to '%c'",
								symbol,
								getSymbol(env, symbol),
								newCharacter));
			} catch (IOException ignorable) {
			}

			if (symbol.equals("MORELINES")) {
				env.setMorelinesSymbol(newCharacter);
			} else if (symbol.equals("MULTILINE")) {
				env.setMultilineSymbol(newCharacter);
			}
		} else {
			try {
				env.writeln(
						"Error: Invalid arguments for the command 'symbol'!");
			} catch (IOException ignorable) {
			}
		}
	}

	/**
	 * Returns the morelines or multiline character depending on the symbol.
	 * 
	 * @param env
	 *            environment of the shell
	 * @param symbol
	 *            symbol
	 * @return the morelines or multiline character depending on the symbol
	 */
	private static Character getSymbol(Environment env, String symbol) {
		if (symbol.equals("MULTILINE")) {
			return env.getMultilineSymbol();
		} else if (symbol.equals("MULTILINE")) {
			return env.getMorelinesSymbol();
		} else {
			return null;
		}
	}
}
