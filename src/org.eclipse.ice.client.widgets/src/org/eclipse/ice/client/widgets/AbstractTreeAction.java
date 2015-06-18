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
 *    Jay Jay Billings (UT-Battelle, LLC.) - fixed author tags
 *    Jay Jay Billings (UT-Battelle, LLC.) - moved from 
 *      org.eclipse.ice.client.common package
 *******************************************************************************/
package org.eclipse.ice.client.widgets;

import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * This class is a base for JFace <code>Action</code>s done based on the current
 * selection in an Eclipse UI part that contains {@link TreeComposite}s.
 * <p>
 * By default, this class listens for the selection in the
 * {@link TreeCompositeViewer}. The source of the selections can be changed via
 * {@link #setPartId(String)}, but the source is expected to provide an
 * {@link IStructuredSelection} containing at least one
 * <code>TreeComposite</code>.
 * </p>
 * 
 * @author Jordan H. Deyton
 * 
 */
public abstract class AbstractTreeAction extends Action implements
		ISelectionListener {

	/**
	 * The currently selected <code>TreeComposite</code> in the
	 * <code>MOOSETreeCompositeViewer</code>. This may be null.
	 */
	private TreeComposite selectedNode;

	/**
	 * The ID of the part (typically a <code>TreeCompositeViewer</code>) that
	 * this <code>Action</code> corresponds to.
	 */
	private String partId;

	/**
	 * The default constructor.
	 */
	public AbstractTreeAction() {

		// Set the initial selection.
		selectedNode = null;

		// Set the default ID to the TreeCompositeViewer.
		partId = null;
		setPartId(TreeCompositeViewer.ID);

		// Set the text and tool tip for the action.
		setText("Action text not set");
		setToolTipText("Action tool tip not set.");

		return;
	}

	/**
	 * Changes the workbench part this <code>Action</code> listens to for
	 * selection changes.
	 * 
	 * @param partId
	 *            The ID of the part whose selections will be used by this
	 *            <code>Action</code>.
	 */
	public void setPartId(String partId) {

		if (partId != null && !partId.equals(this.partId)) {
			// Get the Eclipse UI selection service.
			IWorkbench bench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = bench.getActiveWorkbenchWindow();
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
	 * Gets the currently selected <code>TreeComposite</code> in the
	 * <code>TreeCompositeViewer</code>. This may be null.
	 * 
	 * @return The currently selected node in the
	 *         <code>TreeCompositeViewer</code>'s tree, or null if none is
	 *         selected.
	 */
	protected TreeComposite getSelectedNode() {
		return selectedNode;
	}

	/**
	 * Whether or not the action can be run. By default, this returns true if
	 * the {@link #selectedNode} is not null, false otherwise.
	 * 
	 * @return True if the <code>AbstractTreeAction</code> can be run, false
	 *         otherwise.
	 */
	protected boolean canRun() {
		return selectedNode != null;
	}

	/**
	 * Overrides the default behavior and forces sub-classes to implement the
	 * run action.
	 */
	@Override
	public abstract void run();

	/**
	 * Updates the {@link #selectedNode} based on the selection in the current
	 * Eclipse view corresponding to the {@link #partId}. If the selection is
	 * empty, this value is null. The action is also enabled or disabled
	 * depending on whether the selection is valid.
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// Proceed only if the parameters are valid and match the source
		// TreeCompositeViewer's ID.
		if (part != null && selection != null
				&& partId.equals(part.getSite().getId())) {

			// Clear the selected node since the selection changed.
			selectedNode = null;

			// Get the selected element if possible.
			if (!selection.isEmpty()
					&& selection instanceof IStructuredSelection) {
				Object selectedObject = ((IStructuredSelection) selection)
						.getFirstElement();

				// Convert the selected element to a TreeComposite.
				if (selectedObject instanceof TreeComposite) {
					selectedNode = (TreeComposite) selectedObject;
				}
			}

			// Enable/disable the action depending on whether or not the
			// selected node can be renamed.
			setEnabled(canRun());
		}

		return;
	}

}
