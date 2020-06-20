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
package gov.ornl.rse.renderer.client.test;

import java.io.Serializable;
import org.eclipse.ice.renderer.DataElement;

public interface IRendererClient<T extends Serializable> {

	/**
	 * This operation sets the data that should be rendered.
	 * 
	 * @param otherData The data element that should be rendered. This function
	 *                  overwrites the existing data on the client and server.
	 */
	public void setData(DataElement<T> otherData);

	/**
	 * This function returns the present version of the DataElement
	 * 
	 * @return the data element. Note that it is as up to date as possible, but
	 *         there is a chance that the most recent updates from the client have
	 *         not been committed due to latency.
	 */
	public DataElement<T> getData();

}