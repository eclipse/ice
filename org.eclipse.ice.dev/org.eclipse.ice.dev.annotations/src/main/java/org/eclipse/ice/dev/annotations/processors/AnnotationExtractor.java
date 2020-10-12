package org.eclipse.ice.dev.annotations.processors;

import java.util.Optional;

import javax.lang.model.element.Element;

import org.slf4j.Logger;

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
	 * @throws InvalidElementException if element is not annotated as expected
	 *         for this annotation extractor.
	 */
	public T extract(Element element) throws InvalidElementException;

	/**
	 * Get handle to logger.
	 * @return logger.
	 */
	public Logger log();

	/**
	 * Extract information from element and annotations found on or within
	 * element if possible, return empty otherwise.
	 * @param element from which information will be extracted.
	 * @return extracted information wrapped in optional or empty.
	 */
	public default Optional<T> extractIfApplies(Element element) {
		Optional<T> value = Optional.empty();
		try {
			value = Optional.of(extract(element));
		} catch (InvalidElementException e) {
			log().debug(
				"Failed to extract metadata from annotation, returning empty:",
				e
			);
		}
		return value;
	}
}