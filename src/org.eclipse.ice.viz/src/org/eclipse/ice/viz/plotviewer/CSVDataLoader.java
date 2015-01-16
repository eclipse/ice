/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.viz.plotviewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.ice.analysistool.IData;

/**
 * CSVDataLoader instantiates a CSVDataProvider and returns it
 * 
 * @author Claire Saunders, Anna Wojtowicz
 * 
 */
public class CSVDataLoader {

	/**
	 * ArrayList of Double to hold times
	 */
	private ArrayList<Double> times;

	/**
	 * String for the time-Units
	 */
	private String timeUnits;

	/**
	 * File for the csvInputFile
	 */
	private File csvInputFile;

	/**
	 * String for the input file name
	 */
	private String csvInputString;

	/**
	 * ArrayList of Strings for the csvInputString
	 */
	private String[] csvInputStringList;

	/**
	 * Boolean marker for ValueComp method
	 */
	private boolean boolVarComp;

	/**
	 * Default CSVDataLoader constructor
	 */
	public CSVDataLoader() {
		times = new ArrayList<Double>();
		timeUnits = null;
		boolVarComp = true;
	}

	/**
	 * Constructor for File
	 * 
	 * @param csvFileName
	 */
	public CSVDataLoader(File csvFileName) {
		times = new ArrayList<Double>();
		timeUnits = null;
		boolVarComp = true;
		this.setCSVInputFile(csvFileName);
	}

	/**
	 * Constructor for Array of Strings
	 * 
	 * @param csvInputStringList
	 */
	public CSVDataLoader(String[] csvInputStringList) {
		times = new ArrayList<Double>();
		timeUnits = null;
		boolVarComp = true;
		this.setCSVInputStringList(csvInputStringList);
	}

	/**
	 * Constructor of the String, sets String and Filename
	 * 
	 * @param csvFileString
	 */
	public CSVDataLoader(String csvFileString) {
		times = new ArrayList<Double>();
		timeUnits = null;
		boolVarComp = true;
		this.setCSVInputString(csvFileString);
		this.setCSVInputFile(new File(csvFileString));
	}

