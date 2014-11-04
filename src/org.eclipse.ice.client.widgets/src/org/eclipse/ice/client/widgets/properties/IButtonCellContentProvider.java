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
package org.eclipse.ice.client.widgets.properties;


/**
 * This interface provides additional methods for accessing an
 * {@link ICellContentProvider} in the context of a cell represented by a
 * <code>Button</code> widget.
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IButtonCellContentProvider extends ICellContentProvider {

	/**
	 * Gets whether or not the specified element's cell <code>Button</code>
	 * should be selected.
	 * <p>
	 * For <code>Button</code>s with the style <code>SWT.CHECK</code>, selected
	 * corresponds to being checked.
	 * </p>
	 * 
	 * @param element
	 *            The element to test.
	 * @return True if the element's cell should be selected, false otherwise.
	 */
	public boolean isSelected(Object element);

}
