package org.eclipse.ice.dev.annotations.processors;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataField;

/**
 * An AnnotatedElement subclass representing a DataField.
 * @author Daniel Bluhm
 */
public class DataFieldSpec extends AnnotatedElement {

	/**
	 * Set of Annotation names that extractAnnotations should filter out.
	 */
	private static final Set<String> ANNOTATION_CLASS_NAMES = Set.of(
		DataField.class,
		DataField.Default.class
	).stream()
		.map(cls -> cls.getCanonicalName())
		.collect(Collectors.toSet());

	/**
	 * Used to get DataField Annotation values.
	 */
	private DataField fieldInfo;

	/**
	 * Instantiate a DataFieldSpec.
	 * @param element annotated with {@code @DataField}
	 * @param elementUtils Elements helper class from processing environment
	 */
	public DataFieldSpec(Element element, Elements elementUtils) {
		super(element, elementUtils);
		this.fieldInfo = this.element.getAnnotation(DataField.class);
	}

	/**
	 * Determine if the passed field is a DataField.
	 * @param element to check
	 * @return whether element is a DataField
	 */
	public static boolean isDataField(Element element) {
		return element.getAnnotation(DataField.class) != null;
	}

	/**
	 * Return the set of access modifiers on this Field.
	 * @return extract field modifiers
	 * @see Modifier
	 */
	private Set<Modifier> extractModifiers() {
		return this.element.getModifiers();
	}

	/**
	 * Return the set of annotations on this DataField, excepting the DataField
	 * Annotation itself.
	 * @return extracted annotations, excluding DataField related annotations
	 */
	private List<String> extractAnnotations() {
		return this.element.getAnnotationMirrors().stream()
			.filter(mirror -> !ANNOTATION_CLASS_NAMES.contains(
				mirror.getAnnotationType().toString()
			))
			.map(mirror -> mirror.toString())
			.collect(Collectors.toList());
	}

	/**
	 * Return the class of this Field.
	 * @return extracted field type
	 */
	private TypeMirror extractFieldType() {
		return this.element.asType();
	}

	/**
	 * Return the name of this Field.
	 * @return extracted field name
	 */
	private String extractFieldName() {
		return this.element.getSimpleName().toString();
	}

	/**
	 * Return the DocString of this Field.
	 * @return extracted doc comment
	 */
	private String extractDocString() {
		return this.elementUtils.getDocComment(this.element);
	}

	/**
	 * Extract the defaultValue of this Field. Checks for {@link DataField.Default}
	 * and if not present checks for a constant expression if the field is
	 * {@code final}.
	 * @return extracted default value
	 */
	private String extractDefaultValue() {
		String retval = null;
		DataField.Default defaults = this.element.getAnnotation(DataField.Default.class);
		if (defaults != null) {
			if (defaults.isString()) {
				retval = this.elementUtils.getConstantExpression(defaults.value());
			} else {
				retval = defaults.value();
			}
		} else if (this.element.getModifiers().contains(Modifier.FINAL)) {
			retval = this.elementUtils.getConstantExpression(
				((VariableElement) this.element).getConstantValue()
			);
		}
		return retval;
	}

	/**
	 * Return a this DataFieldSpec as a Field.
	 * @return field
	 */
	public Field toField() {
		return Field.builder()
			.name(extractFieldName())
			.type(extractFieldType())
			.defaultValue(extractDefaultValue())
			.docString(extractDocString())
			.annotations(extractAnnotations())
			.modifiersToString(extractModifiers())
			.getter(fieldInfo.getter())
			.setter(fieldInfo.setter())
			.match(fieldInfo.match())
			.unique(fieldInfo.unique())
			.searchable(fieldInfo.searchable())
			.nullable(fieldInfo.nullable())
			.build();
	}
}
