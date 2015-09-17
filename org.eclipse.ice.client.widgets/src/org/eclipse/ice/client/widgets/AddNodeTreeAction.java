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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.eclipse.core.internal.runtime.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.eclipse.ui.dialogs.ListDialog;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This class provides a JFace <code>Action</code> for adding
 * {@link TreeComposite}s to a selected <code>TreeComposite</code> based on the
 * latter's list of child exemplars.
 * 
 * @author Jordan H. Deyton
 * 
 * @see AbstractTreeAction
 */
public class AddNodeTreeAction extends AbstractTreeAction {

	/**
	 * The default constructor.
	 */
	public AddNodeTreeAction() {

		// Set the text and tool tip.
		setText("Add Child");
		setToolTipText("Add a child to the selected node.");

		// Set the image to be the green plus button.
		Bundle bundle = FrameworkUtil.getBundle(AddNodeTreeAction.class);
		Path imagePath = new Path("icons" + System.getProperty("file.separator") + "add.png");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		setImageDescriptor(ImageDescriptor.createFromURL(imageURL));

		return;
	}

	/**
	 * Overrides the default behavior to additionally check that the selected
	 * tree node has child exemplars.
	 */
	@Override
	protected boolean canRun() {
		return canAddNode(getSelectedNode());
	}

	/**
	 * If the <code>Action</code> can be run, this prompts the user to add a new
	 * <code>TreeComposite</code> to the currently selected tree node from its
	 * list of child exemplar <code>TreeComposite</code>s.
	 */
	@Override
	public void run() {
		addToNode(getSelectedNode());
	}

	/**
	 * Determines whether or not a child node can be added to a TreeComposite.
	 * 
	 * @param tree
	 *            The TreeComposite that may be able to have children.
	 * @return Whether or not a child node can be added to the tree.
	 */
	protected boolean canAddNode(TreeComposite tree) {
		return tree != null && tree.hasChildExemplars();
	}

	/**
	 * Prompts the user to add a supported type of node to the specified tree.
	 * If the node does not support children, this method does nothing.
	 * 
	 * @param tree
	 *            The TreeComposite that a new node will be added to.
	 */
	protected void addToNode(TreeComposite tree) {

		if (canAddNode(tree)) {

			HashMap<String, TreeComposite> exemplarMap;
			ArrayList<TreeComposite> exemplars = null;

			// Get the exemplar children and put them in the map
			exemplars = tree.getChildExemplars();
			exemplarMap = new HashMap<String, TreeComposite>();
			// Map them by name
			for (TreeComposite exemplar : exemplars) {
				exemplarMap.put(exemplar.getName(), exemplar);
			}

			// Create a selection dialog so that they can make a choice
			IWorkbench bench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = bench.getActiveWorkbenchWindow();
			TreeNodeFilteredItemsSelectionDialog addNodeDialog = new TreeNodeFilteredItemsSelectionDialog(
					window.getShell(), true, exemplarMap.keySet());

			// Set up the Details Label Provider to return the
			// TreeComposites Description
			addNodeDialog.setDetailsLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					if (element == null) {
						return "";
					} else {
						String text = exemplarMap.get(element.toString()).getDescription();
						if (text.isEmpty()) {
							return element.toString();
						} else {
							return "\n" + text; // FIXME not sure why we need a \n...
						}
					}
				}
			});
			addNodeDialog.setInitialSelections(exemplarMap.keySet().toArray());
			addNodeDialog.setTitle("Child Selector");
			addNodeDialog.setMessage("Select a new child from the list");
			addNodeDialog.setInitialPattern("?");
			addNodeDialog.refresh();
			addNodeDialog.open();

			if (addNodeDialog.getResult() != null) {
				for (Object result : addNodeDialog.getResult()) {
					// Get the exemplar
					TreeComposite exemplar = exemplarMap.get(result);
					// Clone it. This lets you pull a sub-class of TreeComposite
					// if
					// the clone() method is overridden.
					TreeComposite child = (TreeComposite) exemplar.clone();
					// Add it to the tree
					tree.setNextChild(child);
				}
			} else {
				// Close the list dialog otherwise
				addNodeDialog.close();
			}
		}

		return;
	}

}
