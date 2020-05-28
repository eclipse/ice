package org.eclipse.ice.dev.annotations.processors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * An alternate name for a field. This may help identify another JSON attribute
 * name for a field, provide alternate Getters and Setters, etc.
 */
@AllArgsConstructor
@Getter
@Builder
public class FieldAlias {
	String alias;
	boolean getter;
	boolean setter;
	
	public String getGetterName() {
		return alias.substring(0, 1).toUpperCase() + alias.substring(1);
	}
}
