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
package org.eclipse.ice.client.widgets.reactoreditor.grid;

/**
 * This interface provides a listener for grid cells. It provides methods for
 * the selection and clicking of cells.
 * 
 * @author djg
 * 
 */
public interface IGridListener {

	/**
	 * The cell in a particular location has been selected.
	 * 
	 * @param index
	 *            The location of the selected cell.
	 */
	public void selectCell(int index);

	/**
	 * The cell in a particular location has been clicked.
	 * 
	 * @param index
	 *            The location of the clicked cell.
	 */
	public void clickCell(int index);
}
