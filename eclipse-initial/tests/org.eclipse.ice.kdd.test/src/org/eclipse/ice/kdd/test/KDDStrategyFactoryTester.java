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
package org.eclipse.ice.kdd.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ice.kdd.kddstrategy.IStrategyBuilder;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategyFactory;
import org.eclipse.ice.kdd.test.fakeobjects.FakeStrategyBuilder;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleDataProvider;
import org.junit.Before;
import org.junit.Test;

import org.eclipse.ice.analysistool.IDataProvider;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests that the KDDStrategyFactory complies with the OSGi
 * declarative services framework.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class KDDStrategyFactoryTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private KDDStrategyFactory factory;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Before
	public void beforeClass() {
		// begin-user-code
		factory = new KDDStrategyFactory();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkGetAvailableStrategies() {
		// begin-user-code
		ArrayList<IDataProvider> data = new ArrayList<IDataProvider>();
		data.add(new SimpleDataProvider());

		IStrategyBuilder builder = new FakeStrategyBuilder();

		assertNull(factory.getAvailableStrategies(data));

		factory.registerStrategy(builder);

		assertNotNull(factory.getAvailableStrategies(data));
		assertEquals(1, factory.getAvailableStrategies(data).size());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCreateStrategy() {
		// begin-user-code
		ArrayList<IDataProvider> data = new ArrayList<IDataProvider>();
		data.add(new SimpleDataProvider());
		factory.registerStrategy(new FakeStrategyBuilder());

		KDDStrategy strategy = factory.createStrategy("Fake Strategy", data);

		assertNotNull(strategy);
		assertTrue(strategy.executeStrategy());
		// end-user-code
	}
}