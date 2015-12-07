/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.datastructures.VizObject;

/**
 * A enumeration of the types of events for which an IVizUpdateableListener can
 * subscribe to listen for.
 * 
 * @author r8s
 *
 */
public enum UpdateableSubscriptionType {
	/**
	 * A special value denoting that a listener should be update upon any kind
	 * of event.
	 */
	All,

	/**
	 * Child events are fired when an object has a child object added or removed
	 * from it.
	 */
	Child,

	/**
	 * Property events are fired when an object's properties are changed,
	 * disregarding properties which are covered by another event type on this
	 * list.
	 */
	Property,

	/**
	 * Selection events are fired when an object is selected or deselected.
	 */
	Selection,

	/**
	 * Transformation events are fired when an object's graphical transformation
	 * is changed.
	 */
	Transformation

}
