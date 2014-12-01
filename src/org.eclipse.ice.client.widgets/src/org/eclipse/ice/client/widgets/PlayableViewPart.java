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
package org.eclipse.ice.client.widgets;

import org.eclipse.ui.part.ViewPart;

/**
 * This abstract class is extended by views that use the playable actions
 * (NextAction, PlayAction, PreviousAction) to cycle through a list of
 * resources.
 * 
 * @author Taylor Patterson
 */
public abstract class PlayableViewPart extends ViewPart {

	/**
	 * Field to identify if the view is in a state in which the play button
	 * should be usable.
	 */
	protected boolean playable;

	/**
	 * Method to check if the view is in a state where play button functionality
	 * would make sense.
	 * 
	 * @return The playability of the view state
	 */
	public boolean isPlayable() {
		return playable;
	}

	/**
	 * Set a selection to the next item in the list that the playable buttons
	 * cycle through. If current selection is the last item in the list, cycle
	 * to the front of the list.
	 */
	public abstract void setToNextResource();

	/**
	 * Set a selection to the previous item in the list that the playable
	 * buttons cycle through. If current selection is the first item in the
	 * list, cycle to the front of the list.
	 */
	public abstract void setToPreviousResource();

}
