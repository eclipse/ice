package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.util.List;

import javax.lang.model.element.Element;

public interface WriterGenerator {
	public List<VelocitySourceWriter> generateWriters(Element element,AnnotationExtractionResponse response) throws IOException;
}
