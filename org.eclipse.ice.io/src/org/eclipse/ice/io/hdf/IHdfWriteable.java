/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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

import java.util.ArrayList;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

/**
 * <p>
 * An interface that provides the required operations for populating an HDF5
 * file from an ICE data structure.
 * </p>
 * 
 * @author Eric J. Lingerfelt
 */
public interface IHdfWriteable {
	/**
	 * <p>
	 * This operation creates and returns a child H5Group for the parentH5Group
	 * in the h5File. If h5File is null or can not be opened, then null is
	 * returned. If parentH5Group is null, then null is returned. If an
	 * exception is thrown, then null is returned.
	 * </p>
	 * 
	 * @param h5File
	 *            <p>
	 *            The H5File.
	 *            </p>
	 * @param parentH5Group
	 *            <p>
	 *            The parent H5Group.
	 *            </p>
	 * @return <p>
	 *         The new H5Group.
	 *         </p>
	 */
	public H5Group createGroup(H5File h5File, H5Group parentH5Group);

	/**
	 * <p>
	 * This operation returns an ArrayList of IHdfWriteable child objects. If
	 * this IHdfWriteable has no IHdfWriteable child objects, then null is
	 * returned.
	 * </p>
	 * 
	 * @return <p>
	 *         An ArrayList of IHdfWriteable child objects.
	 *         </p>
	 */
	public ArrayList<IHdfWriteable> getWriteableChildren();

	/**
	 * <p>
	 * This operation writes HDF5 Attributes to the metadata of h5Group in the
	 * h5File. If the h5Group is null or h5File is null or can not be opened,
	 * then false is returned. If the operation fails to write all Attributes,
	 * then false is returned. Otherwise, true is returned.
	 * </p>
	 * 
	 * @param h5File
	 *            <p>
	 *            The H5File.
	 *            </p>
	 * @param h5Group
	 *            <p>
	 *            The H5Group to write Attributes to.
	 *            </p>
	 * @return <p>
	 *         If the h5Group is null or h5File is null or can not be opened,
	 *         then false is returned. If the operation fails to write all
	 *         Attributes, then false is returned. Otherwise, true is returned.
	 *         </p>
	 */
	public boolean writeAttributes(H5File h5File, H5Group h5Group);

	/**
	 * <p>
	 * This operation writes HDF5 Datasets to the h5Group in the h5File. If the
	 * h5Group is null or h5File is null or can not be opened, then false is
	 * returned. If the operation fails to write all Datasets, then false is
	 * returned. Otherwise, true is returned.
	 * </p>
	 * 
	 * @param h5File
	 *            <p>
	 *            The H5File.
	 *            </p>
	 * @param h5Group
	 *            <p>
	 *            The H5Group to write Datasets to.
	 *            </p>
	 * @return <p>
	 *         If the h5Group is null or h5File is null or can not be opened,
	 *         then false is returned. If the operation fails to write all
	 *         Datasets, then false is returned. Otherwise, true is returned.
	 *         </p>
	 */
	public boolean writeDatasets(H5File h5File, H5Group h5Group);
}