/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eclipse.ice.viz.service.paraview.ParaViewVizService;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class is responsible for testing the {@link ParaViewVizService}.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewVizServiceTester {

	// TODO Implement these tests.

	/**
	 * This test checks the name of the visualization service.
	 * 
	 * @see ParaViewVizService#getName()
	 */
	@Test
	public void checkName() {
		final String expectedName = "ParaView";

		// The name should always be the same. Just try getting it a few times.
		ParaViewVizService service = new ParaViewVizService();
		assertEquals(expectedName, service.getName());
		assertEquals(expectedName, service.getName());
		assertEquals(expectedName, new ParaViewVizService().getName());

		return;
	}

	/**
	 * This test checks the version information for the service.
	 * 
	 * @see ParaViewVizService#getVersion()
	 */
	@Test
	public void checkVersion() {
		// TODO Update this test. For now, the version should always be the
		// same. However, it may be that we can connect to multiple versions at
		// run-time!
		final String expectedVersion = "";

		// The name should always be the same. Just try getting it a few times.
		ParaViewVizService service = new ParaViewVizService();
		assertEquals(expectedVersion, service.getVersion());
		assertEquals(expectedVersion, service.getVersion());
		assertEquals(expectedVersion, new ParaViewVizService().getVersion());

		return;
	}

	/**
	 * This test checks the service's connection properties, including their
	 * default values.
	 * 
	 * @see ParaViewVizService#hasConnectionProperties()
	 * @see ParaViewVizService#getConnectionProperties()
	 * @see ParaViewVizService#setConnectionProperties(java.util.Map)
	 */
	@Ignore
	@Test
	public void checkConnectionProperties() {
		fail("Not implemented.");
	}

	/**
	 * This test checks that the service connects properly.
	 * 
	 * @see ParaViewVizService#connect()
	 */
	@Ignore
	@Test
	public void checkConnect() {
		fail("Not implemented.");
	}

	/**
	 * This test checks the plots created by the service.
	 * 
	 * @see ParaViewVizService#createPlot(java.net.URI)
	 */
	@Ignore
	@Test
	public void checkPlot() {
		fail("Not implemented.");
	}
}
