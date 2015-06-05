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
package org.eclipse.ice.kdd.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.kdd.kddmath.IDataMatrix;
import org.eclipse.ice.kdd.kddmath.KDDMatrix;
import org.eclipse.ice.kdd.kddstrategy.godfreystrategy.PinPowerDifference;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleData;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;

/**
 * 
 * @author Alex McCaskey
 */
public class PinPowerDifferenceTester {
	/**
	 * 
	 */
	private PinPowerDifference pinDiff;

	private HashMap<Integer, ArrayList<KDDMatrix>> expectedDiff;

	/**
	 * 
	 */
	@Before
	public void beforeClass() {
		ArrayList<IDataProvider> providers = getTestData();

		// Partition the loaded data into HashMaps...
		HashMap<Integer, ArrayList<IDataMatrix>> loaded = partitionData(providers
				.get(0).getDataAtCurrentTime("Data"));
		HashMap<Integer, ArrayList<IDataMatrix>> ref = partitionData(providers
				.get(1).getDataAtCurrentTime("Data"));
		HashMap<Integer, ArrayList<IDataMatrix>> result = partitionData(providers
				.get(2).getDataAtCurrentTime("Data"));

		// Set the only property this sub strategy needs.
		HashMap<String, String> props = new HashMap<String, String>();
		props.put("Difference Type", "Basic");

		pinDiff = new PinPowerDifference(loaded, ref, props);
		expectedDiff = new HashMap<Integer, ArrayList<KDDMatrix>>();

		KDDMatrix matrix;
		ArrayList<KDDMatrix> matrices;
		for (int l = 0; l < result.size(); l++) {
			matrices = new ArrayList<KDDMatrix>();
			for (int k = 0; k < result.get(0).size(); k++) {
				matrix = new KDDMatrix(result.get(0).get(0).numberOfRows(),
						result.get(0).get(0).numberOfColumns());
				for (int i = 0; i < result.get(0).get(0).numberOfRows(); i++) {
					for (int j = 0; j < result.get(0).get(0).numberOfColumns(); j++) {
						matrix.setElement(i, j, result.get(l).get(k)
								.getElementValue(i, j));
					}
				}
				matrices.add(matrix);
			}
			expectedDiff.put(l, matrices);
		}

	}

	/**
	 * 
	 */
	@Test
	public void checkExecuteStrategy() {

		// Local Declarations
		double offset = .00099999;
		assertTrue(pinDiff.executeStrategy());

		// Loop over all elements and make sure they are correct
		// within a certain error. Godfrey's stuff only has 3 sig figs...
		for (int l = 0; l < pinDiff.getPinPowerDifference().size(); l++) {
			for (int k = 0; k < pinDiff.getPinPowerDifference().get(0).size(); k++) {
				for (int i = 0; i < pinDiff.getPinPowerDifference().get(0)
						.get(0).numberOfRows(); i++) {
					for (int j = 0; j < pinDiff.getPinPowerDifference().get(0)
							.get(0).numberOfColumns(); j++) {
						assertEquals(expectedDiff.get(l).get(k)
								.getElement(i, j), pinDiff
								.getPinPowerDifference().get(l).get(k)
								.getElement(i, j), offset);
					}
				}
			}
		}

		// Make sure we get a valid URI
		assertNotNull(pinDiff.getURI());
	}

