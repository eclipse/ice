package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.DataFieldJson;
import org.eclipse.ice.dev.annotations.processors.Field;
import org.eclipse.ice.dev.annotations.IDataElement;

import com.fasterxml.jackson.databind.ObjectMapper;

class ProcessorUtil {
	
	/**
	 * Write the implementation of annotated class to file.
	 * @param element
	 * @param fields
	 * @param processingEnv
	 * @param template
	 * @throws IOException
	 */
	public static void writeClass(AnnotationExtractionResponse response, ProcessingEnvironment processingEnv, String template) throws IOException {
		// Prepare context of template
		final VelocityContext context = new VelocityContext();
		Map classMetadata = response.getClassMetadata();
		context.put(ClassTemplateProperties.Meta.PACKAGE.getKey(), classMetadata.get(ClassTemplateProperties.Meta.PACKAGE.getKey()));
		context.put(ClassTemplateProperties.Meta.INTERFACE.getKey(), classMetadata.get(ClassTemplateProperties.Meta.INTERFACE.getKey()));
		context.put(ClassTemplateProperties.Meta.CLASS.getKey(), classMetadata.get(ClassTemplateProperties.Meta.CLASS.getKey()));
		context.put(ClassTemplateProperties.Meta.FIELDS.getKey(), classMetadata.get(ClassTemplateProperties.Meta.FIELDS.getKey()));
		context.put("DataElement", IDataElement.class);
		context.put("VelocityUtils", new VelocityUtils(processingEnv));
		
		// Write to file
		final JavaFileObject generatedClassFile = processingEnv.getFiler()
			.createSourceFile((String)classMetadata.get(ClassTemplateProperties.Meta.QUALIFIEDIMPL.getKey()));
		try (Writer writer = generatedClassFile.openWriter()) {
			Velocity.mergeTemplate(template, "UTF-8", context, writer);
		}
	}
	
	/**
	 * Collect Fields from DataFieldJson Annotations.
	 *
	 * The JSON input files are searched for in the "CLASS_OUTPUT" location,
	 * meaning the same folder to which compiled class files will be output.
	 * JSON files placed in src/main/resources are moved to this location before
	 * the annotation processing phase and are therefore available at this
	 * location at the time of annotation processing.
	 *
	 * @param element potentially annotated with DataFieldJson
	 * @return discovered fields
	 * @throws IOException
	 */
	public static List<Field> collectFromDataFieldJson(Element element, ProcessingEnvironment processingEnv, ObjectMapper mapper) throws IOException {
		List<Field> fields = new ArrayList<>();
		// Iterate through each JSON Data Field source and attempt to read
		// fields from JSON file.
		for (String source : getDataFieldJsonFileNames(element)) {
			Reader reader = processingEnv.getFiler()
				.getResource(StandardLocation.CLASS_OUTPUT, "", source)
				.openReader(false);
			fields.addAll(Arrays.asList(mapper.readValue(reader, Field[].class)));
		}
		return fields;
	}
	
	
	/**
	 * Collect JSON File Strings from DataFieldJson Annotations.
	 * @return discovered JSON file strings
	 */
	public static List<String> getDataFieldJsonFileNames(Element element) {
		return getAnnotation(element, DataFieldJson.class)
			.map(jsons -> Arrays.asList(jsons.value()))
			.orElse(Collections.emptyList());
	}
	
	/**
	 * Get the AnnotationMirror of a given type if present on the element.
	 * @param <T> Type of annotation to retrieve
	 * @param cls class of annotation to retrieve
	 * @return AnnotationMirror or null if not found
	 */
	public static <T extends Annotation> Optional<T> getAnnotation(Element element, Class<T> cls) {
		T value = element.getAnnotation(cls);
		if (value == null) {
			return Optional.empty();
		}
		return Optional.of(value);
	}
	
	/**
	 * Write the interface of DataElement annotated class to file.
	 * @param element
	 * @param fields
	 * @throws IOException
	 */
	public static void writeInterface(
			AnnotationExtractionResponse response,
			ProcessingEnvironment processingEnv,
			String template
		) throws IOException {
			// Prepare context of template
			final VelocityContext context = new VelocityContext();
			Map classMetadata = response.getClassMetadata();
			context.put(ClassTemplateProperties.Meta.PACKAGE.getKey(), classMetadata.get(ClassTemplateProperties.Meta.PACKAGE.getKey()));
			context.put(ClassTemplateProperties.Meta.INTERFACE.getKey(), classMetadata.get(ClassTemplateProperties.Meta.INTERFACE.getKey()));
			context.put(ClassTemplateProperties.Meta.FIELDS.getKey(), classMetadata.get(ClassTemplateProperties.Meta.FIELDS.getKey()));

			// Write to file
			final JavaFileObject generatedClassFile = processingEnv.getFiler()
				.createSourceFile((String)classMetadata.get(ClassTemplateProperties.Meta.QUALIFIED.getKey()));
			try (Writer writer = generatedClassFile.openWriter()) {
				Velocity.mergeTemplate(template, "UTF-8", context, writer);
			}
		}
	
