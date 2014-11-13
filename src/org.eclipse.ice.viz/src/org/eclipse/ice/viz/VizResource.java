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
package org.eclipse.ice.viz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.ice.datastructures.resource.ICEResource;

/**
 * The VizResource is used for loading a data set used in the CSV Plotting
 * 
 * @author Matthew Wang
 * 
 */
public class VizResource extends ICEResource {

	/**
	 * The file set
	 */
	private String[] fileSet;

	/**
	 * The fileSet title
	 */
	private String fileSetTitle;

	/**
	 * Reference to possible children VizResources
	 */
	private ArrayList<VizResource> childrenResources;
	
	/**
	 * The String representation of the hostname where the resource resides.
	 */
	private String host;

	/**
	 * Comprehensive constructor
	 */
	public VizResource() {

		// Call ICEResource's constructor
		super();

		// Set the subclass fields
		fileSet = null;
		fileSetTitle = null;
	}

	/**
	 * An alternative constructor that takes a File as input.
	 * 
	 * @param resourceFile
	 *            The File to set as the VizResources contents.
	 * @throws IOException
	 */
	public VizResource(File resourceFile) throws IOException {

		// Call ICEResource's constructor
		super(resourceFile);

		// Set the subclass fields
		fileSet = null;
		fileSetTitle = null;
	}

	/**
	 * The Constructor. This allows for the creation 
	 * of a VizResource that is composed of other VizResources.
	 * 
	 * @param resourceFile
	 * @param children
	 * @throws IOException
	 */
	public VizResource(File resourceFile, ArrayList<VizResource> children) throws IOException {
		this(resourceFile);
		childrenResources = children;
		
	}
	
	/**
	 * Return any possible children VizResources.
	 * 
	 * @return
	 */
	public ArrayList<VizResource> getChildrenResources() {
		return childrenResources;
	}
	
	/**
	 * Mutator for the file set
	 * 
	 * @param fileSet
	 *            The list of file names to add to the file set
	 */
	public void setFileSet(String[] fileSet) {
		this.fileSet = fileSet;
	}

	/**
	 * Set the fileSetTitle
	 * 
	 * @param title
	 *            The String to use as the file set ID
	 */
	public void setFileSetTitle(String title) {
		fileSetTitle = title;
	}

	/**
	 * Accessor for the file set
	 * 
	 * @return The list of file names in the file set
	 */
	public String[] getFileSet() {
		return this.fileSet;
	}

	/**
	 * Accessor for the file set's title
	 * 
	 * @return The title used to identify the file set
	 */
	public String getFileSetTitle() {
		return this.fileSetTitle;
	}

	/**
	 * Set the remote file flag
	 * 
	 * @param val
	 *            The boolean value to set resmoteFileFlag to
	 */
	public void setHost(String name) {
		host = name;
		return;
	}

	/**
	 * Retrieve the name of the host for this file.
	 * 
	 * @return The String for the name of the host machine.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Retrieve the flag signifying if this file is on a remote machine or not.
	 * 
	 * @return True if this resource is on a remote machine; false otherwise
	 */
	public boolean isRemote() {
		return !"localhost".equals(host);
	}

}
