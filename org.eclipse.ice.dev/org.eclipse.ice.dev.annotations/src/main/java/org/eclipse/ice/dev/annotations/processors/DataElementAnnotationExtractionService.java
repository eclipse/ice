/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Walsh - Initial implementation
 *******************************************************************************/
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
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.Persisted;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

/**
 * Flavor of ICEAnnotationExtractionService that specializes in extracting data from Spec classes with 
 * the class level annotation of DataElement
 *
 */
public class DataElementAnnotationExtractionService extends ICEAnnotationExtractionService{
	
	/**
	 * Annotations to not be transfered from member variables of Spec classes to final generated classes
	 */
	private static final List<String> nonTransferableAnnotations = Set.of(
			DataField.class,
			DataField.Default.class
		).stream()
			.map(cls -> cls.getCanonicalName())
			.collect(Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf));
	
	DataElementAnnotationExtractionService(Elements elementUtils, ObjectMapper mapper,
			ProcessingEnvironment processingEnv, NameGenerator nameGenerator) {
		super(elementUtils, mapper, processingEnv, nameGenerator, nonTransferableAnnotations, DataElementAnnotationExtractionService::isDataField);
	}

	/**
	 * DataElement specific method of class generation. Includes interfaces, implementation, and possibly a persistence handler
	 */
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
		if(specExtractionHelper.hasAnnotation(request.getElement(), Persisted.class)) {
			//persistence
			writers.add(
					generateWriter((String)classMetadata.get(ClassTemplateProperties.PersistenceHandler.QUALIFIED.getKey()),
							getPersistenceInitializer(classMetadata))
					);
		}
		
		return writers;
	}
	
	/**
	 * Lambda that handles the initialization of a DataElementInterfaceWriter
	 * @param classMetadata
	 * @return
	 */
	private Function<JavaFileObject, VelocitySourceWriter>  getInterfaceInitializer(Map classMetadata) {
		return (obj) -> DataElementInterfaceWriter.builder()
				.packageName((String)classMetadata.get(ClassTemplateProperties.Meta.PACKAGE.getKey()))
				.interfaceName((String)classMetadata.get(ClassTemplateProperties.Meta.INTERFACE.getKey()))
				.fields((Fields)classMetadata.get(ClassTemplateProperties.Meta.FIELDS.getKey()))
				.generatedFile(obj)
				.build();
	}
	
	/**
	 * Lambda that handles the initialization of a DataElementImplementationWriter
	 * @param classMetadata
	 * @return
	 */
	private Function<JavaFileObject, VelocitySourceWriter>  getImplementationInitializer(Map classMetadata) {
		return (obj) -> DataElementImplementationWriter.builder()
				.packageName((String)classMetadata.get(ClassTemplateProperties.Meta.PACKAGE.getKey()))
				.interfaceName((String)classMetadata.get(ClassTemplateProperties.Meta.INTERFACE.getKey()))
				.className((String)classMetadata.get(ClassTemplateProperties.Meta.CLASS.getKey()))
				.fields((Fields)classMetadata.get(ClassTemplateProperties.Meta.FIELDS.getKey()))
				.generatedFile(obj)
				.build();
	}
	
	/**
	 * Lambda that handles the initialization of a DataElementPersistenceHandlerWriter
	 * @param classMetadata
	 * @return
	 */
	private Function<JavaFileObject, VelocitySourceWriter>  getPersistenceInitializer(Map classMetadata) {
		return (obj) -> DataElementPersistenceHandlerWriter.builder()
				.packageName((String)classMetadata.get(ClassTemplateProperties.Meta.PACKAGE.getKey()))
				.className((String)classMetadata.get(ClassTemplateProperties.PersistenceHandler.CLASS.getKey()))
				.interfaceName((String)classMetadata.get(ClassTemplateProperties.PersistenceHandler.INTERFACE.getKey()))
				.fields((Fields)classMetadata.get(ClassTemplateProperties.Meta.FIELDS.getKey()))
				.elementInterface((String)classMetadata.get(ClassTemplateProperties.PersistenceHandler.ELEMENT_INTERFACE.getKey()))
				.collection((String)classMetadata.get(ClassTemplateProperties.PersistenceHandler.COLLECTION.getKey()))
				.implementation((String)classMetadata.get(ClassTemplateProperties.PersistenceHandler.IMPLEMENTATION.getKey()))
				.generatedFile(obj)
				.build();
	}
	
	/**
	 * Determine if the passed field is a DataField.
	 * @param element to check
	 * @return whether element is a DataField
	 */
	public static boolean isDataField(Element element) {
		return element.getAnnotation(DataField.class) != null;
	}
	
}
