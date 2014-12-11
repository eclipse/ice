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
import java.util.ArrayList;

import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.form.Entry;

/**
 * 
 * @author Alex McCaskey
 *
 */
public interface IReader {

	/**
	 * 
	 * @param uri
	 * @return
	 */
	public ICEObject read(URI uri);
	
	/**
	 * 
	 * @param regexp
	 * @return
	 */
	public ArrayList<Entry> findAll(URI uri, String regex);
	
	/**
	 * 
	 * @return
	 */
	public String getReaderType();
	
}
