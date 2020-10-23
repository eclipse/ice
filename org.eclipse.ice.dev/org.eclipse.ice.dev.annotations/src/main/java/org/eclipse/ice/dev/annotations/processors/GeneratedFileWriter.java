/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

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
