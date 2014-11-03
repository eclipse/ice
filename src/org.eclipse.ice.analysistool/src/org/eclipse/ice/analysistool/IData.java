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
package org.eclipse.ice.analysistool;

import java.util.ArrayList;

/** 
 * <!-- begin-UML-doc -->
 * <p>An interface that provides the position and value of a data entry as well as a descriptive tag about the featureof the entry that the data represents.</p>
 * <!-- end-UML-doc -->
 * @author els
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IData {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The position of the data relative to the position of the containing object.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The position.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Double> getPosition();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The value of the particular feature (pin-power, temperature, etc).</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The value of the IData object.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getValue();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The amount of uncertainty in the value.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The value of uncertainty.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getUncertainty();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>A string describing the units of the value and its uncertainty.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The units.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getUnits();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The name of the feature that this data represents (pin-power, temperature, etc).</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The name of the feature.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getFeature();
}