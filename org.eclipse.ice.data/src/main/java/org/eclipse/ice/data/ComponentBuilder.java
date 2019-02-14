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

import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

/**
 * This is a builder class for constructing Components. It uses a standard
 * builder API to dynamically build and construct a new Component based on the
 * way the functions are called.
 * 
 * All components should be constructed by using the with* star operations to
 * configure data members before the build function (build()) is called.
 * 
 * The builder will configure default values for the field variables of the
 * components that it constructs. The defaults for this class are:<br>
 * name = "NO_NAME"<br>
 * description = "NO_DESCRIPTION"<br>
 * context = "DEFAULT"<br>
 * id = 0<br>
 * 
 * The builder will clear its state between separate calls to the build
 * function. That is, successively calling build() will not return two resources
 * with the same values. The builder will do it automatically, although clients
 * may elect to do it themselves by calling clear().
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
	 * The name of the component that should be built.
	 */
	private String name;

	/**
	 * The identifier of the component that should be built.
	 */
	private long id;

	/**
	 * The unique context in which the Component exists
	 */
	private String context;

	/**
	 * A description of the component.
	 */
	private String description;

	/**
	 * And OWL class, managed by the RDF framework, for the root component class in
	 * the ontology.
	 */
	private OntClass compClass;

	/**
	 * An OWL property for the name
	 */
	private ObjectProperty nameProp;

	/**
	 * An OWL property for the description
	 */
	private ObjectProperty descProp;

	/**
	 * An OWL property for the context
	 */
	private ObjectProperty contextProp;

	/**
	 * An OWL property for the id
	 */
	private ObjectProperty idProp;

	/**
	 * Constructor
	 * 
	 * @param ICEOntModel The ICE ontology model.
	 */
	public ComponentBuilder(final OntModel ICEOntModel) {
		ontModel = ICEOntModel;
		// Grab all the properties for fast references
		compClass = ontModel.getOntClass(ICEConstants.COMPONENT);
		nameProp = ontModel.getObjectProperty(ICEConstants.NAME);
		descProp = ontModel.getObjectProperty(ICEConstants.DESCRIPTION);
		contextProp = ontModel.getObjectProperty(ICEConstants.CONTEXT);
		idProp = ontModel.getObjectProperty(ICEConstants.ID);
		
		// Set the default values.
		clear();
	}

	/**
	 * This operation clears the configured state from the previous build and
	 * returns the builder to default values. In general, clients should not have to
	 * call this but may if they like.
	 */
	public void clear() {
		name = "NO_NAME";
		context = "DEFAULT";
		description = "NO_DESCRIPTION";
		id = 0;
	}

	/**
	 * This operation sets the name of the component. It should be called before the
	 * build operation.
	 * 
	 * @param compName the name of the component
	 * @return the updated component builder
	 */
	public ComponentBuilder withName(final String compName) {
		name = compName;
		return this;
	}

	/**
	 * This operation sets the id of the component. It should be called before the
	 * build operation.
	 * 
	 * @param compId the component's id
	 * @return the updated component builder
	 */
	public ComponentBuilder withId(final long compId) {
		id = compId;
		return this;
	}

	/**
	 * This operation sets the description of the component. It should be called
	 * before the build operation.
	 * 
	 * @param compDesc the description of the component
	 * @return the updated component builder
	 */
	public ComponentBuilder withDescription(final String compDesc) {
		description = compDesc;
		return this;
	}

	/**
	 * This operation sets the context tag of the component. It should be called
	 * before the build operation. The context is a special property that identifies
	 * the environment in which the component exists and can be used for
	 * specialization purposes when environments change.
	 * 
	 * @param compDesc the context of the component
	 * @return the updated component builder
	 */
	public ComponentBuilder withContext(final String compContext) {
		context = compContext;
		return this;
	}

	/**
	 * This operation executes the construction process for the Component and builds
	 * the ontological entity. This operation always builds the Component with some
	 * property values attached, whether the be user-specified or default values.
	 * 
	 * @param the data model to which the newly created resource should be attached.
	 * @param the IRI of the new component
	 * @return the fully initialized component based on the build parameters
	 */
	public Resource build(Model dataModel, final String iri) {

		Resource resource = dataModel.createResource(iri, compClass).addProperty(nameProp, name)
				.addProperty(idProp, String.valueOf(id)).addProperty(contextProp, context)
				.addProperty(descProp, description);

		// Clear the state
		clear();
		
		return resource;
	}

}
