package org.eclipse.ice.dev.pojofromjson;

import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.ice.dev.annotations.processors.DefaultFields;
import org.eclipse.ice.dev.annotations.processors.Fields;
import org.eclipse.ice.dev.annotations.processors.ImplementationWriter;
import org.eclipse.ice.dev.annotations.processors.InterfaceWriter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Take JSON files and generate Java classes.
 * @author Daniel Bluhm
 */
public class PojoFromJson {

	public static PojoOutline readFromStdIn() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(System.in, PojoOutline.class);
	}

	public static void main(String[] args) {
		try {
			PojoOutline pojo = readFromStdIn();
			Fields fields = new Fields();
			fields.collect(DefaultFields.get());
			fields.collect(pojo.getFields());
			try (FileWriter elementInterface = new FileWriter(pojo.getElement() + ".java")) {
				new InterfaceWriter(
					pojo.getPackageName(),
					pojo.getElement(),
					fields
				).write(elementInterface);
			}
			try (FileWriter elementImpl = new FileWriter(pojo.getImplementation() + ".java")) {
				new ImplementationWriter(
					pojo.getPackageName(),
					pojo.getElement(),
					pojo.getImplementation(),
					fields
				).write(elementImpl);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
