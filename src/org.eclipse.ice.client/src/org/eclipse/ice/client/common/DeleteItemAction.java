/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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

import java.util.List;

import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

/***
 * This class is the Action by which Items are deleted from the ICE database
 * through Eclipse.
 * 
 * @author Jay Jay Billings
 * 
 */
public class DeleteItemAction extends Action implements ISelectionListener,
		IWorkbenchAction {

	// FIXME This class is no longer required. Now the ItemViewer uses the
	// DeleteItemHandler (added to the ItemViewer's ToolBar via the
	// org.eclipse.ui.menus extension point).

	/**
	 * Handle to the workbench window
	 */
	private final IWorkbenchWindow workbenchWindow;

	/**
	 * A handle to the current selection
	 */
	private ISelection selectedItem = null;

	/**
	 * Action ID
	 */
	public static final String ID = "org.eclipse.ice.client.common.deleteItemAction";

	/**
	 * Constructor
	 * 
	 * @param window
	 *            The window that should be used by the action
	 */
	public DeleteItemAction(IWorkbenchWindow window) {

		workbenchWindow = window;
		return;
	}

	/**
	 * This operation connects to the Client to delete an Item
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	@Override
	public void run() {

		// Get the client reference
		IClient client = ClientHolder.getClient();

		// Only try to do the delete if the selection is not null. This must be
		// checked here even though selectionChanged() checks it because this
		// class could be called elsewhere.
		if (selectedItem != null) {
			// Get the array from the selection. ItemViewer uses an
			// IStructuredSelection from its TableViewer.
			List<?> list = ((IStructuredSelection) selectedItem).toList();

			// Loop over the list of item names
			for (Object obj : list) {
				// Cast to a string
				String itemName = obj.toString();

				// Since we know that the id is the last set of characters after
				// the final space, get the index of that final space.
				int index = itemName.lastIndexOf(" ");

				// Direct the client to delete the item
				client.deleteItem(Integer.valueOf(itemName.substring(index + 1)));
			}
		}

		return;
	}

	/**
	 * Operation to dispose of the action
	 * 
	 * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
	 */
	@Override
	public void dispose() {

		// Remove the listener
		workbenchWindow.getSelectionService().removeSelectionListener(this);

		return;
	}

	/**
	 * Empty method to override a change in the selection
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

		// Make sure the selection is not empty before and is the right type
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			selectedItem = selection;
		}
	}

}
