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
package org.eclipse.ice.viz.service.paraview.proxy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.eclipse.ice.viz.service.connections.IVizConnection;
import org.eclipse.ice.viz.service.paraview.connections.ParaViewConnection;
import org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.ProxyFeature;
import org.eclipse.ice.viz.service.paraview.proxy.ProxyProperty;
import org.eclipse.ice.viz.service.paraview.test.FakeParaViewWebClient;
import org.eclipse.ice.viz.service.paraview.test.TestUtils;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the basic features provided by the
 * {@link AbstractParaViewProxy}.
 * 
 * @author Jordan Deyton
 *
 */
public class AbstractParaViewProxyTester {

	/**
	 * The proxy that will be tested.
	 */
	private AbstractParaViewProxy proxy;
	/**
	 * The fake proxy that is used to test the basic implementation provided by
	 * {@link AbstractParaViewProxy}. This should be the same as {@link #proxy}.
	 */
	private FakeParaViewProxy fakeProxy;

	/**
	 * A test URI used to create the {@link #fakeProxy}.
	 */
	private URI testURI;

	/**
	 * A fake ParaView web client. This is the same one contained in
	 * {@link #connection}.
	 */
	private FakeParaViewWebClient fakeClient;

	/**
	 * The connection that should be used by the proxy.
	 */
	private ParaViewConnection connection;

