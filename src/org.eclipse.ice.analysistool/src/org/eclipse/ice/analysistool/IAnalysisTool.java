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
package org.eclipse.ice.analysistool;

import java.net.URI;

/** 
 * <!-- begin-UML-doc -->
 * <p>IAnalysisTools are tools that can be used to analyze data. There is one instance of each IAnalysisTool in ICE and they are responsible for creating IAnalysisDocument for their respective tool. Each IAnalysisTool should have a name and a version number that are unique so that they can be distinguished from each other.</p>
 * <!-- end-UML-doc -->
 * @author aqw
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IAnalysisTool {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation allows subclasses to perform shutdown operations when they are required by a back-end analysis service (such as VisIt) to close the running processes. </p>
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Boolean close();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the name of the analysis tool.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The name of the analysis tool.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getName();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the version number of the analysis tool.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The version number, as a string, for this analysis tool.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getVersion();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation create a new IAnalysisDocument for the data at the given URI.</p>
	 * <!-- end-UML-doc -->
	 * @param data <p>A URI to a folder or file that contains data which should be analyzed in the IAnalysisDocument.</p>
	 * @return <p>A new analysis document for the data provided by this analysis tool. This IAnalysisDocument is made to work with this analysis tool only.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IAnalysisDocument createDocument(URI data);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation notifies a client if the IAnalysisTool is ready to be used.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>True if the tool is ready to be used, false otherwise.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isReady();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation creates a new IAnalysisDocument for the data within the given IDataProvider realization.</p>
	 * <!-- end-UML-doc -->
	 * @param data <p>A concrete realization of IDataProvider that contains data which should be analyzed in the IAnalysisDocument.</p>
	 * @return <p>A new analysis document for the data provided by this analysis tool. This IAnalysisDocument is made to work with this analysis tool only.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IAnalysisDocument createDocument(IDataProvider data);
}