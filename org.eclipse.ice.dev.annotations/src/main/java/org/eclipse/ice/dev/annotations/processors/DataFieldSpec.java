package org.eclipse.ice.dev.annotations.processors;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataField;

public class DataFieldSpec extends AnnotatedElement {

	private static final Set<Class<?>> ANNOTATION_CLASSES = Set.of(
		DataField.class
	);

	public DataFieldSpec(Element element, Elements elementUtils) {
		super(ANNOTATION_CLASSES, element, elementUtils);
		System.out.println(element.getEnclosedElements());
	}

	public static boolean isDataField(Element element) {
		return element.getAnnotation(DataField.class) != null;
	}

	public Set<Modifier> getModifiers() {
		return this.element.getModifiers();
	}

	public Class<?> getFieldClass() throws ClassNotFoundException {
		return typeMirrorToClass(this.element.asType());
	}

	public String getFieldName() {
		return this.element.getSimpleName().toString();
	}

	public String getDocString() {
		return this.elementUtils.getDocComment(this.element);
	}
}
