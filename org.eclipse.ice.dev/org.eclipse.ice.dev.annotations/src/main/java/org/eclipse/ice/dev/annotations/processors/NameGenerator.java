package org.eclipse.ice.dev.annotations.processors;

import javax.lang.model.element.Element;

public interface NameGenerator {

	String getImplName(String name);

	String getQualifiedImplName(String fullyQualifiedName);

	String getQualifiedPersistenceHandlerName(String fullyQualifiedName);

	String getPersistenceHandlerName(String name);

	String extractName(Element element);

	String extractCollectionName(Element element);

}
