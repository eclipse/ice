package org.eclipse.ice.dev.annotations.processors;

import java.util.Optional;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;

import org.eclipse.ice.dev.annotations.processors.Fields;

/**
 * Visitor that accumulates DataField information from AnnotationValues. This
 * Visitor is only intended for use on the AnnotationValues of DataField
 * AnnotationMirrors.
 *
 * Returns an Optional<UnexpectedValueError> to report errors to the caller
 * while conforming to the AnnotationValueVisitor Interface which does not throw
 * any exceptions. Return value should be checked.
 *
 * Additional Parameter Fields acts as the data accumulator, appending visited
 * data to its list of fields.
 */
class DataFieldVisitor extends SimpleAnnotationValueVisitor8<Optional<UnexpectedValueError>, Fields> {

	/**
	 * Check if the given type mirror represents a primitive type.
	 * @param t type to check
	 * @return true if primitive, false otherwise
	 */
	private static boolean isPrimitiveType(TypeMirror t) {
		TypeKind kind = t.getKind();
		return
			kind == TypeKind.BOOLEAN ||
			kind == TypeKind.BYTE ||
			kind == TypeKind.CHAR ||
			kind == TypeKind.DOUBLE ||
			kind == TypeKind.FLOAT ||
			kind == TypeKind.INT ||
			kind == TypeKind.LONG ||
			kind == TypeKind.SHORT;
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
	 * Visit AnnotationValues of type String. For DataField annotations, this is
	 * expected to be the value of DataField.fieldName.
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
	 * Visit AnnotationValues of type Type. For DataField annotations, this is
	 * expected to be the value of DataField.fieldType.
	 */
	@Override
	public Optional<UnexpectedValueError> visitType(final TypeMirror t, final Fields f) {
		if (!f.isBuilding()) {
			f.begin();
		}
		f.setType(t.toString());
		f.setPrimitive(isPrimitiveType(t));
		if (f.isComplete()) {
			f.finish();
		}
		return Optional.empty();
	}
}