	/**
	 * This method loads a CSV input file and returns the contents as a
	 * CSVDataProvider object.
	 * 
	 * @param csvInputFile
	 *            The CSV input file to load
	 * @return The contents of the CSV file as a CSVDataProvider object
	 * @throws Exception
	 */
	public CSVDataProvider load(File csvInputFile) {
		// Local Declarations
		CSVDataProvider dataSet = new CSVDataProvider();
		ArrayList<String> features = new ArrayList<String>();
		ArrayList<String> units = new ArrayList<String>();
		HashMap<Integer, Integer> featureErrorIndices = new HashMap<Integer, Integer>();
		BufferedReader inputStream = null;
		String line;
		String[] commentLine;
		String[] featureLine;
		int commentLineLength;
		int featureLineLength = 0;
		int lineNumber = 1;
		boolean hasHashFeature = false;
		int elementOffset;
		
		// Reading in the data file line by line and passing to the provider
		try {
			
			// Create a BufferedReader for reading the file
			inputStream = new BufferedReader(new FileReader(csvInputFile));

			// Begin reading the file. Find the line which contains the list
			// of features, denoted either by the "#somefeature"-style label 
			// format, or just use line 1 if the hash-format is not used
			while ((line = inputStream.readLine()) != null
					&& (line.contains("#") || lineNumber == 1)) {
				
				// Replace characters if we can find a match to the 
				// "#label:stuff", "#label;stuff" or "#label/stuff" formats
				// (not whitespace sensitive)
				if (line.matches("#\\s*\\w+\\s*([:;/]).+")) {
							
					// Replace all special delimiters (":", ";", "/") with
					// commas
					line = line.replaceAll(":", ",");
					line = line.replaceAll(";", ",");
					line = line.replaceAll("/", ",");
				}
				
				// Split the line at each comma
				commentLine = line.trim().split(",");
				commentLineLength = commentLine.length;
				for (int i = 0; i < commentLineLength; i++) {
					commentLine[i] = commentLine[i].trim();
				}

				// Check if the line contains the "#feature" tag
				hasHashFeature = line.toLowerCase().contains("#features");

				// If this line contains all the feature names (either with the
				// "#feature" format (or simply by being line 1 if it contains
				// no hashes), add them to the features ArrayList
				if (hasHashFeature || 
						(!line.contains("#") && lineNumber == 1)) {
					
					// First, get the number of features. This is used later
					// to check each line contains the same number of entries
					// as there are number of features
					featureLineLength = commentLine.length;
					
					// We'll also check if there are error/uncertainty provided
					// for each feature. Set up regex matcher.					
					String pattern = "(.*)_(error|uncertainty)";
					Pattern errorPattern = Pattern.compile(pattern);
					Matcher match = null;
					
					// Now loop through the split line, add features to the
					// list of features, and look for any error/uncertainty 
					// matches
					elementOffset = (hasHashFeature ? 1 : 0);
					for (int i = elementOffset; i < commentLineLength; i++) {
						
						// Add the current element of commentLine to the
						// ArrayList of features
						features.add(commentLine[i]);
						
						// Try to find any error/uncertainty match
						match = errorPattern.matcher(commentLine[i]);
						if (match.find()) {

							 // Add the feature and it's corresponding error to
							 // the feature error hashmap.
							featureErrorIndices.put(
									features.indexOf(match.group(1)), i
											- elementOffset);
						}
					}
				} else if (line.toLowerCase().contains("#units")) {
					
					// Loops through the split line and appends to the 
					// ArrayList of units
					boolVarComp = ValueComp(featureLineLength,
							commentLineLength);
					if (boolVarComp) {
						for (int i = 1; i < commentLineLength; i++) {
							units.add(commentLine[i]);
						}
					} else {
						System.out.println("Number of units and "
								+ "features do not match.");
					}
					
				} else if (line.toLowerCase().contains("#time-units")) {
					
					// Set the time units
					timeUnits = commentLine[1];
					dataSet.setTimeUnits(timeUnits);
					
				} else if (line.toLowerCase().contains("#matrix")) {
					
					// Splits the line by comma
					String[] matrixData = line.split(",");
					// get the data width
					int dataWidth = Integer.parseInt(matrixData[1]);
					// get the data height
					int dataHeight = Integer.parseInt(matrixData[2]);
					// set the data width in the provider
					dataSet.setDataWidth(dataWidth);
					// set the data height in the provider
					dataSet.setDataHeight(dataHeight);
					
				} 				
				
				// Increment the line counter
				lineNumber++;
			}
			
			/**
			 * If the file had no given features, create a set of features
			 * x0,x1,x2,...,xn for the fakeDataSet
			 */
			if (features.isEmpty()) {

				// Split the line at each comma
				featureLine = line.trim().split(",");

				// Create as many dummy feature names as there were splits
				for (int i = 0; i < featureLine.length; i++) {
					features.add("x" + i);
				}
			}
			
			// Create an IData object to store information
			IData data;
			String[] dataLines;
			dataLines = line.trim().split(",");
			// Set the minimum and maximum as the first value
			dataSet.setDataMax(Double.parseDouble(dataLines[0]));
			dataSet.setDataMin(Double.parseDouble(dataLines[0]));
			/**
			 * Loop handles the columns of the first line of data
			 */
			for (int i = 0; i < dataLines.length; i++) {
				/**
				 * Check if error/uncertainties are present in the data file
				 */
				if (!(featureErrorIndices.isEmpty())) {
					// Checks if the column is not an error value
					if (!featureErrorIndices.containsValue(i)) {
						/**
						 * Instantiate a new CSVData object
						 */
						data = new CSVData(features.get(i),
								Double.parseDouble(dataLines[i]));
						/**
						 * Set the uncertainty using the HashMap by finding the
						 * column of error associated with this data
						 */
						if (featureErrorIndices.containsKey(i)) {
							((CSVData) data).setUncertainty(Double
									.parseDouble(dataLines[featureErrorIndices
											.get(i)]));
						}
						/**
						 * Set the units if the units exist
						 */
						if (!(units.isEmpty())) {
							((CSVData) data).setUnits(units.get(i));
						}
						/**
						 * Add the data with a time if the times exist,otherwise
						 * add without time
						 */
						if (!(times.isEmpty())) {
							dataSet.addData(times.get(i), data);
						} else {
							dataSet.addData(data);
						}
					}
				} else {
					/**
					 * Instantiate a new CSVData object
					 */
					data = new CSVData(features.get(i),
							Double.parseDouble(dataLines[i]));
					/**
					 * Set the units if the units exist
					 */
					if (!(units.isEmpty())) {
						((CSVData) data).setUnits(units.get(i));
					}
					/**
					 * Add the data with a time if the times exist, otherwise
					 * add with out time
					 */
					if (!(times.isEmpty())) {
						dataSet.addData(times.get(i), data);
					} else {
						dataSet.addData(data);
					}
				}
				// Finding the minimum and maximum for the contour plot
				if (dataSet.getDataMin() > Double.parseDouble(dataLines[i])) {
					dataSet.setDataMin(Double.parseDouble(dataLines[i]));
				}
				if (dataSet.getDataMax() < Double.parseDouble(dataLines[i])) {
					dataSet.setDataMax(Double.parseDouble(dataLines[i]));
				}
			}

			/**
			 * While loop to read the rest of the data lines
			 */
			while ((line = inputStream.readLine()) != null && !line.isEmpty()
					&& !line.startsWith("#")) {
				/**
				 * dataLines- for the split input line
				 */
				dataLines = line.trim().split(",");

				/**
				 * Loop through each data in the line
				 */
				for (int i = 0; i < dataLines.length; i++) {
					/**
					 * Check if error/uncertainties are present in the data file
					 */
					if (!(featureErrorIndices.isEmpty())) {
						if (!featureErrorIndices.containsValue(i)) {
							/**
							 * Instantiate a new CSVData object
							 */
							data = new CSVData(features.get(i),
									Double.parseDouble(dataLines[i]));

							/**
							 * Set the uncertainty using the hashmap
							 */
							if (featureErrorIndices.containsKey(i)) {
								((CSVData) data)
										.setUncertainty(Double
												.parseDouble(dataLines[featureErrorIndices
														.get(i)]));
							}
							/**
							 * Set the units if the units exist
							 */
							if (!(units.isEmpty())) {
								((CSVData) data).setUnits(units.get(i));
							}
							/**
							 * Add the data with a time if the times exist,
							 * otherwise add without time
							 */
							if (!(times.isEmpty())) {
								dataSet.addData(times.get(i), data);
							} else {
								dataSet.addData(data);
							}
						}
					} else {
						/**
						 * Instantiate a new CSVData object
						 */
						data = new CSVData(features.get(i),
								Double.parseDouble(dataLines[i]));
						/**
						 * Set the units if the units exist
						 */
						if (!(units.isEmpty())) {
							((CSVData) data).setUnits(units.get(i));
						}
						/**
						 * Add the data with a time if the times exist,
						 * otherwise add with out time
						 */
						if (!(times.isEmpty())) {
							dataSet.addData(times.get(i), data);
						} else {
							dataSet.addData(data);
						}
					}
					// Finding the minimum and maximum for the contour plot
					if (dataSet.getDataMin() > Double.parseDouble(dataLines[i])) {
						dataSet.setDataMin(Double.parseDouble(dataLines[i]));
					}
					if (dataSet.getDataMax() < Double.parseDouble(dataLines[i])) {
						dataSet.setDataMax(Double.parseDouble(dataLines[i]));
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("filIn: " + e.fillInStackTrace());
			System.out.println("cause: " + e.getCause());
			System.out.println("local: " + e.getLocalizedMessage());
			System.out.println("messa: " + e.getMessage());
			System.out.println("trace: " + e.getStackTrace());
			System.out.print("trace: ");
			e.printStackTrace();
			System.out.println();
			System.out.print("string: ");
			e.toString();
			System.out.println();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("filIn: " + e.fillInStackTrace());
			System.out.println("cause: " + e.getCause());
			System.out.println("local: " + e.getLocalizedMessage());
			System.out.println("messa: " + e.getMessage());
			System.out.println("trace: " + e.getStackTrace());
			System.out.print("trace: ");
			e.printStackTrace();
			System.out.println();
			System.out.print("string: ");
			e.toString();
			System.out.println();
		} finally {
			/**
			 * Check if the stream is null to catch IO error Close stream
			 */
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error: Could not close stream");
				}
			}
		}
		return dataSet;
	}

	/**
	 * Checks the number of features
	 * 
	 * @param featureSize
	 * @param size
	 * @return
	 */
	public boolean ValueComp(int featureSize, int size) {
		/**
		 * Checks if the number of features matches for the number of variables
		 * and that the size is not zero
		 */
		if (featureSize == size && featureSize != 0) {
			/**
			 * Returns true if the sizes match
			 */
			boolVarComp = true;
			return boolVarComp;
		} else {
			boolVarComp = false;
			return boolVarComp;
		}

	}

	/**
	 * Loads csvInputStringList via the CSVDataProvider
	 * 
	 * @return
	 */
	public CSVDataProvider loadFileSet() {
		if (csvInputStringList != null) {
			try {
				return loadAsFileSet(csvInputStringList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return null;
	}

	/**
	 * Loads a file set, where it file is a separate time.
	 * 
	 * @param csvFileSet
	 * @return
	 */
	public CSVDataProvider loadAsFileSet(String[] csvFileSet) {
		/**
		 * Instantiate the dataSet
		 */
		CSVDataProvider dataSet = new CSVDataProvider();

		// Sort the array and set it
		Arrays.sort(csvFileSet);
		this.setCSVInputStringList(csvFileSet);

		// The regex pattern for a csv file with a time for each file
		String filePattern = "(.*)_(.*)\\.csv";
		Pattern csvFileSetPattern = Pattern.compile(filePattern);
		// Loop through each file name
		for (int i = 0; i < csvFileSet.length; i++) {
			// Match the regex to the file name
			Matcher match = csvFileSetPattern.matcher(csvFileSet[i]);
			if (match.find()) {
				// If filename matches the scheme, get the second part, replace
				// any non-numerics and non-periods
				String newTime = match.group(2).replaceAll("[^\\d.]", "");
				// Parse as a double and add it to the times
				times.add(Double.parseDouble(newTime));
			} else {
				// Otherwise, use i as the time
				times.add((double) i);
			}
		}

		/**
		 * File Reader
		 */
		BufferedReader inputStream = null;
		// Loop to read through all the files
		for (int fileIndex = 0; fileIndex < csvFileSet.length; fileIndex++) {
			try {
				inputStream = new BufferedReader(new FileReader(
						csvFileSet[fileIndex]));

				// ArrayList of strings to hold each feature for the file
				ArrayList<String> features = new ArrayList<String>();
				// ArrayList of strings to hold each unit for the feature in the
				// file
				ArrayList<String> units = new ArrayList<String>();
				// Hashmap for feature and associated error indicies:
				// <featureIndex, errorIndex>
				HashMap<Integer, Integer> featureErrorIndices = new HashMap<Integer, Integer>();

				String line;
				String[] commentLine;
				String[] valuesLine;
				int valuesLineLength = 0;
				/**
				 * While loop that parses the comments for the features and
				 * units
				 */
				while ((line = inputStream.readLine()) != null
						&& ("#").equals(line.substring(0, 1))) {

					/**
					 * Check for the case where the line contains ":",";","/"
					 * For example, #features: or #features; or #features/
					 */
					if (line.toLowerCase().contains(":")
							|| line.toLowerCase().contains(";")
							|| line.toLowerCase().contains("/")) {
						/**
						 * If the line does contain ":", ";","/" replace with a
						 * comma and split the line
						 */
						line = line.replaceAll(":", ",");
						line = line.replaceAll(";", ",");
						line = line.replaceAll("/", ",");
					}
					/**
					 * Split the line
					 */
					commentLine = line.trim().split(",");
					int commentLineLength = commentLine.length;
					for (int i = 0; i < commentLineLength; i++) {
						commentLine[i] = commentLine[i].trim();
					}

					/**
					 * Checks for the features, units, times, and time-units
					 * keywords
					 */
					if (line.toLowerCase().contains("#features,")) {
						/**
						 * Initialize the pattern for the error and uncertainty
						 * of the features
						 */
						String pattern = "(.*)_(error|uncertainty)";
						Pattern errorPattern = Pattern.compile(pattern);
						/**
						 * Loops through the split line and appends to ArrayList
						 * of features. If error/uncertainties exist in the
						 * file, they are added to the hashmap
						 */
						for (int i = 1; i < commentLineLength; i++) {
							/**
							 * Match the pattern for the error|uncertainty
							 */
							Matcher match = errorPattern
									.matcher(commentLine[i]);
							if (match.find()) {
								/**
								 * Add the feature and it's corresponding error
								 * index to the hashmap.
								 */
								featureErrorIndices
										.put(features.indexOf(match.group(1)),
												i - 1);
							}
							/**
							 * Add array commentLine to features
							 */
							valuesLineLength = commentLine.length;
							features.add(commentLine[i]);
						}
					} else if (line.toLowerCase().contains("#units,")) {
						/**
						 * Loops through the split line and appends the
						 * ArrayList of units
						 */
						boolVarComp = ValueComp(valuesLineLength,
								commentLineLength);
						if (boolVarComp) {
							for (int i = 1; i < commentLineLength; i++) {
								units.add(commentLine[i]);
							}
						} else {
							System.out
									.println("Number of units and features do not match.");
						}
					} else if (line.toLowerCase().contains("#time-units,")) {
						/**
						 * Sets the timeUnits
						 */
						timeUnits = commentLine[1];
						dataSet.setTimeUnits(timeUnits);
					}
				}

				/**
				 * The split the first data line
				 */
				valuesLine = line.trim().split(",");
				/**
				 * If the file had no given features, create a set of features
				 * x0,x1,x2,...,xn for the fakeDataSet
				 */
				if (features.isEmpty()) {
					valuesLineLength = valuesLine.length;
					for (int i = 0; i < valuesLineLength; i++) {
						features.add("x" + i);
					}
				}

				/**
				 * Declare an IData object to store the data
				 */
				IData data;
				String[] dataLines;
				dataLines = line.trim().split(",");

				/**
				 * Loop handles the first line of data
				 */
				for (int i = 0; i < dataLines.length; i++) {
					/**
					 * Check if error/uncertainties are present in the data file
					 */
					if (!(featureErrorIndices.isEmpty())) {
						if (!featureErrorIndices.containsValue(i)) {
							/**
							 * Instantiate a new CSVData object
							 */
							data = new CSVData(features.get(i),
									Double.parseDouble(dataLines[i]));
							/**
							 * Set the uncertainty using the HashMap
							 */
							if (featureErrorIndices.containsKey(i)) {
								((CSVData) data)
										.setUncertainty(Double
												.parseDouble(dataLines[featureErrorIndices
														.get(i)]));
							}
							/**
							 * Set the units if the units exist
							 */
							if (!(units.isEmpty())) {
								((CSVData) data).setUnits(units.get(i));
							}
							/**
							 * Add the data with a time if the times
							 * exist,otherwise add with out time
							 */
							if (!(times.isEmpty())) {
								dataSet.addData(times.get(fileIndex), data);
							} else {
								dataSet.addData(data);
							}
						}
					} else {
						/**
						 * Instantiate a new CSVData object
						 */
						data = new CSVData(features.get(i),
								Double.parseDouble(dataLines[i]));
						/**
						 * Set the units if the units exist
						 */
						if (!(units.isEmpty())) {
							((CSVData) data).setUnits(units.get(i));
						}
						/**
						 * Add the data with a time if the times exist,
						 * otherwise add with out time
						 */
						if (!(times.isEmpty())) {
							dataSet.addData(times.get(fileIndex), data);
						} else {
							dataSet.addData(data);
						}
					}
				}
				/**
				 * While loop to read the rest of the data lines
				 */
				while ((line = inputStream.readLine()) != null
						&& !("#").equals(line.substring(0, 1))) {
					/**
					 * dataLines- for the split input line
					 */
					dataLines = line.trim().split(",");

					/**
					 * Loop through each data in the line
					 */
					for (int i = 0; i < dataLines.length; i++) {
						/**
						 * Check if error/uncertainties are present in the data
						 * file
						 */
						if (!(featureErrorIndices.isEmpty())) {
							if (!featureErrorIndices.containsValue(i)) {
								/**
								 * Instantiate a new CSVData object
								 */
								data = new CSVData(features.get(i),
										Double.parseDouble(dataLines[i]));
								/**
								 * Set the uncertainty using the hashmap
								 */
								if (featureErrorIndices.containsKey(i)) {
									((CSVData) data)
											.setUncertainty(Double
													.parseDouble(dataLines[featureErrorIndices
															.get(i)]));
								}
								/**
								 * Set the units if the units exist
								 */
								if (!(units.isEmpty())) {
									((CSVData) data).setUnits(units.get(i));
								}
								/**
								 * Add the data with a time if the times exist,
								 * otherwise add without time
								 */
								if (!(times.isEmpty())) {
									dataSet.addData(times.get(fileIndex), data);
								} else {
									dataSet.addData(data);
								}
							}
						} else {
							/**
							 * Instantiate a new CSVData object
							 */
							data = new CSVData(features.get(i),
									Double.parseDouble(dataLines[i]));
							/**
							 * Set the units if the units exist
							 */
							if (!(units.isEmpty())) {
								((CSVData) data).setUnits(units.get(i));
							}
							/**
							 * Add the data with a time if the times exist,
							 * otherwise add with out time
							 */
							if (!(times.isEmpty())) {
								dataSet.addData(times.get(fileIndex), data);
							} else {
								dataSet.addData(data);
							}
						}
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("filIn: " + e.fillInStackTrace());
				System.out.println("cause: " + e.getCause());
				System.out.println("local: " + e.getLocalizedMessage());
				System.out.println("messa: " + e.getMessage());
				System.out.println("trace: " + e.getStackTrace());
				System.out.print("trace: ");
				e.printStackTrace();
				System.out.println();
				System.out.print("string: ");
				e.toString();
				System.out.println();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("filIn: " + e.fillInStackTrace());
				System.out.println("cause: " + e.getCause());
				System.out.println("local: " + e.getLocalizedMessage());
				System.out.println("messa: " + e.getMessage());
				System.out.println("trace: " + e.getStackTrace());
				System.out.print("trace: ");
				e.printStackTrace();
				System.out.println();
				System.out.print("string: ");
				e.toString();
				System.out.println();
			} finally {
				/**
				 * Check if the stream is null to catch IO error Close stream
				 */
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("Error: Could not close stream");
					}
				}
			}
		}

		return dataSet;
	}

	/**
	 * Loads the FileName via the CSVDataProvider
	 * 
	 * @param csvFileName
	 * @return
	 */
	public CSVDataProvider load(String csvFileName) {
		/**
		 * Invocation of setCSVInputString(csvFileName) and setCSVInputFile(new
		 * File(csvFileName)). Returns the load method.
		 */
		this.setCSVInputString(csvFileName);
		this.setCSVInputFile(new File(csvFileName));
		return load();
	}

	/**
	 * Loads csvInputFile via the CSVDataProvider
	 * 
	 * @return
	 */
	public CSVDataProvider load() {
		/**
		 * Checks that the InputFile is not null before it returns load
		 */
		if (this.csvInputFile != null) {
			try {
				return load(csvInputFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return null;
	}

	/**
	 * Sets the ArrayList csvInputStringList
	 * 
	 * @param csvInputStringList
	 */
	public void setCSVInputStringList(String[] csvInputStringList) {
		if (csvInputStringList != null) {
			this.csvInputStringList = csvInputStringList;
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * Sets the String csvInputString
	 * 
	 * @param csvInputString
	 */
	public void setCSVInputString(String csvInputString) {
		/**
		 * Checks if the string input is null. If !null, set the string. If
		 * null, throw NullPointerException.
		 */
		if (csvInputString != null) {
			this.csvInputString = csvInputString;
			this.setCSVInputFile(new File(csvInputString));
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * Sets the File csvInputFile
	 * 
	 * @param csvInputFile
	 */
	public void setCSVInputFile(File csvInputFile) {
		/**
		 * Checks if the file is null. If !null, set the file. If null, throw
		 * NullPointerException.
		 */
		if (csvInputFile != null) {
			this.csvInputFile = csvInputFile;
		} else {
			throw new NullPointerException();
		}

	}

	/**
	 * Gets the InputString
	 * 
	 * @return
	 */
	public String getCSVInputString() {
		return csvInputString;
	}

	/**
	 * Gets the InputFile
	 * 
	 * @return
	 */
	public File getCSVInputFile() {
		return csvInputFile;
	}

	/**
	 * Gets the CSVInputStringList
	 * 
	 * @return
	 */
	public String[] getCSVInputStringMult() {
		return csvInputStringList;
	}

}
