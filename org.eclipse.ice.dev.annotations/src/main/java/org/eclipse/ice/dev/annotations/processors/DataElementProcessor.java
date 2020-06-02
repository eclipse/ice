package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import javax.lang.model.element.AnnotationMirror;
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
	private static final String template = "templates/DataElement.vm";

	/**
	 * Get a list of annotation values from an annotation mirror.
	 * @param mirror the mirror from which to grab values.
	 * @return list of AnnotationValue
	 */
	public static List<AnnotationValue> getAnnotationValuesForMirror(
		final Elements elementUtils, final AnnotationMirror mirror
	) {
		return elementUtils.getElementValuesWithDefaults(mirror).entrySet().stream()
			.map(entry -> entry.getValue())
			.collect(Collectors.toList());
	}

	/**
	 * Get a list of annotation values from an annotation mirror.
	 * @param mirror the mirror from which to grab values.
	 * @return list of AnnotationValue
	 */
	public static Map<String, Object> getAnnotationValueMapForMirror(
		final Elements elementUtils, final AnnotationMirror mirror
	) {
		return (Map<String, Object>) elementUtils.getElementValuesWithDefaults(mirror).entrySet().stream()
			.collect(Collectors.toMap(
				entry -> entry.getKey().getSimpleName().toString(),
				entry -> entry.getValue().getValue()
			));
	}

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
			if (!elem.getKind().isInterface()) {
				String errorMsg = "DataElement annotation can only be applied to interfaces, found " + elem.toString();
				messager.printMessage(Diagnostic.Kind.ERROR, errorMsg);
				return false;
			}

			List<Field> fields = new ArrayList<Field>();
			fields.addAll(DefaultFields.get());

			try {
				fields.addAll(collectFromDataFields(elem));
				fields.addAll(collectFromDataFieldJson(elem));
				this.writeClass(((TypeElement) elem).getQualifiedName().toString(), fields);
			} catch (final IOException | UnexpectedValueError e) {
				messager.printMessage(Diagnostic.Kind.ERROR, stackTraceToString(e));
				return false;
			}
		}
		return false;
	}

	private AnnotationMirror getAnnotationMirror(Element element, Class<?> cls) {
		Optional<? extends AnnotationMirror> mirror = element.getAnnotationMirrors().stream()
			.filter(m -> m.getAnnotationType().toString().equals(cls.getCanonicalName()))
			.findFirst();
		if (mirror.isPresent()) {
			return mirror.get();
		}
		return null;
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
	private List<Field> collectFromDataFieldJson(Element element) throws IOException {
		List<Field> fields = new ArrayList<>();
		final AnnotationMirror mirror = getAnnotationMirror(element, DataFieldJson.class);
		if (mirror == null) {
			return fields;
		}

		// Iterate through AnnotationValues of AnnotationMirrors for DataFieldJson
		for (
			final AnnotationValue value :
				getAnnotationValuesForMirror(elementUtils, mirror)
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
	private List<Field> collectFromDataFields(Element element) throws UnexpectedValueError {
		List<Field> fields = new ArrayList<>();

		// Iterate over the AnnotationValues of AnnotationMirrors of type DataFields.
		// DataFields present when more than one DataField annotation is used.
		final AnnotationMirror dataFieldsMirror = getAnnotationMirror(element, DataFields.class);
		if (dataFieldsMirror != null) {
			for (
				final AnnotationValue value :
					getAnnotationValuesForMirror(elementUtils, dataFieldsMirror)
			) {
				// Traditional for-loop used to allow raising an exception with unwrap if the
				// field visitor returns an error result
				unwrap(value.accept(fieldsVisitor, fields));
			}
		}

		// Check for DataField and visit; only present when only one DataField
		// Annotation used.
		final AnnotationMirror dataFieldMirror = getAnnotationMirror(element, DataField.class);
		if (dataFieldMirror != null) {
			unwrap(fieldsVisitor.visitAnnotation(dataFieldMirror, fields));
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
	private void writeClass(final String interfaceName, final List<Field> fields) throws IOException {
		// Determine package, class name from annotated interface name
		String packageName = null;
		final int lastDot = interfaceName.lastIndexOf('.');
		if (lastDot > 0) {
			packageName = interfaceName.substring(0, lastDot);
		}
		final String simpleName = interfaceName.substring(lastDot + 1);
		final String generatedClassName = interfaceName + "Implementation";
		final String generatedSimpleClassName = generatedClassName.substring(lastDot + 1);

		// Prepare context of template
		final VelocityContext context = new VelocityContext();
		context.put(ContextProperty.PACKAGE.key(), packageName);
		context.put(ContextProperty.INTERFACE.key(), simpleName);
		context.put(ContextProperty.CLASS.key(), generatedSimpleClassName);
		context.put(ContextProperty.FIELDS.key(), fields);

		// Write to file
		final JavaFileObject generatedClassFile = processingEnv.getFiler().createSourceFile(generatedClassName);
		try (Writer writer = generatedClassFile.openWriter()) {
			Velocity.mergeTemplate(template, "UTF-8", context, writer);
		}
	}
}
