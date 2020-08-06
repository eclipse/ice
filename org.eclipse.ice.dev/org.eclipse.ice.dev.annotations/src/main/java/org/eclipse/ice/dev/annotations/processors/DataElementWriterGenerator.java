package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import org.eclipse.ice.dev.annotations.Persisted;

public class DataElementWriterGenerator extends AbstractWriterGenerator implements WriterGenerator {

	protected SpecExtractionHelper specExtractionHelper = new SpecExtractionHelper();
	
	DataElementWriterGenerator(ProcessingEnvironment processingEnv) {
		super(processingEnv);
	}

	/**
	 * DataElement specific method of class generation. Includes interfaces, implementation, and possibly a persistence handler
	 */
	@Override
	public List<VelocitySourceWriter> generateWriters(Element element, AnnotationExtractionResponse response) throws IOException {
		List<VelocitySourceWriter> writers = new ArrayList<>();
		Map<TemplateProperty, Object> classMetadata = response.getClassMetadata();
		
		//interface
		writers.add(
					getInterfaceWriter(classMetadata, (String)classMetadata.get(MetaTemplateProperty.QUALIFIED))
				);
		//implementation
		writers.add(
					getImplementationWriter(classMetadata, (String)classMetadata.get(MetaTemplateProperty.QUALIFIEDIMPL))
				);
		if(specExtractionHelper.hasAnnotation(element, Persisted.class)) {
			//persistence
			writers.add(
						getPersistenceWriter(classMetadata, (String)classMetadata.get(PersistenceHandlerTemplateProperty.QUALIFIED))
					);
		}
		
		return writers;
	}
	
	/**
	 * Initialization of a DataElementInterfaceWriter
	 * @param classMetadata
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	private VelocitySourceWriter getInterfaceWriter(Map classMetadata, String name) throws IOException {
		return DataElementInterfaceWriter.builder()
				.packageName((String)classMetadata.get(MetaTemplateProperty.PACKAGE))
				.interfaceName((String)classMetadata.get(MetaTemplateProperty.INTERFACE))
				.fields((Fields)classMetadata.get(MetaTemplateProperty.FIELDS))
				.generatedFile(createFileObjectForName(name))
				.build();
	}
	
	/**
	 * Initialization of a DataElementImplementationWriter
	 * @param classMetadata
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	private VelocitySourceWriter  getImplementationWriter(Map classMetadata, String name) throws IOException {
		return DataElementImplementationWriter.builder()
				.packageName((String)classMetadata.get(MetaTemplateProperty.PACKAGE))
				.interfaceName((String)classMetadata.get(MetaTemplateProperty.INTERFACE))
				.className((String)classMetadata.get(MetaTemplateProperty.CLASS))
				.fields((Fields)classMetadata.get(MetaTemplateProperty.FIELDS))
				.generatedFile(createFileObjectForName(name))
				.build();
	}
	
	/**
	 * Initialization of a DataElementPersistenceHandlerWriter
	 * @param classMetadata
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	private VelocitySourceWriter  getPersistenceWriter(Map classMetadata, String name) throws IOException {
		return DataElementPersistenceHandlerWriter.builder()
				.packageName((String)classMetadata.get(MetaTemplateProperty.PACKAGE))
				.className((String)classMetadata.get(PersistenceHandlerTemplateProperty.CLASS))
				.interfaceName((String)classMetadata.get(PersistenceHandlerTemplateProperty.INTERFACE))
				.fields((Fields)classMetadata.get(MetaTemplateProperty.FIELDS))
				.elementInterface((String)classMetadata.get(PersistenceHandlerTemplateProperty.ELEMENT_INTERFACE))
				.collection((String)classMetadata.get(PersistenceHandlerTemplateProperty.COLLECTION))
				.implementation((String)classMetadata.get(PersistenceHandlerTemplateProperty.IMPLEMENTATION))
				.generatedFile(createFileObjectForName(name))
				.build();
	}

}
