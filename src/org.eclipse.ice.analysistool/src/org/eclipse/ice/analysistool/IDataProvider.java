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
 * <p>An interface for determining what features are available  for a particular object(pin-power, temperature, etc).  IDataProviders are anything that have information to share regardless of their positions in any particular hierarchy.</p>
 * @author Eric J. Lingerfelt
 */
public interface IDataProvider {
	/** 
	 * <p>This operation returns the list of features available across all time steps (pin-power, temperature, etc).</p>
	 * @return <p>The list of features.</p>
	 */
	public ArrayList<String> getFeatureList();

	/** 
	 * <p>This operation returns the total number of time steps.</p>
	 * @return <p>The number of time steps.</p>
	 */
	public int getNumberOfTimeSteps();

	/** 
	 * <p>This operation sets the current time step for which data should be retrieved.  It is 0-indexed such that time step 0 is the initial state and time step 1 is the state after the first time step.  This operation should be called to set the current time step before data is retrieved from the provider.  The provider will always default to the initial state.  </p>
	 * @param step <p>The time step to set.</p>
	 */
	public void setTime(double step);

	/** 
	 * <p>This operation returns all of the data (as IData[*]) related to a particular feature for this provider at a specific time step.</p><p>This operation will return null if no data is available and such a situation will most likely signify an error.</p>
	 * @param feature <p>The feature for the IData.</p>
	 * @return <p>The returned IData.</p>
	 */
	public ArrayList<IData> getDataAtCurrentTime(String feature);

	/** 
	 * <p>This operation is a description of the source of information for this provider and its data.</p>
	 * @return <p>The source information.</p>
	 */
	public String getSourceInfo();

	/** 
	 * <p>Returns the list of features at the current time.</p>
	 * @return <p>The returned list of features at the current time step.</p>
	 */
	public ArrayList<String> getFeaturesAtCurrentTime();

	/** 
	 * <p>Returns all the times in ascending order.</p>
	 * @return <p>An arraylist of times in order from least to greatest.  This operation does not allow the user to change the order of this list.</p>
	 */
	public ArrayList<Double> getTimes();

	/** 
	 * <p>Returns the integer time based upon the time step.  Returns -1 if the time does not exist.</p>
	 * @param time
	 * @return
	 */
	public int getTimeStep(double time);

	/** 
	 * <p>Returns the time units.</p>
	 * @return <p>The time unit.</p>
	 */
	public String getTimeUnits();
}