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
import org.apache.jena.util.FileManager;
import org.eclipse.ice.data.ComponentBuilder;
import org.eclipse.ice.data.ICEConstants;
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
	protected String namespace = "https://www.galactic-empire.gov/data#";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Load the ontology
		Model baseModel = FileManager.get().loadModel("data/org.eclipse.ice.data.owl.ttl", null, "TURTLE");
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
	 * This operation insures that the build() operation works in its default
	 * configuration.
	 */
	@Test
	public void testDefaultBuild() {
		String name = "NO_NAME", context = "DEFAULT", desc = "NO_DESCRIPTION";
		long id = 0;
		// Create the IRI
		String iri = namespace + "Comp1";

		// Build the component
		Resource comp = builder.build(dataModel, iri);
		// Make sure the component exists
		assertNotNull(comp);
		// Check the deets, namespace first
		assertEquals(namespace, comp.getNameSpace());
		// name
		assertEquals(name, comp.getProperty(ICEConstants.NAME_PROPERTY).getObject().toString());
		// context
		assertEquals(context,
				comp.getProperty(ICEConstants.CONTEXT_PROPERTY).getObject().toString());
		// description
		assertEquals(desc, comp.getProperty(ICEConstants.DESC_PROPERTY).getObject().toString());
		// id
		assertEquals(id,
				comp.getProperty(ICEConstants.ID_PROPERTY).getObject().asLiteral().getLong());

		// Have a look at it
		dataModel.write(System.out, "TURTLE");

		// Clear the data model before using it again to avoid test errors
		dataModel.removeAll();
		
		return;
	}

	/**
	 * This operation insures that the build() operation works.
	 */
	@Test
	public void testBuild() {
		String name = "Tk-421", context = "bay-gaurd",
				desc = "Presently guarding a YT-1300 freighter suspected of being the same one that "
						+ "blasted out of the Mos Eisley spaceport.";
		long id = 421;
		// Create the IRI
		String iri = namespace + "Trooper421";

		// Build the component
		Resource comp = builder.withName(name).withContext(context).withDescription(desc).withId(id)
				.build(dataModel, iri);
		// Make sure the component exists
		assertNotNull(comp);
		// Check the deets, namespace first
		assertEquals(namespace, comp.getNameSpace());
		// name
		assertEquals(name, comp.getProperty(ICEConstants.NAME_PROPERTY).getObject().toString());
		// context
		assertEquals(context,
				comp.getProperty(ICEConstants.CONTEXT_PROPERTY).getObject().toString());
		// description
		assertEquals(desc, comp.getProperty(ICEConstants.DESC_PROPERTY).getObject().toString());
		// id
		assertEquals(id,
				comp.getProperty(ICEConstants.ID_PROPERTY).getObject().asLiteral().getLong());

		// Have a look at it
		dataModel.write(System.out, "TURTLE");

		// Clear the data model before using it again to avoid test errors
		dataModel.removeAll();
		
		return;
	}
	
	/**
	 * This operation insures that the build() operation works.
	 */
	@Test
	public void testClearBuild() {
		String name = "Tk-422", context = "bay-gaurd",
				desc = "Presently guarding a YT-1300 freighter suspected of being the same one that "
						+ "blasted out of the Mos Eisley spaceport.";
		String defaultName = "NO_NAME", defaultContext = "DEFAULT", defaultDesc = "NO_DESCRIPTION";
		long defaultId = 0, id = 422;
		// Create the IRI
		String iri = namespace + "Trooper422";

		// Build the component
		Resource comp = builder.withName(name).withContext(context).withDescription(desc).withId(id)
				.build(dataModel, iri);
		// Make sure the component exists
		assertNotNull(comp);
		// Check the deets, namespace first
		assertEquals(namespace, comp.getNameSpace());
		// name
		assertEquals(name, comp.getProperty(ICEConstants.NAME_PROPERTY).getObject().toString());
		// context
		assertEquals(context,
				comp.getProperty(ICEConstants.CONTEXT_PROPERTY).getObject().toString());
		// description
		assertEquals(desc, comp.getProperty(ICEConstants.DESC_PROPERTY).getObject().toString());
		// id
		assertEquals(id,
				comp.getProperty(ICEConstants.ID_PROPERTY).getObject().asLiteral().getLong());

		// Build the component
		iri = namespace + "Comp2";
		Resource resetComp = builder.build(dataModel, iri);
		// Make sure the component exists
		assertNotNull(resetComp);
		// Check the deets, namespace first
		assertEquals(namespace, resetComp.getNameSpace());
		// name
		assertEquals(defaultName, resetComp.getProperty(ICEConstants.NAME_PROPERTY).getObject().toString());
		// context
		assertEquals(defaultContext,
				resetComp.getProperty(ICEConstants.CONTEXT_PROPERTY).getObject().toString());
		// description
		assertEquals(defaultDesc, resetComp.getProperty(ICEConstants.DESC_PROPERTY).getObject().toString());
		// id
		assertEquals(defaultId,
				resetComp.getProperty(ICEConstants.ID_PROPERTY).getObject().asLiteral().getLong());
		
		// Have a look at it
		dataModel.write(System.out, "TURTLE");
		
		// Clear the data model before using it again to avoid test errors
		dataModel.removeAll();

		return;
	}

}
