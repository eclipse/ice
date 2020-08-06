package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import org.eclipse.ice.dev.annotations.DataElement;
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
@SupportedAnnotationTypes({
	"org.eclipse.ice.dev.annotations.DataElement",
	"org.eclipse.ice.dev.annotations.DataField",
	"org.eclipse.ice.dev.annotations.DataField.Default",
	"org.eclipse.ice.dev.annotations.Persisted"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class DataElementProcessor extends AbstractProcessor {
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


	protected Messager messager;
	protected Elements elementUtils;
	protected ObjectMapper mapper;

	@Override
	public void init(final ProcessingEnvironment env) {
		messager = env.getMessager();
		elementUtils = env.getElementUtils();
		mapper = new ObjectMapper();
		super.init(env);
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		// Iterate over all elements with DataElement Annotation
		for (final Element elem : roundEnv.getElementsAnnotatedWith(DataElement.class)) {
			try {
				DataElementSpec dataElement = new DataElementSpec(elem, elementUtils);
				Fields fields = new Fields();

				// Collect fields from Defaults, DataField Annotations, and DataFieldJson
				// Annotations.
				fields.collect(DefaultFields.get());
				fields.collect(dataElement.fieldsFromDataFields());
				fields.collect(collectFromDataFieldJson(dataElement));

				// Write the DataElement's interface to file.
				writeInterface(dataElement, fields);

				// Write the DataElement Implementation to file.
				writeImpl(dataElement, fields);

				// Check if Persistence should be generated.
				if (dataElement.hasAnnotation(Persisted.class)) {
					writePersistence(
						dataElement,
						dataElement.getCollectionName(),
						fields
					);
				}
			} catch (final IOException | InvalidDataElementSpec e) {
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
	private List<Field> collectFromDataFieldJson(DataElementSpec element) throws IOException {
		List<Field> fields = new ArrayList<>();
		// Iterate through each JSON Data Field source and attempt to read
		// fields from JSON file.
		for (String source : element.getDataFieldJsonFileNames()) {
			Reader reader = processingEnv.getFiler()
				.getResource(StandardLocation.CLASS_OUTPUT, "", source)
				.openReader(false);
			fields.addAll(Arrays.asList(mapper.readValue(reader, Field[].class)));
		}
		return fields;
	}

	/**
	 * Write the implementation of DataElement annotated class to file.
	 * @param element
	 * @param fields
	 * @throws IOException
	 */
	private void writeImpl(DataElementSpec element, final Fields fields) throws IOException {
		final JavaFileObject generatedClassFile = processingEnv.getFiler()
			.createSourceFile(element.getQualifiedImplName());
		try (Writer writer = generatedClassFile.openWriter()) {
			ImplementationWriter.builder()
				.packageName(element.getPackageName())
				.className(element.getImplName())
				.interfaceName(element.getName())
				.fields(fields)
				.types(fields.getTypes())
				.build()
				.write(writer);
		}
	}

	/**
	 * Write the persistence handler of DataElement annotated class to file.
	 * @param element
	 * @param collectionName
	 * @param fields
	 * @throws IOException
	 */
	private void writePersistence(
		DataElementSpec element,
		final String collectionName,
		Fields fields
	) throws IOException {
		// Write to file
		final JavaFileObject generatedClassFile = processingEnv.getFiler()
			.createSourceFile(element.getQualifiedPersistenceHandlerName());
		try (Writer writer = generatedClassFile.openWriter()) {
			PersistenceHandlerWriter.builder()
				.packageName(element.getPackageName())
				.elementInterface(element.getName())
				.className(element.getPersistenceHandlerName())
				.interfaceName(element.getPersistenceHandlerInterfaceName())
				.implementation(element.getImplName())
				.collection(collectionName)
				.fields(fields)
				.types(fields.getTypes())
				.build()
				.write(writer);
		}
	}

	/**
	 * Write the interface of DataElement annotated class to file.
	 * @param element
	 * @param fields
	 * @throws IOException
	 */
	private void writeInterface(
		DataElementSpec element,
		Fields fields
	) throws IOException {
		final JavaFileObject generatedClassFile = processingEnv.getFiler()
			.createSourceFile(element.getFullyQualifiedName());
		try (Writer writer = generatedClassFile.openWriter()) {
			InterfaceWriter.builder()
				.packageName(element.getPackageName())
				.interfaceName(element.getName())
				.fields(fields)
				.types(new Types(fields.getInterfaceFields()))
				.build()
				.write(writer);
		}
	}
}
