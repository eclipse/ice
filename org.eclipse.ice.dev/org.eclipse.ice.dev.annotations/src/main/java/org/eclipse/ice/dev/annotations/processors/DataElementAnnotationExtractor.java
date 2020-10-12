/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Walsh - Initial implementation
 *******************************************************************************/
package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;

import org.eclipse.ice.dev.annotations.DataField;

import lombok.Builder;

/**
 * Flavor of ICEAnnotationExtractionService that specializes in extracting data
 * from Spec classes with the class level annotation of {@link DataElement}
 *
 * @author Michael Walsh
 */
public class DataElementAnnotationExtractor {

	/**
	 * Annotations to not be transfered from member variables of Spec classes to
	 * final generated classes
	 */
	private static final List<String> nonTransferableAnnotations =
		Stream.of(DataField.class, DataField.Default.class)
			.map(Class::getCanonicalName)
			.collect(
				Collectors.collectingAndThen(
					Collectors.toList(), Collections::unmodifiableList
				)
			);

	/**
	 * used for extracting and preparing data for writer generation
	 */
	private ICEAnnotationExtractionService annotationExtractionService;
	
	/**
	 * Filer used for generating files.
	 */
	private Filer filer;

	/**
	 * used to generate writers based on the output of the annotation extraction
	 * service
	 */
	private WriterGenerator<AnnotationExtractionResponse> writerGenerator;

	/**
	 * Constructor that lets you initialize the {@link DataElementAnnotationExtractor} with different
	 * implementations of {@link ICEAnnotationExtractionService} and {@link WriterGenerator}
	 * @param annotationExtractionService
	 * @param writerGenerator
	 */
	@Builder
	DataElementAnnotationExtractor(
		ICEAnnotationExtractionService annotationExtractionService,
		Filer filer,
		WriterGenerator<AnnotationExtractionResponse> writerGenerator
	) {
		this.annotationExtractionService = annotationExtractionService;
		this.filer = filer;
		this.writerGenerator = writerGenerator;
		this.annotationExtractionService.setNonTransferableAnnotations(nonTransferableAnnotations);
		this.annotationExtractionService.setFieldFilter(DataElementAnnotationExtractor::isDataField);
	}

	/**
	 * For a given request it will extract data from client classes
	 * and generate a list of {@link VelocitySourceWriter}
	 *
	 * @param request
	 * @return list of generated SourceWriters
	 * @throws IOException due to {@link ICEAnnotationExtractionService#extract(AnnotationExtractionRequest)}
	 */
	public List<GeneratedFileWriter> generateWriters(
		AnnotationExtractionRequest request
	) throws IOException {
		AnnotationExtractionResponse response = annotationExtractionService.extract(request);
		return writerGenerator.generate(response);
	}

	/**
	 * For a given request it will generate then execute writers
	 *
	 * @param request
	 * @throws IOException
	 */
	public void generateAndWrite(AnnotationExtractionRequest request) throws IOException {
		generateWriters(request)
			.forEach(writer -> {
				try (Writer file = writer.openWriter(filer)) {
					writer.write(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
	}

	/**
	 * Determine if the passed field is a DataField.
	 *
	 * @param element to check
	 * @return whether element is a DataField
	 */
	public static boolean isDataField(Element element) {
		return element.getAnnotation(DataField.class) != null;
	}
}
