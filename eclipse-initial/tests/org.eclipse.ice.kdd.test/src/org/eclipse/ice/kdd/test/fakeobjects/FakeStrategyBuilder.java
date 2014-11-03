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

import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.kdd.kddstrategy.IStrategyBuilder;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;

import org.eclipse.ice.analysistool.IDataProvider;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeStrategyBuilder implements IStrategyBuilder {
	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#build(ArrayList<IDataProvider> data)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDStrategy build(ArrayList<IDataProvider> data) {
		// begin-user-code
		return new FakeStrategy();
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#getStrategyName()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getStrategyName() {
		// begin-user-code
		return "Fake Strategy";
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#isAvailable(ArrayList<IDataProvider> dataToCheck)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isAvailable(ArrayList<IDataProvider> dataToCheck) {
		// begin-user-code
		return true;
		// end-user-code
	}

	@Override
	public ArrayList<Entry> getStrategyPropertiesAsEntries() {
		return null;
	}
}