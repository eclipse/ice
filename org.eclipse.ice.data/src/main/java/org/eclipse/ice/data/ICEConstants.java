/******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *****************************************************************************/
package org.eclipse.ice.data;

/**
 * This class stores ICE Data Model constants used throughout the code base.
 * 
 * @author Jay Jay Billings
 *
 */
public class ICEConstants {

	/**
	 * Root Namespace of the ICE ontology
	 */
	public static String NAMESPACE = "https://www.eclipse.org/ice#";

	/**
	 * Fully qualified type name of ICE Components
	 */
	public static String COMPONENT = NAMESPACE + "Component";

	/**
	 * Fully qualified type name of ICE Identifiable names
	 */
	public static String NAME = NAMESPACE + "name";

	/**
	 * Fully qualified type name of ICE Identifiable descriptions
	 */
	public static String DESCRIPTION = NAMESPACE + "desc";

	/**
	 * Fully qualified type name of ICE Identifiable context
	 */
	public static String CONTEXT = NAMESPACE + "context";

	/**
	 * Fully qualified type name of ICE Identifiable identifier numbers
	 */
	public static String ID = NAMESPACE + "identifier";

}
