package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.MyShell;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * {@code CommandCopy} class represent shell command that copies files.
 * <p>
 * If you want to call {@code copy} command in {@link MyShell} you must type to
 * shell:
 * <ul>
 * <li>{@code copy [SRC] [DEST]}
 * </ul>
 * <p>
 * The {@code copy} command expects two arguments: source file name(SRC) and
 * destination file name (i.e. paths and names)(DEST). If destination file
 * exists, user will be asked if it's allowed to overwrite it. Source file name
 * arguments accepts paths to files only. If the second argument is directory,
 * you should assume that user wants to copy the original file into that
 * directory using the original file name.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see ShellCommand
 */
public class CommandCopy implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String COMMAND_NAME = "copy";

	/**
	 * Command description and manual.
	 */
	private static List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();

		COMMAND_DESCRIPTION.add("'copy' command copies files");
		
		COMMAND_DESCRIPTION.add("General syntax is:  copy [SRC] [DEST]");
		
		COMMAND_DESCRIPTION.add("Source file name arguments accepts paths to"
				+ " files only. If destination file");
		
		COMMAND_DESCRIPTION.add("exists, user will be asked if it's allowed to"
				+ " overwrite it. Source file name");

		COMMAND_DESCRIPTION.add("arguments accepts paths to files only. If the"
				+ " second argument is directory, you");
		
		COMMAND_DESCRIPTION.add("should assume that user wants to copy the"
				+ " original file into that directory");
		
		COMMAND_DESCRIPTION.add("using the original file name.");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (!checkArguments(env, arguments)) {
			return ShellStatus.CONTINUE;
		}

		String[] args = arguments.trim().split("\\s+");

		Path src = Paths.get(args[0]);
		Path dest = Paths.get(args[1]);

		if (Files.exists(dest)) {
			while (true) {
				String response = null;
				try {
					env.writeln(
							String.format(
									"File '%s' already exists. Would you like to"
											+ " overwrite it?(Y/N)",
									dest.getFileName()));

					response = env.readLine().trim().toUpperCase();

					if (response.equals("Y")) {
						break;
					} else if (response.equals("N")) {
						return ShellStatus.CONTINUE;
					} else {
						env.writeln("Invalid answer!");
					}
				} catch (IOException ignorable) {
				}
			}
		}

		try {
			copy(env, src, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * Checks if arguments are valid
	 * 
	 * @param env
	 *            environment of the shell
	 * @param arguments
	 *            arguments of this command
	 * @return {@code true} if arguments are valid; {code false} otherwise
	 */
	private boolean checkArguments(Environment env, String arguments) {
		if (env == null) {
			System.err.println("Environment cannot be null reference!");
			return false;
		}

		if (arguments == null) {
			try {
				env.writeln(
						"Error: wrong number of arguments for command 'copy'!");
			} catch (IOException ignorable) {
			}

			return false;
		}

		String[] args = arguments.trim().split("\\s+");

		if (args.length != 2) {
			try {
				env.writeln(
						"Error: wrong number of arguments for command 'copy'!");
			} catch (IOException ignorable) {
			}

			return false;
		}

		Path src = Paths.get(args[0]);

		if (!(Files.exists(src) && Files.isRegularFile(src))) {
			try {
				env.writeln(
						String.format(
								"Error: source file '%s' doesn't exists or"
										+ " isn't a file!",
								src.getFileName().toString()));
			} catch (IOException ignorable) {
			}
		}

		return true;
	}

	/**
	 * Copies file at {@code src} path to {@code dest} path.
	 * <p>
	 * Source has to be a file. This function doesn't support copying of
	 * directories. <br>
	 * If destination path doesn't exist method will create it. If destination
	 * path is directory file from source will be copied there with the same
	 * name as source file. If source and destination paths are the same this
	 * method will make another copy of specified file and append " - Copy" to
	 * its name.
	 * 
	 * @param env
	 *            environment of the shell
	 * @param src
	 *            the source path
	 * @param dest
	 *            the destination path
	 * @throws IOException
	 *             if source or destination paths cannot be opened
	 */
	private void copy(Environment env, Path src, Path dest) throws IOException {
		src = src.toAbsolutePath();
		dest = dest.toAbsolutePath();

		if (isDirectoryPath(dest)) {
			Files.createDirectories(dest);

			dest = Paths.get(dest.toString(), src.getFileName().toString());
		} else {
			Files.createDirectories(dest.getParent());
		}

		if (Files.isSameFile(src, dest)) {
			String path = src
					.toString()
					.substring(0, src.toString().lastIndexOf('.'));

			String exstension = src
					.toString()
					.substring(src.toString().lastIndexOf('.'));

			dest = Paths.get(path + " - Copy" + exstension);
		}

		try (InputStream in = Files
				.newInputStream(src, StandardOpenOption.READ);
				OutputStream out = Files.newOutputStream(
						dest,
						StandardOpenOption.CREATE,
						StandardOpenOption.WRITE)) {

			byte[] buf = new byte[4092];
			int n;

			while ((n = in.read(buf)) > 0) {
				out.write(buf, 0, n);
			}
		} catch (IOException e) {
			env.writeln(
					"Error: couldn't open stream to source or destination"
							+ " file!");
		}

	}

	/**
	 * Checks is given path a path to directory. This method will work the same
	 * if path exists or doesn't.
	 * 
	 * @param path
	 *            the path
	 * @return {@code true} if path is a directory path; {@code false} otherwise
	 */
	private boolean isDirectoryPath(Path path) {
		if (!Files.exists(path)) {
			return path.getFileName().toString().lastIndexOf('.') == -1;
		} else {
			return Files.isDirectory(path);
		}
	}

}
