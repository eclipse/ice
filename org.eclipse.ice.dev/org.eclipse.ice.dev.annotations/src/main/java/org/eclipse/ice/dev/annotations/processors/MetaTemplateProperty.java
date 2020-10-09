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

/**
 * Enum of keys mapped to extracted/generated metadata essential to interface
 * and class generation
 *
 * @author Michael Walsh
 */
public enum MetaTemplateProperty implements TemplateProperty {
	/**
	 * String of package name
	 */
	PACKAGE,
	/**
	 * List of fields
	 */
	FIELDS,
	/**
	 * String of interface name
	 */
	INTERFACE,
	/**
	 * String of class name
	 */
	CLASS,
	/**
	 * String of fully qualified class name
	 */
	QUALIFIEDIMPL,
	/**
	 * String of fully qualified interface name
	 */
	QUALIFIED,
	TYPESCRIPT;
}
