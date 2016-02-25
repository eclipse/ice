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
import static org.junit.Assert.assertNotEquals;
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

import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.connections.VizConnection;
import org.eclipse.eavp.viz.service.paraview.proxy.AbstractParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyFeature;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyProperty;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyProperty.PropertyType;
import org.eclipse.eavp.viz.service.paraview.test.FakeParaViewWebClient;
import org.eclipse.eavp.viz.service.paraview.test.TestUtils;
import org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient;
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
	private IVizConnection<IParaViewWebClient> connection;

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
		fakeFeature = new FakeProxyFeature("europe", 2, PropertyType.DISCRETE);
		fakeFeature.propertyName = "eu";
		fakeFeature.initialValue = "london";
		fakeFeature.setAllowedValues("berlin", "madrid", "paris", "london",
				"zagreb");
		fakeProxy.features.add(fakeFeature);
		// Add a "north america" feature with the initial value of "havanna".
		fakeFeature = new FakeProxyFeature("north america", 3,
				PropertyType.DISCRETE);
		fakeFeature.propertyName = "na";
		fakeFeature.initialValue = "havanna";
		fakeFeature.setAllowedValues("ottawa", "mexico city", "havanna",
				"san salvador");
		fakeProxy.features.add(fakeFeature);
		// Add some properties.
		fakeFeature = new FakeProxyFeature("south america", 4,
				PropertyType.DISCRETE);
		fakeFeature.propertyName = "sa";
		fakeFeature.initialValue = "caracas";
		fakeFeature.setAllowedValues("bogota", "brasilia", "caracas",
				"buenos aires");
		fakeProxy.properties.add(fakeFeature);
		fakeFeature = new FakeProxyFeature("africa", 5, PropertyType.DISCRETE);
		fakeFeature.propertyName = "af";
		fakeFeature.initialValue = "johannesburg";
		fakeFeature.setAllowedValues("johannesburg", "cairo", "abuja",
				"djibouti");
		fakeProxy.properties.add(fakeFeature);
		fakeFeature = new FakeProxyFeature("asia", 6, PropertyType.DISCRETE);
		fakeFeature.propertyName = "as";
		fakeFeature.initialValue = "tokyo";
		fakeFeature.setAllowedValues("ulaanbaatar", "beijing", "tokyo", "seoul",
				"new delhi");
		fakeProxy.properties.add(fakeFeature);
		fakeFeature = new FakeProxyFeature("australia", 7,
				PropertyType.DISCRETE);
		fakeFeature.propertyName = "au";
		fakeFeature.initialValue = "canberra";
		fakeFeature.setAllowedValues("canberra");
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
		try {
			((VizConnection<?>) connection).connect().get();
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

	/**
	 * Checks that the feature categories are set properly before and after the
	 * proxy's connection is set.
	 */
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

	/**
	 * Checks that the features are set properly before and after the proxy's
	 * connection is set.
	 */
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

	/**
	 * Checks that the map of current property values is set properly before and
	 * after the proxy's connection is set.
	 */
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

	/**
	 * Checks that each property is set properly before and after the proxy's
	 * connection is set.
	 */
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

	/**
	 * Checks that each property's allowed values are set properly before and
	 * after the proxy's connection is set.
	 */
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
	 * Checks that the file, representation, and view IDs are set properly
	 * before and after the proxy's connection is set.
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

	/**
	 * Checks that the timesteps are set properly before and after the proxy's
	 * connection is set.
	 */
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

	/**
	 * Checks that the proxy correctly fails to open invalid connections but
	 * successfully opens valid, connected connections. Its return value should
	 * reflect the success of the operation.
	 */
	@Test
	public void checkOpen() {

		IVizConnection<IParaViewWebClient> nullConnection = null;
		// Create a disconnected connection.
		IVizConnection<IParaViewWebClient> disconnection = new VizConnection<IParaViewWebClient>() {
			@Override
			protected IParaViewWebClient connectToWidget() {
				return null;
			}

			@Override
			protected boolean disconnectFromWidget(IParaViewWebClient widget) {
				return false;
			}
		};

		try {
			// Attempting to open with a bad connection should return false.
			assertFalse(proxy.open(nullConnection).get());
			assertFalse(proxy.open(disconnection).get());
			assertTrue(proxy.open(connection).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception while setting the connection.");
		}

		return;
	}

	/**
	 * Checks the set feature operation on the proxy as well as its return
	 * value.
	 */
	@Test
	public void checkSetFeature() {

		String feature = "europe";
		String value = "madrid";
		String invalidFeature = "world";
		String invalidValue = "pacific";
		String nullString = null;

		// Attempting to set a valid property should return false because no
		// properties are configured.
		try {
			assertFalse(proxy.setFeature(feature, value).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception thrown while setting features.");
		}

		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}

		// Setting a valid feature the first time should return true, but
		// afterwards setting the same feature should return false.
		try {
			assertTrue(proxy.setFeature(feature, value).get());
			assertFalse(proxy.setFeature(feature, value).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception thrown while setting features.");
		}

		// Attempting to set an invalid feature always false.
		try {
			assertFalse(proxy.setFeature(invalidFeature, invalidValue).get());
			assertFalse(proxy.setFeature(feature, invalidValue).get());
			assertFalse(proxy.setFeature(nullString, nullString).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception thrown while setting invalid features.");
		}

		return;
	}

	/**
	 * Checks the property set operation for the proxy and the return value of
	 * the operation.
	 */
	@Test
	public void checkSetProperty() {

		String property = "africa";
		String value = "abuja";
		String invalidProperty = "world";
		String invalidValue = "pacific";
		String nullString = null;

		// Attempting to set a valid property should return false because no
		// properties are configured.
		try {
			assertFalse(proxy.setProperty(property, value).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception thrown while setting a property.");
		}

		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}

		// Check the initial values of the property.
		assertNotEquals("abuja", proxy.getProperty("africa"));

		// Setting a valid property the first time should return true, but
		// afterwards setting the same property to the same value should return
		// false.
		try {
			assertTrue(proxy.setProperty(property, value).get());
			assertFalse(proxy.setProperty(property, value).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception thrown while setting a property.");
		}

		// Check the value of the property after the change.
		assertEquals("abuja", proxy.getProperty("africa"));

		// Attempting to set an invalid property always false.
		try {
			assertFalse(proxy.setProperty(invalidProperty, invalidValue).get());
			assertFalse(proxy.setProperty(property, invalidValue).get());
			assertFalse(proxy.setProperty(nullString, nullString).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception thrown while setting an invalid property.");
		}

		return;
	}

	/**
	 * Checks the bulk property set operation for the proxy and the return value
	 * of the operation.
	 */
	@Test
	public void checkSetProperties() {
		Map<String, String> properties;

		// Configure some properties that can be set.
		properties = new HashMap<String, String>();
		properties.put("africa", "abuja"); // valid value
		properties.put("world", null); // invalid name and value
		properties.put("asia", "seoul"); // valid value
		properties.put("australia", "sydney"); // invalid value
		properties.put("south america", "caracas"); // same value
		int expectedUpdateCount = 2;

		// Attempting to set the properties with no properties configured should
		// return 0 (for 0 properties changed).
		try {
			assertEquals(0, (int) proxy.setProperties(properties).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception thrown while setting properties.");
		}

		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}

		// Check the initial values of the properties.
		assertNotEquals("abuja", proxy.getProperty("africa"));
		assertNotEquals("seoul", proxy.getProperty("asia"));
		assertNotEquals("sydney", proxy.getProperty("australia"));
		assertEquals("caracas", proxy.getProperty("south america"));

		// Attempting to set the properties should return the expected update
		// count the first time, but 0 the second time because all of the
		// properties have been set.
		try {
			assertEquals(expectedUpdateCount,
					(int) proxy.setProperties(properties).get());
			assertEquals(0, (int) proxy.setProperties(properties).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception thrown while setting properties.");
		}

		// Check the values of the properties now that they have been set.
		assertEquals("abuja", proxy.getProperty("africa")); // changed
		assertEquals("seoul", proxy.getProperty("asia")); // changed
		assertNotEquals("sydney", proxy.getProperty("australia")); // invalid...
		assertEquals("caracas", proxy.getProperty("south america"));

		// Attempting to set the properties with a null or empty map should just
		// return 0.
		try {
			assertEquals(0, (int) proxy
					.setProperties(new HashMap<String, String>(0)).get());
			assertEquals(0, (int) proxy.setProperties(null).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception thrown while setting invalid properties.");
		}

		return;
	}

	/**
	 * Checks that the timestep for the proxy can be set and the return value of
	 * the operation.
	 */
	@Test
	public void checkSetTimestep() {
		// Set the times reported by the fake ParaView web client.
		fakeClient.setTimes(1.0, 2.0);

		// Attempting to set the timestep with no connection should return
		// false.
		try {
			assertFalse(proxy.setTimestep(1).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception thrown while setting the timestep.");
		}

		// Set the connection.
		try {
			proxy.open(connection).get();
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Failed to set the connection.");
		}

		// Setting a valid timestep the first time should return true. The
		// second time it should also return true, as the timestep is currently
		// set to that value.
		try {
			assertTrue(proxy.setTimestep(1).get());
			assertFalse(proxy.setTimestep(1).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception thrown while setting the timestep.");
		}

		// Attempting to set an invalid property always false.
		try {
			assertFalse(proxy.setTimestep(-1).get());
			assertFalse(proxy.setTimestep(2).get());
		} catch (InterruptedException | ExecutionException e) {
			fail("AbstractParaViewProxyTester error: "
					+ "Exception thrown while setting an invalid timestep.");
		}

		return;
	}

}
