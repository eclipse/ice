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
 */
public enum PersistenceHandlerTemplateProperty implements TemplateProperty {
	ELEMENT_INTERFACE, COLLECTION, CLASS, IMPLEMENTATION, QUALIFIED, INTERFACE;
}
