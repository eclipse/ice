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

import ncsa.hdf.object.h5.H5Group;

/**
 * <p>
 * An interface that provides the required operations for populating an ICE data
 * structure from an HDF5 file.
 * </p>
 * 
 * @author Eric J. Lingerfelt
 */
public interface IHdfReadable {

	/**
	 * <p>
	 * This operation assigns the reference of the provided iHdfReadable to a
	 * class variable. If iHdfReadable is null, then false is returned. If
	 * iHdfReadable fails casting, then false is returned. Otherwise, true is
	 * returned.
	 * </p>
	 * 
	 * @param iHdfReadable
	 *            <p>
	 *            The IHdfReadable whose reference is to be cast and assigned to
	 *            a class variable.
	 *            </p>
	 * @return <p>
	 *         If iHdfReadable is null, then false is returned. If iHdfReadable
	 *         fails casting, then false is returned. Otherwise, true is
	 *         returned.
	 *         </p>
	 */
	public boolean readChild(IHdfReadable iHdfReadable);

	/**
	 * <p>
	 * This operation reads Attributes from h5Group and assigns their values to
	 * class variables. If h5Group is null, false is returned. If any Attribute
	 * values are null, false is returned. Otherwise, true is returned.
	 * </p>
	 * 
	 * @param h5Group
	 *            <p>
	 *            The H5Group to read Attributes from.
	 *            </p>
	 * @return <p>
	 *         If h5Group is null, false is returned. If any Attribute values
	 *         are null, false is returned. Otherwise, true is returned.
	 *         </p>
	 */
	public boolean readAttributes(H5Group h5Group);

	/**
	 * <p>
	 * This operation reads Datasets from h5Group and assigns their values to
	 * class variables. If h5Group is null or an Exception is thrown, false is
	 * returned. If the Otherwise, true is returned.
	 * </p>
	 * 
	 * @param h5Group
	 *            <p>
	 *            The H5Group to read Datasets from.
	 *            </p>
	 * @return <p>
	 *         If h5Group is null or an Exception is thrown, false is returned.
	 *         If the Otherwise, true is returned.
	 *         </p>
	 */
	public boolean readDatasets(H5Group h5Group);
}