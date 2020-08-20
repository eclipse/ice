/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Walsh - Initial implementation
 *******************************************************************************/
package org.eclipse.ice.dev.annotations.processors;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataField;

/**
 * Extraction helper that specializes in pulling Spec data from Spec classes
 *
 */
public class SpecExtractionHelper implements ExtractionHelper {

	/**
	 * Convert Element element and field config fieldInfo to a Field POJO
	 * 
	 * @param element
	 * @param elementUtils
	 * @param fieldInfo
	 * @param handledAnnotations
	 * @return
	 */
	public Field convertElementToField(Element element, Elements elementUtils, Field fieldInfo,
			List<String> handledAnnotations) {
		return Field.builder().name(extractFieldName(element)).type(extractFieldType(element))
				.defaultValue(extractDefaultValue(element, elementUtils))
				.docString(extractDocString(element, elementUtils))
				.annotations(extractAnnotations(element, handledAnnotations))
				.modifiersToString(extractModifiers(element)).validator(extractValidator(element))
				.getter(fieldInfo.isGetter()).setter(fieldInfo.isSetter()).match(fieldInfo.isMatch())
				.unique(fieldInfo.isUnique()).searchable(fieldInfo.isSearchable()).nullable(fieldInfo.isNullable())
				.build();
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	public String extractValidator(Element element) {
		// TODO: implement method for DataModel annotation
		return null;
	}

	/**
	 * Determine if an Element is annotated with a give Annotation
	 * 
	 * @param element
	 * @param annotation
	 * @return
	 */
	public boolean hasAnnotation(Element element, Class<? extends Annotation> annotation) {
		return element.getAnnotation(annotation) != null;
	}

	/**
	 * Return the set of access modifiers on this Field.
	 * 
	 * @param element
	 * @return extract field modifiers
	 * @see Modifier
	 */
	public Set<Modifier> extractModifiers(Element element) {
		return element.getModifiers();
	}

	/**
	 * Return the set of annotations on this DataField, excepting the DataField
	 * Annotation itself.
	 * 
	 * @param element
	 * @param handledAnnotations
	 * @return extracted annotations, excluding DataField related annotations
	 */
	public List<String> extractAnnotations(Element element, List<String> handledAnnotations) {
		return element.getAnnotationMirrors().stream()
				.filter(mirror -> !handledAnnotations.contains(mirror.getAnnotationType().toString()))
				.map(mirror -> mirror.toString()).collect(Collectors.toList());
	};

	/**
	 * Return the class of this Field.
	 * 
	 * @param element
	 * @return extracted field type
	 */
	public TypeMirror extractFieldType(Element element) {
		return element.asType();
	}

	/**
	 * Return the name of this Field.
	 * 
	 * @param element
	 * @return extracted field name
	 */
	public String extractFieldName(Element element) {
		return element.getSimpleName().toString();
	}

	/**
	 * Return the DocString of this Field.
	 * 
	 * @param element
	 * @return extracted doc comment
	 */
	public String extractDocString(Element element, Elements elementUtils) {
		return elementUtils.getDocComment(element);
	}

	/**
	 * Extract the value that a given DataField should be initialized to based on
	 * the Default annotation value
	 * 
	 * @param element
	 * @param elementUtils
	 * @return
	 */
	public String extractDefaultValue(Element element, Elements elementUtils) {
		String retval = null;
		DataField.Default defaults = element.getAnnotation(DataField.Default.class);
		if (defaults != null) {
			if (defaults.isString()) {
				retval = elementUtils.getConstantExpression(defaults.value());
			} else {
				retval = defaults.value();
			}
		} else if (element.getModifiers().contains(Modifier.FINAL)) {
			retval = elementUtils.getConstantExpression(((VariableElement) element).getConstantValue());
		}
		return retval;
	};

	/**
	 * Collect all fields in the given element. Entire member variables can be
	 * ignored with the fieldFilter predicate. handledAnnotations determines which
	 * annotations on the member variables should be maintained when converted into
	 * a field.
	 * 
	 * @param element
	 * @param elementUtils
	 * @param fieldFilter
	 * @param handledAnnotations
	 * @return
	 */
	public List<Field> getAllFields(Element element, Elements elementUtils, Predicate<Element> fieldFilter,
			List<String> handledAnnotations) {
		List<Field> elementList = null;
		if (element instanceof TypeElement) {

			elementList = ElementFilter.fieldsIn(((TypeElement) element).getEnclosedElements()).stream()
					.filter(fieldFilter).map(el -> {

						DataField dataField = el.getAnnotation(DataField.class);
						Field fieldInfo;
						if (dataField == null) {
							fieldInfo = Field.builder().build();
						} else {
							fieldInfo = Field.builder().getter(dataField.getter()).setter(dataField.setter())
									.match(dataField.match()).unique(dataField.unique())
									.searchable(dataField.searchable()).nullable(dataField.nullable()).build();
						}

						return (Field) convertElementToField(el, elementUtils, fieldInfo, handledAnnotations);
					}).collect(Collectors.toList());
		}
		return elementList;
	}
}
