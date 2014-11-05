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
package org.eclipse.ice.client.widgets.reactoreditor.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.client.widgets.reactoreditor.AnalysisDataReader;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.ice.reactor.LWRDataProvider;
import org.junit.Test;

/**
 * This class tests the AnalysisDataReader.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class AnalysisDataReaderTester {

	/**
	 * This test case reads in some example data and manually verifies the data
	 * read in by the AnalysisDataReader.
	 */
	@Test
	public void readExampleData() {
		// Initialize a reader to test.
		AnalysisDataReader dataReader = new AnalysisDataReader();

		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "reactorEditorData";
		File dataFile = new File(userDir + separator + "TestAnalysisData.txt");
		URI uri = dataFile.toURI();
		dataReader.readData(uri);

		// I would do this in a separate function, but... test isolation.
		// Validate the data read in by the reader.
		int nAssemblies = dataReader.getNumberOfAssemblies();
		assertEquals(2, nAssemblies);

		int rows = dataReader.getAssemblyRows();
		assertEquals(4, rows);
		int cols = dataReader.getAssemblyColumns();
		assertEquals(5, cols);
		int axialLevels = dataReader.getAxialLevels();
		assertEquals(3, axialLevels);

		// We need to check the values across each location in each assembly.
		for (int assembly = 0; assembly < nAssemblies; assembly++) {
			ArrayList<LWRDataProvider> assemblyData = (ArrayList<LWRDataProvider>) dataReader
					.getAssemblyDataProviders(assembly);
			assertEquals(rows * cols, assemblyData.size());

			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					LWRDataProvider dataProvider = assemblyData.get(row * cols
							+ col);
					IData datum;

					ArrayList<IData> data = dataProvider
							.getDataAtCurrentTime("Fuel Pin Difference");
					for (int axialLevel = 0; axialLevel < axialLevels; axialLevel++) {
						datum = data.get(axialLevel);
						assertEquals(assembly + axialLevel,
								(int) datum.getValue());
					}
					data = dataProvider
							.getDataAtCurrentTime("Fuel Pin Powers Uncertainties");
					for (int axialLevel = 0; axialLevel < axialLevels; axialLevel++) {
						datum = data.get(axialLevel);
						double value = datum.getValue();
						if ((assembly == 1) && (axialLevel == 2)) {
							value *= -1;
						}
						assertEquals(assembly + axialLevel, (int) value);
					}
					data = dataProvider.getDataAtCurrentTime("Radial Power");
					datum = data.get(0);
					assertEquals(assembly, (int) datum.getValue());
					data = dataProvider
							.getDataAtCurrentTime("Radial Power Difference");
					datum = data.get(0);
					assertEquals(assembly, (int) datum.getValue());
				}
			}
		}

		return;
	}
}