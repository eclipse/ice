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
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.StandardLocation;

import org.eclipse.ice.dev.annotations.DataFieldJson;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Extraction helper that specializes in pulling Spec data from json
 *
 */
public class JsonExtractionHelper implements ExtractionHelper {
	/**
	 * Collect Fields from DataFieldJson Annotations.
	 *
	 * The JSON input files are searched for in the "CLASS_OUTPUT" location, meaning
	 * the same folder to which compiled class files will be output. JSON files
	 * placed in src/main/resources are moved to this location before the annotation
	 * processing phase and are therefore available at this location at the time of
	 * annotation processing.
	 *
	 * @param element
	 * @param processingEnv
	 * @param mapper
	 * @return
	 * @throws IOException
	 */
	public List<Field> collectFromDataFieldJson(Element element, ProcessingEnvironment processingEnv,
			ObjectMapper mapper) throws IOException {
		List<Field> fields = new ArrayList<>();
		// Iterate through each JSON Data Field source and attempt to read
		// fields from JSON file.
		for (String source : getDataFieldJsonFileNames(element)) {
			Reader reader = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, "", source)
					.openReader(false);
			fields.addAll(Arrays.asList(mapper.readValue(reader, Field[].class)));
		}
		return fields;
	}

	/**
	 * Collect JSON File Strings from DataFieldJson Annotations.
	 * 
	 * @param element
	 * @return
	 */
	public List<String> getDataFieldJsonFileNames(Element element) {
		return getAnnotation(element, DataFieldJson.class).map(jsons -> Arrays.asList(jsons.value()))
				.orElse(Collections.emptyList());
	}

	/**
	 * Return stack trace as string.
	 * 
	 * @param e subject exception
	 * @return stack trace as string
	 */
	public String stackTraceToString(final Throwable e) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

}
