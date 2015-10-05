/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Robert Smith
 *******************************************************************************/
package org.eclipse.ice.ui.swtbot.test;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.IntResult;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;

/**
 * A class which allows an SWTBot to detect and interact with a NatTable.
 * 
 * @author Robert Smith
 *
 */
@SWTBotWidget(clasz = NatTable.class, preferredName = "nattable", referenceBy = { ReferenceBy.LABEL })
public class SWTBotNatTable extends AbstractSWTBot<NatTable> {

	/**
	 * The default constructor
	 * 
	 * @param nattable
	 *            The NatTable to be wrapped.
	 * @throws WidgetNotFoundException
	 *             An exception to be thrown when the SWTBot is unable to
	 *             find a valid NatTable.
	 */
	public SWTBotNatTable(NatTable nattable) throws WidgetNotFoundException {
		super(nattable);
	}

	/**
	 * Gets the row count for the NatTable
	 * 
	 * @return The number of rows in the NatTable
	 */
	public int rowCount() {
		int totalRows = syncExec(new IntResult() {
			public Integer run() {
				return widget.getRowCount();
			}
		});

		// The NatTable counts the top row listing column names in its total
		// count of rows, so the actual number of items in the table is one
		// less.
		return totalRows - 1;
	}

	/**
	 * Clicks on the specified cell in the NatTable.
	 * 
	 * @param bot The SWTBot which is running the tests.
	 * @param column The target column.
	 * @param row The target row.
	 */
	public void selectCell(SWTWorkbenchBot bot, final int column,
			final int row) {

		//Get the cell's position.
		Point point = syncExec(new Result<Point>() {
			public Point run() {
				Rectangle cell = widget.getBoundsByPosition(column + 1,
						row + 1);
				return widget.toDisplay(cell.x + cell.width / 2, cell.y
						+ cell.height / 2);
			}
		});

		Event event = new Event();
		Display display = bot.getDisplay();

		//Move the mouse to the cell
		event.type = SWT.MouseMove;
		event.x = point.x;
		event.y = point.y;
		display.post(event);

		//Click
		event = new Event();
		event.type = SWT.MouseDown;
		event.button = 1;
		display.post(event);
		display.post(event);

		//Unclick
		event = new Event();
		event.type = SWT.MouseUp;
		event.button = 1;
		display.post(event);
	}
}
