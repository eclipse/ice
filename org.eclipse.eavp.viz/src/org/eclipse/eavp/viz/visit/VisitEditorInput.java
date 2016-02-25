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
package org.eclipse.eavp.viz.visit;

import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * This class implements the IEditorInput interface to provide input for the
 * VisualizationEditor. It expects to be constructed with a map that contains
 * information needed by the VisIt widget.
 * 
 * It expects the map to contain the following key value pairs:
 * 
 * username = the username
 * 
 * password = the password
 * 
 * dataType = the data type returned from VisIt, normally "image"
 * 
 * windowWidth = the width of the window
 * 
 * windowHeight = the height of the window
 * 
 * windowId = the window ID that should be shown
 * 
 * gateway = the gateway through which a remote connection will be forwarded
 * 
 * localGatewayPort = the local port on which the gateway can connect for
 * tunneling
 * 
 * useTunneling = "true" if tunneling should be used, "false" otherwise
 * 
 * url = the hostname of the remote server
 * 
 * port = the port on the remote machine
 * 
 * visDir = the directory that contains the files to be visualized
 * 
 * isRemote = "true" if this is a connection to a running service, "false"
 * otherwise
 * 
 * @author Jay Jay Billings
 * 
 */
public class VisitEditorInput implements IEditorInput {

	/**
	 * The credentials, etc. for the VisIt widget.
	 */
	HashMap<String, String> contextMap;

	/**
	 * Constructor
	 */
	public VisitEditorInput(HashMap<String, String> inputMap) {
		// Make sure the resource is legit before storing it
		assert inputMap != null;
		contextMap = inputMap;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	@Override
	public String getName() {
		return "VisIt Viewer: Window " + contextMap.get("windowid");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return "VisIt Viewer: Window " + contextMap.get("windowid");
	}

	/**
	 * Check if an instance of this class is equal to the input object.
	 * 
	 * @param otherInput
	 *            The other object to compare this to
	 * @return True if this object equals the input: false otherwise.
	 */
	@Override
	public boolean equals(Object otherInput) {

		// Just return if the references area equal
		if (this == otherInput) {
			return true;
		}

		// Check the other object to make sure it is valid.
		if (!(otherInput instanceof VisitEditorInput)) {
			return false;
		}

		// Check the ICEResource otherwise
		VisitEditorInput otherVizInput = (VisitEditorInput) otherInput;
		return contextMap == otherVizInput.contextMap;
	}

	/**
	 * Compute and return the hash code for instances of this object
	 */
	@Override
	public int hashCode() {

		// Call Object#hashCode()
		int hash = super.hashCode();

		// Compute the hash code from this object's data
		hash = 31 * hash + (null == contextMap ? 0 : contextMap.hashCode());

		// Return the computed hash code
		return hash;
	}

	/**
	 * This operation returns the input map.
	 * 
	 * @return The map of input parameters to configure the VisIt widget
	 */
	public HashMap<String, String> getInputMap() {
		return (HashMap<String, String>) contextMap.clone();
	}
}
