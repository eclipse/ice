/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.eavp.viz.service.geometry.widgets;

/**
 * <p>
 * Enables the implementer to be notified of changes to a RealSpinner by calling
 * its listen() operation
 * </p>
 * 
 * @author Andrew P. Belt
 */
public interface RealSpinnerListener {
    /**
     * <p>
     * The function to call when RealSpinner is updated
     * </p>
     * 
     * @param realSpinner
     */
    public void update(RealSpinner realSpinner);
}