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
 * This class represents a connection to a remote system. This could be a system
 * that is physically remote or remote in a process sense.
 * 
 * @author Jay Jay Billings
 *
 */
public class Connection {

	/**
	 * The particular configuration for a particular connection
	 */
	ConnectionConfiguration configuration;

	/**
	 * A name identifier for a particular connection.
	 */
	private String name;
	
	/**
	 *  Default constructor
	 */

	public Connection() {
	}

}
