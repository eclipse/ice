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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import org.junit.Test;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.action.TaggedOutputWriterAction;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for checking that the TaggedOutputWriter action can
 * write a set of key-value pairs to an output file.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TaggedOutputWriterActionTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TaggedOutputWriterAction taggedOutputWriterAction;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks that the TaggedOutputWriter action can write a set
	 * of key-value pairs to an output file.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkWriting() {
		// begin-user-code

		// Local Declarations
		Hashtable<String, String> testDictionary = new Hashtable<String, String>();

		// Create a dictionary
		testDictionary.put("Kirk", "captain");
		testDictionary.put("Spock", "first officer");
		testDictionary.put("Bones", "chief medical officer");
		testDictionary.put("Scotty", "chief engineer");

		// Load and execute the action - make sure it returns the proper return
		// value
		taggedOutputWriterAction = new TaggedOutputWriterAction();
		// First make sure it returns FormStatus.InfoError if it receives null
		assertEquals(FormStatus.InfoError,
				taggedOutputWriterAction.execute(null));
		// Second make sure that it returns FormStatus.InfoError if it receives
		// a dictionary missing the file name
		assertEquals(FormStatus.InfoError,
				taggedOutputWriterAction.execute(testDictionary));
		// Add the file name and do the real execution, which should return
		// FormStatus.Processed and will write a file that can be checked
		testDictionary.put("iceTaggedOutputFileName", "taggedTestFile.txt");
		assertEquals(FormStatus.Processed,
				taggedOutputWriterAction.execute(testDictionary));

		// Check the file
		File testFile = new File("taggedTestFile.txt");
		assertTrue(testFile.exists());
		assertTrue(testFile.canRead());

		// Load the file and check it
		Properties testFileProperties = new Properties();
		try {
			testFileProperties.load(new FileInputStream(testFile));
		} catch (FileNotFoundException e) {
			// Fail if it catches an exception
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			// Fail if it catches an exception
			e.printStackTrace();
			fail();
		}
		System.out.println(testFileProperties);
		assertTrue(testFileProperties.containsKey("Kirk"));
		assertEquals(testFileProperties.get("Kirk"), testDictionary.get("Kirk"));
		assertTrue(testFileProperties.containsKey("Spock"));
		assertEquals(testFileProperties.get("Spock"),
				testDictionary.get("Spock"));
		assertTrue(testFileProperties.containsKey("Bones"));
		assertEquals(testFileProperties.get("Bones"),
				testDictionary.get("Bones"));
		assertTrue(testFileProperties.containsKey("Scotty"));
		assertEquals(testFileProperties.get("Scotty"),
				testDictionary.get("Scotty"));

		// Get rid of the file if everything worked
		if (testFile.exists()) {
			testFile.delete();
		}

		return;

		// end-user-code
	}
}