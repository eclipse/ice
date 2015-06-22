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
package org.eclipse.ice.kdd.kddstrategy.godfreystrategy;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ice.kdd.kddmath.IDataMatrix;
import org.eclipse.ice.kdd.kddmath.KDDMatrix;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;

/**
 * 
 * @author Alex McCaskey
 */
public class GodfreySubStrategy extends KDDStrategy {
	/**
	 * 
	 */
	protected boolean executed;

	/**
	 * <p>
	 * Reference to pin power difference between the data and the reference
	 * data.
	 * </p>
	 * 
	 */
	protected HashMap<Integer, ArrayList<IDataMatrix>> loadedPinPowers;

	/**
	 * <p>
	 * Reference to the rank-4 weight tensor.
	 * </p>
	 * 
	 */
	protected HashMap<Integer, ArrayList<KDDMatrix>> weights;

	/**
	 * 
	 */
	protected HashMap<Integer, ArrayList<IDataMatrix>> refPinPowers;

	/**
	 * 
	 * @return
	 */
	public boolean hasExecuted() {
		return executed;
	}

	/**
	 * 
	 * @param pinPowers
	 * @param refPinPowers
	 * @param weights
	 */
	public GodfreySubStrategy(
			HashMap<Integer, ArrayList<IDataMatrix>> pinPowers,
			HashMap<Integer, ArrayList<IDataMatrix>> refPinPowers,
			HashMap<Integer, ArrayList<KDDMatrix>> weights) {
		super();
		loadedPinPowers = pinPowers;
		this.refPinPowers = refPinPowers;
		this.weights = weights;
		this.executed = false;
	}

	/**
	 * <p>
	 * This method allows subclasses to implement a unique clustering or anomaly
	 * detection algorithm and produce a KDDAnalysisAsset for clients to display
	 * and manipulate.
	 * </p>
	 * 
	 * @return
	 */
	@Override
	public boolean executeStrategy() {
		return false;
	}

	/**
	 * 
	 */
	public void resetExecuted() {
		executed = false;
	}

}