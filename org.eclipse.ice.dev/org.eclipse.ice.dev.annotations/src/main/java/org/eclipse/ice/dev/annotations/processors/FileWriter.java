package org.eclipse.ice.dev.annotations.processors;

import java.io.Writer;

/**
 * Interface for classes that generate a file. In general, FileWriters will
 * have additional methods and operations to determine the contents of the
 * file to be written.
 * @author Daniel Bluhm
 */
public interface FileWriter {

	/**
	 * Write the file to the open writer.
	 * @param writer to which the file will be written.
	 */
	public void write(Writer writer);
}