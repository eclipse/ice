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
package org.eclipse.ice.caebat.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import org.eclipse.ice.caebat.model.VisitorEntryCopy;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TableComponent;

/**
 * <!-- begin-UML-doc -->
 * <p>This class tests the visit operations on INIVisitorTester.</p>
 * @author s4h
 * <!-- end-UML-doc -->
 */
public class VisitorEntryCopyTester {
	
	/**
	 * <!-- begin-UML-doc -->
	 * <p>This operation tests the constructor.</p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkConstruction() {
		
		//begin-user-code
		
		//Local Declarations
		VisitorEntryCopy visitor = new VisitorEntryCopy(null);
		
		//Check default visitation value
		assertFalse(visitor.wasVisited());
		
		//end-user-code
		
	}
	
	/**
	 * <!-- begin-UML-doc -->
	 * <p>This operation tests the visit operation for DataComponent for creating iniString</p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkDataComponentVisitation() {
		
		//begin-user-code
		
		//Local Declarations
		VisitorEntryCopy visitor;
		DataComponent manyEntryComp;
		DataComponent nullComponent = null;
		Entry entry1, entry2, entry3;
		
		//Create entry1
		entry1 = new Entry();
		entry1.setName("Foooo");
		entry1.setTag("fooTag");
		entry1.setValue("VALUE!");
		
		//Create entry2
		entry2 = new Entry();
		entry2.setName("Foooo2");
		entry2.setTag("fooTag2");
		entry2.setValue(" ");
		
		//Create entry3
		entry3 = new Entry();
		entry3.setName("Foooo3");
		entry3.setTag("fooTag3");
		entry3.setValue("value3");
		
		//Setup manyEntryComp
		manyEntryComp = new DataComponent();
		manyEntryComp.addEntry(entry1);
		manyEntryComp.addEntry(entry2);
		manyEntryComp.addEntry(entry3);
		
		//Setup visitor for null component
		visitor = new VisitorEntryCopy(nullComponent);
		
		//test null
		visitor.visit(nullComponent);
		
		//Check that it was not visited
		assertFalse(visitor.wasVisited());
		
		//Use null visitor with full datacomponent
		visitor.visit(manyEntryComp);
		
		//Check that it was not visited and that nothing was set on the string
		assertFalse(visitor.wasVisited());
		
		//Edit values and see if values changed
		DataComponent changedComp = (DataComponent) manyEntryComp.clone();
		changedComp.retrieveAllEntries().get(0).setValue("DIFFERINGVALUE!!@#!!@#");
		
		//Activate visitor
		visitor = new VisitorEntryCopy(changedComp);
		
		//Call the visit operation
		manyEntryComp.accept(visitor);
		
		//Check to see if values are the same
		assertTrue(manyEntryComp.equals(changedComp));
		assertTrue(visitor.wasVisited());
		
		
		//end-user-code
		
	}
	
	/**
	 * <!-- begin-UML-doc -->
	 * <p>This operation tests the visit operation for TableComponent for creating iniString</p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkTableComponentVisitation() {
		
		//begin-user-code
		
		//Local Declarations
		VisitorEntryCopy visitor;
		TableComponent tableComp;
		TableComponent nullComponent = null;
		Entry entry1, entry2, entry3;
		ArrayList<Entry> template = new ArrayList<Entry>();
		String name = "port";
		
		
		//Create entry1
		entry1 = new Entry();
		entry1.setName("Foooo");
		entry1.setTag("fooTag");
		entry1.setValue("VALUE!");
		
		//Create entry2
		entry2 = new Entry();
		entry2.setName("Foooo2");
		entry2.setTag("fooTag2");
		entry2.setValue(" ");
		
		//Setup TableComponent
		template.add(entry1);
		template.add(entry2);
		
		tableComp = new TableComponent();
		tableComp.setRowTemplate(template);
		
		tableComp.addRow();
		tableComp.addRow();
		
		//Setup visitor for null component
		visitor = new VisitorEntryCopy(nullComponent);
		
		//test null
		visitor.visit(nullComponent);
		
		//Check that it was not visited
		assertFalse(visitor.wasVisited());
		
		//Use null visitor with full datacomponent
		visitor.visit(tableComp);
		
		//Check that it was not visited and that nothing was set on the string
		assertFalse(visitor.wasVisited());
		
		//Edit values and see if values changed
		TableComponent changedComp = (TableComponent) tableComp.clone();
		changedComp.getRow(0).get(0).setValue("FOOorangatang");
		
		//Activate visitor
		visitor = new VisitorEntryCopy(changedComp);
		
		//Call the visit operation
		tableComp.accept(visitor);
		
		//Check to see if values are the same
		assertTrue(tableComp.equals(changedComp));
		assertTrue(visitor.wasVisited());

		
		//end-user-code
			
	}
	


}
