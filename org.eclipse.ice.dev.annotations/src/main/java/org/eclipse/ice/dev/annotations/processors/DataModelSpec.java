package org.eclipse.ice.dev.annotations.processors;

import java.util.Arrays;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataFieldJson;
import org.eclipse.ice.dev.annotations.DataModel;
import org.eclipse.ice.dev.annotations.Persisted;

import lombok.Getter;

/**
 * Helper class for extracting data from DataModelSpec classes and their
 * annotations.
 * 
 * Much of this is based on the DataElementSpec class
 *
 * @author Michael Walsh
 */
public class DataModelSpec extends AnnotatedElement {

	/**
	 * The DataFields found in this DataElementSpec.
	 */
	@Getter
	private List<DataElementSpec> dataElements;

	/**
	 * Construct a DataElementSpec from an Element.
	 * @param element the element annotated with {@code @DataModel}
	 * @param elementUtils Elements class from processing environment
	 * @throws InvalidDataElementSpec if given element is in an invalid element
	 *         specification
	 */
	public DataModelSpec(Element element, Elements elementUtils) throws InvalidDataElementSpec {
		super(element, elementUtils);
		// Gather DataFields
		this.dataElements= this.extractDataElements();
	}
	
	@Override
	public boolean isValidAnnotatedElement(Element element, Elements elementUtils){
		return element.getKind().isClass() && (element instanceof TypeElement);	
	}

	/**
	 * Extract data fields from enclosed elements.
	 * @return list of data field specs
	 * @throws InvalidDataElementSpec 
	 */
	public List<DataElementSpec> extractDataElements() throws InvalidDataElementSpec {
		
		//somewhere here need to extract validators as well
		
		AtomicBoolean successfulExtraction = new AtomicBoolean(true);
		List<DataElementSpec> dataElements = this.element.getEnclosedElements().stream()
			.filter(DataElementSpec::isDataElement)
			.map(enclosedElement -> {
				try {
					return new DataElementSpec(enclosedElement, elementUtils);
				} catch(InvalidDataElementSpec e) {
					e.printStackTrace();
					successfulExtraction.set(false);
				}
				return null;
			})
			.collect(Collectors.toList());
		
		if(!successfulExtraction.get()) {
			throw new InvalidDataElementSpec("One or more invalid DataElements were unable to be extracted from DataModel(" + getFullyQualifiedName() + ")");
		}
		
		return dataElements;
	}

	/**
	 * Return the element name as extracted from the DataModel annotation.
	 * @return the extracted name
	 */
	@Override
	public String extractName() {
		return this.getAnnotation(DataModel.class)
			.map(e -> e.name())
			.orElse(null);
	}

	/**
	 * Return the collection name as extracted from the Persisted annotation.
	 * @return the extracted collection name
	 */
	@Override
	public String extractCollectionName() {
		return this.getAnnotation(Persisted.class)
			.map(p -> p.collection())
			.orElse(null);
	}

	/**
	 * Collect Fields from DataField and DataFields Annotations.
	 * @return discovered fields
	 */
	public List<Field> fieldsFromDataFields() {
		return Collections.emptyList();
				/*dataFields.stream()
			.map(dataField -> dataField.toField())
			.collect(Collectors.toList());*/
	}

	/**
	 * Collect JSON File Strings from DataFieldJson Annotations.
	 * @return discovered JSON file strings
	 */
	public List<String> getDataFieldJsonFileNames() {
		return Collections.emptyList();
				/*this.getAnnotation(DataFieldJson.class)
			.map(jsons -> Arrays.asList(jsons.value()))
			.orElse(Collections.emptyList());*/
	}

	@Override
	protected List<String> extractAnnotations() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	protected String extractDefaultValue() {
		// TODO Auto-generated method stub
		return "null";
	}
}
