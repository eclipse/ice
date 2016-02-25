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
package org.eclipse.eavp.viz.service.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * This class provides a tree containing JFace Actions. Each node in the tree
 * has either a developer-defined Action (passed in through the constructor) or
 * a default Action that builds a Menu from the children of the node.
 * 
 * 
 * 
 * @author Jordan H. Deyton
 * 
 */
public class VizActionTree {
	/**
	 * The text displayed by the Action's IContributionItem.
	 */
	private final String text;
	/**
	 * The list of children of this node, or <code>null</code> for no children.
	 */
	private final List<VizActionTree> children;
	/**
	 * The JFace Action associated with this node. This is either
	 * developer-defined (passed in through the constructor) or default (with a
	 * drop down menu).
	 */
	private final IAction action;
	/**
	 * The IMenuCreator responsible for building the menu for the children, or
	 * <code>null</code> for no children.
	 */
	private final IMenuCreator menuCreator;

	/**
	 * Whether or not the ActionTree is enabled. If the ActionTree is configured
	 * for children but its List is empty, then it will still be disabled.
	 */
	private boolean enabled;

	/**
	 * ActionTree constructor for <b>parent</b> tree nodes that will get a menu.
	 * These nodes should have child ActionTrees added via the function
	 * 
	 * @param text
	 *            The text displayed by the Action's IContributionItem. This
	 *            will be seen on the MenuItem or ToolItem.
	 */
	public VizActionTree(String text) {
		// Set the text.
		this.text = text;

		// Define a new JFace Action that will open a Menu for this node. The
		// Menu will show items for the children of this node.
		action = new Action(this.text, IAction.AS_DROP_DOWN_MENU) {
			@Override
			public void run() {
				// This method only appears to be run when the action
				// corresponds to a ToolItem.
				// TODO Have the menu appear in the correct location below the
				// button.

				// ToolItem toolItem = null;
				// Menu menu = null;
				// TODO How do we get the Action's ToolItem?
				// TODO How do we get the Action's Menu?
				// getMenuCreator().getMenu(Control parent);
				//
				// Rectangle r = toolItem.getBounds();
				// Point p = new Point(r.x, r.y + r.height);
				// p = toolItem.getParent().toDisplay(p.x, p.y);
				// menu.setLocation(p.x, p.y);
				// menu.setVisible(true);
			}
		};
		action.setEnabled(false);

		// Initialize the list of children.
		children = new ArrayList<VizActionTree>();

		// Initialize and set the IMenuCreator that will build the menu.
		menuCreator = new MenuCreator();
		action.setMenuCreator(menuCreator);

		// Default to being enabled as soon as possible.
		enabled = true;

		return;
	}

	/**
	 * ActionTree constructor for <b>child</b> tree nodes that will not have a
	 * menu.
	 * 
	 * @param action
	 *            A developer-defined JFace Action.
	 */
	public VizActionTree(IAction action) {
		// Set the text.
		text = action.getText();

		// Set the developer-defined action.
		this.action = action;

		// We have no children and do not need the IMenuCreator.
		children = null;
		menuCreator = null;

		// Default to being enabled as soon as possible.
		enabled = true;

		return;
	}

	/**
	 * Enables or disables the ActionTree. If the ActionTree is configured for
	 * children but its List is empty, then it will still be disabled.
	 * 
	 * @param enabled
	 *            Whether or not to enable the ActionTree.
	 */
	public void setEnabled(boolean enabled) {
		// Only proceed if the value has changed.
		if (enabled != this.enabled) {
			this.enabled = enabled;

			// Actions with children should always be disabled if its list of
			// children is empty!
			if (children == null || !children.isEmpty()) {
				action.setEnabled(enabled);
			}
		}
		return;
	}

	/**
	 * Disposes of any system resources held by this ActionTree node and all of
	 * its descendants.
	 */
	public void dispose() {
		if (children != null) {
			menuCreator.dispose();
			for (VizActionTree child : children) {
				child.dispose();
			}
		}
		return;
	}

