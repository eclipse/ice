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
package org.eclipse.ice.viz.service.paraview.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.viz.service.connections.paraview.ParaViewConnectionAdapter;
import org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.kitware.vtk.web.VtkWebClient;

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

	private FakeVtkWebClient fakeClient;
	private ParaViewConnectionAdapter connection;

	/**
	 * Initializes the {@link #proxy}, {@link #fakeProxy}, and {@link #testURI}.
	 */
	@Before
	public void beforeEachTest() {

		// Initialize the proxy with a test URI.
		testURI = createTestURI("go-go-gadget-extension");
		fakeProxy = new FakeParaViewProxy(testURI);
		proxy = fakeProxy;

		// Add some features.
		fakeProxy.features.put("europe", new String[] { "berlin", "madrid",
				"paris", "london", "zagreb" });
		fakeProxy.features.put("north america", new String[] { "ottawa",
				"mexico city", "havanna", "san salvador" });
		// Add some properties.
		fakeProxy.properties.put("south america", new String[] { "bogota",
				"brasilia", "caracas", "buenos aires" });
		fakeProxy.properties.put("africa", new String[] { "johannesburg",
				"cairo", "abuja", "djibouti" });
		fakeProxy.properties.put("asia", new String[] { "ulaanbaatar",
				"beijing", "tokyo", "seoul", "new delhi" });
		fakeProxy.properties.put("australia", new String[] { "canberra" });

		// Set up the fake client.
		fakeClient = new FakeVtkWebClient();

		// Establish a valid ParaView connection that is connected.
		connection = new ParaViewConnectionAdapter() {
			@Override
			protected VtkWebClient openConnection() {
				// Point the connection to localhost.
				setConnectionProperty("host", "localhost");
				// Return the fake client.
				fakeClient.connect("localhost");
				return fakeClient;
			}
		};
		connection.connect(true);

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
	 * Checks that {@link AbstractParaViewProxy#getURI()} returns the same URI
	 * as was passed to it.
	 */
	@Test
	public void checkURI() {

		final URI nullURI = null;

		// Check that the URI returned is the same as the one passed into the
		// hidden constructor.
		assertEquals(testURI, proxy.getURI());

		// Trying to use a null URI should throw a NullPointerException.
		try {
			fakeProxy = new FakeParaViewProxy(nullURI);
			fail("AbstractParaViewProxyTester error: "
					+ "A NullPointerException was not thrown when constructed "
					+ "with a null URI.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		return;
	}

	/**
	 * Checks that {@link AbstractParaViewProxy#open(ParaViewConnectionAdapter)}
	 * throws exceptions when the arguments are invalid. Also checks that it
	 * correctly calls the implemented open operation.
	 */
	@Test
	public void checkOpen() {

		final ParaViewConnectionAdapter nullConnection = null;

		// Although the client returns nothing (and thus opening fails), the
		// client and URI should pass initial checks, and the open
		// implementation should be called.
		assertFalse(proxy.open(connection));
		assertTrue(fakeProxy.openImplCalled.getAndSet(false));

		// Set a valid connection that is not connected. An exception should not
		// be thrown, but the return value should be false.
		connection = new ParaViewConnectionAdapter();
		assertFalse(proxy.open(connection));
		assertFalse(fakeProxy.openImplCalled.get());

		// Trying to use a null connection should throw an NPE when opening.
		try {
			proxy.open(nullConnection);
			fail("AbstractParaViewProxyTester error: "
					+ "A NullPointerException was not thrown when opened with "
					+ "a null connection.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}
		assertFalse(fakeProxy.openImplCalled.get());

		// TODO Add a test that checks a URI for a different host.
		// TODO Add a test that checks a URI for the same host but specified
		// differently (e.g. FQDN vs IP address).

		return;
	}

	/**
	 * Checks that the abstract implementation for opening a ParaView file works
	 * when it should and gracefully fails when the connection is bad.
	 */
	@Test
	public void checkOpenImplementation() {
		// Add a test response for creating a view. This is required when
		// "opening" the proxy's file.
		fakeClient.responseMap.put("createView", new Callable<JsonObject>() {
			@Override
			public JsonObject call() throws Exception {
				JsonObject response = new JsonObject();
				response.add("proxyId", new JsonPrimitive(0));
				response.add("viewId", new JsonPrimitive(1));
				response.add("repId", new JsonPrimitive(2));
				return response;
			}
		});

		// Initially, the file, view, and representation IDs should be -1.
		assertEquals(-1, fakeProxy.getFileId());
		assertEquals(-1, fakeProxy.getViewId());
		assertEquals(-1, fakeProxy.getRepresentationId());

		// Set a valid connection that is connected. An exception should not be
		// thrown, and the return value should be true.
		assertTrue(proxy.open(connection));

		// Check that the ParaView IDs were set.
		assertEquals(0, fakeProxy.getFileId());
		assertEquals(1, fakeProxy.getViewId());
		assertEquals(2, fakeProxy.getRepresentationId());

		// Set the same valid, open connection again. It should just return
		// true.
		assertTrue(proxy.open(connection));

		// Simulate a failed request (RPC returns a failure).
		fakeClient.responseMap.put("createView", new Callable<JsonObject>() {
			@Override
			public JsonObject call() throws Exception {
				JsonObject response = new JsonObject();
				response.add("success", new JsonPrimitive(false));
				response.add("error", new JsonPrimitive("Simulated error."));
				return response;
			}
		});
		// Opening should return false.
		assertFalse(proxy.open(connection));

		// Simulate a failed request (RPC returns incomplete response).
		fakeClient.responseMap.put("createView", new Callable<JsonObject>() {
			@Override
			public JsonObject call() throws Exception {
				JsonObject response = new JsonObject();
				response.add("proxyId", new JsonPrimitive(10));
				response.add("viewId", new JsonPrimitive(11));
				// repId is omitted... which is an error.
				return response;
			}
		});
		// Opening should return false.
		assertFalse(proxy.open(connection));

		// Simulate a connection error.
		fakeClient.responseMap.put("createView", new Callable<JsonObject>() {
			@Override
			public JsonObject call() throws Exception {
				throw new InterruptedException();
			}
		});
		// Opening should return false.
		assertFalse(proxy.open(connection));

		// The file, view, and representation IDs should remain unchanged.
		assertEquals(0, fakeProxy.getFileId());
		assertEquals(1, fakeProxy.getViewId());
		assertEquals(2, fakeProxy.getRepresentationId());

		return;
	}

	/**
	 * Checks values returned by
	 * {@link AbstractParaViewProxy#getFeatureCategories()} and
	 * {@link AbstractParaViewProxy#getFeatures(String)} as well as exceptions
	 * thrown by the latter method.
	 */
	@Test
	public void checkFeatures() {

		final String nullString = null;

		Set<String> categorySet;
		Set<String> featureSet;

		// Since we know the expected categories and features from the fake
		// proxy's construction, we can just iterate over the maps and sets and
		// make our comparisons.

		// Check the set of categories.
		categorySet = proxy.getFeatureCategories();
		// Check the overall size first.
		assertNotNull(categorySet);
		assertEquals(fakeProxy.features.size(), categorySet.size());
		// Now check each category's features against the expected features.
		for (Entry<String, String[]> entry : fakeProxy.features.entrySet()) {
			// Get the expected category and features.
			String category = entry.getKey();
			String[] features = entry.getValue();

			// Get the set of features for the category, then check its size and
			// content.
			assertTrue(categorySet.contains(category));
			featureSet = proxy.getFeatures(category);
			assertNotNull(featureSet);
			assertEquals(features.length, featureSet.size());
			for (String feature : features) {
				assertTrue(featureSet.contains(feature));
			}
		}

		// Check that new, equivalent sets are returned for both the categories
		// and each category's set of features.
		assertNotSame(categorySet, proxy.getFeatureCategories());
		assertEquals(categorySet, proxy.getFeatureCategories());
		for (String category : categorySet) {
			featureSet = proxy.getFeatures(category);
			assertNotSame(featureSet, proxy.getFeatures(category));
			assertEquals(featureSet, proxy.getFeatures(category));
		}

		// Check that manipulating the returned set of categories or features
		// does not affect the proxy's underlying categories or features.
		proxy.getFeatureCategories().clear();
		categorySet = proxy.getFeatureCategories();
		for (Entry<String, String[]> entry : fakeProxy.features.entrySet()) {
			// Get the expected category and features.
			String category = entry.getKey();
			String[] features = entry.getValue();

			assertTrue(categorySet.contains(category));
			// Try clearing the category's features.
			proxy.getFeatures(category).clear();
			// The feature set for the category should not have changed.
			featureSet = proxy.getFeatures(category);
			assertNotNull(featureSet);
			assertEquals(features.length, featureSet.size());
			for (String feature : features) {
				assertTrue(featureSet.contains(feature));
			}
		}

		// Trying to get the features for a null category should throw an NPE.
		try {
			proxy.getFeatures(nullString);
			fail("AbstractParaViewProxyTester error: "
					+ "When passed a null category, " + "getFeatures(String) "
					+ "should throw a NullPointerException.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		// Trying to get the features for an invalid category should throw an
		// IllegalArgumentException.
		try {
			proxy.getFeatures("antarctica");
			fail("AbstractParaViewProxyTester error: "
					+ "When passed an invalid category, "
					+ "getFeatures(String) "
					+ "should throw an IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}

		return;
	}

	/**
	 * Checks that the current feature can be set by calling
	 * {@link AbstractParaViewProxy#setFeature(String, String)} and that the
	 * appropriate exceptions are thrown based on the supplied input.
	 */
	@Test
	public void checkSetFeature() {

		final String nullString = null;
		String validCategory;
		String validFeature;

		Set<String> categorySet;
		Set<String> featureSet;

		// Check that all valid categories/features can be set.
		categorySet = proxy.getFeatureCategories();
		for (String category : categorySet) {
			featureSet = proxy.getFeatures(category);
			for (String feature : featureSet) {
				// The first call should successfully set the feature.
				assertTrue(proxy.setFeature(category, feature));
				// The second call should return false, because the feature was
				// already set.
				assertFalse(proxy.setFeature(category, feature));

				// The fake proxy's category and feature should have been set.
				assertEquals(category, fakeProxy.currentCategory);
				assertEquals(feature, fakeProxy.currentFeature);
			}
		}

		// Get the first valid category/feature from the proxy.
		validCategory = proxy.getFeatureCategories().iterator().next();
		validFeature = proxy.getFeatures(validCategory).iterator().next();

		// Trying to set the feature using a null category should throw an NPE.
		try {
			proxy.setFeature(nullString, validFeature);
			fail("AbstractParaViewProxyTester error: "
					+ "When passed a null category, "
					+ "setFeature(String, String) "
					+ "should throw a NullPointerException.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		// Trying to set the feature using a null feature should throw an NPE.
		try {
			proxy.setFeature(validCategory, nullString);
			fail("AbstractParaViewProxyTester error: "
					+ "When passed a null feature, " + ""
					+ "setFeature(String, String) "
					+ "should throw a NullPointerException.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		// Trying to set the feature for an invalid category should throw an
		// IllegalArgumentException.
		try {
			proxy.setFeature("antarctica", validFeature);
			fail("AbstractParaViewProxyTester error: "
					+ "When passed an invalid category, "
					+ "setFeature(String, String) "
					+ "should throw a IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}

		// Trying to set the feature to an invalid feature should throw an
		// IllegalArgumentException.
		try {
			proxy.setFeature(validCategory, "international space station");
			fail("AbstractParaViewProxyTester error: "
					+ "When passed an invalid feature, "
					+ "setFeature(String, String) "
					+ "should throw a IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}

		return;
	}

	/**
	 * Checks values returned by {@link AbstractParaViewProxy#getProperties()}.
	 */
	@Test
	public void checkProperties() {

		final String nullString = null;

		Set<String> propertySet;
		Set<String> propertyValueSet;

		// Since we know the expected properties and values from the fake
		// proxy's construction, we can just iterate over the maps and sets and
		// make our comparisons.

		// Check the set of properties.
		propertySet = proxy.getProperties();
		// Check the overall size first.
		assertNotNull(propertySet);
		assertEquals(fakeProxy.properties.size(), propertySet.size());
		// Now check each property's values against the expected values.
		for (Entry<String, String[]> entry : fakeProxy.properties.entrySet()) {
			// Get the expected property and values.
			String property = entry.getKey();
			String[] values = entry.getValue();

			// Get the set of values for the property, then check its size and
			// content.
			assertTrue(propertySet.contains(property));
			propertyValueSet = proxy.getPropertyValues(property);
			assertNotNull(propertyValueSet);
			assertEquals(values.length, propertyValueSet.size());
			for (String value : values) {
				assertTrue(propertyValueSet.contains(value));
			}
		}

		// Check that new, equivalent sets are returned for both the properties
		// and each property's set of allowed values.
		assertNotSame(propertySet, proxy.getProperties());
		assertEquals(propertySet, proxy.getProperties());
		for (String property : propertySet) {
			propertyValueSet = proxy.getPropertyValues(property);
			assertNotSame(propertyValueSet, proxy.getPropertyValues(property));
			assertEquals(propertyValueSet, proxy.getPropertyValues(property));
		}

		// Check that manipulating the returned set of properties or values
		// does not affect the proxy's underlying properties or values.
		proxy.getProperties().clear();
		propertySet = proxy.getProperties();
		for (Entry<String, String[]> entry : fakeProxy.properties.entrySet()) {
			// Get the expected property and values.
			String property = entry.getKey();
			String[] values = entry.getValue();

			assertTrue(propertySet.contains(property));
			// Try clearing the property's values.
			proxy.getPropertyValues(property).clear();
			// The value set for the property should not have changed.
			propertyValueSet = proxy.getPropertyValues(property);
			assertNotNull(propertyValueSet);
			assertEquals(values.length, propertyValueSet.size());
			for (String value : values) {
				assertTrue(propertyValueSet.contains(value));
			}
		}

		// Trying to get the values for a null property should throw an NPE.
		try {
			proxy.getFeatures(nullString);
			fail("AbstractParaViewProxyTester error: "
					+ "When passed a null property, "
					+ "getPropertyValues(String) "
					+ "should throw a NullPointerException.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		// Trying to get the values for an invalid property should throw an
		// IllegalArgumentException.
		try {
			proxy.getFeatures("antarctica");
			fail("AbstractParaViewProxyTester error: "
					+ "When passed an invalid property, "
					+ "getPropertyValues(String) "
					+ "should throw an IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}

		return;
	}

	/**
	 * Checks that a single property can be set via
	 * {@link AbstractParaViewProxy#setProperty(String, String)} and that the
	 * appropriate exceptions are thrown based on the supplied input.
	 */
	@Test
	public void checkSetProperty() {

		final String nullString = null;
		String validProperty;
		String validValue;

		Set<String> propertySet;
		Set<String> valueSet;

		// Check that all valid properties/values can be set.
		propertySet = proxy.getProperties();
		for (String property : propertySet) {
			valueSet = proxy.getPropertyValues(property);
			for (String value : valueSet) {
				// The first call should successfully set the property.
				assertTrue(proxy.setProperty(property, value));
				// The second call should return false, because the property was
				// already set.
				assertFalse(proxy.setProperty(property, value));
			}
		}

		// Get the first valid property/value from the proxy.
		validProperty = proxy.getProperties().iterator().next();
		validValue = proxy.getPropertyValues(validProperty).iterator().next();

		// Trying to set the property value using a null property should throw
		// an NPE.
		try {
			proxy.setProperty(nullString, validValue);
			fail("AbstractParaViewProxyTester error: "
					+ "When passed a null property, "
					+ "setProperty(String, String) "
					+ "should throw a NullPointerException.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		// Trying to set the property value using a null value should throw an
		// NPE.
		try {
			proxy.setProperty(validProperty, nullString);
			fail("AbstractParaViewProxyTester error: "
					+ "When passed a null value, " + ""
					+ "setProperty(String, String) "
					+ "should throw a NullPointerException.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		// Trying to set the property value for an invalid property should throw
		// an IllegalArgumentException.
		try {
			proxy.setProperty("antarctica", validValue);
			fail("AbstractParaViewProxyTester error: "
					+ "When passed an invalid property, "
					+ "setProperty(String, String) "
					+ "should throw a IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}

		// Trying to set the property value to an invalid value should throw an
		// IllegalArgumentException.
		try {
			proxy.setProperty(validProperty, "international space station");
			fail("AbstractParaViewProxyTester error: "
					+ "When passed an invalid value, "
					+ "setProperty(String, String) "
					+ "should throw a IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}

		return;
	}

	/**
	 * Checks that a set of properties can be set via
	 * {@link AbstractParaViewProxy#setProperties(java.util.Map)} and that the
	 * appropriate exceptions are thrown based on the supplied input.
	 */
	@Test
	public void checkSetProperties() {

		final String nullString = null;
		String validProperty;
		String validValue;

		// Set up a map of properties that will be set on the proxy.
		final Map<String, String> newProperties = new HashMap<String, String>();
		final Map<String, String> newPropertiesCopy;

		// Add 3 properties where 2 of them are new values and one is old.
		if ("djibouti".equals(fakeProxy.currentProperties.get("africa"))) {
			newProperties.put("africa", "abuja");
		} else {
			newProperties.put("africa", "djibouti");
		}
		if ("tokyo".equals(fakeProxy.currentProperties.get("asia"))) {
			newProperties.put("asia", "beijing");
		} else {
			newProperties.put("asia", "tokyo");
		}
		newProperties.put("australia", "canberra"); // The old value.

		// Get the first valid property/value from the proxy.
		validProperty = proxy.getProperties().iterator().next();
		validValue = proxy.getPropertyValues(validProperty).iterator().next();

		// If any of the new property names are null, an NPE will be thrown.
		newProperties.put(nullString, validValue);
		try {
			proxy.setProperties(newProperties);
			fail("AbstractParaViewProxyTester error: "
					+ "When passed a null property, "
					+ "setProperties(Map<String, String>) "
					+ "should throw a NullPointerException.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}
		// Remove the property that we attempted to update.
		newProperties.remove(nullString);

		// If any of the new property values are null, an NPE will be thrown.
		newProperties.put(validProperty, nullString);
		try {
			proxy.setProperties(newProperties);
			fail("AbstractParaViewProxyTester error: "
					+ "When passed a null value, " + ""
					+ "setProperties(Map<String, String>) "
					+ "should throw a NullPointerException.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}
		// Remove the property that we attempted to update.
		newProperties.remove(validProperty);

		// If any of the new property names are invalid, an
		// IllegalArgumentException will be thrown.
		newProperties.put("antarctica", validValue);
		try {
			proxy.setProperties(newProperties);
			fail("AbstractParaViewProxyTester error: "
					+ "When passed an invalid property, "
					+ "setProperties(Map<String, String>) "
					+ "should throw a IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}
		// Remove the property that we attempted to update.
		newProperties.remove("antarctica");

		// If any of the new property values are invalid, an
		// IllegalArgumentException will be thrown.
		newProperties.put(validProperty, "international space station");
		try {
			proxy.setProperties(newProperties);
			fail("AbstractParaViewProxyTester error: "
					+ "When passed an invalid value, "
					+ "setProperties(Map<String, String>) "
					+ "should throw a IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}
		// Remove the property that we attempted to update.
		newProperties.remove(validProperty);

		// Make sure the remaining valid properties were never set by sending
		// the new properties minus all of the invalid properties.
		newPropertiesCopy = new HashMap<String, String>(newProperties);
		assertEquals(2, proxy.setProperties(newProperties));

		// Since the properties were updated, sending the same set of properties
		// should return 0 (for 0 properties changed).
		assertEquals(0, proxy.setProperties(newPropertiesCopy));

		return;
	}

	/**
	 * Creates a simple URI for the provided extension.
	 * 
	 * @param extension
	 *            The extension for the test URI file. This file probably will
	 *            not actually exist. If {@code null}, then the file will have
	 *            no extension.
	 * @return A correctly formed URI with the provided extension.
	 */
	private URI createTestURI(String extension) {
		String filename = (extension != null ? "kung_fury." + extension
				: "future_cop");
		return new File(filename).toURI();
	}

	private class FakeParaViewProxy extends AbstractParaViewProxy {

		/**
		 * The current category for the feature, or {@code null} if one is not
		 * set. This will be changed only if the {@link AbstractParaViewProxy}
		 * successfully changes the category/feature.
		 */
		public String currentCategory = null;
		/**
		 * The current feature, or {@code null} if one is not set. This will be
		 * changed only if the {@link AbstractParaViewProxy} successfully
		 * changes the category/feature.
		 */
		public String currentFeature = null;
		/**
		 * The current properties set for the proxy. This will be changed only
		 * if {@link AbstractParaViewProxy} successfully updates a property.
		 */
		public final Map<String, String> currentProperties;

		/**
		 * A map of supported categories and features for the fake proxy. This
		 * should be set at construction time.
		 */
		public final Map<String, String[]> features;
		/**
		 * A map of supported properties and their allowed values for the fake
		 * proxy. This should be set at construction time.
		 */
		public final Map<String, String[]> properties;

		public final AtomicBoolean openImplCalled = new AtomicBoolean();

		/**
		 * The default constructor. Used to access the parent class' hidden
		 * constructor (after all, it is an abstract class).
		 * 
		 * @param uri
		 *            The URI for the ParaView-supported file.
		 * @throws NullPointerException
		 *             If the specified URI is null.
		 */
		public FakeParaViewProxy(URI uri) throws NullPointerException {
			super(uri);

			features = new HashMap<String, String[]>();
			properties = new HashMap<String, String[]>();

			currentProperties = new HashMap<String, String>();
		}

		/**
		 * Sets the current category and feature if the
		 * {@link AbstractParaViewProxy} implementation returns true.
		 */
		@Override
		public boolean setFeature(String category, String feature)
				throws NullPointerException, IllegalArgumentException {
			boolean changed = super.setFeature(category, feature);
			if (changed) {
				this.currentCategory = category;
				this.currentFeature = feature;
			}
			return changed;
		}

		/**
		 * Exposes the parent class' operation.
		 */
		public int getFileId() {
			return super.getFileId();
		}

		/**
		 * Exposes the parent class' operation.
		 */
		public int getViewId() {
			return super.getViewId();
		}

		/**
		 * Exposes the parent class' operation.
		 */
		public int getRepresentationId() {
			return super.getRepresentationId();
		}

		public boolean openImpl(VtkWebClient client, String fullPath) {
			openImplCalled.set(true);
			return super.openImpl(client, fullPath);
		}
	}
}