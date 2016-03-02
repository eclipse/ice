/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.modeling;

/**
 * A wrapper class which contains some object which serves as the graphical
 * representation of a modeling part in the native data type of whatever
 * rendering engine is being used. Representations are provided by IViews, which
 * are responsible for constructing them based on the data in the corresponding
 * IMesh.
 * 
 * @author Robert Smith
 *
 * @param <T>
 *            The class of the object contained in the representation.
 */
public class Representation<T> {

	/**
	 * The rendering engine specific object which this representation is
	 * wrapping.
	 */
	T data;

	/**
	 * The default constructor.
	 * 
	 * @param data
	 *            The rendering object which this representation will contain.
	 */
	public Representation(T data) {
		this.data = data;
	}

	/**
	 * Getter method for the wrapped data.
	 * 
	 * @return A renderable object of a type usable by the graphics engine
	 *         associated with the IView that created this Representation.
	 */
	public T getData() {
		return data;
	}
}
