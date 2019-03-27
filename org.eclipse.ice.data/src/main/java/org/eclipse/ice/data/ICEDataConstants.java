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

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

/**
 * This class stores ICE Data Model constants used throughout the code base.
 * 
 * @author Jay Jay Billings
 *
 */
public class ICEDataConstants {

	/**
	 * A private model used for creating property and resource references.
	 */
	private static final Model refModel = ModelFactory.createDefaultModel();

	/**
	 * Root Namespace of the ICE ontology
	 */
	public static String NAMESPACE = "https://www.eclipse.org/ice/data#";

	/**
	 * Fully qualified type name of ICE Components
	 */
	public static String COMPONENT = NAMESPACE + "Component";

	/**
	 * Fully qualified type name of ICE Identifiable names
	 */
	public static String NAME = NAMESPACE + "name";

	/**
	 * A property reference for the ICE Identifiable name property
	 */
	public static final Property NAME_PROPERTY = refModel.createProperty(NAME);

	/**
	 * Fully qualified type name of ICE Identifiable descriptions
	 */
	public static String DESCRIPTION = NAMESPACE + "desc";

	/**
	 * A property reference for the ICE Identifiable description property
	 */
	public static final Property DESC_PROPERTY = refModel.createProperty(DESCRIPTION);

	/**
	 * Fully qualified type name of ICE Identifiable context
	 */
	public static String CONTEXT = NAMESPACE + "context";

	/**
	 * A property reference for the ICE Identifiable context property
	 */
	public static final Property CONTEXT_PROPERTY = refModel.createProperty(CONTEXT);
	
	/**
	 * Fully qualified type name of ICE Identifiable identifier numbers
	 */
	public static String ID = NAMESPACE + "identifier";

	/**
	 * A property reference for the ICE Identifiable identifier numbers
	 */
	public static final Property ID_PROPERTY = refModel.createProperty(ID);
	
}
