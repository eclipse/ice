/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
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
package org.eclipse.eavp.viz.service.datastructures.VizObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A class which manages a list of IManagedUpdateableListeners and holds
 * messages intended to be sent to them. The manager filters updates by matching
 * types of events to listeners which have subscribed to the corresponding event
 * type(s).
 * 
 * @author Robert Smith
 *
 */
public class UpdateableSubscriptionManager {

	/**
	 * The updateable object the manager is controlling the message passing for.
	 */
	private IManagedUpdateable source;

	/**
	 * A map of registered listeners associated with the event types they are
	 * registered to receive.
	 */
	private HashMap<SubscriptionType, ArrayList<IManagedUpdateableListener>> subscriptionMap = new HashMap<SubscriptionType, ArrayList<IManagedUpdateableListener>>();

	/**
	 * A list of queued messages to be delivered all at once, for cases where
	 * multiple events occur in quick succession.
	 */
	private ArrayList<SubscriptionType> messageQueue = new ArrayList<SubscriptionType>();

	/**
	 * Keeps track of whether the manager is in queue mode. In queue mode,
	 * events will be saved to the message queue instead of sent to listeners.
	 * When queue mode is exited, all queued events are to be fired in bulk with
	 * a single call to a listener's update() method.
	 * 
	 * The number represents the amount of times the manager has been ordered to
	 * queue messages. Only once it has been ordered to flush the queue that
	 * many times will it actually begin notifying listeners. Thus, the manager
	 * is in queue mode if and only if the variable queueCount is greater than
	 * 0.
	 */
	private int queueCount = 0;

	/**
	 * A parent manager belonging to a registered listener. This parent should
	 * be an object which can safely be assumed to never need to send an update
	 * notification while waiting for an update from this manager's source, such
	 * as in the case of a controller and model from the viz.service.modeling
	 * packages. A parent should never be able to leave its queued state while
	 * this object is in a queued state, but this object flushing its queue is
	 * not a guarantee that the parent will then flush its queue.
	 */
	private UpdateableSubscriptionManager parent;

	/**
	 * The default constructor.
	 * 
	 * @param source
	 *            The object whose listeners this manager will control
	 *            communications for.
	 */
	public UpdateableSubscriptionManager(IManagedUpdateable source) {
		this.source = source;
	}

	/**
	 * Sets the manager in queue mode.
	 */
	public void enqueue() {
		queueCount++;

		// If there is a parent, lock its notifications as well
		if (parent != null) {
			parent.enqueue();
		}
	}

	/**
	 * Exit queue mode, sending all queued messages to listeners.
	 */
	public void flushQueue() {

		// Attempt to exit queue mode
		queueCount--;
		if (queueCount == 0) {

			// Handle the message queue if there is one
			if (!messageQueue.isEmpty()) {

				// Get a local copy of the queued messages and clear the queue.
				// This prevents subsequent calls to the manager before all
				// events are dispatched from sending the same message twice.
				SubscriptionType[] types = new SubscriptionType[messageQueue
						.size()];
				messageQueue.toArray(types);
				messageQueue.clear();

				// Send the messages
				notifyListeners(types);

				// Empty the queue
				messageQueue.clear();
			}

			// If there is a parent, release its queue as well
			if (parent != null) {
				parent.flushQueue();
			}
		}
	}

