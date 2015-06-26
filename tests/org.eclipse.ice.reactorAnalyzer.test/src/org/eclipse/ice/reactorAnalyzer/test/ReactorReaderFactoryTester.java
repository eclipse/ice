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
package org.eclipse.ice.reactorAnalyzer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;

import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.reactor.LWRComponentWriter;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.sfr.base.SFReactorIOHandler;
import org.eclipse.ice.reactor.sfr.core.SFReactor;
import org.eclipse.ice.reactorAnalyzer.ReactorReaderFactory;
import org.junit.Test;

public class ReactorReaderFactoryTester {

	@Test
	public void checkReading() {

		// Initialize a factory.
		ReactorReaderFactory factory = new ReactorReaderFactory();

		// Set up some URIs in the test folder.
		String separator = System.getProperty("file.separator");
		String directory = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "reactorReaderTesterWorkspace";
		URI badURI = new File(directory + separator + "doesNotExist.h5").toURI();

		// Bad URIs shouldn't work.
		assertNull(factory.readReactor(null));
		assertNull(factory.readReactor(badURI));

		// Test valid URIs.
		IReactorComponent reactor;
		IReactorComponent loadedReactor;
		LWRComponentWriter lwrWriter = new LWRComponentWriter();
		SFReactorIOHandler sfrWriter = new SFReactorIOHandler();
		File file;
		URI uri;

		/* ---- Test LWR functionality. ---- */
		// Generate and write a simple reactor.
		reactor = new PressurizedWaterReactor(4);
		file = new File(directory + "simpleLWR.h5");
		uri = file.toURI();
		lwrWriter.write((PressurizedWaterReactor) reactor, uri);

		// Use the factory to read the reactor.
		loadedReactor = factory.readReactor(uri);

		// Verify that they are equivalent.
		assertTrue(reactor.equals(loadedReactor));

		// Delete the file that was just created.
		try {
			file.delete();
		} catch (Exception e) {
			System.err
					.println("ReactorReaderFactoryTester error: Could not delete file \""
							+ file.getAbsolutePath() + "\".");
			fail();
		}
		/* --------------------------------- */

		/* ---- Test SFR functionality. ---- */
		// Generate and write a simple reactor.
		reactor = new SFReactor(4);
		file = new File(directory + "simpleSFR.h5");
		uri = file.toURI();
		sfrWriter.writeHDF5(uri, (SFReactor) reactor);

		// Use the factory to read the reactor.
		loadedReactor = factory.readReactor(uri);

		// Verify that they are equivalent.
		assertTrue(reactor.equals(loadedReactor));

		// Delete the file that was just created.
		try {
			file.delete();
		} catch (Exception e) {
			System.err
					.println("ReactorReaderFactoryTester error: Could not delete file \""
							+ file.getAbsolutePath() + "\".");
			fail();
		}
		/* --------------------------------- */

		return;
	}

	@Test
	public void checkCopying() {

		// Initialize a factory.
		ReactorReaderFactory factory = new ReactorReaderFactory();

		// We need a source and destination.
		IReactorComponent source = null;
		IReactorComponent destination = null;

		/* ---- Can't copy if either are null. ---- */
		assertFalse(factory.copyReactor(source, destination));
		assertNull(source);
		assertNull(destination);

		source = new PressurizedWaterReactor(1);
		destination = null;
		assertFalse(factory.copyReactor(source, destination));
		assertNotNull(source);
		assertNull(destination);

		source = null;
		destination = new PressurizedWaterReactor(2);
		assertFalse(factory.copyReactor(source, destination));
		assertNull(source);
		assertNotNull(destination);
		/* ---------------------------------------- */

		/* ---- Can't copy if they are different types. --- */
		source = new PressurizedWaterReactor(3);
		destination = new SFReactor(4);

		assertFalse(factory.copyReactor(source, destination));
		assertTrue(source instanceof PressurizedWaterReactor);
		assertTrue(destination instanceof SFReactor);
		/* ------------------------------------------------ */

		/* ---- Can copy if they are the same type. ---- */
		// Test PWRs.
		source = new PressurizedWaterReactor(1);
		source.setName("Spike Spiegel");
		source.setDescription("Just a wandering cowboy.");

		destination = new PressurizedWaterReactor(2);
		destination.setName("Faye Valentine");
		destination.setDescription("Already a fairy.");

		assertFalse(source.equals(destination));
		assertTrue(factory.copyReactor(source, destination));
		assertTrue(source.equals(destination));

		// Test SFRs.
		source = new SFReactor(3);
		source.setName("Jet Black");
		source.setDescription("The Black Dog.");

		destination = new SFReactor(4);
		destination.setName("Ed");
		destination.setDescription("Hacker extraordinaire.");

		assertFalse(source.equals(destination));
		assertTrue(factory.copyReactor(source, destination));
		assertTrue(source.equals(destination));
		/* --------------------------------------------- */

		return;
	}
}
