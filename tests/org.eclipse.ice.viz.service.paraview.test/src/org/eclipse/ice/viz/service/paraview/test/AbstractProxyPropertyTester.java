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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.viz.service.connections.paraview.ParaViewConnectionAdapter;
import org.eclipse.ice.viz.service.paraview.proxy.AbstractProxyProperty;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IProxyProperty;
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
public class AbstractProxyPropertyTester {

	/**
	 * The property that is being tested.
	 */
	private AbstractProxyProperty property;

	/**
	 * A fake property used to provide some basic behavior for {@link #property}
	 * that works in the absence of proxies and connections to ParaView. Should
	 * be the same instance.
	 */
	private FakeProxyProperty fakeProperty;

	/**
	 * The allowed values to use.
	 */
	private Set<String> allowedValues;

	/**
	 * The current value of the property "on the client".
	 */
	private String value;

	/**
	 * The default property name to use.
	 */
	private String name;

	/**
	 * Initializes the class variables used in the test methods.
	 */
	@Before
	public void beforeEachTest() {
		// Set up some allowed values.
		allowedValues = new HashSet<String>();
		allowedValues.add("true");
		allowedValues.add("false");

		// Set up the initial value.
		value = "true";

		// Set the name to use for the property.
		name = "perturbator";

		// Initialize the property to test.
		fakeProperty = new FakeProxyProperty(name, null, null);
		property = fakeProperty;

		return;
	}

	/**
	 * Checks the name of the property.
	 */
	@Test
	public void checkName() {

		// The name should be the same one passed in via constructor.
		assertEquals(name, property.getName());
		assertEquals(name, property.getName());

		return;
	}

	/**
	 * Checks the set of allowed values.
	 */
	@Test
	public void checkAllowedValues() {

		// Check the expected allowed values.
		assertEquals(2, property.getAllowedValues().size());
		assertTrue(property.getAllowedValues().contains("true"));
		assertTrue(property.getAllowedValues().contains("false"));

		// Make sure the returned set cannot be modified.
		property.getAllowedValues().clear();
		assertEquals(2, property.getAllowedValues().size());
		assertTrue(property.getAllowedValues().contains("true"));
		assertTrue(property.getAllowedValues().contains("false"));

		// Make sure that what is returned is a distinct copy.
		assertNotSame(property.getAllowedValues(), property.getAllowedValues());

		return;
	}

	/**
	 * This checks that setting the value to a new value calls the sub-class
	 * implementation, updates the value if necessary, and returns whether the
	 * value actually changed. It also checks the value can be retrieved by its
	 * getter.
	 */
	@Test
	public void checkValue() {

		// Initially, the value is true.
		assertEquals("true", property.getValue());

		// We can't set it to the same value. It is already set! The underlying
		// implementation shouldn't be called, either.
		assertFalse(property.setValue("true"));
		assertFalse(fakeProperty.setValueImplCalled.get());
		assertEquals("true", property.getValue());

		// The value is new, so it can be set. It should call the underlying
		// implementation.
		assertTrue(property.setValue("false"));
		assertTrue(fakeProperty.setValueImplCalled.getAndSet(false));
		assertEquals("false", property.getValue());

		// We can't set it to the same value. It is already set! The underlying
		// implementation shouldn't be called, either.
		assertFalse(property.setValue("false"));
		assertFalse(fakeProperty.setValueImplCalled.get());
		assertEquals("false", property.getValue());

		// The value is new, so it can be set. It should call the underlying
		// implementation.
		assertTrue(property.setValue("true"));
		assertTrue(fakeProperty.setValueImplCalled.getAndSet(false));
		assertEquals("true", property.getValue());

		// Try a failing call to the client implementation.
		fakeProperty.failToSetValue = true;
		// This time, the method returns false, meaning it failed. It does
		// attempt to call the client implementation, but the property is not
		// updated because the client claims to have failed.
		assertFalse(property.setValue("false"));
		assertTrue(fakeProperty.setValueImplCalled.getAndSet(false));
		assertEquals("true", property.getValue());

		return;
	}

	/**
	 * This method checks for exceptions that are thrown when attempting to set
	 * the property's value to null or an invalid value.
	 */
	@Test
	public void checkSetValueExceptions() {

		final String nullString = null;
		final String invalidValue = "something";

		// Trying a null value should throw a NullPointerException.
		try {
			property.setValue(nullString);
			fail("AbstractProxyPropertyTester error: "
					+ "Null values are not supported by the property, yet a "
					+ "NullPointerException was not thrown.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		// Trying an invalid value should throw an IllegalArgumentException.
		try {
			property.setValue(invalidValue);
			fail("AbstractProxyPropertyTester error: "
					+ "IllegalArgumentException not thrown for invalid value.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}

		return;
	}

	/**
	 * A fake class to serve both as a basic implementation for testing purposes
	 * and to expose hidden methods.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class FakeProxyProperty extends AbstractProxyProperty {

		/**
		 * This boolean is set if
		 * {@link #setValueOnClient(String, ParaViewConnectionAdapter)} is
		 * called.
		 */
		public final AtomicBoolean setValueImplCalled = new AtomicBoolean();

		/**
		 * Whether or not to set the value on the "client".
		 */
		public boolean failToSetValue = false;

		/**
		 * The default constructor. Passes through to the super constructor.
		 */
		public FakeProxyProperty(String name, IParaViewProxy proxy,
				ParaViewConnectionAdapter connection, String... allowedValues) {
			super(name, proxy, connection);
		}

		/**
		 * Returns {@link #value}.
		 */
		@Override
		protected String findValue(ParaViewConnectionAdapter connection) {
			return value;
		}

		/**
		 * Returns {@link #allowedValues}.
		 */
		@Override
		protected Set<String> findAllowedValues(
				ParaViewConnectionAdapter connection) {
			return allowedValues;
		}

		/**
		 * If {@link #failToSetValue} is false, sets {@link #value} and returns
		 * true. Otherwise, it just returns false.
		 */
		@Override
		protected boolean setValueOnClient(String value,
				ParaViewConnectionAdapter connection) {
			setValueImplCalled.set(true);
			if (!failToSetValue) {
				AbstractProxyPropertyTester.this.value = value;
			}
			return !failToSetValue;
		}
	}
}
