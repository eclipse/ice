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
package org.eclipse.ice.kdd.test;

import java.util.ArrayList;

import org.eclipse.ice.kdd.KDDAnalysisTool;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleDataProvider;
import org.junit.Before;
import org.junit.Test;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import static org.junit.Assert.*;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The class tests the functionality of the KDDAnalysisTool, particularly that
 * its name and version were set correctly, and that it can create non-null
 * KDDAnalysisDocuments.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Alex McCaskey
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class KDDAnalysisToolTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the KDDAnalysisTool to test.
	 * </p>
	 * 
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private KDDAnalysisTool kddAnalysisTool;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the name of the tool to test.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String toolName;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * checks the construction.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Before
	public void beforeClass() {
		// begin-user-code
		// Set the name and create the Tool
		toolName = "KDDTool";
		kddAnalysisTool = new KDDAnalysisTool(toolName, "1.0");
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Test that the name was set correctly
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkName() {
		// begin-user-code
		// Make sure the name was set correctly
		assertEquals(toolName, kddAnalysisTool.getName());
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * checks if the Tool is ready or not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkReady() {
		// begin-user-code
		// Make sure that this Tool is ready
		assertTrue(kddAnalysisTool.isReady());
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * checks the version of the tool.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkVersion() {
		// begin-user-code
		// Make sure the version is correct
		assertEquals("1.0", kddAnalysisTool.getVersion());
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * checks if an IAnalysisDocument type is created or not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCreateDocument() {
		// begin-user-code

		// Create some fake data
		IDataProvider fakeData = new SimpleDataProvider();

		// Make sure we can create a document, don't care that its fake data
		assertNotNull(kddAnalysisTool.createDocument(fakeData));

		// end-user-code
	}

}