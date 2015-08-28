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
package org.eclipse.ice.viz.service.geometry.shapes;

/**
 * <p>
 * Implementing this interface allows a class to discover the type of an IShape
 * through the visitor design pattern
 * </p>
 * 
 * @author Jay Jay Billings
 */
public interface IShapeVisitor {
	/**
	 * <p>
	 * Visits an IShapeVisitor as a ComplexShape
	 * </p>
	 * 
	 * @param complexShape
	 */
	public void visit(ComplexShape complexShape);

	/**
	 * <p>
	 * Visits an IShapeVisitor as a PrimitiveShape
	 * </p>
	 * 
	 * @param primitiveShape
	 */
	public void visit(PrimitiveShape primitiveShape);
}