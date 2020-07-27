package org.eclipse.ice.tests.integration.dev.annotation;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.Persisted;

@DataElement(name = "TestElement")
@Persisted(collection = "test")
public class TestElementSpec {
	@DataField private String test;
}
