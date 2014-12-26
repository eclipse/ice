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
package org.eclipse.ice.kdd.test.fakeobjects;

import org.eclipse.ice.analysistool.IDataProvider;
import static org.eclipse.ice.kdd.test.fakeobjects.SimpleData.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.ice.analysistool.IData;

import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * SimpleDataProvider is a realization of the IDataProvider that encapsulates a
 * 1D array of IData elements.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Alex McCaskey
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class SimpleDataProvider implements IDataProvider {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to this SimpleDataProvider's map of data, indexed by feature
	 * name.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private HashMap<String, ArrayList<IData>> dataSet;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SimpleDataProvider() {
		// begin-user-code
		dataSet = new HashMap<String, ArrayList<IData>>();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method adds a new IData to this SimpleDataProvider.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param data
	 * @param featureName
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addData(ArrayList<IData> data, String featureName) {
		// begin-user-code
		dataSet.put(featureName, data);
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getFeatureList()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getFeatureList() {
		// begin-user-code
		return new ArrayList<String>(dataSet.keySet());
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getNumberOfTimeSteps()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumberOfTimeSteps() {
		// begin-user-code
		// TODO Auto-generated method stub
		return 0;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#setTime(double step)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTime(double step) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getDataAtCurrentTime(String feature)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IData> getDataAtCurrentTime(String feature) {
		// begin-user-code
		if (!dataSet.get(feature).isEmpty()) {
			return dataSet.get(feature);
		} else {
			return null;
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getSourceInfo()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getSourceInfo() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getFeaturesAtCurrentTime()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getFeaturesAtCurrentTime() {
		// begin-user-code
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getTimes()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Double> getTimes() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getTimeStep(double time)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getTimeStep(double time) {
		// begin-user-code
		// TODO Auto-generated method stub
		return 0;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getTimeUnits()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getTimeUnits() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}
}