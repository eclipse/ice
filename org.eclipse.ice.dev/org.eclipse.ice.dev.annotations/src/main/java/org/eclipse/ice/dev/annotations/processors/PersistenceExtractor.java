package org.eclipse.ice.dev.annotations.processors;

import javax.lang.model.element.Element;

import org.eclipse.ice.dev.annotations.Persisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extractor for persistence metadata.
 * @author Daniel Bluhm
 */
public class PersistenceExtractor implements AnnotationExtractor<PersistenceMetadata> {
	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(PersistenceExtractor.class);

	@Override
	public PersistenceMetadata extract(Element element) throws InvalidElementException {
		Persisted persisted = element.getAnnotation(Persisted.class);
		if (persisted == null) {
			throw new InvalidElementException(
				"Persisted annotation not found on element."
			);
		}
		return PersistenceMetadata.builder()
			.collection(persisted.collection())
			.build();
	}

	@Override
	public Logger log() {
		return logger;
	}

}
