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
package org.eclipse.ice.kdd.kddstrategy.compositestrategy;

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.analysistool.AnalysisAssetType;

import java.util.Properties;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;

import java.net.URI;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The CompositeStrategy is a realization of the Composite design pattern that
 * encapsulates a tree structure of available KDDStrategies to execute. Its
 * executeStrategy method should primarily loop over its collection of
 * KDDStrategies and execute each.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Alex McCaskey
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class CompositeStrategy extends KDDStrategy {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of KDDStrategies that this KDDStrategy contains.
	 * </p>
	 * 
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ArrayList<KDDStrategy> strategies;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param data
	 * @throws IllegalArgumentException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public CompositeStrategy(ArrayList<IDataProvider> data) {
		// begin-user-code
		super("", data);
		strategies = new ArrayList<KDDStrategy>();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param data
	 * @throws IllegalArgumentException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public CompositeStrategy() {
		// begin-user-code
		super();
		strategies = new ArrayList<KDDStrategy>();
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
		for (KDDStrategy strategy : strategies) {
			if (!strategy.executeStrategy()) {
				return false;
			}
		}
		return true;
		// end-user-code
	}

}