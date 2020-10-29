package org.eclipse.ice.dev.jsonschemaconverter;

/**
 * Exception to be thrown when an invalid file name cannot be converted to a valid file name
 * @author gzi
 *
 */
public class InvalidFileNameException extends Exception {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5668012508905904582L;

	public InvalidFileNameException(String message) {
		super(message);
	}
}
