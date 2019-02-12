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

import java.util.Iterator;

import org.apache.http.client.cache.ResourceFactory;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.vocabulary.XSD;

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
	 * The base model namespace
	 */
	private static String namespace;

	public ComponentBuilder(final OntModel ICEOntModel) {
		ontModel = ICEOntModel;
		namespace = ontModel.getNsPrefixURI("ice");

	}

	/**
	 * This operation executes the construction process for the Component.
	 * 
	 * @return the fully initialized component based on the build parameters
	 */
	public Resource build(Model dataModel) {
		OntClass compClass = ontModel.getOntClass(namespace + "Component");
		Individual compIndividual = ontModel.createIndividual(compClass);

		ObjectProperty prop = ontModel.getObjectProperty(namespace + "name");

		dataModel.createResource("https://www.galactic-empire.gov/data#Comp1", compClass).addProperty(prop, "Tk-421");

		return compIndividual;
	}

}
