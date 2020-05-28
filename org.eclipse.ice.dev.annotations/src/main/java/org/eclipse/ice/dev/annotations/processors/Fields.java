package org.eclipse.ice.dev.annotations.processors;

import java.util.ArrayList;
import java.util.List;

/**
 * A container for fields discovered on a DataElement.
 *
 * Fields can create and hold onto an instance of FieldBuilder to assist in
 * constructing new Fields while visiting an AnnotationValue tree.
 */
class Fields {
	protected List<Field> fields;
	protected Field.FieldBuilder building;

	public Fields() {
		this.fields = new ArrayList<>();
		this.building = null;
	}

	/**
	 * Begin construction of a new Field.
	 */
	public void begin() {
		this.building = Field.builder();
	}

	/**
	 * Finish the under construction Field and append to fields list.
	 */
	public void finish() {
		this.fields.add(this.building.build());
		this.building = null;
	}

	public List<Field> getFields() {
		return fields;
	}

	/**
	 * @return true if there is an instance of Field currently under construction.
	 */
	public boolean isBuilding() {
		return this.building != null;
	}

	/**
	 * @return true if the currently under construction Field is ready to be finished.
	 */
	public boolean isComplete() {
		Field partial = this.building.build();
		return (partial.getType() != null) && (partial.getName() != null);
	}

	/**
	 * Shortcut to FieldBuilder.type().
	 * @param type
	 */
	public void setType(final String type) {
		this.building.type(Field.raw(type));
	}

	/**
	 * Shortcut to FieldBuilder.name().
	 * @param name
	 */
	public void setName(final String name) {
		this.building.name(name);
	}

	/**
	 * Shortcut to FieldBuilder.primitive().
	 * @param primitive
	 */
	public void setPrimitive(boolean primitive) {
		this.building.primitive(primitive);
	}

	@Override
	public String toString() {
		return fields.toString();
	}

	/**
	 * Append field to fields list.
	 * @param field
	 */
	public void add(Field field) {
		this.fields.add(field);
	}

	/**
	 * Extend fields with the contents of another Fields object.
	 * @param fields
	 */
	public void addAll(Fields fields) {
		this.fields.addAll(fields.getFields());
	}

	/**
	 * Extend fields with a list of Field objects.
	 * @param fields
	 */
	public void addAll(List<Field> fields) {
		this.fields.addAll(fields);
	}
}