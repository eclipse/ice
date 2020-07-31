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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.Persisted;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;

/**
 * Base service for the extraction of class data from Spec classes
 *
 */
public abstract class ICEAnnotationExtractionService {
	
	private Elements elementUtils;
	private ObjectMapper mapper;
	private ProcessingEnvironment processingEnv;
	private NameGenerator nameGenerator;
	private List<String> nonTransferableAnnotations;
	private Predicate<Element> fieldFilter;
	protected SpecExtractionHelper specExtractionHelper = new SpecExtractionHelper();
	protected JsonExtractionHelper jsonExtractionHelper = new JsonExtractionHelper();
	
	
	ICEAnnotationExtractionService(Elements elementUtils, ObjectMapper mapper, ProcessingEnvironment processingEnv, NameGenerator nameGenerator, List<String> nonTransferableAnnotations, Predicate<Element> fieldFilter){
		this.elementUtils = elementUtils;
		this.mapper = mapper;
		this.processingEnv = processingEnv;
		this.nameGenerator = nameGenerator;
		this.nonTransferableAnnotations = nonTransferableAnnotations;
		this.fieldFilter = fieldFilter;
	}
	
	/**
	 * Abstract method to be implemented in a specialized manner per annotation to be extracted and class to be generated
	 * @param request
	 * @return generated writers used to spawn classes through Velocity
	 * @throws IOException
	 */
	public abstract List<VelocitySourceWriter> generateWriters(AnnotationExtractionRequest request) throws IOException;
	
	/**
	 * Initializes and returns a VelocitySourceWriter
	 * @param name
	 * @param writerInitializer
	 * @return VelocitySourceWriter
	 * @throws IOException
	 */
	public VelocitySourceWriter generateWriter(String name, Function<JavaFileObject, VelocitySourceWriter> writerInitializer) throws IOException {
		final JavaFileObject generatedClassFile = processingEnv.getFiler()
				.createSourceFile(name);
		return writerInitializer.apply(generatedClassFile);
	}
	
	/**
	 * Main entry point into the metadata extraction flow
	 * @param request
	 * @return Extracted metadata
	 * @throws IOException
	 */
	public AnnotationExtractionResponse extract(AnnotationExtractionRequest request) throws IOException {
		Fields fields = extractFields(request);
		return AnnotationExtractionResponse.builder()
				.fields(fields)
				.classMetadata(extractClassMetadata(request, fields))
				.build();
	}
	
	/**
	 * Collect fields from Spec class, static default field collection, and json if applicable
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public Fields extractFields(AnnotationExtractionRequest request) throws IOException {
		Fields fields = new Fields();
		Element element = request.getElement();

		if(request.isIncludeDefaults()) fields.collect(DefaultFields.get());
		fields.collect(specExtractionHelper.getAllFields(element, elementUtils, fieldFilter, nonTransferableAnnotations));			//get all members with given filter
		fields.collect(jsonExtractionHelper.collectFromDataFieldJson(element, processingEnv, mapper));
		return fields;
	}
	
	/**
	 * Parse, generate, and store class metadata in a map.
	 * @param request
	 * @param fields
	 * @return metadata map
	 */
	public Map extractClassMetadata(AnnotationExtractionRequest request, Fields fields) {
		Map context = new HashMap<String, Object>();
		Element element = request.getElement();
		String packageName = null;
		String fullyQualifiedName;
		String name = request.getClassName();
		String elementFQN = (element instanceof TypeElement)
		        ? ((TypeElement)element).getQualifiedName().toString() : element.getClass().getName();
		String collectionName;
		        
		final int lastDot = elementFQN.lastIndexOf('.');
		if (lastDot > 0) {
			packageName = elementFQN.substring(0, lastDot);
			fullyQualifiedName = packageName + "." + name;
		} else {
			fullyQualifiedName = name;
		}
		collectionName = nameGenerator.extractCollectionName(element);
		
		context.put(ClassTemplateProperties.Meta.PACKAGE.getKey(), packageName);
		context.put(ClassTemplateProperties.Meta.INTERFACE.getKey(), name);
		context.put(ClassTemplateProperties.Meta.CLASS.getKey(), nameGenerator.getImplName(name));
		context.put(ClassTemplateProperties.Meta.FIELDS.getKey(), fields);
		context.put(ClassTemplateProperties.Meta.QUALIFIED.getKey(), fullyQualifiedName);
		context.put(ClassTemplateProperties.Meta.QUALIFIEDIMPL.getKey(), nameGenerator.getQualifiedImplName(fullyQualifiedName));
		context.put(ClassTemplateProperties.PersistenceHandler.ELEMENT_INTERFACE.getKey(), name);
		context.put(ClassTemplateProperties.PersistenceHandler.COLLECTION.getKey(), collectionName);
		context.put(ClassTemplateProperties.PersistenceHandler.IMPLEMENTATION.getKey(), nameGenerator.getImplName(name));	
		context.put(ClassTemplateProperties.PersistenceHandler.QUALIFIED.getKey(), nameGenerator.getQualifiedPersistenceHandlerName(fullyQualifiedName));
		context.put(ClassTemplateProperties.PersistenceHandler.CLASS.getKey(), nameGenerator.getPersistenceHandlerName(name));
		context.put(ClassTemplateProperties.PersistenceHandler.INTERFACE.getKey(), nameGenerator.getPersistenceHandlerInterfaceName());
		
		return context;
	}
	
}
