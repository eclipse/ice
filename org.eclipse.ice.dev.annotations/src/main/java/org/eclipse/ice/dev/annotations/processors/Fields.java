package org.eclipse.ice.dev.annotations.processors;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple container for fields discovered on a DataElement.
 */
class Fields {
	protected List<Field> fields;
	protected Field.FieldBuilder building;

	public Fields() {
		this.fields = new ArrayList<>();
		this.building = null;
	}

	public void begin() {
		this.building = Field.builder();
	}

	public void finish() {
		this.fields.add(this.building.build());
		this.building = null;
	}

	public List<Field> getFields() {
		return fields;
	}

	public boolean isBuilding() {
		return this.building != null;
	}

	public boolean isComplete() {
		Field partial = this.building.build();
		return (partial.getType() != null) && (partial.getName() != null);
	}

	public void setType(final String type) {
		this.building.type(Field.raw(type));
	}

	public void setName(final String name) {
		this.building.name(name);
	}

	public void setPrimitive(boolean primitive) {
		this.building.primitive(primitive);
	}

	@Override
	public String toString() {
		return fields.toString();
	}

	public void add(Field field) {
		this.fields.add(field);
	}

	public void addAll(Fields fields) {
		this.fields.addAll(fields.getFields());
	}

	public void addAll(List<Field> fields) {
		this.fields.addAll(fields);
	}

	public void addAll(Field[] fields) {
		if (fields == null) {
			return;
		}
		for (int i = 0; i < fields.length; i++) {
			this.fields.add(fields[i]);
		}
	}
}