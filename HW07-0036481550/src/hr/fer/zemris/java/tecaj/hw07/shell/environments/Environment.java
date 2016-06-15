package hr.fer.zemris.java.tecaj.hw07.shell.environments;

import java.io.IOException;

import hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand;

/**
 * {@code Environment} interface represents an environment of the shell.
 * <p>
 * Implementations of this interface should contain all information about the
 * current state of the shell and should be used to output and input data and
 * commands from user.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public interface Environment {

	/**
	 * Reads the next line from input stream.
	 * 
	 * @return the next line
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public String readLine() throws IOException;

	/**
	 * Writes the text to output stream.
	 * 
	 * @param text
	 *            the text
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void write(String text) throws IOException;

	/**
	 * Writes the text to output stream and writes a newline character after
	 * text.
	 * 
	 * @param text
	 *            the text
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void writeln(String text) throws IOException;

	/**
	 * Returns the iterable collection of all commands.
	 * 
	 * @return the iterable collection of all commands
	 */
	public Iterable<ShellCommand> commands();
	
	/**
	 * Returns the current prompt character.
	 * 
	 * @return the current prompt character
	 */
	public Character getPromptSymbol();
	
	/**
	 * Sets the new prompt character.
	 * 
	 * @param symbol the new prompt character
	 */
	public void setPromptSymbol(Character symbol);

	/**
	 * Returns the current moreline character.
	 * 
	 * @return the current moreline character
	 */
	public Character getMorelinesSymbol();

	/**
	 * Sets the new moreline character.
	 * 
	 * @param symbol the new moreline character
	 */
	public void setMorelinesSymbol(Character symbol);
	
	/**
	 * Returns the current multiline character.
	 * 
	 * @return the current multiline character
	 */
	public Character getMultilineSymbol();

	/**
	 * Sets the new multiline character.
	 * 
	 * @param symbol the new multiline character
	 */
	public void setMultilineSymbol(Character symbol);

}