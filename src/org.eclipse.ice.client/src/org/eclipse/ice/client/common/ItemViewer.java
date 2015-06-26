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
import java.util.List;

import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * This class provides an Eclipse view showing the list of Items that have been
 * created and made available in the Core.
 * 
 * @author Jay Jay Billings
 * 
 */
public class ItemViewer extends ViewPart {

	/**
	 * The id
	 */
	public static final String ID = "org.eclipse.ice.client.common.ItemViewer";

	/**
	 * The tableviewer used to display the items
	 */
	private TableViewer tableViewer = null;

	/**
	 * The IClient from which information about the Items is retrieved.
	 */
	private IClient client = null;

	/**
	 * A reference to the Viewer's parent that is passed in to
	 * createPartControl.
	 */
	private Composite viewerParent = null;

	/**
	 * Create the table viewer that shows the list of Items accessible in the
	 * Core by the Client.
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Local Declarations
		Thread drawThread = null;

		// Set the parent reference
		viewerParent = parent;

		// Draw the viewer
		drawViewer();

		// Setup the thread that will wait for the client to come alive and then
		// add the items to the table
		drawThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// Wait until the client is available
				while (client == null) {
					// Get the client reference
					client = ClientHolder.getClient();
					// Sleep for a little bit (.5 sec) if the thread needs to
					// wait
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// Sync with the display
				PlatformUI.getWorkbench().getDisplay()
						.asyncExec(new Runnable() {
							@Override
							public void run() {
								// If the client is no longer null, set the
								// input on the
								// tableviewer. Just pass it the client
								// reference even though it can get it directly
								// from the class.
								tableViewer.setInput(client);
							}
						});

			}
		});

		// Start the thread
		drawThread.start();

		return;
	}

	/**
	 * This function draws the ItemViewer.
	 */
	private void drawViewer() {

		// Initialize the tableviewer
		tableViewer = new TableViewer(viewerParent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		// Set and configure the content provider
		tableViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// TODO Auto-generated method stub

			}

			@Override
			public Object[] getElements(Object inputElement) {

				// Local Declarations
				ArrayList<Identifiable> iceObjects;
				String[] uniqueNames;

				// Assert we have a valid, non-null, Client instance
				if (client != null) {
					iceObjects = client.getItems();
					uniqueNames = new String[client.getItems().size()];
					// Create a list of strings with name + id of each Item
					for (int i = 0; i < uniqueNames.length; i++) {
						Identifiable iceObject = iceObjects.get(i);
						uniqueNames[i] = iceObject.getName() + " "
								+ iceObject.getId();
					}
				} else {
					uniqueNames = new String[0];
				}
				return uniqueNames;
			}

		});

		// Add a double click listener to load items from the viewer
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				// Get the selection from the table viewer
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();
				// If the selection is empty, jump ship
				if (selection.isEmpty()) {
					return;
				}
				// Get the selection from the table
				List<?> list = selection.toList();
				// Get the Item name
				String itemName = list.get(0).toString();
				// Since we know that the id is the last set of characters after
				// the final space, get the index of that final space.
				int index = itemName.lastIndexOf(" ");
				// Direct the client to load the item
				client.loadItem(Integer.parseInt(itemName.substring(index + 1)));

			}
		});

		// Register the table view as a selection provider
		getSite().setSelectionProvider(tableViewer);

		// Create an update thread
		Thread updateThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					// Only update if the display is running
					if (tableViewer.getTable().isDisposed()) {
						return;
					} else {
						// Asynchronously refresh the tableviewer
						PlatformUI.getWorkbench().getDisplay()
								.asyncExec(new Runnable() {
									@Override
									public void run() {
										// Just do a blanket update
										if (!tableViewer.getTable()
												.isDisposed()) {
											tableViewer.refresh();

										}
									}
								});

						try {
							// Sleep for one second
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		};
		// Start the thread and watch for new Items!
		updateThread.start();

	}

	/**
	 * A simple pass-through for setting the focus. It does nothing.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
