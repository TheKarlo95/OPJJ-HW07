package hr.fer.zemris.java.tecaj.hw07.shell;

/**
 * {@code ShellStatus} represents the status of the shell.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public enum ShellStatus {
	/**
	 * Shell continues accepting user input
	 */
	CONTINUE,
	
	/**
	 * Shell stops accepting user input and program is terminated.
	 */
	TERMINATE;
}
