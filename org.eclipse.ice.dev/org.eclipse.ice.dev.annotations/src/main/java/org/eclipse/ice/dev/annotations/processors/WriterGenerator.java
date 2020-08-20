/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Walsh - Initial implementation
 *******************************************************************************/
package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.util.List;

import javax.lang.model.element.Element;

/**
 * Interface implemented with the purpose of generating specific files given the
 * annotation data extracted
 *
 */
public interface WriterGenerator {
	/**
	 * Generates a list of VelocitySourceWriters based on data extracted from a Spec class
	 * @param element
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public List<VelocitySourceWriter> generateWriters(Element element,AnnotationExtractionResponse response) throws IOException;
}
