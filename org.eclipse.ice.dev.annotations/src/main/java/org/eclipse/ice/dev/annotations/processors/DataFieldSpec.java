package org.eclipse.ice.dev.annotations.processors;

import java.util.List;
import java.util.Map;
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
	 * The set of Annotations processed by this AnnotatedElement.
	 */
	private static final Set<Class<?>> ANNOTATION_CLASSES = Set.of(
		DataField.class,
		DataField.Default.class
	);

	/**
	 * Set of Annotation names that extractAnnotations should filter out.
	 */
	private static final Set<String> ANNOTATION_CLASS_NAMES =
		ANNOTATION_CLASSES.stream()
			.map(cls -> cls.getCanonicalName())
			.collect(Collectors.toSet());

	/**
	 * @see org.eclipse.ice.dev.annotations.processor.Field#getter
	 */
	private boolean getter;

	/**
	 * @see org.eclipse.ice.dev.annotations.processor.Field#setter
	 */
	private boolean setter;

	/**
	 * @see org.eclipse.ice.dev.annotations.processor.Field#match
	 */
	private boolean match;

	/**
	 * @see org.eclipse.ice.dev.annotations.processor.Field#unique
	 */
	private boolean unique;

	/**
	 * @see org.eclipse.ice.dev.annotations.processor.Field#search
	 */
	private boolean search;

	/**
	 * @see org.eclipse.ice.dev.annotations.processor.Field#nullable
	 */
	private boolean nullable;

	/**
	 * Instantiate a DataFieldSpec.
	 * @param element
	 * @param elementUtils
	 */
	public DataFieldSpec(Element element, Elements elementUtils) {
		super(ANNOTATION_CLASSES, element, elementUtils);
		Map<String, Object> annotationValues = this.getAnnotationValueMap(DataField.class);
		this.getter = (boolean) annotationValues.get(DataFieldValues.GETTER.getKey());
		this.setter = (boolean) annotationValues.get(DataFieldValues.SETTER.getKey());
		this.match = (boolean) annotationValues.get(DataFieldValues.MATCH.getKey());
		this.unique = (boolean) annotationValues.get(DataFieldValues.UNIQUE.getKey());
		this.search = (boolean) annotationValues.get(DataFieldValues.SEARCH.getKey());
		this.nullable = (boolean) annotationValues.get(DataFieldValues.NULLABLE.getKey());
	}

	/**
	 * Determine if the passed field is a DataField.
	 * @param element
	 * @return
	 */
	public static boolean isDataField(Element element) {
		return element.getAnnotation(DataField.class) != null;
	}

	/**
	 * Return the set of access modifiers on this Field.
	 * @return
	 */
	private Set<Modifier> extractModifiers() {
		return this.element.getModifiers();
	}

	/**
	 * Return the set of annotations on this DataField, excepting the DataField
	 * Annotation itself.
	 * @return
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
	 * @return
	 */
	private TypeMirror extractFieldType() {
		return this.element.asType();
	}

	/**
	 * Return the name of this Field.
	 * @return
	 */
	private String extractFieldName() {
		return this.element.getSimpleName().toString();
	}

	/**
	 * Return the DocString of this Field.
	 * @return
	 */
	private String extractDocString() {
		return this.elementUtils.getDocComment(this.element);
	}

	/**
	 * Extract the defaultValue of this Field.
	 * @return
	 */
	private String extractDefaultValue() {
		String retval = null;
		if (this.hasAnnotation(DataField.Default.class)) {
			Map<String, Object> map = this.getAnnotationValueMap(DataField.Default.class);
			boolean isString = (boolean) map.get(DataFieldValues.Default.IS_STRING.getKey());
			String value = (String) map.get(DataFieldValues.Default.VALUE.getKey());
			if (isString) {
				retval = this.elementUtils.getConstantExpression(value);
			} else {
				retval = value;
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
			.getter(getter)
			.setter(setter)
			.match(match)
			.unique(unique)
			.search(search)
			.nullable(nullable)
			.build();
	}
}
