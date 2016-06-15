package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.MyShell;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * {@code CommandLs} class represent shell command that lists content of a
 * directory specified by arguments
 * <p>
 * If you want to call {@code ls} command in {@link MyShell} you must type to
 * shell:
 * <ul>
 * <li>{@code ls [DIRECTORY]}
 * </ul>
 * <p>
 * This command writes a directory listing. Information about files and
 * directories are written in this format:
 * <ul>
 * <li>{@code flags size_in_bytes yyyy-mm-dd hh:mm:ss name}
 * </ul>
 * First column({@code flags}) indicates if current object is directory(d),
 * readable(r), writable(w) and executable(x). If current object doesn't meets
 * requirements for some flag it's indicated with '-' sign.
 * <p>
 * Example output:
 * <p>
 * -rw- 53412 2009-03-15 12:59:31 azuriraj.ZIP<br>
 * drwx 4096 2011-06-08 12:59:31 b<br>
 * drwx 4096 2011-09-19 12:59:31 backup<br>
 * -rw- 17345597 2009-02-18 12:59:31 backup-ferko-20090218.tgz<br>
 * drwx 4096 2008-11-09 12:59:31 beskonacno<br>
 * drwx 4096 2010-10-29 12:59:31 bin<br>
 * -rwx 282 2011-02-10 12:59:31 burza.sh<br>
 * -rwx 281 2011-02-10 12:59:31 burza.sh~<br>
 * -rwx 1316 2009-09-10 12:59:31 burza_stat.sh<br>
 * drwx 4096 2011-09-02 12:59:31 ca<br>
 * drwx 4096 2008-09-02 12:59:31 CA<br>
 * -rw- 0 2008-09-02 12:59:31 ca.key<br>
 * <p>
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see ShellCommand
 */
public class CommandLs implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String COMMAND_NAME = "ls";

	/**
	 * Command description and manual.
	 */
	private static List<String> COMMAND_DESCRIPTION;
	
	static {
		COMMAND_DESCRIPTION = new ArrayList<>();

		COMMAND_DESCRIPTION.add(
				"'ls' command lists all files and directories from specified"
				+ " directory path.");

		COMMAND_DESCRIPTION.add("General syntax is:  ls [DIRECTORY]");
		
		COMMAND_DESCRIPTION.add("This command writes a directory listing."
				+ " Information about files and directories");
		
		COMMAND_DESCRIPTION.add("are written in this format:");
		
		COMMAND_DESCRIPTION.add("\tflags size_in_bytes yyyy-mm-dd hh:mm:ss name");
		
		COMMAND_DESCRIPTION.add("First column({@code flags}) indicates if"
				+ " current object is directory(d),");
		
		COMMAND_DESCRIPTION.add("readable(r), writable(w) and executable(x). If"
				+ " current object doesn't meets");
		
		COMMAND_DESCRIPTION.add("requirements for some flag it's indicated with"
				+ " '-' sign.");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (!CommandUtils.checkArgumentsLs(env, arguments)) {
			return ShellStatus.CONTINUE;
		}
		
		if(arguments == null) {
			arguments = "./";
		}

		Path dir = Paths.get(arguments.trim());

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for (Path child : stream) {
				env.writeln(format(child));
			}

		} catch (NotDirectoryException e) {
			try {
				env.writeln("Error: you didn't provide path to the directory!");
			} catch (IOException ignorable) {
			}
		} catch (IOException e) {
			try {
				env.writeln("Error: input/output exception occured!");
			} catch (IOException ignorable) {
			}
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Formats and returns a string representation of a child path.
	 * <p>
	 * Example output: {@code "drwx 4096 2011-06-08 12:59:31 b"} <br>
	 * 
	 * @param child
	 *            the child path
	 * @return a formatted string representation of a child path
	 */
	private String format(Path child) {
		StringBuilder sb = new StringBuilder();

		sb.append(flags(child));
		sb.append(' ');

		BasicFileAttributes attr = null;
		try {
			attr = Files.readAttributes(child, BasicFileAttributes.class);
			long size = Files.isDirectory(child) ? getFolderSize(child)
					: attr.size();
			sb.append(String.format("%10d ", size));
		} catch (IOException ignorable) {
		}

		String time = attr.creationTime().toString();
		time = time
				.substring(0, Math.max(time.lastIndexOf('.'), time.length()))
				.replace('T', ' ');

		sb.append(time);
		sb.append(' ');

		sb.append(child.getFileName());

		return sb.toString();
	}

	/**
	 * Returns the string representation of path attributes.
	 * 
	 * @param path
	 *            the path to file or directory
	 * @return the string representation of path attributes
	 */
	private String flags(Path path) {
		StringBuilder sb = new StringBuilder();

		sb.append(Files.isDirectory(path) ? 'd' : '-');
		sb.append(Files.isReadable(path) ? 'r' : '-');
		sb.append(Files.isWritable(path) ? 'w' : '-');
		sb.append(Files.isExecutable(path) ? 'x' : '-');

		return sb.toString();
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
	 * Returns the size of the file (in bytes).
	 * 
	 * @param dir the path of directory
	 * @return the size of a directory
	 */
	private long getFolderSize(Path dir) {
		long size = 0;
		File[] files = dir.toFile().listFiles();

		int count = files.length;

		for (int i = 0; i < count; i++) {
			if (files[i].isFile()) {
				size += files[i].length();
			} else {
				size += getFolderSize(files[i].toPath());
			}
		}

		return size;
	}
}
