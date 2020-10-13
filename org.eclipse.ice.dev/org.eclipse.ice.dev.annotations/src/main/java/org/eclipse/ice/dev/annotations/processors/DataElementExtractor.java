/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Michael Walsh - Initial implementation
 *     Daniel Bluhm - Modifications
 *******************************************************************************/
package org.eclipse.ice.dev.annotations.processors;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

import lombok.Builder;

/**
 * Extractor of DataElement metadata from annotated elements.
 *
 * @author Michael Walsh
 * @author Daniel Bluhm
 */
public class DataElementExtractor
	extends AbstractAnnotationExtractor<DataElementMetadata> {

	/**
	 * Extractor for data fields found on data element.
	 */
	private DataFieldExtractor dataFieldExtractor;

	/**
	 * Create DataElement Annotation Extractor.
	 * @param elementUtils Elements from processing environment.
	 * @param dataFieldExtractor Extractor for elements annotated with
	 *        DataField.
	 */
	@Builder
	public DataElementExtractor(
		Elements elementUtils,
		DataFieldExtractor dataFieldExtractor
	) {
		super(elementUtils);
		this.dataFieldExtractor = dataFieldExtractor;
	}

	@Override
	public DataElementMetadata extract(Element element) throws InvalidElementException {
		if (element.getKind() != ElementKind.CLASS) {
			throw new InvalidElementException(
				"Element must be class, found " + element.toString()
			);
		}
		if (!hasAnnotation(element, DataElement.class)) {
			throw new InvalidElementException(
				"Element is not annotated with DataElement"
			);
		}
		Fields fields = new Fields();
		fields.collect(DefaultFields.get());
		fields.collect(extractFields(element));

		DataElementMetadata data = DataElementMetadata.builder()
			.name(extractName(element))
			.packageName(extractPackageName(element))
			.fields(fields)
			.build();
		return data;
	}

	/**
	 * Extract name from DataElement annotation.
	 * @param element from which name will be extracted.
	 * @return String or null if DataElement annotation is missing.
	 */
	private String extractName(Element element) {
		return getAnnotation(element, DataElement.class)
			.map(DataElement::name)
			.orElse(null);
	}

	/**
	 * Determine package name from element.
	 * @param element from which package name will be determined.
	 * @return String or null if element is not in a package.
	 */
	private String extractPackageName(Element element) {
		String elementFQN = ((TypeElement) element).getQualifiedName().toString();
		String packageName = null;
		final int lastDot = elementFQN.lastIndexOf('.');
		if (lastDot > 0) {
			packageName = elementFQN.substring(0, lastDot);
		}
		return packageName;
	}

	/**
	 * Use DataFieldExtractor to extract DataField annotated elements contained
	 * in this DataElement.
	 * @param element containing DataFields.
	 * @return extracted Fields.
	 */
	private List<Field> extractFields(Element element) {
		return element.getEnclosedElements().stream()
			.filter(child -> child.getAnnotation(DataField.class) != null)
			.map(dataField -> {
				try {
					return dataFieldExtractor.extract(dataField);
				} catch (InvalidElementException e) {
					logger.warn(
						"Invalid element encountered while extracting DataField",
						e
					);
					return null;
				}
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}
}
