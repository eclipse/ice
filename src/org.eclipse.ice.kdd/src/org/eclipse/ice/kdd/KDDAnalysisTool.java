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
package org.eclipse.ice.kdd;

import org.eclipse.ice.analysistool.IAnalysisTool;
import static org.eclipse.ice.kdd.KDDAnalysisDocument.*;
import org.eclipse.ice.analysistool.IAnalysisDocument;

import java.net.URI;

import org.eclipse.ice.kdd.kddstrategy.KDDStrategyFactory;

import org.eclipse.ice.analysistool.IDataProvider;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The KDDAnalysisTool is a realization of the ICE IAnalysisTool interface, and
 * is responsible for creating a KDDAnalysisDocument that performs and displays
 * various types of data clustering and anomaly detection for nuclear reactors.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Alex McCaskey
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class KDDAnalysisTool implements IAnalysisTool {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of this AnomalyDetectionTool.
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
	 * A boolean flag indicating whether or not this tool is ready for use in
	 * ICE
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Boolean ready;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The version number of this AnomalyDetectionTool.
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
	 * Reference to the OSGi created strategy factory to be used in the
	 * KDDAnalysisDocument.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private KDDStrategyFactory strategyFactory;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            The name to be used for this KDDAnalysisTool
	 *            </p>
	 * @param version
	 *            <p>
	 *            The version of this KDDAnalysisTool
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDAnalysisTool(String name, String version) {
		// begin-user-code
		this.name = name;
		this.version = version;
		ready = true;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The nullary constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDAnalysisTool() {
		// begin-user-code
		name = "Knowledge Discovery and Data Mining ICE Analysis Tool";
		version = "1.0";
		ready = true;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param factory
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerStrategyFactory(KDDStrategyFactory factory) {
		// begin-user-code
		if (factory != null) {
			System.out
					.println("KDDAnalysisTool Message: Registering new KDDStrategyFactory.");
			strategyFactory = factory;
		} else {
			System.out
					.println("KDDAnalysisTool Message: Invalid KDDStrategyFactory instance.");
			strategyFactory = null;
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
		return false;
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
		// TODO Auto-generated method stub
		return null;
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
	 * @see IAnalysisTool#createDocument(IDataProvider data)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IAnalysisDocument createDocument(IDataProvider data) {
		// begin-user-code

		// Create an IAnalysisDocument reference
		IAnalysisDocument document;

		// If the strategy factory is not null, then use
		// it, since it is from the OSGi framework
		// and has been populated with
		// the available strategies
		if (strategyFactory != null) {
			document = new KDDAnalysisDocument(strategyFactory);
		} else {
			document = new KDDAnalysisDocument();
		}

		// Load the given data, return null
		// if the load didn't work.
		if (document.loadData(data)) {
			return document;
		} else {
			return null;
		}

		// end-user-code
	}
}