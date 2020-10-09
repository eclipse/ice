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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;

/**
 * Base service for the extraction of class data from Spec classes
 * @author Michael Walsh
 *
 */
public class ICEAnnotationExtractionService {

	/**
	 * Util provided by the annotation processing environment
	 */
	private Elements elementUtils;

	/**
	 * Object serialization and deserialization
	 */
	@Setter
	private ObjectMapper mapper;

	/**
	 * Annotation environment interface for interaction with the annotation
	 * processing flow
	 */
	private ProcessingEnvironment processingEnv;

	/**
	 * Class that determines the naming schema of generated classes
	 */
	@Setter
	private NameGenerator nameGenerator;

	/**
	 * List of annotations to not transfer from spec fields to their generated
	 * counterparts
	 */
	@Setter
	private List<String> nonTransferableAnnotations = new ArrayList<>();

	/**
	 * Boolean lambda that determines whether or not a specific field is eligible
	 * for generation
	 */
	@Setter
	private Predicate<Element> fieldFilter = p->true;

	/**
	 * Util instance for extracting specific data from spec element classes
	 */
	protected SpecExtractionHelper specExtractionHelper = new SpecExtractionHelper();

	/**
	 * Util instance for extracting specific data from json spec schemas
	 */
	protected JsonExtractionHelper jsonExtractionHelper = new JsonExtractionHelper();

	/**
	 * Constructor
	 */
	public ICEAnnotationExtractionService(Elements elementUtils,
			ProcessingEnvironment processingEnv) {
		this.elementUtils = elementUtils;
		this.mapper = new ObjectMapper();
		this.processingEnv = processingEnv;
		this.nameGenerator = new DefaultNameGenerator();
	}
	
	/**
	 * Constructor
	 */
	public ICEAnnotationExtractionService(Elements elementUtils, ObjectMapper mapper,
			ProcessingEnvironment processingEnv, NameGenerator nameGenerator) {
		this.elementUtils = elementUtils;
		this.mapper = mapper;
		this.processingEnv = processingEnv;
		this.nameGenerator = nameGenerator;
	}

	/**
	 * Main entry point into the metadata extraction flow
	 * 
	 * @param request
	 * @return Extracted metadata
	 * @throws IOException
	 */
	public AnnotationExtractionResponse extract(AnnotationExtractionRequest request) throws IOException {
		Fields fields = extractFields(request);
		Map<TemplateProperty, Object> metaData = extractClassMetadata(request, fields);
		return AnnotationExtractionResponse.builder().fields(fields).classMetadata(metaData).build();
	}

	/**
	 * Collect fields from Spec class, static default field collection, and json if
	 * applicable
	 * 
	 * @param request
	 * @return Fields of the Spec class
	 * @throws IOException
	 */
	public Fields extractFields(AnnotationExtractionRequest request) throws IOException {
		Fields fields = new Fields();
		Element element = request.getElement();

		if (request.isIncludeDefaults())
			fields.collect(DefaultFields.get());
		fields.collect(
				specExtractionHelper.getAllFields(element, elementUtils, fieldFilter, nonTransferableAnnotations)); // get
																													// all
																													// members
																													// with
																													// given
																													// filter
		fields.collect(jsonExtractionHelper.collectFromDataFieldJson(element, processingEnv, mapper));
		return fields;
	}

	/**
	 * Parse, generate, and store class metadata in a map.
	 * 
	 * @param request
	 * @param fields
	 * @return metadata map
	 */
	public Map<TemplateProperty, Object> extractClassMetadata(AnnotationExtractionRequest request, Fields fields) {
		SpecClassMetadata specData = extractSpecData(request, fields);
		Map<TemplateProperty, Object> context = generateClassMetadata(specData);
		return context;
	}

	/**
	 * Given seed data extracted from a spec class, this method generates the
	 * necessary metadata for class generation
	 * 
	 * @param specData
	 * @return enum keyed map of extracted and processed class metadata
	 */
	protected Map<TemplateProperty, Object> generateClassMetadata(SpecClassMetadata specData) {
		Map<TemplateProperty, Object> context = new HashMap<>();

		generateMetaTemplateData(specData, context);
		generatePersistenceHandlerTemplateData(specData, context);

		return context;
	}

	/**
	 * Package meta data for class interface and implementation into the supplied 
	 * map context
	 * 
	 * @param specData metadata extracted from the client Spec class
	 * @param context map to store the processed metadata harvested from
	 * the client Spec class
	 */
	protected void generateMetaTemplateData(SpecClassMetadata specData, Map<TemplateProperty, Object> context) {
		context.put(MetaTemplateProperty.PACKAGE, specData.getPackageName());
		context.put(MetaTemplateProperty.INTERFACE, specData.getName());
		context.put(MetaTemplateProperty.CLASS, nameGenerator.getImplName(specData.getName()));
		context.put(MetaTemplateProperty.FIELDS, specData.getFields());
		context.put(MetaTemplateProperty.QUALIFIED, specData.getFullyQualifiedName());
		context.put(MetaTemplateProperty.QUALIFIEDIMPL,
				nameGenerator.getQualifiedImplName(specData.getFullyQualifiedName()));
	}

	/**
	 * Package metadata for class persistence handler into the supplied map context
	 * 
	 * @param specData metadata extracted from the client Spec class
	 * @param context map to store the processed metadata harvested from
	 * the client Spec class
	 */
	protected void generatePersistenceHandlerTemplateData(SpecClassMetadata specData,
			Map<TemplateProperty, Object> context) {
		context.put(PersistenceHandlerTemplateProperty.ELEMENT_INTERFACE, specData.getName());
		context.put(PersistenceHandlerTemplateProperty.COLLECTION, specData.getCollectionName());
		context.put(PersistenceHandlerTemplateProperty.IMPLEMENTATION, nameGenerator.getImplName(specData.getName()));
		context.put(PersistenceHandlerTemplateProperty.QUALIFIED,
				nameGenerator.getQualifiedPersistenceHandlerName(specData.getFullyQualifiedName()));
		context.put(PersistenceHandlerTemplateProperty.CLASS,
				nameGenerator.getPersistenceHandlerName(specData.getName()));
		context.put(PersistenceHandlerTemplateProperty.INTERFACE, nameGenerator.getPersistenceHandlerInterfaceName());
	}

	/**
	 * Extract and package seed data given an element(via request) and a list of
	 * extracted fields
	 * 
	 * @param request
	 * @param fields
	 * @return extracted, unprocessed data from the Spec class
	 */
	protected SpecClassMetadata extractSpecData(AnnotationExtractionRequest request, Fields fields) {
		Element element = request.getElement();
		String packageName = null;
		String fullyQualifiedName;
		String name = request.getClassName();
		String elementFQN = (element instanceof TypeElement) ? ((TypeElement) element).getQualifiedName().toString()
				: element.getClass().getName();
		String collectionName;

		final int lastDot = elementFQN.lastIndexOf('.');
		if (lastDot > 0) {
			packageName = elementFQN.substring(0, lastDot);
			fullyQualifiedName = packageName + "." + name;
		} else {
			fullyQualifiedName = name;
		}
		collectionName = nameGenerator.extractCollectionName(element);

		return SpecClassMetadata.builder().name(name).packageName(packageName).fullyQualifiedName(fullyQualifiedName)
				.collectionName(collectionName).fields(fields).build();
	}

}
