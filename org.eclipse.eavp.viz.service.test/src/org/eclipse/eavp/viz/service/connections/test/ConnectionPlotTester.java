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
package org.eclipse.eavp.viz.service.connections.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.eavp.viz.service.connections.ConnectionPlot;
import org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite;
import org.eclipse.eavp.viz.service.connections.ConnectionState;
import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.connections.VizConnection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class checks that the {@link ConnectionPlot} correctly synchronizes the
 * associated {@link ConnectionPlotRender} with changes to its properties or to
 * its associated {@link IVizConnection}.
 * 
 * @author Jordan Deyton
 *
 */
public class ConnectionPlotTester {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ConnectionPlot.class);

	// TODO Implement these tests.

	/**
	 * This test checks the default state of a {@code ConnectionPlot} after
	 * being constructed.
	 */
	@Test
	public void checkConstruction() {
		// Create a fake Viz Connection
		VizConnection<FakeClient> connection = new FakeVizConnection();

		// Create the TestConnectionPlot for a FakeClient
		TestConnectionPlot<FakeClient> testPlot = new TestConnectionPlot(
				connection);

		// Test the initial values for the TestConnectionPlot's data members.
		assertTrue(testPlot.getCategories().isEmpty());
		assertNull(testPlot.getDataSource());
		assertNull(testPlot.getIndependentSeries());
		assertEquals(0, testPlot.getNumberOfAxes());
		assertNull(testPlot.getPlotTitle());
		assertTrue(testPlot.getProperties().isEmpty());
		assertNull(testPlot.getSourceHost());

	}

	/**
	 * Tests {@code ConnectionPlot}'s response to updating the plot's data
	 * source URI.
	 */
	@Test
	public void checkDataSource() {
		// Create a fake Viz Connection
		VizConnection<FakeClient> connection = new FakeVizConnection();

		// Create the TestConnectionPlot for a FakeClient
		TestConnectionPlot<FakeClient> testPlot = new TestConnectionPlot(
				connection);

		// Create two URIs
		URI filepath = null;
		URI filepath2 = null;

		try {
			filepath = new URI("temp.txt");
			filepath2 = new URI("text.tmp");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		// Set the URI as the plot's data source
		try {
			testPlot.setDataSource(filepath);
		} catch (Exception e) {
			fail("Failed to set the plot's data source.");
		}

		// Check that the data source was properly set
		assertTrue(testPlot.getDataSource().equals(filepath));

		// Check that the plot returns that the data source has been changed.
		try {
			assertTrue(testPlot.setDataSource(filepath2));
		} catch (Exception e) {
			fail("failed to set the plot's data source.");
		}

		// Check that the plot has the correct host
		assertTrue(testPlot.getSourceHost().equals("localhost"));

	}

	/**
	 * Tests the {@code IVizConnection} associated with a {@code ConnectionPlot}
	 * 
	 * 
	 */
	@Test
	public void checkConnection() {
		// Create a fake Viz Connection connected to IP address 1.2.3.4
		VizConnection<FakeClient> connection = new FakeVizConnection() {
			@Override
			public String getHost() {
				return "1.2.3.4";
			}
		};

		// Create the TestConnectionPlot for a FakeClient
		TestConnectionPlot<FakeClient> testPlot = new TestConnectionPlot(
				connection);

		// Create a fake URI
		URI filepath = null;

		try {
			filepath = new URI("temp.txt");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		// Try to set the data source. This should fail, because the
		// connection is to a different host than the file.

		try {
			testPlot.setDataSource(filepath);
		} catch (Exception e1) {
			//An exception is expected for this illegal data source assignment
		}
		assertFalse(filepath.equals(testPlot.getDataSource()));

		try {
			// Check that a new connection can be set
			VizConnection<FakeClient> connection2 = new FakeVizConnection();
			assertTrue(testPlot.setConnection(connection2));

			// Trying to set it to the same connection should fail and return
			// false
			assertFalse(testPlot.setConnection(connection2));

			// The URI should be allowed by this connection
			assertTrue(testPlot.setDataSource(filepath));
		} catch (Exception e) {
			fail();
		}
	}
}

class TestConnectionPlot<T> extends ConnectionPlot<T> {

	/**
	 * A constructor to create a simple connectionPlot.
	 * 
	 * @param connection
	 */
	TestConnectionPlot(VizConnection<T> connection) {
		try {
			this.setConnection(connection);
		} catch (Exception e) {
			fail("Failed to set the connection for the test plot.");
		}
	}

	@Override
	protected ConnectionPlotComposite<T> createPlotComposite(Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void connectionStateChanged(IVizConnection<T> connection,
			ConnectionState state, String message) {
		// Nothing to do.
	}

	@Override
	public String createAdditionalPage(MultiPageEditorPart parent, IFileEditorInput file, int pageNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumAdditionalPages() {
		// TODO Auto-generated method stub
		return 0;
	}

}
