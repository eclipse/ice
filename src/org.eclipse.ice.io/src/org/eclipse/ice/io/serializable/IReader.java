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

import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;

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
	 * @param uri
	 *            The URI of the file to be read.
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
	 * @param regexp
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

}
