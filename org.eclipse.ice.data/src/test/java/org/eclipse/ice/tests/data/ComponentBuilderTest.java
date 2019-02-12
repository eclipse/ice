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
package org.eclipse.ice.tests.data;

import static org.junit.Assert.*;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.eclipse.ice.data.ComponentBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests {@link ComponentBuilder}.
 * 
 * @author Jay Jay Billings
 */
public class ComponentBuilderTest {

	// Reference ICE ontology
	protected static OntModel ICEOntModel;

	// Component builder test instance
	protected static ComponentBuilder builder;

	// Test data model
	protected static Model dataModel;

	// Ontology namespace
	protected String namespace = "https://www.eclipse.org/ice#";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Load the ontology
		Model baseModel = FileManager.get().loadModel("data/ice-owl.ttl", null, "TURTLE");
		// No need for a complicated inferencer
		ICEOntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, baseModel);
		// Setup the builder that is used for *all* the tests. Note that the ontology is
		// injected.
		builder = new ComponentBuilder(ICEOntModel);
		// Setup the default data model for the test. Note that this is different than
		// the ontology model in that it will be filled with data based on the ontology.
		dataModel = ModelFactory.createDefaultModel();
	}

	/**
	 * This operation insures that the build() operation works.
	 */
	@Test
	public void testBuild() {
		Resource comp = null;
		comp = builder.build(dataModel);
		// Make sure the component exists
		assertNotNull(comp);
		// Check the namespace
		assertEquals(namespace, comp.getNameSpace());

		// I don't know if namespaces make sense as

		// Print the properties to examine them
		System.out.println("-----");
		for (StmtIterator foo = comp.listProperties(); foo.hasNext();) {
			System.out.println(foo.next().toString());
		}

		return;
	}

}
