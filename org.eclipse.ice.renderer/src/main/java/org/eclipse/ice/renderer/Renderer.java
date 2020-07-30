/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.renderer;

import java.io.Serializable;
import java.util.function.BiConsumer;
import javax.inject.Inject;

import org.eclipse.ice.data.IDataElement;

/**
 * This class is a basic controller that connects data and UI classes in a
 * simple, pre-determined way. Data can be provided through the DataElement<V>
 * class and functionality for working with UI elements of type T can be
 * provided as call back functions (bi-consumers).
 * 
 * This class relies on dependency injection to locate and instantiate
 * dependencies of types T and V. Both can be overwritten using the associated
 * setters. However, call back functions must be injected with the
 * setDrawMethod() function.
 * 
 * No special lifecycle management is provided by this class. Instead, the
 * expectation is that the viewer T can handle updates directly.
 * 
 * Also how does that class relate to this
 * one and the VaadinRenderer?
 * 
 * @author Jay Jay Billings
 *
 */
public class Renderer<T, V extends IDataElement> implements Serializable {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = -8514680453430699108L;

	private T viewer;
	private V dataElement;

	private BiConsumer<T, V> drawMethod;

	@Inject
	public void setViewer(T view) {
		viewer = view;
	}
	
	@Inject
	public void setDataElement(V data) {
		dataElement = data;
	}

	public void setDrawMethod(BiConsumer<T, V> drawFunction) {
		drawMethod = drawFunction;
	}

	public void render() {
		drawMethod.accept(viewer, dataElement);
	}
}
