package org.eclipse.ice.dev.jsonschemaconverter;

import lombok.Data;

@Data
public class JsonNode {
	private String name;
	private String type;
	private String defaultValue;
	private String docString;

}
