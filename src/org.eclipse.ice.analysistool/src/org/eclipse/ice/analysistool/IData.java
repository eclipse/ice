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
package org.eclipse.ice.analysistool;

import java.util.ArrayList;

/** 
 * <p>An interface that provides the position and value of a data entry as well as a descriptive tag about the featureof the entry that the data represents.</p>
 * @author els
 */
public interface IData {
	/** 
	 * <p>The position of the data relative to the position of the containing object.</p>
	 * @return <p>The position.</p>
	 */
	public ArrayList<Double> getPosition();

	/** 
	 * <p>The value of the particular feature (pin-power, temperature, etc).</p>
	 * @return <p>The value of the IData object.</p>
	 */
	public double getValue();

	/** 
	 * <p>The amount of uncertainty in the value.</p>
	 * @return <p>The value of uncertainty.</p>
	 */
	public double getUncertainty();

	/** 
	 * <p>A string describing the units of the value and its uncertainty.</p>
	 * @return <p>The units.</p>
	 */
	public String getUnits();

	/** 
	 * <p>The name of the feature that this data represents (pin-power, temperature, etc).</p>
	 * @return <p>The name of the feature.</p>
	 */
	public String getFeature();
}