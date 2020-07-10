package org.eclipse.ice.dev.annotations.processors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.DataFieldJson;
import org.eclipse.ice.dev.annotations.Persisted;

import lombok.Getter;

/**
 * Helper class for extracting data from DataElementSpec classes and their
 * annotations.
 *
 * @author Daniel Bluhm
 */
public class DataElementSpec extends AnnotatedElement {

	/**
	 * The DataFields found in this DataElementSpec.
	 */
	@Getter
	private List<DataFieldSpec> dataFields;

	/**
	 * Construct a DataElementSpec from an Element.
	 * @param element the element annotated with {@code @DataElement}
	 * @param elementUtils Elements class from processing environment
	 * @throws InvalidDataElementSpec if given element is in an invalid element
	 *         specification
	 */
	public DataElementSpec(Element element, Elements elementUtils) throws InvalidDataElementSpec {
		super(element, elementUtils);
		// Gather DataFields
		this.dataFields = this.extractDataFields();
	}
	
	@Override
	public boolean isValidAnnotatedElement(Element element, Elements elementUtils){
		return element.getKind().isClass() && (element instanceof TypeElement);	
	}
	
	/**
	 * Determine if the passed field is a DataElement.
	 * @param element to check
	 * @return whether element is a DataElement
	 */
	public static boolean isDataElement(Element element) {
		return element.getAnnotation(DataElement.class) != null;
	}

	/**
	 * Extract data fields from enclosed elements.
	 * @return list of data field specs
	 */
	public List<DataFieldSpec> extractDataFields() {
		return this.element.getEnclosedElements().stream()
			.filter(DataFieldSpec::isDataField)
			.map(enclosedElement -> {
				try {
					return new DataFieldSpec(enclosedElement, elementUtils);
				} catch(InvalidDataElementSpec e) {
					//should never rach this point as the validation for DataField always returns true as of now
					e.printStackTrace();
				}
				return null;
			})
			.collect(Collectors.toList());
	}


	/**
	 * Collect JSON File Strings from DataFieldJson Annotations.
	 * @return discovered JSON file strings
	 */
	public List<String> getDataFieldJsonFileNames() {
		return this.getAnnotation(DataFieldJson.class)
			.map(jsons -> Arrays.asList(jsons.value()))
			.orElse(Collections.emptyList());
	}

	@Override
	protected List<String> extractAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String extractDefaultValue() {
		// TODO Auto-generated method stub
		return null;
	}

	private static final List<String> ANNOTATION_CLASS_NAMES = Set.of(
			DataField.class,
			DataField.Default.class
		).stream()
			.map(cls -> cls.getCanonicalName())
			.collect(Collectors.toList());
	
	public Collection<Field> fieldsFromDataFields() {
		return ProcessorUtil.getAllFields(element, elementUtils, DataFieldSpec::isDataField, ANNOTATION_CLASS_NAMES);
	}
}
