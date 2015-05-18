/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.analysistool;

/**
 * <p>
 * This is an enumeration of all the types of assets that can be created.
 * </p>
 * 
 * @author els
 */
public enum AnalysisAssetType {
	/**
	 * <p>
	 * This asset is a picture.
	 * </p>
	 * 
	 */
	PICTURE,
	/**
	 * <p>
	 * This asset is a table of data organized into rows and columns.
	 * </p>
	 * 
	 */
	TABLE,
	/**
	 * <p>
	 * This asset is a single number.
	 * </p>
	 * 
	 */
	VALUE
}