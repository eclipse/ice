package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.DataFieldJson;
import org.eclipse.ice.dev.annotations.DataFields;
import org.eclipse.ice.dev.annotations.Persisted;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;

/**
 * Processor for DataElement Annotations.
 *
 * This will generate an implementation for an interface annotated with
 * DataElement, populating the implementation with metadata and fields specified
 * with the DataField annotation.
 */
@SupportedAnnotationTypes("org.eclipse.ice.dev.annotations.DataElement")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DataElementProcessor extends AbstractProcessor {

	/**
	 * Location of DataElement template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String DATAELEMENT_TEMPLATE = "templates/DataElement.vm";

	/**
	 * Location of PersistenceHandler template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String PERSISTENCE_HANDLER_TEMPLATE = "templates/PersistenceHandler.vm";

	/**
	 * Location of Interface template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String INTERFACE_TEMPLATE = "templates/ElementInterface.vm";

	/**
	 * Return stack trace as string.
	 * @param e subject exception
	 * @return stack trace as string
	 */
	private static String stackTraceToString(final Throwable e) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	/**
	 * Unwrap an optional exception.
	 * @param <T> Exception type
	 * @param e Optional exception to throw if present
	 * @throws T
	 */
	private static <T extends Throwable> void unwrap(final Optional<T> e) throws T {
		if (e.isPresent()) {
			throw e.get();
		}
	}

	protected Messager messager;
	protected Elements elementUtils;
	protected DataFieldsVisitor fieldsVisitor;
	protected ObjectMapper mapper;

	@Override
	public void init(final ProcessingEnvironment env) {
		messager = env.getMessager();
		elementUtils = env.getElementUtils();
		fieldsVisitor = new DataFieldsVisitor(elementUtils);
		mapper = new ObjectMapper();

		// Set up Velocity using the Singleton approach; ClasspathResourceLoader allows
		// us to load templates from src/main/resources
		final Properties p = new Properties();
		for (VelocityProperty vp : VelocityProperty.values()) {
			p.setProperty(vp.key(), vp.value());
		}
		Velocity.init(p);

		super.init(env);
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		// Iterate over all elements with DataElement Annotation
		for (final Element elem : roundEnv.getElementsAnnotatedWith(DataElement.class)) {
			try {
				DataElementRoot dataElement = new DataElementRoot(elementUtils, elem);
				List<Field> fields = new ArrayList<Field>();

				// Collect fields from Defaults, DataField Annotations, and DataFieldJson
				// Annotations.
				fields.addAll(DefaultFields.get());
				fields.addAll(collectFromDataFields(dataElement));
				fields.addAll(collectFromDataFieldJson(dataElement));

				// Write the DataElement Implementation to file.
				writeClass(dataElement, fields);

				// Check if Persistence should be generated.
				if (dataElement.hasAnnotation(Persisted.class)) {
					writePersistence(
						dataElement,
						dataElement.getCollectionName(),
						fields
					);
				}
			} catch (final IOException | UnexpectedValueError | InvalidDataElementRoot e) {
				messager.printMessage(Diagnostic.Kind.ERROR, stackTraceToString(e));
				return false;
			}
		}
		return false;
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
	@SuppressWarnings("unchecked")
	private List<Field> collectFromDataFieldJson(DataElementRoot element) throws IOException {
		List<Field> fields = new ArrayList<>();
		if (!element.hasAnnotation(DataFieldJson.class)) {
			return fields;
		}

		// Iterate through AnnotationValues of AnnotationMirrors for DataFieldJson
		for (
			final AnnotationValue value : element.getAnnotationValues(DataFieldJson.class)
		) {
			// Flatten the AnnotationValue List into List of Strings in Annotation
			List<String> sources = ((List<? extends AnnotationValue>) value.getValue()).stream()
				.map(val -> (String) val.getValue())
				.collect(Collectors.toList());

			// Iterate through each JSON Data Field source and attempt to read
			// fields from JSON file.
			for (String source : sources) {
				Reader reader = processingEnv.getFiler()
					.getResource(StandardLocation.CLASS_OUTPUT, "", source)
					.openReader(false);
				fields.addAll(Arrays.asList(mapper.readValue(reader, Field[].class)));
			}
		}
		return fields;
	}

	/**
	 * Collect Fields from DataField and DataFields Annotations.
	 *
	 * @param element potentially annotated with one or more DataField Annotations.
	 * @return discovered fields
	 * @throws UnexpectedValueError
	 */
	private List<Field> collectFromDataFields(DataElementRoot element) throws UnexpectedValueError {
		List<Field> fields = new ArrayList<>();

		// Iterate over the AnnotationValues of AnnotationMirrors of type DataFields.
		// DataFields present when more than one DataField annotation is used.
		if (element.hasAnnotation(DataFields.class)) {
			for (
				final AnnotationValue value : element.getAnnotationValues(DataFields.class)
			) {
				// Traditional for-loop used to allow raising an exception with unwrap if the
				// field visitor returns an error result
				unwrap(value.accept(fieldsVisitor, fields));
			}
		}

		// Check for DataField and visit; only present when only one DataField
		// Annotation used.
		if (element.hasAnnotation(DataField.class)) {
			unwrap(fieldsVisitor.visitAnnotation(element.getAnnotation(DataField.class), fields));
		}
		return fields;
	}

	/**
	 * Write the implementation of DataElement annotated class to file.
	 * @param interfaceName the annotated interface name, used to determine package and
	 *        name of the generated class
	 * @param fields the fields extracted from DataField annotations on interface
	 * @throws IOException
	 */
	private void writeClass(DataElementRoot element, final List<Field> fields) throws IOException {
		// Prepare context of template
		final VelocityContext context = new VelocityContext();
		context.put(DataElementTemplateProperty.PACKAGE.getKey(), element.getPackageName());
		context.put(DataElementTemplateProperty.INTERFACE.getKey(), element.getName());
		context.put(DataElementTemplateProperty.CLASS.getKey(), element.getImplName());
		context.put(DataElementTemplateProperty.FIELDS.getKey(), fields);

		// Write to file
		final JavaFileObject generatedClassFile = processingEnv.getFiler()
			.createSourceFile(element.getQualifiedImplName());
		try (Writer writer = generatedClassFile.openWriter()) {
			Velocity.mergeTemplate(DATAELEMENT_TEMPLATE, "UTF-8", context, writer);
		}
	}

	/**
	 * Write the persistence handler of DataElement annotated class to file.
	 * @param interfaceName the annotated interface name, used to determine package and
	 *        name of the generated class
	 * @param fields the fields extracted from DataField annotations on interface
	 * @throws IOException
	 */
	private void writePersistence(
		DataElementRoot element,
		final String collectionName,
		List<Field> fields
	) throws IOException {
		// Prepare context of template
		final VelocityContext context = new VelocityContext();
		context.put(
			PersistenceHandlerTemplateProperty.PACKAGE.getKey(),
			element.getPackageName()
		);
		context.put(
			PersistenceHandlerTemplateProperty.ELEMENT_INTERFACE.getKey(),
			element.getName()
		);
		context.put(
			PersistenceHandlerTemplateProperty.CLASS.getKey(),
			element.getPersistenceHandlerName()
		);
		context.put(
			PersistenceHandlerTemplateProperty.COLLECTION.getKey(),
			collectionName
		);
		context.put(
			PersistenceHandlerTemplateProperty.IMPLEMENTATION.getKey(),
			element.getImplName()
		);
		context.put(
			PersistenceHandlerTemplateProperty.FIELDS.getKey(),
			fields
		);

		// Write to file
		final JavaFileObject generatedClassFile = processingEnv.getFiler()
			.createSourceFile(element.getQualifiedPersistenceHandlerName());
		try (Writer writer = generatedClassFile.openWriter()) {
			Velocity.mergeTemplate(PERSISTENCE_HANDLER_TEMPLATE, "UTF-8", context, writer);
		}
	}

	/**
	 * Write the interface of DataElement annotated class to file.
	 * @param interfaceName the annotated interface name, used to determine package and
	 *        name of the generated class
	 * @param fields the fields extracted from DataField annotations on interface
	 * @throws IOException
	 */
	private void writeInterface(
		DataElementRoot element,
		List<Field> fields
	) throws IOException {
		// Prepare context of template
		final VelocityContext context = new VelocityContext();
		context.put(
			InterfaceTemplateProperty.PACKAGE.getKey(),
			element.getPackageName()
		);
		context.put(
			InterfaceTemplateProperty.INTERFACE.getKey(),
			element.getName()
		);
		context.put(
			PersistenceHandlerTemplateProperty.FIELDS.getKey(),
			fields
		);

		// Write to file
		final JavaFileObject generatedClassFile = processingEnv.getFiler()
			.createSourceFile(element.getQualifiedPersistenceHandlerName());
		try (Writer writer = generatedClassFile.openWriter()) {
			Velocity.mergeTemplate(INTERFACE_TEMPLATE, "UTF-8", context, writer);
		}
	}
}
