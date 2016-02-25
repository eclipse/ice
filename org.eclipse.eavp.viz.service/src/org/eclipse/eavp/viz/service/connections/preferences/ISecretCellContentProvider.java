/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections.preferences;


/**
 * An {@code ISecretCellContentProvider} is simply a basic
 * {@link ICellContentProvider} with one additional property: Should the content
 * in the cell be obscured (secret)?
 * 
 * @author Jordan Deyton
 *
 */
public interface ISecretCellContentProvider extends IVizCellContentProvider {

	/**
	 * The default character to use when obscuring text.
	 */
	public static final char SECRET_CHAR = '*';
	/**
	 * The default "echo" character to use when not-obscuring text. This is
	 * based on SWT.
	 */
	public static final char PUBLIC_CHAR = '\0';

	/**
	 * Gets the character that should be used to obscure secret text. The
	 * default value to return should be {@link #SECRET_CHAR}. This method
	 * simply provides a way for sub-classes to provide their own secret
	 * character.
	 * 
	 * @return The "echo" character used for obscured or secret text.
	 */
	public char getSecretChar();

	/**
	 * Gets whether or not the specified element's cell text should be obscured.
	 * 
	 * @param element
	 *            The element to test.
	 * @return True if the element's cell text should be obscured, false
	 *         otherwise.
	 */
	public boolean isSecret(Object element);
}
