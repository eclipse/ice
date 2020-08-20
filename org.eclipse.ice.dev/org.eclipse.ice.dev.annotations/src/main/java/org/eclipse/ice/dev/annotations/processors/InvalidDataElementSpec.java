package org.eclipse.ice.dev.annotations.processors;

/**
 * Thrown when a type is annotated with {@code @DataElement} but is not a class.
 * 
 * @author Daniel Bluhm
 */
public class InvalidDataElementSpec extends Exception {

	private static final long serialVersionUID = -6969771795954829191L;

	public InvalidDataElementSpec(String string) {
		super(string);
	}

}
