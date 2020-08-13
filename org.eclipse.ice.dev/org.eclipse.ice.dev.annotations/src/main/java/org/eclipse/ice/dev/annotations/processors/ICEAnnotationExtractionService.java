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
import lombok.Data;

/**
 * Base service for the extraction of class data from Spec classes
 *
 */
public class ICEAnnotationExtractionService {
	
	private Elements elementUtils;
	private ObjectMapper mapper;
	private ProcessingEnvironment processingEnv;
	private NameGenerator nameGenerator;
	private List<String> nonTransferableAnnotations;
	private Predicate<Element> fieldFilter;
	protected SpecExtractionHelper specExtractionHelper = new SpecExtractionHelper();
	protected JsonExtractionHelper jsonExtractionHelper = new JsonExtractionHelper();
	
	
	public ICEAnnotationExtractionService(Elements elementUtils, ObjectMapper mapper, ProcessingEnvironment processingEnv, NameGenerator nameGenerator){
		this.elementUtils = elementUtils;
		this.mapper = mapper;
		this.processingEnv = processingEnv;
		this.nameGenerator = nameGenerator;
	}
	
	public void setNonTransferableAnnotations (List<String> nonTransferableAnnotations) {
		this.nonTransferableAnnotations = nonTransferableAnnotations;
	}
	
	public void setFieldFilter(Predicate<Element> fieldFilter) {
		this.fieldFilter = fieldFilter;
	}
	
	/**
	 * Main entry point into the metadata extraction flow
	 * @param request
	 * @return Extracted metadata
	 * @throws IOException
	 */
	public AnnotationExtractionResponse extract(AnnotationExtractionRequest request) throws IOException {
		Fields fields = extractFields(request);
		Map<TemplateProperty, Object> metaData = extractClassMetadata(request, fields);
		return AnnotationExtractionResponse.builder()
				.fields(fields)
				.classMetadata(metaData)
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
	public Map<TemplateProperty, Object> extractClassMetadata(AnnotationExtractionRequest request, Fields fields) {
		ClassSeedData seedData = extractSeedData(request, fields);
		Map<TemplateProperty, Object> context = generateClassMetadata(seedData);	
		return context;
	}
	
	protected Map<TemplateProperty, Object> generateClassMetadata(ClassSeedData seedData) {
		Map<TemplateProperty, Object> context = new HashMap<TemplateProperty, Object>();
		
		generateMetaTemplateData(seedData, context);
		generatePersistenceHandlerTemplateData(seedData, context);
	
		return context;
	}
	
	protected void generateMetaTemplateData(ClassSeedData seedData, Map<TemplateProperty, Object> context) {
		context.put(MetaTemplateProperty.PACKAGE, seedData.getPackageName());
		context.put(MetaTemplateProperty.INTERFACE, seedData.getName());
		context.put(MetaTemplateProperty.CLASS, nameGenerator.getImplName(seedData.getName()));
		context.put(MetaTemplateProperty.FIELDS, seedData.getFields());
		context.put(MetaTemplateProperty.QUALIFIED, seedData.getFullyQualifiedName());
		context.put(MetaTemplateProperty.QUALIFIEDIMPL, nameGenerator.getQualifiedImplName(seedData.getFullyQualifiedName()));
	}
	
	protected void generatePersistenceHandlerTemplateData(ClassSeedData seedData, Map<TemplateProperty, Object> context) {
		context.put(PersistenceHandlerTemplateProperty.ELEMENT_INTERFACE, seedData.getName());
		context.put(PersistenceHandlerTemplateProperty.COLLECTION, seedData.getCollectionName());
		context.put(PersistenceHandlerTemplateProperty.IMPLEMENTATION, nameGenerator.getImplName(seedData.getName()));	
		context.put(PersistenceHandlerTemplateProperty.QUALIFIED, nameGenerator.getQualifiedPersistenceHandlerName(seedData.getFullyQualifiedName()));
		context.put(PersistenceHandlerTemplateProperty.CLASS, nameGenerator.getPersistenceHandlerName(seedData.getName()));
		context.put(PersistenceHandlerTemplateProperty.INTERFACE, nameGenerator.getPersistenceHandlerInterfaceName());
	}
	
	protected ClassSeedData extractSeedData(AnnotationExtractionRequest request, Fields fields) {
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
		
		return ClassSeedData.builder()
				.name(name)
				.packageName(packageName)
				.fullyQualifiedName(fullyQualifiedName)
				.collectionName(collectionName)
				.fields(fields)
				.build();
	}
	
}
