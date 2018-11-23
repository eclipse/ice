/*******************************************************************************
 * Copyright (c) 2013, 2014- UT-Battelle, LLC.
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

package org.eclipse.ice.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.eavp.viz.service.BasicVizServiceFactory;
import org.eclipse.eavp.viz.service.IVizService;
import org.eclipse.eavp.viz.service.IVizServiceFactory;
import org.eclipse.eavp.viz.service.csv.CSVVizService;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.resource.ResourceHandler;
import org.eclipse.ice.datastructures.resource.VizResource;
import org.junit.Test;

/**
 * This class tests the {@link ResourceHandler} methods.
 * 
 * @author Anna Wojtowicz
 */
public class ResourceHandlerTester {

	@Test
	public void checkGettingResource() throws IOException {

		// Local Declarations
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "datastructuresData";
		String txtFilePath = userDir + separator + "txtResource.txt";
		String csvFilePath = userDir + separator + "csvResource.csv";

		// Create a ResourceHandler, ICEResource and VizResource for testing
		ResourceHandler handler = new ResourceHandler();
		ICEResource iceResource = null;
		VizResource vizResource = null;

		// Set up a basic factory with a CSVVizService for the test
		IVizServiceFactory factory = new TestVizServiceFactory();
		factory.register(new CSVVizService());
		org.eclipse.ice.datastructures.internal.VizServiceFactoryHolder
				.setVizServiceFactory(factory);

		// First try get the resources with invalid file paths
		iceResource = handler.getResource("");
		vizResource = (VizResource) handler.getResource("");

		// Verify they're still null
		assertNull(iceResource);
		assertNull(vizResource);

		// Now try to get the resources with valid parameters
		iceResource = handler.getResource(txtFilePath);
		vizResource = (VizResource) handler.getResource(csvFilePath);

		// Verify the the resources are no longer null and are the right type
		assertNotNull(iceResource);
		assertNotNull(vizResource);

		// Verify the type, default name, ID and description of the ICEResource
		assertTrue(iceResource instanceof ICEResource);
		assertEquals("txtResource.txt", iceResource.getName());
		assertEquals(1, iceResource.getId());
		assertEquals(txtFilePath, iceResource.getDescription());

		// Verify the type, default name, ID and description of the VizResource
		assertTrue(vizResource instanceof VizResource);
		assertEquals("csvResource.csv", vizResource.getName());
		assertEquals(1, vizResource.getId());
		assertEquals(csvFilePath, vizResource.getDescription());

		// Now set up some parameters to pass in
		String nameOne = "Senor Smudgy McButtScooch";
		String nameTwo = "Lady Mittens";
		int idOne = 9;
		int idTwo = 3;
		String descOne = "A scholar and a gentlekitten";
		String descTwo = "Countess of Litterville, Patron of Hairballs";

		// Null out our resources once more
		iceResource = null;
		vizResource = null;

		// Try getting the resources again with invalid filepaths, using the
		// method signature with 4 parameters.
		iceResource = handler.getResource("", nameOne, idOne, descOne);
		vizResource = (VizResource) handler.getResource("", nameTwo, idTwo,
				descTwo);

		// NOTE: since all four getResource(...) methods are daisy chained
		// together, testing the first and last one inherently tests everything
		// in between.

		// Check the resources are still null
		assertNull(iceResource);
		assertNull(vizResource);

		// Finally, now pass in all valid parameters
		iceResource = handler.getResource(txtFilePath, nameOne, idOne, descOne);
		vizResource = (VizResource) handler.getResource(csvFilePath, nameTwo,
				idTwo, descTwo);

		// Verify the type, default name, ID and description of the ICEResource
		assertTrue(iceResource instanceof ICEResource);
		assertEquals(nameOne, iceResource.getName());
		assertEquals(idOne, iceResource.getId());
		assertEquals(descOne, iceResource.getDescription());

		// Verify the type, default name, ID and description of the VizResource
		assertTrue(vizResource instanceof VizResource);
		assertEquals(nameTwo, vizResource.getName());
		assertEquals(idTwo, vizResource.getId());
		assertEquals(descTwo, vizResource.getDescription());
	}

	/**
	 * A simple implementation of an IVizServiceFactory that simply holds a single VizService.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestVizServiceFactory extends BasicVizServiceFactory {

		/**
		 * The single VizService that the factory will contain
		 */
		IVizService service;

		@Override
		public IVizService get(String name) {
			return service;
		}

		@Override
		public String[] getServiceNames() {
			String[] names = new String[] { service.getName() };
			return names;
		}

		@Override
		public void register(IVizService service) {
			this.service = service;
		}
	}
}
