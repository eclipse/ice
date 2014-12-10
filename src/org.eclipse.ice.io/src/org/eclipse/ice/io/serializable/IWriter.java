/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.io.serializable;

import java.net.URI;

import org.eclipse.ice.datastructures.ICEObject.ICEObject;

/**
 * 
 * @author Alex McCaskey
 *
 */
public interface IWriter {

	/**
	 * 
	 * @param objectToWrite
	 * @param uri
	 */
	public void write(ICEObject objectToWrite, URI uri);
	
	/**
	 * 
	 * @param regex
	 * @param value
	 */
	public void replace(String regex, ICEObject value);
	
	/**
	 * 
	 * @return
	 */
	public String getWriterType();
	
}
