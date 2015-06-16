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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

/**
 * This class is used by Eclipse to launch the action that will delete Items
 * from ICE's database.
 * 
 * @author Jay Jay Billings
 * 
 */
public class DeleteItemViewActionDelegate implements IViewActionDelegate {

	// FIXME This class is no longer used in favor of the DeleteItemHandler
	// added to the ItemViewer's ToolBar via the org.eclipse.ui.menus extension
	// point.

	/**
	 * The IViewPart with which this delegate works, in this case the ItemViewer
	 */
	private IViewPart viewPart = null;

	/**
	 * The action that will perform the deletion.
	 */
	IWorkbenchAction deleteItemAction = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {

		// Launch the action
		deleteItemAction.run();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(IViewPart view) {

		// Set the reference
		viewPart = view;

		// Setup the action
		deleteItemAction = new DeleteItemAction(viewPart.getSite()
				.getWorkbenchWindow());

		// Register the action as a listener
		viewPart.getSite()
				.getPage()
				.addPostSelectionListener((ISelectionListener) deleteItemAction);

	}

}
