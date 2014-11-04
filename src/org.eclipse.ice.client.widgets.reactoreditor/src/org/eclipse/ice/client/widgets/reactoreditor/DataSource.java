/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.reactoreditor;

/**
 * An enum representing the possible data sources supported by the
 * ReactorEditor. This is to centralize it so that no hardcoded Strings for the
 * data sources are necessary.<br>
 * <br>
 * To get the String for a particular value, use {@link DataSource#toString()}.<br>
 * To get the value for a particular String, use {@link DataSource#valueOf(String)}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public enum DataSource {
	
	/**
	 * Input data.
	 */
	Input,
	/**
	 * Reference data.
	 */
	Reference,
	/**
	 * Comparison data. Currently only available for LWReactor data mining
	 * strategies.
	 */
	Comparison;
}
