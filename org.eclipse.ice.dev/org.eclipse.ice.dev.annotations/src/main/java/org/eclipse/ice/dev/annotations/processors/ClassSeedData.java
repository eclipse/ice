package org.eclipse.ice.dev.annotations.processors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassSeedData {
	private String name;
	private String packageName;
	private String fullyQualifiedName;
	private String collectionName;
	private Fields fields;
}