/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/
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

import org.apache.velocity.app.Velocity;
import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
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
/**
 * @author Michael Walsh
 *
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
	
	/**
	 * Util object provided by the Velocity Templating Engine
	 */
	protected Elements elementUtils;
	
	/**
	 * For serialization and deserializion of objects to and from json during processing
	 */
	protected ObjectMapper mapper;
	
	/**
	 * For the extraction of key data from Spec classes used in class generation
	 */
	protected DataElementAnnotationExtractor extractor;
	
	
	/**
	 * Util class for various specific data extraction from Elements
	 */
	private SpecExtractionHelper specExtractionHelper = new SpecExtractionHelper();
	

	@Override
	public void init(final ProcessingEnvironment env) {
		messager = env.getMessager();
		elementUtils = env.getElementUtils();
		mapper = new ObjectMapper();
		
		ICEAnnotationExtractionService extractionService = new ICEAnnotationExtractionService(elementUtils, mapper, env, new DefaultNameGenerator());
		WriterGenerator writerGenerator = new DataElementWriterGenerator(env);
		this.extractor = new DataElementAnnotationExtractor(extractionService, writerGenerator);
		
		// Set up Velocity using the Singleton approach; ClasspathResourceLoader allows
		// us to load templates from src/main/resources
		final Properties p = VelocityProperties.get();
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
				
				AnnotationExtractionRequest request = AnnotationExtractionRequest.builder()
						.element(elem)
						.className(extractName(elem))
						.build();
				
				extractor.generateAndWrite(request);
				
			} catch (final IOException | InvalidDataElementSpec e) {
				messager.printMessage(Diagnostic.Kind.ERROR, stackTraceToString(e));
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
		return specExtractionHelper.getAnnotation(element, DataElement.class)
			.map(e -> e.name())
			.orElse(null);
	}
	
	/**
	 * Determine if a given annotated element is a valid class for transformation
	 * @param element
	 * @return
	 */
	private boolean valid(Element element) {
		return element.getKind() == ElementKind.CLASS;
	}
}
