package org.eclipse.ice.dev.annotations.processors;

import java.lang.annotation.Annotation;
import java.util.Optional;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for AnnotationExtractors providing common methods to
 * all extractors.
 * 
 * @author Daniel Bluhm
 *
 * @param <T> Type of data extracted from element.
 */
public abstract class AbstractAnnotationExtractor<T> implements AnnotationExtractor<T> {

	/**
	 * Logger.
	 */
	protected final Logger logger;

	/**
	 * Elements used to retrieve defaults for annotation values.
	 */
	protected Elements elementUtils;

	public AbstractAnnotationExtractor(Elements elementUtils) {
		this.logger = LoggerFactory.getLogger(getClass());
		this.elementUtils = elementUtils;
	}

	@Override
	public Optional<T> extractIfApplies(Element element) {
		Optional<T> value = Optional.empty();
		try {
			value = Optional.of(extract(element));
		} catch (InvalidElementException e) {
			logger.debug(
				"Failed to extract metadata from annotation, returning empty:",
				e
			);
		}
		return value;
	}

	/**
	 * Determine if an annotation of a given type decorates this element.
	 *
	 * @param cls class of annotation to check
	 * @return whether annotation is present or not
	 */
	public boolean hasAnnotation(Element element, Class<? extends Annotation> cls) {
		return element.getAnnotation(cls) != null;
	}

	/**
	 * Get the AnnotationMirror of a given type if present on the element.
	 *
	 * @param <T> Type of annotation to retrieve
	 * @param cls class of annotation to retrieve
	 * @return AnnotationMirror or null if not found
	 */
	public <R extends Annotation> Optional<R> getAnnotation(
		Element element, Class<R> cls
	) {
		R value = element.getAnnotation(cls);
		if (value == null) {
			return Optional.empty();
		}
		return Optional.of(value);
	}

	/**
	 * Find and return annotation mirror of a given type if present.
	 * 
	 * When an annotation value is a Class, Annotation Mirrors must be used to
	 * retrieve a TypeMirror to the class (using {@link
	 * Elements#getElementValuesWithDefaults(AnnotationMirror)}).
	 * 
	 * If the annotation in question has no Class values, it is recommended to
	 * use the Annotation directly with {@link #getAnnotation(Element, Class)}.
	 *
	 * @param annotationClass Class of annotation mirror to retrieve
	 * @return {@link Optional} of annotation mirror
	 */
	public Optional<AnnotationMirror> getAnnotationMirror(
		Element element, Class<?> annotationClass
	) {
		return element.getAnnotationMirrors().stream()
			.filter(m -> m.getAnnotationType()
				.toString().equals(annotationClass.getCanonicalName())
			).findAny()
			.map(m -> Optional.of((AnnotationMirror) m))
			.orElse(Optional.empty());
	}
}