package org.eclipse.ice.dev.annotations.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.DataFieldJson;
import org.eclipse.ice.dev.annotations.DataFields;
import org.eclipse.ice.dev.annotations.Persisted;

import lombok.Getter;

import javax.lang.model.element.AnnotationValue;

/**
 * Helpers for working with Elements that form the "root" of a DataElement.
 *
 * @author Daniel Bluhm
 */
public class DataElementRoot extends AnnotatedClass {

	/**
	 * The Set of possible annotation classes we expect to see
	 * on DataElements.
	 */
	private final static Set<Class<?>> ANNOTATION_CLASSES = Set.of(
		DataElement.class,
		DataField.class,
		DataFields.class,
		DataFieldJson.class,
		Persisted.class
	);

	/**
	 * The value appended to DataElement implementation class names.
	 */
	private final static String IMPL_SUFFIX = "Implementation";

	/**
	 * The value appended to DataElement Persistence Handler class names.
	 */
	private final static String PERSISTENCE_SUFFIX = "PersistenceHandler";

	/**
	 * FieldsVisitor for extracting DataField info from annotations.
	 */
	private DataFieldsVisitor fieldsVisitor;

	/**
	 * The fully qualified name of this element.
	 */
	private String fullyQualifiedName;

	/**
	 * The name of the DataElement as extracted from the DataElement annotation.
	 */
	@Getter private String name;

	/**
	 * The package of this element represented as a String.
	 */
	@Getter private String packageName;

	/**
	 * The name of the collection as extracted from Persisted or null.
	 */
	@Getter private String collectionName;

	/**
	 * Construct a DataElementRoot from an Element.
	 * @param elementUtils
	 * @param element
	 * @throws InvalidDataElementRoot
	 */
	public DataElementRoot(Element element, Elements elementUtils) throws InvalidDataElementRoot {
		super(ANNOTATION_CLASSES, element, elementUtils);
		if (!element.getKind().isInterface()) {
			throw new InvalidDataElementRoot(
				"DataElementRoots must be interface, found "
					+ element.toString()
			);
		}

		this.fieldsVisitor = new DataFieldsVisitor(elementUtils);

		// Names
		this.name = this.extractName();
		String elementFQN = ((TypeElement) element).getQualifiedName().toString();
		this.packageName = null;
		final int lastDot = elementFQN.lastIndexOf('.');
		if (lastDot > 0) {
			this.packageName = elementFQN.substring(0, lastDot);
			this.fullyQualifiedName = this.packageName + "." + this.name;
		} else {
			this.fullyQualifiedName = this.name;
		}
		this.collectionName = this.extractCollectionName();
	}

	/**
	 * Return the element name as extracted from the DataElement annotation.
	 * @param element
	 * @return
	 */
	public String extractName() {
		AnnotationValue value = this.getAnnotationValues(DataElement.class)
			.stream()
			.findAny()
			.orElse(null);
		 if (value == null) {
			return null;
		 }
		 return (String) value.getValue();
	}


	/**
	 * Return the collection name as extracted from the Persisted annotation.
	 * @param element
	 * @return
	 */
	public String extractCollectionName() {
		if (!this.hasAnnotation(Persisted.class)) {
			return null;
		}
		AnnotationValue value = this.getAnnotationValues(Persisted.class)
			.stream()
			.findAny()
			.orElse(null);
		 if (value == null) {
			return null;
		 }
		 return (String) value.getValue();
	}

	/**
	 * Get the name of the Implementation to be generated.
	 * @return
	 */
	public String getImplName() {
		return this.name + IMPL_SUFFIX;
	}

	/**
	 * Get the fully qualified name of the Implementation to be generated.
	 * @return
	 */
	public String getQualifiedImplName() {
		return this.fullyQualifiedName + IMPL_SUFFIX;
	}

	/**
	 * Get the name of the Persistence Handler to be generated.
	 * @return
	 */
	public String getPersistenceHandlerName() {
		return this.name + PERSISTENCE_SUFFIX;
	}

	/**
	 * Get the fully qualified name of the Persistence Handler to be generated.
	 * @return
	 */
	public String getQualifiedPersistenceHandlerName() {
		return this.fullyQualifiedName + PERSISTENCE_SUFFIX;
	}

	/**
	 * Collect Fields from DataField and DataFields Annotations.
	 *
	 * @return discovered fields
	 * @throws UnexpectedValueError
	 */
	public List<Field> fieldsFromDataFields() throws UnexpectedValueError {
		List<Field> fields = new ArrayList<>();

		// Iterate over the AnnotationValues of AnnotationMirrors of type DataFields.
		// DataFields present when more than one DataField annotation is used.
		if (hasAnnotation(DataFields.class)) {
			for (
				final AnnotationValue value : getAnnotationValues(DataFields.class)
			) {
				// Traditional for-loop used to allow raising an exception with unwrap if the
				// field visitor returns an error result
				DataFieldsVisitor.unwrap(value.accept(fieldsVisitor, fields));
			}
		}

		// Check for DataField and visit; only present when only one DataField
		// Annotation used.
		if (hasAnnotation(DataField.class)) {
			DataFieldsVisitor.unwrap(fieldsVisitor.visitAnnotation(
				getAnnotation(DataField.class), fields
			));
		}
		return fields;
	}

	/**
	 * Collect JSON File Strings from DataFieldJson Annotations.
	 *
	 * @return discovered JSON file strings
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public List<String> getDataFieldJsonFileNames() {
		List<String> fieldJsonStrings = new ArrayList<>();
		if (!hasAnnotation(DataFieldJson.class)) {
			return fieldJsonStrings;
		}
		AnnotationValue value = this.getAnnotationValues(DataFieldJson.class)
			.stream()
			.findAny()
			.orElse(null);
		if (value == null) {
			return fieldJsonStrings;
		}

		// Flatten the AnnotationValue List into List of Strings in Annotation
		fieldJsonStrings.addAll(
			((List<? extends AnnotationValue>) value.getValue()).stream()
				.map(val -> (String) val.getValue())
				.collect(Collectors.toList())
		);

		return fieldJsonStrings;
	}
}
