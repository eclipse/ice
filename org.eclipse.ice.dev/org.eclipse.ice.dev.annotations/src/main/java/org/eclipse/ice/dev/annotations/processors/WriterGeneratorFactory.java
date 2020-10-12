package org.eclipse.ice.dev.annotations.processors;

import java.util.Optional;
import java.util.Set;

/**
 * Factory for WriterGenerators. Create method parameters represent dependencies
 * of a set of generators.
 * @author Daniel Bluhm
 */
public class WriterGeneratorFactory {
	private WriterGeneratorFactory() {}

	public static Set<WriterGenerator> create(
		DataElementMetadata dataElement
	) {
		return Set.of(new DataElementWriterGenerator(dataElement));
	}

	public static Set<WriterGenerator> create(
		DataElementMetadata dataElement,
		Optional<PersistenceMetadata> persistence
	) {
		Set<WriterGenerator> value = null;
		if (persistence.isEmpty()) {
			value = create(dataElement);
		} else {
			value = Set.of(
				new DataElementWriterGenerator(dataElement),
				new PersistenceWriterGenerator(dataElement, persistence.get())
			);
		}
		return value;
	}
}