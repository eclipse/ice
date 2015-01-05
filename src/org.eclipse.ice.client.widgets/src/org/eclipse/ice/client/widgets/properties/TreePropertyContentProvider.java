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
package org.eclipse.ice.client.widgets.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.SelectiveComponentVisitor;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.PlatformUI;

/**
 * This is the content provider for the {@link TableViewer} of
 * {@link TreeProperty}s. When the associated {@link TreeComposite} is updated,
 * the <code>TableViewer</code> should also be refreshed accordingly.
 * <p>
 * The input of the <code>TableViewer</code> that is using this content provider
 * is expected to be a single <code>TreeComposite</code>.
 * </p>
 * 
 * @author Jordan H. Deyton
 * 
 */
public class TreePropertyContentProvider implements IStructuredContentProvider,
		IUpdateableListener {

	/**
	 * The <code>TableViewer</code> managed by this content provider. Its input
	 * is expected to be a single <code>TreeComposite</code>.
	 */
	private TableViewer tableViewer;

	/**
	 * The <code>TreeComposite</code> whose properties are exposed in the
	 * {@link #tableViewer}.
	 */
	private TreeComposite tree;

	// ---- Implements IStructuredContentProvider ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.BaseLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
		// Clear the references and any other variables.
		tableViewer = null;
		tree = null;

		return;
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

		// Update the reference to the TableViewer.
		if (viewer instanceof TableViewer) {
			tableViewer = (TableViewer) viewer;
		}

		// We need to do slightly different operations depending on the type of
		// tree that is set as input.
		if (newInput != null && newInput instanceof Component) {
			IComponentVisitor visitor = new SelectiveComponentVisitor() {
				@Override
				public void visit(TreeComposite component) {

					// If the visited tree is new, we need to replace the
					// current tree with the new one.
					if (tree != component) {
						// If possible, unregister from the current tree.
						if (tree != null) {
							tree.unregister(TreePropertyContentProvider.this);
						}

						// Set the reference to the new tree and register for
						// updates. We need the updates for keeping the type
						// TableViewer updated.
						tree = component;
						tree.register(TreePropertyContentProvider.this);
					}

					return;
				}

				@Override
				public void visit(AdaptiveTreeComposite component) {
					// For adaptive trees, do the same thing.
					visit((TreeComposite) component);
				}
			};
			((Component) newInput).accept(visitor);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {

		Object[] elements = null;

		if (inputElement != null && inputElement == tree) {
			final AtomicInteger id = new AtomicInteger();
			// Compile a list of all Entries for all DataComponents stored in
			// this TreeComposite. For convenience, we store them in
			// TreeProperty instances.
			final List<TreeProperty> properties = new ArrayList<TreeProperty>();
			IComponentVisitor visitor = new SelectiveComponentVisitor() {
				@Override
				public void visit(DataComponent component) {
					TreeProperty property;
					for (Entry entry : component.retrieveReadyEntries()) {
						property = new TreeProperty(id.getAndIncrement(), tree,
								component, entry);
						properties.add(property);
					}
				}
			};
			for (Component component : tree.getDataNodes()) {
				component.accept(visitor);
			}
			// Convert the list to an array for the return value.
			elements = properties.toArray();
		}

		return elements;
	}

	// ----------------------------------------------- //

	// ---- Implements IUpdateableListener ---- //
	/**
	 * If the current {@link #tree} has updated, we need to refresh the
	 * {@link #tableViewer} to reflect the changes to the tree.
	 */
	@Override
	public void update(IUpdateable component) {
		// We need to update if the tree or any of its DataComponent data nodes
		// are updated.
		if (component != null) {
			// We could be smart and just refresh the elements that have
			// changed, but just refreshing the entire table works reasonably
			// well enough and is a simple solution.
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					tableViewer.refresh();
				}
			});
		}

		return;
	}
	// ---------------------------------------- //

}
