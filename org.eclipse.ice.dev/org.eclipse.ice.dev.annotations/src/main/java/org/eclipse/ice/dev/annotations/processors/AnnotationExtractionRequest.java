package org.eclipse.ice.dev.annotations.processors;

import java.util.List;
import java.util.function.Predicate;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnnotationExtractionRequest {
	private Element element;
	@Builder.Default
	private boolean includeDefaults = true;
	private String className;
}
