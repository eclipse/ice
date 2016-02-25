/*******************************************************************************
 * Copyright (c) 2016- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.datastructures.VizObject.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.datastructures.VizObject.UpdateableSubscriptionManager;
import org.junit.Test;

/**
 * A class for testing the functionality of the UpdateableSubscriptionManager
 * 
 * @author Robert Smith
 *
 */
public class UpdateableSubscriptionManagerTester {

	/**
	 * Checks that the UpdateableSubscriptionManager will queue and flush
	 * messages correctly.
	 */
	@Test
	public void checkQueueing() {

		// Create an object to listen to
		TestUpdateable source = new TestUpdateable();
		UpdateableSubscriptionManager manager = source.getManager();

		// Create a listener for the object
		ArrayList<SubscriptionType> allList = new ArrayList<SubscriptionType>();
		allList.add(SubscriptionType.ALL);
		TestManagedListener listener = new TestManagedListener(allList);
		source.register(listener);

		// Create a list containing the CHILD type
		ArrayList<SubscriptionType> list = new ArrayList<SubscriptionType>();
		list.add(SubscriptionType.CHILD);

		// Create a list containing the PROPERTY type
		ArrayList<SubscriptionType> propertyList = new ArrayList<SubscriptionType>();
		propertyList.add(SubscriptionType.PROPERTY);

		// Check that the listener receives updates normally
		source.sendUpdate(list);
		assertTrue(listener.gotChild());

		// Set the manager to queue mode
		manager.enqueue();

		// Sent messages should be held while the manager is queued.
		source.sendUpdate(list);
		assertFalse(listener.gotChild());
		source.sendUpdate(list);
		assertFalse(listener.gotChild());

		// Flush the queue and check that the listener was finally updated
		manager.flushQueue();
		assertTrue(listener.gotChild());

		// Check that every message type is queued
		manager.enqueue();
		source.sendUpdate(propertyList);
		source.sendUpdate(list);
		manager.flushQueue();
		assertTrue(listener.gotChild());
		assertTrue(listener.gotProperty());
		assertFalse(listener.gotAll());

		// Check that the queue is not flushed until the manager receives a
		// number of flush requests equal to the number of queue requests it has
		// received
		manager.enqueue();
		manager.enqueue();
		source.sendUpdate(propertyList);
		assertFalse(listener.gotProperty());

		// Flushing the queue once should not release the manager's messages
		manager.flushQueue();
		assertFalse(listener.gotProperty());

		// Flushing it again should release them
		manager.flushQueue();
		assertTrue(listener.gotProperty());

		// Add a parent manager
		TestUpdateable parentSource = new TestUpdateable();
		UpdateableSubscriptionManager parent = parentSource.getManager();
		manager.setParent(parent);

		// Create a listener for the object
		TestManagedListener parentListener = new TestManagedListener(allList);
		parentSource.register(parentListener);

		// Queuing the child should block the parent's messages
		manager.enqueue();
		parentSource.sendUpdate(propertyList);
		assertFalse(parentListener.gotProperty());

		// Flushing the child should flush the parent
		manager.flushQueue();
		assertTrue(parentListener.gotProperty());

		// Flushing the child should be equivalent to invoking flushQueue() on
		// the parent once. Thus it should not automatically release the queued
		// state if other queueing requests have come in.
		parent.enqueue();
		manager.enqueue();
		parentSource.sendUpdate(propertyList);
		manager.flushQueue();
		assertFalse(parentListener.gotProperty());

		// Releasing the parent's queue afterwards should send the messages
		parent.flushQueue();
		assertTrue(parentListener.gotProperty());
	}

