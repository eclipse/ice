/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections.test;

/**
 * This class is used as a test "connection" for testing viz service connection
 * infrastructure code. It serves no other purpose than to imitate some custom
 * viz connection client code that would connect to a third-party viz service
 * instance.
 * 
 * @author Jordan Deyton
 *
 */
public class FakeConnection {

	/**
	 * "Establishes" the connection. This operation only sleeps for the
	 * specified time and returns.
	 * 
	 * @param ms
	 *            The time to delay when connecting. If less than or equal to
	 *            zero, it simply returns.
	 */
	public void connect(long ms) {
		if (ms > 0) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * "Closes" the connection. This operation only sleeps for the specified
	 * time and returns.
	 * 
	 * @param ms
	 *            The time to delay when disconnecting. If less than or equal to
	 *            zero, it simply returns.
	 */
	public void disconnect(long ms) {
		if (ms > 0) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}