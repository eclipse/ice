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
package org.eclipse.ice.kdd.kddstrategy.kmeansclustering;

import java.net.URI;
import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.ice.analysistool.AnalysisAssetType;
import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;

/**
 * <p>
 * The ComparativeKMeansStrategy performs a basic KMeans clustering algorithm on
 * a matrix that is the difference between a loaded set of analysis data and a
 * given reference set of data. It instantiates a RawKMeansStrategy with that
 * difference matrix.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class ComparativeKMeansStrategy extends KDDStrategy {
	/**
	 * <p>
	 * The Constructor
	 * </p>
	 * 
	 * @param data
	 */
	public ComparativeKMeansStrategy(IDataProvider data) {
		super("Comparative KMeans", null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <p>
	 * The Constructor with direct matrix to cluster injection.
	 * </p>
	 * 
	 * @param matrix
	 */
	public ComparativeKMeansStrategy(ClusterKDDMatrix matrix) {
		super(null, null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @return
	 */
	public int getNumberOfClusters() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getName()
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getType()
	 */
	public AnalysisAssetType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getProperty(String key)
	 */
	public String getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#setProperty(String key, String value)
	 */
	public boolean setProperty(String key, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#resetProperties()
	 */
	public void resetProperties() {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getProperties()
	 */
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getPropertiesAsEntryList()
	 */
	public ArrayList<Entry> getPropertiesAsEntryList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getURI()
	 */
	public URI getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see KDDStrategy#executeStrategy()
	 */
	public boolean executeStrategy() {
		// TODO Auto-generated method stub
		return false;
	}

}