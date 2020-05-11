package org.eclipse.ice.dev.annotations.processors;

import java.util.Optional;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;

import org.eclipse.ice.dev.annotations.processors.Fields;

/**
 * Visitor that accumulates DataField information from AnnotationValues. This
 * Visitor is only intended for use on the AnnotationValues of DataField
 * AnnotationMirrors.
 */
class DataFieldVisitor extends SimpleAnnotationValueVisitor8<Optional<UnexpectedValueError>, Fields> {

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
	 * Visit DataField.fieldName.
	 */
	@Override
	public Optional<UnexpectedValueError> visitString(final String s, final Fields f) {
		if (!f.isBuilding()) {
			f.begin();
		}
		f.setName(s);
		if (f.isComplete()) {
			f.finish();
		}
		return Optional.empty();
	}

	/**
	 * Visit DataField.fieldType.
	 */
	@Override
	public Optional<UnexpectedValueError> visitType(final TypeMirror t, final Fields f) {
		if (!f.isBuilding()) {
			f.begin();
		}
		f.setClassName(t.toString());
		if (f.isComplete()) {
			f.finish();
		}
		return Optional.empty();
	}
}