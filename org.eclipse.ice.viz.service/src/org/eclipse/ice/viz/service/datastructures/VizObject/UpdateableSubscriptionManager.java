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
	IVizUpdateable source; 
	
	/**
	 * A map of registered listeners associated with the event types they are registered to receive.
	 */
	HashMap<IVizUpdateableListener, ArrayList<UpdateableSubscription>> subscriptionMap;
	
	/**
	 * The default constructor.
	 * 
	 * @param source The object whose listeners this manager will control communications for.
	 */
	public UpdateableSubscriptionManager(IVizUpdateable source){
		this.source = source;
	}
	
	/**
	 * Send an update to each listener 
	 * 
	 * @param source
	 * @param eventTypes
	 */
	public void notifyListeners(UpdateableSubscription[] eventTypes){
		
		//Check each listener in the map
		for(IVizUpdateableListener listener :subscriptionMap.keySet()){
			
			//Get the event types the listener is subscribed for. 
			ArrayList<UpdateableSubscription> types = subscriptionMap.get(listener);
			
			//If the listener is subscribed for all events, fire an update
			if(types.contains(UpdateableSubscription.All)){
				listener.update(source);
			}
			
			else{
				
				//Check each of the types for the event. If any match, fire the update
				for(UpdateableSubscription type : eventTypes){
					
					if(type == UpdateableSubscription.All || types.contains(type)){
						listener.update(source);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Register a listener to receive updates from the given lift of event types.
	 * 
	 * @param listener The listener which is being registered
	 * @param types The list of event types the listener will receive 
	 */
	public void register(IVizUpdateableListener listener, ArrayList<UpdateableSubscription> types){
		subscriptionMap.put(listener, types);
	}
	
	/**
	 * Unregister a listener so that it will receive no new updates.
	 * 
	 * @param listener The listener to be unregistered
	 */
	public void unregister(IVizUpdateableListener listener){
		subscriptionMap.remove(listener);
	}
}
