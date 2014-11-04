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
package org.eclipse.ice.reactorAnalyzer.test;

import org.eclipse.ice.analysistool.IAnalysisTool;
import org.eclipse.ice.analysistool.IAnalysisDocument;
import org.eclipse.ice.analysistool.IDataProvider;

import java.net.URI;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A fake implementation of IAnalysisTool that does not require any external
 * capabilities to run. It is used to test the ReactorAnalyzerBuilder and the
 * ReactorAnalyzer in a lightweight fashion.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeAnalysisTool implements IAnalysisTool {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the document was created, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean documentCreated;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A reference to the FakeAnalysisDocument that is fed to clients by this
	 * FakeAnalysisTool.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private FakeAnalysisDocument analysisDoc;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation resets the FakeAnalysisTool to its initial state.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void reset() {
		// begin-user-code

		// Perform a factory reset. ;)
		analysisDoc = null;
		documentCreated = false;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the IAnalysisDocument was retrieved, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean documentRetrieved() {
		// begin-user-code
		return documentCreated;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the FakeAnalysisDocument that was delivered by the
	 * FakeAnalysisTool.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The FakeAnalysisDocument used to fake out the ReactorAnalyzer.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FakeAnalysisDocument getDocument() {
		// begin-user-code

		return analysisDoc;
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
		return true;
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
		return "Fake Analysis Tool";
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
		return "1.0.0";
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

		// Create the document if necessary
		if (analysisDoc == null) {
			analysisDoc = new FakeAnalysisDocument();
		}

		return analysisDoc;
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
		return true;
		// end-user-code
	}

	@Override
	public IAnalysisDocument createDocument(IDataProvider data) {
		// TODO Auto-generated method stub
		return null;
	}
}