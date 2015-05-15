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
 * <p>
 * The CompositeStrategy is a realization of the Composite design pattern that
 * encapsulates a tree structure of available KDDStrategies to execute. Its
 * executeStrategy method should primarily loop over its collection of
 * KDDStrategies and execute each.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class CompositeStrategy extends KDDStrategy {
	/**
	 * <p>
	 * The list of KDDStrategies that this KDDStrategy contains.
	 * </p>
	 * 
	 * 
	 */
	protected ArrayList<KDDStrategy> strategies;

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 * @param data
	 * @throws IllegalArgumentException
	 */
	public CompositeStrategy(ArrayList<IDataProvider> data) {
		super("", data);
		strategies = new ArrayList<KDDStrategy>();
	}

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 * @param data
	 * @throws IllegalArgumentException
	 */
	public CompositeStrategy() {
		super();
		strategies = new ArrayList<KDDStrategy>();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see KDDStrategy#executeStrategy()
	 */
	public boolean executeStrategy() {
		for (KDDStrategy strategy : strategies) {
			if (!strategy.executeStrategy()) {
				return false;
			}
		}
		return true;
	}

}