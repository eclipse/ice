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
package org.eclipse.ice.reactor.perspective;

import org.eclipse.ice.client.widgets.reactoreditor.ReactorFormWidgetBuilder;

import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.reactor.perspective.internal.ReactorEditorRegistry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This class looks up the currently open Reactor Editors using the
 * {@link ReactorEditorRegistry} and tells the {@link ReactorViewer} to send its
 * current selection to one of the Reactor Editors.
 * 
 * @author Taylor Patterson, Jordan H. Deyton
 */
public class CompareAction extends Action {

	/**
	 * The name used by ICE for Reactor Editors.
	 */
	private final String editorName;

	/**
	 * The ViewPart that owns an object of this class.
	 */
	private final ViewPart viewer;

	/**
	 * The constructor
	 * 
	 * @param parent
	 *            The ViewPart to whom the object of this class belongs.
	 */
	public CompareAction(ViewPart parent) {
		// Create this action as a drop down menu.
		super(null, IAction.AS_DROP_DOWN_MENU);

		viewer = parent;

		// Get the editor name from the ReactorFormWidgetBuilder.
		editorName = ReactorFormWidgetBuilder.name;

		// Set the action's tool tip text.
		setText("Compare with...");
		setToolTipText("Compare with a component in an existing " + editorName);

		// Set the action's image (the import arrow).
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "compare.png");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		setImageDescriptor(imageDescriptor);

		// Create the IMenuCreator that generates the menus for the
		// ShowInReactorEditorAction.
		final IMenuCreator menuCreator = new IMenuCreator() {

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

				// Get the List of editor IDs.
				List<Integer> editorIds = ReactorEditorRegistry.getEditorIds();

				// If the list of editors is empty, place an empty action to
				// notify the user that they can't compare.
				// TODO Disable the action if the list of editors is empty!
				if (editorIds.isEmpty()) {
					menuManager.add(new Action("No editor available.") {
						// Nothing to do. Don't override run().
					});
				}

				// Add an item for each of the current Reactor Editors.
				for (final int id : editorIds) {
					Action action = new Action() {
						@Override
						public void run() {
							((ReactorViewer) viewer).compareSelection(id);
						}
					};
					action.setText(editorName + " " + id);
					action.setToolTipText("Compares the tree selection with "
							+ editorName + " " + id);
					menuManager.add(action);
				}

				return;
			}
		};
		// Don't forget to set the menu creator for this action.
		setMenuCreator(menuCreator);

		return;
	}

	/**
	 * The function called whenever the action is clicked.
	 */
	@Override
	public void run() {

		// TODO We may want to change this behavior to do something like compare
		// with the currently-active reactor editor.

		return;
	}

}
