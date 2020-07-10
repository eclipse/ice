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

@Data
@Builder
@JsonDeserialize(builder = PojoOutline.PojoOutlineBuilder.class)
public class PojoOutline {

	private static final String IMPL_SUFFIX = "Implementation";

	@JsonProperty("package")
	@NonNull private String packageName;

	@NonNull private String element;

	private String implementation;

	@Singular("field") private List<Field> fields;

	public String getImplementation() {
		if (this.implementation == null) {
			return this.element + IMPL_SUFFIX;
		}
		return this.implementation;
	}
	
	private interface PojoOutlineBuilderMeta {
		@JsonDeserialize(contentAs = Field.class)
		public PojoOutlineBuilder fields(Collection<? extends Field> fields);

		@JsonProperty("package")
		public PojoOutlineBuilder packageName(@NonNull String package_);
	}

	@JsonPOJOBuilder(withPrefix = "")
	public static class PojoOutlineBuilder implements PojoOutlineBuilderMeta {}
}
