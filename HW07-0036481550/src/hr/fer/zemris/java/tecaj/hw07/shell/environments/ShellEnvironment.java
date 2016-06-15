package hr.fer.zemris.java.tecaj.hw07.shell.environments;

import java.io.IOException;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.CompositeCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand;

/**
 * {@code ShellEnvironment} class represents an environment of the shell.
 * <p>
 * It contains all information about the current state of the shell and is used
 * to output and input data and commands from user.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see Environment
 */
public class ShellEnvironment implements Environment {

	/**
	 * Default character for {@code PROMPT}.
	 */
	public static final Character DEFAULT_PROMPT = '>';

	/**
	 * Default character indicating {@code MORELINES} mode.
	 */
	public static final Character DEFAULT_MORELINE = '\\';

	/**
	 * Default character for {@code MULTILINES}.
	 */
	public static final Character DEFAULT_MULTILINE = '|';

	/**
	 * The prompt character
	 */
	private Character prompt;

	/**
	 * The moreline character
	 */
	private Character moreLine;

	/**
	 * The multiline character
	 */
	private Character multiLine;

	/**
	 * Constructs a new {@code ShellCommand} object specified by input stream,
	 * output stream, prompt symbol, moreline symbol and multiline symbol are
	 * specified by arguments.
	 * 
	 * @param prompt
	 *            the prompt character
	 * @param moreLine
	 *            the moreline character
	 * @param multiLine
	 *            the multiline character
	 */
	public ShellEnvironment(Character prompt, Character moreLine,
			Character multiLine) {
		if (prompt == null || Character.isWhitespace(prompt)) {
			throw new IllegalArgumentException(
					"Prompt character cannot be null reference or whitespace.");
		}

		if (moreLine == null || Character.isWhitespace(moreLine)) {
			throw new IllegalArgumentException(
					"Moreline character cannot be null reference or whitespace.");
		}

		if (moreLine == null || Character.isWhitespace(multiLine)) {
			throw new IllegalArgumentException(
					"Multiline character cannot be null reference or whitespace.");
		}

		this.prompt = prompt;
		this.moreLine = moreLine;
		this.multiLine = multiLine;
	}

	/**
	 * Constructs a new {@code ShellCommand} object with default input stream
	 * {@code System.in}, output stream ({@code System.out}), moreline symbol(
	 * {@value #DEFAULT_MORELINE}) and default multiline symbol(
	 * {@value #DEFAULT_MULTILINE}). Prompt symbol is specified by argument.
	 *
	 * @param prompt
	 *            the prompt character
	 */
	public ShellEnvironment(Character prompt) {
		this(prompt, DEFAULT_MORELINE, DEFAULT_MULTILINE);
	}

	/**
	 * Constructs a new {@code ShellCommand} object with default input stream
	 * {@code System.in}, output stream ({@code System.out}), prompt symbol(
	 * {@value #DEFAULT_PROMPT}), moreline symbol({@value #DEFAULT_MORELINE})
	 * and default multiline symbol({@value #DEFAULT_MULTILINE}).
	 */
	public ShellEnvironment() {
		this(DEFAULT_PROMPT, DEFAULT_MORELINE, DEFAULT_MULTILINE);
	}

	@Override
	public String readLine() throws IOException {
		StringBuilder sb = new StringBuilder();

		while (true) {
			char c = (char) System.in.read();

			if (!Character.isDefined(c) || c == '\n') {
				break;
			}

			sb.append(c);
		}

		return sb.toString();
	}

	@Override
	public void write(String text) throws IOException {
		if (text == null) {
			throw new NullPointerException("Text argument cannot be null!");
		}

		System.out.print(text);
	}

	@Override
	public void writeln(String text) throws IOException {
		if (text == null) {
			throw new NullPointerException("Text argument cannot be null!");
		}

		System.out.println(text);
	}

	@Override
	public Iterable<ShellCommand> commands() {
		return CompositeCommand.getAllCommands().values();
	}

	@Override
	public Character getMultilineSymbol() {
		return multiLine;
	}

	@Override
	public void setMultilineSymbol(Character symbol) {
		multiLine = symbol;
	}

	@Override
	public Character getPromptSymbol() {
		return prompt;
	}

	@Override
	public void setPromptSymbol(Character symbol) {
		prompt = symbol;
	}

	@Override
	public Character getMorelinesSymbol() {
		return moreLine;
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {
		moreLine = symbol;
	}

}
