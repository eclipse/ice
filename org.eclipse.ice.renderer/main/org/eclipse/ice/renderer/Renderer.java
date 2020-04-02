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

/**
 * @author Jay Jay Billings
 *
 */
public class Renderer<T, V extends Serializable> {

	@Inject
	private T viewer;
	
	@Inject
	private DataElement<V> dataElement;
	
	private BiConsumer<T, DataElement<V>> drawMethod;

	public void setViewer(T view) {
		viewer = view;
	}

	public void setDataElement(DataElement<V> data) {
		dataElement = data;
	}

	public void setDrawMethod(BiConsumer<T,DataElement<V>> drawFunction) {
		drawMethod = drawFunction;
	}

	public void render() {
		drawMethod.accept(viewer,dataElement);
	}
}
