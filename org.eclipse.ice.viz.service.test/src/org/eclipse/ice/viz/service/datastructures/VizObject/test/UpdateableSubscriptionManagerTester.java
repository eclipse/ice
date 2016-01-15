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
package org.eclipse.ice.viz.service.datastructures.VizObject.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.ice.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.ice.viz.service.datastructures.VizObject.UpdateableSubscriptionManager;
import org.eclipse.ice.viz.service.datastructures.VizObject.UpdateableSubscriptionType;
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
		ArrayList<UpdateableSubscriptionType> allList = new ArrayList<UpdateableSubscriptionType>();
		allList.add(UpdateableSubscriptionType.All);
		TestListener listener = new TestListener(allList);
		source.register(listener);

		// Create a list containing the CHILD type
		ArrayList<UpdateableSubscriptionType> list = new ArrayList<UpdateableSubscriptionType>();
		list.add(UpdateableSubscriptionType.Child);

		// Create a list containing the PROPERTY type
		ArrayList<UpdateableSubscriptionType> propertyList = new ArrayList<UpdateableSubscriptionType>();
		propertyList.add(UpdateableSubscriptionType.Property);

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

		// Check that every message
	}

	/**
	 * Check that events are sent to the right subscription lists.
	 */
	@Test
	public void checkMessageTypes() {
		// Create an object to listen to
		TestUpdateable source = new TestUpdateable();

		// A list that specifies all subscription types
		ArrayList<UpdateableSubscriptionType> allList = new ArrayList<UpdateableSubscriptionType>();
		allList.add(UpdateableSubscriptionType.All);

		// A list that specifies the child and property subscription types
		ArrayList<UpdateableSubscriptionType> childPropertyList = new ArrayList<UpdateableSubscriptionType>();
		childPropertyList.add(UpdateableSubscriptionType.Child);
		childPropertyList.add(UpdateableSubscriptionType.Property);

		// A list that specifies the child and transformation subscription types
		ArrayList<UpdateableSubscriptionType> childTransformationList = new ArrayList<UpdateableSubscriptionType>();
		childTransformationList.add(UpdateableSubscriptionType.Child);
		childTransformationList.add(UpdateableSubscriptionType.Property);

		// A list that specifies the child subscription type
		ArrayList<UpdateableSubscriptionType> childList = new ArrayList<UpdateableSubscriptionType>();
		childList.add(UpdateableSubscriptionType.Child);

		// A list that specifies the property subscription type
		ArrayList<UpdateableSubscriptionType> propertyList = new ArrayList<UpdateableSubscriptionType>();
		propertyList.add(UpdateableSubscriptionType.Property);

		// Create a listener that will receive all updates
		TestListener listener = new TestListener(allList);
		source.register(listener);

		// Create a listener that will only receive CHILD type updates
		TestListener childListener = new TestListener(childList);

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

		// Send child and property updates, and check that bother were received
		source.sendUpdate(childPropertyList);
		assertFalse(listener.gotChild());
		assertTrue(listener.gotProperty());
		assertFalse(listener.gotAll());
		assertFalse(childListener.gotChild());
		assertFalse(childListener.gotProperty());
		assertFalse(childListener.gotAll());

		// Send an ALL update, and check that nothing was received, as ALL is
		// not a valid type for a message to send.
		source.sendUpdate(allList);
		assertFalse(listener.gotChild());
		assertFalse(listener.gotProperty());
		assertFalse(listener.gotAll());
		assertFalse(childListener.gotChild());
		assertFalse(childListener.gotProperty());
		assertFalse(childListener.gotAll());

	}

	/**
	 * A basic implementation of IManagedUpdateableListener for testing
	 * purposes.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestListener implements IManagedUpdateableListener {

		/**
		 * Whether the listener has received an update of the property type
		 */
		private boolean propertyNotified;

		/**
		 * Whether the listener has received an update of the child type
		 */
		private boolean childNotified;

		/**
		 * Whether the listener has received an update of the all type
		 */
		private boolean allNotified;

		/**
		 * The list of types of events this listener will receive
		 */
		ArrayList<UpdateableSubscriptionType> types;

		/**
		 * The default constructor.
		 * 
		 * @param types
		 *            The list of types of events this listener will receive.
		 */
		public TestListener(ArrayList<UpdateableSubscriptionType> types) {
			this.types = types;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ice.viz.service.datastructures.VizObject.
		 * IManagedUpdateableListener#getSubscriptions(org.eclipse.ice.viz.
		 * service.datastructures.VizObject.IManagedUpdateable)
		 */
		@Override
		public ArrayList<UpdateableSubscriptionType> getSubscriptions(
				IManagedUpdateable source) {
			return types;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ice.viz.service.datastructures.VizObject.
		 * IManagedUpdateableListener#update(org.eclipse.ice.viz.service.
		 * datastructures.VizObject.IManagedUpdateable,
		 * org.eclipse.ice.viz.service.datastructures.VizObject.
		 * UpdateableSubscriptionType[])
		 */
		@Override
		public void update(IManagedUpdateable component,
				UpdateableSubscriptionType[] types) {

			// For each type in the update, set each notification received to
			// true if it matches one of the checked types
			for (UpdateableSubscriptionType type : types) {

				if (type == UpdateableSubscriptionType.Property) {
					propertyNotified = true;
				}

				else if (type == UpdateableSubscriptionType.Child) {
					childNotified = true;
				}

				else if (type == UpdateableSubscriptionType.All) {
					allNotified = true;
				}
			}

		}

		/**
		 * Checks if the listener has received an ALL type notification and
		 * resets its state for the ALL type to the original, unnotified state.
		 * 
		 * @return True if the listener has received a notification of type ALL
		 *         since the last time this function was invoked. False
		 *         otherwise.
		 */
		public boolean gotAll() {
			boolean temp = allNotified;
			allNotified = false;
			return allNotified;
		}

		/**
		 * Checks if the listener has received a CHILD type notification and
		 * resets its state for the CHILD type to the original, unnotified
		 * state.
		 * 
		 * @return True if the listener has received a notification of type
		 *         CHILD since the last time this function was invoked. False
		 *         otherwise.
		 */
		public boolean gotChild() {
			boolean temp = childNotified;
			childNotified = false;
			return childNotified;
		}

		/**
		 * Checks if the listener has received an PROPERTY type notification and
		 * resets its state for the PROPERTY type to the original, unnotified
		 * state.
		 * 
		 * @return True if the listener has received a notification of type
		 *         PROPERTY since the last time this function was invoked. False
		 *         otherwise.
		 */
		public boolean gotProperty() {
			boolean temp = propertyNotified;
			propertyNotified = false;
			return propertyNotified;
		}

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

		public void sendUpdate(ArrayList<UpdateableSubscriptionType> types) {
			manager.notifyListeners(
					(UpdateableSubscriptionType[]) types.toArray());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ice.viz.service.datastructures.VizObject.
		 * IManagedUpdateable#register(org.eclipse.ice.viz.service.
		 * datastructures.VizObject.IManagedUpdateableListener)
		 */
		@Override
		public void register(IManagedUpdateableListener listener) {
			manager.register(listener);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ice.viz.service.datastructures.VizObject.
		 * IManagedUpdateable#unregister(org.eclipse.ice.viz.service.
		 * datastructures.VizObject.IManagedUpdateableListener)
		 */
		@Override
		public void unregister(IManagedUpdateableListener listener) {
			manager.unregister(listener);
		}
	}
}
