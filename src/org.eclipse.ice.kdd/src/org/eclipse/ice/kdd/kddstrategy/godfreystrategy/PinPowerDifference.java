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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.kdd.kddmath.IDataMatrix;
import org.eclipse.ice.kdd.kddmath.KDDMatrix;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * PinPowerDifference is a subclass of GodfreySubStrategy that takes the user
 * input nuclear reactor pin power data and produces the difference between it
 * and some reference set of pin power data. It produces a URI whose file
 * contains the pin power difference matrices at each axial level and assembly,
 * as well as the uncertainties in those differences. It also outputs the
 * maximum power difference by axial level.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class PinPowerDifference extends GodfreySubStrategy {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the calculated pin power difference matrices.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private HashMap<Integer, ArrayList<KDDMatrix>> difference;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the uncertainties pin power difference matrices.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private HashMap<Integer, ArrayList<KDDMatrix>> uncertaintyDiff;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the type of difference this sub strategy should calculate.
	 * This value can be Basic or Relative.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String differenceType;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor. It takes the difference type, which could be Basic or
	 * Relative, and the loaded pin powers, reference pin powers, and the
	 * symmetry dependent weights.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param pinPowers
	 * @param refPinPowers
	 * @param props
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PinPowerDifference(
			HashMap<Integer, ArrayList<IDataMatrix>> pinPowers,
			HashMap<Integer, ArrayList<IDataMatrix>> refPinPowers,
			HashMap<String, String> props) {
		// begin-user-code
		super(pinPowers, refPinPowers, null);
		difference = new HashMap<Integer, ArrayList<KDDMatrix>>();
		uncertaintyDiff = new HashMap<Integer, ArrayList<KDDMatrix>>();

		assetName = "Pin Power Difference Godfrey Sub-Strategy";

		// Set the difference type
		if (props.get("Difference Type") != null) {
			differenceType = props.get("Difference Type");
		}

		// Make sure the passed in a valid string
		if (!("Basic").equals(differenceType)
				|| !("Relative".equals(differenceType))) {
			differenceType = "Basic";
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method allows subclasses to implement a unique clustering or anomaly
	 * detection algorithm and produce a KDDAnalysisAsset for clients to display
	 * and manipulate.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean executeStrategy() {
		// begin-user-code

		// Make sure we have valid data to work with
		if (loadedPinPowers == null || refPinPowers == null) {
			return false;
		}

		// Local Declarations
		int nAssemblies = loadedPinPowers.size();
		int nAxial = loadedPinPowers.get(0).size();
		int nRows = loadedPinPowers.get(0).get(0).numberOfRows();
		int nCols = loadedPinPowers.get(0).get(0).numberOfColumns();
		KDDMatrix matrix, uncertainMatrix;
		ArrayList<KDDMatrix> matrices;
		ArrayList<KDDMatrix> unMatrices;

		// Perform the loaded and reference differences
		// Also, get the difference in the uncertainties.
		for (int l = 0; l < nAssemblies; l++) {
			matrices = new ArrayList<KDDMatrix>();
			unMatrices = new ArrayList<KDDMatrix>();
			for (int k = 0; k < nAxial; k++) {
				matrix = new KDDMatrix(nRows, nCols);
				uncertainMatrix = new KDDMatrix(nRows, nCols);
				for (int i = 0; i < nRows; i++) {
					for (int j = 0; j < nCols; j++) {
						if ("Relative".equals(differenceType)) {

							// Subtract the data, relative type
							matrix.setElement(
									i,
									j,
									(loadedPinPowers.get(l).get(k)
											.getElementValue(i, j) - refPinPowers
											.get(l).get(k)
											.getElementValue(i, j))
											/ refPinPowers.get(l).get(k)
													.getElementValue(i, j));

							// Subtract the uncertainties, relative type
							uncertainMatrix
									.setElement(
											i,
											j,
											(loadedPinPowers
													.get(l)
													.get(k)
													.getElementUncertainty(i, j) - refPinPowers
													.get(l)
													.get(k)
													.getElementUncertainty(i, j))
													/ refPinPowers
															.get(l)
															.get(k)
															.getElementUncertainty(
																	i, j));

						} else {
							// Subtract the data, basic type
							matrix.setElement(i, j,
									loadedPinPowers.get(l).get(k)
											.getElementValue(i, j)
											- refPinPowers.get(l).get(k)
													.getElementValue(i, j));

							// Subtract the uncertainties, basic type
							uncertainMatrix.setElement(i, j, loadedPinPowers
									.get(l).get(k).getElementUncertainty(i, j)
									- refPinPowers.get(l).get(k)
											.getElementUncertainty(i, j));
						}
					}
				}

				// Add them to the temp arraylists
				matrices.add(matrix);
				unMatrices.add(uncertainMatrix);
			}

			// Set them in the final result
			difference.put(l, matrices);
			uncertaintyDiff.put(l, unMatrices);
		}

		// Create this Asset
		return createAsset();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method is for the creation of this IAnalysisAsset's URI.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean createAsset() {
		// begin-user-code
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
		IFile file = project.getFile("pindifferences.txt");

		// If this file exists, then we've already used
		// it to write cluster data, so lets create a file
		// with a different name
		if (file.exists()) {
			int counter = 1;
			while (file.exists()) {
				file = project.getFile("pindifferences_"
						+ String.valueOf(counter) + ".txt");
				counter++;
			}
		}

		int nAssemblies = difference.size();
		int nAxial = difference.get(0).size();
		int nRows = difference.get(0).get(0).numberOfRows();
		int nCols = difference.get(0).get(0).numberOfColumns();

		fileContents.add("\nFuel Pin Difference");
		for (int l = 0; l < nAssemblies; l++) {
			fileContents.add("\nAssembly " + l);
			for (int k = 0; k < nAxial; k++) {
				fileContents.add("Axial Level " + k);
				for (int i = 0; i < nRows; i++) {
					for (int j = 0; j < nCols; j++) {
						line = line
								+ formatter.format(difference.get(l).get(k)
										.getElement(i, j)) + " ";
					}
					fileContents.add(line);
					line = "";
				}
			}
		}

		// Write the % Errors
		fileContents.add("\nFuel Pin Powers Uncertainties");
		for (int l = 0; l < nAssemblies; l++) {
			fileContents.add("\nAssembly " + l);
			for (int k = 0; k < nAxial; k++) {
				fileContents.add("Axial Level " + k);
				for (int i = 0; i < nRows; i++) {
					for (int j = 0; j < nCols; j++) {
						line = line
								+ formatter.format(uncertaintyDiff.get(l)
										.get(k).getElement(i, j)) + " ";
					}
					fileContents.add(line);
					line = "";
				}
			}
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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the pin power difference between the input data and the reference
	 * data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public HashMap<Integer, ArrayList<KDDMatrix>> getPinPowerDifference() {
		// begin-user-code
		return difference;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the uncertainty difference between the input data and the
	 * reference data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public HashMap<Integer, ArrayList<KDDMatrix>> getUncertaintyDifference() {
		// begin-user-code
		return uncertaintyDiff;
		// end-user-code
	}

}