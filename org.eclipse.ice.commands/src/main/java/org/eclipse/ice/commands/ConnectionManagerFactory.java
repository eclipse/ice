/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/

package org.eclipse.ice.commands;

/**
 * This factory class returns a static instance of ConnectionManager. The class
 * is intended to act as a global ConnectionManager returner, so that there is 
 * only a single ConnectionManager that can be accessed by all of the classes.
 * This creates a cleaner API so that all classes can access the same connections
 * at any given time.
 * 
 * @author Joe Osborn
 *
 */
public class ConnectionManagerFactory {

	static ConnectionManager manager = new ConnectionManager();
	
	/**
	 * Default constructor
	 */
	public ConnectionManagerFactory() {
	}

	/**
	 * A function to return the static instance of ConnectionManager
	 * @return
	 */
	public static ConnectionManager getConnectionManager() {
		return manager;
		
	}
}
