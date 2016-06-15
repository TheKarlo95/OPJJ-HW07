package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.MyShell;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * {@code CommandCharsets} class represent shell command that lists all
 * available charsets.
 * <p>
 * If you want to call {@code charset} command in {@link MyShell} you must type
 * to shell:
 * <ul>
 * <li>{@code charsets}
 * </ul>
 * <p>
 * This command writes a charset listing. Standard charsets offered by this
 * program are:
 * <ul>
 * <li>US-ASCII&nbsp;&nbsp;&nbsp;&nbsp;Seven-bit ASCII, a.k.a. ISO646-US, a.k.a.
 * the Basic Latin block of the Unicode character set
 * <li>ISO-8859-1&nbsp;&nbsp;&nbsp;&nbsp;ISO Latin Alphabet No. 1, a.k.a.
 * ISO-LATIN-1
 * <li>UTF-8&nbsp;&nbsp;&nbsp;&nbsp;Eight-bit UCS Transformation Format
 * <li>UTF-16BE&nbsp;&nbsp;&nbsp;&nbsp;Sixteen-bit UCS Transformation Format,
 * big-endian byte order
 * <li>UTF-16LE&nbsp;&nbsp;&nbsp;&nbsp;Sixteen-bit UCS Transformation Format,
 * little-endian byte order
 * <li>UTF-16&nbsp;&nbsp;&nbsp;&nbsp;Sixteen-bit UCS Transformation Format, byte
 * order identified by an optional byte-order mark
 * </ul>
 * <br>
 * Note that there can be more charsets depending on your system.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see ShellCommand
 */
public class CommandCharsets implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String COMMAND_NAME = "charsets";

	/**
	 * Command description and manual.
	 */
	private static List<String> COMMAND_DESCRIPTION;
	
	static {
		COMMAND_DESCRIPTION = new ArrayList<>();

		COMMAND_DESCRIPTION.add(
				"'charsets' command displays all available charsets.");

		COMMAND_DESCRIPTION.add("General syntax is:  charsets");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (!CommandUtils.checkArgumentsCharsets(env, arguments)) {
			return ShellStatus.CONTINUE;
		}

		Charset.availableCharsets().values().forEach(x -> {
			try {
				env.writeln(x.toString());
			} catch (IOException ignorable) {
			}
		});

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
