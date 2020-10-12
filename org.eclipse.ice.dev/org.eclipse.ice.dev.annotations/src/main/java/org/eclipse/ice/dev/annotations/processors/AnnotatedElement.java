/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.dev.annotations.processors;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

/**
 * Helper for accessing and working with Annotated Classes.
 *
 * @author Daniel Bluhm
 */
public class AnnotatedElement {
	/**
	 * List of all annotation mirrors on this element.
	 */
	private List<? extends AnnotationMirror> mirrors;

	/**
	 * Elements used to retrieve defaults for annotation values.
	 */
	protected Elements elementUtils;

	/**
	 * The element representing an interface annotated with
	 * {@code @DataElement}.
	 */
	protected Element element;

	/**
	 * Construct an AnnotatedElement from an Element.
	 * @param element The annotated element
	 * @param elementUtils Elements helper from processing environment
	 */
	public AnnotatedElement(Element element, Elements elementUtils) {
		this.element = element;
		this.elementUtils = elementUtils;
	}

	/**
	 * Determine if an annotation of a given type decorates this element.
	 *
	 * @param cls class of annotation to check
	 * @return whether annotation is present or not
	 */
	public boolean hasAnnotation(Class<? extends Annotation> cls) {
		return this.element.getAnnotation(cls) != null;
	}

	/**
	 * Get the AnnotationMirror of a given type if present on the element.
	 *
	 * @param <T> Type of annotation to retrieve
	 * @param cls class of annotation to retrieve
	 * @return AnnotationMirror or null if not found
	 */
	public <T extends Annotation> Optional<T> getAnnotation(Class<T> cls) {
		T value = this.element.getAnnotation(cls);
		if (value == null) {
			return Optional.empty();
		}
		return Optional.of(value);
	}

	/**
	 * Get a map of annotation value names to the value identified by that name.
	 *
	 * This is useful when dealing with a complicated Annotation potentially
	 * containing a value that is a Class object. Otherwise, it is recommended to
	 * directly retrieve the value from an Annotation instance.
	 * @param annotationClass the class of the annotation from which values
	 *        will be retrieved.
	 * @return Map of String to unwrapped AnnotationValue (Object)
	 */
	public Map<String, Object> getAnnotationValueMap(Class<?> annotationClass) {
		return this.getAnnotationMirror(annotationClass)
			.map(mirror -> elementUtils.getElementValuesWithDefaults(mirror))
			.map(map -> map.entrySet().stream()
				.collect(Collectors.toMap(
					entry -> entry.getKey().getSimpleName().toString(),
					entry -> entry.getValue().getValue()
				))
			).orElse(Collections.emptyMap());
	}

	/**
	 * Get a list of annotation values from an annotation mirror of a given type.
	 *
	 * This is useful when dealing with a complicated Annotation potentially
	 * containing a value that is a Class object. Otherwise, it is recommended to
	 * directly retrieve the value from an Annotation instance.
	 *
	 * @param annotationClass the class of the annotation from which values will be
	 *        retrieved.
	 * @return list of AnnotationValue
	 */
	public List<AnnotationValue> getAnnotationValues(Class<?> annotationClass) {
		return this.getAnnotationMirror(annotationClass)
			.map(mirror -> elementUtils.getElementValuesWithDefaults(mirror))
			.map(map -> map.entrySet().stream()
				.map(entry -> (AnnotationValue) entry.getValue())
				.collect(Collectors.toList())
			).orElse(Collections.emptyList());
	}

	/**
	 * Find and return annotation of a given on this element.
	 *
	 * @param annotationClass Class of annotation mirror to retrieve
	 * @return {@link Optional} of annotation mirror
	 */
	private Optional<AnnotationMirror> getAnnotationMirror(Class<?> annotationClass) {
		if (this.mirrors == null) {
			this.mirrors = this.element.getAnnotationMirrors();
		}
		return this.mirrors.stream()
			.filter(m -> m.getAnnotationType()
				.toString().equals(annotationClass.getCanonicalName())
			).findAny()
			.map(m -> Optional.of((AnnotationMirror) m))
			.orElse(Optional.empty());
	}
}
