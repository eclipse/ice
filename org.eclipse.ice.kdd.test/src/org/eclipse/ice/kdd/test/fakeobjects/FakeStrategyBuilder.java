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

import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.kdd.kddstrategy.IStrategyBuilder;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;

/**
 * 
 * @author Alex McCaskey
 */
public class FakeStrategyBuilder implements IStrategyBuilder {
	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#build(ArrayList<IDataProvider> data)
	 */
	@Override
	public KDDStrategy build(ArrayList<IDataProvider> data) {
		return new FakeStrategy();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#getStrategyName()
	 */
	@Override
	public String getStrategyName() {
		return "Fake Strategy";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#isAvailable(ArrayList<IDataProvider> dataToCheck)
	 */
	@Override
	public boolean isAvailable(ArrayList<IDataProvider> dataToCheck) {
		return true;
	}

	@Override
	public ArrayList<Entry> getStrategyPropertiesAsEntries() {
		return null;
	}
}