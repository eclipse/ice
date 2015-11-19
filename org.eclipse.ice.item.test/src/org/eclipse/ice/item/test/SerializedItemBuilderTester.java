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
package org.eclipse.ice.item.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.SerializedItemBuilder;
import org.junit.Test;

/**
 * <p>
 * The SerializedItemBuilderTester is responsible for testing the
 * SerializedItemBuilder. It is primarily concerned with ensuring that the
 * SerializedItemBuilder can load an Item from an InputStream and return the
 * proper name and type.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class SerializedItemBuilderTester {
	/**
	 * <p>
	 * An Item reference used in the testing.
	 * </p>
	 * 
	 */
	private Item serializedItem;
	/**
	 * <p>
	 * The SerializedItemBuilder under test.
	 * </p>
	 * 
	 */
	private SerializedItemBuilder serializedItemBuilder;

	/**
	 * <p>
	 * This operation checks the SerializedItemBuilder using a Painfully Simple
	 * Form (PSF) file. It checks the name and type of the Item and tries to
	 * create and check a SerializedItem initialized from the PSF.
	 * </p>
	 * 
	 */
	@Test
	public void checkLoadingFromPSF() {

		// Local Declarations
		ByteArrayInputStream stream = null;
		Form form = null;
		DataComponent dataComp1 = null, dataComp2 = null;
		Entry entry = null;
		String psfItemString = "";

		// Local Declarations
		ArrayList<String> PSFForm = new ArrayList<String>();

		// Setup the string containing the Form in PSF format. This was
		// taken from the Painfully Simple Form article at
		// https://sourceforge.net/apps/mediawiki/niceproject/index.php?title=ICE_Painfully_Simple_Form
		// and is a good example because it is complete, contains lots of
		// whitespace and comments and, of course, interesting! I have added
		// whitespaces and comments in some places to make the test more
		// rigorous and changed some of the comment statements from "//" to "#"
		// to cover all the possibilities.
		PSFForm.add("\t  \n");
		PSFForm.add("#Form name and type\n");
		PSFForm.add("formName=PSF Wiki Article Form\n");
		PSFForm.add("formDescription=A PSF Wiki Article Sample\n");
		PSFForm.add("formType=Model\n");
		PSFForm.add(" \n");
		PSFForm.add("#The DataComponents block - it must come first!\n");
		PSFForm.add("group=Assembly\n");
		PSFForm.add("groupDescription=Relevant quantities for modeling a full assembly\n");
		PSFForm.add("\n");
		PSFForm.add("#The Entry blocks will appear below this line");
		PSFForm.add("\n");
		PSFForm.add("\t  \n");
		PSFForm.add("#Some comments to ignore at the top\n");
		PSFForm.add("//More comments to ignore at the top\n");
		PSFForm.add("name=Coolant Temperature                        "
				+ "                                #The name that a user "
				+ "will see\n");
		PSFForm.add("description=The temperature of the coolant that surrounds "
				+ "the assembly and pins //A description that will help the user\n");
		PSFForm.add("defaultValue=550                                          "
				+ "                      //The default value\n");
		PSFForm.add("allowedValueType=Continuous                               "
				+ "                      //Indicates that the value can be "
				+ "anything between 550 and 650 K.\n");
		PSFForm.add("allowedValue=550                                          "
				+ "                      //The lower bound of the range\n");
		PSFForm.add("allowedValue=650                                          "
				+ "                      //The upper bound of the range\n");
		PSFForm.add("tag=coolantTemperature                                    "
				+ "                      //A tag to mark it\n");
		PSFForm.add("parent=Full Assembly Flag                         "
				+ "                              //The parent\n");
		PSFForm.add("group=Assembly                                       "
				+ "                      //The group\n");
		PSFForm.add("  \t  \n");
		PSFForm.add("name=Number of Pins\n");
		PSFForm.add("description=The number of pins in an assembly\n");
		PSFForm.add("defaultValue=289\n");
		PSFForm.add("allowedValueType=Discrete\n");
		PSFForm.add("allowedValue=196\n");
		PSFForm.add("allowedValue=289\n");
		PSFForm.add("tag=numberOfPins\n");
		PSFForm.add("parent=Full Assembly Flag\n");
		PSFForm.add("group=Assembly\n");
		PSFForm.add("  \t  \n");
		PSFForm.add("name=Full Assembly Flag\n");
		PSFForm.add("description=True if a full assembly should be modeled, false if not\n");
		PSFForm.add("defaultValue=false\n");
		PSFForm.add("allowedValueType=Discrete\n");
		PSFForm.add("allowedValue=true\n");
		PSFForm.add("allowedValue=false\n");
		PSFForm.add("tag=fullAssemblyFlag\n");
		PSFForm.add("group=Assembly\n");

		// Convert the ArrayList to a string
		for (String i : PSFForm) {
			psfItemString += i;
		}
		System.out.println("Printing stream:\n" + psfItemString);

		// Create the InputStream
		stream = new ByteArrayInputStream(psfItemString.getBytes());

		// Setup the builder
		try {
			serializedItemBuilder = new SerializedItemBuilder(stream);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		// Check the name
		assertEquals("PSF Wiki Article Form",
				serializedItemBuilder.getItemName());
		// Check the type
		assertEquals(ItemType.Model, serializedItemBuilder.getItemType());

		// Build and Item and check its Form
		serializedItem = serializedItemBuilder.build(null);
		form = serializedItem.getForm();
		assertNotNull(form);
		// Check the Form in some random spots - FIXME: Use Form.equals() once
		// AJM implements it!
		assertEquals("PSF Wiki Article Form", form.getName());
		assertEquals("A PSF Wiki Article Sample", form.getDescription());
		assertEquals(1, form.getNumberOfComponents());
		dataComp1 = (DataComponent) form.getComponent(1);
		dataComp2 = (DataComponent) form.getComponent(2);
		assertNotNull(dataComp1);
		assertEquals("Assembly", dataComp1.getName());
		assertEquals(null, dataComp2);// There should only be one component!
		entry = dataComp1.retrieveEntry("Number of Pins");
		assertNotNull(entry);
		assertEquals("289", entry.getDefaultValue());
		assertEquals("numberOfPins", entry.getTag());

		// If we've made it this far, then

		// Try loading the Item from a null stream, which should result in an
		// exception
		boolean exceptionCaught = false;
		try {
			serializedItemBuilder = new SerializedItemBuilder(null);
		} catch (IOException e) {
			// Mark the flag as true.
			exceptionCaught = true;
		}
		// Make sure the exception was caught
		assertTrue(exceptionCaught);

		return;

	}
}