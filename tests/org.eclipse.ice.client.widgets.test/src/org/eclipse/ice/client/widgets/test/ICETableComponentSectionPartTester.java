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
package org.eclipse.ice.client.widgets.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.junit.Ignore;
import org.junit.Test;

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICETableComponentSectionPart;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TableComponent;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the ICEDataComponentSectionPart class.
 * It only tests the accessor operations for the TableComponent and the update
 * routine from IComponentListener.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */

public class ICETableComponentSectionPartTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ICETableComponentSectionPart ICETableComponentSectionPart;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ICESectionPart to make sure that it can store a
	 * TableComponent. This operation also tests the update operation of
	 * ICESectionPart by modifying a TableComponent and insuring that the
	 * ICESectionPart gets the update.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkTableComponent() {
		// begin-user-code

		// Local Declarations
		TableComponent tableComponent = new TableComponent();
		TableComponent tempTableC = null;
		TableComponent tableComponent2 = null;
		Display display;
		FormToolkit formTk;
		ManagedForm eclipseTestForm;

		// Setup the display, form toolkit and test form. The display must be
		// retrieve from the Eclipse PlatformUI if it is running or created
		// separately if not.
		if (!PlatformUI.isWorkbenchRunning()) {
			display = new Display();
		} else {
			display = PlatformUI.getWorkbench().getDisplay();
		}
		formTk = new FormToolkit(display);
		eclipseTestForm = new ManagedForm(new Shell(display));

		// Setup TableComponent
		Entry column1 = new Entry();
		Entry column2 = new Entry();
		Entry column3 = new Entry();

		ArrayList<Entry> template = new ArrayList<Entry>();
		ArrayList<Entry> row1 = new ArrayList<Entry>();
		ArrayList<Entry> row2 = new ArrayList<Entry>();

		// set tableComponent values for name, id, description
		tableComponent.setName("Table 1");
		tableComponent.setId(1);
		tableComponent.setDescription("This is a table that contains entries.");

		// Set row template - gather columns to table
		column1.setName("Column1");
		column1.setId(1);
		column1.setDescription("I am Column1!");
		column1.setValue("Over 9000!");
		column1.setTag("Column");

		column2 = new Entry() {
			@Override
			protected void setup() {
				allowedValueType = AllowedValueType.Continuous;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("0");
				this.allowedValues.add("15");
			}
		};
		column2.setName("Column2");
		column2.setId(2);
		column2.setDescription("I am Column2! I can only take values from 0 to 15");
		column2.setValue("0");
		column2.setTag("Column");

		// Override ->add menuitem
		column3 = new Entry() {
			@Override
			protected void setup() {
				allowedValueType = AllowedValueType.Discrete;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("I am one");
				this.allowedValues.add("I am two");
			}
		};
		column3.setName("Column3");
		column3.setId(3);
		column3.setDescription("I am Column3!");
		column3.setValue("I am one");
		column3.setTag("Column");

		// Add columns to template arraylist
		template.add(column1);
		template.add(column2);
		template.add(column3);

		// Add template to tableComponent
		tableComponent.setRowTemplate(template);

		assertNotNull(tableComponent.getRowTemplate());

		// add rows to tableComponent
		tableComponent.addRow();
		tableComponent.addRow();

		// Get row ArrayLists
		row1 = tableComponent.getRow(0);
		row2 = tableComponent.getRow(1);

		// edit row1
		row1.get(0).setName("Entry1");
		row1.get(0).setId(1);
		row1.get(0).setDescription("I am Entry1!");

		row1.get(1).setName("Entry2");
		row1.get(1).setId(2);
		row1.get(1).setDescription("I am Entry2!");
		row1.get(1).setValue("0");

		row1.get(2).setName("Entry3");
		row1.get(2).setId(3);
		row1.get(2).setDescription("I am Entry3!");

		// edit row2
		row2.get(0).setName("Entry4");
		row2.get(0).setId(4);
		row2.get(0).setDescription("I am Entry4!");

		row2.get(1).setName("Entry5");
		row2.get(1).setId(5);
		row2.get(1).setDescription("I am Entry5!");
		row2.get(1).setValue("15");

		row2.get(2).setName("Entry6");
		row2.get(2).setId(6);
		row2.get(2).setDescription("I am Entry6!");

		// check to make sure the values have been set for row1 and row2
		assertEquals("0", row1.get(1).getValue());
		assertEquals("15", row2.get(1).getValue());

		// Initialize the ICETableComponentSectionPart - with proper values
		this.ICETableComponentSectionPart = new ICETableComponentSectionPart(
				formTk.createSection(eclipseTestForm.getForm().getBody(),
						Section.TITLE_BAR | Section.DESCRIPTION
								| Section.TWISTIE | Section.EXPANDED),
				new ICEFormEditor(), eclipseTestForm);

		// make sure that the tableComponent is set to null when the getter is
		// used
		// on a newly initialized object of ICETableComponentSectionPart
		assertNull(this.ICETableComponentSectionPart.getTableComponent());

		// call the setter with a valid tableComponent (initialized and pretty)
		this.ICETableComponentSectionPart.setTableComponent(tableComponent);

		// call the getter - check values
		tempTableC = this.ICETableComponentSectionPart.getTableComponent();

		// make sure it is not null and check some values
		assertNotNull(tempTableC);

		// check the values
		assertTrue(tempTableC.equals(tableComponent));

		// test for null on setter
		this.ICETableComponentSectionPart.setTableComponent(null);

		// check the values - nothing has changed
		tempTableC = null;
		tempTableC = this.ICETableComponentSectionPart.getTableComponent();

		// make sure it has some values
		assertNotNull(tempTableC);

		// check values
		assertTrue(tempTableC.equals(tableComponent));

		// rowTemplate needs to be setup before it can be set in the table
		tableComponent2 = new TableComponent();

		// set up a name to identfy it
		tableComponent2.setName("Table2");
		tableComponent2.setDescription("I am not a fully created table");
		tableComponent2.setId(2);

		// pass the table to the setter
		this.ICETableComponentSectionPart.setTableComponent(tableComponent2);

		// get the tableComponent - make sure the tableComponent2 has not been
		// set.
		tempTableC = null;
		tempTableC = this.ICETableComponentSectionPart.getTableComponent();

		assertNotNull(tempTableC);

		// do a comparison
		assertTrue(tempTableC.equals(tableComponent));

		// this is to test the update on the ICETableComponentSectionPart

		// add a row to tableComponent
		tableComponent.addRow();

		// get the tableComponent and see if the row was added
		tempTableC = null;
		tempTableC = this.ICETableComponentSectionPart.getTableComponent();
		assertNotNull(tempTableC);
		assertEquals(3, tempTableC.numberOfRows());

		// close display
		display.dispose();

		// end-user-code
	}
}