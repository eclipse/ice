/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.widgets.moose.components;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;

/**
 * This class can be used to listen to an {@link Entry}. This is just for
 * convenience to avoid having to cast IUpdateables to Entries when making an
 * IUpdateableListener.
 * 
 * @author djg
 * 
 */
public abstract class EntryListener implements IUpdateableListener {

	/**
	 * The entry that is this listener observes.
	 */
	protected final Entry entry;

	/**
	 * The default constructor. Registers with the entry.
	 * 
	 * @param entry
	 *            The Entry to listen to.
	 */
	public EntryListener(Entry entry) {
		if (entry != null) {
			this.entry = entry;
			entry.register(this);
		} else {
			throw new IllegalArgumentException("EntryListener error: "
					+ "Cannot register with null entry!");
		}
	}

	/**
	 * Redirects the update method to {@link #updateEntry(Entry)}.
	 */
	public final void update(IUpdateable component) {
		if (component == entry) {
			updateEntry();
		}
	}

	/**
	 * This method is only called if the Entry that was specified in the
	 * constructor is valid and has been updated.
	 */
	public abstract void updateEntry();

	/**
	 * Utility method for converting the Entry's value into an integer.
	 * 
	 * @return A valid integer if the value could be parsed, null if a
	 *         NumberFormatException was encountered.
	 */
	protected final Integer parseInteger() {
		Integer value;

		try {
			value = Integer.parseInt(entry.getValue().trim());
		} catch (NumberFormatException e) {
			value = null;
		}

		return value;
	}

	/**
	 * Utility method for converting an Entry's value into a double.
	 * 
	 * @return A valid double if the value could be parsed, null if a
	 *         NumberFormatException was encountered.
	 */
	protected final Double parseDouble() {
		Double value;

		try {
			value = Double.parseDouble(entry.getValue().trim());
		} catch (NumberFormatException e) {
			value = null;
		}

		return value;
	}

	/**
	 * Utility method for converting an Entry's value into a double array.
	 * 
	 * @param size
	 *            The expected size of the array.
	 * @return A double array if successful, or null if the size is 0 or less or
	 *         if the Entry's value could not be converted.
	 */
	protected final double[] parseDoubleArray(int size) {
		double[] array = null;

		if (size > 0) {
			try {

				String[] split = entry.getValue().replace("'", "").trim()
						.split("\\s+");
				if (split.length == size) {
					array = new double[size];
					for (int i = 0; i < size; i++) {
						array[i] = Double.parseDouble(split[i]);
					}
				}
			} catch (NumberFormatException e) {
				array = null;
			}
		}

		return array;
	}

}