	/**
	 * Adds the specified child ActionTree to this node, if possible. This will
	 * enable ActionTrees that have child nodes.
	 * 
	 * @param child
	 *            The child ActionTree node to add.
	 */
	public void add(VizActionTree child) {
		if (children != null) {
			children.add(child);
			action.setEnabled(enabled);
		}
		return;
	}

	/**
	 * Removes the first occurrence of the specified child ActionTree to this
	 * node, if possible. If the resulting List of child trees is empty, this
	 * ActionTree will be disabled.
	 * 
	 * @param child
	 *            The child ActionTree node to remove.
	 */
	public void remove(VizActionTree child) {
		if (children != null) {
			children.remove(child);
			if (children.isEmpty()) {
				action.setEnabled(false);
			}
		}
		return;
	}

	/**
	 * Removes all child ActionTrees. This ActionTree will be disabled.
	 */
	public void removeAll() {
		if (children != null) {
			for (int i = children.size() - 1; i >= 0; i--) {
				children.remove(i);
			}
			action.setEnabled(false);
		}
		return;
	}

	/**
	 * This returns a <b>new, dynamic</b> IContributionItem initialized with
	 * this node's JFace Action. The caller is responsible for disposing of this
	 * IContributionItem with the item's dispose() function.
	 * 
	 * @return A new, dynamic IContributionItem.
	 */
	public IContributionItem getContributionItem() {
		// In order to have dynamic Menus, our ActionContributionItems must be
		// dynamic, otherwise the MenuCreator's getMenu() method is only called
		// once.
		return new ActionContributionItem(action) {
			@Override
			public boolean isDynamic() {
				return true;
			}
		};
	}

	/**
	 * This returns the ActionTree node's child nodes, or <code>null</code> if
	 * the node has no children.
	 * 
	 * @return An <b>unmodifiable</b> List of ActionTree nodes or
	 *         <code>null</code>.
	 */
	public List<VizActionTree> getChildren() {
		return (children == null ? null : Collections
				.unmodifiableList(children));
	}

	/**
	 * This subclass implements the IMenuCreator interface for use in the
	 * ActionTree. This class is responsible for building Menus based on an
	 * ActionTree node's children from their IContributionItems.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private class MenuCreator implements IMenuCreator {

		/**
		 * A reference to the current Menu used by this ActionTree node.
		 */
		private Menu menu = null;
		/**
		 * A List of the IContributionItems that have been added to the Menu.
		 */
		private final List<IContributionItem> items = new ArrayList<IContributionItem>();

		/**
		 * Disposes of any resources occupied by this IMenuCreator
		 * implementation.
		 */
		@Override
		public void dispose() {
			if (menu == null) {
				return;
			}
			// Dispose of the Menu.
			menu.dispose();

			// Dispose of all of the IContributionItems and clear the List.
			for (IContributionItem item : items) {
				item.dispose();
			}
			items.clear();
		}

		/**
		 * This method constructs a new Menu based on the child ActionTree nodes
		 * for a parent Control (e.g., a ToolItem).
		 */
		@Override
		public Menu getMenu(Control parent) {
			// Get the current ActionTree node.
			VizActionTree tree = VizActionTree.this;

			// Dispose of the old Menu and create a new one.
			dispose();
			menu = new Menu(parent);

			// Add the IContributionItems from the child nodes.
			for (VizActionTree child : tree.getChildren()) {
				IContributionItem item = child.getContributionItem();
				items.add(item);

				// This adds the item to the Menu as a MenuItem.
				item.fill(menu, -1);
			}

			return menu;
		}

		/**
		 * This method constructs a new Menu based on the child ActionTree nodes
		 * for a parent Control (e.g., a MenuItem).
		 */
		@Override
		public Menu getMenu(Menu parent) {
			// Get the current ActionTree node.
			VizActionTree tree = VizActionTree.this;

			// Dispose of the old Menu and create a new one.
			dispose();
			menu = new Menu(parent);

			// Add the IContributionItems from the child nodes.
			for (VizActionTree child : tree.getChildren()) {
				IContributionItem item = child.getContributionItem();
				items.add(item);

				// This adds the item to the Menu as a MenuItem.
				item.fill(menu, -1);
			}

			return menu;
		}
	}
}
