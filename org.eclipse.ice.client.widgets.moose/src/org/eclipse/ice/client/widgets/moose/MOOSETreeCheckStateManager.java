/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or 
 *      initial documentation
 *    Dasha Gorin (UT-Battelle, LLC.) - code and doc cleanup
 *    Jay Jay Billings (UT-Battelle, LLC.) - fixed author tags
 *    Jordan Deyton (UT-Battelle, LLC.) - refactored from 
 *      org.eclipse.ice.client.common.TreeCompositeCheckStateProvider
 *    Jordan Deyton (UT-Battelle, LLC.) - fixed performance bug
 *    Jordan Deyton (UT-Battelle, LLC.) - checking node now checks its children
 *******************************************************************************/
package org.eclipse.ice.client.widgets.moose;

import java.util.Iterator;

import org.eclipse.january.form.BreadthFirstTreeCompositeIterator;
import org.eclipse.january.form.TreeComposite;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * The behavior of the {@link MOOSETreeCompositeView} is not standard. This
 * class provides said non-standard behavior for checking/unchecking elements in
 * the MOOSE Tree View. The list below describes the behavior of this check
 * state manager:
 * 
 * <ul>
 * <li>An element ({@link TreeComposite} is checked if and only if its active
 * field is set to true.</li>
 * <li>When a node is checked, all of its ancestor nodes should be checked.</li>
 * <li>When a node is unchecked, all of its descendant nodes should be
 * unchecked.</li>
 * </ul>
 * 
 * @author Jordan
 */
public class MOOSETreeCheckStateManager implements ICheckStateProvider,
		ICheckStateListener {

	// ---- Implements ICheckStateListener ---- //
	/**
	 * This method is called when one of the nodes in the tree is checked. For
	 * MOOSE trees, this must enforce two rules:
	 * <ul>
	 * <li>When a node is checked, all of its ancestor nodes should be checked.</li>
	 * <li>When a node is unchecked, all of its descendant nodes should be
	 * unchecked.</li>
	 * </ul>
	 */
	@Override
	public void checkStateChanged(CheckStateChangedEvent event) {

		if (event != null && event.getElement() instanceof TreeComposite) {
			// Get the checked/unchecked node.
			TreeViewer treeViewer = (TreeViewer) event.getSource();
			TreeComposite node = (TreeComposite) event.getElement();

			boolean checked = event.getChecked();

			// If checked, all ancestor nodes should also be checked. This is
			// done by setting their active flags.
			if (checked) {
				while (node != null) {
					// If the state of the node changes, we need to tell the
					// TreeViewer to refresh that element.
					if (!node.isActive()) {
						node.setActive(true);
						treeViewer.update(node, null);
					}
					node = node.getParent();
				}
			}

			// Either way, all descendant nodes should have the same check state
			// as the ancestor node when it changes.

			// Get the root node (it may have changed due to the above loop).
			node = (TreeComposite) event.getElement();

			// Get a breadth-first iterator so we can walk the tree.
			Iterator<TreeComposite> iterator;
			iterator = new BreadthFirstTreeCompositeIterator(node);
			// Loop over and deactivate the children.
			while (iterator.hasNext()) {
				node = iterator.next();
				// If the state of the node changes, we need to tell the
				// TreeViewer to refresh that element.
				if (node.isActive() != checked) {
					node.setActive(checked);
					treeViewer.update(node, null);
				}
			}
		}

		return;
	}

	// ---------------------------------------- //

	// ---- Implements ICheckStateProvider ---- //
	/**
	 * Nodes are checked if their active flag is set to true.
	 */
	@Override
	public boolean isChecked(Object element) {
		boolean checked = false;

		if (element != null && element instanceof TreeComposite) {
			checked = ((TreeComposite) element).isActive();
		}

		return checked;
	}

	@Override
	public boolean isGrayed(Object element) {
		// Nothing should be grayed out.
		return false;
	}
	// ---------------------------------------- //

}
