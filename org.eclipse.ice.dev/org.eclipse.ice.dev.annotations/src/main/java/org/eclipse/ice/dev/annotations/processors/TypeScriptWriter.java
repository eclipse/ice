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

import java.util.Map;

import lombok.Builder;
import lombok.NonNull;

/**
 * Writer for TypeScript representation of DataElement.
 * @author Daniel Bluhm
 */
public class TypeScriptWriter extends VelocitySourceWriter {

	/**
	 * Template used for this writer.
	 */
	private static final String TEMPLATE = "templates/TypeScript.vm";

	/**
	 * Context key for name.
	 */
	private static final String NAME = "name";

	/**
	 * Context key for fields.
	 */
	private static final String FIELDS = "fields";

	/**
	 * Context key for types.
	 */
	private static final String TYPES = "types";

	/**
	 * Context key for primitiveMap.
	 */
	private static final String PRIMITIVE_MAP = "primitiveMap";

	/**
	 * Map of Java primitive + String type strings to TypeScript type strings.
	 */
	private static Map<String, String> primitiveMap = Map.ofEntries(
		Map.entry("String", "string"),
		Map.entry("boolean", "boolean"),
		Map.entry("float", "number"),
		Map.entry("long", "number"),
		Map.entry("int", "number"),
		Map.entry("double", "number")
	);

	/**
	 * Create Writer.
	 * @param name of TypeScript class generated.
	 * @param fields present on data element.
	 * @param types of fields.
	 * @throws UnsupportedOperationException When any field is not supported.
	 */
	@Builder
	public TypeScriptWriter(
		String name, @NonNull Fields fields, @NonNull Types types
	) throws UnsupportedOperationException {
		super();
		for (Field field : fields) {
			if (!primitiveMap.containsKey(types.resolve(field.getType()))) {
				throw new UnsupportedOperationException(String.format(
					"Field %s: type %s is unsupported",
					field.getName(), field.getType()
				));
			}
		}
		this.template = TEMPLATE;
		this.context.put(NAME, name);
		this.context.put(FIELDS, fields);
		this.context.put(TYPES, types);
		this.context.put(PRIMITIVE_MAP, primitiveMap);
	}
}