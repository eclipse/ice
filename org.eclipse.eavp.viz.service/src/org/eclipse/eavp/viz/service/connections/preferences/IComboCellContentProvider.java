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

import java.util.List;

/**
 * This interface provides additional methods for accessing an
 * {@link ICellContentProvider} that may have a discrete set of allowed values
 * associated with it. In such a case, the widgets used to edit the cell should
 * represent all allowed values.
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IComboCellContentProvider extends ICellContentProvider {

	/**
	 * Whether or not the specified element requires a <code>Combo</code> widget
	 * to let the user select from a list of available values.
	 * 
	 * @param element
	 *            The element to test.
	 * @return True if the element has multiple allowed values that could
	 *         displayed in a <code>Combo</code> widget, false otherwise.
	 * @see #getAllowedValues(Object)
	 */
	public boolean requiresCombo(Object element);

	/**
	 * Gets the allowed values for the specified element.
	 * 
	 * @param element
	 *            The element whose allowed values are being requested.
	 * @return A list containing the allowed values for the element, or an empty
	 *         list if the element is invalid or does not restrict its allowed
	 *         values.
	 * @see #requiresCombo(Object)
	 */
	public List<String> getAllowedValues(Object element);

}
