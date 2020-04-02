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

import java.io.IOException;

/**
 * This class posts Command job updates to an HTTP link via POST
 * 
 * @author Joe Osborn
 *
 */
public class HTTPCommandUpdateHandler implements ICommandUpdateHandler{

	
	private String HTTPAddress = "";
	
	/**
	 * Default constructor
	 */
	public HTTPCommandUpdateHandler() {
	}

	public void setOption(String option) {
		this.HTTPAddress = option;
		
	}

	public void postUpdate() throws IOException {
		
	}



}
