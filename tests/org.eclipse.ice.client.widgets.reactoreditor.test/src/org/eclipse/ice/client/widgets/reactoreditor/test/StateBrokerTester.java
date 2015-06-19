/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.reactoreditor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.client.widgets.reactoreditor.DataSource;
import org.eclipse.ice.client.widgets.reactoreditor.IStateListener;
import org.eclipse.ice.client.widgets.reactoreditor.StateBroker;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the StateBroker from the ReactorEditor package.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class StateBrokerTester {

	/**
	 * Since the broker needs to be able to handle multiple IAnalysisViews for
	 * multiple data sources, we should reuse the same broker for the test.
	 */
	private StateBroker broker;

	/**
	 * Initializes the shared StateBroker.
	 */
	@Before
	public void before() {
		broker = new StateBroker();
	}

	/**
	 * Checks the putValue() and getValue() functions.
	 */
	@Test
	public void checkAccessors() {
		// There are seven possible scenarios with the old and new values for a
		// key.
		// These are (oldValue/newValue):
		// empty/null
		// empty/!null
		// null/null (no change in value)
		// null/!null
		// !null/null
		// !null/!null and not equal
		// !null/!null and equal (no change in value)

		String key;
		Object oldValue;
		Object newValue;

		// Put null. // empty/null
		key = "key1";
		newValue = null;
		oldValue = broker.putValue(key, newValue);
		assertEquals(newValue, broker.getValue(key));
		assertEquals(oldValue, null);

		// Put a null, then put a null. // null/null (no change in value)
		newValue = null;
		oldValue = broker.putValue(key, newValue);
		assertEquals(newValue, broker.getValue(key));
		assertEquals(oldValue, null);

		// Put a null, then put an integer. // null/!null
		newValue = 1;
		oldValue = broker.putValue(key, newValue);
		assertEquals(newValue, broker.getValue(key));
		assertEquals(oldValue, null);

		// Put a string. // empty/!null
		key = "key2";
		newValue = "newValue";
		oldValue = broker.putValue(key, newValue);
		assertEquals(newValue, broker.getValue(key));
		assertEquals(oldValue, null);

		// Put a string, then put a null. // !null/null
		newValue = null;
		oldValue = broker.putValue(key, newValue);
		assertEquals(newValue, broker.getValue(key));
		assertEquals(oldValue, "newValue");

		// Put a string, then put an class object. // !null/!null and not equal
		key = "key3";
		newValue = "newValue";
		broker.putValue(key, newValue);
		newValue = new TestClass(5);
		oldValue = broker.putValue(key, newValue);
		assertEquals(newValue, broker.getValue(key));
		assertEquals(oldValue, "newValue");

		// Put a string, then put the same string. // !null/!null and equal (no
		// change in value)
		key = "key4";
		newValue = "a string";
		broker.putValue(key, newValue);
		oldValue = broker.putValue(key, "a string");
		assertEquals(oldValue, broker.getValue(key));
		assertEquals(oldValue, newValue);

		// Put a class object, then put the same class object.
		// This is just for good measure.
		key = "key5";
		newValue = new TestClass(42);
		broker.putValue(key, newValue);
		oldValue = broker.putValue(key, newValue);
		assertEquals(oldValue, broker.getValue(key));
		assertEquals(oldValue, newValue);
	}

	/**
	 * Checks registration and un-registration for the StateBroker.
	 */
	@Test
	public void checkRegistration() {
		String key;
		TestListener listener1;
		TestListener listener2;
		TestListener listener3;

		// Add a value.
		key = "key1";
		broker.putValue(key, 0);

		// Register a listener, change the value.
		// The listener should be notified.
		listener1 = new TestListener(key, broker);
		listener1.registerKeys();
		broker.putValue(key, 1);
		assertEquals(listener1.getValue(), 1);

		// Unregister the listener, change the value.
		// The listener should not be notified.
		listener1.unregisterKeys();
		broker.putValue(key, 2);
		assertEquals(listener1.getValue(), 1);

		// Re-register the old and Register another listener, change the value.
		// Both listeners should be notified.
		listener1.registerKeys();
		listener2 = new TestListener(key, broker);
		listener2.registerKeys();
		broker.putValue(key, 3);
		assertEquals(listener1.getValue(), 3);
		assertEquals(listener2.getValue(), 3);

		// Unregister one of the listeners, change the value.
		// Only the still-registered listener should be notified.
		listener2.unregisterKeys();
		broker.putValue(key, 4);
		assertEquals(listener1.getValue(), 4);
		assertEquals(listener2.getValue(), 3);

		// Register a listener for a new key, change its value.
		// Only the new listener should be notified.
		key = "key2";
		listener3 = new TestListener(key, broker);
		listener3.registerKeys();
		broker.putValue(key, 0);
		assertEquals(listener3.getValue(), 0);
		assertEquals(listener1.getValue(), 4);
		assertEquals(listener2.getValue(), 3);

		// We need to make sure the listeners are notified if the value has
		// changed for the cases:
		// empty, null != !null, !null != !null, !null == !null
		key = "key3";
		listener1 = new TestListener(key, broker);
		listener1.registerKeys();

		// empty
		broker.putValue(key, null);
		assertEquals(listener1.getValue(), null);
		assertEquals(listener1.getUpdateCount(), 1);

		// null != !null
		TestClass tester = new TestClass(42);
		broker.putValue(key, tester);
		assertEquals(listener1.getValue(), tester);
		assertEquals(listener1.getUpdateCount(), 2);

		// !null == !null (no change)
		TestClass tester2 = tester;
		broker.putValue(key, tester2);
		assertEquals(listener1.getValue(), tester);
		assertEquals(listener1.getUpdateCount(), 2);

		// !null != !null
		broker.putValue(key, 42);
		assertEquals(listener1.getValue(), 42);
		assertEquals(listener1.getUpdateCount(), 3);

		// Lastly, we should make sure the listener gets the current value in
		// the broker when it registers.

		// The default key value is null.
		key = "key4";
		listener1 = new TestListener(key, broker);
		listener1.registerKeys();
		assertEquals(null, listener1.getValue());
		assertEquals(0, listener1.getUpdateCount());
		listener1.unregisterKeys();

		// Check the return value for a key that has been set already.
		broker.putValue(key, "qwerty");
		listener1.registerKeys();
		assertEquals("qwerty", listener1.getValue());
		assertEquals(0, listener1.getUpdateCount());

		return;
	}

	/**
	 * This test checks the resetSource() method, which should remove all keys
	 * prefixed by the specified data source String.
	 */
	@Test
	public void checkSourceReset() {

		String datasource;
		String key;
		Object value;
		List<String> keys = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();

		/* ---- Test the input data source. ---- */
		datasource = DataSource.Input.toString();

		// Add some keys with a particular data source.
		key = datasource + "-civilization";
		value = 1;
		keys.add(key);
		values.add(value);
		broker.putValue(key, value);

		// Register a listener.
		TestListener listener = new TestListener(key, broker);
		listener.registerKeys();
		assertEquals(value, listener.getValue());
		assertEquals(0, listener.getUpdateCount());

		key = datasource + "-v";
		value = 3.1415926;
		keys.add(key);
		values.add(value);
		broker.putValue(key, value);

		key = datasource + "-is";
		value = "This is a Java String.";
		keys.add(key);
		values.add(value);
		broker.putValue(key, value);

		key = datasource + "-addictive";
		value = new StateBroker();
		keys.add(key);
		values.add(value);
		broker.putValue(key, value);

		// Register a key with a different datasource.
		String otherDatasource = DataSource.Reference.toString();
		key = otherDatasource + "-can't touch this";
		value = 1;
		keys.add(key);
		values.add(value);
		broker.putValue(key, value);

		// Reset the data source. The keys' values should be null. Listeners
		// should also be notified.
		for (int i = 0; i < keys.size(); i++) {
			assertEquals(values.get(i), broker.getValue(keys.get(i)));
		}
		// Reset all the keys for the main data source.
		broker.resetSource(datasource);

		// Check the keys in the broker. Only the last key/value pair should be
		// unchanged.
		for (int i = 0; i < keys.size() - 1; i++) {
			assertEquals(null, broker.getValue(keys.get(i)));
		}
		assertEquals(values.get(values.size() - 1),
				broker.getValue(keys.get(values.size() - 1)));

		// Check the listener's state.
		assertEquals(null, listener.getValue());
		assertEquals(1, listener.getUpdateCount());
		/* ------------------------------------- */

		return;
	}

	/**
	 * Makes sure that {@link StateBroker#copyValues(StateBroker)} properly
	 * copies the values from one broker to the other.
	 */
	@Test
	public void checkCopying() {

		// Create two brokers (we need an extra to copy from).
		StateBroker broker = new StateBroker();
		StateBroker sourceBroker = new StateBroker();

		// Put a unique value and a shared value in the first broker.
		broker.putValue("uniqueKey1", true);
		broker.putValue("nonUniqueKey", true);

		// Put unique values and shared values in the second broker.
		sourceBroker.putValue("uniqueKey2", true);
		sourceBroker.putValue("nonUniqueKey", false);
		sourceBroker.putValue("uniqueKey3", "smash");
		sourceBroker.putValue("the", "beetles");
		sourceBroker.putValue("smash", "em");

		// Add listeners to unique and shared values from both brokers.
		TestListener listener1 = new TestListener("uniqueKey1", broker);
		listener1.registerKeys();
		TestListener listener2 = new TestListener("nonUniqueKey", broker);
		listener2.registerKeys();
		TestListener listener3 = new TestListener("uniqueKey2", broker);
		listener3.registerKeys();

		// Copy the values from the source to the destination broker.
		broker.copyValues(sourceBroker);

		// Check the contents of the destination broker.
		assertNull(broker.getValue("uniqueKey1"));
		assertTrue((Boolean) broker.getValue("uniqueKey2"));
		assertFalse((Boolean) broker.getValue("nonUniqueKey"));
		assertEquals("smash", broker.getValue("uniqueKey3"));
		assertEquals("beetles", broker.getValue("the"));
		assertEquals("em", broker.getValue("smash"));

		// FIXME What should this really be? Should the broker notify listeners
		// that the value was removed?
		assertEquals(0, listener1.getUpdateCount());
		assertEquals(true, listener1.getValue());

		// Check the listener to the non-unique key (its value changes).
		assertEquals(1, listener2.getUpdateCount());
		assertEquals(false, listener2.getValue());

		// Check the listener for the unique key from the source broker.
		assertEquals(1, listener3.getUpdateCount());
		assertEquals(true, listener3.getValue());

	}

	/**
	 * I use this basic test class to test the broker's ability to store and
	 * compare class instances. The contents of the class don't really matter.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private class TestClass {
		/**
		 * An integer value, just to give this class a little personality.
		 */
		private int value;

		/**
		 * The default constructor.
		 * 
		 * @param value
		 *            An integer.
		 */
		public TestClass(int value) {
			this.value = value;
		}

		/**
		 * Implement equality for a TestClass instance.
		 */
		@Override
		public boolean equals(Object otherObject) {
			// Return false if otherObject is null, super.equals fails, or the
			// value is different.
			return (otherObject != null && super.equals(otherObject) && value == ((TestClass) otherObject).value);
		}
	}

	/**
	 * We need to implement an IStateListener and check the values it receives
	 * from its broker for a specific key. By default, the value stored is -1.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private class TestListener implements IStateListener {
		/**
		 * The key this listener observes.
		 */
		private String key;
		/**
		 * The value this listener observes.
		 */
		private Object value;
		/**
		 * The broker with which this listener registers.
		 */
		private StateBroker broker;
		/**
		 * The number of updates to the key-value pair.
		 */
		private int updates;

		/**
		 * The default constructor.
		 * 
		 * @param key
		 *            The key this listener observes.
		 * @param broker
		 *            The broker with which this listener registers.
		 */
		public TestListener(String key, StateBroker broker) {
			this.key = key;
			this.broker = broker;
			this.value = -1;
			this.updates = 0;

			return;
		}

		/* ---- Implements IStateListener. ---- */
		@Override
		public void update(String key, Object value) {
			this.value = value;
			this.updates++;
		}

		@Override
		public void setBroker(StateBroker broker) {
			// Unnecessary method: the broker is in the constructor.
		}

		@Override
		public void registerKeys() {
			value = this.broker.register(key, this);
		}

		@Override
		public void unregisterKeys() {
			broker.unregister(key, this);
		}

		/* ------------------------------------ */

		/**
		 * Get the current value known to this listener.
		 * 
		 * @return The current value associated with the key (as far as the
		 *         listener is aware).
		 */
		public Object getValue() {
			return value;
		}

		/**
		 * Get the number of times this listener has been notified of an update.
		 * 
		 * @return The number of updates to the key-value pair.
		 */
		public int getUpdateCount() {
			return updates;
		}
	}
}
