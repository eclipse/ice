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

package org.eclipse.ice.dev.pojofromjson;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.dev.annotations.processors.DefaultFields;
import org.eclipse.ice.dev.annotations.processors.Fields;
import org.eclipse.ice.dev.annotations.processors.ImplementationWriter;
import org.eclipse.ice.dev.annotations.processors.InterfaceWriter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Read JSON from Standard In and generate DataElement interface and
 * implementation.
 * @author Daniel Bluhm
 */
public class PojoFromJson {

	/**
	 * Mapper used for deserializing POJO Outline JSON
	 */
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * List of files to operate on.
	 */
	@Parameter(description = "FILE [FILE...]")
	private List<String> jsonFiles = new ArrayList<>();

	/**
	 * Directory to output generated files into.
	 */
	@Parameter(names = {"-o", "--output"}, description = "Output directory")
	private String output = ".";

	/**
	 * Display help text.
	 */
	@Parameter(names = "--help", description = "Display this usage text", help = true)
	private boolean help;

	/**
	 * Read from Input and write interface and implementation to files in
	 * destination.
	 * @param is InputStream of POJO Outline JSON
	 * @param destination directory in which files will be generated
	 * @throws JsonParseException On failure to parse POJO Outline JSON
	 * @throws JsonMappingException On failure to map to POJO Outline
	 * @throws IOException On failure to open file for writing
	 */
	public static void handleInputJson(
		InputStream is, Path destination
	) throws JsonParseException, JsonMappingException, IOException {
		// Parse outline from input stream
		PojoOutline pojo = mapper.readValue(is, PojoOutline.class);

		// Collect fields
		Fields fields = new Fields();
		fields.collect(DefaultFields.get());
		fields.collect(pojo.getFields());

		// Write Interface
		try (Writer elementInterface = Files.newBufferedWriter(
			destination.resolve(pojo.getElement() + ".java")
		)) {
			InterfaceWriter.builder()
				.packageName(pojo.getPackageName())
				.interfaceName(pojo.getElement())
				.fields(fields)
				.build()
				.write(elementInterface);
		}

		// Write implementation
		try (Writer elementImpl = Files.newBufferedWriter(
			destination.resolve(pojo.getImplementation() + ".java")
		)) {
			ImplementationWriter.builder()
				.packageName(pojo.getPackageName())
				.interfaceName(pojo.getElement())
				.className(pojo.getImplementation())
				.fields(fields)
				.build()
				.write(elementImpl);
		}
	}

	/**
	 * Execution entry point
	 * @param args from command line
	 */
	public static void main(String[] args) {
		PojoFromJson pfj = new PojoFromJson();
		pfj.run(args);
	}

	/**
	 * Read JSON form Standard In or from arguments and generate DataElement
	 * interfaces and implementations.
	 * @param args from command line
	 */
	public void run(String... args) {
		JCommander jcomm = JCommander.newBuilder()
			.addObject(this)
			.build();
		jcomm.setProgramName("POJOfromJSON");
		jcomm.parse(args);

		if (help) {
			jcomm.usage();
			return;
		}

		try {
			if (jsonFiles.size() == 0) {
				handleInputJson(System.in, Path.of(output));
			}
			for (String filePath : jsonFiles) {
				try (FileInputStream inputJson = new FileInputStream(filePath)) {
					handleInputJson(inputJson, Path.of(output));
				}
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}
}
