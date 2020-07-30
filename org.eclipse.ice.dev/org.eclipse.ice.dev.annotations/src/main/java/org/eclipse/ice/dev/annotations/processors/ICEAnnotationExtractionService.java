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

public abstract class ICEAnnotationExtractionService {
	
	private Elements elementUtils;
	private ObjectMapper mapper;
	private ProcessingEnvironment processingEnv;
	private NameGenerator nameGenerator;
	private List<String> nonTransferableAnnotations;
	private Predicate<Element> fieldFilter;
	
	
	ICEAnnotationExtractionService(Elements elementUtils, ObjectMapper mapper, ProcessingEnvironment processingEnv, NameGenerator nameGenerator, List<String> nonTransferableAnnotations, Predicate<Element> fieldFilter){
		this.elementUtils = elementUtils;
		this.mapper = mapper;
		this.processingEnv = processingEnv;
		this.nameGenerator = nameGenerator;
		this.nonTransferableAnnotations = nonTransferableAnnotations;
		this.fieldFilter = fieldFilter;
	}
	
	public abstract List<VelocitySourceWriter> generateWriters(AnnotationExtractionRequest request) throws IOException;
	
	public VelocitySourceWriter generateWriter(String name, Function<Writer, VelocitySourceWriter> writerInitializer) throws IOException {
		final JavaFileObject generatedClassFile = processingEnv.getFiler()
				.createSourceFile(name);
			try (Writer writer = generatedClassFile.openWriter()) {
				return writerInitializer.apply(writer);
			}
	}
	
	public AnnotationExtractionResponse extract(AnnotationExtractionRequest request) throws IOException {
		Fields fields = extractFields(request);
		return AnnotationExtractionResponse.builder()
				.fields(fields)
				.classMetadata(extractClassMetadata(request, fields))
				.build();
	}
	
	public Fields extractFields(AnnotationExtractionRequest request) throws IOException {
		Fields fields = new Fields();
		Element element = request.getElement();

		if(request.isIncludeDefaults()) fields.collect(DefaultFields.get());
		fields.collect(ProcessorUtil.getAllFields(element, elementUtils, fieldFilter, nonTransferableAnnotations));			//get all members with given filter
		fields.collect(ProcessorUtil.collectFromDataFieldJson(element, processingEnv, mapper));
		return fields;
	}
	
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
		
		return context;
	}
	
}
