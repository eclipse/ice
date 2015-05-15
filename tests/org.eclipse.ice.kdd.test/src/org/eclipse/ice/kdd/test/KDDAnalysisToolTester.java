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
 * <p>
 * The class tests the functionality of the KDDAnalysisTool, particularly that
 * its name and version were set correctly, and that it can create non-null
 * KDDAnalysisDocuments.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class KDDAnalysisToolTester {
	/**
	 * <p>
	 * Reference to the KDDAnalysisTool to test.
	 * </p>
	 * 
	 * 
	 */
	private KDDAnalysisTool kddAnalysisTool;
	/**
	 * <p>
	 * Reference to the name of the tool to test.
	 * </p>
	 * 
	 */
	private String toolName;

	/**
	 * <p>
	 * checks the construction.
	 * </p>
	 * 
	 */
	@Before
	public void beforeClass() {
		// Set the name and create the Tool
		toolName = "KDDTool";
		kddAnalysisTool = new KDDAnalysisTool(toolName, "1.0");
	}

	/**
	 * <p>
	 * Test that the name was set correctly
	 * </p>
	 * 
	 */
	@Test
	public void checkName() {
		// Make sure the name was set correctly
		assertEquals(toolName, kddAnalysisTool.getName());
	}

	/**
	 * <p>
	 * checks if the Tool is ready or not.
	 * </p>
	 * 
	 */
	@Test
	public void checkReady() {
		// Make sure that this Tool is ready
		assertTrue(kddAnalysisTool.isReady());
	}

	/**
	 * <p>
	 * checks the version of the tool.
	 * </p>
	 * 
	 */
	@Test
	public void checkVersion() {
		// Make sure the version is correct
		assertEquals("1.0", kddAnalysisTool.getVersion());
	}

	/**
	 * <p>
	 * checks if an IAnalysisDocument type is created or not.
	 * </p>
	 * 
	 */
	@Test
	public void checkCreateDocument() {

		// Create some fake data
		IDataProvider fakeData = new SimpleDataProvider();

		// Make sure we can create a document, don't care that its fake data
		assertNotNull(kddAnalysisTool.createDocument(fakeData));

	}

}