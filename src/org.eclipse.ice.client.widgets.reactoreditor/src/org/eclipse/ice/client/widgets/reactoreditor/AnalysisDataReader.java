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
package org.eclipse.ice.client.widgets.reactoreditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.reactor.LWRData;
import org.eclipse.ice.reactor.LWRDataProvider;

/**
 * This class should read in data into data structures used by the
 * ReactorEditor. Ideally, the files read in by this class should be
 * standardized in some way. This class should read the files into a array (an
 * "assembly") of data providers.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class AnalysisDataReader {
	// NOTES
	// 1) Currently, we have a Map of supported features. The map tells us if
	// we need to look for axial levels.
	// 2) readFeature expects the data to be in order from lowest-indexed to
	// highest in all cases. Axial data ordering may prove a problem if we
	// need to handle disordered feature data.
	// 3) Reading in the data should probably be handled by the specific
	// strategy that output the file in the first place.

	// FIXME - We may want to move this to the lwr package.

	/**
	 * A map of features supported by this reader and whether or not they use
	 * axial levels.
	 */
	private Map<String, Boolean> supportedFeatures;
	/**
	 * The arrays of data providers. The first index is the assembly, and the
	 * second is the rod data provider.
	 */
	private List<List<LWRDataProvider>> fuelAssemblies;
	/**
	 * The number of rows in each assembly.
	 */
	private int assemblyRows;
	/**
	 * The number of columns in each assembly.
	 */
	private int assemblyCols;
	/**
	 * The number of axial levels for data that uses axial levels.
	 */
	private int axialLevels;

	/**
	 * This is used to check the lines of input that contain the data. Since it
	 * is required in a few methods, we make it an instance variable.
	 */
	private String dataRowRegex;

	/**
	 * The default constructor. Standard procedure requires a call to readData()
	 * to actually read in the data.
	 */
	public AnalysisDataReader() {
		// Build the currently supported features (computed by the
		// GodfreyStrategy).
		supportedFeatures = new HashMap<String, Boolean>(4);
		supportedFeatures.put("Fuel Pin Difference", true);
		supportedFeatures.put("Fuel Pin Powers Uncertainties", true);
		supportedFeatures.put("Radial Power", false);
		supportedFeatures.put("Radial Power Difference", false);
	}

	/**
	 * Reads in data from a provided file. Data will be stored within this
	 * instance and should be queried through other functions.
	 * 
	 * @param uri
	 *            The URI of the data file to read from.
	 */
	public void readData(URI uri) {

		// Get a File object from the URI and get its name.
		File file = new File(uri.getPath());
		String fileName = file.getName();

		// Make sure the file exists.
		if (!file.exists()) {
			System.err.println("Invalid file " + fileName
					+ ". Cannot read data.");
			return;
		}

		// Read in the data.
		try {
			// Create a BufferedReader from the file.
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));

			// Get the number of assemblies.
			int assemblies = Integer.parseInt(reader.readLine().split(":")[1]
					.trim());
			// Get the number of axial levels.
			axialLevels = Integer.parseInt(reader.readLine().split(":")[1]
					.trim());
			// Get the number of rows in each assembly.
			assemblyRows = Integer.parseInt(reader.readLine().split(":")[1]
					.trim());
			// Get the number of columns in each assembly.
			assemblyCols = Integer.parseInt(reader.readLine().split(":")[1]
					.trim());

			// Set up the regular expressions used to check the lines of data.
			// dlbRegex is the regular expression for a double with the logic:
			// 1-or-more digits, maybe followed by a period and
			// 1-or-more-digits.
			String dblRegex = "-?\\d+(\\.\\d+)?";
			// dataRowRegex makes sure that the number of doubles on a line is
			// exactly the same as the number of columns in an assembly. The
			// logic here is:
			// Start of line (^), 0-or-more whitespace (\\s*), a double followed
			// by 1-or-more whitespace (dblRegex + \\s+) exactly assemblyCols-1
			// times ({assemblyCols - 1}), a double (dblRegex), 0-or-more
			// whitespace (\\s*), end of line ($).
			dataRowRegex = "^\\s*(" + dblRegex + "\\s+){" + (assemblyCols - 1)
					+ "}" + dblRegex + "\\s*$";

			// Initialize the array of fuel assemblies.
			fuelAssemblies = new ArrayList<List<LWRDataProvider>>(assemblies);

			// Each fuel assembly is an array of rod data providers.
			List<LWRDataProvider> fuelAssembly;

			// Build the array of fuel assemblies.
			for (int i = 0; i < assemblies; i++) {
				fuelAssembly = new ArrayList<LWRDataProvider>();
				for (int j = 0; j < assemblyRows * assemblyCols; j++) {
					fuelAssembly.add(new LWRDataProvider());
				}
				fuelAssemblies.add(fuelAssembly);
			}

			// Read in the data file one feature at a time.
			String feature;
			while ((feature = nextLine(reader)) != null) {
				if (supportedFeatures.containsKey(feature)) {
					readFeature(reader, feature, supportedFeatures.get(feature));
				}
				// If the line does not match any feature, this should just read
				// the next line until a feature is read.
			}

			// Close the reader.
			reader.close();
		} catch (IOException e) {
			// Handle IOExceptions.
			System.err.println("IOException while reading " + fileName
					+ ". Cannot read data.");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// Handle NumberFormatExceptions. These are thrown when dataRowRegex
			// is not matched.
			System.err.println("NumberFormatException while reading "
					+ fileName + ". Invalid number.");
			e.printStackTrace();
		}
		;
	}

	/**
	 * Get the number of rows in each assembly.
	 * 
	 * @return The number of rows in each assembly.
	 */
	public int getAssemblyRows() {
		return assemblyRows;
	}

	/**
	 * Get the number of columns in each assembly.
	 * 
	 * @return The number of columns in each assembly.
	 */
	public int getAssemblyColumns() {
		return assemblyCols;
	}

	/**
	 * Get the number of axial levels used for the assembly data.
	 * 
	 * @return The number of axial levels used for the assembly data.
	 */
	public int getAxialLevels() {
		return axialLevels;
	}

	/**
	 * Get the number of assemblies read in from the provided data.
	 * 
	 * @return Returns the number of assemblies read in or 0 if no data has been
	 *         read in.
	 */
	public int getNumberOfAssemblies() {
		int count = 0;
		if (fuelAssemblies != null) {
			count = fuelAssemblies.size();
		}
		return count;
	}

	/**
	 * Get an array of LWRDataProviders for a particular fuel assembly. Each
	 * LWRDataProvider provides data for a single rod and multiple features.
	 * 
	 * @param assembly
	 *            The index of the assembly.
	 * @return An ArrayList of LWRDataProviders, or null if assembly is an
	 *         invalid index or the data has not been read in.
	 */
	public List<LWRDataProvider> getAssemblyDataProviders(int assembly) {
		List<LWRDataProvider> fuelAssembly = null;
		if ((fuelAssemblies != null) && (assembly >= 0)
				&& (assembly < fuelAssemblies.size())) {
			fuelAssembly = fuelAssemblies.get(assembly);
		}
		return fuelAssembly;
	}

	/**
	 * This function gets the next non-empty line from a BufferedReader. Empty
	 * lines may include whitespace.
	 * 
	 * @param reader
	 *            The BufferedReader from which to read.
	 * @return The next non-empty line or null (if reader.readLine() == null).
	 * @throws IOException
	 *             IOExceptions from reading need to be handled by the caller.
	 */
	private String nextLine(BufferedReader reader) throws IOException {
		String line = null;
		while ((line = reader.readLine()) != null) {
			// If the line is not empty, break from the loop.
			if (!(line.trim().isEmpty())) {
				break;
			}
		}
		return line;
	}

	/**
	 * Gets the next non-empty line of text from the reader if it matches it
	 * with a regular expression.
	 * 
	 * @param reader
	 *            The BufferedReader from which to read.
	 * @param expected
	 *            The regular expression to match against.
	 * @return Returns the next line from the reader if it matches with the
	 *         regular expression.
	 * @throws IOException
	 *             Throws an IOException either from reading or if the pattern
	 *             is not matched.
	 */
	private String nextLine(BufferedReader reader, String expected)
			throws IOException {
		String line = null;
		while ((line = reader.readLine()) != null) {
			// If the line is not empty, break from the loop.
			if (!(line.trim().isEmpty())) {
				break;
			}
		}
		// Compare the line against the regular expression.
		if (!line.matches(expected)) {
			System.err.println("Expected string of form \"" + expected
					+ "\" but got \"" + line + "\"");
			throw new IOException();
		}
		return line;
	}

	/**
	 * Reads in a feature from the text file. This means grabbing data for each
	 * rod in each assembly at all possible axial levels.
	 * 
	 * @param reader
	 *            The BufferedReader used to read the file.
	 * @param feature
	 *            The feature to read in.
	 * @param useAxialLevels
	 *            Whether or not to look for axial levels in the data.
	 * @throws IOException
	 *             The caller needs to handle reading IO exceptions.
	 * @throws NumberFormatException
	 *             The caller needs to handle invalid doubles.
	 */
	private void readFeature(BufferedReader reader, String feature,
			boolean useAxialLevels) throws IOException, NumberFormatException {

		// Try to read in the feature data for each fuel assembly.
		for (int assembly = 0; assembly < fuelAssemblies.size(); assembly++) {
			// Expected "Assembly #"
			String line = nextLine(reader, "^Assembly\\s" + assembly + "\\s*$");

			// Get the current fuel assembly.
			List<LWRDataProvider> fuelAssembly = fuelAssemblies.get(assembly);

			// See if we need to iterate over axial levels.
			int iterations = 1;
			if (useAxialLevels) {
				iterations = axialLevels;
			}

			// Loop over the axial levels (if applicable).
			for (int i = 0; i < iterations; i++) {
				// Expected "Axial Level #" (if axial levels are used with this
				// feature).
				if (useAxialLevels) {
					line = nextLine(reader, "^Axial Level\\s" + i + "\\s*$");
				}
				// Loop over the rows of the assembly data. Each line at this
				// point should be lines of doubles matching the dataRowRegex.
				for (int row = 0; row < assemblyRows; row++) {
					line = nextLine(reader, dataRowRegex);
					// Expected assemblyCols numbers.
					String[] splitLine = line.trim().split("\\s+");

					// Loop over each value in the row.
					for (int col = 0; col < assemblyCols; col++) {
						// Store the feature/value pair in the data provider.
						LWRData data = new LWRData(feature);
						data.setValue(Double.parseDouble(splitLine[col]));
						fuelAssembly.get(row * assemblyCols + col).addData(
								data, 0);
					}
				}
			}
		}
	}

}
