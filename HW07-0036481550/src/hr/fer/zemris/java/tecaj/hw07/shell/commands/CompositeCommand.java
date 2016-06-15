package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code CompositeCommand} class is composite of various implementation of the
 * interface {@link ShellCommand}.
 * <p>
 * Shell commands contained in this class are:
 * <ul>
 * <li>{@code cat [FILE] [CHARSET]}
 * <li>{@code charsets}
 * <li>{@code copy [SRC] [DEST]}
 * <li>{@code hexdump [FILE]}
 * <li>{@code ls [DIRECTORY]}
 * <li>{@code mkdir [DIRECTORY]}
 * <li>{@code tree [DIRECTORY]}
 * </ul>
 * <p>
 * If you want more information about shell commands contained here look up
 * documentation of classes where they are implemented.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see ShellCommand
 * @see CommandCat
 * @see CommandCharsets
 * @see CommandCopy
 * @see CommandHexdump
 * @see CommandLs
 * @see CommandMkdir
 * @see CommandTree
 */
public class CompositeCommand {

	/**
	 * Number of commands contained in the internal map
	 */
	private static final int NUM_OF_COMMANDS = 7;

	/**
	 * Map for storing shell commands
	 */
	private static Map<String, ShellCommand> commands;

	static {
		commands = new HashMap<>(NUM_OF_COMMANDS);

		commands.put("cat", new CommandCat());
		commands.put("charsets", new CommandCharsets());
		commands.put("copy", new CommandCopy());
		commands.put("exit", new CommandExit());
		commands.put("help", new CommandHelp());
		commands.put("hexdump", new CommandHexdump());
		commands.put("ls", new CommandLs());
		commands.put("mkdir", new CommandMkdir());
		commands.put("symbol", new CommandSymbol());
		commands.put("tree", new CommandTree());

		commands = Collections.unmodifiableMap(commands);
	}

	/**
	 * Returns the command to which the specified command name is mapped.
	 * 
	 * @param cmd the command name
	 * @return the command to which the specified command name is mapped
	 */
	public static ShellCommand get(String cmd) {
		cmd = cmd.trim();
		
		ShellCommand command = commands.get(cmd.split("\\s+")[0]);

		if(command == null) {
			throw new IllegalArgumentException("Command you wanted doesn't"
					+ " exist!");
		}
		
		return command;
	}

	/**
	 * Returns the read-only map of all commands
	 * 
	 * @return the read-only map of all commands
	 */
	public static Map<String, ShellCommand> getAllCommands() {
		return commands;
	}
}