	/**
	 * Write the persistence handler of DataElement annotated class to file.
	 * @param element
	 * @param collectionName
	 * @param fields
	 * @throws IOException
	 */
	public static void writePersistence(
		AnnotationExtractionResponse response,
		ProcessingEnvironment processingEnv,
		String template
	) throws IOException {
		// Prepare context of template
		final VelocityContext context = new VelocityContext();
		Map classMetadata = response.getClassMetadata();
		context.put(ClassTemplateProperties.Meta.PACKAGE.getKey(), classMetadata.get(ClassTemplateProperties.Meta.PACKAGE.getKey()));
		context.put(ClassTemplateProperties.Meta.CLASS.getKey(), classMetadata.get(ClassTemplateProperties.PersistenceHandler.CLASS.getKey()));
		context.put(ClassTemplateProperties.Meta.FIELDS.getKey(), classMetadata.get(ClassTemplateProperties.Meta.FIELDS.getKey()));
		context.put(ClassTemplateProperties.PersistenceHandler.ELEMENT_INTERFACE.getKey(), classMetadata.get(ClassTemplateProperties.PersistenceHandler.ELEMENT_INTERFACE.getKey()));
		context.put(ClassTemplateProperties.PersistenceHandler.COLLECTION.getKey(), classMetadata.get(ClassTemplateProperties.PersistenceHandler.COLLECTION.getKey()));
		context.put(ClassTemplateProperties.PersistenceHandler.IMPLEMENTATION.getKey(), classMetadata.get(ClassTemplateProperties.PersistenceHandler.IMPLEMENTATION.getKey()));

		// Write to file
		final JavaFileObject generatedClassFile = processingEnv.getFiler()
			.createSourceFile((String)classMetadata.get(ClassTemplateProperties.PersistenceHandler.QUALIFIED.getKey()));
		try (Writer writer = generatedClassFile.openWriter()) {
			Velocity.mergeTemplate(template, "UTF-8", context, writer);
		}
	}
	
	/**
	 * Return stack trace as string.
	 * @param e subject exception
	 * @return stack trace as string
	 */
	public static String stackTraceToString(final Throwable e) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	public static String extractValidator(Element element) {
		return null;
	}
	
	//issue is here these methods should be using the element sent to them
		public static Field convertElementToField(Element element, Elements elementUtils, Field fieldInfo, List<String> handledAnnotations) {
				return Field.builder()
					.name(extractFieldName(element))
					.type(extractFieldType(element))
					.defaultValue(extractDefaultValue(element, elementUtils))
					.docString(extractDocString(element, elementUtils))
					.annotations(extractAnnotations(element, handledAnnotations))
					.modifiersToString(extractModifiers(element))
					.validator(extractValidator(element))
					.getter(fieldInfo.isGetter())
					.setter(fieldInfo.isSetter())
					.match(fieldInfo.isMatch())
					.unique(fieldInfo.isUnique())
					.search(fieldInfo.isSearch())
					.nullable(fieldInfo.isNullable())
					.build();
		}
		
		public static boolean hasAnnotation(Element element, Class<? extends Annotation> annotation) {
			return element.getAnnotation(annotation) != null;
		}
		
		/**
		 * Return the set of access modifiers on this Field.
		 * @return extract field modifiers
		 * @see Modifier
		 */
		public static  Set<Modifier> extractModifiers(Element element) {
			return element.getModifiers();
		}

		/**
		 * Return the set of annotations on this DataField, excepting the DataField
		 * Annotation itself.
		 * @return extracted annotations, excluding DataField related annotations
		 */
		public static  List<String> extractAnnotations(Element element, List<String> handledAnnotations){
			return element.getAnnotationMirrors().stream()
					.filter(mirror -> !handledAnnotations.contains(
						mirror.getAnnotationType().toString()
					))
					.map(mirror -> mirror.toString())
					.collect(Collectors.toList());
		};

		/**
		 * Return the class of this Field.
		 * @return extracted field type
		 */
		public static TypeMirror extractFieldType(Element element) {
			return element.asType();
		}

		/**
		 * Return the name of this Field.
		 * @return extracted field name
		 */
		public static  String extractFieldName(Element element) {
			return element.getSimpleName().toString();
		}

		/**
		 * Return the DocString of this Field.
		 * @return extracted doc comment
		 */
		public static  String extractDocString(Element element, Elements elementUtils) {
			return elementUtils.getDocComment(element);
		}
		
		public static  String extractDefaultValue(Element element, Elements elementUtils) {
			String retval = null;
			DataField.Default defaults = element.getAnnotation(DataField.Default.class);
			if (defaults != null) {
				if (defaults.isString()) {
					retval = elementUtils.getConstantExpression(defaults.value());
				} else {
					retval = defaults.value();
				}
			} else if (element.getModifiers().contains(Modifier.FINAL)) {
				retval = elementUtils.getConstantExpression(
					((VariableElement) element).getConstantValue()
				);
			}
			return retval;
		};
		
		public static List<Field> getAllFields(Element element, Elements elementUtils, Predicate<Element> fieldFilter, List<String> handledAnnotations){
			List<Field> elementList = null;
			if(element instanceof TypeElement) {
				
				elementList = ElementFilter.fieldsIn(((TypeElement) element).getEnclosedElements())
				.stream()
				.filter(fieldFilter)
				.map(el -> {
					
					DataField dataField = el.getAnnotation(DataField.class);
					Field fieldInfo;
					if(dataField == null) fieldInfo = Field.builder().build();
					else fieldInfo = Field.builder()
							.getter(dataField.getter())
							.setter(dataField.setter())
							.match(dataField.match())
							.unique(dataField.unique())
							.search(dataField.search())
							.nullable(dataField.nullable())
							.build();
					
					
					return (Field)convertElementToField(el,
						elementUtils,
						fieldInfo,
						handledAnnotations);
							})
	            .collect(Collectors.toList());
			}
			return elementList;
		}

}
