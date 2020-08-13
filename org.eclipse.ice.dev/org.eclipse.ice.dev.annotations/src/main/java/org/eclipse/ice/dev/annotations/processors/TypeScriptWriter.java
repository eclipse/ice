package org.eclipse.ice.dev.annotations.processors;

import java.util.Map;

import lombok.Builder;
import lombok.NonNull;

public class TypeScriptWriter extends VelocitySourceWriter {
	private static final String TEMPLATE = "templates/TypeScript.vm";
	private static final String NAME = "name";
	private static final String FIELDS = "fields";
	private static final String TYPES = "types";
	private static final String PRIMITIVE_MAP = "primitiveMap";

	private static Map<String, String> primitiveMap = Map.ofEntries(
		Map.entry("String", "string"),
		Map.entry("boolean", "boolean"),
		Map.entry("float", "number"),
		Map.entry("long", "number"),
		Map.entry("int", "number"),
		Map.entry("double", "number")
	);

	@Builder
	public TypeScriptWriter(
		String name, @NonNull Fields fields, @NonNull Types types
	) throws Exception {
		super();
		for (Field field : fields) {
			if (!primitiveMap.containsKey(types.resolve(field.getType()))) {
				throw new Exception(
					"Field " + field.getName() + " can not be processed."
				);
			}
		}
		this.template = TEMPLATE;
		this.context.put(NAME, name);
		this.context.put(FIELDS, fields);
		this.context.put(TYPES, types);
		this.context.put(PRIMITIVE_MAP, primitiveMap);
	}
}