package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.processing.Filer;

/**
 * FileWriter interface for files generated during annotation processing.
 *
 * @author Daniel Bluhm
 */
public interface GeneratedFileWriter extends FileWriter {

	/**
	 * Open a java.io.Writer for this FileWriter using the annotation processing
	 * environment filer.
	 * @param filer Annotation processing environment filer
	 * @return writer
	 */
	public Writer openWriter(Filer filer) throws IOException;
}
