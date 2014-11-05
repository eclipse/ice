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
package org.eclipse.ice.client.common;

import java.util.Map;

import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;

/**
 * This class manages the check states of elements in the
 * {@link TreeCompositeViewer}'s TreeViewer, which is a
 * {@link CheckboxTreeViewer}. It also listens to the TreeViewer as an
 * {@link ICheckStateListener} so that it can update the TreeViewer's input
 * (TreeComposites and their data nodes) when the corresponding widgets are
 * checked or unchecked.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class TreeCompositeCheckStateProvider implements ICheckStateProvider,
		ICheckStateListener {

	/**
	 * The current TreeViewer handled by this content provider.
	 */
	private final CheckboxTreeViewer treeViewer;

	/**
	 * A map of Components displayed in the tree. This map can be used to
	 * quickly find the parent TreeComposite for an element in the tree.
	 */
	private final Map<Component, TreeComposite> parentMap;

	/**
	 * The default constructor.
	 * 
	 * @param treeViewer
	 *            The CheckBoxTreeViewer managed by this ICheckStateProvider.
	 * @param parentMap
	 *            A meta data collection mapping all data nodes to their parent
	 *            TreeComposites in the TreeViewer.
	 */
	public TreeCompositeCheckStateProvider(CheckboxTreeViewer treeViewer,
			Map<Component, TreeComposite> parentMap) {

		this.treeViewer = treeViewer;
		this.parentMap = parentMap;

		// Register with the TreeViewer as a CHeckStateListener. This is so that
		// the TreeComposites and data nodes can be set as active based on their
		// items' check states.
		treeViewer.addCheckStateListener(this);
	}

	// ---- Implements ICheckStateProvider ---- //
	/**
	 * Returns whether or not an item in the tree should be checked. This is in
	 * addition to the ICheckStateListener methods that make sure the tree
	 * elements are updated when the corresponding widgets are updated.
	 */
	public boolean isChecked(Object element) {
		boolean checked = false;

		// For TreeComposites, the item is checked if the TreeComposite is set
		// as active.
		if (element instanceof TreeComposite) {
			checked = ((TreeComposite) element).isActive();
		}
		// For child data nodes, the item is checked if it is the active node.
		else if (element instanceof Component) {
			// FIXME - If the TreeCompositeViewer is only populated by
			// TreeComposites, then this logic is no longer necessary.

			// Get the checked/unchecked Component and its parent.
			Component component = (Component) element;
			TreeComposite parent = parentMap.get(component);

			// Determine if the component should be active.
			if (parent != null && component == parent.getActiveDataNode()) {
				checked = true;
			}
		}

		return checked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ICheckStateProvider#isGrayed(java.lang.Object)
	 */
	public boolean isGrayed(Object element) {
		// Nothing should be grayed out.
		return false;
	}

	// ---------------------------------------- //

	// ---- Implements ICheckStateListener ---- //
	/**
	 * When an element in the TreeViewer is checked, the corresponding model
	 * must also be updated. TreeComposites should just be set as active. If a
	 * data node (a regular Component) is set as active, then all of its
	 * ancestors must also be set to active.
	 */
	public void checkStateChanged(CheckStateChangedEvent event) {

		Object element = event.getElement();
		boolean checked = event.getChecked();

		// Mark checked TreeComposites as active.
		if (element instanceof TreeComposite) {
			((TreeComposite) element).setActive(checked);
		}
		// Mark checked Components and their parents as active.
		else if (element instanceof Component) {
			// FIXME - If the TreeCompositeViewer is only populated by
			// TreeComposites, then this logic is no longer necessary.

			// Get the checked/unchecked Component.
			Component component = (Component) element;

			// Get the parent and set it active or inactive.
			TreeComposite parent = parentMap.get(component);
			parent.setActive(checked);

			// If the parent is active, set the active data node and update all
			// ancestors as checked.
			if (checked) {
				parent.setActiveDataNode(component);
				// Update the parent's element in the TreeViewer.

				// Make sure all of the ancestors are updated.
				TreeComposite grandParent = null, ancestor = parent;
				while ((grandParent = ancestor.getParent()) != null) {
					grandParent.setActive(true);
					ancestor = grandParent;
				}
			}
		}

		return;
	}
	// ---------------------------------------- //
}
