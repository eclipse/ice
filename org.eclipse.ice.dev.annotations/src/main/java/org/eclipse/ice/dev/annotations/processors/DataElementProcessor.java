package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
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


	protected Messager messager;
	protected Elements elementUtils;
	protected ObjectMapper mapper;

	@Override
	public void init(final ProcessingEnvironment env) {
		messager = env.getMessager();
		elementUtils = env.getElementUtils();
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
				
				if(!valid(elem)) throw new InvalidDataElementSpec(
						"DataElementSpec must be class, found " + elem.toString()
						);
				
				DataElementSpec dataElement = new DataElementSpec(elem, elementUtils);
				Fields fields = new Fields();

				// Collect fields from Defaults, DataField Annotations, and DataFieldJson
				// Annotations.
				fields.collect(DefaultFields.get());
				fields.collect(dataElement.fieldsFromDataFields());
				fields.collect(ProcessorUtil.collectFromDataFieldJson(elem, processingEnv, mapper));

				// Write the DataElement's interface to file.
				ProcessorUtil.writeInterface(dataElement, fields, processingEnv, INTERFACE_TEMPLATE);

				// Write the DataElement Implementation to file.
				ProcessorUtil.writeClass(dataElement, fields, processingEnv, DATAELEMENT_TEMPLATE);

				// Check if Persistence should be generated.
				if (dataElement.hasAnnotation(Persisted.class)) {
					ProcessorUtil.writePersistence(
							dataElement,
							dataElement.getCollectionName(),
							fields,
							processingEnv,
							PERSISTENCE_HANDLER_TEMPLATE
						);
				}
			} catch (final IOException | InvalidDataElementSpec e) {
				messager.printMessage(Diagnostic.Kind.ERROR, ProcessorUtil.stackTraceToString(e));
				return false;
			}
		}
		return false;
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
	
	private boolean valid(Element element) {
		return element.getKind().isClass() && (element instanceof TypeElement);
	}

}
