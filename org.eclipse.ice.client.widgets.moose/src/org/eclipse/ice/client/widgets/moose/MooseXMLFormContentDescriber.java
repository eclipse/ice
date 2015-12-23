/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation -
 *   Jay Jay Billings, Alex McCaskey
 *******************************************************************************/
package org.eclipse.ice.client.widgets.moose;

import org.eclipse.ice.persistence.xml.XMLFormContentDescriber;;

/**
 * This is a content describer for the XML files that are persisted by ICE's
 * default XMLPersistenceProvider for a MOOSE Workflow Item.
 *
 * @author Alex McCaskey
 *
 */
public class MooseXMLFormContentDescriber extends XMLFormContentDescriber {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.persistence.xml.XMLFormContentDescriber#isValidFile(java.
	 * lang.String)
	 */
	@Override
	public boolean isValidFile(String lines) {
		return lines.contains("<MOOSE") && lines.contains("MOOSE Workflow");
	}

}
