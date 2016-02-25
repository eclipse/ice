/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
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
package org.eclipse.eavp.viz.service.datastructures.resource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizObject;

/**
 * <p>
 * The Resource class represents persistent data resources used by ICE and the
 * other software packages with which it interacts. This includes files
 * containing simulation input and output data, movies and plots, amongst
 * others.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public interface IResource extends IVizObject {

	/**
	 * <p>
	 * This operation returns the last modification date of the file.
	 * </p>
	 * 
	 * @return <p>
	 *         The date.
	 *         </p>
	 */
	String getLastModificationDate();

	/**
	 * <p>
	 * This operations returns the contents of the Resource as an instance of
	 * File.
	 * </p>
	 * 
	 * @return <p>
	 *         The file.
	 *         </p>
	 */
	File getContents();

	/**
	 * <p>
	 * This operation returns the URI to the Resource.
	 * </p>
	 * 
	 * @return <p>
	 *         The path as a URL.
	 *         </p>
	 */
	URI getPath();

	/**
	 * <p>
	 * This operation sets the path to the Resource and is an alternative to
	 * setContents(). It will reset the File handle if it is different.
	 * </p>
	 * 
	 * @param path
	 */
	void setPath(URI path);

	/**
	 * <p>
	 * This operation associates a set of Entries with the resource that
	 * describe specific properties. The list of Entries is returned by
	 * reference and is not a deep copy, i.e. - changing one will change it on
	 * the resource.
	 * </p>
	 * 
	 * @return <p>
	 *         The properties or null if there are no properties.
	 *         </p>
	 */
	ArrayList<VizEntry> getProperties();

	/**
	 * <p>
	 * This operation returns the set of Entries that describe specific
	 * properties of the resource. The properties can be set multiple times.
	 * </p>
	 * 
	 * @param props
	 *            <p>
	 *            The properties.
	 *            </p>
	 */
	void setProperties(ArrayList<VizEntry> props);

	/**
	 * <p>
	 * This operation returns true if the ICEResource is an image and false if
	 * not based upon the isPicture attribute.
	 * </p>
	 * 
	 * @return <p>
	 *         True if this is a picture, false otherwise.
	 *         </p>
	 */
	boolean isPictureType();

	/**
	 * <p>
	 * An operation that sets the isPicture attribute on ICEResource.
	 * </p>
	 * 
	 * @param isPicture
	 *            <p>
	 *            Determines if ICEResource is a picture.
	 *            </p>
	 */
	void setPictureType(boolean isPicture);

	/**
	 * <p>
	 * This operation sets the File which the Resource represents. The default
	 * values of the name, id and description for this class are the filename, 1
	 * and the absolute path, respectively.
	 * </p>
	 * 
	 * @param resourceFile
	 *            <p>
	 *            The file that the Resource should be created to represent.
	 *            </p>
	 * @throws IOException
	 * @throws NullPointerException
	 */
	void setContents(File resourceFile) throws IOException,
			NullPointerException;

}