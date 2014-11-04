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

import org.eclipse.ice.caebat.model.VisitorINI;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsPair;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;

/**
 * <!-- begin-UML-doc -->
 * <p>This class tests the visit operations on INIVisitorTester.</p>
 * @author s4h
 * <!-- end-UML-doc -->
 */
public class INIVisitorTester {
	
	/**
	 * <!-- begin-UML-doc -->
	 * <p>This operation tests the constructor.</p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkConstruction() {
		
		//begin-user-code
		
		//Local Declarations
		VisitorINI visitor = new VisitorINI();
		
		//Check default visitation value and the getINIString operation
		assertFalse(visitor.wasVisited());
		assertNull(visitor.getINIString());
		
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
		VisitorINI visitor = new VisitorINI();
		VisitorINI visitor1 = new VisitorINI();
		VisitorINI visitor2 = new VisitorINI();
		DataComponent emptyComponent;
		DataComponent oneEntryComp;
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
		
		//test null
		visitor.visit(nullComponent);
		
		//Check that it was not visited and that nothing was set on the string
		assertFalse(visitor.wasVisited());
		assertNull(visitor.getINIString());
		
		//Pass an empty DataComponent
		emptyComponent = new DataComponent();
		visitor.visit(emptyComponent);
		
		//Check that it was visited, and that the string is empty since no entries exist on this DataComponent
		assertTrue(visitor.wasVisited());
		assertEquals("", visitor.getINIString());
		
		//Pass a DataComponent with one entry
		
		//Create DataComponent
		oneEntryComp = new DataComponent();
		
		//Add entry
		oneEntryComp.addEntry(entry1);
		
		//Run visit operation on DataComponent and verify the string returned is correct
		oneEntryComp.accept(visitor1);
		
		//Check values
		assertTrue(visitor1.wasVisited());
		assertEquals("fooTag = VALUE!\n", visitor1.getINIString());
		
		//Pass a DataComponent with multiple entries
		
		//Create DataComponent
		manyEntryComp = new DataComponent();
		
		//Add entries
		manyEntryComp.addEntry(entry1);
		manyEntryComp.addEntry(entry2);
		manyEntryComp.addEntry(entry3);
		
		//Run visit operation on DataComponent and verify the string returned is correct
		manyEntryComp.accept(visitor2);
		
		//Check values
		assertTrue(visitor1.wasVisited());
		assertEquals("fooTag = VALUE!\nfooTag2 =  \nfooTag3 = value3\n", visitor2.getINIString());
		
		//Show that an erroneous null was added to visitor2's accept, then it should reset the visitor
		visitor2.visit(nullComponent);
		assertFalse(visitor2.wasVisited());
		assertNull(visitor2.getINIString());
		
		//Show that visitors can be reused safely
		visitor2.visit(emptyComponent);
		
		assertTrue(visitor2.wasVisited());
		assertEquals("", visitor2.getINIString());
		
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
		VisitorINI visitor = new VisitorINI();
		VisitorINI visitor1 = new VisitorINI();
		VisitorINI visitor2 = new VisitorINI();
		VisitorINI visitor3 = new VisitorINI();
		VisitorINI visitor4 = new VisitorINI();
		VisitorINI visitor5 = new VisitorINI();
		TableComponent emptyComponent;
		TableComponent oneTemplateEmptyComp;
		TableComponent twoTemplateEmptyComp;
		TableComponent threeTemplateEmptyComp;
		TableComponent nullComponent = null;
		TableComponent oneRow;
		TableComponent manyRows;
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
		
		//Create entry3
		entry3 = new Entry();
		entry3.setName("Foooo3");
		entry3.setTag("fooTag3");
		entry3.setValue("value3");
		
		//test null
		visitor.visit(nullComponent);
		
		//Check that it was not visited and that nothing was set on the string
		assertFalse(visitor.wasVisited());
		assertNull(visitor.getINIString());
		
		//Pass an empty TableComponent
		emptyComponent = new TableComponent();
		visitor.visit(emptyComponent);
		
		//Check that it was not visited, and that the string is null since the template has not been set
		assertFalse(visitor.wasVisited());
		assertNull(visitor.getINIString());
		
		//Create a tableComponent with one sized template
		oneTemplateEmptyComp = new TableComponent();
		template.add(entry1);
		oneTemplateEmptyComp.setRowTemplate(template);
		oneTemplateEmptyComp.accept(visitor1);
		
		//Check that it was not visited, and that the string is null since the template has not been set with two
		assertFalse(visitor1.wasVisited());
		assertNull(visitor1.getINIString());
		
		//Create a tableComponent with two sized template
		twoTemplateEmptyComp = new TableComponent();
		template.add(entry2);
		twoTemplateEmptyComp.setRowTemplate(template);
		twoTemplateEmptyComp.accept(visitor2);
		
		//Check that it was visited, and that the string is empty since there are no rows
		assertTrue(visitor2.wasVisited());
		assertEquals("", visitor2.getINIString());
		
		//Now, try with size 3 rows
		threeTemplateEmptyComp = new TableComponent();
		template.add(entry3);
		threeTemplateEmptyComp.setRowTemplate(template);
		threeTemplateEmptyComp.accept(visitor3);
		
		//Check that it was not visited, and that the string is null since the template has not been set with two
		assertFalse(visitor3.wasVisited());
		assertNull(visitor3.getINIString());
		
		//Now then, let us remove the last entry from the template, create a new tableComponent with a row
		template.remove(2); //Remove entry3
		
		//Create tableComponent, set the template, and add a row
		oneRow = new TableComponent();
		oneRow.setRowTemplate(template);
		oneRow.addRow();
		
		//Set the name
		oneRow.setName(name);
		
		//Visit it
		oneRow.accept(visitor4);
		
		//Check values
		assertTrue(visitor4.wasVisited());
		assertEquals("[" + name + "]\n" +
				"    NAMES = " +  entry1.getValue() + "\n\n" +
				"    [[" + entry1.getValue()+"]]\n" +
				"        IMPLEMENTATION = " + entry2.getValue() + "\n", visitor4.getINIString() );
		
		//Create tableComponent, set the template, and add many rows
		manyRows = new TableComponent();
		manyRows.setRowTemplate(template);
		manyRows.setName(name);
		manyRows.addRow();
		manyRows.addRow();
		manyRows.addRow();
		
		//Set the values for each row
		String valueC1R1 = "INIT";
		String valueC2R1 = " ";
		String valueC1R2 = "DRIVER";
		String valueC2R2 = "CHARTAN_THERMAL_DRIVER";
		String valueC1R3 = "THERMAL";
		String valueC2R3 = "AMPERES";
		
		//Set the values
		//Row 1
		manyRows.getRow(0).get(0).setValue(valueC1R1);
		manyRows.getRow(0).get(1).setValue(valueC2R1);
		
		//Row 2
		manyRows.getRow(1).get(0).setValue(valueC1R2);
		manyRows.getRow(1).get(1).setValue(valueC2R2);
		
		//Row 3
		manyRows.getRow(2).get(0).setValue(valueC1R3);
		manyRows.getRow(2).get(1).setValue(valueC2R3);
		
		//Set the name
		oneRow.setName(name);
		
		//Visit it
		manyRows.accept(visitor4);
		
		//Check values
		assertTrue(visitor4.wasVisited());
		assertEquals("[" + name + "]\n" +
				"    NAMES = " +  manyRows.getRow(0).get(0).getValue() + " " + manyRows.getRow(1).get(0).getValue() + " " + manyRows.getRow(2).get(0).getValue() + "\n\n" +
				"    [[" + manyRows.getRow(0).get(0).getValue()+"]]\n" +
				"        IMPLEMENTATION = " + manyRows.getRow(0).get(1).getValue() + "\n" +
				"    [[" + manyRows.getRow(1).get(0).getValue()+"]]\n" +
				"        IMPLEMENTATION = " + manyRows.getRow(1).get(1).getValue() + "\n" +
				"    [[" + manyRows.getRow(2).get(0).getValue()+"]]\n" +
				"        IMPLEMENTATION = " + manyRows.getRow(2).get(1).getValue() + "\n"
				, visitor4.getINIString() );
	
		//Show that an erroneous null was added to visitor2's accept, then it should reset the visitor
		visitor5.visit(nullComponent);
		assertFalse(visitor5.wasVisited());
		assertNull(visitor5.getINIString());
		
		//Show that visitors can be reused safely
		visitor5.visit(twoTemplateEmptyComp);
		
		assertTrue(visitor5.wasVisited());
		assertEquals("", visitor5.getINIString());
		
		//end-user-code
			
	}
	
	/**
	 * <!-- begin-UML-doc -->
	 * <p>This operation tests the visit operation for MasterDetailsComponent for creating iniString.</p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkMasterDetailsComponentVisitation() {
		
		//begin-user-code
		
		//Local Declarations
		VisitorINI visitor = new VisitorINI();
		VisitorINI visitor1 = new VisitorINI();
		VisitorINI visitor2 = new VisitorINI();
		VisitorINI visitor3 = new VisitorINI();
		VisitorINI visitor4 = new VisitorINI();
		VisitorINI visitor5 = new VisitorINI();
		VisitorINI visitor6 = new VisitorINI();
		VisitorINI visitor7 = new VisitorINI();
		VisitorINI visitor8 = new VisitorINI();
		VisitorINI visitor9 = new VisitorINI();
		VisitorINI visitor10 = new VisitorINI();
		MasterDetailsComponent emptyComponent;
		MasterDetailsComponent nullComponent = null;
		MasterDetailsComponent onePair;
		MasterDetailsComponent twoPairs;
		MasterDetailsComponent multiPairs;
		MasterDetailsPair pair1;
		MasterDetailsPair pair2;
		MasterDetailsPair pair3;
		Entry entry1, entry2, entry3, entry4, entry5, entry6;
		DataComponent detailsComponent1, detailsComponent2, detailsComponent3;
		String name1 = "Foo1";
		String name2 = "Foo2";
		String name3 = " ";
		
		//Values of entries
		String valueC1P1 = "INIT";
		String valueC2P1 = "INIT_NAME";
		String valueC1P2 = "DRIVER";
		String valueC2P2 = "CHARTAN_THERMAL_DRIVER";
		String valueC1P3 = "EmptyValue";
		String valueC2P3 = "My name is empty";
		
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
		
		//Create entry4
		entry4 = new Entry();
		entry4.setName("Foooo4");
		entry4.setTag("fooTag4");
		entry4.setValue("value4");
		
		//Create entry5
		entry5 = new Entry();
		entry5.setName("Foooo5");
		entry5.setTag("fooTag5");
		entry5.setValue("value5");
		
		//Create entry6
		entry6 = new Entry();
		entry6.setName("Foooo6");
		entry6.setTag("fooTag6");
		entry6.setValue("value6");
		
		//test null
		visitor.visit(nullComponent);
		
		//Check that it was not visited and that nothing was set on the string
		assertFalse(visitor.wasVisited());
		assertNull(visitor.getINIString());
		
		//Pass an empty MasterDetailsComponent
		emptyComponent = new MasterDetailsComponent();
		visitor1.visit(emptyComponent);
		
		//Check that it was not visited, and that the string is null since the template has not been set
		assertFalse(visitor1.wasVisited());
		assertNull(visitor1.getINIString());
		
		//The way these master details will be printed out assumes the following:
		//0.)  That there are only two entries on every DataComponent.
		//1.)  That there is only one master details pair template for every one implementation on the table.
		//2.)  The name of these pieces are actually the name of the template (master).
		//3.)  If there are multiple pairs for the same master details pair template, it will only grab the first one
		
		//Create a MasterDetailsComponent with one template with only one entry, but no pairs
		onePair = new MasterDetailsComponent();
		
		//Setup DataComponent
		detailsComponent1 = new DataComponent();
		detailsComponent1.setName(name1);
		detailsComponent1.setDescription("Foo Component 1");
		
		//Add Entries
		detailsComponent1.addEntry(entry1);
		
		//Create MasterDetailsPair
		pair1 = new MasterDetailsPair(detailsComponent1.getName(), detailsComponent1);
		
		//Add template to list
		ArrayList<MasterDetailsPair> pairs = new ArrayList<MasterDetailsPair>();
		pairs.add(pair1);
		
		//Add to MasterDetailsComponent
		onePair.setTemplates(pairs);
		
		//Add a master
		onePair.addMaster();
		
		//Visit it
		onePair.accept(visitor2);
		//Check values - should not be visited since only one entry on template
		assertTrue(visitor2.wasVisited());
		assertEquals("[" + pair1.getMaster() + "]\n" +
				"    " + entry1.getTag() + " = " + entry1.getValue() + "\n\n", visitor2.getINIString());
		
		//Create a MasterDetailsComponent with one template with two entries, but no pairs
		onePair = new MasterDetailsComponent();
		
		//Setup DataComponent
		detailsComponent1 = new DataComponent();
		detailsComponent1.setName(name1);
		detailsComponent1.setDescription("Foo Component 1");
		
		//Add Entries
		detailsComponent1.addEntry((Entry) entry1.clone());
		detailsComponent1.addEntry((Entry) entry2.clone());
		
		//Create MasterDetailsPair - clone it to separate the entries from the datacomponent inside the pair
		pair1 = new MasterDetailsPair(detailsComponent1.getName(), (DataComponent) detailsComponent1.clone());
		
		//Add template to list
		pairs = new ArrayList<MasterDetailsPair>();
		pairs.add(pair1);
		
		//Add to MasterDetailsComponent
		onePair.setTemplates(pairs);
		
		//Visit it
		onePair.accept(visitor3);
		
		//Check that it was visited, and that the string is not null since it was valid
		assertTrue(visitor3.wasVisited());
		assertEquals("", visitor3.getINIString());
		
		//Create a MasterDetailsComponent with one template, and one pair
		onePair.addMaster();
		
		//Edit the values of the pair
		onePair.getDetailsAtIndex(0).retrieveAllEntries().get(0).setValue(valueC1P1);
		onePair.getDetailsAtIndex(0).retrieveAllEntries().get(1).setValue(valueC2P1);
		
		//Visit it
		onePair.accept(visitor4);
		
		//Check that it was visited, and that the string is not null since it was valid
		assertTrue(visitor4.wasVisited());
		assertEquals("[" + pair1.getMaster() + "]\n" +
				"    " + entry1.getTag() + " = " + valueC1P1 + "\n" +
				"    " + entry2.getTag() + " = " + valueC2P1 + "\n\n", visitor4.getINIString());
		
		
		//Create a MasterDetailsComponent with one template, but two pairs.  This should return ONLY ONE pair in the INI format.
		onePair.addMaster();
		
		//Visit it
		onePair.accept(visitor5);
		
		//Check that it was visited, and that the string is not null since it was valid
		assertTrue(visitor5.wasVisited());
		assertEquals("[" + pair1.getMaster() + "]\n" +
				"    " + entry1.getTag() + " = " + valueC1P1 + "\n" +
				"    " + entry2.getTag() + " = " + valueC2P1 + "\n\n", visitor4.getINIString());
		
		//Create a MasterDetailsComponent with two templates, but one pair.
		twoPairs = new MasterDetailsComponent();
		
		//Create second template
		detailsComponent2 = new DataComponent();
		detailsComponent1.setName(name2);
		detailsComponent1.setDescription("Foo Component 2");
		detailsComponent2.addEntry((Entry)entry3.clone());
		detailsComponent2.addEntry((Entry)entry4.clone());
		
		//Create MasterDetailsPair 2 - clone it to separate the entries from the datacomponent inside the pair
		pair2 = new MasterDetailsPair(detailsComponent2.getName(), (DataComponent) detailsComponent2.clone());
		
		//Add template to list
		pairs = new ArrayList<MasterDetailsPair>();
		pairs.add(pair1);
		pairs.add(pair2);
		
		//Add to MasterDetailsComponent
		twoPairs.setTemplates(pairs);
		
		//Add master
		twoPairs.addMaster();
		
		//Edit the values of the pair
		twoPairs.getDetailsAtIndex(0).retrieveAllEntries().get(0).setValue(valueC1P1);
		twoPairs.getDetailsAtIndex(0).retrieveAllEntries().get(1).setValue(valueC2P1);
		
		//Visit it
		twoPairs.accept(visitor6);
		
		//Check that it was visited, and that the string is not null since it was valid
		assertTrue(visitor6.wasVisited());
		assertEquals("[" + pair1.getMaster() + "]\n" +
				"    " + entry1.getTag() + " = " + valueC1P1 + "\n" +
				"    " + entry2.getTag() + " = " + valueC2P1 + "\n\n", visitor6.getINIString());
		
		//Create a MasterDetailsComponent with two templates, and two pairs.
		
		//Add Master and set its instance value
		twoPairs.setMasterInstanceValue(twoPairs.addMaster(), detailsComponent2.getName());
		
		//Setup entries
		twoPairs.getDetailsAtIndex(1).retrieveAllEntries().get(0).setValue(valueC1P2);
		twoPairs.getDetailsAtIndex(1).retrieveAllEntries().get(1).setValue(valueC2P2);
		
		//Visit it
		twoPairs.accept(visitor7);
		
		//Check that it was visited, and that the string is not null since it was valid
		assertTrue(visitor7.wasVisited());
		assertEquals("[" + pair1.getMaster() + "]\n" +
				"    " + entry1.getTag() + " = " + valueC1P1 + "\n" +
				"    " + entry2.getTag() + " = " + valueC2P1 + "\n\n" +
				"[" + pair2.getMaster() + "]\n" +
				"    " + entry3.getTag() + " = " + valueC1P2 + "\n" +
				"    " + entry4.getTag() + " = " + valueC2P2 + "\n\n", visitor7.getINIString());
		
		//Create a MasterDetailsComponent with two templates, but three pairs.  The second pair being replicated twice.  Should only return two pairs in INI format.
		
		//Add Master
		twoPairs.addMaster();
		twoPairs.setMasterInstanceValue(2, detailsComponent2.getName());
		
		//Setup entries
		twoPairs.getDetailsAtIndex(2).retrieveAllEntries().get(0).setValue(valueC1P1);
		twoPairs.getDetailsAtIndex(2).retrieveAllEntries().get(1).setValue(valueC2P1);
		
		//Visit it
		twoPairs.accept(visitor8);
		
		//Check that it was visited, and that the string is not null since it was valid
		assertTrue(visitor8.wasVisited());
		assertEquals("[" + pair1.getMaster() + "]\n" +
				"    " + entry1.getTag() + " = " + valueC1P1 + "\n" +
				"    " + entry2.getTag() + " = " + valueC2P1 + "\n\n" +
				"[" + pair2.getMaster() + "]\n" +
				"    " + entry3.getTag() + " = " + valueC1P2 + "\n" +
				"    " + entry4.getTag() + " = " + valueC2P2 + "\n\n", visitor8.getINIString());
		
		//Show that an erroneous null was added to visitor2's accept, then it should reset the visitor
		visitor9.visit(nullComponent);
		assertFalse(visitor9.wasVisited());
		assertNull(visitor9.getINIString());
		
		//Show that visitors can be reused safely
		visitor9.visit(twoPairs);
		
		assertTrue(visitor9.wasVisited());
		assertEquals("[" + pair1.getMaster() + "]\n" +
				"    " + entry1.getTag() + " = " + valueC1P1 + "\n" +
				"    " + entry2.getTag() + " = " + valueC2P1 + "\n\n" +
				"[" + pair2.getMaster() + "]\n" +
				"    " + entry3.getTag() + " = " + valueC1P2 + "\n" +
				"    " + entry4.getTag() + " = " + valueC2P2 + "\n\n", visitor9.getINIString());
		
		
		
		//Check to state that if a key does not have a value, then to ignore it accordingly.
		multiPairs = new MasterDetailsComponent();
		
		//Setup DataComponent3
		detailsComponent3 = new DataComponent();
		detailsComponent3.setName(name3);
		detailsComponent3.setDescription("Foo Component 3");
		
		//Add Entries
		detailsComponent3.addEntry((Entry) entry5.clone());
		detailsComponent3.addEntry((Entry) entry6.clone());
		
		//Create MasterDetailsPair - clone it to separate the entries from the datacomponent inside the pair
		pair3 = new MasterDetailsPair(detailsComponent3.getName(), (DataComponent) detailsComponent3.clone());
		
		//Add template to list
		pairs = new ArrayList<MasterDetailsPair>();
		pairs.add(pair1);
		pairs.add(pair2);
		pairs.add(pair3);
		
		//Add to MasterDetailsComponent
		multiPairs.setTemplates(pairs);
		
		//Add masters and set instance of master
		multiPairs.setMasterInstanceValue(multiPairs.addMaster(), detailsComponent1.getName());
		multiPairs.setMasterInstanceValue(multiPairs.addMaster(), detailsComponent2.getName());
		multiPairs.setMasterInstanceValue(multiPairs.addMaster(), detailsComponent3.getName());
		
		//Setup entries
		
		//Index 0
		multiPairs.getDetailsAtIndex(0).retrieveAllEntries().get(0).setValue(valueC1P1);
		multiPairs.getDetailsAtIndex(0).retrieveAllEntries().get(1).setValue(valueC2P1);
		
		//Index 1
		multiPairs.getDetailsAtIndex(1).retrieveAllEntries().get(0).setValue(valueC1P2);
		multiPairs.getDetailsAtIndex(1).retrieveAllEntries().get(1).setValue(valueC2P2);
		
		//Index 2
		multiPairs.getDetailsAtIndex(2).retrieveAllEntries().get(0).setValue(valueC1P3);
		multiPairs.getDetailsAtIndex(2).retrieveAllEntries().get(1).setValue(valueC2P3);
		
		//visit it
		visitor10.visit(multiPairs);
		
		//Check that it was visited, and that the string is not null since it was valid - note that a K/V pair must have some content to show up on the list
		assertTrue(visitor10.wasVisited());
		assertEquals("[" + pair1.getMaster() + "]\n" +
				"    " + entry1.getTag() + " = " + valueC1P1 + "\n" +
				"    " + entry2.getTag() + " = " + valueC2P1 + "\n\n" +
				"[" + pair2.getMaster() + "]\n" +
				"    " + entry3.getTag() + " = " + valueC1P2 + "\n" +
				"    " + entry4.getTag() + " = " + valueC2P2 + "\n\n", visitor10.getINIString());
		
		//end-user-code
			
	}
	
	/**
	 * <!-- begin-UML-doc -->
	 * <p>This operation tests the visit operation for TimeDataComponent for creating iniString</p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkTimeDataComponentVisitation() {
		
		//begin-user-code
		
		//Local Declarations
		VisitorINI visitor = new VisitorINI();
		VisitorINI visitor2 = new VisitorINI();
		TimeDataComponent nullComponent = null;
		TimeDataComponent manyEntryComp;
		
		//Entries are auto generated on this class
		
		//test null
		visitor.visit(nullComponent);
		
		//Check that it was not visited and that nothing was set on the string
		assertFalse(visitor.wasVisited());
		assertNull(visitor.getINIString());
		
		//Pass a DataComponent with multiple entries
		
		//Create DataComponent
		manyEntryComp = new TimeDataComponent();
		
		//Run visit operation on DataComponent and verify the string returned is correct
		manyEntryComp.accept(visitor2);

		//Check values
		assertTrue(visitor2.wasVisited());
		assertEquals("[" + manyEntryComp.getName() + "]" 
				+"\n    " + manyEntryComp.retrieveAllEntries().get(0).getTag()+ " = " + "REGULAR"
				+"\n    " + manyEntryComp.retrieveAllEntries().get(1).getTag()+ " = " + manyEntryComp.retrieveAllEntries().get(1).getValue()
				+"\n    " + manyEntryComp.retrieveAllEntries().get(2).getTag()+ " = " + manyEntryComp.retrieveAllEntries().get(2).getValue()
				+"\n    " + manyEntryComp.retrieveAllEntries().get(3).getTag()+ " = " + manyEntryComp.retrieveAllEntries().get(3).getValue()
				+"\n    " + manyEntryComp.retrieveAllEntries().get(4).getTag()+ " = " + manyEntryComp.retrieveAllEntries().get(4).getValue() + "\n    ", visitor2.getINIString());
		
		//end-user-code
		
	}

}
