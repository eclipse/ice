/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation 
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.widgets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * This class can be used to create a dialog for selecting leaf elements from a
 * tree.
 * <p>
 * To set up a dialog, instantiate a provider. You must override the following
 * methods as would normally be done for a JFace content or label provider:
 * </p>
 * <ul>
 * <li>{@link #getChildren(Object)}</li>
 * <li>{@link #getText(Object)}</li>
 * <li>{@link #isSelected(Object)}</li>
 * </ul>
 * <p>
 * You may now open the dialog by calling
 * {@link #openDialog(Shell, Object, boolean)}.
 * </p>
 * <p>
 * To get the results of the selection after OK has been pressed, the provider
 * has the following methods:
 * </p>
 * <ul>
 * <li>{@link #getAllSelectedLeafElements()}</li>
 * <li>{@link #getSelectedLeafElements()}</li>
 * <li>{@link #getUnselectedLeafElements()}</li>
 * </ul>
 * 
 * @author Jordan
 *
 */
public class TreeSelectionDialogProvider {

	/**
	 * Nodes wrap elements in the tree so they can be tracked more easily
	 * (especially when determining whether the element is checked or "grayed").
	 * 
	 * @author Jordan
	 *
	 */
	private class Node {
		/**
		 * The node's children. Should be initialized at least to an empty
		 * array.
		 */
		public Node[] children = emptyNodeArray;
		/**
		 * The wrapped element.
		 */
		public final Object element;
		/**
		 * The node's parent.
		 */
		public final Node parent;

		/**
		 * Constructs a node wrapping the specified element with the specific
		 * parent node.
		 * 
		 * @param element
		 *            The wrapped element.
		 * @param parent
		 *            The parent of the node, or {@code null} if the node is the
		 *            root.
		 */
		public Node(Object element, Node parent) {
			this.element = element;
			this.parent = parent;
		}
	}

	/**
	 * A reference to an empty array of nodes. Used for leaf nodes.
	 */
	private Node[] emptyNodeArray = new Node[0];
	/**
	 * The initial selection of leaf nodes. This is updated when
	 * {@link #setInput(Object)} is called.
	 */
	private final Set<Node> initialSelection = new HashSet<Node>();
	/**
	 * The message to display above the dialog's tree.
	 */
	private String message = null;
	/**
	 * The current selection of leaf nodes. This is
	 */
	private final Set<Node> newSelection = new HashSet<Node>();
	/**
	 * The root node for the tree.
	 */
	private Node root;
	/**
	 * The title for the dialog.
	 */
	private String title = null;

	/**
	 * Checks/un-checks the specified node, which cascades down to all leaf
	 * nodes in the node's sub-tree. This updates the set of selected nodes, but
	 * <i>does not update the TreeViewer.</i>
	 * 
	 * @param node
	 *            The node to check/un-check.
	 * @param checked
	 *            If true, the node sub-tree will be checked. Otherwise, it will
	 *            be un-checked.
	 */
	private void checkNode(Node node, boolean checked) {
		Stack<Node> descendants = new Stack<Node>();
		descendants.push(node);
		while (!descendants.isEmpty()) {
			Node descendant = descendants.pop();
			if (descendant.children.length > 0) {
				for (Node child : descendant.children) {
					descendants.push(child);
				}
			} else if (checked) {
				newSelection.add(descendant);
			} else {
				newSelection.remove(descendant);
			}
		}
		return;
	}

	/**
	 * Creates a dialog used to select <i>multiple</i> values from the input
	 * tree.
	 * 
	 * @param shell
	 *            The parent shell for the dialog.
	 * @param labelProvider
	 *            The label provider for the tree.
	 * @param contentProvider
	 *            The content provider for the tree.
	 * @return The new dialog.
	 */
	private CheckedTreeSelectionDialog createCheckedTreeSelectionDialog(
			Shell shell, ILabelProvider labelProvider,
			ITreeContentProvider contentProvider) {

		// Create a custom check-state listener to update the selected elements
		// on the fly.
		final ICheckStateListener checkStateListener = createCheckStateListener();

		CheckedTreeSelectionDialog treeDialog = new CheckedTreeSelectionDialog(
				shell, labelProvider, contentProvider) {
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.ui.dialogs.CheckedTreeSelectionDialog#createSelectionButtons(org.eclipse.swt.widgets.Composite)
			 */
			@Override
			protected Composite createSelectionButtons(Composite composite) {
				Composite buttonComposite = super.createSelectionButtons(
						composite);

				// Add listeners to the select-all and deselect-all buttons
				// because they do not trigger the check-state listener.
				getButton(IDialogConstants.SELECT_ALL_ID)
						.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						newSelection.clear();
						checkNode(root, true);
					}
				});
				getButton(IDialogConstants.DESELECT_ALL_ID)
						.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						newSelection.clear();
					}
				});

				return buttonComposite;
			}

			/*
			 * (non-Javadoc)
			 * @see org.eclipse.ui.dialogs.CheckedTreeSelectionDialog#createTreeViewer(org.eclipse.swt.widgets.Composite)
			 */
			@Override
			protected CheckboxTreeViewer createTreeViewer(Composite parent) {
				// Create the default TreeViewer.
				CheckboxTreeViewer viewer = super.createTreeViewer(parent);

				// Add the check state provider and listener.
				viewer.addCheckStateListener(checkStateListener);

				return viewer;
			}
		};

		// Set this flag so the "grayed" state of parent nodes is handled
		// properly.
		treeDialog.setContainerMode(true);

		return treeDialog;
	}

	/**
	 * Creates a new check-state listener for the dialog's tree. This listener
	 * is notified when the user checks/un-checks a node in the dialog's tree
	 * and updates the bookkeeping.
	 * 
	 * @return A new check-state listener.
	 */
	private ICheckStateListener createCheckStateListener() {
		return new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				checkNode((Node) event.getElement(), event.getChecked());
			}
		};
	}

	/**
	 * Creates a new content provider to be used for the dialog's tree. The
	 * provider is based on the current tree of {@link Node}s.
	 * 
	 * @return A new tree content provider.
	 */
	private ITreeContentProvider createContentProvider() {
		return new ITreeContentProvider() {
			@Override
			public void dispose() {
				// Nothing to do.
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return ((Node) parentElement).children;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return ((Node) inputElement).children;
			}

			@Override
			public Object getParent(Object element) {
				return ((Node) element).parent;
			}

			@Override
			public boolean hasChildren(Object element) {
				return ((Node) element).children.length > 0;
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// Nothing to do.
			}
		};
	}

	/**
	 * Creates a dialog used to select exactly <i>one</i> value from the input
	 * tree.
	 * 
	 * @param shell
	 *            The parent shell for the dialog.
	 * @param labelProvider
	 *            The label provider for the tree.
	 * @param contentProvider
	 *            The content provider for the tree.
	 * @return The new dialog.
	 */
	private ElementTreeSelectionDialog createElementTreeSelectionDialog(
			Shell shell, ILabelProvider labelProvider,
			ITreeContentProvider contentProvider) {

		// Create a custom selection listener to update the selected elements on
		// the fly.
		final ISelectionChangedListener selectionListener = createSelectionChangedListener();

		ElementTreeSelectionDialog treeDialog = new ElementTreeSelectionDialog(
				shell, labelProvider, contentProvider) {
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.ui.dialogs.ElementTreeSelectionDialog#createTreeViewer(org.eclipse.swt.widgets.Composite)
			 */
			@Override
			protected TreeViewer createTreeViewer(Composite parent) {
				TreeViewer viewer = super.createTreeViewer(parent);

				// Add the selection changed listener.
				viewer.addSelectionChangedListener(selectionListener);

				return viewer;
			}
		};

		// Do not allow multi-selection here.
		treeDialog.setAllowMultiple(false);

		return treeDialog;
	}

	/**
	 * Creates a new label provider to be used for the dialog's tree. The
	 * provider re-directs to the {@link #getText(Object)} method whenever
	 * possible.
	 * 
	 * @return A new label provider.
	 */
	private ILabelProvider createLabelProvider() {
		return new LabelProvider() {
			@Override
			public String getText(Object element) {
				Object actualElement = ((Node) element).element;
				String text = TreeSelectionDialogProvider.this
						.getText(actualElement);
				return text != null ? text : super.getText(actualElement);
			}
		};
	}

	/**
	 * Creates a new selection listener to update the {@link Node} bookkeeping
	 * when a node is selected and multi-select is disabled.
	 * 
	 * @return The new selection listener.
	 */
	private ISelectionChangedListener createSelectionChangedListener() {
		return new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// Add the first selected leaf node to the selected leaves.
				ISelection selection = event.getSelection();
				newSelection.clear();
				if (!selection.isEmpty()) {
					Node node = (Node) ((IStructuredSelection) selection)
							.getFirstElement();
					// Add only leaf nodes.
					if (node.children.length == 0) {
						newSelection.clear();
						newSelection.add(node);
					}
				}
				return;
			}
		};
	}

	/**
	 * Gets all selected leaf nodes in the tree.
	 * 
	 * @return A list containing all selected leaf nodes in the tree.
	 */
	public List<Object> getAllSelectedLeafElements() {
		List<Object> selectedObjects = new ArrayList<Object>();
		for (Node node : newSelection) {
			selectedObjects.add(node.element);
		}
		return selectedObjects;
	}

	/**
	 * Gets the children of the specified element.
	 * <p>
	 * <b>Note:</b> Cycles are prohibited and may cause unexpected results.
	 * </p>
	 * 
	 * @param parent
	 *            The element that may or may not have children that will also
	 *            be put in the tree.
	 * @return An array containing all child elements that will be put in the
	 *         tree.
	 */
	public Object[] getChildren(Object parent) {
		return new Object[0];
	}

	/**
	 * Gets all newly selected leaf nodes in the tree. This does not include
	 * nodes that were selected when the dialog was opened.
	 * 
	 * @return A list containing newly selected leaf nodes in the tree.
	 */
	public List<Object> getSelectedLeafElements() {
		List<Object> selectedObjects = new ArrayList<Object>();
		for (Node node : newSelection) {
			if (!initialSelection.contains(node)) {
				selectedObjects.add(node.element);
			}
		}
		return selectedObjects;
	}

	/**
	 * Gets the text label for the element.
	 * 
	 * @param element
	 *            The element that will be put in the tree.
	 * @return A string label for the element.
	 */
	public String getText(Object element) {
		return "";
	}

	/**
	 * Gets all leaf nodes that were unselected from the tree. In other words,
	 * this returns all nodes that were part of the initial selection but were
	 * at some point unselected by the user.
	 * 
	 * @return A list containing nodes that were unselected from the tree.
	 */
	public List<Object> getUnselectedLeafElements() {
		List<Object> unselectedObjects = new ArrayList<Object>();
		for (Node node : initialSelection) {
			if (!newSelection.contains(node)) {
				unselectedObjects.add(node.element);
			}
		}
		return unselectedObjects;
	}

	/**
	 * Whether or not the element is selected.
	 * 
	 * @param element
	 *            The element that will be put in the tree.
	 * @return True if the element is selected, false otherwise.
	 */
	public boolean isSelected(Object element) {
		return false;
	}

	/**
	 * Creates and opens a dialog enabling the user to select one or more
	 * elements from the specified input tree.
	 * 
	 * @param shell
	 *            The parent shell for the dialog.
	 * @param input
	 *            The input for the dialog's tree.
	 * @param allowMultipleSelections
	 *            Whether or not to allow multiple items to be selected. If
	 *            false, then only one item may be selected from the tree,
	 *            otherwise the tree's selection will be determined by
	 *            checkboxes.
	 * @return The result of the dialog, usually either {@link Window#OK} or
	 *         {@link Window#CANCEL}.
	 */
	public int openDialog(Shell shell, Object input,
			boolean allowMultipleSelections) {

		// Create the dialog if necessary.
		final SelectionDialog dialog;

		// Set up the content and label providers. These are required for
		// any TreeViewer.
		ITreeContentProvider contentProvider = createContentProvider();
		ILabelProvider labelProvider = createLabelProvider();

		// Create a dialog with or without checkboxes in the tree.
		if (allowMultipleSelections) {
			dialog = createCheckedTreeSelectionDialog(shell, labelProvider,
					contentProvider);
		} else {
			dialog = createElementTreeSelectionDialog(shell, labelProvider,
					contentProvider);
		}

		// Set its title and message.
		dialog.setTitle(title);
		dialog.setMessage(message);

		setInput(input);

		// Set the input and refresh the viewer.
		if (allowMultipleSelections) {
			CheckedTreeSelectionDialog treeDialog = (CheckedTreeSelectionDialog) dialog;
			treeDialog.setInput(root);
			if (!initialSelection.isEmpty()) {
				treeDialog.setInitialSelections(initialSelection.toArray());
				newSelection.addAll(initialSelection);
			}
		} else {
			ElementTreeSelectionDialog treeDialog = (ElementTreeSelectionDialog) dialog;
			treeDialog.setInput(root);
			if (!initialSelection.isEmpty()) {
				Node initialNode = initialSelection.iterator().next();
				treeDialog.setInitialSelection(initialNode);
				newSelection.add(initialNode);
			}
		}

		// Return the result of opening the dialog.
		int result = dialog.open();

		// If the dialog was closed or cancelled, reset the selection to the
		// initial one.
		if (result != Window.OK) {
			newSelection.clear();
			for (Node node : initialSelection) {
				newSelection.add(node);
			}
		}

		return result;
	}

	/**
	 * Sets the input object that will be shown in the dialog's tree.
	 * 
	 * @param input
	 *            The input for the tree.
	 */
	private void setInput(Object input) {

		// This method wraps the elements with instances of the Node class. This
		// is convenient for getting the parent or child nodes quickly.

		Node node;
		Node child;

		initialSelection.clear();
		newSelection.clear();

		root = new Node(input, null);

		Stack<Node> stack = new Stack<Node>();

		stack.push(root);
		while (!stack.isEmpty()) {
			node = stack.pop();

			Object[] childObjects = getChildren(node.element);
			if (childObjects == null || childObjects.length == 0) {
				if (isSelected(node.element)) {
					initialSelection.add(node);
				}
			} else {
				int size = childObjects.length;
				node.children = new Node[size];
				for (int i = 0; i < size; i++) {
					child = new Node(childObjects[i], node);
					node.children[i] = child;
					stack.push(child);
				}
			}
		}

		return;
	}

	/**
	 * Sets the message to display in the created dialog. This appears above the
	 * tree.
	 * 
	 * @param message
	 *            The new message.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Sets the title to display in the created dialog.
	 * 
	 * @param title
	 *            The new title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
