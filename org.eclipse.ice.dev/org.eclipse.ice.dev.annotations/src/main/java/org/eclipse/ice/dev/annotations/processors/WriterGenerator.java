/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Walsh - Initial implementation
 *    Daniel Bluhm
 *******************************************************************************/
package org.eclipse.ice.dev.annotations.processors;

import java.util.List;

/**
 * Interface for classes that generate one or more file writers from a given
 * set of data.
 * 
 * @author Michael Walsh
 * @author Daniel Bluhm
 */
public interface WriterGenerator<T> {

	/**
	 * Generate one or more FileWriters from the passed data.
	 * @param data from which file writers will be generated.
	 * @return
	 */
	public List<GeneratedFileWriter> generate(T data);
}