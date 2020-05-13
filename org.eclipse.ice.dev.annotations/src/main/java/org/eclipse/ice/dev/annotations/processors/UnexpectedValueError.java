package org.eclipse.ice.dev.annotations.processors;

/**
 * Raised by AnnotationValue Visitors when an unexpected AnnotationValue is encountered.
 */
public class UnexpectedValueError extends Exception {
	private static final long serialVersionUID = -8486833574190525020L;

	public UnexpectedValueError(final String message) {
		super(message);
	}
}