package org.eclipse.ice.dev.annotations.processors;

import java.util.List;
import java.util.Optional;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;

import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.processors.Fields;

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
class DataFieldsVisitor extends SimpleAnnotationValueVisitor8<Optional<UnexpectedValueError>, Fields> {
	protected Elements elementUtils;
	protected DataFieldVisitor fieldVisitor;

	public DataFieldsVisitor(Elements elementUtils, DataFieldVisitor fieldVisitor) {
		super();
		this.elementUtils = elementUtils;
		this.fieldVisitor = fieldVisitor;
	}

	/**
	 * Return error as default action for unhandled annotation values.
	 */
	@Override
	protected Optional<UnexpectedValueError> defaultAction(final Object o, final Fields f) {
		return Optional.of(
			new UnexpectedValueError(
				"An unexpected annotation value was encountered: " + o.getClass().getCanonicalName()
			)
		);
	}

	/**
	 * Visit AnnotationValues of type Annotation (as an AnnotationMirror), expected
	 * to visit DataField AnnotationMirrors.
	 */
	@Override
	public Optional<UnexpectedValueError> visitAnnotation(final AnnotationMirror a, final Fields f) {
		if (!a.getAnnotationType().toString().equals(DataField.class.getCanonicalName())) {
			return Optional.of(
				new UnexpectedValueError(
					"Found AnnotationMirror not of type DataField"
				)
			);
		}

		for (final AnnotationValue value : DataElementProcessor.getAnnotationValuesForMirror(elementUtils, a)) {
			final Optional<UnexpectedValueError> result = value.accept(fieldVisitor, f);
			if (result.isPresent()) {
				return result;
			}
		}
		return Optional.empty();
	}

	/**
	 * Visit AnnotationValues of type Array, visiting the Array of DataField AnnotationMirrors.
	 */
	@Override
	public Optional<UnexpectedValueError> visitArray(final List<? extends AnnotationValue> vals, final Fields f) {
		for (final AnnotationValue val : vals) {
			final Optional<UnexpectedValueError> result = val.accept(this, f);
			if (result.isPresent()) {
				return result;
			}
		}
		return Optional.empty();
	}
}