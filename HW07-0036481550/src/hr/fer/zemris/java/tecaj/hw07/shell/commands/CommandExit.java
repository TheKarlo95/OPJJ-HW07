package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.MyShell;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * {@code CommandExit} class represent shell command that exits shell.
 * <p>
 * If you want to call {@code exit} command in {@link MyShell} you must type to
 * shell:
 * <ul>
 * <li>{@code exit}
 * </ul>
 * <p>
 * The {@code exit} command expects no arguments.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see ShellCommand
 */
public class CommandExit implements ShellCommand {
	
	/**
	 * Command name.
	 */
	private static final String COMMAND_NAME = "exit";

	/**
	 * Command description and manual.
	 */
	private static List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();

		COMMAND_DESCRIPTION.add(
				"'exit' command exits from the MyShell program");

		COMMAND_DESCRIPTION.add("General syntax is:  exit");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(env == null || arguments != null) {
			try {
				env.writeln("Error: Invalid arguments for the command 'exit'!");
			} catch (IOException ignorable) {
			}
		}
		
		return ShellStatus.TERMINATE;
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		return COMMAND_DESCRIPTION;
	}
}
