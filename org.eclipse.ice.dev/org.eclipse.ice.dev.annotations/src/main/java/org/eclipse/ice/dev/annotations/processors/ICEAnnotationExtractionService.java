package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.Persisted;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ICEAnnotationExtractionService {
	
	private Elements elementUtils;
	private ObjectMapper mapper;
	private ProcessingEnvironment processingEnv;
	
	/**
	 * The value appended to DataElement implementation class names.
	 */
	private final static String IMPL_SUFFIX = "Implementation";
	
	/**
	 * The value appended to DataElement Persistence Handler class names.
	 */
	private final static String PERSISTENCE_SUFFIX = "PersistenceHandler";
	
	
	ICEAnnotationExtractionService(Elements elementUtils, ObjectMapper mapper, ProcessingEnvironment processingEnv){
		this.elementUtils = elementUtils;
		this.mapper = mapper;
		this.processingEnv = processingEnv;
	}
	
	public AnnotationExtractionResponse extract(AnnotationExtractionRequest request) throws IOException {
		Fields fields = extractFields(request);
		return AnnotationExtractionResponse.builder()
				.fields(fields)
				.classMetadata(extractClassMetadata(request, fields))
				.build();
	}
	
	private Fields extractFields(AnnotationExtractionRequest request) throws IOException {
		Fields fields = new Fields();
		Element element = request.getElement();

		if(request.isIncludeDefaults()) fields.collect(DefaultFields.get());
		fields.collect(ProcessorUtil.getAllFields(element, elementUtils, request.getFieldFilter(), request.getHandledAnnotations()));			//get all members with given filter
		fields.collect(ProcessorUtil.collectFromDataFieldJson(element, processingEnv, mapper));
		return fields;
	}
	
	private Map extractClassMetadata(AnnotationExtractionRequest request, Fields fields) {
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
		collectionName = extractCollectionName(element);
		
		context.put(ClassTemplateProperties.Meta.PACKAGE.getKey(), packageName);
		context.put(ClassTemplateProperties.Meta.INTERFACE.getKey(), name);
		context.put(ClassTemplateProperties.Meta.CLASS.getKey(), getImplName(name));
		context.put(ClassTemplateProperties.Meta.FIELDS.getKey(), fields);
		context.put(ClassTemplateProperties.Meta.QUALIFIED.getKey(), fullyQualifiedName);
		context.put(ClassTemplateProperties.Meta.QUALIFIEDIMPL.getKey(), getQualifiedImplName(fullyQualifiedName));
		context.put(ClassTemplateProperties.PersistenceHandler.ELEMENT_INTERFACE.getKey(), name);
		context.put(ClassTemplateProperties.PersistenceHandler.COLLECTION.getKey(), collectionName);
		context.put(ClassTemplateProperties.PersistenceHandler.IMPLEMENTATION.getKey(), getImplName(name));	
		context.put(ClassTemplateProperties.PersistenceHandler.QUALIFIED.getKey(), getQualifiedPersistenceHandlerName(fullyQualifiedName));
		context.put(ClassTemplateProperties.PersistenceHandler.CLASS.getKey(), getPersistenceHandlerName(name));
		
		return context;
	}
	
	/**
	 * Return the element name as extracted from the DataElement annotation.
	 * @return the extracted name
	 */
	private String extractName(Element element) {
		return ProcessorUtil.getAnnotation(element, DataElement.class)
			.map(e -> e.name())
			.orElse(null);
	}
	
	/**
	 * Return the collection name as extracted from the Persisted annotation.
	 * @return the extracted collection name
	 */
	private String extractCollectionName(Element element) {
		return ProcessorUtil.getAnnotation(element, Persisted.class)
			.map(p -> p.collection())
			.orElse(null);
	}
	
	/**
	 * Get the name of the Implementation to be generated.
	 * @return implementation name
	 */
	private String getImplName(String name) {
		return name + IMPL_SUFFIX;
	}
	
	/**
	 * Get the fully qualified name of the Implementation to be generated.
	 * @return fully qualified implementation name
	 */
	private String getQualifiedImplName(String fullyQualifiedName) {
		return fullyQualifiedName + IMPL_SUFFIX;
	}
	
	/**
	 * Get the name of the Persistence Handler to be generated.
	 * @return persistence handler name
	 */
	public String getPersistenceHandlerName(String name) {
		return name + PERSISTENCE_SUFFIX;
	}
	
	public String getQualifiedPersistenceHandlerName(String fullyQualifiedName) {
		return fullyQualifiedName + PERSISTENCE_SUFFIX;
	}
}
