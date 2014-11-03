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
 * <p>An interface for determining what features are available  for a particular object(pin-power, temperature, etc).  IDataProviders are anything that have information to share regardless of their positions in any particular hierarchy.</p>
 * <!-- end-UML-doc -->
 * @author els
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IDataProvider {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the list of features available across all time steps (pin-power, temperature, etc).</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The list of features.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getFeatureList();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the total number of time steps.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The number of time steps.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumberOfTimeSteps();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation sets the current time step for which data should be retrieved.  It is 0-indexed such that time step 0 is the initial state and time step 1 is the state after the first time step.  This operation should be called to set the current time step before data is retrieved from the provider.  The provider will always default to the initial state.  </p>
	 * <!-- end-UML-doc -->
	 * @param step <p>The time step to set.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTime(double step);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns all of the data (as IData[*]) related to a particular feature for this provider at a specific time step.</p><p>This operation will return null if no data is available and such a situation will most likely signify an error.</p>
	 * <!-- end-UML-doc -->
	 * @param feature <p>The feature for the IData.</p>
	 * @return <p>The returned IData.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IData> getDataAtCurrentTime(String feature);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation is a description of the source of information for this provider and its data.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The source information.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getSourceInfo();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Returns the list of features at the current time.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The returned list of features at the current time step.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getFeaturesAtCurrentTime();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Returns all the times in ascending order.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>An arraylist of times in order from least to greatest.  This operation does not allow the user to change the order of this list.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Double> getTimes();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Returns the integer time based upon the time step.  Returns -1 if the time does not exist.</p>
	 * <!-- end-UML-doc -->
	 * @param time
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getTimeStep(double time);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Returns the time units.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The time unit.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getTimeUnits();
}