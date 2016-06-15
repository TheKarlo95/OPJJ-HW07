package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * {@code ShellCommand} interface represents a shell command.
 * <p>
 * Every class that implements {@code ShellCommand} must implement these
 * methods:
 * <ul>
 * <li>{@link #executeCommand(Environment, String)}
 * <li>{@link #getCommandName()}
 * <li>{@link #getCommandDescription()}
 * </ul>
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public interface ShellCommand {

	/**
	 * Executes the command in given {@link Environment} with given arguments.
	 * <p>
	 * Arguments are given as one continuous string which will be split into
	 * arguments. Splitting is not specified by this abstract method.
	 * 
	 * @param env
	 *            environment of the shell
	 * @param arguments
	 *            arguments of this command
	 * @return status of shell after execution of this command
	 */
	public ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Returns the command name.
	 * 
	 * @return the command name
	 */
	public String getCommandName();

	/**
	 * Returns the command description as read-only list. Every list entry is
	 * another line of description.
	 * 
	 * @return the command description
	 */
	public List<String> getCommandDescription();

}
