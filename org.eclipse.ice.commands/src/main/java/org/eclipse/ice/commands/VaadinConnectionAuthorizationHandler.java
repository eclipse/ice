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
 * This class enables the password authentication for remote commands through
 * vaadin
 * 
 * @author Joe Osborn
 *
 */
public class VaadinConnectionAuthorizationHandler extends ConnectionAuthorizationHandler {

	/**
	 * Default constructor
	 */
	public VaadinConnectionAuthorizationHandler() {
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#getPassword()}
	 */
	@Override
	protected char[] getPassword() {
		return null;
	}

}
