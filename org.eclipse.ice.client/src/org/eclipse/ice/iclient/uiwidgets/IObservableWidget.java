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
package org.eclipse.ice.iclient.uiwidgets;

/** 
 * <p>The IObservableWidget interface describes the operations that must be realized by Widgets that are observable vian ICE event listeners (i.e. - IProcessEventListener, IUpdateEventListener, etc.).</p>
 * @author Jay Jay Billings
 */
public interface IObservableWidget {
	/** 
	 * <p>This operation registers an IUpdateEventListener with the FormWidget.</p>
	 * @param listener <p>The listener.</p>
	 */
	public void registerUpdateListener(IUpdateEventListener listener);

	/** 
	 * <p>This operation registers an IProcessEventListener with the FormWidget.</p>
	 * @param listener <p>The listener.</p>
	 */
	public void registerProcessListener(IProcessEventListener listener);

	/** 
	 * <p>This operation registers an ISimpleResourceProvider with the observable widget so that it can notify the provider when a resource should be loaded.</p>
	 * @param provider <p>The ICEResource provider.</p>
	 */
	public void registerResourceProvider(ISimpleResourceProvider provider);

	/** 
	 * <p>This protected operation notifies the IUpdateEventListeners of a change.</p>
	 */
	public void notifyUpdateListeners();

	/** 
	 * <p>This protected operation notifies the IProcessEventListeners of a change.</p>
	 * @param process <p>The process that should be performed for the Form.</p>
	 */
	public void notifyProcessListeners(String process);

	/** 
	 * <p>This  operation notifies the IProcessEventListeners of a cancellation request..</p>
	 * @param process <p>The process that should be performed for the Form.</p>
	 */
	public void notifyCancelListeners(String process);
}