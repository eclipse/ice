package org.eclipse.ice.dev.annotations;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldInfo {
	
	private boolean getter = true;
	
	private boolean setter = true;
	
	private boolean match = true;
	
	private boolean unique = true;
	
	private boolean search = true;
	
	private boolean nullable = true;
}
