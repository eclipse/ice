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
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import org.eclipse.ice.dev.annotations.DataElement;

import com.google.auto.service.AutoService;

/**
 * Processor for DataElement Annotations.
 *
 * This will generate an implementation for an interface annotated with
 * DataElement, populating the implementation with metadata and fields specified
 * with the DataField annotation.
 *
 * @author Daniel Bluhm
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
	 *
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
	 * logger
	 */
	protected Messager messager;

	/**
	 * Elements utilities from processing environment.
	 */
	protected Elements elementUtils;

	@Override
	public synchronized void init(final ProcessingEnvironment env) {
		this.messager = env.getMessager();
		this.elementUtils = env.getElementUtils();
		super.init(env);
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		// Initialize extractors
		// TODO Move this to a service?
		DataElementExtractor dataElementExtractor = DataElementExtractor.builder()
			.elementUtils(elementUtils)
			.dataFieldExtractor(new DataFieldExtractor(elementUtils))
			.build();
		PersistenceExtractor persistenceExtractor = new PersistenceExtractor();
		WriterGeneratorFactory generatorFactory = new WriterGeneratorFactory(
			Set.of(
				DataElementWriterGenerator.class,
				PersistenceWriterGenerator.class
			)
		);

		// Iterate over all elements with DataElement Annotation
		for (final Element element : roundEnv.getElementsAnnotatedWith(DataElement.class)) {
			try {
				// Create and populate DataPool
				Map<Class<?>, Object> dataPool = new HashMap<>();
				dataPool.put(
					DataElementMetadata.class,
					dataElementExtractor.extract(element)
				);
				Optional<PersistenceMetadata> persistence =
					persistenceExtractor.extractIfApplies(element);
				if (persistence.isPresent()) {
					dataPool.put(PersistenceMetadata.class, persistence.get());
				}

				// Get flattened list of GeneratedFileWriters from set of
				// Generators.
				List<GeneratedFileWriter> fileWriters =
					generatorFactory.create(dataPool).stream()
						// generators into GeneratedFileWriter Streams
						.flatMap(generator -> generator.generate().stream())
						// Collect into flattened list
						.collect(Collectors.toList());

				// Run the writers
				for (GeneratedFileWriter fileWriter : fileWriters) {
					try (Writer writer = fileWriter.openWriter(processingEnv.getFiler())) {
						fileWriter.write(writer);
					}
				}
			} catch (final IOException | InvalidElementException e) {
				messager.printMessage(Diagnostic.Kind.ERROR, stackTraceToString(e));
				return false;
			}
		}
		return false;
	}
}
