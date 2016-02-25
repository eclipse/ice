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
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.eavp.viz.service.datastructures.VizObject.VizObject;

/**
 * The VizResource is used for loading a data set that should be visualized in
 * one of the platform's many visualization services. It is significantly
 * different than the base resource in that it can track multiple files that
 * collectively define a single resource as well as whether or not that resource
 * is a local or remote file.
 * 
 * @author Scott Forest Hull II, Matthew Wang, Jay Jay Billings, Taylor
 *         Patterson
 * 
 */

@XmlRootElement(name = "VizResource")
@XmlAccessorType(XmlAccessType.FIELD)
public class VisualizationResource extends VizObject implements IVizResource {

	/**
	 * <p>
	 * A File reference to the Resource.
	 * </p>
	 * 
	 */
	@XmlAttribute()
	private File file;
	/**
	 * <p>
	 * The path to the file as a URI.
	 * </p>
	 * 
	 */
	@XmlAttribute()
	private URI path;

	/**
	 * <p>
	 * The set of properties associated with this resource.
	 * </p>
	 * 
	 */
	@XmlElement(name = "Properties")
	private ArrayList<VizEntry> properties;

	/**
	 * <p>
	 * An attribute that determines if ICEResouce is a picture or not. Can be
	 * set multiple times.
	 * </p>
	 * 
	 */
	@XmlAttribute
	private boolean isPicture;

	/**
	 * The set of files contained associated with this resource
	 */
	private String[] fileSet;

	/**
	 * The fileSet title
	 */
	private String fileSetTitle = "No title";

	/**
	 * Reference to possible children VizResources
	 */
	@XmlAnyElement()
	@XmlElementRef(name = "VisualizationResource", type = VisualizationResource.class)
	private ArrayList<IVizResource> childrenResources;

	/**
	 * The String representation of the hostname where the resource resides. By
	 * default it assumes localhost.
	 */
	private String host = "localhost";

	/**
	 * <p>
	 * The default constructor. If this constructor is used, a second call to
	 * setContents() must be made.
	 * </p>
	 * 
	 */
	public VisualizationResource() {

		super();

		// Set the particulars
		setName(null);
		setDescription(null);
		file = null;
		path = null;

		// Setup properties list
		properties = new ArrayList<VizEntry>();

		// Default is set to false for isPicture
		isPicture = false;

		// Setup the list
		childrenResources = new ArrayList<IVizResource>();

		return;

	}

	/**
	 * <p>
	 * An alternative constructor that takes the File as an argument. The
	 * default values of the name, id and description for this class are the
	 * filename, 1 and the absolute path, respectively. It is the same as
	 * calling the no-arg constructor followed by calling setContents().
	 * </p>
	 * 
	 * @param resourceFile
	 *            <p>
	 *            The file that the Resource should be created to represent.
	 *            </p>
	 * @throws IOException
	 */
	public VisualizationResource(File resourceFile) throws IOException {

		super();

		// Set the particulars
		setName(resourceFile.getName());
		setId(1);
		setDescription(resourceFile.getAbsolutePath());

		// Set the file properties
		setContents(resourceFile);

		// Setup properties list
		properties = new ArrayList<VizEntry>();

		// Default is set to false for isPicture
		isPicture = false;

		// Setup the list
		childrenResources = new ArrayList<IVizResource>();

		return;

	}

