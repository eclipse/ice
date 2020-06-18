package org.eclipse.ice.dev.annotations.processors;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

/**
 * Helper for accessing and working with Annotated Classes.
 * @author Daniel Bluhm
 */
public abstract class AnnotatedElement {
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
	 * <code>@DataElement</code>.
	 */
	protected Element element;

	/**
	 * A Map of Annotation Class to AnnotationMirrors on this element.
	 */
	private Map<Class<?>, AnnotationMirror> annotations;


	/**
	 * Construct an AnnotatedElement from an Element.
	 * @param annotationClasses The set of classes that this element expects
	 * @param element The annotated element
	 * @param elementUtils Elements helper from processing environment
	 */
	public AnnotatedElement(Set<Class<?>> annotationClasses, Element element, Elements elementUtils) {
		this.element = element;
		this.elementUtils = elementUtils;

		// Construct annotations map
		this.annotations = new HashMap<>();
		for (Class<?> cls : annotationClasses) {
			AnnotationMirror mirror = getAnnotationMirror(cls);
			if (mirror != null) {
				this.annotations.put(cls, mirror);
			}
		}
	}

	/**
	 * Determine if an annotation of a given type decorates this element.
	 * @param cls class of annotation to check
	 * @return whether annotation is present or not
	 */
	public boolean hasAnnotation(Class<?> cls) {
		return this.annotations.containsKey(cls);
	}

	/**
	 * Get the AnnotationMirror of a given type if present on the element.
	 * @param cls class of annotation to retrieve
	 * @return AnnotationMirror or null if not found
	 */
	public AnnotationMirror getAnnotation(Class<?> cls) {
		return this.annotations.get(cls);
	}

	/**
	 * Get a map of annotation value names to the value identified by that name.
	 * @param annotation the class of the annotation from which values will be retrieved.
	 * @return Map of String to unwrapped AnnotationValue (Object)
	 */
	public Map<String, Object> getAnnotationValueMap(Class<?> annotation) {
		if (!annotations.containsKey(annotation)) {
			return Collections.emptyMap();
		}
		final AnnotationMirror mirror = annotations.get(annotation);
		return (Map<String, Object>) elementUtils.getElementValuesWithDefaults(mirror).entrySet().stream()
			.collect(Collectors.toMap(
				entry -> entry.getKey().getSimpleName().toString(),
				entry -> entry.getValue().getValue()
			));
	}

	/**
	 * Get a list of annotation values from an annotation mirror of a given type.
	 * @param annotation the class of the annotation from which values will be retrieved.
	 * @return list of AnnotationValue
	 */
	public List<AnnotationValue> getAnnotationValues(Class<?> annotation) {
		if (!annotations.containsKey(annotation)) {
			return Collections.emptyList();
		}
		final AnnotationMirror mirror = annotations.get(annotation);
		return elementUtils.getElementValuesWithDefaults(mirror).entrySet().stream()
			.map(entry -> entry.getValue())
			.collect(Collectors.toList());
	}

	/**
	 * Extract the value from an annotation. This is useful for extracting the value
	 * from annotations where there is only a single annotation value.
	 * @param <T> The type of the value to which it will be cast
	 * @param annotation the annotation to extract a value from
	 * @param targetType The type of the value to which it will be cast
	 * @return the value
	 */
	@SuppressWarnings("unchecked")
	public <T> T getSingleValue(Class<?> annotation, Class<T> targetType) {
		Object retval = null;
		if (this.hasAnnotation(annotation)) {
			AnnotationValue value = this.getAnnotationValues(annotation)
				.stream()
				.findAny()
				.orElse(null);
			if (value != null) {
				Object v = value.getValue();
				if (targetType.isInstance(v)) {
					retval = v;
				}
			}
		}
		return (T) retval;
	}

	/**
	 * Find and return annotation of type cls on this element or return null.
	 * @param cls
	 * @return
	 */
	private AnnotationMirror getAnnotationMirror(Class<?> cls) {
		if (this.mirrors == null) {
			this.mirrors = this.element.getAnnotationMirrors();
		}
		return this.mirrors.stream()
			.filter(m -> m.getAnnotationType().toString().equals(cls.getCanonicalName()))
			.findAny()
			.orElse(null);
	}
}
