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

import org.eclipse.core.resources.IFile;

/**
 * The ITemplatedReader interface defines the functionality needed to read files
 * into a form that has been built using a template file.
 * 
 * @author Andrew Bennett
 *
 */
public interface ITemplatedReader extends IReader {

	/**
	 * Set the string that will be recognized as a comment when reading the
	 * file.
	 * 
	 * @param regex
	 *            The pattern that will be matched to a comment
	 */
	public void setCommentString(String regex);

	/**
	 * Set the string that will be recognized as indentation when reading the
	 * file
	 * 
	 * @param regex
	 *            The pattern that will be matched to indentation
	 */
	public void setIndentString(String regex);

	/**
	 * Set the string that will be recognized as the section delimiter when
	 * reading the file.
	 * 
	 * @param regex
	 *            The pattern that will be matched to a section header
	 */
	public void setSectionPattern(String regex);

	/**
	 * Set the string that will be recognized as an assignment operator for
	 * assigning variables values within the file.
	 * 
	 * @param regex
	 *            The pattern that will be matched to variable assignments
	 */
	public void setAssignmentPattern(String regex);

	/**
	 * Set the type of template that will be used to build the initial Form
	 * 
	 * @param template
	 *            A string matching the type of template to use
	 */
	public void setTemplateType(String template);

	/**
	 * Add new templates that can be used to build custom Forms
	 * 
	 * @param templateFile
	 *            The file representation of the template
	 * @param templateName
	 *            A name to grab the template via the setTemplateType method
	 */
	public void addTemplateType(String templateName, IFile templateFile);

}
