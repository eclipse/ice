/******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *****************************************************************************/
package org.eclipse.ice.tasks;

/**
 * Hooks are special types of Actions that execute before or after other
 * Actions and conditionally in place of other Actions.
 * 
 * At the moment, this is simply a Marker interface.
 * 
 * @author Jay Jay Billings
 *
 */
public interface Hook<T> extends Action<T> {

	
	
}