	/**
	 * Initializes the {@link #proxy}, {@link #fakeProxy}, and {@link #testURI}.
	 */
	@Before
	public void beforeEachTest() {

		// Initialize the proxy with a test URI.
		testURI = TestUtils.createURI("go-go-gadget-extension");
		fakeProxy = new FakeParaViewProxy(testURI);
		proxy = fakeProxy;

		FakeProxyFeature fakeFeature;

		// Add some features.
		// Add a "europe" feature with the initial value of "london".
		fakeFeature = new FakeProxyFeature("europe", 2, "eu", "london",
				"berlin", "madrid", "paris", "london", "zagreb");
		fakeProxy.features.add(fakeFeature);
		// Add a "north america" feature with the initial value of "havanna".
		fakeFeature = new FakeProxyFeature("north america", 3, "na", "havanna",
				"ottawa", "mexico city", "havanna", "san salvador");
		fakeProxy.features.add(fakeFeature);
		// Add some properties.
		fakeFeature = new FakeProxyFeature("south america", 4, "sa", "caracas",
				"bogota", "brasilia", "caracas", "buenos aires");
		fakeProxy.properties.add(fakeFeature);
		fakeFeature = new FakeProxyFeature("africa", 5, "af", "johannesburg",
				"johannesburg", "cairo", "abuja", "djibouti");
		fakeProxy.properties.add(fakeFeature);
		fakeFeature = new FakeProxyFeature("asia", 6, "as", "tokyo",
				"ulaanbaatar", "beijing", "tokyo", "seoul", "new delhi");
		fakeProxy.properties.add(fakeFeature);
		fakeFeature = new FakeProxyFeature("australia", 7, "au", "canberra",
				"canberra");
		fakeProxy.properties.add(fakeFeature);

		// Set up the fake client.
		fakeClient = new FakeParaViewWebClient();
		// Add all the features and properties to the fake client.
		for (ProxyProperty property : fakeProxy.features) {
			fakeClient.addProxyProperty((FakeProxyFeature) property);
		}
		for (ProxyProperty property : fakeProxy.properties) {
			fakeClient.addProxyProperty((FakeProxyFeature) property);
		}

		// Establish a valid ParaView connection that is connected.
		connection = new ParaViewConnection() {
			@Override
			protected IParaViewWebClient connectToWidget() {
				// Point the connection to localhost.
				setHost("localhost");
				// Return the fake client.
				fakeClient.connect("localhost");
				return fakeClient;
			}
		};
		try {
			connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * Unsets all of the shared test variables.
	 */
	@After
	public void afterEachTest() {
		testURI = null;
		fakeProxy = null;
		proxy = null;
	}

	/**
	 * Checks that the constructor works and throws exceptions when necessary.
	 */
	@Test
	public void checkConstruction() {
		URI nullURI = null;
		try {
			proxy = new FakeParaViewProxy(nullURI);
			fail("AbstractParaViewProxyTester error: "
					+ "NullPointerException not thrown when constructor is "
					+ "passed a null URI.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		return;
	}

	@Test
	public void checkGetFeatureCategories() {
		// Check the default value is an empty collection.
		assertNotNull(proxy.getFeatureCategories());
		assertTrue(proxy.getFeatureCategories().isEmpty());

		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}

		// Check that the feature categories were loaded for the proxy with
		// configured features.
		Set<String> categories = proxy.getFeatureCategories();
		assertNotNull(categories);
		assertEquals(fakeProxy.features.size(), categories.size());
		for (ProxyFeature feature : fakeProxy.features) {
			assertTrue(
					"AbstractParaViewProxyTester error: "
							+ "Proxy does not contain the feature category \""
							+ feature.name + "\".",
					categories.contains(feature.name));
		}

		return;
	}

	@Test
	public void checkGetFeatures() {
		// Check the default value is null since there are no features.
		assertNull(proxy.getFeatures("europe"));
		assertNull(proxy.getFeatures(""));
		assertNull(proxy.getFeatures(null));

		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}

		// Trying to get the features for bad categories should return null.
		assertNull(proxy.getFeatures(""));
		assertNull(proxy.getFeatures(null));

		// Check that each category's set of features matches the fake feature's
		// allowed values. This ensures that the proxy's underlying map of
		// features is correctly populated.
		for (ProxyFeature feature : fakeProxy.features) {
			String category = feature.name;
			Set<String> features = proxy.getFeatures(category);
			assertNotNull("AbstractParaViewProxyTester error: "
					+ "Error getting features for category \"" + category
					+ "\".", features);
			assertEquals(feature.getAllowedValues(), features);
		}

		return;
	}

	@Test
	public void checkGetProperties() {
		// Check the default value is an empty collection.
		assertNotNull(proxy.getProperties());
		assertTrue(proxy.getProperties().isEmpty());

		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}

		// The properties should no longer be empty.
		assertNotNull(proxy.getProperties());
		assertFalse(proxy.getProperties().isEmpty());

		// Check the contents of the map of properties. Each property in the
		// fake proxy should be accounted for, and their initial values should
		// be set.
		Map<String, String> currentProperties = proxy.getProperties();
		for (ProxyProperty property : fakeProxy.properties) {
			String expectedValue = ((FakeProxyFeature) property).initialValue;
			assertTrue(
					"AbstractParaViewProxyTester error: "
							+ "Error getting value for property \""
							+ property.name + "\".",
					currentProperties.containsKey(property.name));
			assertEquals(expectedValue, currentProperties.get(property.name));
		}

		return;
	}

	@Test
	public void checkGetProperty() {
		// Check the default value is null since there are no properties.
		assertNull(proxy.getProperty("australia"));
		assertNull(proxy.getProperty(""));
		assertNull(proxy.getProperty(null));

		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}

		// Trying to get invalid properties should just return null.
		assertNull(proxy.getProperty(""));
		assertNull(proxy.getProperty(null));

		// Check the contents of the map of properties. Each property in the
		// fake proxy should be accounted for, and their initial values should
		// be set.
		for (ProxyProperty property : fakeProxy.properties) {
			String expectedValue = ((FakeProxyFeature) property).initialValue;
			assertEquals(expectedValue, proxy.getProperty(property.name));
		}

		return;
	}

	@Test
	public void checkGetPropertyAllowedValues() {
		// Check the default value is null since there are no properties.
		assertNull(proxy.getPropertyAllowedValues("australia"));
		assertNull(proxy.getPropertyAllowedValues(""));
		assertNull(proxy.getPropertyAllowedValues(null));

		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}

		// Trying to get invalid properties should just return null.
		assertNull(proxy.getPropertyAllowedValues(""));
		assertNull(proxy.getPropertyAllowedValues(null));

		// Check the allowed property values as reported by the proxy. These are
		// compared with the fake proxy property's list of allowed values.
		for (ProxyProperty property : fakeProxy.properties) {
			List<String> expectedAllowedValues = ((FakeProxyFeature) property).allowedValues;
			Set<String> actualAllowedValues = proxy
					.getPropertyAllowedValues(property.name);
			// Check each value that should be an allowed value.
			for (String expectedAllowedValue : expectedAllowedValues) {
				assertTrue(
						"AbstractParaViewProxyTester error: "
								+ "The allowed values for property \""
								+ property.name + "\" did not contain \""
								+ expectedAllowedValue + "\".",
						actualAllowedValues.contains(expectedAllowedValue));
			}
		}

