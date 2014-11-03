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
package org.eclipse.ice.client.common;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import org.eclipse.ice.client.widgets.ExtraInfoDialog;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.BatteryComponent;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * This class provides an Eclipse view showing the list of Items that have been
 * created and made available in the Core.
 * 
 * This class uses a private, recursive tree walking operation (mapParents) to
 * create map where the keys are the data nodes and the values are their
 * TreeComposite parents since it cannot directly mark regular components as
 * active.
 * 
 * @author Jay Jay Billings, djg
 * 
 */
public class TreeCompositeViewer extends ViewPart implements
		IUpdateableListener, IComponentVisitor,
		ITabbedPropertySheetPageContributor {

	/**
	 * The id
	 */
	public static final String ID = "org.eclipse.ice.client.common.TreeCompositeViewer";

	/**
	 * The JFace TreeViewer used to display the TreeComposite
	 */
	protected CheckboxTreeViewer treeViewer;

	/**
	 * A reference to the Viewer's parent that is passed in to
	 * createPartControl.
	 */
	private Composite viewerParent;

	/**
	 * The Action that adds a new TreeComposite node.
	 */
	protected AddNodeTreeAction addAction;
	/**
	 * The Action that deletes a selected TreeComposite node.
	 */
	protected DeleteNodeTreeAction deleteAction;

	/**
	 * The input to the TreeViewer. This should be a single TreeComposite.
	 */
	private TreeComposite inputTree;

	/**
	 * A map of Components displayed in the tree. This map can be used to
	 * quickly find the parent TreeComposite for an element in the tree.
	 */
	private final IdentityHashMap<Component, TreeComposite> parentMap;

	/**
	 * A map of TreeComposites used to cache the children of loaded
	 * TreeComposites.
	 */
	private final IdentityHashMap<TreeComposite, List<TreeComposite>> childMap;

	/**
	 * The default constructor.
	 */
	public TreeCompositeViewer() {

		inputTree = null;
		treeViewer = null;
		viewerParent = null;

		// Initialize the meta data containers.
		parentMap = new IdentityHashMap<Component, TreeComposite>();
		childMap = new IdentityHashMap<TreeComposite, List<TreeComposite>>();
	}

	/**
	 * Create the tree viewer that shows the TreeComposite for the current Form
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 *      .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Local Declarations
		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolbarManager = actionBars.getToolBarManager();
		addAction = new AddNodeTreeAction(this);
		deleteAction = new DeleteNodeTreeAction();

		// Set the parent reference
		viewerParent = parent;

		// Draw the viewer
		drawViewer();

		// Setup the toolbar
		toolbarManager.add(addAction);
		toolbarManager.add(deleteAction);

		// Set the input
		treeViewer.setInput(inputTree);

		// Register the view as a selection provider
		getSite().setSelectionProvider(treeViewer);

		return;
	}

	/**
	 * A simple pass-through for setting the focus. It does nothing.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// Nothing TODO
	}

	/**
	 * Gets the root TreeComposite displayed in the {@link #treeViewer}.
	 * 
	 * @return The root TreeComposite for all nodes displayed in the tree
	 *         viewer. May be null.
	 */
	protected TreeComposite getRoot() {
		return inputTree;
	}

	/**
	 * This operation sets the TreeComposite that should be displayed for the
	 * current FormEditor.
	 * 
	 * @param tree
	 *            The tree composite
	 */
	public void setInput(TreeComposite tree) {

		if (tree != inputTree) {
			// Unregister from the old root TreeComposite, if possible.
			if (tree != null) {
				tree.unregister(this);
			}
			// Set the reference to the new root TreeComposite.
			inputTree = tree;

			// Clear the old meta data (parent and child maps).
			clearMetaData();

			if (inputTree != null) {
				// Load the root element. This also builds the meta data
				// associated with it.
				loadTree(inputTree);
				// Register with the root element. This also has the effect of
				// registering with all of its child data nodes and,
				// recursively, all of its child TreeComposites. New and deleted
				// children are registered and unregistered automatically.
				inputTree.register(this);
			}

			// Send the updated tree to the TreeViewer.
			treeViewer.setInput(inputTree);

			// Set the name of the view
			setPartName(inputTree.getName() + " -- Tree View");
		}

		return;
	}

	/**
	 * This operation creates an adapter that allows this viewer to publish
	 * information in the properties view.
	 */
	public Object getAdapter(Class adapter) {
		// If the requested adapter is a property page, send in the one for the
		// TreeComposite.
		if (adapter == IPropertySheetPage.class) {
			return new TabbedPropertySheetPage(this);
		}
		// Otherwise bump this up to the parent.
		return super.getAdapter(adapter);
	}

	/**
	 * Disposes of any data or structures required by the viewer.
	 */
	@Override
	public void dispose() {
		// Perform the usual dispose operation.
		super.dispose();

		// Dispose of the meta data structures.
		clearMetaData();
	}

	/**
	 * This function draws the ItemViewer.
	 */
	private void drawViewer() {

		// Initialize the tableviewer
		treeViewer = new CheckboxTreeViewer(viewerParent, SWT.VIRTUAL
				| SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
				| SWT.BORDER);

		// Set and configure the content and label providers
		treeViewer.setContentProvider(new TreeCompositeContentProvider(this,
				parentMap));
		treeViewer.setCheckStateProvider(new TreeCompositeCheckStateProvider(
				treeViewer, parentMap));
		treeViewer.setLabelProvider(new TreeCompositeLabelProvider());

		// Add a double click listener to load components from the viewer
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				// Get the selection from the table viewer
				IStructuredSelection selection = (IStructuredSelection) treeViewer
						.getSelection();
				// If the selection is empty, jump ship
				if (selection.isEmpty()) {
					return;
				}
				// Get the listed of selected objects.
				List<?> list = selection.toList();
				// Get the component
				Component comp = (Component) list.get(0);
				// Visit the component to figure out what to draw
				if (comp != null) {
					comp.accept(TreeCompositeViewer.this);
				}

			}
		});

		return;
	}

	/**
	 * This methods loads a TreeComposite and all meta data associated with it.
	 * This includes caching the links between the tree and its children in both
	 * {@link #parentMap} and {@link #childMap}.
	 * 
	 * @param tree
	 *            The TreeComposite that is getting UI resources allocated.
	 * @return A list containing the data nodes (Components) and children
	 *         (TreeComposites).
	 */
	protected List<TreeComposite> loadTree(TreeComposite tree) {

		// Get the list of the TreeComposite's children.
		List<TreeComposite> children = childMap.get(tree);

		// Get the number of children we expect to have.
		int childCount = tree.getNumberOfChildren();

		// The list will be null if the tree has never been loaded before. We
		// should create the list.
		if (children == null) {
			children = new ArrayList<TreeComposite>(childCount);
			childMap.put(tree, children);
		}

		// Fill out the list of children if necessary.
		if (children.size() != childCount) {
			// Reset the list of children.
			children.clear();

			// Add all non-default child components to the list.
			for (int i = 0; i < childCount; i++) {
				TreeComposite child = tree.getChildAtIndex(i);
				if (!("ICE Object".equals(child.getName()))) {
					children.add(child);
					parentMap.put(child, tree);
				}
			}
		}

		return children;
	}

	/**
	 * Clears the meta data used to speed up synchronization between the model
	 * (a TreeComposite) and the view (a TreeViewer). This includes resetting
	 * {@link #parentMap} and {@link #childMap}.
	 */
	private void clearMetaData() {

		// Clear the map for looking up parents in the tree.
		parentMap.clear();

		// Clear out the map of TreeComposites and their children.
		for (List<TreeComposite> children : childMap.values()) {
			children.clear();
		}
		childMap.clear();
	}

	// ---- Implements IUpdateableListener ---- //
	/**
	 * This method is only called by TreeComposites and their child data nodes
	 * (Components). It updates the TreeViewer specifically for the updated
	 * element rather than, say, refreshing the entire tree.
	 * 
	 * @param component
	 *            The component (a TreeComposite or Component) that has been
	 *            updated.
	 */
	public void update(IUpdateable component) {

		// Only proceed if the component is one of the TreeComposites with
		// allocated UI resources or if it is a child of a displayed
		// TreeComposite.
		if (childMap.containsKey(component) || parentMap.containsKey(component)) {
			// Create a final reference to the object that can be used in the
			// UI thread.
			final Object element = component;
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					// To be used when a particular TreeComposite has added or
					// removed children.
					treeViewer.refresh(element);
				}
			});
		}

		return;
	}

	// ---------------------------------------- //

	// ---- Implements IComponentVisitor ---- //
	/**
	 * When called, this prompts the user with a dialog to select from a list of
	 * available Entries from a DataComponent. If no Entries are available, an
	 * appropriate message is displayed.
	 */
	public void visit(DataComponent component) {

		// Open a dialog if there are parameters that can be edited.
		if (!(component.retrieveAllEntries().isEmpty())) {
			// Instantiate the dialog, set the DataComponent and set the
			// listeners
			ExtraInfoDialog infoDialog = new ExtraInfoDialog(getSite()
					.getShell());
			infoDialog.setDataComponent((DataComponent) component);
			// Open the dialog
			infoDialog.open();
		} else {
			// Otherwise, notify the user that there are no parameters for this
			// selection.
			String msg = "No parameters available.";
			MessageDialog msgBox = new MessageDialog(viewerParent.getShell(),
					msg, null, msg, MessageDialog.INFORMATION,
					new String[] { "OK" }, 0);
			msgBox.open();
		}

		return;
	}

	public void visit(ResourceComponent component) {
		// Do nothing.
	}

	public void visit(TableComponent component) {
		// Do nothing.
	}

	public void visit(MatrixComponent component) {
		// Do nothing.
	}

	public void visit(IShape component) {
		// Do nothing.
	}

	public void visit(GeometryComponent component) {
		// Do nothing.
	}

	public void visit(MasterDetailsComponent component) {
		// Do nothing.
	}

	public void visit(TreeComposite component) {
		// Do nothing.
	}

	public void visit(IReactorComponent component) {
		// Do nothing.
	}

	public void visit(TimeDataComponent component) {
		// Do nothing.
	}

	public void visit(MeshComponent component) {
		// Do nothing.
	}

	public void visit(BatteryComponent component) {
		// Do nothing.
	}

	public void visit(AdaptiveTreeComposite component) {
		// Do nothing.

	}

	// -------------------------------------- //

	// ---- Implements ITabbedPropertySheetPageContributor ---- //
	/**
	 * This operation returns the contributor id of this view.
	 */
	public String getContributorId() {
		return getSite().getId();
	}
	// -------------------------------------------------------- //

}
