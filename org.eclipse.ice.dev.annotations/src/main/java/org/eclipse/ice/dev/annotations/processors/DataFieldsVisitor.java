package org.eclipse.ice.dev.annotations.processors;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;

import org.apache.commons.lang3.ClassUtils;
import org.eclipse.ice.dev.annotations.DataField;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Visitor that accumulates DataField information from AnnotationValues. This
 * Visitor is only intended for use on the AnnotationValues of DataFields
 * AnnotationMirrors.
 *
 * Returns an Optional<UnexpectedValueError> to report errors to the caller
 * while conforming to the AnnotationValueVisitor Interface which does not throw
 * any exceptions. Return value should be checked.
 *
 * Additional Parameter Fields acts as the data accumulator, appending visited
 * data to its list of fields.
 *
 * When visiting the value of DataFields Annotations, visitArray will visit the
 * DataField Array then pass the AnnotationMirror of each DataField to
 * visitAnnotation by recursively visiting the values.
 */
class DataFieldsVisitor extends SimpleAnnotationValueVisitor8<Optional<UnexpectedValueError>, List<Field>> {
	protected Elements elementUtils;
	protected ObjectMapper mapper;

	/**
	 * Unwrap an optional exception.
	 * @param <T> Exception type
	 * @param e Optional exception to throw if present
	 * @throws T
	 */
	public static <T extends Throwable> void unwrap(final Optional<T> e) throws T {
		if (e.isPresent()) {
			throw e.get();
		}
	}

	public DataFieldsVisitor(Elements elementUtils) {
		super();
		this.elementUtils = elementUtils;
		mapper = new ObjectMapper();
	}

	/**
	 * Return error as default action for unhandled annotation values.
	 */
	@Override
	protected Optional<UnexpectedValueError> defaultAction(final Object o, final List<Field> f) {
		return Optional.of(
			new UnexpectedValueError(
				"An unexpected annotation value was encountered: " + o.getClass().getCanonicalName()
			)
		);
	}

	/**
	 * Visit AnnotationValues of type Annotation. AnnotationMirror for DataField is
	 * expected.
	 */
	@Override
	public Optional<UnexpectedValueError> visitAnnotation(final AnnotationMirror a, final List<Field> f) {
		if (!a.getAnnotationType().toString().equals(DataField.class.getCanonicalName())) {
			return Optional.of(
				new UnexpectedValueError(
					"Found AnnotationMirror not of type DataField"
				)
			);
		}

		Map<String, Object> valueMap = DataElementRoot.getAnnotationValueMap(elementUtils, a).entrySet().stream()
			.collect(Collectors.toMap(
				entry -> entry.getKey(),
				entry -> {
					if (entry.getValue() instanceof TypeMirror) {
						return ((TypeMirror) entry.getValue()).toString();
					}
					return entry.getValue();
				}
			));
		f.add(mapper.convertValue(valueMap, Field.class));
		return Optional.empty();
	}

	/**
	 * Visit AnnotationValues of type Array, visiting the Array of DataField AnnotationMirrors.
	 */
	@Override
	public Optional<UnexpectedValueError> visitArray(final List<? extends AnnotationValue> vals, final List<Field> f) {
		for (final AnnotationValue val : vals) {
			final Optional<UnexpectedValueError> result = val.accept(this, f);
			if (result.isPresent()) {
				return result;
			}
		}
		return Optional.empty();
	}
}