	/**
	 * The Constructor. This allows for the creation of a VizResource that is
	 * composed of other VizResources.
	 * 
	 * @param resourceFile
	 * @param children
	 * @throws IOException
	 */
	public VisualizationResource(File resourceFile,
			ArrayList<IVizResource> children) throws IOException {
		this(resourceFile);
		childrenResources = children;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.resource.IResource#getLastModificationDate
	 * ()
	 */
	public String getLastModificationDate() {

		// Local Declarations
		Date fileDate = null;
		String retVal = "0";

		// Get the modification date if the File is good
		if (file != null) {
			fileDate = new Date(file.lastModified());
			retVal = fileDate.toString();
		}

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.resource.IResource#getContents()
	 */
	public File getContents() {

		return file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.resource.IResource#getPath()
	 */
	public URI getPath() {
		if (this.file != null) {
			path = file.toURI();
		}
		return path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.resource.IResource#setPath(java.net.URI)
	 */
	public void setPath(URI path) {

		// If null, return
		if (path == null) {
			return;
		}

		// Set the path and create a new file
		this.path = path;
		// If the URI differs from the current file, change it
		if (!this.file.toURI().equals(path)) {
			this.file = new File(path);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.resource.IResource#getProperties()
	 */
	public ArrayList<VizEntry> getProperties() {

		return this.properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.resource.IResource#setProperties(java.
	 * util.ArrayList)
	 */
	public void setProperties(ArrayList<VizEntry> props) {
		// If null return
		if (props == null) {
			return;
		}

		this.properties = props;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.resource.IResource#isPictureType()
	 */
	public boolean isPictureType() {
		return this.isPicture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.resource.IResource#setPictureType(boolean)
	 */
	@XmlTransient
	public void setPictureType(boolean isPicture) {
		this.isPicture = isPicture;

	}

	/**
	 * This operation performs a deep copy of the attributes of another
	 * VizResource into the current VizResource.
	 * 
	 * @param otherResource
	 *            The other VizResource from which information should be copied.
	 */
	public void copy(VisualizationResource otherResource) {

		// if resource is null, return
		if (otherResource == null) {
			return;
		}

		// copy from super class
		super.copy(otherResource);

		// Copy current values
		// These files are not cloned
		this.file = otherResource.file;
		this.path = otherResource.path;

		// Iteratively clone the entries in properties
		// These items are cloned
		this.properties.clear();
		for (int i = 0; i < otherResource.properties.size(); i++) {
			this.properties.add((VizEntry) otherResource.getProperties().get(i)
					.clone());
		}

		// Copy picture
		this.isPicture = otherResource.isPicture;

		// Copy everything else we need
		fileSetTitle = otherResource.fileSetTitle;
		host = otherResource.host;
		childrenResources.clear();
		childrenResources = (ArrayList<IVizResource>) otherResource.childrenResources
				.clone();
		fileSet = Arrays.copyOf(otherResource.fileSet,
				otherResource.fileSet.length);

	}

	/**
	 * This operation provides a deep copy of the VizResource.
	 * 
	 * @return A clone of the VizResource.
	 */
	@Override
	public Object clone() {

		// Create a new instance of resource and copy contents
		VisualizationResource resource = null;
		resource = new VisualizationResource();
		resource.copy(this);

		return resource;
	}

	/**
	 * This operation is used to check equality between the ICEResource and
	 * another ICEResource. It returns true if the ICEResources are equal and
	 * false if they are not.
	 * 
	 * @param otherICEResource
	 *            The other ICEResource to which this ICEResource should be
	 *            compared.
	 * @return True if the ICEResources are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Default the return value to fail
		boolean retVal = false;

		// Check if they are same reference in memory
		if (this == otherObject) {
			// If so, return true, saves time
			return true;
		}

		// We're only interested if the types match and the resource exists
		if (otherObject instanceof VisualizationResource && otherObject != null) {
			VisualizationResource otherVizResource = (VisualizationResource) otherObject;

			// Make sure all the identity information, etc., matches.
			retVal = super.equals(otherVizResource);

			// Check that their attributes are the same
			retVal &= (file.equals(otherVizResource.file))
					&& (path.equals(otherVizResource.path)
							&& properties.equals(otherVizResource.properties) && (isPicture == otherVizResource.isPicture));
			// Check the children resources if they both exists
			retVal &= childrenResources
					.equals(otherVizResource.childrenResources);
			// Do the same check for the file sets
			if (fileSet != null && otherVizResource.fileSet != null) {
				// The files must be checked one by one because [] only checks
				// reference equality, not element equality.
				if (fileSet.length == otherVizResource.fileSet.length) {
					for (int i = 0; i < fileSet.length; i++) {
						retVal &= fileSet[i]
								.equals(otherVizResource.fileSet[i]);
					}
				} else {
					retVal = false;
				}
			} else if ((fileSet != null && otherVizResource.fileSet == null)
					|| (fileSet == null && otherVizResource.fileSet != null)) {
				// If one of the arrays of file sets is null, but the
				// other isn't, they are not equal.
				retVal = false;
			}
			// Check the host and title
			retVal &= (host.equals(otherVizResource.host))
					&& (fileSetTitle.equals(otherVizResource.fileSetTitle));
		}

		return retVal;

	}

	/**
	 * This operation returns the hashcode value of the ICEObject.
	 * 
	 * @return The hashcode for the ICEResource.
	 */
	@Override
	public int hashCode() {

		// Local Declaration
		int hash = 11;

		// Compute hashcode from ICEResource data
		hash = 31 * hash + super.hashCode();
		hash = 31 * hash + (null == this.file ? 0 : this.file.hashCode());
		hash = 31 * hash + (null == this.path ? 0 : this.path.hashCode());
		// hash = 31 * hash
		// + (null == this.file.toString() ? 0 : this.file.hashCode());
		// hash = 31 * hash
		// + (null == this.path.toString() ? 0 : this.path.hashCode());
		hash = 31 * hash + this.properties.hashCode();
		hash = 31 * hash + (!this.isPicture ? 0 : 1);

		// Compute hashcode from ICEResource data
		hash = 31 * hash + childrenResources.hashCode();

		return hash;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.resource.IResource#setContents(java.io
	 * .File)
	 */
	@XmlTransient
	public void setContents(File resourceFile) throws IOException,
			NullPointerException {

		// Set the file reference and path
		try {
			file = resourceFile;
			path = resourceFile.toURI();
		} catch (NullPointerException e) {
			throw e;
		}

		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.resource.IVizResource#getChildrenResources
	 * ()
	 */
	public ArrayList<IVizResource> getChildrenResources() {
		return childrenResources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.resource.IVizResource#setFileSet(java.
	 * lang.String[])
	 */
	public void setFileSet(String[] fileSet) {
		for (String i : fileSet)
			logger.info(i);
		this.fileSet = fileSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.resource.IVizResource#setFileSetTitle(
	 * java.lang.String)
	 */
	public void setFileSetTitle(String title) {
		fileSetTitle = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.resource.IVizResource#getFileSet()
	 */
	public String[] getFileSet() {
		return this.fileSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.resource.IVizResource#getFileSetTitle()
	 */
	public String getFileSetTitle() {
		return this.fileSetTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.resource.IVizResource#setHost(java.lang
	 * .String)
	 */
	public void setHost(String name) {
		host = name;
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.resource.IVizResource#getHost()
	 */
	public String getHost() {
		return host;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.resource.IVizResource#isRemote()
	 */
	public boolean isRemote() {
		return !"localhost".equals(host);
	}

}
