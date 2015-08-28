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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.kdd.kddstrategy.IStrategyBuilder;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategyFactory;
import org.eclipse.ice.kdd.test.fakeobjects.FakeStrategyBuilder;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleDataProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * This class tests that the KDDStrategyFactory complies with the OSGi
 * declarative services framework.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class KDDStrategyFactoryTester {
	/**
	 * 
	 */
	private KDDStrategyFactory factory;

	/**
	 * 
	 */
	@Before
	public void beforeClass() {
		factory = new KDDStrategyFactory();
	}

	/**
	 * 
	 */
	@Test
	public void checkGetAvailableStrategies() {
		ArrayList<IDataProvider> data = new ArrayList<IDataProvider>();
		data.add(new SimpleDataProvider());

		IStrategyBuilder builder = new FakeStrategyBuilder();

		assertNull(factory.getAvailableStrategies(data));

		factory.registerStrategy(builder);

		assertNotNull(factory.getAvailableStrategies(data));
		assertEquals(1, factory.getAvailableStrategies(data).size());

	}

	/**
	 * 
	 */
	@Test
	public void checkCreateStrategy() {
		ArrayList<IDataProvider> data = new ArrayList<IDataProvider>();
		data.add(new SimpleDataProvider());
		factory.registerStrategy(new FakeStrategyBuilder());

		KDDStrategy strategy = factory.createStrategy("Fake Strategy", data);

		assertNotNull(strategy);
		assertTrue(strategy.executeStrategy());
	}
}