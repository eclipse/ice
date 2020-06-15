package org.eclipse.ice.dev.annotations.processors;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataField;

/**
 * An AnnotatedElement subclass representing a DataField.
 * @author Daniel Bluhm
 */
public class DataFieldSpec extends AnnotatedElement {

	private static final Set<Class<?>> ANNOTATION_CLASSES = Set.of(
		DataField.class
	);

	private static final Set<String> ANNOTATION_CLASS_NAMES =
		ANNOTATION_CLASSES.stream()
			.map(cls -> cls.getCanonicalName())
			.collect(Collectors.toSet());

	/**
	 * Instantiate a DataFieldSpec.
	 * @param element
	 * @param elementUtils
	 */
	public DataFieldSpec(Element element, Elements elementUtils) {
		super(ANNOTATION_CLASSES, element, elementUtils);
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
	public Set<Modifier> getModifiers() {
		return this.element.getModifiers();
	}

	/**
	 * Return the set of annotations on this DataField, excepting the DataField
	 * Annotation itself.
	 * @return
	 */
	public List<String> getAnnotations() {
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
	 * @throws ClassNotFoundException
	 */
	public TypeMirror getFieldClass() {
		return this.element.asType();
	}

	/**
	 * Return the name of this Field.
	 * @return
	 */
	public String getFieldName() {
		return this.element.getSimpleName().toString();
	}

	/**
	 * Return the DocString of this Field.
	 * @return
	 */
	public String getDocString() {
		return this.elementUtils.getDocComment(this.element);
	}
}
