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
package org.eclipse.ice.viz.service.geometry.test;

import org.eclipse.ice.viz.service.geometry.ComplexShape;
import org.eclipse.ice.viz.service.geometry.IShapeVisitor;
import org.eclipse.ice.viz.service.geometry.PrimitiveShape;

/**
 * <p>
 * Tool for testing whether an IShapeVisitor is visited by an IShape
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class TestShapeVisitor implements IShapeVisitor {
	/**
	 * <p>
	 * The number of visits from an element to an instance of this class
	 * </p>
	 * 
	 */
	private int visits;

	/**
	 * <p>
	 * Initializes the number of visits to 0
	 * </p>
	 * 
	 */
	public TestShapeVisitor() {

		// Reset the visit count
		resetVisits();

	}

	/**
	 * <p>
	 * Returns the number of visits to an instance of this class
	 * </p>
	 * 
	 * @return <p>
	 *         The number of visits from an element to an instance of this class
	 *         </p>
	 */
	public int getVisits() {
		return visits;
	}

	/**
	 * <p>
	 * Resets the number of visits to 0
	 * </p>
	 * 
	 */
	public void resetVisits() {
		visits = 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShapeVisitor#visit(ComplexShape complexShape)
	 */
	@Override
	public void visit(ComplexShape complexShape) {
		visits++;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShapeVisitor#visit(PrimitiveShape primitiveShape)
	 */
	@Override
	public void visit(PrimitiveShape primitiveShape) {
		visits++;
	}
}