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

import org.eclipse.ice.analysistool.IData;
import java.util.Set;
import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * SimpleData is a realization of the IData interface that represents an element
 * in a matrix of data.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class SimpleData implements IData {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The position of this SimpleData point.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<Double> position;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The value of this SimpleData.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Double value;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The uncertainty in this SimpleData.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Double uncertainty;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The feature name that this SimpleData corresponds to.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String feature;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The units for this data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String units;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param newFeature
	 * @param newValue
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SimpleData(String newFeature, Double newValue) {
		// begin-user-code
		feature = newFeature;
		value = newValue;
		position = new ArrayList<Double>();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Set the units for this data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param newUnits
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setUnits(String newUnits) {
		// begin-user-code
		units = newUnits;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Set the uncertainty for this SimpleData
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param value
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setUncertainty(Double value) {
		// begin-user-code
		uncertainty = value;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Set the position of this SimpleData.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param pos
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPosition(ArrayList<Double> pos) {
		// begin-user-code
		if (pos != null && !pos.isEmpty() && pos.size() == 3) {
			position.clear();
			position.add(pos.get(0));
			position.add(pos.get(1));
			position.add(pos.get(2));
		}

		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getPosition()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Double> getPosition() {
		// begin-user-code
		return position;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getValue()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getValue() {
		// begin-user-code
		return value;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getUncertainty()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getUncertainty() {
		// begin-user-code
		return uncertainty;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getUnits()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getUnits() {
		// begin-user-code
		return units;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getFeature()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getFeature() {
		// begin-user-code
		return feature;
		// end-user-code
	}
}