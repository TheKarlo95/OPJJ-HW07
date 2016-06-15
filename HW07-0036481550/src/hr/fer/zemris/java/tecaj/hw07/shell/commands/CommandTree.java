package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
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
 * {@code CommandTree} class represent shell command that prints a depth
 * indented listing of files.
 * <p>
 * If you want to call {@code ls} command in {@link MyShell} you must type to
 * shell:
 * <ul>
 * <li>{@code tree [DIRECTORY]}
 * </ul>
 * <p>
 * Each directory level shifts output two characters to the right like in the
 * example output below:
 * <p>
 * file1<br>
 * dir1<br>
 * &nbsp;&nbsp;file2<br>
 * &nbsp;&nbsp;file3<br>
 * &nbsp;&nbsp;dir2<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;file4<br>
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class CommandTree implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String COMMAND_NAME = "tree";

	/**
	 * Command description and manual.
	 */
	private static List<String> COMMAND_DESCRIPTION;

	static {
		COMMAND_DESCRIPTION = new ArrayList<>();

		COMMAND_DESCRIPTION.add(
				"'tree' command prints a depth indented listing of files.");

		COMMAND_DESCRIPTION.add("General syntax is:  tree [DIRECTORY]");
		
		COMMAND_DESCRIPTION.add("Each directory level shifts output two charcaters right.");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (!CommandUtils.checkArgumentsTree(env, arguments)) {
			return ShellStatus.CONTINUE;
		}
		
		if(arguments == null) {
			arguments = "./";
		}

		Path path = Paths.get(arguments).normalize().toAbsolutePath();

		try {
			Files.walkFileTree(path, new TreeLister(env));
		} catch (IOException e) {
			try {
				env.writeln("Error: input/output exception occured!");
			} catch (IOException ignorable) {
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

	/**
	 * {@code TreeLister} is a visitor of files. It is provided to the
	 * {@link Files#walkFileTree(Path, FileVisitor)} methods to visit each file
	 * in a file tree.
	 * 
	 * @author Karlo Vrbić
	 * @version 1.0
	 * @see FileVisitor
	 */
	private static class TreeLister implements FileVisitor<Path> {

		/**
		 * Current depth of a tree
		 */
		private int level = 0;

		/**
		 * Environment of the shell
		 */
		private Environment env;

		/**
		 * Constructs a new {@code TreeLister} with specified
		 * {@link Environment}
		 * 
		 * @param env
		 *            environment of the shell
		 */
		public TreeLister(Environment env) {
			this.env = env;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) throws IOException {
			env.writeln(format(dir));
			level++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc)
				throws IOException {
			level--;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			env.writeln(format(file));
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc)
				throws IOException {
			return FileVisitResult.CONTINUE;
		}

		/**
		 * Returns formatted string of file name specified by path. Before file
		 * name spaces are appended depending on depth of tree.
		 * 
		 * @param path
		 *            the path of file or directory
		 * @return formatted string of file name
		 */
		private String format(Path path) {
			if (level == 0) {
				return path.normalize().toAbsolutePath().toString();
			} else {
				String fileName = null;

				if (path != null) {
					fileName = path.getFileName().toString();
				}

				if (fileName != null) {
					return String
							.format("%" + (2 * level) + "s%s", "", fileName);
				} else {
					return null;
				}
			}
		}

	}

}
