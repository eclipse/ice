///*******************************************************************************
// * Copyright (c) 2015 UT-Battelle, LLC.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
// *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
// *   Claire Saunders, Matthew Wang, Anna Wojtowicz
// *******************************************************************************/
//package org.eclipse.ice.datastructures.form.geometry;
//
//import java.util.ArrayList;
//
//import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
//import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
//
///**
// * An interface for objects which hold and manage a collection of geometrical
// * shapes.
// * 
// * @author Robert Smith
// *
// */
//public interface IGeometry extends IUpdateable, IUpdateableListener {
//
//	/**
//	 * Adds a shape to the list of managed shapes.
//	 * 
//	 * @param shape
//	 *            The shape to be added
//	 */
//	public void addShape(IShape shape);
//
//	/**
//	 * Accessor method for the list of managed shapes.
//	 * 
//	 * @return The lsit of managed shapes.
//	 */
//	public ArrayList<IShape> getShapes();
//
//	/**
//	 * Removes a shape from the list of managed shapes.
//	 * 
//	 * @param shape
//	 *            That shape to be removed.
//	 */
//	public void removeShape(IShape shape);
//
//	/**
//	 * Mutator method for the list of managed shapes.
//	 * 
//	 * @param shapes
//	 *            The new list of shapes for the IGeometry to hold.
//	 */
//	public void setShapes(ArrayList<IShape> shapes);
//
//	/**
//	 * Turn this IGeometry into a copy of the given IGeomety, by performing a
//	 * deep copy.
//	 * 
//	 * @param iceObject
//	 *            The source object that this IGeometry will become a copy of.
//	 */
//	public void copy(IGeometry iceObject);
//}
