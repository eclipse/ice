/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015- UT-Battelle, LLC.
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

import java.util.ArrayList;

/**
 * The VizResource is used for loading a data set that should be visualized in
 * one of the platform's many visualization services. It is significantly
 * different than the base resource in that it can track multiple files that
 * collectively define a single resource as well as whether or not that resource
 * is a local or remote file.
 * 
 * @author Matthew Wang, Jay Jay Billings, Taylor Patterson
 * 
 */

public interface IVizResource extends IResource {

	/**
	 * Return any possible children VizResources.
	 * 
	 * @return A list containing child viz resources.
	 */
	ArrayList<IVizResource> getChildrenResources();

	/**
	 * Mutator for the file set
	 * 
	 * @param fileSet
	 *            The list of file names to add to the file set
	 */
	void setFileSet(String[] fileSet);

	/**
	 * Set the fileSetTitle
	 * 
	 * @param title
	 *            The String to use as the file set ID
	 */
	void setFileSetTitle(String title);

	/**
	 * Accessor for the file set
	 * 
	 * @return The list of file names in the file set
	 */
	String[] getFileSet();

	/**
	 * Accessor for the file set's title
	 * 
	 * @return The title used to identify the file set
	 */
	String getFileSetTitle();

	/**
	 * Set the remote file flag
	 * 
	 * @param name
	 *            The new name of the host. If local, use "localhost". Any other
	 *            hostname will be treated as remote.
	 */
	void setHost(String name);

	/**
	 * Retrieve the name of the host for this file.
	 * 
	 * @return The String for the name of the host machine.
	 */
	String getHost();

	/**
	 * Retrieve the flag signifying if this file is on a remote machine or not.
	 * 
	 * @return True if this resource is on a remote machine; false otherwise
	 */
	boolean isRemote();

}