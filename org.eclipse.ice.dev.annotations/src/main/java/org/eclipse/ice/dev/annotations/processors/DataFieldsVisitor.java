package org.eclipse.ice.dev.annotations.processors;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;

import org.apache.commons.lang3.ClassUtils;
import org.eclipse.ice.dev.annotations.DataField;

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

	public DataFieldsVisitor(Elements elementUtils) {
		super();
		this.elementUtils = elementUtils;
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

		Field.FieldBuilder builder = Field.builder();
		Map<String, Object> valueMap = DataElementProcessor.getAnnotationValueMapForMirror(elementUtils, a);
		if (valueMap.containsKey("fieldName")) {
			builder.name((String) valueMap.get("fieldName"));
		}
		if (valueMap.containsKey("fieldType")) {
			TypeMirror type = (TypeMirror) valueMap.get("fieldType");
			try {
				builder.type(ClassUtils.getClass(type.toString()));
			} catch (ClassNotFoundException e) {
				builder.type(Field.raw(type.toString()));
			}
		}
		if (valueMap.containsKey("docString")) {
			builder.docString((String) valueMap.get("docString"));
		}
		f.add(builder.build());
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