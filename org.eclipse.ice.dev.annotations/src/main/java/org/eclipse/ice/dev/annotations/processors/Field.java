package org.eclipse.ice.dev.annotations.processors;

/**
 * Container for Field information, taken from DataField Annotations, in
 * simplified String form.
 */
public class Field {
	String name;
	String className;
	String defaultValue;
	boolean nullable;
	boolean primitive;
	boolean match;

	public Field(String name, String className, String defaultValue, boolean nullable, boolean primitive) {
		this.name = name;
		this.className = className;
		this.defaultValue = defaultValue;
		this.nullable = nullable;
		this.primitive = primitive;
		this.match = true;
	}

	public Field(String name, String className, String defaultValue, boolean nullable, boolean primitive, boolean match) {
		this.name = name;
		this.className = className;
		this.defaultValue = defaultValue;
		this.nullable = nullable;
		this.primitive = primitive;
		this.match = match;
	}

	public Field() {
		this.nullable = false;
		this.primitive = false;
		this.match = true;
	}

	public boolean isMatch() {
		return match;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}

	public boolean isPrimitive() {
		return primitive;
	}

	public void setPrimitive(boolean primitive) {
		this.primitive = primitive;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getClassName() {
		return className;
	}

	public String getName() {
		return name;
	}

	public void setClassName(final String className) {
		this.className = className;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Field (name=" + name + ", className=" + className + ")";
	}
}