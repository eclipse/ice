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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.painfullySimpleForm.PainfullySimpleEntry;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The PainfullySimpleEntryTester is responsible for testing the
 * PainfullySimpleEntry class. It is primarily concerned with checking the
 * ability of the PainfullySimpleEntry to load itself from a block from a PSF
 * file.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author jaybilly
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class PainfullySimpleEntryTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private PainfullySimpleEntry painfullySimpleEntry;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An arraylist of strings for PSF.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<String> PSFEntry;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the tests for PainfullySimpleEntryTester.
	 * Annotated with an @Before clause.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Before
	public void setupTests() {
		// begin-user-code
		// Local Declarations
		PSFEntry = new ArrayList<String>();

		// Setup the string containing the Entry block in PSF format. This was
		// taken from the Painfully Simple Form article at
		// https://sourceforge.net/apps/mediawiki/niceproject/index.php?title=ICE_Painfully_Simple_Form
		// and is a good example because it is complete, contains lots of
		// whitespace and comments and, of course, interesting! I have added
		// whitespaces lines at the top and the bottom to make the test more
		// rigorous and changed some of the comment statements from "//" to "#"
		// to cover all the possibilities.
		PSFEntry.add("\t  \n");
		PSFEntry.add("#Some comments to ignore at the top\n");
		PSFEntry.add("//More comments to ignore at the top\n");
		PSFEntry.add("name=Coolant Temperature                        "
				+ "                                #The name that a user "
				+ "will see\n");
		PSFEntry.add("description=The temperature of the coolant that surrounds "
				+ "the assembly and pins //A description that will help the user\n");
		PSFEntry.add("defaultValue=550                                          "
				+ "                      //The default value\n");
		PSFEntry.add("allowedValueType=Continuous                               "
				+ "                      //Indicates that the value can be "
				+ "anything between 550 and 650 K.\n");
		PSFEntry.add("allowedValue=550                                          "
				+ "                      //The lower bound of the range\n");
		PSFEntry.add("allowedValue=650                                          "
				+ "                      //The upper bound of the range\n");
		PSFEntry.add("tag=coolantTemperature                                    "
				+ "                      //A tag to mark it\n");
		PSFEntry.add("parent=Full Assembly Flag                         "
				+ "                              //The parent\n");
		PSFEntry.add("group=Coolant Group                                       "
				+ "                      //The group\n");
		PSFEntry.add("  \t  \n");

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the PainfullySimpleEntry by loading it from a
	 * string representation of an Entry block from a PSF.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkLoadingFromPSFBlock() {
		// begin-user-code

		// Uses the Preloaded PSF file for testing.

		// Print the block for diagnostics
		System.out.println("Dumping PSF Block for Diagnostics:\n" + PSFEntry);

		// Create the Entry and load it
		painfullySimpleEntry = new PainfullySimpleEntry();
		try {
			painfullySimpleEntry.loadFromPSFBlock(PSFEntry);
		} catch (Exception e2) {
			fail();
		}

		// Print the tag for diagnostics
		System.out.println("PainfullySimpleEntry tag value = "
				+ painfullySimpleEntry.getTag());

		// Create a duplicate Entry programmatically
		Entry duplicateEntry = new Entry() {
			@Override
			protected void setup() {
				setName("Coolant Temperature");
				setDescription("The temperature of the coolant " + ""
						+ "that surrounds the assembly and pins");
				defaultValue = "550";
				allowedValueType = AllowedValueType.Continuous;
				allowedValues.add("550");
				allowedValues.add("650");
				tag = "coolantTemperature";
			}
		};
		duplicateEntry.setParent("Full Assembly Flag");

		// Check the group, which is not an attribute on Entry and will not be
		// checked
		assertEquals("Coolant Group", painfullySimpleEntry.getGroup());

		// Make sure the two Entries are equal
		assertTrue(duplicateEntry.equals(painfullySimpleEntry));

		// Now replace the AllowedValueType string with the Discrete type to
		// check that it can be parsed correctly
		PSFEntry.set(
				6,
				"allowedValueType=Discrete                               "
						+ "                      //Indicates that the value can be "
						+ "any one of 550, 575 and 650 K.\n");
		// Add 575K to the block. Note order matters for Arraylist equality!
		PSFEntry.add(8,
				"allowedValue=575                                          "
						+ "                      //The mid-range value\n");

		// Print the block for diagnostics
		System.out.println("Dumping PSF Block for Diagnostics:\n" + PSFEntry);

		// Create a duplicate Entry programmatically, but with
		// AllowedValueType.Discrete. Also add 575K.
		duplicateEntry = new Entry() {
			@Override
			protected void setup() {
				setName("Coolant Temperature");
				setDescription("The temperature of the coolant " + ""
						+ "that surrounds the assembly and pins");
				defaultValue = "550";
				allowedValueType = AllowedValueType.Discrete;
				allowedValues.add("550");
				allowedValues.add("575");
				allowedValues.add("650");
				parent = "Full Assembly Flag";
				tag = "coolantTemperature";
			}
		};

		// Re-create the Entry and load it
		painfullySimpleEntry = new PainfullySimpleEntry();
		try {
			painfullySimpleEntry.loadFromPSFBlock(PSFEntry);
		} catch (Exception e1) {
			fail();
		}

		// Make sure the Entries are equal
		assertTrue(duplicateEntry.equals(painfullySimpleEntry));

		// Finally replace the AllowedValueType string with the Undefined type
		// to
		// check that it can be parsed correctly
		PSFEntry.set(
				6,
				"allowedValueType=Undefined                               "
						+ "                      //Indicates that the value can be "
						+ "any thing (which would be bad!).\n");
		// Remove the temperature blocks
		PSFEntry.remove(7);
		PSFEntry.remove(7);
		PSFEntry.remove(7);

		// Print the block for diagnostics
		System.out.println("Dumping PSF Block for Diagnostics:\n" + PSFEntry);

		// Create a duplicate Entry programmatically, but with
		// AllowedValueType.Discrete. Also add 575K.
		duplicateEntry = new Entry() {
			@Override
			protected void setup() {
				setName("Coolant Temperature");
				setDescription("The temperature of the coolant " + ""
						+ "that surrounds the assembly and pins");
				defaultValue = "550";
				allowedValueType = AllowedValueType.Undefined;
				parent = "Full Assembly Flag";
				tag = "coolantTemperature";
			}
		};

		// Re-create the Entry and load it
		painfullySimpleEntry = new PainfullySimpleEntry();
		try {
			painfullySimpleEntry.loadFromPSFBlock(PSFEntry);
		} catch (Exception e) {
			fail();
		}

		// Make sure the Entries are equal
		assertTrue(duplicateEntry.equals(painfullySimpleEntry));

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the loadingFromPSFBlock() on PainfullySimpleEntry
	 * for an invalid Type. Should throw an IOException.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @throws IOException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test()
	public void checkLoadingFromPSFBlockwithInvalidType() throws IOException {
		// begin-user-code
		// Do one last check to make sure an exception is thrown if a bad flag
		// is detected. In this case, it should be "allowedValueType" not
		// "allowedType."

		// The operation spec says that the key-value pair will be in the
		// text of the exception. Check it.
		thrown.expect(IOException.class); // Expected exception to catch.
		thrown.expectMessage("allowedType=Undefined"); // Part of message

		// Perform test
		PSFEntry.set(6, "allowedType=Undefined                               "
				+ "                      //Indicates that the value can be "
				+ "any thing (which would be bad!).\n");

		// Re-create the Entry and load it
		painfullySimpleEntry = new PainfullySimpleEntry();
		painfullySimpleEntry.loadFromPSFBlock(PSFEntry);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the loadingFromPSFBlock() on PainfullySimpleEntry
	 * for a missing equals sign. Should throw an IOException.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @throws Class
	 * @throws IOException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test(expected = java.io.IOException.class)
	public void checkLoadingFromPSFBlockwithMissingEquals() throws IOException {
		// begin-user-code
		// Now replace the AllowedValueType string with an missing value and
		// equals sign. The parser should throw an exception.
		PSFEntry.set(6, "allowedValueType");

		// Print the block for diagnostics
		System.out.println("Dumping PSF Block for Diagnostics:\n" + PSFEntry);

		// Create a duplicate Entry programmatically, but with
		// AllowedValueType.Discrete. Also add 575K.
		Entry duplicateEntry = new Entry() {
			@Override
			protected void setup() {
				setName("Coolant Temperature");
				setDescription("The temperature of the coolant " + ""
						+ "that surrounds the assembly and pins");
				defaultValue = "550";
				allowedValueType = AllowedValueType.Discrete;
				allowedValues.add("550");
				allowedValues.add("575");
				allowedValues.add("650");
				parent = "Full Assembly Flag";
				tag = "coolantTemperature";
			}
		};

		// Re-create the Entry and load it
		painfullySimpleEntry = new PainfullySimpleEntry();
		// This should catch an error for missing equals sign!
		painfullySimpleEntry.loadFromPSFBlock(PSFEntry);

		// end-user-code
	}
}