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

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

/**
 * This is a builder class for constructing Components
 * 
 * @author Jay Jay Billings
 *
 */
public class ComponentBuilder {

	/**
	 * The ontology model from which all resources are built.
	 */
	private OntModel ontModel;

	/**
	 * Constructor
	 * 
	 * @param ICEOntModel The ICE ontology model.
	 */
	public ComponentBuilder(final OntModel ICEOntModel) {
		ontModel = ICEOntModel;
	}

	/**
	 * This operation executes the construction process for the Component.
	 * 
	 * @return the fully initialized component based on the build parameters
	 */
	public Resource build(Model dataModel) {
		OntClass compClass = ontModel.getOntClass(ICEConstants.NAMESPACE + "Component");
		Individual compIndividual = ontModel.createIndividual(compClass);

		ObjectProperty prop = ontModel.getObjectProperty(ICEConstants.NAMESPACE + "name");

		dataModel.createResource("https://www.galactic-empire.gov/data#Comp1", compClass)
				.addProperty(prop, "Tk-421");

		return compIndividual;
	}

}
