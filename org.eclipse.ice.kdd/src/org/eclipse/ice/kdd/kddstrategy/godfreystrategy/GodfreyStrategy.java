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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.kdd.kddmath.IDataMatrix;
import org.eclipse.ice.kdd.kddmath.KDDMatrix;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;
import org.eclipse.ice.kdd.kddstrategy.compositestrategy.CompositeStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * The GodfreyStrategy is a CompositeStrategy that is composed of a number of
 * KDDStrategies that are used in calculating various statistics on the axial
 * power, radial power, assembly power, and full reactor power. Clients should
 * provide an IDataProvider that contains the features "Number of Pin Rows",
 * "Number of Pin Columns", "Number of Assemblies", and "Data". The features
 * should correspond to one element ArrayLists of IData giving the pin power
 * matrix size. This Strategy will then parse the data for the number of axial
 * levels, and create a HashMap of assembly indices to an ArrayList of
 * KDDMatrices of size number of axial levels.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class GodfreyStrategy extends CompositeStrategy {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(GodfreyStrategy.class);

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
	 * Reference to pin power difference between the data and the reference
	 * data.
	 * </p>
	 * 
	 */
	private HashMap<Integer, ArrayList<IDataMatrix>> loadedPinPowers;

	/**
	 * <p>
	 * Reference to the rank-4 weight tensor.
	 * </p>
	 * 
	 */
	private HashMap<Integer, ArrayList<KDDMatrix>> weights;

	/**
	 * 
	 */
	private HashMap<Integer, ArrayList<IDataMatrix>> refPinPowers;

	/**
	 * 
	 */
	private SubStrategyFactory subStrategyFactory;

	/**
	 * <p>
	 * The constructor. Should initialize the following properties: Symmetry
	 * (default is Full, other options are Half, Quarter, Eighth),
	 * DifferenceType (default is Basic, other option is Relative),
	 * "SubStrategyName" that takes values yes or no, indicating whether the
	 * user wants to run the particular sub strategy algorithm. Note that at
	 * this point, the data should already be validated.
	 * </p>
	 * 
	 * @param nRows
	 * @param nCols
	 * @param nAxial
	 * @param nAssemblies
	 * @param data
	 * @param refData
	 * @throws IllegalArgumentException
	 */
	public GodfreyStrategy(int nRows, int nCols, int nAxial, int nAssemblies,
			ArrayList<IData> data, ArrayList<IData> refData) {
		super();

		// Set the name of this Strategy,
		// will be displayed on ICEResource
		assetName = "Godfrey Pin Difference Strategy";

		// Initialize values
		this.nAxial = nAxial;
		this.nAssemblies = nAssemblies;
		nPinRows = nRows;
		nPinCols = nCols;
		subStrategyFactory = new SubStrategyFactory();

		// Populate the properties
		// Value could also be Relative
		properties.put("Difference Type", "Basic");
		// Could be Half, Quarter, Eighth
		properties.put("Symmetry Type", "Full");
		properties.put("Pin Power Difference", "yes");
		properties.put("Axial Power", "yes");
		properties.put("Radial Power", "yes");

		// Create the data partitions
		this.loadedPinPowers = partitionData(data);
		this.refPinPowers = partitionData(refData);

		// Initialize the weights
		weights = new HashMap<Integer, ArrayList<KDDMatrix>>();

		return;
	}

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 */
	public GodfreyStrategy(SubStrategyFactory factory, int nRows, int nCols,
			int nAxial, int nAssemblies, ArrayList<IData> data,
			ArrayList<IData> refData) {
		super();

		// Set the name of this Strategy,
		// will be displayed on ICEResource
		assetName = "Godfrey Pin Difference Strategy";

		// Initialize values
		this.nAxial = nAxial;
		this.nAssemblies = nAssemblies;
		nPinRows = nRows;
		nPinCols = nCols;
		subStrategyFactory = factory;

		// Populate the properties
		// Value could also be Relative
		properties.put("Difference Type", "Basic");
		// Could be Half, Quarter, Eighth
		properties.put("Symmetry Type", "Full");
		properties.put("Axial Power", "yes");
		properties.put("Radial Power", "yes");
		properties.put("Pin Power Difference", "yes");

		// Create the data partitions
		this.loadedPinPowers = partitionData(data);
		this.refPinPowers = partitionData(refData);

		// Initialize the weights
		weights = new HashMap<Integer, ArrayList<KDDMatrix>>();

		return;

	}

	/**
	 * <p>
	 * The nullary constructor.
	 * </p>
	 * 
	 */
	public GodfreyStrategy() {
		super();
	}

	@Override
	public boolean executeStrategy() {

		// Make sure the data isn't null
		if (loadedPinPowers == null || refPinPowers == null) {
			return false;
		}
		// Fill the Weights Mapping
		if (!calculateWeights()) {
			return false;
		}
		// Create this CompositeStrategy's children, clear just in
		// case this isn't the first time this strategy has been
		// executed.
		strategies.clear();
		KDDStrategy temp;

		// We want to know if we are to run the PinDiff strategy,
		// other strategies depend on it.
		if ("yes".equals(properties.get("Pin Power Difference"))) {
			strategies.add(subStrategyFactory.createSubStrategy(
					"Pin Power Difference", loadedPinPowers, refPinPowers,
					weights, properties));
		}

		for (String name : properties.keySet()) {
			if (!("Pin Power Difference").equals(name)
					&& "yes".equals(properties.get(name))
					&& (temp = subStrategyFactory.createSubStrategy(name,
							loadedPinPowers, refPinPowers, weights, properties)) != null) {
				logger.info("Adding " + name + " Sub-Strategy");
				strategies.add(temp);
			}
		}

		// Since this is a CompositeStrategy, loop over its children
		// invoking executeStrategy on each
		logger.info("Executing sub-strategies...");
		if (super.executeStrategy()) {
			return createAsset();
		} else {
			return false;
		}
	}

	private boolean createAsset() {
		ArrayList<URI> uris = new ArrayList<URI>();
		ArrayList<String> fileContents = new ArrayList<String>();
		String line = "", contents = "";

		// Get the default project, which should be
		// the only element in getProjects()
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		if (root.getProjects().length == 0) {
			System.err.println("Invalid Project Workspace. Execution Failed.");
			return false;
		}

		// Get the IProject
		IProject project = root.getProjects()[0];

		// Create a handle to the file we are going to write to
		IFile file = project.getFile("godfreydata.txt");

		// If this file exists, then we've already used
		// it to write cluster data, so lets create a file
		// with a different name
		if (file.exists()) {
			int counter = 1;
			while (file.exists()) {
				file = project.getFile("godfreydata_" + String.valueOf(counter)
						+ ".txt");
				counter++;
			}
		}

		// Write general data
		fileContents.add("Number of Assemblies: " + nAssemblies);
		fileContents.add("Number of Axial Levels: " + nAxial);
		fileContents.add("Number of Pin Rows: " + nPinRows);
		fileContents.add("Number of Pin Columns: " + nPinCols);
		fileContents.add("");

		// Since this is a Composite KDDStrategy, here we
		// should collect all the sub-strategy's URIs
		// and combine them into one URI for use in ICE
		for (KDDStrategy strategy : strategies) {
			logger.info("Adding URI for " + strategy.getName());
			uris.add(strategy.getURI());
		}

		// Pattern is used for cross platform compatibility
		IFile subFile;
		BufferedReader reader = null;
		File tempFile = null;
		for (URI u : uris) {
			// Creating a File object because it works
			// well with all platforms...
			tempFile = new File(u.getPath());
			subFile = project.getFile(tempFile.getName());

			// If it doesn't exist, just move on to the next subfile
			if (!subFile.exists()) {
				System.err
						.println("Failed to locate file " + subFile.getName());
				continue;
			}

			try {
				reader = new BufferedReader(new InputStreamReader(
						subFile.getContents()));
				while ((line = reader.readLine()) != null) {
					fileContents.add(line);
				}
			} catch (CoreException e) {
				logger.error(getClass().getName() + " Exception!",e);
				return false;
			} catch (IOException e) {
				logger.error(getClass().getName() + " Exception!",e);
				return false;
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
			logger.error(getClass().getName() + " Exception!",e);
			return false;
		}

		// set the URI
		uri = file.getLocationURI();

		return true;
	}

	/**
	 * <p>
	 * Return the number of rows in the pin powers matrix.
	 * </p>
	 * 
	 * @return
	 */
	public int getNumberOfPinRows() {
		return nPinRows;
	}

	/**
	 * <p>
	 * Return the number of columns in the pin powers matrix.
	 * </p>
	 * 
	 * @return
	 */
	public int getNumberOfPinCols() {
		return nPinCols;
	}

	/**
	 * <p>
	 * Return the number of axial levels in a given fuel pin.
	 * </p>
	 * 
	 * @return
	 */
	public int getNumberOfAxialLevels() {
		return nAxial;
	}

	/**
	 * <p>
	 * Return the number of fuel pin assemblies.
	 * </p>
	 * 
	 * @return
	 */
	public int getNumberOfAssemblies() {
		return nAssemblies;
	}

	/**
	 * <p>
	 * This operation returns the asset's properties as a list of Entry objects.
	 * </p>
	 * 
	 * @return
	 */
	@Override
	public ArrayList<Entry> getPropertiesAsEntryList() {

		// Value could also be Relative
		// Could be Half, Quarter, Eighth

		// Symmetry Type Entry
		Entry symType = new Entry() {
			@Override
			public void setup() {
				// Set the particulars
				this.objectName = "Symmetry Type";
				this.uniqueId = 3;
				this.objectDescription = "The list of available reactor symmetries. "
						+ "Dictates the weighting used in ensuing statistical calculations.";
				// Set the data sources list
				allowedValueType = AllowedValueType.Discrete;
				allowedValues.add("Full");
			}
		};

		// Pin Difference Strategy
		Entry pinDiff = new Entry() {
			@Override
			public void setup() {
				// Set the particulars
				this.objectName = "Pin Difference Sub-Strategy";
				this.uniqueId = 4;
				this.objectDescription = "";
				// Set the data sources list
				allowedValueType = AllowedValueType.Discrete;
				allowedValues.add("yes");
				allowedValues.add("no");
			}

		};

		// Axial Power Strategy
		Entry axial = new Entry() {
			@Override
			public void setup() {
				// Set the particulars
				this.objectName = "Axial Power Sub-Strategy";
				this.uniqueId = 5;
				this.objectDescription = "";
				// Set the data sources list
				allowedValueType = AllowedValueType.Discrete;
				allowedValues.add("yes");
				allowedValues.add("no");
			}
		};

		// Radial Power Strategy
		Entry radial = new Entry() {
			@Override
			public void setup() {
				// Set the particulars
				this.objectName = "Radial Power Sub-Strategy";
				this.uniqueId = 1;
				this.objectDescription = "";
				// Set the data sources list
				allowedValueType = AllowedValueType.Discrete;
				allowedValues.add("yes");
				allowedValues.add("no");
			}
		};

		ArrayList<Entry> retEntries = new ArrayList<Entry>();
		retEntries.add(symType);
		retEntries.add(pinDiff);
		retEntries.add(axial);
		retEntries.add(radial);

		return retEntries;
	}

	/**
	 * <p>
	 * Partition this list of IData elements into nAxial matrices per each
	 * assembly.
	 * </p>
	 * 
	 * @param data
	 * @return
	 */
	private HashMap<Integer, ArrayList<IDataMatrix>> partitionData(
			ArrayList<IData> data) {

		// Local Declarations
		ArrayList<ArrayList<IData>> assemblyPartitions = new ArrayList<ArrayList<IData>>();
		ArrayList<ArrayList<IData>> axialPartitions = new ArrayList<ArrayList<IData>>();
		HashMap<Integer, ArrayList<IDataMatrix>> result = new HashMap<Integer, ArrayList<IDataMatrix>>();
		ArrayList<IDataMatrix> matrices;
		int increment = 0, counter = 0;

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

	/**
	 * <p>
	 * This method generates the rank-4 weight tensor to be used in the
	 * calculation of all required quantities. Its construction is dependent on
	 * the Symmetry Type property
	 * </p>
	 * 
	 */
	private boolean calculateWeights() {
		// TODO Auto-generated method stub
		ArrayList<KDDMatrix> matrices = new ArrayList<KDDMatrix>();
		KDDMatrix matrix = new KDDMatrix(nPinRows, nPinCols);
		if ("Full".equals(properties.get("Symmetry Type"))) {

			// For full symmetry, we want the weights to be all
			// 1.0
			for (int i = 0; i < nPinRows; i++) {
				for (int j = 0; j < nPinCols; j++) {
					matrix.setElement(i, j, 1.0);
				}
			}

			// Add nAxial Matrices to each assembly
			for (int l = 0; l < nAssemblies; l++) {
				for (int k = 0; k < nAxial; k++) {
					matrices.add(matrix);
				}
				// Add the matrices to this assembly
				weights.put(l, (ArrayList<KDDMatrix>) matrices.clone());

				// Clear for the next go round
				matrices.clear();
			}

			return true;
		} else if ("Half".equals(properties.get("Symmetry"))) {
			return false;
		}

		// Bad if we get here, return false
		return false;

	}

}