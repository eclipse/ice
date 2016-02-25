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
package org.eclipse.eavp.viz.service.connections.preferences;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;

/**
 * For individual columns in a JFace {@link TableViewer}, we often require a
 * {@link CellLabelProvider} to generate labels for table cells and an
 * {@link EditingSupport} to enable editing the values in those cells. An
 * <code>ICellContentProvider</code> provides the necessary methods to link
 * these to the underlying model so that the code that touches the model resides
 * primarily in the content provider.
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IVizCellContentProvider {

	/**
	 * Gets whether or not the specified element is a valid <code>Object</code>
	 * to be put in a cell.
	 * 
	 * @param element
	 *            The element to test.
	 * @return True if the element is supported by this content provider, false
	 *         otherwise.
	 */
	public boolean isValid(Object element);

	/**
	 * Gets whether or not the specified element's cell should be enabled.
	 * 
	 * @param element
	 *            The element to test.
	 * @return True if the element's cell should be enabled, false otherwise.
	 */
	public boolean isEnabled(Object element);

	/**
	 * Gets the string to display for the specified element's cell.
	 * 
	 * @param element
	 *            The element that needs a string.
	 * @return A string representing the element. This value should never be
	 *         <code>null</code>.
	 */
	public String getText(Object element);

	/**
	 * Gets the string to display for the specified element's cell tool tip.
	 * 
	 * @param element
	 *            The element that needs a tool tip.
	 * @return A string representing the element. This value should never be
	 *         <code>null</code>.
	 */
	public String getToolTipText(Object element);

	/**
	 * Gets an <code>Image</code> representing the specified element. This will
	 * be placed in its cell.
	 * 
	 * @param element
	 *            The element that needs a cell <code>Image</code>.
	 * @return An <code>Image</code> for the cell, or null if <code>Image</code>
	 *         s are not supported.
	 */
	public Image getImage(Object element);

	/**
	 * Gets the current value associated with the specified element.
	 * 
	 * @param element
	 *            The element whose value is requested.
	 * @return The value that should be represented in the element's cell, or
	 *         null if the element is invalid.
	 */
	public Object getValue(Object element);

	/**
	 * Sets a new value for the specified element.
	 * 
	 * @param element
	 *            The element whose value is being set.
	 * @param value
	 *            The value that should be set for the element. This is expected
	 *            to match the underlying element's value type.
	 * @return True if the value changed, false otherwise.
	 */
	public boolean setValue(Object element, Object value);

}
