package org.eclipse.ice.renderer;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement
@DataField(fieldName = "age", fieldType = String.class)
@DataField(fieldName = "name", fieldType = String.class)
public interface Person {
}
