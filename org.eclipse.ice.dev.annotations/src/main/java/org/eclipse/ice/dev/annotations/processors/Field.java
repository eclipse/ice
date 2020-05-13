package org.eclipse.ice.dev.annotations.processors;

/**
 * Container for Field information, taken from DataField Annotations, in
 * simplified String form.
 */
public class Field {
	String name;
	String className;

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