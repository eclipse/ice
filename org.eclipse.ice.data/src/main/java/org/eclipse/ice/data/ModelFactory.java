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
import org.apache.jena.rdf.model.Resource;

/**
 * The ModelFactory class is responsible for creating ICE RDF models and
 * associated resources.
 * 
 * @author Jay Jay Billings
 *
 */
public class ModelFactory {

	/**
	 * This operation creates a basic ICE data RDF model.
	 * 
	 * @return the empty RDF model
	 */
	public Model createModel() {
		Model model = null;
		return model;
	}

	/**
	 * This operation returns a builder that can be used to construct a new ICE component.
	 * @return the Component Builder for making new components
	 */
	public ComponentBuilder createComponent() {
		return new ComponentBuilder(null);
	}

	public Resource createComponent(final int id) {
		Resource component = null;
		return component;
	}

	public Resource createComponent(final int id, final String name) {
		Resource component = null;
		return component;
	}

	public Resource createComponent(final int id, final String name, final String desc) {
		Resource component = null;
		return component;
	}

	public Resource createComponent(final int id, final String name, final String desc, final String context) {
		Resource component = null;
		return component;
	}
}
