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
package org.eclipse.ice.viz.service.datastructures.VizObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class which manages a list of IVizUpdateable listeners and filters updates
 * by matching types of events to listeners which have subscribed to the
 * corresponding event type(s).
 * 
 * @author Robert Smith
 *
 */
public class UpdateableSubscriptionManager {

	/**
	 * The updateable object the manager is controlling the message passing for.
	 */
	IManagedVizUpdateable source;

	/**
	 * A map of registered listeners associated with the event types they are
	 * registered to receive.
	 */
	HashMap<UpdateableSubscriptionType, ArrayList<IManagedVizUpdateableListener>> subscriptionMap = new HashMap<UpdateableSubscriptionType, ArrayList<IManagedVizUpdateableListener>>();

	/**
	 * A list of queued messages to be delivered all at once, for cases where
	 * multiple events occur in quick succession.
	 */
	ArrayList<UpdateableSubscriptionType> messageQueue = new ArrayList<UpdateableSubscriptionType>();

	/**
	 * Keeps track of whether the manager is in queue mode. In queue mode,
	 * events will be saved to the message queue instead of sent to listeners.
	 * When queue mode is exited, all queued events are to be fired in bulk with
	 * a single call to a listener's update() method.
	 * 
	 * The number represents the amount of times the manager has been ordered to
	 * queue messages. Only once it has been ordered to flush the queue that
	 * many times will it actually begin notifying listeners. Thus, the manager
	 * is in queue mode if and only if the variable queue is greater than 0.
	 */
	int queue = 0;

	/**
	 * A parent manager belonging to a registered listener. This parent should
	 * be an object which can safely be assummed to never need to send an update
	 * notification while waiting for an update from this manager's source, such
	 * as in the case of a controller and model from the viz.service.modeling
	 * packages.
	 */
	UpdateableSubscriptionManager parent;

	/**
	 * The default constructor.
	 * 
	 * @param source
	 *            The object whose listeners this manager will control
	 *            communications for.
	 */
	public UpdateableSubscriptionManager(IManagedVizUpdateable source) {
		this.source = source;
	}

	/**
	 * Sets the manager in queue mode.
	 */
	public void enqueue() {
		queue++;

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
		queue--;
		if (queue == 0) {

			// Handle the message queue if there is one
			if (!messageQueue.isEmpty()) {

				// Send the queued messages
				UpdateableSubscriptionType[] types = new UpdateableSubscriptionType[messageQueue
						.size()];
				notifyListeners(messageQueue.toArray(types));

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
	 * Send an update to each listener
	 * 
	 * @param source
	 * @param eventTypes
	 */
	public void notifyListeners(UpdateableSubscriptionType[] eventTypes) {

		// In queue mode, place the events in the queue
		if (queue > 0) {

			// Add each event type to the queue, avoiding repeats
			for (UpdateableSubscriptionType event : eventTypes) {
				if (!messageQueue.contains(event)) {
					messageQueue.add(event);
				}
			}
		}

		// If not in queue mode, notify the listeners
		else {

			// Check each event type with registered listeners in the map
			for (UpdateableSubscriptionType listenerType : subscriptionMap
					.keySet()) {

				// Whether there are any matches for this event type
				boolean match = false;

				// Listeners for all events are automatically updated
				if (listenerType == UpdateableSubscriptionType.All) {
					match = true;
				}

				else

					// Check each event type to see if one matches
					for (UpdateableSubscriptionType eventType : eventTypes) {
						if (listenerType == eventType) {
							match = true;
							break;
						}
					}

				// If one of the event types is relevant to this subscription
				// list, broadcast the message
				if (match) {

					// Get the listeners of that type
					ArrayList<IManagedVizUpdateableListener> listeners = subscriptionMap
							.get(listenerType);

					// Update each listener, providing event types if it can
					// handle that kind of input
					for (IManagedVizUpdateableListener listener : listeners) {
						listener.update(source, eventTypes);

					}
				}

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
	public void register(IManagedVizUpdateableListener listener) {

		// Poll the listener as to which event types it wants to receive updates
		// for
		ArrayList<UpdateableSubscriptionType> types = listener
				.getSubscriptions(source);

		// Add the listener to each type's list in the map
		for (UpdateableSubscriptionType type : types) {

			// Get the current list of subscribers
			ArrayList<IManagedVizUpdateableListener> tempListeners = subscriptionMap
					.get(type);

			// If it is empty, create a new list
			if (tempListeners == null) {
				tempListeners = new ArrayList<IManagedVizUpdateableListener>();
			}

			// Add the listener to the map
			tempListeners.add(listener);
			subscriptionMap.put(type, tempListeners);
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
	public void unregister(IManagedVizUpdateableListener listener) {
		subscriptionMap.remove(listener);
	}
}
