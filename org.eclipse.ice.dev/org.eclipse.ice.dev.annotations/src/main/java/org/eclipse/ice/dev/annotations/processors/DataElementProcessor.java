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
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.DataModel;
import org.eclipse.ice.dev.annotations.Persisted;
import org.eclipse.ice.dev.annotations.Validator;

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
	protected ICEAnnotationExtractionService extractionService;

	@Override
	public void init(final ProcessingEnvironment env) {
		messager = env.getMessager();
		elementUtils = env.getElementUtils();
		mapper = new ObjectMapper();
		extractionService = new ICEAnnotationExtractionService(elementUtils, mapper, env);

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
				
				AnnotationExtractionResponse response = extractionService.extract(
						AnnotationExtractionRequest.builder()
						.element(elem)
						.handledAnnotations(Set.of(
								DataField.class,
								DataField.Default.class,
								DataModel.class,
								Validator.class
							).stream()
								.map(cls -> cls.getCanonicalName())
								.collect(Collectors.toList()))
						.fieldFilter(DataFieldSpec::isDataField)
						.className(extractName(elem))
						.build());

				// Write the DataElement's interface to file.
				ProcessorUtil.writeInterface(response, processingEnv, INTERFACE_TEMPLATE);

				// Write the DataElement Implementation to file.
				ProcessorUtil.writeClass(response, processingEnv, DATAELEMENT_TEMPLATE);

				// Check if Persistence should be generated.
				if (ProcessorUtil.hasAnnotation(elem, Persisted.class)) {
					ProcessorUtil.writePersistence(
							response,
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
		return element.getKind().isClass() && (element instanceof TypeElement) && element.getKind() != ElementKind.ENUM;
	}

}
