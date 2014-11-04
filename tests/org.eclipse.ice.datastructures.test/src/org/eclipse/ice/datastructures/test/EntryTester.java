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
package org.eclipse.ice.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The EntryTester is responsible for testing the Entry class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 */
public class EntryTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 */
	private Entry entry;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation tests the Entry by creating an Entry using the simple
	 * constructor. It checks that the name and ID are set to proper values
	 * while all remaining values are set to the defaults values for an Entry.
	 * It also checks the ability of the Entry to report whether or not it is
	 * secret.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void createSimpleEntry() {
		// begin-user-code

		// Local Declarations
		String parentName = "Clark Griswald";

		// Create the simple entry
		entry = new Entry();
		entry.setId(1);
		entry.setName("Simple Entry");
		entry.setParent(parentName);
		entry.setTag("ChevyChase");

		// Check the name
		assertEquals("Simple Entry", entry.getName());
		// Check the id
		assertEquals(entry.getId(), 1);
		// Check the description
		assertEquals("Entry 1", entry.getDescription());
		// Check the allowed values
		assertEquals(0, entry.getAllowedValues().size());
		// Check the default values
		assertEquals("", entry.getDefaultValue());
		// Check the readiness state
		assertEquals(true, entry.isReady());
		// Check the changed state
		assertEquals(false, entry.isModified());
		// By default the Entry should not be secret
		assertTrue(!entry.isSecret());
		// Make sure the parent name is Clark's
		assertEquals(parentName, entry.getParent());
		// Make sure the Entry's tag is correct
		assertEquals("ChevyChase", entry.getTag());
		// Make sure the Entry is not required by default
		assertFalse(entry.isRequired());
		// Change its required state and make sure it updated properly.
		entry.setRequired(true);
		assertTrue(entry.isRequired());
		// Check turning it back to not required.
		entry.setRequired(false);
		assertFalse(entry.isRequired());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation tests the readyState attribute of the Entry by first
	 * trying to set the readiness state using setReady() and then calling
	 * isReady().
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkReadiness() {
		// begin-user-code

		// Local Declarations
		String parentName = "Clark Griswald";

		// Create the test entry
		entry = new Entry();
		entry.setId(3);
		entry.setName("Ready Entry");

		// Set the readiness to true
		entry.setReady(true);
		// Check the readiness
		assertEquals(true, entry.isReady());
		// Set the readiness to false
		entry.setReady(false);
		// Check the readiness
		assertEquals(false, entry.isReady());

		// Set a parent
		entry.setParent(parentName);

		// Since the readiness is false, just go ahead and pass the update.
		entry.update(parentName, "ready");

		// Ready should be true since the parent name was passed with the ready
		// key.
		assertTrue(entry.isReady());
		// The Entry should now be marked as unmodified.
		assertTrue(!entry.isModified());

		// Passing null values for either argument should not change the
		// readiness of the Entry.
		entry.update(parentName, null);
		assertTrue(entry.isReady());
		entry.update(null, "blah");
		assertTrue(entry.isReady());
		entry.update(null, null);
		assertTrue(entry.isReady());

		// Passing the keys in the wrong order shouldn't change the readiness
		// either.
		entry.update("ready", parentName);
		assertTrue(entry.isReady());

		// Tell the Entry that it shouldn't be ready and make sure it updated.
		entry.update(parentName, "not ready");
		assertTrue(!entry.isReady());
		// The Entry should not be marked as modified.
		assertTrue(!entry.isModified());

		// Tell the Entry to update with "yes"
		entry.update(parentName, "yes");
		assertTrue(entry.isReady());
		assertTrue(!entry.isModified());

		// Tell the Entry that it shouldn't be ready with "no"
		entry.update(parentName, "no");
		assertTrue(!entry.isReady());
		assertTrue(!entry.isModified());

		// Tell the Entry to update with "y"
		entry.update(parentName, "y");
		assertTrue(entry.isReady());
		assertTrue(!entry.isModified());

		// Tell the Entry that it shouldn't be ready with "n"
		entry.update(parentName, "n");
		assertTrue(!entry.isReady());
		assertTrue(!entry.isModified());

		// Tell the Entry to update with "true"
		entry.update(parentName, "true");
		assertTrue(entry.isReady());
		assertTrue(!entry.isModified());

		// Tell the Entry that it shouldn't be ready with "false"
		entry.update(parentName, "false");
		assertTrue(!entry.isReady());
		assertTrue(!entry.isModified());

		// Tell the Entry to update with "enabled"
		entry.update(parentName, "enabled");
		assertTrue(entry.isReady());
		assertTrue(!entry.isModified());

		// Tell the Entry that it shouldn't be ready with "disabled"
		entry.update(parentName, "disabled");
		assertTrue(!entry.isReady());
		assertTrue(!entry.isModified());

		// Tell the Entry to update with "enabled"
		entry.update(parentName, "on");
		assertTrue(entry.isReady());
		assertTrue(!entry.isModified());

		// Tell the Entry that it shouldn't be ready with "disabled"
		entry.update(parentName, "off");
		assertTrue(!entry.isReady());
		assertTrue(!entry.isModified());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation tries to set the value stored in the Entry.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkValue() {
		// begin-user-code

		// Create the test Entry, just use the default since the string
		// doesn't need to be checked for validity.
		entry = new Entry() {
			@Override
			protected void setup() {
				defaultValue = "SynthOne";
			}
		};
		entry.setId(4);
		entry.setName("Value Entry");

		// Make sure that the correct value is returned. Since setValue has not
		// been called, getValue should return the default value.
		assertNotNull(entry.getValue());
		assertEquals(entry.getValue(), entry.getDefaultValue());

		// Set the value and check that the acceptance code is valid.
		// Strings are merely accepted for AllowedValueType.Undefined.
		assertEquals(true, entry.setValue("Outshined."));

		// check value to make sure no error is set.
		assertNull(entry.getErrorMessage());
		// Check the value
		assertEquals("Outshined.", entry.getValue());
		// Make sure that the Entry's change state is true
		assertEquals(true, entry.isModified());

		// Create the full entry, allowedValueType = AllowedValueType.Discrete
		entry = new Entry() {

			@Override
			protected void setup() {
				allowedValues = new ArrayList<String>(2);
				allowedValues.add("true");
				allowedValues.add("false");
				defaultValue = "true";
				allowedValueType = AllowedValueType.Discrete;
				// Using the setters instead of setting the member variables
				// helps check the IEntryContentProvider.
				setParent("Otis Redding");
				setTag("theseArmsOfMine");
			}
		};
		entry.setId(4);
		entry.setName("Full Valid Entry");
		// Make sure the call to Entry.setValue returns true
		// if the value is accepted
		assertEquals(true, entry.setValue("true"));
		// Check the value
		assertEquals("true", entry.getValue());
		// Check the parent
		assertEquals("Otis Redding", entry.getParent());
		// Check the tag
		assertEquals("theseArmsOfMine", entry.getTag());
		// Make sure that the Entry's change state is true
		assertEquals(true, entry.isModified());
		// Make sure the call to Entry.setValue returns true
		// if the value is accepted
		assertEquals(true, entry.setValue("false"));
		// Check the value
		assertEquals("false", entry.getValue());
		// Make sure that the Entry's change state is true
		assertEquals(true, entry.isModified());
		// Make sure the the Entry returns false
		// the value is not accepted
		assertEquals(false, entry.setValue("Overburdened."));
		assertEquals(
				"'Overburdened.' is an unacceptable value. The value must be one of true, or false.",
				entry.getErrorMessage());
		// Set value back to make sure the error is false
		assertEquals(true, entry.setValue("true"));
		// Check value to make sure no error is set.
		assertNull(entry.getErrorMessage());

		// Recreate the full entry, but this time with
		// AllowedValueType.Continuous and doubles
		entry = new Entry() {

			@Override
			protected void setup() {
				allowedValues = new ArrayList<String>(2);
				allowedValues.add("0.0");
				allowedValues.add("0.2");
				this.defaultValue = "0.1";
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// check value to make sure no error is set.
		assertNull(entry.getErrorMessage());
		entry.setId(4);
		entry.setName("Value Entry");
		// Make sure the call to Entry.setValue can check the bounds
		assertEquals(true, entry.setValue("0.088"));
		// Check the value
		assertEquals("0.088", entry.getValue());
		// Make sure that the Entry's change state is true
		assertEquals(true, entry.isModified());
		// Check an invalid Entry
		assertEquals(false, entry.setValue("1.88"));
		assertEquals(
				"'1.88' is an unacceptable value. The value must be between 0.0 and 0.2.",
				entry.getErrorMessage());

		// Recreate the full entry, but this time with
		// AllowedValueType.Continuous and integers
		entry = new Entry() {

			@Override
			protected void setup() {
				allowedValues = new ArrayList<String>(2);
				allowedValues.add("0");
				allowedValues.add("2");
				this.defaultValue = "1";
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// check value to make sure no error is set.
		assertNull(entry.getErrorMessage());
		entry.setId(4);
		entry.setName("Value Entry");
		// Make sure the call to Entry.setValue can check the bounds
		assertEquals(true, entry.setValue("1"));
		// Check the value
		assertEquals("1", entry.getValue());
		// Make sure that the Entry's change state is true
		assertEquals(true, entry.isModified());
		// Check an invalid Entry
		assertEquals(false, entry.setValue("3"));
		// end-user-code

		// Discrete test - error message catching - list of one size
		entry = new Entry() {

			@Override
			protected void setup() {
				allowedValues = new ArrayList<String>(2);
				allowedValues.add("true");
				this.defaultValue = "true";
				this.allowedValueType = AllowedValueType.Discrete;
			}
		};
		assertEquals(false, entry.setValue("false"));
		assertEquals(
				"'false' is an unacceptable value. The value must be one of true.",
				entry.getErrorMessage());

		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the changed state of the Entry by first setting the
	 * value of the Entry and then making sure that isModified() returns true.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkChanged() {
		// begin-user-code
		// Create the test Entry
		entry = new Entry();
		entry.setId(5);
		entry.setName("Changed Entry");

		// Make sure the Entry starts in an unchanged (false) state
		assertEquals(false, entry.isModified());
		// Set the value
		entry.setValue("Get stoned.");
		// Make sure that the Entry's change state is true
		assertEquals(true, entry.isModified());
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the Entry class to insure that its copy operation
	 * works.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkEquality() {
		// begin-user-code

		// Local Declarations
		Entry copyOfEntry, otherEntry;
		String parentName = "Clark Griswald";

		// Setup the base Entry
		entry = new Entry() {

			@Override
			protected void setup() {
				allowedValues = new ArrayList<String>(2);
				allowedValues.add("8675308");
				allowedValues.add("8675310");
				this.defaultValue = "1";
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		entry.setId(6);
		entry.setName("Copy Test Entry");
		entry.setDescription("Fluffy Bunny");
		entry.setValue("8675309");
		entry.setParent(parentName);
		entry.setTag("ChevyChase");
		entry.setRequired(true);

		// Setup the copy
		copyOfEntry = new Entry() {

			@Override
			protected void setup() {
				allowedValues = new ArrayList<String>(2);
				allowedValues.add("8675308");
				allowedValues.add("8675310");
				this.defaultValue = "1";
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		copyOfEntry.setId(entry.getId());
		copyOfEntry.setName(entry.getName());
		copyOfEntry.setDescription(entry.getDescription());
		copyOfEntry.setValue(entry.getValue());
		copyOfEntry.setParent(parentName);
		copyOfEntry.setTag("ChevyChase");
		copyOfEntry.setRequired(true);
		
		// Setup a different Entry
		otherEntry = new Entry();

		// Test Entry.equals(), first one should be true, second false
		assertEquals(entry.equals(copyOfEntry), true);
		assertEquals(entry.equals(otherEntry), false);

		// Check Entry.hashcode(), first one should be true, second true,
		// third false and fourth false
		assertEquals(entry.hashCode(), entry.hashCode());
		assertEquals(entry.hashCode(), copyOfEntry.hashCode());
		copyOfEntry.setId(444);
		assertEquals(entry.hashCode() == copyOfEntry.hashCode(), false);
		assertEquals(entry.hashCode() == otherEntry.hashCode(), false);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the Entry to ensure that its copy() and clone()
	 * operations work as specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

		// Local Declarations
		String parentName = "Clark Griswold";

		/*
		 * The following sets of operations will be used to test the
		 * "clone and copy" portion of Entry.
		 */

		// Test to show valid usage of copy

		// Setup the base Entry
		entry = new Entry() {

			@Override
			protected void setup() {
				allowedValues = new ArrayList<String>(2);
				allowedValues.add("8675308");
				allowedValues.add("8675310");
				this.defaultValue = "1";
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		entry.setId(6);
		entry.setName("Copy Test Entry");
		entry.setDescription("Fluffy Bunny");
		entry.setValue("8675309");
		entry.setParent(parentName);
		entry.setTag("ChevyChase");
		entry.setRequired(true);

		// Create a new instance of Entry and copy contents
		Entry entryCopy = new Entry();
		entryCopy.copy(entry);

		// Check contents
		assertTrue(entry.equals(entryCopy));

		// Test to show valid usage of clone

		// Run clone operation
		Entry cloneEntry = (Entry) entry.clone();

		// Check contents
		assertTrue(entry.equals(cloneEntry));

		// Call copy with null, which should not change anything
		entry.copy(null);

		// Check contents - nothing has changed
		assertTrue(entry.equals(entryCopy));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of the Entry to persist itself to XML
	 * and to load itself from an XML input stream.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkXMLPersistence() {
		// begin-user-code
		/*
		 * The following sets of operations will be used to test the
		 * "read and write" portion of the Entry. It will demonstrate the
		 * behavior of reading and writing from an
		 * "XML (inputStream and outputStream)" file. It will use an annotated
		 * Entry to demonstrate basic behavior.
		 */

		// Local declarations
		Entry entry, entry2;
		String notAnXMLString = "A String not in XML";
		String parentName = "Clark Griswald";

		// Fill out Entry and override setup
		Entry myEntry = new Entry() {

			@Override
			protected void setup() {
				allowedValues = new ArrayList<String>(2);
				allowedValues.add("8675308");
				allowedValues.add("8675310");
				this.defaultValue = "1";
				this.allowedValueType = AllowedValueType.Continuous;
				this.secretFlag = true;
				this.ready = false;
				this.changeState = true;

			}
		};
		myEntry.setId(1);
		myEntry.setName("Simple Entry");
		myEntry.setParent(parentName);
		myEntry.setTag("ChevyChase");

		// Demonstrate a basic "write" to file. Should not fail

		// persist to an output stream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		myEntry.persistToXML(outputStream);
		System.err.println(outputStream.toString());

		// Demonstrate a basic read in. Create file in memory and convert to an
		// inputstream.
		InputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Initialize object and pass inputStream to read()
		entry2 = new Entry();
		entry2.loadFromXML(inputStream);
		System.out.println(entry2.getAllowedValues());

		// Check contents - currently broken due to isReady() needs to return a
		// class Boolean
		// not an attribute s4h
		assertTrue(myEntry.equals(entry2));

		// The next following tests demonstrate behavior for when you pass null
		// args for read()

		// test for read - null args
		entry = new Entry();
		entry.setId(1);
		entry.setName("Simple Entry");

		// Set entry equal to entry2
		entry2 = (Entry) entry.clone();

		// load from XML
		entry.loadFromXML(null);

		// checkContents - nothing has changed
		assertTrue(entry2.equals(entry));

		// args for write() - null args
		entry = new Entry();
		outputStream = null;
		entry.persistToXML(outputStream);
		// Since operation returns, outputStream should still be null
		assertNull(outputStream);

		// This test will demonstrate what happens when inputStream is not an
		// XMLFile for read()

		// Initialize variables
		inputStream = new ByteArrayInputStream(notAnXMLString.getBytes());
		entry = new Entry();
		entry.setId(1);
		entry.setName("Simple Entry");

		// Set entry equal to entry2
		entry2 = (Entry) entry.clone();

		// run method
		entry.loadFromXML(inputStream);

		// checkContents - nothing has changed
		assertTrue(entry2.equals(entry));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the Entry(IEntryContentProvider) method.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void checkContentProviderConstructor() {
		// begin-user-code
		// Local Declarations
		BasicEntryContentProvider contentProvider = new BasicEntryContentProvider();
		ArrayList<String> goodValues = new ArrayList<String>();
		goodValues.add("Yabba");
		goodValues.add("Dabba");
		goodValues.add("Dooo!");
		Entry entry, overriddenEntry = null;

		// Setup the content provider
		contentProvider.setAllowedValues(goodValues);
		contentProvider.setParent("Parental Figure");
		contentProvider.setTag("tagorama!");
		contentProvider.setAllowedValueType(AllowedValueType.Discrete);
		contentProvider.setDefaultValue("Yabba");

		// Create entry
		entry = new Entry(contentProvider);

		// Check contents
		assertEquals(contentProvider.getAllowedValues(),
				entry.getAllowedValues());
		assertEquals(contentProvider.getAllowedValueType(),
				entry.getValueType());
		assertEquals(contentProvider.getTag(), entry.getTag());
		assertEquals(contentProvider.getDefaultValue(), entry.getDefaultValue());
		assertEquals(contentProvider.getParent(), entry.getParent());

		// Create overridden Entry
		overriddenEntry = new Entry(contentProvider) {
			@Override
			protected void setup() {
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("3");
				this.allowedValues.add("5");
				this.allowedValueType = AllowedValueType.Continuous;
				this.defaultValue = "Foo";
				this.parent = "ORPHAN!";
				this.tag = "Not a tag";
				this.value = "4";
			}
		};

		// Check contents - forces changes by contentProvider
		assertEquals(contentProvider.getAllowedValues(),
				overriddenEntry.getAllowedValues());
		assertEquals(contentProvider.getAllowedValueType(),
				overriddenEntry.getValueType());
		assertEquals(contentProvider.getTag(), overriddenEntry.getTag());
		assertEquals(contentProvider.getDefaultValue(),
				overriddenEntry.getDefaultValue());
		assertEquals(contentProvider.getParent(), overriddenEntry.getParent());
		assertEquals("", overriddenEntry.getValue()); // Resets the value to
														// empty!

		// Change contentProvider on the fly.
		overriddenEntry = new Entry() {
			@Override
			protected void setup() {
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("3");
				this.allowedValues.add("5");
				this.allowedValueType = AllowedValueType.Continuous;
				this.defaultValue = "Foo";
				this.parent = "ORPHAN!";
				this.tag = "Not a tag";
				this.value = "4";
			}
		};

		// Change the content provider
		overriddenEntry.setContentProvider(contentProvider);

		// Check contents - forces changes by contentProvider
		assertEquals(contentProvider.getAllowedValues(),
				overriddenEntry.getAllowedValues());
		assertEquals(contentProvider.getAllowedValueType(),
				overriddenEntry.getValueType());
		assertEquals(contentProvider.getTag(), overriddenEntry.getTag());
		assertEquals(contentProvider.getDefaultValue(),
				overriddenEntry.getDefaultValue());
		assertEquals(contentProvider.getParent(), overriddenEntry.getParent());
		assertEquals("", overriddenEntry.getValue()); // Resets the value to
														// empty!

		// Null check

		// Change the content provider
		overriddenEntry.setContentProvider(null);

		// Check contents - nothing has changed
		assertEquals(contentProvider.getAllowedValues(),
				overriddenEntry.getAllowedValues());
		assertEquals(contentProvider.getAllowedValueType(),
				overriddenEntry.getValueType());
		assertEquals(contentProvider.getTag(), overriddenEntry.getTag());
		assertEquals(contentProvider.getDefaultValue(),
				overriddenEntry.getDefaultValue());
		assertEquals(contentProvider.getParent(), overriddenEntry.getParent());
		assertEquals("", overriddenEntry.getValue()); // Resets the value to
														// empty!

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation tests the Entry to insure that it can properly dispatch
	 * notifications when its value changes.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkNotifications() {
		// begin-user-code

		// Setup the listener
		TestComponentListener testComponentListener = new TestComponentListener();

		// Setup the Entry
		entry = new Entry();
		// Register the listener
		entry.register(testComponentListener);

		// Set the value of the Entry
		entry.setValue("Investment");
		// Check the Listener
		assertTrue(testComponentListener.wasNotified());
		// Reset the listener
		testComponentListener.reset();

		// The test case where Entries do not notify listeners if their values
		// do not change upon a setValue() call is not checked here. This
		// operation can not be checked with the TestComponentListener. This
		// more of an optimization than a functional requirement, anyway, so it
		// should be no big deal. The presence of this optimization can be
		// easily verified by running ICE and triggering event on any Entry
		// with the UI. ~JJB 20130224 16:49

		return;
		// end-user-code
	}

}