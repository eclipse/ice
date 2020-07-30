package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.Persisted;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

public class DataElementAnnotationExtractionService extends ICEAnnotationExtractionService{
	
	private static final List<String> nonTransferableAnnotations = Set.of(
			DataField.class,
			DataField.Default.class
		).stream()
			.map(cls -> cls.getCanonicalName())
			.collect(Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf));
	
	DataElementAnnotationExtractionService(Elements elementUtils, ObjectMapper mapper,
			ProcessingEnvironment processingEnv, NameGenerator nameGenerator) {
		super(elementUtils, mapper, processingEnv, nameGenerator, nonTransferableAnnotations, DataFieldSpec::isDataField);
	}

	@Override
	public List<VelocitySourceWriter> generateWriters(AnnotationExtractionRequest request) throws IOException {
		AnnotationExtractionResponse response = extract(request);
		List<VelocitySourceWriter> writers = new ArrayList<>();
		Map classMetadata = response.getClassMetadata();
		
		//interface
		writers.add(
				generateWriter((String)classMetadata.get(ClassTemplateProperties.Meta.QUALIFIED.getKey()),
						getInterfaceInitializer(classMetadata))
				);
		//implementation
		writers.add(
				generateWriter((String)classMetadata.get(ClassTemplateProperties.Meta.QUALIFIEDIMPL.getKey()),
						getImplementationInitializer(classMetadata))
				);
		
		if(ProcessorUtil.hasAnnotation(request.getElement(), Persisted.class)) {
			//persistence
			writers.add(
					generateWriter((String)classMetadata.get(ClassTemplateProperties.PersistenceHandler.QUALIFIED.getKey()),
							getPersistenceInitializer(classMetadata))
					);
		}
		
		return writers;
	}
	
	private Function<Writer, VelocitySourceWriter>  getInterfaceInitializer(Map classMetadata) {
		return (writer) -> DataElementInterfaceWriter.builder()
				.packageName((String)classMetadata.get(ClassTemplateProperties.Meta.PACKAGE.getKey()))
				.interfaceName((String)classMetadata.get(ClassTemplateProperties.Meta.INTERFACE.getKey()))
				.fields((Fields)classMetadata.get(ClassTemplateProperties.Meta.FIELDS.getKey()))
				.writer(writer)
				.build();
	}
	
	private Function<Writer, VelocitySourceWriter>  getImplementationInitializer(Map classMetadata) {
		return (writer) -> DataElementImplementationWriter.builder()
				.packageName((String)classMetadata.get(ClassTemplateProperties.Meta.PACKAGE.getKey()))
				.interfaceName((String)classMetadata.get(ClassTemplateProperties.Meta.INTERFACE.getKey()))
				.className((String)classMetadata.get(ClassTemplateProperties.Meta.CLASS.getKey()))
				.fields((Fields)classMetadata.get(ClassTemplateProperties.Meta.FIELDS.getKey()))
				.writer(writer)
				.build();
	}
	
	private Function<Writer, VelocitySourceWriter>  getPersistenceInitializer(Map classMetadata) {
		return (writer) -> DataElementPersistenceHandlerWriter.builder()
				.packageName((String)classMetadata.get(ClassTemplateProperties.Meta.PACKAGE.getKey()))
				.className((String)classMetadata.get(ClassTemplateProperties.PersistenceHandler.CLASS.getKey()))
				.fields((Fields)classMetadata.get(ClassTemplateProperties.Meta.FIELDS.getKey()))
				.elementInterface((String)classMetadata.get(ClassTemplateProperties.PersistenceHandler.ELEMENT_INTERFACE.getKey()))
				.collection((String)classMetadata.get(ClassTemplateProperties.PersistenceHandler.COLLECTION.getKey()))
				.implementation((String)classMetadata.get(ClassTemplateProperties.PersistenceHandler.IMPLEMENTATION.getKey()))
				.writer(writer)
				.build();
	}
	
}
