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
package org.eclipse.ice.commands.notification;

/**
 * This class provides email updates for Command job statuses
 * @author Joe Osborn
 *
 */
public class EmailUpdateHandler implements ICommandUpdateHandler{

	private String emailAddress = "";
	
	/**
	 * Default constructor
	 */
	public EmailUpdateHandler() {
	}

	public void setOption(String option) {
	}

	public void postUpdate() {
	}



}
