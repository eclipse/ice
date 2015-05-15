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

import static org.eclipse.ice.kdd.kddstrategy.godfreystrategy.GodfreyStrategy.*;

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.kdd.kddstrategy.IStrategyBuilder;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;

/**
 * <p>
 * The GodfreyBuilder is a realization of the KDDStrategyBuilder and is used to
 * validate incoming data and return a new instance of the
 * GodfreyStrategy.<b></b>
 * </p>
 * 
 * @author Alex McCaskey
 */
public class GodfreyBuilder implements IStrategyBuilder {
	/**
	 * <p>
	 * Boolean indicating whether this strategy is available or not.
	 * </p>
	 * 
	 */
	private boolean isAvailable;
	/**
	 * <p>
	 * Reference to the number of rows in the pin powers matrix.
	 * </p>
	 * 
	 */
	private int nPinRows;
	/**
	 * <p>
	 * Reference to the number of columns in the pin powers matrix.
	 * </p>
	 * 
	 */
	private int nPinCols;
	/**
	 * <p>
	 * Reference to the number of Axial levels.
	 * </p>
	 * 
	 */
	private int nAxial;
	/**
	 * <p>
	 * Reference to the number of Assemblies.
	 * </p>
	 * 
	 */
	private int nAssemblies;

	/**
	 * <p>
	 * This method should return a new instance of the KDDStrategy.
	 * </p>
	 * 
	 * @param data
	 * @return
	 */
	public KDDStrategy build(ArrayList<IDataProvider> data) {

		// Create a reference to create and return
		GodfreyStrategy strategy = null;

		// Only construct if we've been given
		// valid data
		if (isAvailable) {
			strategy = new GodfreyStrategy(nPinRows, nPinCols, nAxial,
					nAssemblies, data.get(0).getDataAtCurrentTime("Data"), data
							.get(1).getDataAtCurrentTime("Data"));
		}

		// Now give it back...
		return strategy;
	}

	/**
	 * <p>
	 * Return the name of this KDDStrategy.
	 * </p>
	 * 
	 * @return
	 */
	public String getStrategyName() {
		return "Godfrey Fuel Pin Powers Strategy";
	}

	/**
	 * <p>
	 * This method should take the input IDataProvider list and perform custom
	 * checks on the providers to indicate whether or not the given data can be
	 * used by the corresponding KDDStrategy. This implementation should check
	 * that the correct features are available, then set the number of pin rows
	 * and columns, axial levels, and assemblies data for use in the build menu.
	 * If valid, it sets the isAvailable flag to true.
	 * </p>
	 * 
	 * @param dataToCheck
	 * @return
	 */
	public boolean isAvailable(ArrayList<IDataProvider> dataToCheck) {

		isAvailable = false;

		System.out.println("Data Size is " + dataToCheck.size());
		// Make sure we got 2 IDataProviders
		if (dataToCheck.isEmpty() || dataToCheck.size() == 1) {
			System.err
					.println("Invalid input data for GodfreyStrategy. Must be an ArrayList containing "
							+ "the data to be analyzed and the reference data to use for comparison.");
			return false;
		}

		// Separate the data
		IDataProvider loaded = dataToCheck.get(0);
		IDataProvider reference = dataToCheck.get(1);

		// Make sure we have the correct Features for the loaded Data
		if (!loaded.getFeatureList().contains("Data")
				|| !loaded.getFeatureList().contains("Number of Rows")
				|| !loaded.getFeatureList().contains("Number of Columns")
				|| !loaded.getFeatureList().contains("Number of Assemblies")) {
			System.err
					.println("Invalid Loaded Data. You must specify the number of pin rows and columns, "
							+ "as well as the number of assemblies.");
			return false;
		}

		// Make sure we have the correct Features for the reference Data
		if (!reference.getFeatureList().contains("Data")
				|| !reference.getFeatureList().contains("Number of Rows")
				|| !reference.getFeatureList().contains("Number of Columns")
				|| !reference.getFeatureList().contains("Number of Assemblies")) {
			System.err
					.println("Invalid Reference Data. You must specify the number of pin rows and columns, "
							+ "as well as the number of assemblies.");
			return false;
		}

		// Get t he loaded rows and columns
		ArrayList<IData> loadedRows = loaded
				.getDataAtCurrentTime("Number of Rows");
		ArrayList<IData> loadedCols = loaded
				.getDataAtCurrentTime("Number of Columns");

		// Get the reference rows and columns
		ArrayList<IData> refRows = loaded
				.getDataAtCurrentTime("Number of Rows");
		ArrayList<IData> refCols = loaded
				.getDataAtCurrentTime("Number of Columns");

		// Make sure the sizes are correct
		if (loadedRows.size() != 1 || loadedCols.size() != 1
				|| refRows.size() != 1 || refCols.size() != 1) {
			System.err
					.println("Invalid Data for Pin row and column numbers. Must be provided as "
							+ "an ArrayList<IData> of size 1.");
			return false;
		}

		// We've made it here, the nRows and nCols are valid
		double nRows = loadedRows.get(0).getValue();
		double nCols = loadedCols.get(0).getValue();

		// Make sure each has the same number of rows and columns
		if (nRows != (int) refRows.get(0).getValue()) {
			System.err
					.println("Loaded Data and Reference Data do not have the same number of rows.");
			return false;
		}
		if (nCols != (int) refCols.get(0).getValue()) {
			System.err
					.println("Loaded Data and Reference Data do not have the same number of columns.");
			return false;
		}

		nPinRows = (int) nRows;
		nPinCols = (int) nCols;

		// Get the number of assemblies and validate it
		ArrayList<IData> assemblies = loaded
				.getDataAtCurrentTime("Number of Assemblies");
		if (assemblies.size() != 1) {
			System.err
					.println("Invalid number of assemblies ArrayList. Must have only one element.");
			return false;
		}

		// Set the Number of assemblies
		double nAss = assemblies.get(0).getValue();
		if (nAss != (int) assemblies.get(0).getValue()) {
			return false;
		}

		nAssemblies = (int) nAss;

		nAxial = loaded.getDataAtCurrentTime("Data").size()
				/ (nPinRows * nPinCols * nAssemblies);
		isAvailable = true;

		// FIXME Make sure there are position values.

		return isAvailable;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#getStrategyPropertiesAsEntries()
	 */
	public ArrayList<Entry> getStrategyPropertiesAsEntries() {
		GodfreyStrategy temp = new GodfreyStrategy();
		return temp.getPropertiesAsEntryList();
	}
}