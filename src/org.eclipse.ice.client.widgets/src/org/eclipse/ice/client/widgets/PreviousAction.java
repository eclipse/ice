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
package org.eclipse.ice.client.widgets;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * <p>
 * This class is an Action that sets the selection to the previous item in a
 * ICEResourceView.
 * </p>
 * 
 * @author Taylor Patterson
 */
public class PreviousAction extends Action {

	/**
	 * <p>
	 * The PlayableViewPart that owns an object of this class.
	 * </p>
	 */
	private PlayableViewPart viewer;

	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 * @param parent
	 *            <p>
	 *            The ViewPart to whom the object of this class belongs.
	 *            </p>
	 */
	public PreviousAction(PlayableViewPart parent) {

		// Set the calling class to the local ViewPart field
		viewer = parent;

		// Set the "hover" text
		this.setText("Previous");

		// Set the button image
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "previous.gif");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		this.setImageDescriptor(imageDescriptor);

		return;
	}

	/**
	 * <p>
	 * The function called whenever the button is clicked.
	 * </p>
	 */
	public void run() {
		if (viewer.isPlayable()) {
			viewer.setToPreviousResource();
		}
	}
}