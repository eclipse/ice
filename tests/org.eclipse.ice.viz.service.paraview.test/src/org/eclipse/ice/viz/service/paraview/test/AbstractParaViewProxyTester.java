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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.viz.service.connections.paraview.ParaViewConnectionAdapter;
import org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IProxyProperty;
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

	// TODO Revisit these tests...
	// TODO Check that the initial category, feature, and properties are set.

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
	private FakeVtkWebClient fakeClient;

	/**
	 * The connection adapter that should be used by the proxy.
	 */
	private ParaViewConnectionAdapter connection;

	/**
	 * Initializes the {@link #proxy}, {@link #fakeProxy}, and {@link #testURI}.
	 */
	@Before
	public void beforeEachTest() {

		// Initialize the proxy with a test URI.
		testURI = TestUtils.createURI("go-go-gadget-extension");
		fakeProxy = new FakeParaViewProxy(testURI);
		proxy = fakeProxy;

		// Add some features.
		fakeProxy.features.put("europe",
				createSet("berlin", "madrid", "paris", "london", "zagreb"));
		fakeProxy.features.put("north america",
				createSet("ottawa", "mexico city", "havanna", "san salvador"));
		// Add some properties.
		fakeProxy.properties.put("south america",
				createSet("bogota", "brasilia", "caracas", "buenos aires"));
		fakeProxy.properties.put("africa",
				createSet("johannesburg", "cairo", "abuja", "djibouti"));
		fakeProxy.properties.put(
				"asia",
				createSet("ulaanbaatar", "beijing", "tokyo", "seoul",
						"new delhi"));
		fakeProxy.properties.put("australia", createSet("canberra"));

		// Set up the fake client.
		fakeClient = new FakeVtkWebClient();

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
	public void checkOpen() throws InterruptedException, ExecutionException {

		final ParaViewConnectionAdapter nullConnection = null;
		// Create a remote URI and remote connection for testing.
		final ParaViewConnectionAdapter remoteConnection = new ParaViewConnectionAdapter() {
			@Override
			protected VtkWebClient openConnection() {
				// Point the connection to localhost.
				setConnectionProperty("host", "remoteHost");
				// Return the fake client.
				fakeClient.connect("remoteHost");
				return fakeClient;
			}
		};
		URI remoteURI = TestUtils.createURI("someExtension", "remoteHost");
		remoteConnection.connect(true);
		FakeParaViewProxy fakeRemoteProxy;
		AbstractParaViewProxy remoteProxy;

		// ---- Can't open remote file with localhost, and vice versa ---- //
		// Test a local connection with a local file. The connection *can* be
		// set.
		assertTrue(proxy.open(connection).get());
		assertTrue(fakeProxy.openImplCalled.getAndSet(false));
		// The features and properties should have also been queried.
		assertTrue(fakeProxy.findFeaturesCalled.getAndSet(false));
		assertTrue(fakeProxy.findPropertiesCalled.getAndSet(false));

		// Test a remote connection with a local file. The connection *cannot*
		// be set.
		assertFalse(proxy.open(remoteConnection).get());
		assertFalse(fakeProxy.openImplCalled.get());
		assertFalse(fakeProxy.findFeaturesCalled.get());
		assertFalse(fakeProxy.findPropertiesCalled.get());

		// Test a local connection with a remote file. The connection *cannot*
		// be set.
		fakeRemoteProxy = new FakeParaViewProxy(remoteURI);
		remoteProxy = fakeRemoteProxy;
		assertFalse(remoteProxy.open(connection).get());
		assertFalse(fakeRemoteProxy.openImplCalled.get());
		assertFalse(fakeRemoteProxy.findFeaturesCalled.get());
		assertFalse(fakeRemoteProxy.findPropertiesCalled.get());

		// Test a remote connection with a remote file (same host). The
		// connection *can* be set.
		assertTrue(remoteProxy.open(remoteConnection).get());
		assertTrue(fakeRemoteProxy.openImplCalled.getAndSet(false));
		assertTrue(fakeRemoteProxy.findFeaturesCalled.getAndSet(false));
		assertTrue(fakeRemoteProxy.findPropertiesCalled.getAndSet(false));

		// Rest a remote connection with a remote file (different host). The
		// connection *cannot* be set.
		remoteURI = TestUtils.createURI("fails", "diffRemoteHost");
		fakeRemoteProxy = new FakeParaViewProxy(remoteURI);
		remoteProxy = fakeRemoteProxy;
		assertFalse(remoteProxy.open(connection).get());
		assertFalse(fakeRemoteProxy.openImplCalled.get());
		assertFalse(fakeRemoteProxy.findFeaturesCalled.get());
		assertFalse(fakeRemoteProxy.findPropertiesCalled.get());
		// --------------------------------------------------------------- //

		// Set a valid connection that is not connected. An exception should not
		// be thrown, but the return value should be false.
		connection = new ParaViewConnectionAdapter();
		assertFalse(proxy.open(connection).get());
		assertFalse(fakeProxy.openImplCalled.get());
		// The features and properties should not have been queried.
		assertFalse(fakeProxy.findFeaturesCalled.get());
		assertFalse(fakeProxy.findPropertiesCalled.get());

		// Trying to use a null connection should throw an NPE when opening.
		try {
			proxy.open(nullConnection).get();
			fail("AbstractParaViewProxyTester error: "
					+ "A NullPointerException was not thrown when opened with "
					+ "a null connection.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}
		assertFalse(fakeProxy.openImplCalled.get());

		return;
	}

	/**
	 * Checks that the abstract implementation for opening a ParaView file works
	 * when it should and gracefully fails when the connection is bad.
	 */
	@Test
	public void checkOpenImplementation() throws InterruptedException,
			ExecutionException {

		// Initially, the file, view, and representation IDs should be -1.
		assertEquals(-1, fakeProxy.getFileId());
		assertEquals(-1, fakeProxy.getViewId());
		assertEquals(-1, fakeProxy.getRepresentationId());

		// Set a valid connection that is connected. An exception should not be
		// thrown, and the return value should be true.
		assertTrue(proxy.open(connection).get());

		// Check that the ParaView IDs were set.
		assertEquals(0, fakeProxy.getFileId());
		assertEquals(1, fakeProxy.getViewId());
		assertEquals(2, fakeProxy.getRepresentationId());

		// Set the same valid, open connection again. It should just return
		// true.
		assertTrue(proxy.open(connection).get());

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
		assertFalse(proxy.open(connection).get());

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
		assertFalse(proxy.open(connection).get());

		// Simulate a connection error.
		fakeClient.responseMap.put("createView", new Callable<JsonObject>() {
			@Override
			public JsonObject call() throws Exception {
				throw new InterruptedException();
			}
		});
		// Opening should return false.
		assertFalse(proxy.open(connection).get());

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

		// Open the proxy. We don't care about its return value, it just must be
		// opened before it finds features for the file.
		try {
			proxy.open(connection).get();
		} catch (NullPointerException | InterruptedException
				| ExecutionException e1) {
			e1.printStackTrace();
			fail("AbstractParaViewProxyTester error: " + "Thread interrupted!");
		}
		// The features should have been re-built.
		assertTrue(fakeProxy.findFeaturesCalled.getAndSet(false));

		// Since we know the expected categories and features from the fake
		// proxy's construction, we can just iterate over the maps and sets and
		// make our comparisons.

		// Check the set of categories.
		categorySet = proxy.getFeatureCategories();
		// Check the overall size first.
		assertNotNull(categorySet);
		assertEquals(fakeProxy.features.size(), categorySet.size());
		// Now check each category's features against the expected features.
		for (Entry<String, Set<String>> entry : fakeProxy.features.entrySet()) {
			// Get the expected category and features.
			String category = entry.getKey();
			Set<String> features = entry.getValue();

			// Get the set of features for the category, then check its size and
			// content.
			assertTrue(categorySet.contains(category));
			featureSet = proxy.getFeatures(category);
			assertNotNull(featureSet);
			assertEquals(features.size(), featureSet.size());
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
		for (Entry<String, Set<String>> entry : fakeProxy.features.entrySet()) {
			// Get the expected category and features.
			String category = entry.getKey();
			Set<String> features = entry.getValue();

			assertTrue(categorySet.contains(category));
			// Try clearing the category's features.
			proxy.getFeatures(category).clear();
			// The feature set for the category should not have changed.
			featureSet = proxy.getFeatures(category);
			assertNotNull(featureSet);
			assertEquals(features.size(), featureSet.size());
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
	public void checkSetFeature() throws InterruptedException,
			ExecutionException {

		final String nullString = null;
		String validCategory;
		String validFeature;

		Set<String> categorySet;
		Set<String> featureSet;

		// Open the proxy. We don't care about its return value, it just must be
		// opened before it finds features for the file.
		try {
			proxy.open(connection).get();
		} catch (NullPointerException | InterruptedException
				| ExecutionException e1) {
			e1.printStackTrace();
			fail("AbstractParaViewProxyTester error: " + "Thread interrupted!");
		}

		// Check that all valid categories/features can be set.
		categorySet = proxy.getFeatureCategories();
		for (String category : categorySet) {
			featureSet = proxy.getFeatures(category);
			boolean firstFeature = true;
			for (String feature : featureSet) {

				// If the client fails to render, then the category and feature
				// will not be set, although setFeatureImpl(...) will be called.
				fakeProxy.failToSetFeature = true;
				assertFalse(proxy.setFeature(category, feature).get());
				assertTrue(fakeProxy.setFeatureImplCalled.getAndSet(false));
				if (firstFeature) {
					assertNotEquals(category, fakeProxy.getCategory());
					firstFeature = false;
				}
				assertNotEquals(feature, fakeProxy.getFeature());

				// The first call should successfully set the feature.
				// setFeatureImpl(...) will also be called.
				fakeProxy.failToSetFeature = false;
				assertTrue(proxy.setFeature(category, feature).get());
				assertTrue(fakeProxy.setFeatureImplCalled.getAndSet(false));
				assertEquals(category, fakeProxy.getCategory());
				assertEquals(feature, fakeProxy.getFeature());

				// The second call should return false, because the feature was
				// already set. setFeatureImpl(...) should not be called.
				assertFalse(proxy.setFeature(category, feature).get());
				assertFalse(fakeProxy.setFeatureImplCalled.get());
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

		Map<String, String> propertySet;
		Set<String> propertyValueSet;

		// Open the proxy. We don't care about its return value, it just must be
		// opened before it finds properties for the file.
		try {
			proxy.open(connection).get();
		} catch (NullPointerException | InterruptedException
				| ExecutionException e1) {
			e1.printStackTrace();
			fail("AbstractParaViewProxyTester error: " + "Thread interrupted!");
		}
		// The properties should have been re-built.
		assertTrue(fakeProxy.findPropertiesCalled.getAndSet(false));

		// Since we know the expected properties and values from the fake
		// proxy's construction, we can just iterate over the maps and sets and
		// make our comparisons.

		// Check the set of properties.
		propertySet = proxy.getProperties();
		// Check the overall size first.
		assertNotNull(propertySet);
		assertEquals(fakeProxy.properties.size(), propertySet.size());
		// Now check each property's values against the expected values.
		for (Entry<String, Set<String>> entry : fakeProxy.properties.entrySet()) {
			// Get the expected property and values.
			String property = entry.getKey();
			Set<String> values = entry.getValue();

			// Get the set of values for the property, then check its size and
			// content.
			assertTrue(propertySet.containsKey(property));
			propertyValueSet = proxy.getPropertyAllowedValues(property);
			assertNotNull(propertyValueSet);
			assertEquals(values.size(), propertyValueSet.size());
			for (String value : values) {
				assertTrue(propertyValueSet.contains(value));
			}
		}

		// Check that new, equivalent sets are returned for both the properties
		// and each property's set of allowed values.
		assertNotSame(propertySet, proxy.getProperties());
		assertEquals(propertySet, proxy.getProperties());
		for (String property : propertySet.keySet()) {
			propertyValueSet = proxy.getPropertyAllowedValues(property);
			assertNotSame(propertyValueSet,
					proxy.getPropertyAllowedValues(property));
			assertEquals(propertyValueSet,
					proxy.getPropertyAllowedValues(property));
		}

		// Check that manipulating the returned set of properties or values
		// does not affect the proxy's underlying properties or values.
		proxy.getProperties().clear();
		propertySet = proxy.getProperties();
		for (Entry<String, Set<String>> entry : fakeProxy.properties.entrySet()) {
			// Get the expected property and values.
			String property = entry.getKey();
			Set<String> values = entry.getValue();

			assertTrue(propertySet.containsKey(property));
			// Try clearing the property's values.
			proxy.getPropertyAllowedValues(property).clear();
			// The value set for the property should not have changed.
			propertyValueSet = proxy.getPropertyAllowedValues(property);
			assertNotNull(propertyValueSet);
			assertEquals(values.size(), propertyValueSet.size());
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
	public void checkSetProperty() throws InterruptedException,
			ExecutionException {

		final String nullString = null;
		String validProperty;
		String validValue;

		Map<String, String> propertySet;
		Set<String> valueSet;

		// Open the proxy. We don't care about its return value, it just must be
		// opened before it finds properties for the file.
		try {
			proxy.open(connection).get();
		} catch (NullPointerException | InterruptedException
				| ExecutionException e1) {
			e1.printStackTrace();
			fail("AbstractParaViewProxyTester error: " + "Thread interrupted!");
		}

		// Check that all valid properties/values can be set.
		propertySet = proxy.getProperties();
		for (String property : propertySet.keySet()) {
			valueSet = proxy.getPropertyAllowedValues(property);
			for (String value : valueSet) {
				// If the client fails to update the property, then the property
				// value will not be set, although setPropertyImpl(...) will
				// still be called.
				fakeProxy.failToSetProperty = true;
				assertFalse(proxy.setProperty(property, value).get());
				assertTrue(fakeProxy.setPropertyImplCalled.getAndSet(false));
				assertNotEquals(value, fakeProxy.getProperty(property));

				// The first call should successfully set the property.
				// setPropertyImpl(...) will also be called.
				fakeProxy.failToSetProperty = false;
				assertTrue(proxy.setProperty(property, value).get());
				assertTrue(fakeProxy.setPropertyImplCalled.getAndSet(false));
				assertEquals(value, fakeProxy.getProperty(property));

				// The second call should return false, because the property was
				// already set.
				assertFalse(proxy.setProperty(property, value).get());
				assertFalse(fakeProxy.setFeatureImplCalled.get());
			}
		}

		// Get the first valid property/value from the proxy.
		validProperty = proxy.getProperties().keySet().iterator().next();
		validValue = proxy.getPropertyAllowedValues(validProperty).iterator()
				.next();

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

		// Open the proxy. We don't care about its return value, it just must be
		// opened before it finds properties for the file.
		try {
			proxy.open(connection).get();
		} catch (NullPointerException | InterruptedException
				| ExecutionException e1) {
			e1.printStackTrace();
			fail("AbstractParaViewProxyTester error: " + "Thread interrupted!");
		}

		// Add 3 properties where 2 of them are new values and one is old.
		if ("djibouti".equals(fakeProxy.getProperty("africa"))) {
			newProperties.put("africa", "abuja");
		} else {
			newProperties.put("africa", "djibouti");
		}
		if ("tokyo".equals(fakeProxy.getProperty("asia"))) {
			newProperties.put("asia", "beijing");
		} else {
			newProperties.put("asia", "tokyo");
		}
		newProperties.put("australia", "canberra"); // The old value.

		// Get the first valid property/value from the proxy.
		validProperty = proxy.getProperties().keySet().iterator().next();
		validValue = proxy.getPropertyAllowedValues(validProperty).iterator()
				.next();

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
	 * Creates a set of strings, including null values.
	 * 
	 * @param elements
	 *            The elements to add to the set.
	 * @return A new set containing the specified elements.
	 */
	private Set<String> createSet(String... elements) {
		Set<String> set = new HashSet<String>();
		for (String element : elements) {
			set.add(element);
		}
		return set;
	}

	/**
	 * A fake proxy that extends {@link AbstractParaViewProxy} and exposes
	 * certiain methods to ensure the abstract class re-directs method calls
	 * when appropriate to its sub-classes.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class FakeParaViewProxy extends AbstractParaViewProxy {

		/**
		 * A map of supported categories and features for the fake proxy. This
		 * should be set at construction time.
		 */
		public final Map<String, Set<String>> features;
		/**
		 * A map of supported properties and their allowed values for the fake
		 * proxy. This should be set at construction time.
		 */
		public final Map<String, Set<String>> properties;

		/**
		 * Whether or not {@link #openProxyOnClient(VtkWebClient, String)} was
		 * called.
		 */
		public final AtomicBoolean openImplCalled = new AtomicBoolean();
		/**
		 * Whether or not {@link #findFeatures(VtkWebClient)} was called.
		 */
		public final AtomicBoolean findFeaturesCalled = new AtomicBoolean();
		/**
		 * Whether or not {@link #findProperties(VtkWebClient)} was called.
		 */
		public final AtomicBoolean findPropertiesCalled = new AtomicBoolean();
		/**
		 * Whether or not
		 * {@link #setFeatureOnClient(VtkWebClient, String, String)} was called.
		 */
		public final AtomicBoolean setFeatureImplCalled = new AtomicBoolean();
		/**
		 * Whether or not
		 * {@link #setPropertyOnClient(VtkWebClient, String, String)} was
		 * called.
		 */
		public final AtomicBoolean setPropertyImplCalled = new AtomicBoolean();

		/**
		 * If true, then
		 * {@link #setFeatureOnClient(VtkWebClient, String, String)} will "fail"
		 * and return false, otherwise it will "succeed" and return true.
		 */
		public boolean failToSetFeature = false;
		/**
		 * If true, then
		 * {@link #setPropertyOnClient(VtkWebClient, String, String)} will
		 * "fail" and return false, otherwise it will "succeed" and return true.
		 */
		public boolean failToSetProperty = false;

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

			features = new HashMap<String, Set<String>>();
			properties = new HashMap<String, Set<String>>();
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

		/**
		 * Exposes the parent class' operation.
		 */
		@Override
		public String getCategory() {
			return super.getCategory();
		}

		/**
		 * Exposes the parent class' operation.
		 */
		@Override
		public String getFeature() {
			return super.getFeature();
		}

		/**
		 * Overrides the default behavior to additionally set
		 * {@link #openImplCalled} to true when called.
		 */
		public boolean openProxyOnClient(ParaViewConnectionAdapter connection,
				String fullPath) {
			openImplCalled.set(true);
			return super.openProxyOnClient(connection, fullPath);
		}

		/*
		 * Overrides a method from AbstractParaViewProxy.
		 */
		@Override
		protected Map<String, Set<String>> findFeatures(
				ParaViewConnectionAdapter connection) {
			findFeaturesCalled.set(true);
			return features;
		}

		/*
		 * Overrides a method from AbstractParaViewProxy.
		 */
		@Override
		protected List<IProxyProperty> findProperties(
				ParaViewConnectionAdapter connection) {
			// Load the properties into the required list of properties.
			List<IProxyProperty> propertyList = new ArrayList<IProxyProperty>();
			for (Entry<String, Set<String>> e : properties.entrySet()) {
				// Get the name and allowed values.
				final String name = e.getKey();
				final Set<String> allowedValues = e.getValue();
				// Create the required property object to maintain the value.
				IProxyProperty property = new IProxyProperty() {
					@Override
					public String getName() {
						return name;
					}

					@Override
					public String getValue() {
						return null;
					}

					@Override
					public Set<String> getAllowedValues() {
						return allowedValues;
					}

					/**
					 * Sets {@link #setPropertyImplCalled} to true. Returns true
					 * if {@link #failToSetProperty} is false, false otherwise.
					 */
					@Override
					public boolean setValue(String value)
							throws NullPointerException,
							IllegalArgumentException,
							UnsupportedOperationException {
						setPropertyImplCalled.set(true);
						return !failToSetProperty;
					}
				};
				propertyList.add(property);
			}

			findPropertiesCalled.set(true);
			return propertyList;
		}

		/**
		 * Sets {@link #setFeatureImplCalled} to true. Returns true if
		 * {@link #failToSetFeature} is false, false otherwise.
		 */
		@Override
		protected boolean setFeatureOnClient(
				ParaViewConnectionAdapter connection, String category,
				String feature) {
			setFeatureImplCalled.set(true);
			return !failToSetFeature;
		}
	}
}
