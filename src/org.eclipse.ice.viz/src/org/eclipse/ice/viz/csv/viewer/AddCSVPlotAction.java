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
package org.eclipse.ice.viz.csv.viewer;

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.viz.VizFileViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This Action opens a dialog that allows the user to pick from plots available
 * in the selected file in the {@link VizFileViewer}.
 * 
 * @author Matthew Wang, Taylor Patterson
 */
public class AddCSVPlotAction extends Action implements IMenuCreator {

	/**
	 * The menu for managing the drop-down.
	 */
	private Menu menu;

	/**
	 * The structure for the Actions in the drop-down.
	 */
	private ArrayList<Action> addActions;

	/**
	 * The Action to execute if a drop-down Action is not selected. This will be
	 * the most recent child Action executed. Initially, this will be set to
	 * AddLocalFileAction.
	 */
	private Action defaultAction;

	/**
	 * The constructor
	 * 
	 * @param parent
	 *            The ViewPart to whom the object of this class belongs.
	 */
	public AddCSVPlotAction(ViewPart parent) {

		// Call the super class' constructor to create this action as a
		// drop-down and declare this class as the menu creator
		super(null, IAction.AS_DROP_DOWN_MENU);
		setMenuCreator(this);

		// Set the action's tool tip text.
		setToolTipText("Add a Plot");

		// Set the action's image (the green plus button for adding).
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "add.png");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		setImageDescriptor(imageDescriptor);

		// Create the drop-down selections to add local files and remote files.
		addActions = new ArrayList<Action>();
		CreateCSVPlotAction addPlot = new CreateCSVPlotAction(parent, this);
		addActions.add(addPlot);
		AddCSVSeriesAction addSeries = new AddCSVSeriesAction(parent, this);
		addActions.add(addSeries);

		// Set the default Action to AddLocalFileAction
		defaultAction = addPlot;

		return;
	}

	/**
	 * The function called whenever the action is clicked. It opens a FileDialog
	 * and creates a {@link ICEResource} for any selected compatible files.
	 */
	@Override
	public void run() {

		// Execute the default Action
		defaultAction.run();

		return;
	}

	/**
	 * Set the defaultAction field
	 * 
	 * @param action
	 *            The Action to set defaultAction to
	 */
	public void setDefaultAction(Action action) {

		defaultAction = action;

		return;
	}

	/**
	 * Dispose the menu.
	 * 
	 * @see IMenuCreator#dispose
	 */
	@Override
	public void dispose() {

		if (menu != null) {
			menu.dispose();
			menu = null;
		}

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IMenuCreator#getMenu(Menu)
	 */
	@Override
	public Menu getMenu(Menu parent) {
		// Do nothing
		return null;
	}

	/**
	 * Refresh the menu field.
	 * 
	 * @see IMenuCreator#getMenu(Control)
	 */
	@Override
	public Menu getMenu(Control parent) {

		// Dispose of the old menu
		if (menu != null) {
			menu.dispose();
		}
		// Put all the actions into a new menu
		menu = new Menu(parent);

		for (Action action : addActions) {
			ActionContributionItem item = new ActionContributionItem(action);
			item.fill(menu, -1);
		}

		return menu;
	}
}
