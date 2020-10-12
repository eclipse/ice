/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.dev.annotations.processors;

/**
 * Exception raised during annotation extraction if the passed element is
 * invalid.
 * @author Daniel Bluhm
 */
public class InvalidElementException extends Exception {

	/**
	 * Exception with message.
	 * @param string message
	 */
	public InvalidElementException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1481453819406854006L;
}
