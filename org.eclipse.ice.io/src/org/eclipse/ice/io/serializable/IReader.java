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

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The IReader interface defines the functionality needed to read files into an
 * expected ICE internal object representation. It also allows clients to search
 * a file for all occurrences of a specific pattern.
 * 
 * @author Alex McCaskey
 *
 */
public interface IReader {

	/**
	 * Return a Form representation of the input file with given URI.
	 * 
	 * @param file
	 *            The file to be read.
	 * @return A Form object containing the data read in from the file.
	 */
	public Form read(IFile file);

	/**
	 * This method can be used by realizations of this interface to provide the
	 * functionality needed to find all occurrences of a String regular
	 * expression in a file with the given URI and return a list of Entries
	 * representing those occurrences.
	 * 
	 * @param file
	 *            The file to search
	 * @param regex
	 *            The regular expression to search.
	 * @return A list of Entries representing occurrences of the given regular
	 *         expression.
	 */
	public ArrayList<Entry> findAll(IFile file, String regex);

	/**
	 * This method is used by the IOService to store unique references of
	 * IReaders. It is used as the key in the IOService's IReader mapping.
	 * 
	 * This type should not include dots or other special characters. It should
	 * just be the name of the format.
	 * 
	 * @return The String name of this IReader.
	 */
	public String getReaderType();
	
	/**
	 * This operation retrieves all of the IReaders from the
	 * ExtensionRegistry.
	 *
	 * @return The array of IReaders that were found in the registry.
	 * @throws CoreException
	 *             This exception is thrown if an extension cannot be loaded.
	 */
	public static IReader[] getIReaders() throws CoreException {

		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(IReader.class);

		IReader[] readers = null;
		String id = "org.eclipse.ice.io.reader";
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(id);

		// If the point is available, create all the readers and load them into
		// the array.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			readers = new IReader[elements.length];
			for (int i = 0; i < elements.length; i++) {
				readers[i] = (IReader) elements[i]
						.createExecutableExtension("class");
			}
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return readers;
	}

}
