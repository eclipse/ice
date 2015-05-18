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
package org.eclipse.ice.reactor;

import java.net.URI;
import java.util.ArrayList;

import org.eclipse.ice.io.hdf.HdfFileFactory;
import org.eclipse.ice.io.hdf.IHdfWriter;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;
import org.eclipse.ice.io.hdf.IHdfWriteable;

/**
 * <p>
 * The LWRComponentReader class writes an LWRComponent or LWRComposite tree to
 * an HDF5 file. This class implements the IHdfWriteable interface. This takes
 * any type of LWRComponent or LWRComposite and iterates from the top of that
 * particular tree down, so a user could write to HDF5 and populate that list
 * accordingly to any part of a Reactor or its delegated classes that inherit
 * from LWRComponent.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class LWRComponentWriter implements IHdfWriter {

	/**
	 * (non-Javadoc)
	 * 
	 * @see IHdfWriter#write(IHdfWriteable iHdfWriteable, URI uri)
	 */
	public boolean write(IHdfWriteable iHdfWriteable, URI uri) {


		// Check for a null iHdfWriteable. If null then return false
		if (iHdfWriteable == null) {

			return false;

		}

		// Create and open a new h5file with the provided uri
		H5File h5File = HdfFileFactory.createH5File(uri);

		// If the file is null, then return false
		if (h5File == null) {

			return false;

		}

		// Get the root group from the file
		H5Group rootH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();

		// If the root group is null then return false
		if (rootH5Group == null) {

			return false;

		}

		// Write the iHdfWriteable to the root group and store the boolean
		// result
		boolean flag = this.write(iHdfWriteable, h5File, rootH5Group);

		// Close the file
		HdfFileFactory.closeH5File(h5File);

		// Return the result
		return flag;
	}

	/**
	 * <p>
	 * Recursively writes the H5Groups, Attributes, and Datasets of the provided
	 * IHdfWriteable to the provided H5File.
	 * </p>
	 * 
	 * @param iHdfWriteable
	 *            <p>
	 *            The IHdfWriteable to be written.
	 *            </p>
	 * @param h5File
	 *            <p>
	 *            The H5File to write to.
	 *            </p>
	 * @param parentH5Group
	 *            <p>
	 *            A H5Group to write for this iteration.
	 *            </p>
	 * @return <p>
	 *         True if the write was successful, false otherwise.
	 *         </p>
	 */
	private boolean write(IHdfWriteable iHdfWriteable, H5File h5File,
			H5Group parentH5Group) {

		// Create a flag to store and return any write failures
		boolean flag = true;

		// Write a new group for this LWRComponent
		H5Group h5Group = iHdfWriteable.createGroup(h5File, parentH5Group);

		// Write the attributes for this LWRComponent
		flag &= iHdfWriteable.writeAttributes(h5File, h5Group);

		// Write the datasets for this LWRComponent
		flag &= iHdfWriteable.writeDatasets(h5File, h5Group);

		// Get the children of iHdfWriteable
		ArrayList<IHdfWriteable> children = iHdfWriteable
				.getWriteableChildren();

		// If there are children
		if (children != null && !(children.isEmpty())) {

			// Cycle over the children
			for (IHdfWriteable child : children) {

				// Write the child
				flag &= this.write(child, h5File, h5Group);

			}

		}

		// Return the flag
		return flag;

	}
}