package org.eclipse.ice.dev.annotations.processors;

/**
 * Exception raised during annotation extraction if the passed element is
 * invalid.
 * @author Daniel Bluhm
 */
public class InvalidElementException extends Exception {

	/**
	 * Exception with message.
	 * @param string message
	 */
	public InvalidElementException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1481453819406854006L;
}
