package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
public class CommandHelp implements ShellCommand {
	/**
	 * Command name.
	 */
	private static final String COMMAND_NAME = "help";

	/**
	 * Command description and manual.
	 */
	private static List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();

		COMMAND_DESCRIPTION
				.add("'help' command prints out description of a command");

		COMMAND_DESCRIPTION.add("General syntax is:  help [CMD]");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Map<String, ShellCommand> commands = CompositeCommand.getAllCommands();

		if (arguments == null) {
			for (String cmd : CompositeCommand.getAllCommands().keySet()) {
				try {
					env.writeln(cmd);
				} catch (IOException ignorable) {
				}
			}
		} else {
			if (CompositeCommand.getAllCommands().containsKey(arguments)) {
				ShellCommand command = commands.get(arguments);
				if (command != null) {
					try {
						env.writeln(command.getCommandName());

						for (String line : command.getCommandDescription()) {
							env.writeln(line);
						}
					} catch (IOException ignorable) {
					}
				} else {
					try {
						env.writeln(
								String.format(
										"Error: '%s' is not recognized as a command!",
										arguments));
					} catch (IOException ignorable) {
					}
				}
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
}
