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

import java.util.Collection;
import java.util.List;

import org.eclipse.ice.dev.annotations.processors.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

/**
 * Representation of information read in via JSON for POJO generation.
 * @author Daniel Bluhm
 */
@Data
@Builder
@JsonDeserialize(builder = PojoOutline.PojoOutlineBuilder.class)
public class PojoOutline {

	/**
	 * Default suffix for implementation names.
	 */
	private static final String IMPL_SUFFIX = "Implementation";

	/**
	 * Package of the generated classes.
	 */
	@JsonProperty("package")
	@NonNull private String packageName;

	/**
	 * Name of the Element (interface) to be generated.
	 */
	@NonNull private String element;

	/**
	 * Name of the implementation to be generated. If null, element + IMPL_SUFFIX is
	 * used.
	 */
	private String implementation;

	/**
	 * List of fields to generate on element.
	 */
	@Singular("field") private List<Field> fields;

	/**
	 * Get name of the implementation to generate.
	 * @return implementation name.
	 */
	public String getImplementation() {
		if (this.implementation == null) {
			return this.element + IMPL_SUFFIX;
		}
		return this.implementation;
	}
	
	/**
	 * JSON Serialization info for Builder.
	 */
	private interface PojoOutlineBuilderMeta {
		@JsonDeserialize(contentAs = Field.class)
		public PojoOutlineBuilder fields(Collection<? extends Field> fields);

		@JsonProperty("package")
		public PojoOutlineBuilder packageName(@NonNull String package_);
	}

	/**
	 * JSON Serialization info for Builder.
	 */
	@JsonPOJOBuilder(withPrefix = "")
	public static class PojoOutlineBuilder implements PojoOutlineBuilderMeta {}
}
