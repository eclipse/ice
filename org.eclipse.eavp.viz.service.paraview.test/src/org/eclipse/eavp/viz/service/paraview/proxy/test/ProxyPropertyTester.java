/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan H. Deyton (UT-Battelle, LLC.) - Initial API and implementation 
 *   and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.paraview.proxy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.connections.VizConnection;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyProperty;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyProperty.PropertyType;
import org.eclipse.eavp.viz.service.paraview.test.FakeParaViewWebClient;
import org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the {@link AbstractProxyProperty}'s implementation of
 * {@link IProxyProperty} and that it correctly calls the sub-class'
 * implementation when necessary.
 * 
 * @author Jordan Deyton
 *
 */
public class ProxyPropertyTester {

	/**
	 * The property that will be tested. This needs to be created in each test.
	 */
	private ProxyProperty property;

	/**
	 * A fake ParaView client.
	 */
	private FakeParaViewWebClient fakeClient;
	/**
	 * A fake connection that uses the fake client.
	 */
	private IVizConnection<IParaViewWebClient> connection;

	/**
	 * Sets up the fake connection that can be associated with the property for
	 * testing (it interacts with a connection to find and set values).
	 */
	@Before
	public void beforeEachTest() {

		FakeProxyFeature property;

		fakeClient = new FakeParaViewWebClient();
		// ---- Add properties to the fake client. ---- //
		// Add a DISCRETE property.
		property = new FakeProxyFeature("...I Care Because You Do", 2,
				PropertyType.DISCRETE);
		property.propertyName = "Aphex Twin";
		property.initialValue = "Acrid Avid Jam Shred";
		property.setAllowedValues("Acrid Avid Jam Shred", "The Waxen Pith",
				"Wax The Nip");
		fakeClient.addProxyProperty(property);
		// Add a DISCRETE_MULTI property.
		property = new FakeProxyFeature("Richard D. James Album", 3,
				PropertyType.DISCRETE_MULTI);
		property.propertyName = "Aphex Twin";
		property.initialValue = "4";
		property.setAllowedValues("4", "Cornish Acid", "Fingerbib",
				"Yellow Calx");
		fakeClient.addProxyProperty(property);
		// Add an UNDEFINED property.
		property = new FakeProxyFeature("Donkey Rhubarb", 4,
				PropertyType.UNDEFINED);
		property.propertyName = "Aphex Twin";
		property.initialValue = "Donkey Rhubarb";
		fakeClient.addProxyProperty(property);
		// -------------------------------------------- //

		// Set up a fake connection that uses the fake client.
		connection = new VizConnection<IParaViewWebClient>() {
			@Override
			protected IParaViewWebClient connectToWidget() {
				// Point the connection to localhost.
				setHost("localhost");
				// Return the fake client.
				fakeClient.connect("localhost");
				return fakeClient;
			}

			@Override
			protected boolean disconnectFromWidget(IParaViewWebClient widget) {
				return true;
			}
		};
		// Connect the connection.
		try {
			((VizConnection<IParaViewWebClient>) connection).connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * Checks that the basic fields and getters for a property are set at
	 * construction time.
	 */
	@Test
	public void checkConstruction() {
		// Check the first constructor.
		property = new ProxyProperty("...I Care Because You Do", 2) {
			@Override
			protected int getProxyId() {
				return 0;
			}
		};
		// Check the public final fields.
		assertEquals("...I Care Because You Do", property.name);
		assertEquals(2, property.index);
		// Check that the getters return empty or null values when appropriate.
		assertNotNull(property.getAllowedValues());
		assertTrue(property.getAllowedValues().isEmpty());
		assertNull(property.getValue());
		assertNotNull(property.getValues());
		assertTrue(property.getValues().isEmpty());

		// Check the second constructor.
		property = new ProxyProperty("Richard D. James Album", 3,
				PropertyType.DISCRETE) {
			@Override
			protected int getProxyId() {
				return 0;
			}
		};
		// Check the public final fields.
		assertEquals("Richard D. James Album", property.name);
		assertEquals(3, property.index);
		// Check that the getters return empty or null values when appropriate.
		assertNotNull(property.getAllowedValues());
		assertTrue(property.getAllowedValues().isEmpty());
		assertNull(property.getValue());
		assertNotNull(property.getValues());
		assertTrue(property.getValues().isEmpty());

		return;
	}

	/**
	 * Checks that the property's initial values are loaded when its connection
	 * is set.
	 */
	@Test
	public void checkSetConnection() {

		// Set up the expected values for the property under test.
		String initialValue = "Acrid Avid Jam Shred";
		String[] allowedValues = new String[] { "Acrid Avid Jam Shred",
				"The Waxen Pith", "Wax The Nip" };

		// Set up a regular property.
		property = new ProxyProperty("...I Care Because You Do", 2,
				PropertyType.DISCRETE) {
			@Override
			protected int getProxyId() {
				return 0;
			}
		};

		// Initially, the connection is null. Setting it to a new value should
		// return true, but setting it to the same value should return false.
		assertFalse(property.setConnection(null));
		assertTrue(property.setConnection(connection));
		assertFalse(property.setConnection(connection));

		// ---- Check the contents of the property. ---- //)
		// Check the allowed values.
		assertNotNull(property.getAllowedValues());
		for (String value : allowedValues) {
			assertTrue(
					"ProxyPropertyTester error: "
							+ "Property did not contain the allowed value \""
							+ value + "\".",
					property.getAllowedValues().contains(value));
			assertTrue("ProxyPropertyTester error: "
					+ "Property did not contain the allowed value \"" + value
					+ "\".", property.valueAllowed(value));
		}
		// Check the initial value.
		assertEquals(initialValue, property.getValue());
		// Because the type is discrete, the values array is empty.
		assertNotNull(property.getValues());
		assertTrue(property.getValues().isEmpty());
		// --------------------------------------------- //

		// Reset the property by setting its connection to null, and check that
		// everything was cleared.
		assertTrue(property.setConnection(null));
		// Check that the getters return empty or null values when appropriate.
		assertNotNull(property.getAllowedValues());
		assertTrue(property.getAllowedValues().isEmpty());
		assertNull(property.getValue());
		assertNotNull(property.getValues());
		assertTrue(property.getValues().isEmpty());

		return;
	}

	/**
	 * Checks that the property's value can be set to one of multiple allowed
	 * values when its type is discrete.
	 */
	@Test
	public void checkSetValueDiscrete() {
		// Set up the expected values for the property under test.
		String initialValue = "Acrid Avid Jam Shred";
		String value = "The Waxen Pith";
		String invalidValue = "Digeridoo";
		String nullValue = null;
		List<String> valueArray = new ArrayList<String>();

		// Set up a regular property.
		property = new ProxyProperty("...I Care Because You Do", 2,
				PropertyType.DISCRETE) {
			@Override
			protected int getProxyId() {
				return 0;
			}
		};

		// Set the connection.
		property.setConnection(connection);

		// Setting the property to the initial value should make no difference.
		assertEquals(initialValue, property.getValue());
		assertFalse(property.setValue(initialValue));
		assertEquals(initialValue, property.getValue());

		// Setting it to a new value should update it, but the second attempt
		// should return false.
		assertTrue(property.setValue(value));
		assertEquals(value, property.getValue());
		assertFalse(property.setValue(value));
		assertEquals(value, property.getValue());

		// Setting it to invalid values shouldn't work.
		assertFalse(property.setValue(invalidValue));
		assertFalse(property.setValue(nullValue));
		assertEquals(value, property.getValue());

		// Setting the value via the array input shouldn't work.
		valueArray.add(initialValue);
		assertFalse(property.setValues(valueArray));
		assertFalse(property.setValues(null));
		assertEquals(value, property.getValue());

		return;
	}

	/**
	 * Checks that the property's value can be set to any value when its type is
	 * undefined.
	 */
	@Test
	public void checkSetValueUndefined() {
		// Set up the expected values for the property under test.
		String initialValue = "Donkey Rhubarb";
		String value = "Yellow Calx";
		String nullValue = null;
		List<String> valueArray = new ArrayList<String>();

		// Set up a regular property.
		property = new ProxyProperty("Donkey Rhubarb", 4,
				PropertyType.UNDEFINED) {
			@Override
			protected int getProxyId() {
				return 0;
			}
		};

		// Set the connection.
		property.setConnection(connection);

		// Setting the property to the initial value should make no difference.
		assertEquals(initialValue, property.getValue());
		assertFalse(property.setValue(initialValue));
		assertEquals(initialValue, property.getValue());

		// Setting it to a new value should update it, but the second attempt
		// should return false.
		assertTrue(property.setValue(value));
		assertEquals(value, property.getValue());
		assertFalse(property.setValue(value));
		assertEquals(value, property.getValue());

		// It should also work with null values.
		assertTrue(property.setValue(nullValue));
		assertNull(property.getValue());
		assertFalse(property.setValue(nullValue));
		assertNull(property.getValue());

		// Setting the value via the array input shouldn't work.
		valueArray.add(initialValue);
		assertFalse(property.setValues(valueArray));
		assertFalse(property.setValues(null));
		assertNull(property.getValue());

		return;
	}

	/**
	 * Checks that the property's value can be set to multiple allowed values
	 * when its type is discrete (with multi-select).
	 */
	@Test
	public void checkSetValueDiscreteMulti() {
		// Set up the expected values for the property under test.
		String initialValue = "4";
		String value = "Yellow Calx";
		String invalidValue = "Digeridoo";
		List<String> valueArray = new ArrayList<String>();

		// Set up a regular property.
		property = new ProxyProperty("Richard D. James Album", 3,
				PropertyType.DISCRETE_MULTI) {
			@Override
			protected int getProxyId() {
				return 0;
			}
		};

		// Set the connection.
		property.setConnection(connection);

		// Setting the property to the initial value should make no
		valueArray.add(initialValue);
		assertEquals(valueArray, property.getValues());
		assertFalse(property.setValues(valueArray));
		assertEquals(valueArray, property.getValues());

		// Setting it to a new value should update it, but the second attempt
		// should return false.
		valueArray.add(value);
		assertTrue(property.setValues(valueArray));
		assertEquals(valueArray, property.getValues());
		assertFalse(property.setValues(valueArray));
		assertEquals(valueArray, property.getValues());

		// Setting it to invalid values shouldn't work.
		valueArray.add(invalidValue);
		assertFalse(property.setValues(valueArray));
		assertFalse(property.setValues(null));
		valueArray.remove(2);
		assertEquals(valueArray, property.getValues());

		// Setting the value via the single value input should work by setting
		// the value array to a single value.
		assertTrue(property.setValue(value));
		valueArray.clear();
		valueArray.add(value);
		assertEquals(valueArray, property.getValues());
		assertFalse(property.setValue(value));
		assertEquals(valueArray, property.getValues());

		// Getting the single value returns null (we have to get the array of
		// values).
		assertNull(property.getValue());

		return;
	}

}
