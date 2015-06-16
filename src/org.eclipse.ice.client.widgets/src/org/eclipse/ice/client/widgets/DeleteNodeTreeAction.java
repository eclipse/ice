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
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This class provides a JFace <code>Action</code> that deletes the child of a
 * selected {@link TreeComposite} or tree node.
 * 
 * @author Jordan H. Deyton
 * 
 * @see AbstractTreeAction
 */
public class DeleteNodeTreeAction extends AbstractTreeAction {

	/**
	 * The default constructor.
	 */
	public DeleteNodeTreeAction() {

		// Set the text
		setText("Delete Child");
		setToolTipText("Delete a child of this node.");

		// Create the image descriptor from the file path
		Bundle bundle = FrameworkUtil.getBundle(DeleteNodeTreeAction.class);
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "delete_X.png");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		setImageDescriptor(ImageDescriptor.createFromURL(imageURL));

		return;
	}

	/**
	 * Overrides the default behavior to additionally check that the selected
	 * tree node has a parent. We do not want to be able to delete the root
	 * node!
	 */
	@Override
	protected boolean canRun() {
		return super.canRun() && getSelectedNode().getParent() != null;
	}

	/**
	 * If the <code>Action</code> can be run, this deletes the selected
	 * <code>TreeComposite</code> from its parent.
	 */
	@Override
	public void run() {

		if (isEnabled()) {
			TreeComposite selectedNode = getSelectedNode();
			selectedNode.getParent().removeChild(selectedNode);
		}

		return;
	}

}