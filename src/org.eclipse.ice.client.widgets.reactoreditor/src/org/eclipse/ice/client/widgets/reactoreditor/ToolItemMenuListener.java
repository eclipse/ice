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
package org.eclipse.ice.client.widgets.reactoreditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;

/**
 * This class implements a Listener that enables a drop-down Menu for a ToolItem
 * on a ToolBar.
 * 
 * @author djg
 * 
 */
public class ToolItemMenuListener implements Listener {

	/**
	 * Local reference to the ToolItem that needs a drop-down Menu.
	 */
	private ToolItem toolItem;
	/**
	 * Local reference to the drop-down Menu.
	 */
	private Menu menu;

	/**
	 * The constructor for this custom Listener.
	 * 
	 * @param bar
	 *            The ToolBar containing the ToolItem.
	 * @param item
	 *            The ToolItem receiving a drop-down Menu.
	 * @param menu
	 *            The ToolItem's drop-down Menu.
	 */
	public ToolItemMenuListener(ToolItem item, Menu menu) {
		// We need these components in the handleEvent function.
		// These parameters facilitate dynamic Menus for dynamic ToolItems.
		toolItem = item;
		this.menu = menu;

		return;
	}

	/**
	 * This function should only make the Menu visible if necessary. Nothing
	 * inside the Menu will be changed.
	 */
	@Override
	public void handleEvent(Event event) {
		// We want to align the menu with the bottom-left corner of the ToolItem
		// arrow.

		// event.detail == SWT.ARROW means the arrow has been clicked.
		// event.detail == SWT.NONE means the button has been clicked.
		if (event.detail == SWT.ARROW | event.detail == SWT.NONE) {
			Rectangle r = toolItem.getBounds();
			Point p = new Point(r.x, r.y + r.height);
			p = toolItem.getParent().toDisplay(p.x, p.y);
			menu.setLocation(p.x, p.y);
			menu.setVisible(true);
		}

		return;
	}

}
