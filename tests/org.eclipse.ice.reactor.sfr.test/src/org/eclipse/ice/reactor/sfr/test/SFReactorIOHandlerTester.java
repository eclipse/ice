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
package org.eclipse.ice.reactor.sfr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;

import org.eclipse.ice.reactor.sfr.base.SFReactorIOHandler;
import org.eclipse.ice.reactor.sfr.core.SFReactor;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <p>
 * Tests the operations of the SFReactorIOHandler class.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class SFReactorIOHandlerTester {

	// FIXME We need to update the SFReactorIOHandler in the C++ library so that
	// it too writes and reads null-terminated strings correctly from the h5
	// files. See bug #180
	@Ignore
	@Test
	public void checkRead() {

		// Constants for this test.
		int reactorSize = 0; // Use the default.
		int assemblySize = 0; // Use the default.
		int nAssemblies = 7;
		int nAssemblyComponents = 5;
		int nAxialLevels = 3;
		boolean randomData = false; // Use fixed data and properties.
		boolean randomLocations = false; // Use fixed component locations.

		// Input/Output files for this test.
		// On the Java side, this uses a previously generated file from the C++
		// side. If we make changes to the way h5 files are written, we will
		// need to generate a new file and commit it to the repo.
		String s = System.getProperty("file.separator");
		String directory = System.getProperty("user.dir") + s
				+ "ICEIOTestDirectory" + s;
		String testInputPath = directory + "fromCPP.h5";
		// String testOutputPath = directory + "fromJava.h5";

		// Create a factory for generating populated reactors.
		SFReactorFactory factory = new SFReactorFactory();

		// Create an IO handler for writing/reading the reactor.
		SFReactorIOHandler handler = new SFReactorIOHandler();

		// Get a URI from the input path.
		File inFile = new File(testInputPath);
		URI uri = inFile.toURI();

		// Declare the reactors. One will be generated; the other will be read.
		SFReactor reactor;
		SFReactor loadedReactor;

		// Time the test.
		long start = System.nanoTime();
		long testStart = start;

		// Generate a reactor with no random info.
		reactor = factory.generatePopulatedFullCoreReactor(reactorSize,
				assemblySize, nAssemblies, nAssemblyComponents, nAxialLevels,
				0, randomData, randomLocations);
		reactor.setName("Arcturus");

		long end = System.nanoTime();
		System.out
				.println("SFReactorIOHandlerTester message: Random reactor generated for read test.");
		System.out.println("Time elapsed (s): " + (end - start) / 1000000000.0);

		start = System.nanoTime();

		// Read the other reactor from the input file.
		loadedReactor = handler.readHDF5(uri);

		end = System.nanoTime();
		System.out
				.println("SFReactorIOHandlerTester message: Reading completed for read test.");
		System.out.println("Time elapsed (s): " + (end - start) / 1000000000.0);

		start = System.nanoTime();

		// The reactors should be equivalent.
		assertEquals(reactor, loadedReactor);

		end = System.nanoTime();
		System.out
				.println("SFReactorIOHandlerTester message: Comparison completed for read test.");
		System.out.println("Time elapsed (s): " + (end - start) / 1000000000.0);

		// /* ---- Write the reactor (for manual testing, only). ---- */
		// // Uncomment this if you would like to compare the h5 files manually.
		//
		// // Get a URI from the output path.
		// File outFile = new File(testOutputPath);
		// uri = outFile.toURI();
		//
		// start = System.nanoTime();
		//
		// // Write the output file from the loaded reactor.
		// handler.writeHDF5(uri, loadedReactor);
		//
		// end = System.nanoTime();
		//
		// System.out.println("SFReactorIOHandlerTester message: Writing completed for read test. You may now compare the h5 files.");
		// System.out.println("Time elapsed (s): " + (end - start) /
		// 1000000000.0);
		// /* ------------------------------------------------------- */

		System.out.println("Total Time elapsed (s): " + (end - testStart)
				/ 1000000000.0);

		return;
	}

	@Test
	public void checkWriteAndRead() {

		// Constants for this test.
		int reactorSize = 0; // Use the default.
		int assemblySize = 0; // Use the default.
		int nAssemblies = 5;
		int nAssemblyComponents = 10;
		int nAxialLevels = 15;
		boolean randomData = true; // Randomize the data and properties.
		boolean randomLocations = true; // Randomize the component locations.

		// Input/Output files for this test.
		String s = System.getProperty("file.separator");
		String directory = System.getProperty("user.dir") + s
				+ "ICEIOTestDirectory" + s;
		String path = directory + "fromJava.h5";

		// Create a factory for generating populated reactors.
		SFReactorFactory factory = new SFReactorFactory();

		// Create an IO handler for writing/reading the reactor.
		SFReactorIOHandler handler = new SFReactorIOHandler();

		// Create a URI for a file to write/read for the test.
		URI uri = new File(path).toURI();

		// Set up some seeds.
		// int[] seeds = new int[] { 0, 3, 42, 1337, (int) System.nanoTime() };
		int[] seeds = new int[] { 1337 };

		// Run the test for each seed.
		for (int seed : seeds) {

			// Generate a reactor, an identical but separate copy of it, and a
			// reactor to read written data into.
			SFReactor reactor;
			SFReactor backupReactor;
			SFReactor loadedReactor = null;

			// Time the test.
			long start = System.nanoTime();
			long testStart = start;

			// Construct a reactor based on the seed.
			reactor = factory.generatePopulatedFullCoreReactor(reactorSize,
					assemblySize, nAssemblies, nAssemblyComponents,
					nAxialLevels, seed, randomData, randomLocations);
			backupReactor = factory.generatePopulatedFullCoreReactor(
					reactorSize, assemblySize, nAssemblies,
					nAssemblyComponents, nAxialLevels, seed, randomData,
					randomLocations);

			reactor.setName("Arcturus");
			backupReactor.setName("Arcturus");

			long end = System.nanoTime();
			System.out
					.println("SFReactorIOHandlerTester message: Random reactor generated for seed "
							+ seed);
			System.out.println("Time elapsed (s): " + (end - start)
					/ 1000000000.0);

			// Make sure the reactor and backup reactor are completely the same.
			assertFalse(reactor == backupReactor);
			assertEquals(reactor, backupReactor);

			start = System.nanoTime();

			// Perform the write operation.
			handler.writeHDF5(uri, reactor);

			end = System.nanoTime();
			System.out
					.println("SFReactorIOHandlerTester message: Writing completed for seed "
							+ seed);
			System.out.println("Time elapsed (s): " + (end - start)
					/ 1000000000.0);

			// Make sure the reactor was not modified in any way.
			assertFalse(reactor == backupReactor);
			assertEquals(reactor, backupReactor);

			start = System.nanoTime();

			// Perform the read operation.
			loadedReactor = handler.readHDF5(uri);

			end = System.nanoTime();
			System.out
					.println("SFReactorIOHandlerTester message: Reading completed for seed "
							+ seed);
			System.out.println("Time elapsed (s): " + (end - start)
					/ 1000000000.0);

			// Make sure all of the reactors are equivalent but separate.
			assertFalse(reactor == backupReactor);
			assertFalse(reactor == loadedReactor);
			assertFalse(backupReactor == loadedReactor);
			assertEquals(reactor, backupReactor);
			assertEquals(reactor, loadedReactor);

			end = System.nanoTime();
			System.out
					.println("SFReactorIOHandlerTester message: Writing/reading tests completed successfully for seed "
							+ seed);
			System.out.println("Total Time elapsed (s): " + (end - testStart)
					/ 1000000000.0);
		}

		// Delete the .h5 file produced by this test.
		try {
			File file = new File(path);
			file.delete();
		} catch (Exception e) {
			System.err
					.println("SFReactorIOHandlerTester error: Could not delete file \""
							+ path + "\".");
			fail();
		}

		return;
	}
}