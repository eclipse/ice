package org.eclipse.ice.dev.annotations.processors;

import javax.lang.model.element.Element;

/**
 * Interface for classes acting as extractors of annotation info.
 * @author Daniel Bluhm
 *
 * @param <T> the type of the information extracted by this annotation
 *        extractor.
 */
public interface AnnotationExtractor<T> {

	/**
	 * Extract information from element and annotations found on or within
	 * element. The subclass of element is dependent on the annotation and
	 * implementation of the extractor.
	 * @param element from which information will be extracted.
	 * @return extracted information
	 */
	public T extract(Element element);
}