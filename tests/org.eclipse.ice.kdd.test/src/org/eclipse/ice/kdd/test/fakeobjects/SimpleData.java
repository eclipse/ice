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

import org.eclipse.ice.analysistool.IData;
import java.util.Set;
import java.util.ArrayList;

/**
 * <p>
 * SimpleData is a realization of the IData interface that represents an element
 * in a matrix of data.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class SimpleData implements IData {
	/**
	 * <p>
	 * The position of this SimpleData point.
	 * </p>
	 * 
	 */
	private ArrayList<Double> position;
	/**
	 * <p>
	 * The value of this SimpleData.
	 * </p>
	 * 
	 */
	private Double value;
	/**
	 * <p>
	 * The uncertainty in this SimpleData.
	 * </p>
	 * 
	 */
	private Double uncertainty;
	/**
	 * <p>
	 * The feature name that this SimpleData corresponds to.
	 * </p>
	 * 
	 */
	private String feature;
	/**
	 * <p>
	 * The units for this data.
	 * </p>
	 * 
	 */
	private String units;

	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 * @param newFeature
	 * @param newValue
	 */
	public SimpleData(String newFeature, Double newValue) {
		feature = newFeature;
		value = newValue;
		position = new ArrayList<Double>();
	}

	/**
	 * <p>
	 * Set the units for this data.
	 * </p>
	 * 
	 * @param newUnits
	 */
	public void setUnits(String newUnits) {
		units = newUnits;
	}

	/**
	 * <p>
	 * Set the uncertainty for this SimpleData
	 * </p>
	 * 
	 * @param value
	 */
	public void setUncertainty(Double value) {
		uncertainty = value;
	}

	/**
	 * <p>
	 * Set the position of this SimpleData.
	 * </p>
	 * 
	 * @param pos
	 */
	public void setPosition(ArrayList<Double> pos) {
		if (pos != null && !pos.isEmpty() && pos.size() == 3) {
			position.clear();
			position.add(pos.get(0));
			position.add(pos.get(1));
			position.add(pos.get(2));
		}

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getPosition()
	 */
	public ArrayList<Double> getPosition() {
		return position;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getValue()
	 */
	public double getValue() {
		return value;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getUncertainty()
	 */
	public double getUncertainty() {
		return uncertainty;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getUnits()
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getFeature()
	 */
	public String getFeature() {
		return feature;
	}
}