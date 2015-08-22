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
 *    Jay Jay Billings (UT-Battelle, LLC.) - relocated from 
 *      org.eclipse.ice.client.widgets bundle
 *******************************************************************************/
package org.eclipse.ice.client.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;

/**
 * This is a handler for deleting any currently selected <code>Item</code>s from
 * the {@link ItemViewer}. It is currently used in the <code>ItemViewer</code>'s
 * <code>ToolBar</code>.
 * 
 * @author Jordan
 * 
 */
public class DeleteItemHandler extends AbstractHandler {

	/**
	 * Deletes any currently selected <code>Item</code>s in the
	 * {@link ItemViewer}.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Do not proceed if the handler is not enabled.
		if (isEnabled()) {

			// Get the client reference
			IClient client = ClientHolder.getClient();

			// Delete all selected items.
			for (String itemName : getSelectedItems()) {
				// Since we know that the id is the last set of characters after
				// the final space, get the index of that final space.
				int index = itemName.lastIndexOf(" ");

				// Direct the client to delete the item.
				client.deleteItem(Integer.valueOf(itemName.substring(index + 1)));
			}
		}

		return null;
	}

	/**
	 * Gets a <code>List</code> containing the names of all <code>Item</code>s
	 * selected in the <code>ItemViewer</code>.
	 * 
	 * @return The <code>Item</code> names. The <code>List</code> will be empty
	 *         if none were selected or the viewer cannot be found.
	 */
	private List<String> getSelectedItems() {

		List<String> itemNames = new ArrayList<String>();

		// Get the ItemViewer and its selection.
		IViewPart itemViewer = ItemSelectedPropertyTester
				.getViewPart(ItemViewer.ID);
		if (itemViewer != null) {
			ISelection selection = itemViewer.getSite().getSelectionProvider()
					.getSelection();
			if (selection != null && selection instanceof IStructuredSelection) {
				// Add all selected Item names to the list.
				for (Object object : ((IStructuredSelection) selection)
						.toList()) {
					if (object != null) {
						itemNames.add(object.toString());
					}
				}
			}
		}

		return itemNames;
	}

}
