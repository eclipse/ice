/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections;

/**
 * This enum defines the possible states of a viz service connection. This is
 * intended for use with the viz plots so they can keep users updated based on
 * the state of the associated connection.
 * 
 * @author Jordan Deyton
 *
 */
public enum ConnectionState {
	/**
	 * The connection is being established.
	 */
	Connecting,
	/**
	 * The connection is opened.
	 */
	Connected,
	/**
	 * The connection failed to open.
	 */
	Failed,
	/**
	 * The connection is closed.
	 */
	Disconnected;
}
