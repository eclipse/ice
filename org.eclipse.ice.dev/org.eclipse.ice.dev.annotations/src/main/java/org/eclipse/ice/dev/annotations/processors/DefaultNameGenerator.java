package org.eclipse.ice.dev.annotations.processors;

import javax.lang.model.element.Element;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.Persisted;

public class DefaultNameGenerator implements NameGenerator {
	
	/**
	 * The value appended to DataElement implementation class names.
	 */
	private final static String IMPL_SUFFIX = "Implementation";
	
	/**
	 * The value appended to DataElement Persistence Handler class names.
	 */
	private final static String PERSISTENCE_SUFFIX = "PersistenceHandler";

	/**
	 * Return the element name as extracted from the DataElement annotation.
	 * @return the extracted name
	 */
	@Override
	public String extractName(Element element) {
		return ProcessorUtil.getAnnotation(element, DataElement.class)
			.map(e -> e.name())
			.orElse(null);
	}
	
	/**
	 * Return the collection name as extracted from the Persisted annotation.
	 * @return the extracted collection name
	 */
	@Override
	public String extractCollectionName(Element element) {
		return ProcessorUtil.getAnnotation(element, Persisted.class)
			.map(p -> p.collection())
			.orElse(null);
	}
	
	/**
	 * Get the name of the Implementation to be generated.
	 * @return implementation name
	 */
	@Override
	public String getImplName(String name) {
		return name + IMPL_SUFFIX;
	}
	
	/**
	 * Get the fully qualified name of the Implementation to be generated.
	 * @return fully qualified implementation name
	 */
	@Override
	public String getQualifiedImplName(String fullyQualifiedName) {
		return fullyQualifiedName + IMPL_SUFFIX;
	}
	
	/**
	 * Get the name of the Persistence Handler to be generated.
	 * @return persistence handler name
	 */
	@Override
	public String getPersistenceHandlerName(String name) {
		return name + PERSISTENCE_SUFFIX;
	}
	
	@Override
	public String getQualifiedPersistenceHandlerName(String fullyQualifiedName) {
		return fullyQualifiedName + PERSISTENCE_SUFFIX;
	}
}
