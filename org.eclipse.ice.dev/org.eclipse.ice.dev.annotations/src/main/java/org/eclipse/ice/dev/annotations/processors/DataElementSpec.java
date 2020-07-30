/******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Daniel Bluhm
 *****************************************************************************/
package org.eclipse.ice.dev.annotations.processors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.eclipse.ice.data.IPersistenceHandler;
import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataFieldJson;
import org.eclipse.ice.dev.annotations.Persisted;

import lombok.Getter;

/**
 * Helper class for extracting data from DataElementSpec classes and their
 * annotations.
 *
 * @author Daniel Bluhm
 */
public class DataElementSpec extends AnnotatedElement {

	/**
	 * The value appended to DataElement implementation class names.
	 */
	private final static String IMPL_SUFFIX = "Implementation";

	/**
	 * The value appended to DataElement Persistence Handler class names.
	 */
	private final static String PERSISTENCE_SUFFIX = "PersistenceHandler";

	/**
	 * The fully qualified name of this element.
	 */
	@Getter
	private String fullyQualifiedName;

	/**
	 * The name of the DataElement as extracted from the DataElement annotation.
	 */
	@Getter
	private String name;

	/**
	 * The package of this element represented as a String.
	 */
	@Getter
	private String packageName;

	/**
	 * The name of the collection as extracted from Persisted or null.
	 */
	@Getter
	private String collectionName;

	/**
	 * The DataFields found in this DataElementSpec.
	 */
	@Getter
	private List<DataFieldSpec> dataFields;

	/**
	 * Construct a DataElementSpec from an Element.
	 * @param element the element annotated with {@code @DataElement}
	 * @param elementUtils Elements class from processing environment
	 * @throws InvalidDataElementSpec if given element is in an invalid element
	 *         specification
	 */
	public DataElementSpec(Element element, Elements elementUtils) throws InvalidDataElementSpec {
		super(element, elementUtils);
		if (element.getKind() != ElementKind.CLASS) {
			throw new InvalidDataElementSpec(
				"DataElementSpec must be class, found " + element.toString()
			);
		}

		// Names
		this.name = this.extractName();
		String elementFQN = ((TypeElement) element).getQualifiedName().toString();
		this.packageName = null;
		final int lastDot = elementFQN.lastIndexOf('.');
		if (lastDot > 0) {
			this.packageName = elementFQN.substring(0, lastDot);
			this.fullyQualifiedName = this.packageName + "." + this.name;
		} else {
			this.fullyQualifiedName = this.name;
		}
		this.collectionName = this.extractCollectionName();

		// Gather DataFields
		this.dataFields = this.extractDataFields();
	}

	/**
	 * Extract data fields from enclosed elements.
	 * @return list of data field specs
	 */
	public List<DataFieldSpec> extractDataFields() {
		return this.element.getEnclosedElements().stream()
			.filter(DataFieldSpec::isDataField)
			.map(enclosedElement -> new DataFieldSpec(enclosedElement, elementUtils))
			.collect(Collectors.toList());
	}

	/**
	 * Return the element name as extracted from the DataElement annotation.
	 * @return the extracted name
	 */
	public String extractName() {
		return this.getAnnotation(DataElement.class)
			.map(e -> e.name())
			.orElse(null);
	}

	/**
	 * Return the collection name as extracted from the Persisted annotation.
	 * @return the extracted collection name
	 */
	public String extractCollectionName() {
		return this.getAnnotation(Persisted.class)
			.map(p -> p.collection())
			.orElse(null);
	}

	/**
	 * Get the name of the Implementation to be generated.
	 * @return implementation name
	 */
	public String getImplName() {
		return this.name + IMPL_SUFFIX;
	}

	/**
	 * Get the fully qualified name of the Implementation to be generated.
	 * @return fully qualified implementation name
	 */
	public String getQualifiedImplName() {
		return this.fullyQualifiedName + IMPL_SUFFIX;
	}

	/**
	 * Get the name of the Persistence Handler to be generated.
	 * @return persistence handler name
	 */
	public String getPersistenceHandlerName() {
		return this.name + PERSISTENCE_SUFFIX;
	}

	/**
	 * Get the fully qualified name of the Persistence Handler to be generated.
	 * @return fully qualified persistence handler name
	 */
	public String getQualifiedPersistenceHandlerName() {
		return this.fullyQualifiedName + PERSISTENCE_SUFFIX;
	}

	/**
	 * Collect Fields from DataField and DataFields Annotations.
	 * @return discovered fields
	 */
	public List<Field> fieldsFromDataFields() {
		return dataFields.stream()
			.map(dataField -> dataField.toField())
			.collect(Collectors.toList());
	}

	/**
	 * Collect JSON File Strings from DataFieldJson Annotations.
	 * @return discovered JSON file strings
	 */
	public List<String> getDataFieldJsonFileNames() {
		return this.getAnnotation(DataFieldJson.class)
			.map(jsons -> Arrays.asList(jsons.value()))
			.orElse(Collections.emptyList());
	}

	/**
	 * Get the interface name of the persistence handler.
	 *
	 * Right now, this is simply the IPersistenceHandler interface. In the future
	 * this will be a data element specific interface.
	 * @return Interface name of the Persistence Handler
	 */
	public String getPersistenceHandlerInterfaceName() {
		return org.eclipse.ice.data.IPersistenceHandler.class.getSimpleName();
	}
}
