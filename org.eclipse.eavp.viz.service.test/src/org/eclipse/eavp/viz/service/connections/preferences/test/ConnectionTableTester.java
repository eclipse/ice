/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation 
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections.preferences.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.eavp.viz.service.connections.preferences.ConnectionTable;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.junit.Before;
import org.junit.Test;

/**
 * This checks the new functionality provided by {@link ConnectionTable}, which
 * is a sub-class of {@link TableComponent}, in particular the ability to
 * associated rows with unique keys or names (contained in the first Entry in
 * each row).
 * 
 * @author Jordan Deyton
 *
 */
public class ConnectionTableTester {

	/**
	 * The table that will be tested.
	 */
	private ConnectionTable table;

	/**
	 * The default host entry value.
	 */
	private static final String DEFAULT_HOST = "localhost";
	/**
	 * The default port entry value.
	 */
	private static final String DEFAULT_PORT = "9600";
	/**
	 * The default path entry value.
	 */
	private static final String DEFAULT_PATH = "";

	/**
	 * Sets up the class variables used for testing.
	 */
	@Before
	public void beforeEachTest() {
		table = new ConnectionTable();
	}

	/**
	 * Checks the default structure of the connection TableComponent.
	 */
	@Test
	public void checkDefaults() {

		// Check the custom getters.
		assertNull(table.getConnection(null));
		assertNotNull(table.getConnectionNames());
		assertTrue(table.getConnectionNames().isEmpty());
		// Check the columns.
		assertEquals(4, table.numberOfColumns());
		assertEquals(4, table.getColumnNames().size());
		assertEquals("Name", table.getColumnNames().get(0));
		assertEquals("Host", table.getColumnNames().get(1));
		assertEquals("Port", table.getColumnNames().get(2));
		assertEquals("Path", table.getColumnNames().get(3));
		// Check the rows and row template.
		assertEquals(0, table.numberOfRows());
		assertNotNull(table.getRowTemplate());
		assertEquals(4, table.getRowTemplate().size());

		return;
	}

	/**
	 * Checks that connections can be added and queried from the table using the
	 * additional getters.
	 */
	@Test
	public void checkAddConnection() {

		int i;
		List<VizEntry> row;
		String name;

		// Add a new row and get it.
		name = "Connection1";
		i = table.addRow();
		row = table.getRow(i);

		// It should be a non-empty row.
		assertNotNull(row);
		assertFalse(row.isEmpty());
		// Check its information.
		assertEquals(4, row.size());
		assertEquals(name, row.get(0).getValue());
		assertEquals(DEFAULT_HOST, row.get(1).getValue());
		assertEquals(DEFAULT_PORT, row.get(2).getValue());
		assertEquals(DEFAULT_PATH, row.get(3).getValue());
		// Getting it by connection name should return the same row.
		assertEquals(row, table.getConnection(name));
		// Check the set of connection names.
		assertTrue(table.getConnectionNames().contains(name));
		// Check the number of rows.
		assertEquals(1, table.numberOfRows());

		// Add another row and check it.
		name = "Connection2";
		i = table.addRow();
		row = table.getRow(i);

		// It should be a non-empty row.
		assertNotNull(row);
		assertFalse(row.isEmpty());
		// Check its information.
		assertEquals(4, row.size());
		assertEquals(name, row.get(0).getValue());
		assertEquals(DEFAULT_HOST, row.get(1).getValue());
		assertEquals(DEFAULT_PORT, row.get(2).getValue());
		assertEquals(DEFAULT_PATH, row.get(3).getValue());
		// Getting it by connection name should return the same row.
		assertEquals(row, table.getConnection(name));
		// Check the set of connection names.
		assertTrue(table.getConnectionNames().contains(name));
		assertTrue(table.getConnectionNames().contains("Connection1"));
		// Check the number of rows.
		assertEquals(2, table.numberOfRows());

		return;
	}

