/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.dev.annotations.processors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.EqualsAndHashCode;

/**
 * A collection of Field objects to be used especially in template rendering.
 *
 * @author Daniel Bluhm
 */
@EqualsAndHashCode
public class Fields implements Iterable<Field> {

	/**
	 * The collection of fields on which views will be retrieved.
	 */
	@JsonValue
	private List<Field> list;

	public Fields() {
		this.list = new ArrayList<>();
	}

	/**
	 * Create Fields from existing collection of Field objects.
	 *
	 * @param fields initial fields
	 */
	@JsonCreator
	public Fields(Collection<Field> fields) {
		this.list = new ArrayList<>();
		this.list.addAll(fields);
	}

	/**
	 * Add fields to the collection.
	 *
	 * @param fields to add
	 */
	public void collect(Collection<Field> fields) {
		this.list.addAll(fields);
	}

	/**
	 * Return iterator over constant fields.
	 *
	 * @return iterator over fields
	 * @see Field#isConstant()
	 */
	public Iterator<Field> getConstants() {
		return list.stream()
			.filter(Field::isConstant)
			.iterator();
	}

	/**
	 * Return iterator over mutable fields (fields that are not constant).
	 *
	 * @return iterator over the fields
	 * @see Field#isConstant()
	 */
	public Iterator<Field> getMutable() {
		return list.stream()
			.filter(field -> !field.isConstant())
			.iterator();
	}

	/**
	 * Return iterator over fields that are set to be used in matches.
	 *
	 * @return iterator over the fields
	 * @see org.eclipse.ice.data.IDataElement#matches(Object)
	 */
	public Iterator<Field> getMatch() {
		return list.stream()
			.filter(Field::isMatch)
			.iterator();
	}

	/**
	 * Return iterator over fields where the variable name differs from the Field
	 * name.
	 *
	 * @return iterator over the fields
	 * @see Field#isVarDifferent()
	 */
	public Iterator<Field> getVarNamesDiffer() {
		return list.stream()
			.filter(Field::isVarNameDifferent)
			.iterator();
	}

	/**
	 * Return iterable of fields needed for interface.
	 * @return Iterable of fields needed for interface.
	 */
	public Iterable<Field> getInterfaceFields() {
		return list.stream()
			.filter(field -> !field.isDefaultField())
			.collect(Collectors.toList());
	}

	/**
	 * Return Fields that are not marked as default.
	 * @return Fields new instance with default fields filtered out.
	 */
	public Fields getNonDefaultFields() {
		return new Fields(
			list.stream()
				.filter(field -> !field.isDefaultField())
				.collect(Collectors.toList())
		);
	}

	/**
	 * Return Types instance for this set of Fields.
	 * @return Types instance
	 */
	public Types getTypes() {
		return new Types(this);
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