	/**
	 * Broadcast a message to all listeners subscribed to the given message
	 * types.
	 * 
	 * @param eventTypes
	 *            The list of types of messages which are being sent. A listener
	 *            will be notified if it is subscribed for a type in eventTypes
	 *            or if it is subscribed for UpdateableSubscriptionType.ALL.
	 */
	public void notifyListeners(SubscriptionType[] eventTypes) {

		// In queue mode, place the events in the queue
		if (queueCount > 0) {

			// Add each event type to the queue, avoiding repeats
			for (SubscriptionType event : eventTypes) {
				if (!messageQueue.contains(event)) {
					messageQueue.add(event);
				}
			}
		}

		// If not in queue mode, notify the listeners
		else {

			// A map from a temporary identification number to the listener
			// associated with it
			HashMap<Integer, IManagedUpdateableListener> listenerMap = new HashMap<Integer, IManagedUpdateableListener>();

			// A map from a temporary identification number to the a list of
			// subscription types
			HashMap<Integer, ArrayList<SubscriptionType>> messageMap = new HashMap<Integer, ArrayList<SubscriptionType>>();

			// The next ID number to use when a new listener is encountered in
			// the below search
			int nextID = 0;

			// Check each event type with registered listeners in the map
			for (SubscriptionType listenerType : subscriptionMap.keySet()) {

				// Whether there are any matches for this event type
				boolean match = false;

				// Listeners for all events are automatically updated
				if (listenerType == SubscriptionType.ALL) {
					match = true;
				}

				else

					// Check each event type to see if one matches
					for (SubscriptionType eventType : eventTypes) {
						if (listenerType == eventType
								&& eventType != SubscriptionType.ALL) {
							match = true;
							break;
						}
					}

				// If one of the event types is relevant to this subscription
				// list, broadcast the message
				if (match) {

					// Get the listeners of that type
					ArrayList<IManagedUpdateableListener> listeners = subscriptionMap
							.get(listenerType);

					// Place each listener and the correct event types in the
					// maps, assigning each a temporary matching ID.
					for (IManagedUpdateableListener listener : listeners) {

						// If the listener is already in the map...
						if (listenerMap.containsValue(listener)) {

							// Search for the ID value for the listener
							for (Integer i : listenerMap.keySet()) {

								// When a match is found, add the new type to
								// the map of messages for that ID
								if (listenerMap.get(i) == listener) {
									ArrayList<SubscriptionType> list = messageMap
											.get(i);
									list.add(listenerType);
									messageMap.put(i, list);
								}
							}
						}

						// If the listener is not already in the map, place it
						// there
						else {

							// Put the listener in the listener map
							listenerMap.put(nextID, listener);

							// Create a list of subscription types
							ArrayList<SubscriptionType> list;

							// If the type isn't ALL, initialize the list with
							// the current message type
							if (listenerType != SubscriptionType.ALL) {
								list = new ArrayList<SubscriptionType>();
								list.add(listenerType);
							}

							// If the type is ALL, put all the queued
							// subscription types in the list
							else {

								list = new ArrayList<SubscriptionType>(
										Arrays.asList(eventTypes));
							}
							messageMap.put(nextID, list);

							// Increment the ID
							nextID++;
						}
					}
				}

			}

			// For each listener, send it the message types it is subscribed for
			for (int i = 0; i < nextID; i++) {
				SubscriptionType[] list = new SubscriptionType[messageMap.get(i)
						.size()];
				list = messageMap.get(i).toArray(list);
				listenerMap.get(i).update(source, list);
			}

		}
	}

	/**
	 * Register a listener to receive updates from the given lift of event
	 * types.
	 * 
	 * @param listener
	 *            The listener which is being registered
	 * @param types
	 *            The list of event types the listener will receive
	 */
	public void register(IManagedUpdateableListener listener) {

		// Silently fail if listener is null
		if (listener != null) {

			// Poll the listener as to which event types it wants to receive
			// updates
			// for
			ArrayList<SubscriptionType> types = listener
					.getSubscriptions(source);

			// Add the listener to each type's list in the map
			for (SubscriptionType type : types) {

				// Get the current list of subscribers
				ArrayList<IManagedUpdateableListener> tempListeners = subscriptionMap
						.get(type);

				// If it is empty, create a new list
				if (tempListeners == null) {
					tempListeners = new ArrayList<IManagedUpdateableListener>();
				}

				// Add the listener to the map
				tempListeners.add(listener);
				subscriptionMap.put(type, tempListeners);
			}

		}
	}

	/**
	 * Setter method for the manager's parent.
	 * 
	 * @param parent
	 *            The manager's new parent
	 */
	public void setParent(UpdateableSubscriptionManager parent) {
		this.parent = parent;
	}

	/**
	 * Unregister a listener so that it will receive no new updates.
	 * 
	 * @param listener
	 *            The listener to be unregistered
	 */
	public void unregister(IManagedUpdateableListener listener) {

		// Try to remove the listener from each individual subscription list.
		for (SubscriptionType category : subscriptionMap.keySet()) {
			subscriptionMap.get(category).remove(listener);
		}

	}
}