	/**
	 * Checks that connections can be removed, at which point they can no longer
	 * be queried from the table.
	 */
	@Test
	public void checkRemoveConnection() {

		List<VizEntry> row;
		String name;
		String host = "qwerty";

		// Add three connections.
		assertEquals(0, table.addRow());
		assertEquals(1, table.addRow());
		assertEquals(2, table.addRow());
		assertNotNull(table.getConnection("Connection1"));
		assertNotNull(table.getConnection("Connection2"));
		assertNotNull(table.getConnection("Connection3"));

		// Remove the first one. The first one should no longer exist.
		table.deleteRow(0);
		assertNull(table.getConnection("Connection1"));
		assertNotNull(table.getConnection("Connection2"));
		assertNotNull(table.getConnection("Connection3"));
		// Check the number of rows and the set of connection names.
		assertEquals(2, table.numberOfRows());
		assertFalse(table.getConnectionNames().contains("Connection1"));
		assertTrue(table.getConnectionNames().contains("Connection2"));
		assertTrue(table.getConnectionNames().contains("Connection3"));

		// Check the contents of the remaining connections.
		name = "Connection2";
		row = table.getRow(0);
		// It should be a non-empty row.
		assertNotNull(row);
		assertFalse(row.isEmpty());
		// Check its information.
		assertEquals(4, row.size());
		assertEquals(name, row.get(0).getValue());
		assertEquals(DEFAULT_HOST, row.get(1).getValue());
		assertEquals(DEFAULT_PORT, row.get(2).getValue());
		assertEquals(DEFAULT_PATH, row.get(3).getValue());
		// Getting it by connection name should return the same row.
		assertEquals(row, table.getConnection(name));
		// Do the same for the next connection.
		name = "Connection3";
		row = table.getRow(1);
		// It should be a non-empty row.
		assertNotNull(row);
		assertFalse(row.isEmpty());
		// Check its information.
		assertEquals(4, row.size());
		assertEquals(name, row.get(0).getValue());
		assertEquals(DEFAULT_HOST, row.get(1).getValue());
		assertEquals(DEFAULT_PORT, row.get(2).getValue());
		assertEquals(DEFAULT_PATH, row.get(3).getValue());
		// Getting it by connection name should return the same row.
		assertEquals(row, table.getConnection(name));

		// Update the host name of connection 3.
		row.get(1).setValue(host);

		// Add a new row.
		name = "Connection1";
		assertEquals(2, table.addRow());
		// Check its contents.
		row = table.getRow(2);
		// It should be a non-empty row.
		assertNotNull(row);
		assertFalse(row.isEmpty());
		// Check its information.
		assertEquals(4, row.size());
		assertEquals(name, row.get(0).getValue());
		assertEquals(DEFAULT_HOST, row.get(1).getValue());
		assertEquals(DEFAULT_PORT, row.get(2).getValue());
		assertEquals(DEFAULT_PATH, row.get(3).getValue());
		// Getting it by connection name should return the same row.
		assertEquals(row, table.getConnection(name));

		// Check that the second connection added is still the same.
		name = "Connection2";
		row = table.getRow(0);
		// It should be a non-empty row.
		assertNotNull(row);
		assertFalse(row.isEmpty());
		// Check its information.
		assertEquals(4, row.size());
		assertEquals(name, row.get(0).getValue());
		assertEquals(DEFAULT_HOST, row.get(1).getValue());
		assertEquals(DEFAULT_PORT, row.get(2).getValue());
		assertEquals(DEFAULT_PATH, row.get(3).getValue());
		// Getting it by connection name should return the same row.
		assertEquals(row, table.getConnection(name));
		// Do the same for the third connection.
		name = "Connection3";
		row = table.getRow(1);
		// It should be a non-empty row.
		assertNotNull(row);
		assertFalse(row.isEmpty());
		// Check its information.
		assertEquals(4, row.size());
		assertEquals(name, row.get(0).getValue());
		assertEquals(host, row.get(1).getValue());
		assertEquals(DEFAULT_PORT, row.get(2).getValue());
		assertEquals(DEFAULT_PATH, row.get(3).getValue());
		// Getting it by connection name should return the same row.
		assertEquals(row, table.getConnection(name));

		// Check the number of rows and the set of connection names.
		assertEquals(3, table.numberOfRows());
		assertTrue(table.getConnectionNames().contains("Connection1"));
		assertTrue(table.getConnectionNames().contains("Connection2"));
		assertTrue(table.getConnectionNames().contains("Connection3"));

		return;
	}

	/**
	 * Checks that connections must have unique names and that attempting to
	 * re-use names will not work.
	 */
	@Test
	public void checkUniqueNames() {

		List<VizEntry> row;
		String string = "qwerty";

		// Add two connections.
		assertEquals(0, table.addRow());
		assertEquals(1, table.addRow());
		assertNotNull(table.getConnection("Connection1"));
		assertNotNull(table.getConnection("Connection2"));
		// Update the *host* of connection 2.
		row = table.getRow(1);
		row.get(1).setValue(string);

		// Changing the name of connection 1 should work.
		row = table.getRow(0);
		assertTrue(row.get(0).setValue(string));
		// The table will be notified on a separate thread. Give it some time to
		// work.
		long interval = 50;
		long threshold = 2000;
		long sleepTime = 0;
		while (table.getConnection("Connection1") != null && sleepTime < threshold) {
			try {
				Thread.sleep(interval);
				sleepTime += interval;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		assertNull(table.getConnection("Connection1"));
		assertEquals(row, table.getConnection(string));
		assertFalse(table.getConnectionNames().contains("Connection1"));
		assertTrue(table.getConnectionNames().contains(string));

		// We should not be able to change the name of connection 2 to the same
		// value.
		row = table.getRow(1);
		assertFalse(row.get(0).setValue(string));
		assertEquals(row, table.getConnection("Connection2"));
		assertTrue(table.getConnectionNames().contains("Connection2"));

		// Connection 1 should still be there with its new name.
		row = table.getRow(0);
		assertFalse(table.getConnectionNames().contains("Connection1"));
		assertTrue(table.getConnectionNames().contains(string));
		assertNull(table.getConnection("Connection1"));
		assertEquals(row, table.getConnection(string));

		return;
	}

}
