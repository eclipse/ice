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

import org.eclipse.ice.analysistool.IDataProvider;
import static org.eclipse.ice.kdd.test.fakeobjects.SimpleData.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.ice.analysistool.IData;

import java.util.ArrayList;

/**
 * <p>
 * SimpleDataProvider is a realization of the IDataProvider that encapsulates a
 * 1D array of IData elements.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class SimpleDataProvider implements IDataProvider {
	/**
	 * <p>
	 * Reference to this SimpleDataProvider's map of data, indexed by feature
	 * name.
	 * </p>
	 * 
	 */
	private HashMap<String, ArrayList<IData>> dataSet;

	/**
	 * <p>
	 * The Constructor
	 * </p>
	 * 
	 */
	public SimpleDataProvider() {
		dataSet = new HashMap<String, ArrayList<IData>>();
	}

	/**
	 * <p>
	 * This method adds a new IData to this SimpleDataProvider.
	 * </p>
	 * 
	 * @param data
	 * @param featureName
	 */
	public void addData(ArrayList<IData> data, String featureName) {
		dataSet.put(featureName, data);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getFeatureList()
	 */
	public ArrayList<String> getFeatureList() {
		return new ArrayList<String>(dataSet.keySet());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getNumberOfTimeSteps()
	 */
	public int getNumberOfTimeSteps() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#setTime(double step)
	 */
	public void setTime(double step) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getDataAtCurrentTime(String feature)
	 */
	public ArrayList<IData> getDataAtCurrentTime(String feature) {
		if (!dataSet.get(feature).isEmpty()) {
			return dataSet.get(feature);
		} else {
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getSourceInfo()
	 */
	public String getSourceInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getFeaturesAtCurrentTime()
	 */
	public ArrayList<String> getFeaturesAtCurrentTime() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getTimes()
	 */
	public ArrayList<Double> getTimes() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getTimeStep(double time)
	 */
	public int getTimeStep(double time) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getTimeUnits()
	 */
	public String getTimeUnits() {
		// TODO Auto-generated method stub
		return null;
	}
}