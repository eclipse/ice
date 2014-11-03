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
package org.eclipse.ice.client.common;

import org.eclipse.ice.datastructures.form.TreeComposite;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This class is an action what deletes a new child of the current tree in the
 * TreeView if they are not direct descendants of the root node.
 * 
 * @author jaybilly
 * 
 */
public class DeleteNodeTreeAction extends Action implements ISelectionListener {

	/**
	 * The last selection set to this action.
	 */
	private ISelection lastSelection;

	/**
	 * The workbench window
	 */
	private final IWorkbenchWindow window;

	/**
	 * The ID of the view (typically a TreeCompositeViewer) that this Action
	 * corresponds to.
	 */
	private String partId;

	/**
	 * The default constructor.
	 */
	public DeleteNodeTreeAction() {

		// Set the default ID to the TreeCompositeViewer.
		partId = TreeCompositeViewer.ID;

		// Set the text
		setText("Delete Child");
		setToolTipText("Delete a child of this node.");

		// Create the image descriptor from the file path
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "delete_X.png");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		setImageDescriptor(imageDescriptor);

		// Register for updates from the TreeCompositeViewer
		IWorkbench bench = PlatformUI.getWorkbench();
		window = bench.getActiveWorkbenchWindow();
		window.getSelectionService().addSelectionListener(partId, this);

		return;
	}

	/**
	 * Changes the workbench part this Action listens to for selection changes.
	 * 
	 * @param partId
	 *            The ID of the part whose selections will be used by this
	 *            Action.
	 */
	public void setPartId(String partId) {

		if (partId != null && !partId.equals(this.partId)) {
			ISelectionService selectionService = window.getSelectionService();

			// Unregister from the previous part's selection.
			selectionService.removeSelectionListener(partId, this);

			// Set the partId and register for the part's selection changes.
			this.partId = partId;
			selectionService.addSelectionListener(partId, this);
		}

		return;
	}

	/**
	 * This operation runs the action.
	 */
	@Override
	public void run() {

		// Get the last selected tree composite
		Object selectedNode = ((ITreeSelection) lastSelection)
				.getFirstElement();
		TreeComposite tree = (TreeComposite) selectedNode;

		// If there was a valid selection, delete the node by getting the parent
		// and removing the child.
		if (tree != null && tree.getParent() != null) {
			tree.getParent().removeChild(tree);
		}

		return;
	}

	/**
	 * This operation handles a change in selection in the view.
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		// Local Declarations
		boolean enabled = false;

		// Check the part before saving the selection
		if (part.getSite().getId().equals(partId)) {
			lastSelection = selection;
			// Disable the button if the selection is not a TreeComposite
			Object selectedNode = ((ITreeSelection) lastSelection)
					.getFirstElement();
			if (selectedNode instanceof TreeComposite && selectedNode != null) {
				TreeComposite tree = (TreeComposite) selectedNode;
				TreeComposite parent = tree.getParent();
				// Only enable the button the node has a parent and grandparent
				if (parent != null && parent.getParent() != null) {
					enabled = true;
				}
			}
		}

		setEnabled(enabled);

		return;
	}

}
