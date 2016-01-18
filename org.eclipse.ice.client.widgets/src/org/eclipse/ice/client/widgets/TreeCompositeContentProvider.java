/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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

import java.util.List;
import java.util.Map;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * This class is a content provider for the ICE TreeComposite data structure.
 * 
 * @author Jay Jay Billings, Jordan H. Deyton
 * 
 */
public class TreeCompositeContentProvider implements ILazyTreeContentProvider {

	/**
	 * The current TreeViewer handled by this content provider.
	 */
	private TreeViewer treeViewer;

	/**
	 * A map of Components displayed in the tree. This map can be used to
	 * quickly find the parent TreeComposite for an element in the tree.
	 */
	private final Map<Component, TreeComposite> parentMap;

	/**
	 * The TreeCompositeViewer that is using this content provider to fill out
	 * the {@link #treeViewer}. It maintains meta data about the TreeComposite
	 * displayed in the TreeViewer.
	 */
	private final TreeCompositeViewer treeCompositeViewer;

	/**
	 * The default constructor.
	 * 
	 * @param treeCompositeViewer
	 *            The TreeCompositeViewer that contains the TreeViewer managed
	 *            by this ILazyTreeContentProvider.
	 * @param parentMap
	 *            A meta data collection mapping all data nodes to their parent
	 *            TreeComposites in the TreeViewer.
	 */
	public TreeCompositeContentProvider(TreeCompositeViewer treeCompositeViewer,
			Map<Component, TreeComposite> parentMap) {

		treeViewer = null;
		this.treeCompositeViewer = treeCompositeViewer;
		this.parentMap = parentMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		// TODO Review the logic here...

		// If the viewer has changed, we will need to unregister from the old
		// viewer and register with the new one.
		if (viewer != treeViewer) {
			// Set the reference to the viewer.
			treeViewer = (TreeViewer) viewer;
		}

		return;
	}

	// ---- Implements ILazyTreeContentProvider ---- //
	/**
	 * This operation returns the parent of a given TreeComposite or a Component
	 * that is a child data node of a TreeComposite.
	 */
	@Override
	public Object getParent(Object element) {

		Object parent = null;

		// If the element is a TreeComposite, we can get its parent directly by
		// calling its getParent() method.
		if (element instanceof TreeComposite) {
			parent = ((TreeComposite) element).getParent();
		}
		// If the element is a Component, we need to look it up in the map.
		else if (element instanceof Component) {
			parent = parentMap.get(element);
		}
		return parent;
	}

	/**
	 * <p>
	 * Updates the element at the specified index in the parent element's list
	 * of children.
	 * </p>
	 * <p>
	 * A TreeComposite's children are listed and ordered in the TreeViewer like
	 * so:
	 * </p>
	 * <ol>
	 * <li>All child data nodes. These are Components that are <i>not</i>
	 * TreeComposites.</li>
	 * <li>All child TreeComposites.</li>
	 * </ol>
	 * <p>
	 * Children that have not been set up (e.g., its name has not been changed
	 * from the default, <code>"ICE Object"</code>) are not included.
	 * </p>
	 * <p>
	 * This method needs to call {@link TreeViewer#replace(Object, int, Object)}
	 * and {@link TreeViewer#setChildCount(Object, int)} for the child at the
	 * specified index.
	 * </p>
	 */
	@Override
	public void updateElement(Object parent, int index) {

		// Make sure the parent element is a TreeComposite and cast it if so.
		if (parent instanceof TreeComposite) {
			TreeComposite tree = (TreeComposite) parent;

			// Get the list of the TreeComposite's children.
			List<TreeComposite> children = treeCompositeViewer.loadTree(tree);

			// TODO I'm not yet sure why, but the workbench keeps calling this
			// method with invalid indices. We should look into this a little
			// more to see if we are setting something to an incorrect value.
			if (index < children.size()) {
				// Get the child data node or TreeComposite at the specified
				// index and put it in the TreeViewer.
				TreeComposite child = children.get(index);
				treeViewer.replace(parent, index, child);

				// Update the child's count if possible. Data nodes have no
				// children, but TreeComposites can have multiple children.
				int childCount = 0;
				TreeComposite childTree = child;

				// Get the number of children of the updated child.
				childCount = childTree.getNumberOfChildren();

				// Check the child TreeComposite if it is "active".

				// Update the number of children for the child element.
				treeViewer.setChildCount(child, childCount);
			}
		}

		return;
	}

	/**
	 * Updates the number of children for a particular element in the tree.
	 * TreeComposites have a set of children that includes child TreeComposites
	 * and data nodes (Components that are not TreeComposites).<br>
	 * <br>
	 * This method needs to call {@link TreeViewer#setChildCount(Object, int)}
	 * if the current child count for the specified element changes.
	 */
	@Override
	public void updateChildCount(Object element, int currentChildCount) {

		// By default, elements should have no children.
		int childCount = 0;

		// TreeComposites have a set of data nodes (just Components) and child
		// TreeComposites.
		if (element instanceof TreeComposite) {
			childCount = treeCompositeViewer.loadTree((TreeComposite) element)
					.size();
		}
		// If the child count has changed, we need to update the tree viewer's
		// child count for the tree element.
		if (childCount != currentChildCount) {
			treeViewer.setChildCount(element, childCount);
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// Nothing to dispose yet.
	}
	// --------------------------------------------- //

}
