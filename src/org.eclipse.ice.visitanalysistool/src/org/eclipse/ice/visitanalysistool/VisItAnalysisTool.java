/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.visitanalysistool;

import org.eclipse.ice.analysistool.IAnalysisTool;
import org.eclipse.ice.analysistool.IDataProvider;
import llnl.visit.ViewerProxy;
import org.eclipse.ice.analysistool.IAnalysisDocument;
import java.net.URI;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class implements the IAnalysisTool interface for use with VisIt.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author els
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VisItAnalysisTool implements IAnalysisTool {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of the tool, aka "VisIt".
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String name;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A boolean flag indicating whether the VisItAnalysisTool is ready to use.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean ready;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An instance of the ViewerProxy provided by the VisIt Java API.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ViewerProxy viewerProxy;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The version number of the VisIt executable used by this
	 * VisItAnalysisTool.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String version;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The nullary Constructor. If this constructor is used, then the System
	 * properties visit.binpath and visit.port must be set.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public VisItAnalysisTool() {
		// begin-user-code

		// Call the other constructor with argument values from System
		// Properties
		this(null, -1);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param visitBinPath
	 *            <p>
	 *            The absolute path to the bin directory containing the VisIt
	 *            executable.
	 *            </p>
	 * @param visitPort
	 *            <p>
	 *            The port number on which to launch the VisIt executable.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public VisItAnalysisTool(String visitBinPath, int visitPort) {
		// begin-user-code

		// Assign a name to this tool
		name = "VisIt";

		// Assign a version to this tool. We will need to determine how to do
		// this dynamically later
		version = "2.5.2";

		// Create an instance of ViewerProxy
		viewerProxy = new ViewerProxy();

		// Add command line arguments to pass to VisIt
		viewerProxy.AddArgument("-nowin");
		viewerProxy.AddArgument("-nosplash");
		viewerProxy.AddArgument("-noconfig");

		// If the binPath is null
		if (visitBinPath == null) {

			// Check for a visit.binpath System property
			String property = System.getProperty("visit.binpath");

			if (property != null) {
				// Replace @user.home variable if needed
				if (property.contains("@user.home")) {
					property = property.replace("@user.home",
							System.getProperty("user.home"));
				}
				visitBinPath = property;
			}

		}

		// If the port number is equal to -1
		if (visitPort == -1) {

			// Check for a visit.port system property
			String property = System.getProperty("visit.port");

			if (property != null) {
				visitPort = Integer.valueOf(property);
			}

		}

		// Print VisIt connection information
		System.out.println("VisItAnalysisTool Message: Connection parameters:");
		System.out.println("VisItAnalysisTool Message:\tBin Path = "
				+ visitBinPath);
		System.out.println("VisItAnalysisTool Message:\tPort = " + visitPort);

		// Check the values of visitPort and visitBinPath
		if (visitPort > 0 && visitBinPath != null && !visitBinPath.isEmpty()) {

			// Set the binPath
			viewerProxy.SetBinPath(visitBinPath);

			// Try creating the viewerProxy on the provided port
			if (viewerProxy.Create(visitPort)) {

				// We do wish to synchronize
				viewerProxy.SetSynchronous(true);

				// Set ready equal to true
				ready = true;

			}

		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#close()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Boolean close() {
		// begin-user-code
		// TODO Auto-generated method stub
		return viewerProxy.Close();
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#getName()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getName() {
		// begin-user-code
		return name;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#getVersion()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getVersion() {
		// begin-user-code
		return version;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#createDocument(URI data)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IAnalysisDocument createDocument(URI data) {
		// begin-user-code

		// Create a new VisItAnalysisDocument object
		VisItAnalysisDocument visItAnalysisDocument = new VisItAnalysisDocument(
				viewerProxy);

		// Call loadData on visItAnalysisDocument
		visItAnalysisDocument.loadData(data);

		return visItAnalysisDocument;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#isReady()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isReady() {
		// begin-user-code
		return ready;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#createDocument()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IAnalysisDocument createDocument(IDataProvider data) {
		// begin-user-code
		return null;
		// end-user-code
	}
}