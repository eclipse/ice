package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
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
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.DataFields;

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

	protected DataFieldVisitor fieldVisitor;

	@Override
	public void init(final ProcessingEnvironment env) {
		messager = env.getMessager();
		elementUtils = env.getElementUtils();
		fieldVisitor = new DataFieldVisitor();
		fieldsVisitor = new DataFieldsVisitor(elementUtils, fieldVisitor);

		// Set up Velocity using the Singleton approach; ClasspathResourceLoader allows
		// us to load templates from src/main/resources
		final Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty(
			"class.resource.loader.class",
			"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader"
		);
		Velocity.init(p);

		super.init(env);
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		for (final Element elem : roundEnv.getElementsAnnotatedWith(DataElement.class)) {
			if (!elem.getKind().isInterface()) {
				messager.printMessage(
					Diagnostic.Kind.ERROR,
					"DataElement annotation can only be applied to interfaces, found " + elem.toString()
				);
				return false;
			}

			final Fields fields = new Fields();

			final List<? extends AnnotationMirror> mirrors = elem.getAnnotationMirrors();
			try {
				// Iterate over the AnnotationValues of AnnotationMirrors of type DataFields.
				// DataFields present when more than one DataField annotation is used.
				for (
					final AnnotationValue value : mirrors.stream()
						.filter(
							mirror -> mirror.getAnnotationType().toString().equals(
								DataFields.class.getCanonicalName()
							)
						)
						.map(mirror -> getAnnotationValuesForMirror(elementUtils, mirror))
						.flatMap(List::stream) // Flatten List<List<AnnotationValue> to List<AnnotationValue>
						.collect(Collectors.toList())
				) {
					// Traditional for-loop used to allow raising an exception with unwrap if the
					// field visitor returns an error result
					unwrap(value.accept(fieldsVisitor, fields));
				}
				// Iterate over the AnnotationValues of AnnotationMirrors of type DataField.
				// Only present when only one DataField annotation is used.
				for (
					final AnnotationValue value : mirrors.stream()
						.filter(
							mirror -> mirror.getAnnotationType().toString().equals(
								DataField.class.getCanonicalName()
							)
						)
						.map(mirror -> getAnnotationValuesForMirror(elementUtils, mirror))
						.flatMap(List::stream)
						.collect(Collectors.toList())
				) {
					unwrap(value.accept(fieldVisitor, fields));
				}
				this.writeClass(((TypeElement) elem).getQualifiedName().toString(), fields);
			} catch (final IOException | UnexpectedValueError e) {
				messager.printMessage(Diagnostic.Kind.ERROR, stackTraceToString(e));
				return false;
			}
		}
		return false;
	}

	/**
	 * Write the implementation of DataElement annotated class to file.
	 * @param interfaceName the annotated interface name, used to determine package and
	 *        name of the generated class
	 * @param fields the fields extracted from DataField annotations on interface
	 * @throws IOException
	 */
	private void writeClass(final String interfaceName, final Fields fields) throws IOException {
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
		context.put("package", packageName);
		context.put("interface", simpleName);
		context.put("class", generatedSimpleClassName);
		context.put("fields", fields.getFields()); // Hand over the list directly so template can #foreach on fields

		// Write to file
		final JavaFileObject generatedClassFile = processingEnv.getFiler().createSourceFile(generatedClassName);
		try (Writer writer = generatedClassFile.openWriter()) {
			Velocity.mergeTemplate(template, "UTF-8", context, writer);
		}
	}
}
