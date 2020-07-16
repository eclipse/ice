package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import org.apache.velocity.app.Velocity;
import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.DataModel;
import org.eclipse.ice.dev.annotations.Persisted;
import org.eclipse.ice.dev.annotations.Validator;

import javax.lang.model.element.Element;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;

@SupportedAnnotationTypes({
	"org.eclipse.ice.dev.annotations.DataModel",
	"org.eclipse.ice.dev.annotations.Validator"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DataModelProcessor extends AbstractProcessor {
	
	/**
	 * Location of DataModel template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String DATAELEMENT_TEMPLATE = "templates/DataModel.vm";

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
	private static final String INTERFACE_TEMPLATE = "templates/ModelInterface.vm";
	
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
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		// Iterate over all elements with DataModel Annotation
				for (final Element elem : roundEnv.getElementsAnnotatedWith(DataModel.class)) {
					try {
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
								.className(extractName(elem))
								.build());

						// Write the DataElement's interface to file.
						ProcessorUtil.writeInterface(response, processingEnv, INTERFACE_TEMPLATE);

						// Write the DataElement Implementation to file.
						ProcessorUtil.writeClass(response, processingEnv, DATAELEMENT_TEMPLATE);

					} catch (final IOException e) {
						messager.printMessage(Diagnostic.Kind.ERROR, ProcessorUtil.stackTraceToString(e));
						return false;
					}
				}
				return false;
	}
	
	/**
	 * Return the element name as extracted from the DataModel annotation.
	 * @return the extracted name
	 */
	private String extractName(Element element) {
		return ProcessorUtil.getAnnotation(element, DataModel.class)
			.map(e -> e.name())
			.orElse(null);
	}

}
