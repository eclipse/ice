package org.eclipse.ice.dev.annotations.processors;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PersistenceWriterGenerator implements WriterGenerator {
	
	/**
	 * DataElement Metadata.
	 */
	private DataElementMetadata dataElement;
	
	/**
	 * Persistence Metadata.
	 */
	private PersistenceMetadata persistence;

	@Override
	public List<GeneratedFileWriter> generate() {
		return List.of(
			PersistenceHandlerWriter.builder()
				.packageName(dataElement.getPackageName())
				.elementInterface(dataElement.getName())
				.className(dataElement.getName() + "PersistenceHandler")
				// TODO Just move interface name into template (it's static)
				.interfaceName("IPersistenceHanlder")
				.implementation(dataElement.getName()+ "Implementation")
				.collection(persistence.getCollection())
				.fields(dataElement.getFields())
				.types(dataElement.getFields().getTypes())
				.build()
		);
	}

}
