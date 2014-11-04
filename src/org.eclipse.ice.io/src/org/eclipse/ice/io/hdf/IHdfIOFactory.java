/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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

import java.util.List;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

/**
 * <p>
 * This is an interface for factories that can write and read objects to and
 * from HDF5 files.
 * </p>
 * <p>
 * Bundles that write or read HDF files for data structures should implement
 * this interface or extend the basic implementation, {@link HdfIOFactory}
 * (which contains several useful methods for handling HDF5 files), and provide
 * the {@link IHdfIOFactory} service via OSGi so that the factory is registered
 * with the {@link IHdfIORegistry}.
 * </p>
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IHdfIOFactory {

	/**
	 * Gets the supported classes for this IHDFIOFactory. The classes returned
	 * can be written and read using this factory. Each class should have its
	 * own unique tag String.
	 * 
	 * @return A Map containing the supported classes and their associated tags.
	 */
	public List<Class<?>> getSupportedClasses();

	/**
	 * Gets a tag String for a supported class returned via
	 * {@link #getSupportedClasses()}. All classes should have a String tag that
	 * will be written to the file.
	 * 
	 * @param supportedClass
	 *            The supported class.
	 * @return A tag String uniquely associated with the supported class.
	 */
	public String getTag(Class<?> supportedClass);

	/**
	 * Writes an object to an HDF5 file specified by a URI.
	 * 
	 * @param parentGroupId
	 *            The HDF5 file ID for the parent group containing the object.
	 * @param object
	 *            An object to write to HDF5. If the object type is not
	 *            supported by the handler, nothing is written.
	 */
	public void write(int parentGroupId, Object object)
			throws NullPointerException, HDF5Exception, HDF5LibraryException;

	/**
	 * Reads an object from an HDF5 file specified by a URI.
	 * 
	 * @param groupId
	 *            The HDF5 file ID for the group containing the object.
	 * @param tag
	 *            The tag Attribute for the group. This specifies the type of
	 *            object to be read from the file.
	 * @return An object read in from the HDF5, or null if the type is not
	 *         supported by the handler.
	 */
	public Object read(int groupId, String tag) throws NullPointerException,
			HDF5Exception, HDF5LibraryException;
}
