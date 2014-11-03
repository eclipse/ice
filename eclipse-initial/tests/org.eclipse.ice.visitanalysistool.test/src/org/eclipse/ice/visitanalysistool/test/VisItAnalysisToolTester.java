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
package org.eclipse.ice.visitanalysistool.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import org.eclipse.ice.visitanalysistool.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/** 
 * <!-- begin-UML-doc -->
 * <p>This class is responsible for testing the VisitAnalysisTool class.</p>
 * <!-- end-UML-doc -->
 * @author els
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VisItAnalysisToolTester {

	/**
	 * The visit port number to use
	 */
	private static final int visitPortNumber = 12000;
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private VisItAnalysisTool visItAnalysisTool;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Initializes System properties used by all test operations.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@BeforeClass
	public static void beforeClass(){
		
		//Create a bin path from the user's home directory.
		String binPath = System.getProperty("user.home")
							+ System.getProperty("file.separator")
							+ "visit"
							+ System.getProperty("file.separator")
							+ "bin";
		
		//Auto set bin for windows.
		if(System.getProperty("os.name").contains("Windows")) {
			binPath = System.getProperty("user.home")
			+ System.getProperty("file.separator")
			+ "visit";
		}
		
		//Assign System Property value for visit.binpath
		System.setProperty("visit.binpath", binPath);
		
		//Assign System Property value for visit.port
		System.setProperty("visit.port", String.valueOf(visitPortNumber));
	}
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation checks the getName, getVersion and isReady VisItAnalysisTool operations.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkAccessors() {
		// begin-user-code
		
		//Tell the thread to sleep in order to give time inbetween tests.
		try {
			Thread.sleep(100);
		} catch(Exception e) {
			
		}
		
		//Local declarations
		String name = "VisIt";
		String version = "2.5.2";

		//Create a new VisItAnalysisTool
		//visItAnalysisTool = new VisItAnalysisTool(binPath, port);
		visItAnalysisTool = new VisItAnalysisTool();
		
		//Run some tests
		assertEquals(visItAnalysisTool.getName(), name);
		assertEquals(visItAnalysisTool.getVersion(), version);
		assertTrue(visItAnalysisTool.isReady());

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation checks VisItAnalysisTool createDocument operation. This operation also checks that the correct URI was passed to the created document.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkDocumentCreation() {
		// begin-user-code
		
		// Create a URI from the test data file
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator + "data" + separator + "AMPData.silo");
		URI data = dataFile.toURI();
			
		//Run a test
		assertNotNull(data);

		//Create a new VisItAnalysisTool
		//visItAnalysisTool = new VisItAnalysisTool(binPath, port);
		visItAnalysisTool = new VisItAnalysisTool();

		//Create a VisItAnalysisDocument using the visItAnalysisTool
		VisItAnalysisDocument visItAnalysisDocument = (VisItAnalysisDocument) visItAnalysisTool.createDocument(data);

		//Run a test
		assertNotNull(visItAnalysisDocument);

		// end-user-code
	}
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Closes the VisitAnalysisTool instance on the selected port.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@After
	public void after() {
		// begin-user-code
		visItAnalysisTool.close();
		// end-user-code
	}
	
}