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
package org.eclipse.ice.kdd.kddstrategy;

import java.util.ArrayList;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.datastructures.form.Entry;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The KDDStrategyBuilder is an interface for the creation of available
 * KDDStrategies. KDDStrategy implementations must provide a realization of this
 * interface in order to make themselves available to the KDDAnalysisTool.
 * Realizations must provide an isDataValid implementation to tell the
 * KDDAnalysisDocument if it can be used.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IStrategyBuilder {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method should return a new instance of the KDDStrategy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param data
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDStrategy build(ArrayList<IDataProvider> data);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the name of this KDDStrategy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getStrategyName();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method should take the input IDataProvider list and perform custom
	 * checks on the providers to indicate whether or not the given data can be
	 * used by the corresponding KDDStrategy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param dataToCheck
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isAvailable(ArrayList<IDataProvider> dataToCheck);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return this IStrategyBuilder's KDDStrategy configurable properties as a
	 * list of ICE DataStructures Entries.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Entry> getStrategyPropertiesAsEntries();
}