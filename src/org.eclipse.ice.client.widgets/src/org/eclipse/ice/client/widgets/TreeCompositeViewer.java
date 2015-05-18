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

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import org.eclipse.ice.client.common.TreeCompositeLabelProvider;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
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
 * @author Jay Jay Billings, Jordan H. Deyton
 * 
 */
public class TreeCompositeViewer extends ViewPart implements
		IUpdateableListener, ITabbedPropertySheetPageContributor {

	/**
	 * The id
	 */
	public static final String ID = "org.eclipse.ice.client.widgets.TreeCompositeViewer";

	/**
	 * The JFace TreeViewer used to display the TreeComposite
	 */
	protected TreeViewer treeViewer;

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
	 * This action is used to rename nodes in the tree. Only non-root level
	 * nodes can be renamed. Note that the root tree node is not displayed in
	 * the <code>TreeViewer</code>.
	 */
	protected RenameNodeTreeAction renameAction;

	/**
	 * A reference to the source of the {@link #inputTree}. This is useful if
	 * the associated {@link ICEFormEditor} needs to be marked as dirty after
	 * the tree's contents are changed.
	 */
	private ICEFormEditor editor;

	/**
	 * The input to the TreeViewer. This should be a single TreeComposite.
	 */
	protected TreeComposite inputTree;

	/**
	 * A map of Components displayed in the tree. This map can be used to
	 * quickly find the parent TreeComposite for an element in the tree.
	 */
	protected final IdentityHashMap<Component, TreeComposite> parentMap;

	/**
	 * A map of TreeComposites used to cache the children of loaded
	 * TreeComposites.
	 */
	protected final IdentityHashMap<TreeComposite, List<TreeComposite>> childMap;

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
	 * Gets the source of the {@link #inputTree}. This should be marked as dirty
	 * if and when the tree's contents are changed.
	 * 
	 * @return The associated {@link ICEFormEditor}, or null if none is set.
	 */
	public ICEFormEditor getFormEditor() {
		return editor;
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

		// Create the default Actions.
		createActions();
		toolbarManager.add(addAction);
		toolbarManager.add(deleteAction);

		// Set the parent reference
		viewerParent = parent;

		// Draw the viewer and set the initial tree.
		treeViewer = createViewer(viewerParent);
		treeViewer.setInput(inputTree);
		// Register the TreeViewer as a selection provider so that other widgets
		// and parts can get its current selection.
		getSite().setSelectionProvider(treeViewer);

		// Create a MenuManager that will enable a context menu in the
		// TreeViewer.
		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				TreeCompositeViewer.this.fillContextMenu(manager);
			}
		});
		Control control = treeViewer.getControl();
		Menu menu = menuManager.createContextMenu(control);
		control.setMenu(menu);

		return;
	}

	/**
	 * Creates the JFace <code>Action</code>s used in the viewer's
	 * <code>ToolBar</code>.
	 */
	protected void createActions() {

		// Create a sub-menu Action for adding to the currently-selected node.
		// This just re-directs to the add action.
		final Action addToCurrentNode = new Action() {
			@Override
			public void run() {
				addAction.addToNode(addAction.getSelectedNode());
			}
		};
		addToCurrentNode.setText("Add to selected node");
		addToCurrentNode.setEnabled(false);

		// Create a sub-menu Action for adding to the root node. This just re-
		// directs to the add action.
		final Action addToRootNode = new Action() {
			@Override
			public void run() {
				addAction.addToNode(inputTree);
			}
		};
		addToRootNode.setText("Add to root node");
		addToRootNode.setEnabled(false);

		// Create the add action. We need to override some methods to create the
		// menu that will contain the previous two specialized add actions.
		addAction = new AddNodeTreeAction() {

			@Override
			public void run() {

				if (addToCurrentNode.isEnabled()) {
					addToCurrentNode.run();
				} else {
					// Show a dialog that says the selected node cannot accept
					// children.
					IWorkbench bench = PlatformUI.getWorkbench();
					IWorkbenchWindow window = bench.getActiveWorkbenchWindow();
					MessageDialog.openInformation(window.getShell(),
							"Child Selector",
							"Cannot add a child node to selected node.");
				}

				return;
			}

			@Override
			public void selectionChanged(IWorkbenchPart part,
					ISelection selection) {
				super.selectionChanged(part, selection);

				// Sync the add-to-selected-node Action with the selection.
				addToCurrentNode.setEnabled(isEnabled());
				// Sync the add-to-root-node Action with the TreeViewer's root
				// node.
				addToRootNode.setEnabled(canAddNode(inputTree));
				// Activate the main Action if either of the above are enabled.
				setEnabled(addToRootNode.isEnabled()
						|| addToCurrentNode.isEnabled());

				return;
			}
		};

		// Set an IMenuCreator for the add action. The menu should contain the
		// two specialized add actions.
		addAction.setMenuCreator(new IMenuCreator() {

			/**
			 * The MenuManager that contains all of the actions that show the
			 * current selection in a Reactor Editor.
			 */
			private MenuManager menuManager = null;
			/**
			 * The Menu of actions used in the ToolBar.
			 */
			private Menu dropdownMenu = null;
			/**
			 * The Menu of actions used in the context Menu in the reactor
			 * TreeViewer.
			 */
			private Menu contextMenu = null;

			public void dispose() {

				// Dispose of the dropdown menu if possible.
				if (dropdownMenu != null) {
					dropdownMenu.dispose();
					dropdownMenu = null;
				}

				// Dispose of the context menu if possible.
				if (contextMenu != null) {
					contextMenu.dispose();
					contextMenu = null;
				}

				// Dispose of the MenuManager if possible.
				if (menuManager != null) {
					menuManager.removeAll();
					menuManager.dispose();
					menuManager = null;
				}

				return;
			}

			public Menu getMenu(Control parent) {
				// Refresh the menu of actions.
				updateMenuManager();

				// Make sure any stale dropdown menu is disposed.
				if (dropdownMenu != null) {
					dropdownMenu.dispose();
				}

				// Refresh the dropdown menu.
				dropdownMenu = menuManager.createContextMenu(parent);

				return dropdownMenu;
			}

			public Menu getMenu(Menu parent) {
				// Refresh the menu of actions.
				updateMenuManager();

				// Make sure any stale context menu is disposed.
				if (contextMenu != null) {
					contextMenu.dispose();
				}
				// Refresh the context menu. We need to loop over the
				// MenuManager's actions and add each of them to the context
				// menu.
				contextMenu = new Menu(parent);
				for (IContributionItem item : menuManager.getItems()) {
					item.fill(contextMenu, -1);
				}

				return contextMenu;
			}

			/**
			 * Refreshes the MenuManager with all available Reactor Editors.
			 */
			private void updateMenuManager() {

				// Make sure the MenuManager is empty.
				if (menuManager == null) {
					menuManager = new MenuManager();
				} else {
					menuManager.removeAll();
				}

				// Add the item for creating a new Reactor Editor, followed by a
				// separator.
				menuManager.add(addToRootNode);
				menuManager.add(addToCurrentNode);

				// Enable or disable the root node action when the menu is
				// filled out in case the root TreeComposite has changed.
				addToRootNode.setEnabled(addAction.canAddNode(inputTree));

				return;
			}
		});

		// Create the delete action.
		deleteAction = new DeleteNodeTreeAction();

		// Create the rename node action.
		renameAction = new RenameNodeTreeAction();

		return;
	}

	/**
	 * Creates the {@link #treeViewer} used to display the {@link #inputTree}.
	 * 
	 * @param parent
	 *            The container for the <code>TreeViewer</code>.
	 * @return A new <code>TreeViewer</code>.
	 */
	protected TreeViewer createViewer(Composite parent) {

		TreeViewer treeViewer = null;

		if (parent != null) {
			// Initialize the TreeViewer.
			treeViewer = new TreeViewer(parent, SWT.VIRTUAL | SWT.MULTI
					| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
					| SWT.BORDER);

			// Set and configure the content and label providers
			treeViewer.setContentProvider(new TreeCompositeContentProvider(
					this, parentMap));
			treeViewer.setLabelProvider(new TreeCompositeLabelProvider());
		}

		return treeViewer;
	}

	/**
	 * This operation populates the context menu for this view.
	 * 
	 * @param menuManager
	 *            The IMenuManager instance for the context menu to be populated
	 */
	protected void fillContextMenu(IMenuManager menuManager) {

		ISelection iSelection = treeViewer.getSelection();
		if (!iSelection.isEmpty() && iSelection instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) iSelection;

			Object object = selection.getFirstElement();
			if (object instanceof TreeComposite) {

				// Add the rename action.
				menuManager.add(renameAction);

				// Add a separator and the regular actions.
				menuManager.add(new Separator());
				menuManager.add(addAction);
				menuManager.add(deleteAction);
			}
		}

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
	 * This operation sets the TreeComposite that should be displayed for the
	 * current FormEditor.
	 * 
	 * @param tree
	 *            The tree composite
	 * @param source
	 *            The source editor for the tree.
	 */
	public void setInput(TreeComposite tree, ICEFormEditor source) {

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
			Tree treeWidget = treeViewer.getTree();
			if (treeWidget != null && !treeWidget.isDisposed()) {
				treeViewer.setInput(inputTree);
			}

			// Set the name of the view
			setPartName(inputTree.getName() + " -- Tree View");
		}

		// Set the reference to the source of the input tree. It may need to be
		// marked as dirty later if the tree is updated.
		this.editor = source;

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

	// ---- Implements ITabbedPropertySheetPageContributor ---- //
	/**
	 * This operation returns the contributor id of this view.
	 */
	public String getContributorId() {
		return getSite().getId();
	}

	// -------------------------------------------------------- //

}
