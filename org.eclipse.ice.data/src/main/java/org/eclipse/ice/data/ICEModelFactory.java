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

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

/**
 * The ModelFactory class is responsible for creating ICE RDF models and
 * associated resources. It is the basic abstract factory from which all ICE
 * data structures can be built.
 * 
 * @author Jay Jay Billings
 *
 */
public class ICEModelFactory {

	/**
	 * The ICE ontology
	 */
	private OntModel iceOntModel;

	/**
	 * Constructor responsible for initializing the entire ICE ontology for the
	 * factory.
	 */
	public ICEModelFactory() {
		Model baseModel = FileManager.get().loadModel("data/org.eclipse.ice.data.owl.ttl", null, "TURTLE");
		// No need for an inferencer... yet.
		iceOntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, baseModel);
	}

	/**
	 * This operation creates a basic RDF model for storing ICE data artifacts.
	 * 
	 * @return the empty RDF model
	 */
	public Model createModel() {
		Model model = ModelFactory.createDefaultModel();
		return model;
	}

	/**
	 * This operation returns a builder that can be used to construct a new ICE
	 * component.
	 * 
	 * @return the Component Builder for making new components
	 */
	public ComponentBuilder createComponent() {
		return new ComponentBuilder(iceOntModel);
	}

}
