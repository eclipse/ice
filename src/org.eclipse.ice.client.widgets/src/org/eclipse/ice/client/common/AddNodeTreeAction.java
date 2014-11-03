package org.eclipse.ice.client.common;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This class is an action that adds a new child node to the current tree in the
 * TreeView if it has exemplar children.
 * 
 * @author jaybilly, djg
 * 
 */
public class AddNodeTreeAction extends Action implements ISelectionListener {

	/**
	 * Adds a child node to the currently selected TreeComposite in the
	 * {@link #treeViewer}.
	 */
	private final Action addToCurrentNode = new Action() {
		@Override
		public void run() {
			addToNode(selectedParent);
		}
	};

	/**
	 * Adds a child node to the {@link #treeViewer}'s root TreeComposite.
	 */
	private final Action addToRootNode = new Action() {
		@Override
		public void run() {
			addToNode(treeViewer.getRoot());
		}
	};

	/**
	 * The selected node in the TreeCompositeViewer.
	 */
	private TreeComposite selectedParent;

	/**
	 * The viewer that contains the tree for which this action can add nodes. We
	 * use it to get the root TreeComposite when the selection is empty.
	 */
	private final TreeCompositeViewer treeViewer;

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
	 * 
	 * @param treeViewer
	 *            The viewer that contains the tree for which this action can
	 *            add nodes.
	 */
	public AddNodeTreeAction(TreeCompositeViewer treeViewer) {
		super(null, IAction.AS_DROP_DOWN_MENU);

		// Set the default ID to the TreeCompositeViewer.
		partId = TreeCompositeViewer.ID;

		// Set the text
		setText("Add Child");
		setToolTipText("Add a child to the selected node.");

		// Create the image descriptor from the file path
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "add.png");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		setImageDescriptor(imageDescriptor);

		// Register for updates from the TreeCompositeViewer
		IWorkbench bench = PlatformUI.getWorkbench();
		window = bench.getActiveWorkbenchWindow();
		window.getSelectionService().addSelectionListener(this.partId, this);

		// Initially, the selected parent is null.
		selectedParent = null;

		// Keep a reference of the tree viewer so we can get its root
		// TreeComposite when nothing is selected.
		this.treeViewer = treeViewer;

		// Set the initial text for the two actions.
		addToRootNode.setText("Add to root node");
		addToCurrentNode.setText("Add to selected node");
		addToCurrentNode.setEnabled(false);

		// Add a menu creator to the action. Two choices should be displayed:
		// add a new node to the root TreeComposite, and add a new node to the
		// currently selected TreeComposite.
		setMenuCreator(new IMenuCreator() {

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
				addToRootNode
						.setEnabled(canAddNode(AddNodeTreeAction.this.treeViewer
								.getRoot()));

				return;
			}
		});

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
	 * This operation runs the action. The default action is to try adding a new
	 * node to the currently-selected node. If one cannot be added, show a
	 * dialog that explains a node cannot be added.
	 */
	@Override
	public void run() {

		if (addToCurrentNode.isEnabled()) {
			addToCurrentNode.run();
		} else {
			// Show a dialog that says the selected node cannot accept children.
			MessageDialog.openInformation(window.getShell(), "Child Selector",
					"Cannot add a child node to selected node.");
		}

		return;
	}

	/**
	 * This operation handles a change in selection in the view. If the
	 * selection is empty, then the selected node should be the root
	 * TreeComposite for the {@link #treeViewer}. Otherwise, use the selected
	 * TreeComposite.
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		// Reset the selection and default action. If the selection is valid,
		// these will be updated below.
		selectedParent = null;
		addToCurrentNode.setText("Add to selected node");

		// Selected TreeComposites should be set as the selected parent.
		if (part.getSite().getId().equals(partId) && !selection.isEmpty()) {
			Object selectedNode = ((ITreeSelection) selection)
					.getFirstElement();
			if (selectedNode instanceof TreeComposite) {
				// Set the reference to the selected parent TreeComposite.
				selectedParent = (TreeComposite) selectedNode;
				addToCurrentNode.setText("Add to \"" + selectedParent.getName()
						+ "\"");
			}
		}

		// Update the action for adding to the current node.
		addToCurrentNode.setEnabled(canAddNode(selectedParent));

		return;
	}

	/**
	 * Determines whether or not a child node can be added to a TreeComposite.
	 * 
	 * @param tree
	 *            The TreeComposite that may be able to have children.
	 * @return Whether or not a child node can be added to the tree.
	 */
	private boolean canAddNode(TreeComposite tree) {
		return tree != null && !tree.getChildExemplars().isEmpty();
	}

	/**
	 * Prompts the user to add a supported type of node to the specified tree.
	 * If the node does not support children, this method does nothing.
	 * 
	 * @param tree
	 *            The TreeComposite that a new node will be added to.
	 */
	private void addToNode(TreeComposite tree) {
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
			ListDialog addNodeDialog = new ListDialog(window.getShell());
			addNodeDialog.setAddCancelButton(true);
			addNodeDialog.setContentProvider(new ArrayContentProvider());
			addNodeDialog.setLabelProvider(new LabelProvider());
			addNodeDialog.setInput(exemplarMap.keySet().toArray());
			addNodeDialog.setInitialSelections(exemplarMap.keySet().toArray());
			addNodeDialog.setTitle("Child Selector");
			addNodeDialog.setMessage("Select a new child from the list");
			addNodeDialog.open();

			if (addNodeDialog.getResult() != null) {
				// Get the exemplar
				TreeComposite exemplar = exemplarMap.get(addNodeDialog
						.getResult()[0]);
				// Clone it. This lets you pull a sub-class of TreeComposite if
				// the clone() method is overridden.
				TreeComposite child = (TreeComposite) exemplar.clone();
				// Add it to the tree
				tree.setNextChild(child);
			} else {
				// Close the list dialog otherwise
				addNodeDialog.close();
			}
		}

		return;
	}

}
