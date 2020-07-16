package org.eclipse.ice.dev.pojofromjson;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.ice.dev.annotations.processors.DefaultFields;
import org.eclipse.ice.dev.annotations.processors.Fields;
import org.eclipse.ice.dev.annotations.processors.ImplementationWriter;
import org.eclipse.ice.dev.annotations.processors.InterfaceWriter;

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
			new InterfaceWriter(
				pojo.getPackageName(),
				pojo.getElement(),
				fields
			).write(elementInterface);
		}

		// Write implementation
		try (Writer elementImpl = Files.newBufferedWriter(
			destination.resolve(pojo.getImplementation() + ".java")
		)) {
			new ImplementationWriter(
				pojo.getPackageName(),
				pojo.getElement(),
				pojo.getImplementation(),
				fields
			).write(elementImpl);
		}
	}

	/**
	 * Read JSON form Standard In or from arguments and generate DataElement
	 * interfaces and implementations.
	 * @param args command line arguments are unused
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				handleInputJson(System.in, Path.of("."));
			}
			for (String filePath : args) {
				try (FileInputStream inputJson = new FileInputStream(filePath)) {
					handleInputJson(inputJson, Path.of("."));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
