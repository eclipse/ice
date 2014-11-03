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
package org.eclipse.ice.kdd.kddstrategy.kmeansclustering;

import static org.eclipse.ice.kdd.kddstrategy.kmeansclustering.RawKMeansStrategy.*;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.analysistool.AnalysisAssetType;

import java.util.Properties;
import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;

import java.net.URI;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The ComparativeKMeansStrategy performs a basic KMeans clustering algorithm on
 * a matrix that is the difference between a loaded set of analysis data and a
 * given reference set of data. It instantiates a RawKMeansStrategy with that
 * difference matrix.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ComparativeKMeansStrategy extends KDDStrategy {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param data
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ComparativeKMeansStrategy(IDataProvider data) {
		// begin-user-code
		super("Comparative KMeans", null);
		// TODO Auto-generated constructor stub
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor with direct matrix to cluster injection.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param matrix
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ComparativeKMeansStrategy(ClusterKDDMatrix matrix) {
		// begin-user-code
		super(null, null);
		// TODO Auto-generated constructor stub
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumberOfClusters() {
		// begin-user-code
		// TODO Auto-generated method stub
		return 0;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getName()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getName() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getType()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public AnalysisAssetType getType() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getProperty(String key)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getProperty(String key) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#setProperty(String key, String value)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setProperty(String key, String value) {
		// begin-user-code
		// TODO Auto-generated method stub
		return false;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#resetProperties()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void resetProperties() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getProperties()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Properties getProperties() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getPropertiesAsEntryList()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Entry> getPropertiesAsEntryList() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getURI()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public URI getURI() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see KDDStrategy#executeStrategy()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean executeStrategy() {
		// begin-user-code
		// TODO Auto-generated method stub
		return false;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see KDDStrategy#createAsset()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected boolean createAsset() {
		// begin-user-code
		// TODO Auto-generated method stub
		return false;
		// end-user-code
	}
}