/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.connections.preferences.test;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester;
import org.eclipse.ice.viz.service.BasicVizServiceFactory;
import org.eclipse.ice.viz.service.IVizServiceFactory;
import org.eclipse.ice.viz.service.connections.preferences.TableComponentComposite;
import org.eclipse.ice.viz.service.csv.CSVVizService;
import org.eclipse.ice.viz.service.datastructures.VizEntry;
import org.eclipse.ice.viz.service.datastructures.VizTableComponent;
import org.eclipse.ice.viz.service.internal.VizServiceFactoryHolder;
import org.eclipse.ice.viz.service.widgets.PlotGridComposite;
import org.eclipse.ice.viz.service.widgets.TimeSliderComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class performs automated UI tests on the {@link TableComponentComposite}
 * . In particular, it tests the composite's ability to add, remove, and update
 * rows in the associated {@link TableComponent}.
 * 
 * @author Jordan Deyton
 * @author Robert Smith
 *
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class TableComponentCompositeTester extends AbstractSWTTester {

	// TODO Implement these tests.

	private static TableComponentComposite testComposite;

	private static VizTableComponent table;

	private static SWTBot bot;

	@Override
	public void beforeEachTest() {
		super.beforeAllTests();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				testComposite = new TableComponentComposite(getShell(),
						SWT.NONE);
			}
		});
		// Create the composite that will be tested.
		// Display display = new Display();
		// Shell shell = new Shell(display);
		// syncExec(new Runnable() {
		// @Override
		// public void run() {
		// testComposite = new TableComponentComposite(shell, SWT.NONE);
		// }
		// });
		// testComposite = new TableComponentComposite(shell, SWT.NONE);
		table = new VizTableComponent();
		ArrayList<VizEntry> template = new ArrayList<VizEntry>();
		template.add(new VizEntry() {
			@Override
			public void setup() {
				defaultValue = "new item";
			}
		});
		table.setRowTemplate(template);
		testComposite.setTableComponent(table);

		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				Point p = getShell().getSize();
				getShell().setSize(p.x - 1, p.y);
				getShell().layout();
			}
		});
		// getShell().layout();
		// Point p = getShell().getSize();
		// getShell().setSize(p.x - 1000, p.y);

		// testComposite.layout(true,true);
	}

	/**
	 * Check that rows can be added and that the underlying table is updated.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void checkAddRow() throws InterruptedException {
		// Get the bot
		bot = getBot();
		// SWTBotPreferences.PLAYBACK_DELAY = 9999;

		// Press the add row button
		bot.button(0).click();

		// Get the table's first row
		ArrayList<VizEntry> row = table.getRow(0);

		// The row should have one entry with the default value. Indexing starts
		// at 1 because row 0 is the column header template.
		assertNotNull(row);
		assertEquals(row.size(), 1);
		assertTrue(row.get(0).getValue().equals("new item"));

		// The second row should not exist yet.
		assertNull(table.getRow(1));

		// Try adding multiple more rows
		bot.button(0).click();
		bot.button(0).click();
		bot.button(0).click();

		// There should be four rows in the table now
		assertNotNull(table.getRow(0));
		assertNotNull(table.getRow(1));
		assertNotNull(table.getRow(2));
		assertNotNull(table.getRow(3));
		assertNull(table.getRow(4));

		// Check one of the new rows to ensure it is populated correctly.
		row = table.getRow(3);
		assertEquals(row.size(), 1);
		assertTrue(row.get(0).getValue().equals("new item"));

		return;
	}

	/**
	 * Check that rows can be removed and that the underlying table is updated.
	 */
	@Test
	public void checkRemoveRow() {
		// fail("Not implemented.");
	}

	/**
	 * Check that a row can be updated by changing a value in the table widget.
	 * The associated Entry in the table should be updated.
	 */
	@Test
	public void checkSetEntryValue() {
		// fail("Not implemented.");
	}

}
