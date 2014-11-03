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

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.dialogs.ListDialog;
import org.osgi.framework.Bundle;

/**
 * This class is the Action by which new Items are created through the Eclipse
 * menu or toolbar.
 * 
 * @author Jay Jay Billings
 */
public class CreateItemAction extends Action implements ISelectionListener,
		IWorkbenchAction {

	/**
	 * Handle to the workbench window
	 */
	private final IWorkbenchWindow workbenchWindow;

	/**
	 * Action ID
	 */
	public static final String ID = "org.eclipse.ice.client.common.createItemAction";

	/**
	 * Constructor
	 * 
	 * @param IWorkbenchWindow
	 *            window - the window that should be used by the action
	 */
	public CreateItemAction(IWorkbenchWindow window) {

		// Local declarations
		Bundle bundle = null;
		Path newWizPath = null;
		URL newWizImageURL = null;
		ImageDescriptor newWizImage = null;
		String separator = System.getProperty("file.separator");

		// Find the client bundle
		bundle = Platform.getBundle("org.eclipse.ice.client.widgets");

		// Create the image descriptor for the create item action
		newWizPath = new Path("icons" + separator + "new_wiz.gif");
		newWizImageURL = FileLocator.find(bundle, newWizPath, null);
		newWizImage = ImageDescriptor.createFromURL(newWizImageURL);

		// Set the window, ID and tooltip
		workbenchWindow = window;
		setId(ID);
		setText("&Create an Item");
		setToolTipText("Create a new Item with ICE");
		setImageDescriptor(newWizImage);

		// Add the listener
		workbenchWindow.getSelectionService().addSelectionListener(this);
		
		return;
	}

	/**
	 * This operation connects to the Client to create an Item
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {

		// Local Declarations
		ArrayList<String> itemTypeList = null;
		String msg = "ICE does not have any plugins configured! "
				+ "This most likely means that you are missing some required "
				+ "data files or that ICE hasn't completely loaded yet. "
				+ "In rare instances it may indicate a bug in the "
				+ "plug-in that you are trying to use. If you feel like you have "
				+ "configured everything properly, feel free to submit a bug "
				+ "report at http://projects.eclipse.org/projects/technology.ice"
				+ " and reference error code #2.";

		// Get the client reference
		IClient client = ClientHolder.getClient();

		// Present a selection dialog if Items are available
		if (client != null
				&& (itemTypeList = client.getAvailableItemTypes()) != null) {
			// Setup the list dialog
			ListDialog createItemDialog = new ListDialog(
					this.workbenchWindow.getShell());
			createItemDialog.setAddCancelButton(true);
			createItemDialog.setContentProvider(new ArrayContentProvider());
			createItemDialog.setLabelProvider(new LabelProvider());
			createItemDialog.setInput(itemTypeList.toArray());
			createItemDialog.setInitialSelections(itemTypeList.toArray());
			createItemDialog.setTitle("Item Selector");
			createItemDialog
					.setMessage("Please select an Item from the list. "
							+ "ICE will create this Item for you and present it on the "
							+ "screen.");
			createItemDialog.open();

			// Direct the client to create a new Item if a selection was made
			if (createItemDialog.getResult() != null && createItemDialog.getResult().length > 0) {
				client.createItem((createItemDialog.getResult()[0]).toString());
			} else {
				// Close the list dialog otherwise
				createItemDialog.close();
			}

		} else {
			// Throw an error if no Items are available
			MessageDialog.openError(workbenchWindow.getShell(),
					"Unable to create items!", msg);
		}

	}

	/**
	 * Operation to dispose of the action
	 * 
	 * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
	 */
	@Override
	public void dispose() {

		// Remove the listener
		this.workbenchWindow.getSelectionService()
				.removeSelectionListener(this);

		return;
	}

	/**
	 * Empty method to override a change in the selection
	 * 
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
	 *      IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// Nothing to do

	}

}
