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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.Persisted;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Flavor of ICEAnnotationExtractionService that specializes in extracting data from Spec classes with 
 * the class level annotation of DataElement
 *
 */
public class DataElementAnnotationExtractor {
	
	/**
	 * Annotations to not be transfered from member variables of Spec classes to final generated classes
	 */
	private static final List<String> nonTransferableAnnotations = Stream.of(
			DataField.class,
			DataField.Default.class
		).map(cls -> cls.getCanonicalName())
		.collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
	
	private ICEAnnotationExtractionService annotationExtractionService;
	private WriterGenerator writerGenerator;
	
	DataElementAnnotationExtractor(ICEAnnotationExtractionService annotationExtractionService, WriterGenerator writerGenerator) {
		this.annotationExtractionService = annotationExtractionService;
		this.writerGenerator = writerGenerator;
		this.annotationExtractionService.setNonTransferableAnnotations(nonTransferableAnnotations);
		this.annotationExtractionService.setFieldFilter(DataElementAnnotationExtractor::isDataField);
	}
	
	public List<VelocitySourceWriter> generateWriters(AnnotationExtractionRequest request) throws IOException {
		AnnotationExtractionResponse response = annotationExtractionService.extract(request);
		return writerGenerator.generateWriters(request.getElement(), response);
	}
	
	public void generateAndWrite(AnnotationExtractionRequest request) throws IOException {
		AnnotationExtractionResponse response = annotationExtractionService.extract(request);
		writerGenerator.generateWriters(request.getElement(), response)
		.forEach(writer -> {
			try {
				writer.write();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * Determine if the passed field is a DataField.
	 * @param element to check
	 * @return whether element is a DataField
	 */
	public static boolean isDataField(Element element) {
		return element.getAnnotation(DataField.class) != null;
	}
	
}