		return;
	}

	/**
	 * Checks the file, representation, and view IDs.
	 * 
	 * @see IParaViewProxy#getFileId()
	 * @see IParaViewProxy#getRepresentationId()
	 * @see IParaViewProxy#getViewId()
	 */
	@Test
	public void checkGetProxyIds() {
		// Check the default values are -1 since no connection is configured.
		assertEquals(-1, proxy.getFileId());
		assertEquals(-1, proxy.getRepresentationId());
		assertEquals(-1, proxy.getViewId());

		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}

		// The fake client should return the file, representation, and view IDs
		// of 1, 2, and 3, respectively.
		assertEquals(1, proxy.getFileId());
		assertEquals(2, proxy.getRepresentationId());
		assertEquals(3, proxy.getViewId());

		return;
	}

	@Test
	public void checkGetTimesteps() {
		// Set the times reported by the fake ParaView web client.
		fakeClient.setTimes(1.0, 2.0);

		// Check the default value is an empty collection.
		assertNotNull(proxy.getTimesteps());
		assertTrue(proxy.getTimesteps().isEmpty());

		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}

		// Check the resulting times.
		assertNotNull(proxy.getTimesteps());
		assertEquals(2, proxy.getTimesteps().size());
		assertEquals(1.0, proxy.getTimesteps().get(0), 1e-7);
		assertEquals(2.0, proxy.getTimesteps().get(1), 1e-7);

		return;
	}

	@Test
	public void checkGetURI() {
		// Check the default value is the URI provided to the constructor.
		assertEquals(testURI, proxy.getURI());
	}

	@Test
	public void checkOpen() {
		boolean futureValue;

		IVizConnection<IParaViewWebClient> nullConnection = null;
		IVizConnection<IParaViewWebClient> disconnection = new ParaViewConnection();

		// Attempting to open with an invalid connection should return false.
		futureValue = true;
		try {
			futureValue = proxy.open(nullConnection).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		assertFalse(futureValue);

		// Attempting to open with a valid connection that is not connected
		// should return false.
		futureValue = true;
		try {
			futureValue = proxy.open(disconnection).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		assertFalse(futureValue);

		// Attempting to open with a valid connection that is connected should
		// return true.
		futureValue = false;
		try {
			futureValue = proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		assertTrue(futureValue);
		
		return;
	}

	@Test
	public void checkSetFeature() {
		boolean futureValue;

		String feature = "europe";
		String value = "madrid";
		String invalidProperty = "world";
		String invalidValue = "pacific";

		// Attempting to set a valid property should return false because no
		// properties are configured.
		futureValue = true;
		try {
			futureValue = proxy.setFeature(feature, value).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		assertFalse(futureValue);

	}

	@Test
	public void checkSetProperty() {
		boolean futureValue;

		String property = "africa";
		String value = "abuja";
		String invalidProperty = "world";
		String invalidValue = "pacific";

		// Attempting to set a valid property should return false because no
		// properties are configured.
		futureValue = true;
		try {
			futureValue = proxy.setProperty(property, value).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		assertFalse(futureValue);
		
		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}
		
		// TODO
		
		return;
	}

	@Test
	public void checkSetProperties() {
		int futureValue;
		Map<String, String> properties;

		// Configure some properties that can be set.
		properties = new HashMap<String, String>();
		properties.put("africa", "abuja");
		properties.put("world", null);
		properties.put("asia", "seoul");
		properties.put("australia", "sydney");

		// Attempting to set the properties with no properties configured should
		// return 0 (for 0 properties changed).
		futureValue = -1;
		try {
			futureValue = proxy.setProperties(properties).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		assertEquals(0, futureValue);
		
		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}
		
		// TODO
		
		return;
	}

	@Test
	public void checkSetTimestep() {
		boolean futureValue;

		// Attempting to set the timestep with no connection should return
		// false.
		futureValue = true;
		try {
			futureValue = proxy.setTimestep(1).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		assertFalse(futureValue);
		
		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}
		
		// TODO
		
		return;
	}

}
