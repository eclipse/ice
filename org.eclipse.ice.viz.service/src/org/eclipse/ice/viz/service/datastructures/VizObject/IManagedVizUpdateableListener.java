/*******************************************************************************
 * Copyright (c) 2011, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay 
 *   Billings
 *******************************************************************************/
package org.eclipse.ice.viz.service.datastructures.VizObject;

import java.util.ArrayList;

/**
 * An extension of IVizUpdateableListener which can receive the type of event that triggered the update.
 * 
 * @author Robert Smith
 *
 */
public interface IManagedVizUpdateableListener extends IVizUpdateableListener{

	/**
	 * Receive an update, including the source component and type of event that triggered the update.
	 * 
	 * @param component The updateable component the update is coming from
	 * @param type The event type that of the update
	 */
	public void update(IVizUpdateable component, UpdateableSubscription[] type);
}
