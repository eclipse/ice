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
package org.eclipse.eavp.viz;

/**
 * Classes that implement this interface are expected to include a
 * {@link DeletePlotAction}. This interface provides the method signature for
 * removing list items.
 * 
 * @author Taylor Patterson
 *
 */
public interface IDeletePlotActionViewPart {

	/**
	 * Remove the current selection from the list this view displays.
	 */
	public abstract void removeSelection();

}
