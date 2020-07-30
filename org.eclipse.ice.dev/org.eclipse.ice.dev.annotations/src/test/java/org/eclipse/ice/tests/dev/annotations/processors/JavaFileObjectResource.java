/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.tests.dev.annotations.processors;

import javax.tools.JavaFileObject;

import com.google.testing.compile.JavaFileObjects;

/**
 * Interface for Loaders of test input and pattern files.
 * @author Daniel Bluhm
 */
public interface JavaFileObjectResource {

	/**
	 * The path to this resource's JavaFileObject.
	 * @return String representation of path to this resource's JavaFileObject
	 */
	public String getPath();

	/**
	 * Load the JavaFileObject for this resource.
	 * @return JavaFileObject for this resource
	 */
	public default JavaFileObject get() {
		return JavaFileObjects.forResource(getPath());
	}
}
