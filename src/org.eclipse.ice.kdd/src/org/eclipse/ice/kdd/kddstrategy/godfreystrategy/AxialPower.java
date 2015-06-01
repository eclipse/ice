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

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.kdd.kddmath.IDataMatrix;
import org.eclipse.ice.kdd.kddmath.KDDMatrix;

/**
 * <p>
 * AxialPower is a subclass of GodfreySubStrategy that takes both the raw user
 * input nuclear reactor pin power data as well as the pin power difference and
 * produces the axial power data.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class AxialPower extends GodfreySubStrategy {
	/**
	 * <p>
	 * Reference to the pin power difference calculations.
	 * </p>
	 * 
	 * 
	 */
	private PinPowerDifference differences;

	/**
	 * <p>
	 * Reference to the axial power.
	 * </p>
	 * 
	 */
	private KDDMatrix axialPower;

	/**
	 * <p>
	 * Reference to the generated axial power for the pin power differences.
	 * </p>
	 * 
	 */
	private KDDMatrix axialPowerDiff;

	/**
	 * <p>
	 * Reference to the average axial power.
	 * </p>
	 * 
	 */
	private Double axialPowerAverage;
	/**
	 * <p>
	 * Reference to the average axial power difference.
	 * </p>
	 * 
	 */
	private Double axialPowerDiffAverage;
	/**
	 * <p>
	 * Reference to the RMS average axial power.
	 * </p>
	 * 
	 */
	private Double axialPowerRMS;
	/**
	 * <p>
	 * Reference to the RMS axial power difference.
	 * </p>
	 * 
	 */
	private Double axialPowerDiffRMS;

	private boolean calculateDiffs;

	/**
	 * The constructor takes an instance of the PinPowerDifference substrategy
	 * for use in calculating the axial power difference.
	 * 
	 * @param difference
	 * @param pinPowers
	 * @param refPinPowers
	 * @param weights
	 */
	public AxialPower(PinPowerDifference difference,
			HashMap<Integer, ArrayList<IDataMatrix>> pinPowers,
			HashMap<Integer, ArrayList<IDataMatrix>> refPinPowers,
			HashMap<Integer, ArrayList<KDDMatrix>> weights) {
		super(pinPowers, refPinPowers, weights);

		assetName = "Axial Power Godfrey Sub-Strategy";
		axialPower = new KDDMatrix(loadedPinPowers.get(0).size(), 1);
		axialPowerDiff = new KDDMatrix(loadedPinPowers.get(0).size(), 1);
		if (difference == null) {
			calculateDiffs = false;
		} else {
			differences = difference;
			calculateDiffs = true;
		}
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
	public boolean executeStrategy() {
		// Local Declarations
		int nAxial = loadedPinPowers.get(0).size();
		int nAssemblies = loadedPinPowers.size();
		int nRows = loadedPinPowers.get(0).get(0).numberOfRows();
		int nCols = loadedPinPowers.get(0).get(0).numberOfColumns();
		double sum = 0.0, weightSum = 0.0;
		KDDMatrix axialMesh = new KDDMatrix(nAxial + 1, 1);
		axialMesh.setElement(nAxial, 0, 0.0);

		// Calculate the Axial Power
		for (int k = 0; k < nAxial; k++) {
			for (int l = 0; l < nAssemblies; l++) {
				for (int i = 0; i < nRows; i++) {
					for (int j = 0; j < nCols; j++) {
						sum = sum
								+ loadedPinPowers.get(l).get(k)
										.getElementValue(i, j)
								* weights.get(l).get(k).getElement(i, j);
						weightSum = weightSum
								+ weights.get(l).get(k).getElement(i, j);
					}
				}
			}
			axialPower.setElement(k, 0, Math.sqrt(Math.abs(sum / weightSum)));
			sum = 0.0;
			weightSum = 0.0;
		}

		// Calculate the Axial Power Differences
		// first re-run the PinPowerDifference,
		// just in case a property was changed
		if (calculateDiffs && !differences.executeStrategy()) {
			return false;
		}
		if (calculateDiffs) {
			for (int k = 0; k < nAxial; k++) {
				for (int l = 0; l < nAssemblies; l++) {
					for (int i = 0; i < nRows; i++) {
						for (int j = 0; j < nCols; j++) {
							sum = sum
									+ differences.getPinPowerDifference()
											.get(l).get(k).getElement(i, j)
									* weights.get(l).get(k).getElement(i, j);
							weightSum = weightSum
									+ weights.get(l).get(k).getElement(i, j);
						}
					}
				}
				axialPowerDiff.setElement(k, 0,
						Math.sqrt(Math.abs(sum / weightSum)));
				sum = 0.0;
				weightSum = 0.0;
			}

		}
		// This assumes the z positions are the same across all assemblies,
		// and axial level.
		for (int i = nAxial - 1; i >= 0; i--) {
			axialMesh.setElement(i, 0, loadedPinPowers.get(0).get(i)
					.getElementPosition(0, 0).get(2));
		}

		// Calculate the Axial Mesh for the avg and rms calculations.
		double rmsSum = 0.0, diffSum = 0.0, diffRMSSum = 0.0;
		for (int k = 0; k < nAxial; k++) {
			sum = sum + axialPower.getElement(k, 0)
					* axialMesh.getElement(k, 0);
			diffSum = diffSum + axialPowerDiff.getElement(k, 0)
					* axialMesh.getElement(k, 0);
			rmsSum = rmsSum + Math.pow(axialPower.getElement(k, 0), 2)
					* axialMesh.getElement(k, 0);
			if (calculateDiffs) {
				diffRMSSum = diffRMSSum
						+ Math.pow(axialPowerDiff.getElement(k, 0), 2)
						* axialMesh.getElement(k, 0);
			}
			weightSum = weightSum + axialMesh.getElement(k, 0);
		}

		axialPowerAverage = Math.abs(sum / weightSum);
		axialPowerRMS = Math.sqrt(Math.abs(rmsSum / weightSum));
		if (calculateDiffs) {
			axialPowerDiffAverage = Math.abs(diffSum / weightSum);
			axialPowerDiffRMS = Math.sqrt(Math.abs(diffRMSSum / weightSum));
		}

		return createAsset();
	}

	/**
	 * <p>
	 * This method is responsible for creating the URI describing the generated
	 * data.
	 * </p>
	 * 
	 * @return
	 */
	private boolean createAsset() {
		ArrayList<String> fileContents = new ArrayList<String>();
		String line = "", contents = "";
		DecimalFormat formatter = new DecimalFormat("#.####");

		// Get the default project, which should be
		// the only element in getProjects()
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		if (root.getProjects().length == 0) {
			return false;
		}

		// Get the IProject
		IProject project = root.getProjects()[0];

		// Create a handle to the file we are going to write to
		IFile file = project.getFile("axialpower.txt");

		// If this file exists, then we've already used
		// it to write cluster data, so lets create a file
		// with a different name
		if (file.exists()) {
			int counter = 1;
			while (file.exists()) {
				file = project.getFile("axialpower_" + String.valueOf(counter)
						+ ".txt");
				counter++;
			}
		}

		// Get the number of axial levels
		int nAxial = loadedPinPowers.get(0).size();

		fileContents.add("\n---------- Axial Results ----------");
		fileContents.add("\nAxial Power");
		for (int k = 0; k < nAxial; k++) {
			fileContents.add(formatter.format(axialPower.getElement(k, 0)));
		}

		if (calculateDiffs) {
			fileContents.add("\nAxial Power Difference");
			for (int k = 0; k < nAxial; k++) {
				fileContents.add(formatter.format(axialPowerDiff.getElement(k,
						0)));
			}
		}

		fileContents.add("");
		fileContents.add("Average Axial Power: "
				+ formatter.format(axialPowerAverage));
		fileContents.add("RMS Axial Power: " + formatter.format(axialPowerRMS));

		if (calculateDiffs) {
			fileContents.add("Average Axial Power Difference: "
					+ formatter.format(axialPowerDiffAverage));
			fileContents.add("RMS Axial Power Difference: "
					+ formatter.format(axialPowerDiffRMS));
		}

		// Convert the ArrayList to one string
		// so we can use the getBytes method
		for (String s : fileContents) {
			contents = contents + s + "\n";
		}

		// Create the IFile with a ByteArrayInputStream
		try {
			file.create(new ByteArrayInputStream(contents.getBytes()), false,
					null);
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}

		// set the URI
		uri = file.getLocationURI();

		return true;
	}

	/**
	 * <p>
	 * Return the axial power difference.
	 * </p>
	 * 
	 * @return
	 */
	public KDDMatrix getAxialPower() {
		return axialPower;
	}

	/**
	 * <p>
	 * Return the axial power pin power difference.
	 * </p>
	 * 
	 * @return
	 */
	public KDDMatrix getAxialPowerDifference() {
		return axialPowerDiff;
	}

	/**
	 * <p>
	 * Return the average axial power.
	 * </p>
	 * 
	 * @return
	 */
	public Double getAxialPowerAverage() {
		return axialPowerAverage;
	}

	/**
	 * <p>
	 * Return the average axial power difference.
	 * </p>
	 * 
	 * @return
	 */
	public Double getAxialPowerDifferenceAverage() {
		return axialPowerDiffAverage;
	}

	/**
	 * <p>
	 * Return the RMS axial power.
	 * </p>
	 * 
	 * @return
	 */
	public Double getAxialPowerRMS() {
		return axialPowerRMS;
	}

	/**
	 * <p>
	 * Return the RMS axial power difference.
	 * </p>
	 * 
	 * @return
	 */
	public Double getAxialPowerDifferenceRMS() {
		return axialPowerDiffRMS;
	}
}