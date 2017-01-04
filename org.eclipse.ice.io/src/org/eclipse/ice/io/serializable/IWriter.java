/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.io.serializable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.january.form.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The IWriter interface defines the functionality needed to write an ICE
 * internal object representation to file . It also allows clients to search for
 * a given pattern and replace it with a provided value.
 * 
 * @author Alex McCaskey
 *
 */
public interface IWriter {

	/**
	 * This method lets clients write a Form to a specific file format.
	 * 
	 * @param formToWrite
	 *            The Form to persist to file.
	 * @param file
	 *            The filename where the contents should be written. If this
	 *            file does not exist, it shall be created.
	 */
	public void write(Form formToWrite, IFile file);

	/**
	 * This method provides clients with a basic search and replace
	 * functionality. Realizations of this method should search the file with
	 * given URI for all occurrences of the given regular expression, and
	 * replace it with the given value.
	 * 
	 * @param file
	 *            The filename where the contents should be written. If it does
	 *            not exist, this operation will fail.
	 * @param regex
	 *            The regular expression to search.
	 * @param value
	 *            The value that should replace all occurrences of the given
	 *            regular expression.
	 */
	public void replace(IFile file, String regex, String value);

	/**
	 * This method is used by the IOService to store unique references of
	 * IWriters. It is used as the key in the IOService's IWriter mapping.
	 * 
	 * This type should not include dots or other special characters. It should
	 * just be the name of the format.
	 * 
	 * @return The String name of this IWriter.
	 */
	public String getWriterType();

	/**
	 * This operation retrieves all of the IWriters from the
	 * ExtensionRegistry.
	 *
	 * @return The array of IWriters that were found in the registry.
	 * @throws CoreException
	 *             This exception is thrown if an extension cannot be loaded.
	 */
	public static IWriter[] getIWriters() throws CoreException {

		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(IReader.class);

		IWriter[] writers = null;
		String id = "org.eclipse.ice.io.writer";
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(id);

		// If the point is available, create all the writers and load them into
		// the array.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			writers = new IWriter[elements.length];
			for (int i = 0; i < elements.length; i++) {
				writers[i] = (IWriter) elements[i]
						.createExecutableExtension("class");
			}
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return writers;
	}
}