	/**
	 * Check that events are sent to the right subscription lists.
	 */
	@Test
	public void checkMessageTypes() {

		// Create an object to listen to
		TestUpdateable source = new TestUpdateable();

		// A list that specifies all subscription types
		ArrayList<SubscriptionType> allList = new ArrayList<SubscriptionType>();
		allList.add(SubscriptionType.ALL);

		// A list that specifies the child and property subscription types
		ArrayList<SubscriptionType> childPropertyList = new ArrayList<SubscriptionType>();
		childPropertyList.add(SubscriptionType.CHILD);
		childPropertyList.add(SubscriptionType.PROPERTY);

		// A list that specifies the child and transformation subscription types
		ArrayList<SubscriptionType> childTransformationList = new ArrayList<SubscriptionType>();
		childTransformationList.add(SubscriptionType.CHILD);
		childTransformationList.add(SubscriptionType.TRANSFORMATION);

		// A list that specifies the child subscription type
		ArrayList<SubscriptionType> childList = new ArrayList<SubscriptionType>();
		childList.add(SubscriptionType.CHILD);

		// A list that specifies the property subscription type
		ArrayList<SubscriptionType> propertyList = new ArrayList<SubscriptionType>();
		propertyList.add(SubscriptionType.PROPERTY);

		// Create a listener that will receive all updates
		TestManagedListener listener = new TestManagedListener(allList);
		source.register(listener);

		// Create a listener that will only receive CHILD type updates
		TestManagedListener childListener = new TestManagedListener(childList);
		source.register(childListener);

		// Send a child update and check that it was received.
		source.sendUpdate(childList);
		assertTrue(listener.gotChild());
		assertFalse(listener.gotProperty());
		assertFalse(listener.gotAll());
		assertTrue(childListener.gotChild());
		assertFalse(childListener.gotProperty());
		assertFalse(childListener.gotAll());

		// Send a property update and check that it was received.
		source.sendUpdate(propertyList);
		assertFalse(listener.gotChild());
		assertTrue(listener.gotProperty());
		assertFalse(listener.gotAll());
		assertFalse(childListener.gotChild());
		assertFalse(childListener.gotProperty());
		assertFalse(childListener.gotAll());

		// Send child and transformation updates, and check that the child was
		// received and the transformation ignored
		source.sendUpdate(childTransformationList);
		assertTrue(listener.gotChild());
		assertFalse(listener.gotProperty());
		assertFalse(listener.gotAll());
		assertTrue(childListener.gotChild());
		assertFalse(childListener.gotProperty());
		assertFalse(childListener.gotAll());

		// Send child and property updates, and check that both were received
		source.sendUpdate(childPropertyList);
		assertTrue(listener.gotChild());
		assertTrue(listener.gotProperty());
		assertFalse(listener.gotAll());
		assertTrue(childListener.gotChild());
		assertFalse(childListener.gotProperty());
		assertFalse(childListener.gotAll());

	}

	/**
	 * Check that listeners are registered and unregistered correctly.
	 */
	@Test
	public void checkRegistration() {

		// Create an object to listen to
		TestUpdateable source = new TestUpdateable();

		// A list that specifies the property subscription type
		ArrayList<SubscriptionType> propertyList = new ArrayList<SubscriptionType>();
		propertyList.add(SubscriptionType.PROPERTY);

		// Create two listeners that will receive property updates
		TestManagedListener listener = new TestManagedListener(propertyList);
		source.register(listener);

		// Check that the listener receives updates
		source.sendUpdate(propertyList);
		assertTrue(listener.gotProperty());

		// Remove the listener and check that it no longer receives updates
		source.unregister(listener);
		source.sendUpdate(propertyList);
		assertFalse(listener.gotProperty());

		// Add two listeners and check that both receive updates
		TestManagedListener listener2 = new TestManagedListener(propertyList);
		source.register(listener);
		source.register(listener2);
		source.sendUpdate(propertyList);
		assertTrue(listener.gotProperty());
		assertTrue(listener2.gotProperty());

		// Remove them both and check that neither receives updates
		source.unregister(listener);
		source.unregister(listener2);
		source.sendUpdate(propertyList);
		assertFalse(listener.gotProperty());
		assertFalse(listener2.gotProperty());

		// Check that listeners are notified based on the manager's state at the
		// time the queue is flushed, rather than when messages are sent
		source.register(listener);
		source.register(listener2);
		source.getManager().enqueue();
		source.sendUpdate(propertyList);
		source.unregister(listener);
		source.getManager().flushQueue();

		// Listener2 should have received the update. Listener, which was
		// unregistered before the queue was flushed, should not
		assertFalse(listener.gotProperty());
		assertTrue(listener2.gotProperty());
	}

	/**
	 * A basic implementation of IManagedUpdateable for testing purposes.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestUpdateable implements IManagedUpdateable {

		/**
		 * The manager for the updateable's messages.
		 */
		UpdateableSubscriptionManager manager;

		/**
		 * The default constructor
		 */
		public TestUpdateable() {
			manager = new UpdateableSubscriptionManager(this);
		}

		public UpdateableSubscriptionManager getManager() {
			return manager;
		}

		public void sendUpdate(ArrayList<SubscriptionType> types) {

			// Convert the array list to an array and send it to the manager
			SubscriptionType[] temp = new SubscriptionType[types.size()];
			temp = types.toArray(temp);
			manager.notifyListeners(temp);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
		 * IManagedUpdateable#register(org.eclipse.eavp.viz.service.
		 * datastructures.VizObject.IManagedUpdateableListener)
		 */
		@Override
		public void register(IManagedUpdateableListener listener) {
			manager.register(listener);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
		 * IManagedUpdateable#unregister(org.eclipse.eavp.viz.service.
		 * datastructures.VizObject.IManagedUpdateableListener)
		 */
		@Override
		public void unregister(IManagedUpdateableListener listener) {
			manager.unregister(listener);
		}
	}
}
