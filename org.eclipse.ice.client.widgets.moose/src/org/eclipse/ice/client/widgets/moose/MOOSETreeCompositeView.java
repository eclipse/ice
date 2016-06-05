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
package org.eclipse.ice.client.widgets.moose;

import org.eclipse.ice.client.common.TreeCompositeLabelProvider;
import org.eclipse.ice.client.widgets.AddNodeTreeAction;
import org.eclipse.ice.client.widgets.DeleteNodeTreeAction;
import org.eclipse.ice.client.widgets.ICEFormInput;
import org.eclipse.ice.client.widgets.RenameNodeTreeAction;
import org.eclipse.ice.client.widgets.TreeCompositeContentProvider;
import org.eclipse.ice.client.widgets.TreeCompositeViewer;
import org.eclipse.ice.item.nuclear.MOOSEModel;
import org.eclipse.january.form.Form;
import org.eclipse.january.form.TreeComposite;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

/**
 * This class extends the default ICE {@link TreeCompositeViewer} to add new
 * functionality in support of MOOSE model development. This includes:
 * <ul>
 * <li>Syncing with the currently active {@link MOOSEFormEditor}.</li>
 * </ul>
 * 
 * @author Jordan Deyton
 *
 */
public class MOOSETreeCompositeView extends TreeCompositeViewer implements
		IPartListener2 {

	/**
	 * The ID of this view. This should be the same as its ID in the
	 * <code>plugin.xml</code>.
	 */
	public static final String ID = "org.eclipse.ice.client.widgets.moose.MOOSETreeCompositeView";

	/**
	 * The <code>ICEFormEditor</code> using this view.
	 */
	private MOOSEFormEditor editor;

	/**
	 * The <code>Form</code> contained by the {@link #formInput}.
	 */
	private Form form;

	/**
	 * Overrides the parent class's behavior to register as a workbench part
	 * listener.
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Proceed with the default behavior.
		super.createPartControl(parent);

		// Register with the PartService to tell which MOOSE Model Builder is
		// currently active.
		getSite().getWorkbenchWindow().getPartService().addPartListener(this);

		return;
	}

	/**
	 * Overrides the default <code>ToolBar</code> <code>Action</code>s. Note
	 * that the add action is simpler than the default because adding to the
	 * root node is not allowed.
	 */
	@Override
	protected void createActions() {

		// Create the action for adding child exemplars as new nodes in the
		// tree. The default action is acceptable.
		addAction = new AddNodeTreeAction();
		addAction.setPartId(ID);

		// Create the action for deleting child nodes from the tree. We should
		// only be allowed to delete nodes below the first level of blocks, so
		// we should check that the node has a grandparent.
		deleteAction = new DeleteNodeTreeAction() {
			@Override
			protected boolean canRun() {
				// Note that the default behavior only checks that the node has
				// a parent.
				return super.canRun()
						&& getSelectedNode().getParent().getParent() != null;
			}
		};
		deleteAction.setPartId(ID);

		// Create the action for renaming tree nodes. We should only be allowed
		// to delete nodes below the first level of blocks, so check that it has
		// a grandparent.
		renameAction = new RenameNodeTreeAction() {
			@Override
			protected boolean canRun() {
				TreeComposite selectedNode = getSelectedNode();
				return super.canRun() && selectedNode.getParent() != null
						&& selectedNode.getParent().getParent() != null;
			}
		};
		renameAction.setPartId(ID);

		return;
	}

	/**
	 * Overrides the default viewer to add an additional feature: When a parent
	 * node is unchecked, all of its child nodes are unchecked.
	 */
	@Override
	protected TreeViewer createViewer(Composite parent) {
		TreeViewer treeViewer = null;

		if (parent != null) {
			// Initialize the TreeViewer. Note: We create a CheckboxTreeViewer!
			final CheckboxTreeViewer checkboxTreeViewer = new CheckboxTreeViewer(
					parent, SWT.VIRTUAL | SWT.MULTI | SWT.H_SCROLL
							| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
			treeViewer = checkboxTreeViewer;

			// Set and configure the content and label providers
			treeViewer.setContentProvider(new TreeCompositeContentProvider(
					this, parentMap));
			treeViewer.setLabelProvider(new TreeCompositeLabelProvider());

			// Add a provider to tell the viewer when elements should be
			// checked. This is NOT default behavior.
			MOOSETreeCheckStateManager checkManager;
			checkManager = new MOOSETreeCheckStateManager();
			checkboxTreeViewer.setCheckStateProvider(checkManager);
			checkboxTreeViewer.addCheckStateListener(checkManager);
		}

		return treeViewer;
	}

	// ---- Implements IPartListener2 ---- //
	// This class implements this interface so it can listen to the currently
	// active MOOSE Model Builder. When the Model Builder changes, the app
	// selection widget and the tree's content need to be updated.

	/**
	 * This is called when an editor has been activated. If that part is a
	 * {@link MOOSEFormEditor}, mark it as the active editor and refresh the
	 * content of this view.
	 */
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {

		String partId = partRef.getId();
		IWorkbenchPart part = partRef.getPart(false);

		if (MOOSEFormEditor.ID.equals(partId) && part != editor) {

			// Set the new active editor.
			editor = (MOOSEFormEditor) part;
			ICEFormInput formInput = (ICEFormInput) editor.getEditorInput();
			form = formInput.getForm();

			// Set the MOOSE tree based on the editor's current configuration.
			int treeId = MOOSEModel.mooseTreeCompositeId;
			setInput((TreeComposite) form.getComponent(treeId), editor);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partBroughtToTop(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}

	/**
	 * This is called when an editor has been closed. This method clears the
	 * {@link #editor} and empties the contents of this view if the currently
	 * active editor was closed.
	 */
	@Override
	public void partClosed(IWorkbenchPartReference partRef) {

		IWorkbenchPart part = partRef.getPart(false);

		if (part == editor) {
			// Clear the input to the TreeViewer.
			setInput(new TreeComposite(), null);

			// Unset the reference to the currently active editor.
			editor = null;
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partDeactivated(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partOpened(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partHidden(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partVisible(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partInputChanged(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}

	// ----------------------------------- //
}
