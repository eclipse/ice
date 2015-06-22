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
import java.util.HashMap;

import org.eclipse.ice.kdd.kddmath.IDataMatrix;
import org.eclipse.ice.kdd.kddmath.KDDMatrix;
import org.eclipse.ice.kdd.kddstrategy.godfreystrategy.GodfreySubStrategy;
import org.eclipse.ice.kdd.kddstrategy.godfreystrategy.SubStrategyFactory;

/**
 * 
 * @author Alex McCaskey
 */
public class FakeSubStrategyFactory extends SubStrategyFactory {
	/**
	 * <p>
	 * This method returns the requested GodfreySubStrategy by string name.
	 * </p>
	 * 
	 * @param name
	 * @param pinPowers
	 * @param refPowers
	 * @param weights
	 * @param props
	 * @return
	 */
	@Override
	public GodfreySubStrategy createSubStrategy(String name,
			HashMap<Integer, ArrayList<IDataMatrix>> pinPowers,
			HashMap<Integer, ArrayList<IDataMatrix>> refPowers,
			HashMap<Integer, ArrayList<KDDMatrix>> weights,
			HashMap<String, String> props) {
		return new FakeSubStrategy(pinPowers, refPowers, weights, props);
	}
}