	/**
	 * 
	 * @return
	 */
	private ArrayList<IDataProvider> getTestData() {
		// Need some data....
		IDataProvider provider = new SimpleDataProvider();
		IDataProvider refProvider = new SimpleDataProvider();
		IDataProvider resultProvider = new SimpleDataProvider();

		String workspaceName = "kddtestworkspace";
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + workspaceName;
		IProject project = null;

		try {
			// Get the project handle
			project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(workspaceName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				URI projURI = (new File(userDir)).toURI();
				// Create the project description
				IProjectDescription description = ResourcesPlugin
						.getWorkspace().newProjectDescription(workspaceName);
				// Set the location of the project
				description.setLocationURI(projURI);
				// Create the project
				project.create(description, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}
		} catch (CoreException e) {
			// Catch for creating the project
			e.printStackTrace();
			fail();
		}

		// Delete any old kmeans files.
		try {
			for (IResource r : project.members()) {
				if (r.getType() == IResource.FILE
						&& r.getName().contains("pindifferences")) {
					r.delete(true, null);
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
			fail();
		}

		// Create a JadeD Files directory in project workspace
		IFolder dataFolder = project.getFolder("data");
		if (!dataFolder.exists()) {
			fail();
		}

		IFile denovo = dataFolder.getFile("denovo_results2.csv");
		IFile keno = dataFolder.getFile("keno2.csv");
		IFile result = dataFolder.getFile("resultPinDiff.csv");
		FileInputStream input, refInput, resultInput;
		ArrayList<IData> elements = new ArrayList<IData>();
		ArrayList<IData> refElements = new ArrayList<IData>();
		ArrayList<IData> resultElements = new ArrayList<IData>();

		try {
			input = (FileInputStream) denovo.getContents();
			refInput = (FileInputStream) keno.getContents();
			resultInput = (FileInputStream) result.getContents();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			BufferedReader refReader = new BufferedReader(
					new InputStreamReader(refInput));
			BufferedReader resultReader = new BufferedReader(
					new InputStreamReader(resultInput));
			ArrayList<Double> pos;

			String line = null, refLine = null, resultLine = null;
			String[] numbers, refNumbers, resultNumbers;
			SimpleData data, refData, resultData;
			while ((line = reader.readLine()) != null) {
				numbers = line.split(",");
				for (int i = 0; i < numbers.length; i++) {
					pos = new ArrayList<Double>();
					data = new SimpleData("Data",
							Double.parseDouble(numbers[i]));
					data.setUncertainty(0.0);
					pos.add(0.0);
					pos.add(0.0);
					pos.add(1.0);
					data.setPosition(pos);
					pos.clear();
					elements.add(data);
				}
			}

			while ((refLine = refReader.readLine()) != null) {
				refNumbers = refLine.split(",");
				for (int i = 0; i < refNumbers.length; i++) {
					pos = new ArrayList<Double>();
					refData = new SimpleData("Data",
							Double.parseDouble(refNumbers[i]));
					refData.setUncertainty(0.0);
					pos.add(0.0);
					pos.add(0.0);
					pos.add(1.0);
					refData.setPosition(pos);
					pos.clear();
					refElements.add(refData);
				}
			}

			while ((resultLine = resultReader.readLine()) != null) {
				resultNumbers = resultLine.split(",");
				for (int i = 0; i < resultNumbers.length; i++) {
					pos = new ArrayList<Double>();
					resultData = new SimpleData("Data",
							Double.parseDouble(resultNumbers[i]));
					resultData.setUncertainty(0.0);
					pos.add(0.0);
					pos.add(0.0);
					pos.add(1.0);
					resultData.setPosition(pos);
					pos.clear();
					resultElements.add(resultData);
				}
			}
			
			// Close the readers.
			reader.close();
			refReader.close();
			resultReader.close();

		} catch (CoreException e) {
			e.printStackTrace();
			fail();
		} catch (IOException ex) {
			ex.printStackTrace();
			fail();
		} catch (NumberFormatException exx) {
			exx.printStackTrace();
			fail();
		}

		ArrayList<IData> rowNumber = new ArrayList<IData>();
		ArrayList<IData> colNumber = new ArrayList<IData>();
		ArrayList<IData> assemblyNumber = new ArrayList<IData>();

		rowNumber.add(new SimpleData("Number of Rows", 49.0));
		colNumber.add(new SimpleData("Number of Columns", 39.0));
		assemblyNumber.add(new SimpleData("Number of Assembly", 1.0));

		((SimpleDataProvider) provider).addData(elements, "Data");
		((SimpleDataProvider) refProvider).addData(refElements, "Data");
		((SimpleDataProvider) resultProvider).addData(resultElements, "Data");

		((SimpleDataProvider) provider).addData(rowNumber, "Number of Rows");
		((SimpleDataProvider) provider).addData(colNumber, "Number of Columns");
		((SimpleDataProvider) provider).addData(assemblyNumber,
				"Number of Assemblies");

		((SimpleDataProvider) refProvider).addData(rowNumber, "Number of Rows");
		((SimpleDataProvider) refProvider).addData(colNumber,
				"Number of Columns");
		((SimpleDataProvider) refProvider).addData(assemblyNumber,
				"Number of Assemblies");

		((SimpleDataProvider) resultProvider).addData(rowNumber,
				"Number of Rows");
		((SimpleDataProvider) resultProvider).addData(colNumber,
				"Number of Columns");
		((SimpleDataProvider) resultProvider).addData(assemblyNumber,
				"Number of Assemblies");

		ArrayList<IDataProvider> retProvs = new ArrayList<IDataProvider>();
		retProvs.add(provider);
		retProvs.add(refProvider);
		retProvs.add(resultProvider);

		return retProvs;
	}

	private HashMap<Integer, ArrayList<IDataMatrix>> partitionData(
			ArrayList<IData> data) {

		// Local Declarations
		ArrayList<ArrayList<IData>> assemblyPartitions = new ArrayList<ArrayList<IData>>();
		ArrayList<ArrayList<IData>> axialPartitions = new ArrayList<ArrayList<IData>>();
		HashMap<Integer, ArrayList<IDataMatrix>> result = new HashMap<Integer, ArrayList<IDataMatrix>>();
		ArrayList<IDataMatrix> matrices;
		int increment = 0, counter = 0;
		int nAssemblies = 1, nAxial = 1, nPinRows = 49, nPinCols = 39;

		// Now we need to partition the data into assemblies
		// Get the number of elements per assembly, might not
		// be a whole number, in which case we'll return null
		double elementsInAssembly = ((double) data.size()) / nAssemblies;
		// Make sure elementsInAssembly is an integer
		if (elementsInAssembly != (int) elementsInAssembly) {
			System.err
					.println("Invalid number of elements in a given assembly.");
			return null;
		}

		// Set the increment, since it was valid
		increment = (int) elementsInAssembly;

		// Partition into nAssembly sets of elements...
		for (int l = 0; l < data.size(); l += increment) {
			assemblyPartitions.add(new ArrayList<IData>(data.subList(l, l
					+ increment)));
		}

		// Now we need to partition the assemblies set into axial matrix sets
		for (ArrayList<IData> assembly : assemblyPartitions) {
			// Create new lists to hold this data
			axialPartitions = new ArrayList<ArrayList<IData>>();
			matrices = new ArrayList<IDataMatrix>();

			// Partition into nAxial elements
			for (int k = 0; k < assembly.size(); k += nPinRows * nPinCols) {
				axialPartitions.add(new ArrayList<IData>(assembly.subList(k, k
						+ nPinRows * nPinCols)));
			}

			// Add the nAxial Matrices corresponding to this Assembly
			for (ArrayList<IData> matElements : axialPartitions) {
				matrices.add(new IDataMatrix(nPinRows, nPinCols, matElements));
			}

			// Place them in the map
			result.put(counter, matrices);
			counter++;
		}

		return result;
	}
}