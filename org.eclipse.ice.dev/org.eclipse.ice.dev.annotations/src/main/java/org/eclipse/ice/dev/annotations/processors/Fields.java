package org.eclipse.ice.dev.annotations.processors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A collection of Field objects to be used especially in template rendering.
 * @author Daniel Bluhm
 */
public class Fields implements Iterable<Field> {

	/**
	 * The collection of fields on which views will be retrieved.
	 */
	private List<Field> fields;

	public Fields() {
		this.fields = new ArrayList<>();
	}

	/**
	 * Create Fields from existing collection of Field objects.
	 * @param fields initial fields
	 */
	public Fields(Collection<Field> fields) {
		this.fields = new ArrayList<>();
		this.fields.addAll(fields);
	}

	/**
	 * Add fields to the collection.
	 * @param fields to add
	 */
	public void collect(Collection<Field> fields) {
		this.fields.addAll(fields);
	}

	/**
	 * Return iterator over constant fields.
	 *
	 * @return iterator over fields
	 * @see Field#isConstant()
	 */
	public Iterator<Field> getConstants() {
		return fields.stream()
			.filter(field -> field.isConstant())
			.iterator();
	}

	/**
	 * Return iterator over mutable fields (fields that are not constant).
	 *
	 * @return iterator over the fields
	 * @see Field#isConstant()
	 */
	public Iterator<Field> getMutable() {
		return fields.stream()
			.filter(field -> !field.isConstant())
			.iterator();
	}

	/**
	 * Return iterator over fields that are set to be used in matches.
	 *
	 * @return iterator over the fields
	 * @see org.eclipse.ice.dev.annotations.IDataElement#matches(Object)
	 */
	public Iterator<Field> getMatch() {
		return fields.stream()
			.filter(field -> field.isMatch())
			.iterator();
	}

	/**
	 * Return iterator over fields where the variable name differs from the
	 * Field name.
	 *
	 * @return iterator over the fields
	 * @see Field#isVarDifferent()
	 */
	public Iterator<Field> getVarNamesDiffer() {
		return fields.stream()
			.filter(field -> field.isVarNameDifferent())
			.iterator();
	}

	/**
	 * Returns an iterator over the mutable fields found in this collection. The
	 * mutable fields are the default set iterated over because they are the most
	 * used fields in the templates for DataElement generation.
	 */
	@Override
	public Iterator<Field> iterator() {
		return this.getMutable();
	}
}
