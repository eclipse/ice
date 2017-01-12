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
package org.eclipse.ice.tests.client.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.eclipse.ice.client.widgets.ICEDataComponentSectionPart;
import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.junit.Test;

/**
 * <p>
 * This class is responsible for testing the ICEDataComponentSectionPart class.
 * It only tests the accessor operations for the DataComponent and the update
 * routine from IComponentListener.
 * </p>
 *
 * @author Jay Jay Billings
 */
public class ICEDataComponentSectionPartTester {
	/**
	 *
	 */
	private ICEDataComponentSectionPart iCESectionPart;

	/**
	 * <p>
	 * This operation checks the ICESectionPart to make sure that it can store a
	 * DataComponent. This operation also tests the update operation of
	 * ICESectionPart by modifying a DataComponent and insuring that the
	 * ICESectionPart gets the update.
	 * </p>
	 *
	 */
	@Test
	public void checkDataComponent() {

		// Local Declarations
		final DataComponent comp1 = new DataComponent();
		DataComponent comp2 = null;
		final ArrayList<DataComponent> compList = null;
		final String compName = null;
		final Display display;

		// Set some info on the first component
		comp1.setName("Gravy");

		// Setup the display, form toolkit and test form.
		display = Display.getDefault();
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				FormToolkit formTk;
				ManagedForm eclipseTestForm;
				formTk = new FormToolkit(display);
				eclipseTestForm = new ManagedForm(new Shell(display));


				// Setup the ICESectionPage
				iCESectionPart = new ICEDataComponentSectionPart(formTk
						.createSection(eclipseTestForm.getForm().getBody(),
								Section.TITLE_BAR | Section.DESCRIPTION
										| Section.TWISTIE | Section.EXPANDED),
						new ICEFormEditor(), eclipseTestForm);
				iCESectionPart.setDataComponent(comp1);

			}
		});

		// Get the component and check it
		comp2 = iCESectionPart.getDataComponent();
		assertNotNull(comp2);
		assertEquals(comp2.getName(), "Gravy");

		// Reset comp2 and add an entry to comp1 to cause an update
		// notification to be posted.
		comp2 = null;
		comp1.addEntry(new StringEntry());

		// Grab the Component from the ICESectionPart and make sure it is
		// correct
		comp2 = iCESectionPart.getDataComponent();
		assertNotNull(comp2);
		assertEquals(comp2.retrieveAllEntries().size(), 1);

		return;

	}
}