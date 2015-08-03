/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation -
 *   Jay Jay Billings, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.client.widgets.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.eclipse.ice.client.widgets.ListComponentNattable;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.materials.MaterialWritableTableFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.junit.Test;

/**
 * Tests the ListComponentNattable class and its methods.
 *
 *
 * @author Kasper Gammeltoft
 *
 */
public class ListComponentNattableTest {

	/**
	 * The ListComponent to provide data to the ListComponentNattable
	 */
	private static ListComponent list;

	/**
	 * The Shell that contains the composits for the table
	 */
	private static Shell shell;

	/**
	 * The ListComponentNattable to test
	 */
	private static ListComponentNattable table;

	/**
	 * The SectionClient where the NatTable will be rendered.
	 */
	private static Composite sectionClient;

	/**
	 * Test materials
	 */
	private static Material mat1;
	private static Material mat2;
	private static Material mat3;

	/**
	 * This operation makes sure the Nattable is created correctly with the list
	 * data.
	 */
	@Test
	public void testListComponentNattable() {

		final Display display = Display.getDefault();

		// Setup the display, form toolkit and test form.
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				FormToolkit formToolkit;
				ManagedForm eclipseTestForm;
				eclipseTestForm = new ManagedForm(new Shell(display));

				formToolkit = new FormToolkit(display);

				final ScrolledForm scrolledForm = eclipseTestForm.getForm();

				// Set a GridLayout with a single column. Remove the default
				// margins.
				GridLayout layout = new GridLayout(1, true);
				layout.marginWidth = 0;
				layout.marginHeight = 0;
				scrolledForm.getBody().setLayout(layout);

				// Only create something if there is valid input.

				// Get the parent
				Composite parent = eclipseTestForm.getForm().getBody();

				shell = parent.getShell();
				// Create the section and set its layout info
				Section listSection = formToolkit.createSection(parent,
						Section.TITLE_BAR | Section.DESCRIPTION
								| Section.TWISTIE | Section.EXPANDED
								| Section.COMPACT);
				listSection.setLayout(new GridLayout(1, false));
				listSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
						true, true, 1, 1));
				// Create the section client, which is the client area of the
				// section that will actually render data.
				sectionClient = new Composite(listSection, SWT.FLAT);
				sectionClient.setLayout(new GridLayout(2, false));
				sectionClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
						true, true, 1, 1));
				// parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				// listSection.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				sectionClient.setBackground(Display.getCurrent()
						.getSystemColor(SWT.COLOR_WHITE));
				shell.setBackground(Display.getCurrent().getSystemColor(
						SWT.COLOR_WHITE));

				// create some test materials to put into the table- to provide
				// some
				// sort of data
				Material material = new Material();
				material.setName("Mat1");
				material.setProperty("A", 1.0);
				material.setProperty("B", 2.0);
				mat1 = material;

				Material material2 = new Material();
				material2.setName("Mat2");
				material2.setProperty("A", 5.2);
				material2.setProperty("B", 4.1);
				mat2 = material2;

				mat3 = new Material();
				mat3.setName("Mat3");
				mat3.setProperty("A", 2.4);
				mat3.setProperty("B", .394);

				ArrayList<String> colNames = new ArrayList<String>();
				colNames.addAll(material.getProperties().keySet());

				// Set the format of the table, it is tested for holding
				// materials
				MaterialWritableTableFormat tableFormat = new MaterialWritableTableFormat(
						colNames);

				// Instantiate the ListComponent to test the Nattable with
				list = new ListComponent();
				list.setTableFormat(tableFormat);
				list.add(material);
				list.add(material2);

				// creates the new Nattable for testing
				table = new ListComponentNattable(sectionClient, list, false);

				return;
			}
		});

		// assertions for testing
		assertNotNull(table);

		assertEquals(table.getList().getColumnCount(), 3);

		table.getList().clear();
		assertFalse(table.getList().contains(mat1));

		table.getList().add(mat2);
		table.getList().add(mat3);

		assertEquals(table.getList().getColumnValue(mat2, 1),
				mat2.getProperty("A"));

		return;
	}

}