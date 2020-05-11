package org.eclipse.ice.dev.annotations.processors;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple container for fields discovered on a DataElement.
 */
class Fields {
	/**
	 * Container for name and class name attributes of DataField in string
	 * representation.
	 */

	protected List<Field> fields;
	protected Field building;

	public Fields() {
		this.fields = new ArrayList<>();
		this.building = null;
	}

	public void begin() {
		this.building = new Field();
	}

	public void finish() {
		this.fields.add(this.building);
		this.building = null;
	}

	public List<Field> getFields() {
		return fields;
	}

	public boolean isBuilding() {
		return this.building != null;
	}

	public boolean isComplete() {
		return (this.building.getClassName() != null) && (this.building.getName() != null);
	}

	public void setClassName(final String className) {
		this.building.setClassName(className);
	}

	public void setName(final String name) {
		this.building.setName(name);
	}

	@Override
	public String toString() {
		return fields.toString();
	}
}