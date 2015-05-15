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
package org.eclipse.ice.io.hdf;

import java.net.URI;

/**
 * <p>
 * An interface that provides the required operations for reading and creating
 * an IHdfWriteable tree from an HDF5 file.
 * </p>
 * 
 * @author els
 */
public interface IHdfReader {
	/**
	 * <p>
	 * This operation creates and populates an IHdfWriteable instance and its
	 * children from the HDF5 file at the provided URI. If any error or failure
	 * to read is encountered, then null is returned.
	 * </p>
	 * 
	 * @param uri
	 *            <p>
	 *            The URI of the H5File.
	 *            </p>
	 * @return <p>
	 *         The IHdfreadable instance created and populated from the H5File
	 *         at uri.
	 *         </p>
	 */
	public IHdfReadable read(URI uri);
}