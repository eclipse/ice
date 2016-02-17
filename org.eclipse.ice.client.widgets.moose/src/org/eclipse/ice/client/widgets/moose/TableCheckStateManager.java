/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith, Jordan Deyton - Initial API and implementation and/or initial documentation - Jordan H. Deyton, 
 *	 Jordan Deyton - bug 474744, refactored from MOOSECheckStateProvider 
 *******************************************************************************/
package org.eclipse.ice.client.widgets.moose;

import org.eclipse.ice.client.common.properties.TreeProperty;
import org.eclipse.ice.client.common.properties.TreePropertyCellContentProvider;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;

/**
 * A class which manages the content of a checkbox cell for a
 * MOOSETreePropertySection's table. It maintains the state of the checkbox, as
 * well as providing appropriate tooltip information for it.
 * 
 * @author Jordan Deyton
 * @author Robert Smith
 *
 */
public class TableCheckStateManager extends TreePropertyCellContentProvider
		implements ICheckStateProvider, ICheckStateListener {

	/**
	 * A reference to the viewer. The viewer needs to be updated when the
	 * checkbox cannot be changed for some reason (e.g., the property is
	 * required).
	 */
	private final StructuredViewer viewer;

	/**
	 * The default constructor.
	 * 
	 * @param viewer
	 *            The <b>table</b> for which this manager handles model
	 *            synchronization with checkboxes.
	 */
	public TableCheckStateManager(TableViewer viewer) {
		this.viewer = viewer;
	}

	/*
	 * Implements a method from ICheckStateProvider.
	 */
	@Override
	public boolean isChecked(Object element) {
		return (boolean) getValue(element);
	}

	/*
	 * Implements a method from ICheckStateProvider.
	 */
	@Override
	public boolean isGrayed(Object element) {
		// "gray" the required parameters so that they at least appear
		// differently from the removable, non-required parameters.
		return !isEnabled(element);
	}

	/*
	 * Implements a method from ICheckStateListener.
	 */
	@Override
	public void checkStateChanged(CheckStateChangedEvent event) {
		// Update the underlying TreeProperty. If its value did not change,
		// revert the checkbox to the previous state.
		Object element = event.getElement();
		if (!setValue(element, event.getChecked())) {
			viewer.refresh(element);
		}
	}

	/**
	 * Override the default behavior to show no text in the cell.
	 */
	@Override
	public String getText(Object element) {
		return "";
	}

	/**
	 * Override the default behavior to return a more informative tooltip if the
	 * element is disabled because it is required.
	 */
	@Override
	public String getToolTipText(Object element) {
		final String tooltip;
		if (isEnabled(element)) {
			tooltip = super.getToolTipText(element);
		} else {
			tooltip = "This parameter is required.";
		}
		return tooltip;
	}

	/**
	 * Returns true if the element is valid and the {@link TreeProperty}'s
	 * {@link Entry}'s tag is not "false" (case ignored), false otherwise.
	 */
	@Override
	public Object getValue(Object element) {

		// First, check that the element is valid.
		boolean isSelected = isValid(element);

		// If the element is valid, we should mark the flag as false only if the
		// tag is some variation of "false" (case ignored).
		if (isSelected) {
			String tag = ((TreeProperty) element).getEntry().getTag();
			isSelected = tag == null
					|| !"false".equals(tag.trim().toLowerCase());
		}

		return isSelected;
	}

	/**
	 * Sets the {@link TreeProperty}'s {@link Entry}'s tag to "true" if the
	 * value is true and "false" otherwise.
	 */
	@Override
	public boolean setValue(Object element, Object value) {

		boolean changed = false;

		if (isValid(element) && value != null && isEnabled(element)) {
			String newValue = ((Boolean) value).toString();
			IEntry entry = ((TreeProperty) element).getEntry();
			// If the value has changed, mark the changed flag and set the new
			// value.
			if (changed = !newValue.equals(entry.getTag())) {
				entry.setTag(newValue);
			}
		}

		return changed;
	}

	/**
	 * The cell is enabled if the {@link TreeProperty}'s {@link Entry} is
	 * required, otherwise it is disabled.
	 */
	@Override
	public boolean isEnabled(Object element) {
		return isValid(element)
				&& !((TreeProperty) element).getEntry().isRequired();
	}

}
