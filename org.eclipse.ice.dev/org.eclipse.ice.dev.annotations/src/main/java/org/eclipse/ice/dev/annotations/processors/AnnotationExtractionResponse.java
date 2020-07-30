package org.eclipse.ice.dev.annotations.processors;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.processors.AnnotationExtractionRequest.AnnotationExtractionRequestBuilder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnnotationExtractionResponse {
	private Fields fields;
	private Map classMetadata;
	List<VelocitySourceWriter> writers;
}
