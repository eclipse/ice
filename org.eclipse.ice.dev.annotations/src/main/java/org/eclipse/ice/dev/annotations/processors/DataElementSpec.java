package org.eclipse.ice.dev.annotations.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataFieldJson;
import org.eclipse.ice.dev.annotations.Persisted;

import lombok.Getter;

import javax.lang.model.element.AnnotationValue;

/**
 * Helper class for extracting data from DataElementSpec classes and their
 * annotations.
 *
 * @author Daniel Bluhm
 */
public class DataElementSpec extends AnnotatedElement {

	/**
	 * The Set of possible annotation classes we expect to see
	 * on DataElements.
	 */
	private final static Set<Class<?>> ANNOTATION_CLASSES = Set.of(
		DataElement.class,
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
	 * The fully qualified name of this element.
	 */
	@Getter private String fullyQualifiedName;

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
	 * The DataFields found in this DataElementSpec.
	 */
	@Getter private List<DataFieldSpec> dataFields;

	/**
	 * Construct a DataElementSpec from an Element.
	 * @param elementUtils
	 * @param element
	 * @throws InvalidDataElementRoot
	 */
	public DataElementSpec(Element element, Elements elementUtils) throws InvalidDataElementRoot {
		super(ANNOTATION_CLASSES, element, elementUtils);
		if (!element.getKind().isClass()) {
			throw new InvalidDataElementRoot(
				"DataElementSpec must be class, found "
					+ element.toString()
			);
		}

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

		// Gather DataFields
		this.dataFields = this.element.getEnclosedElements().stream()
			.filter(DataFieldSpec::isDataField)
			.map(enclosedElement -> new DataFieldSpec(enclosedElement, elementUtils))
			.collect(Collectors.toList());
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
	 */
	public List<Field> fieldsFromDataFields() {
		List<Field> fields = new ArrayList<>();
		for (DataFieldSpec field : dataFields) {
			fields.add(
				Field.builder()
					.name(field.getFieldName())
					.type(field.getFieldClass())
					.docString(field.getDocString())
					.build()
			);
		}
		return fields;
	}

	/**
	 * Collect JSON File Strings from DataFieldJson Annotations.
	 *
	 * @return discovered JSON file strings
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
