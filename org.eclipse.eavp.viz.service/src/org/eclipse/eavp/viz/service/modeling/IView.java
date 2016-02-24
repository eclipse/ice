/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.modeling;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateableListener;

/**
 * The view of an IMesh shown to the user. The view is responsible for creating,
 * managing, and updating the datastructure(s) which display the associated
 * IMesh in the view's rendering engine's native data types.
 * 
 * @author Robert Smith
 */
public interface IView extends IManagedUpdateable, IManagedUpdateableListener {

	/**
	 * Getter function for the part's transformation.
	 * 
	 * @return The part's current transformation.
	 */
	Transformation getTransformation();

	/**
	 * Setter function for the part's transformation.
	 * 
	 * @param newTransformation
	 *            The transformation to apply to this part.
	 */
	void setTransformation(Transformation newTransformation);

	/**
	 * Getter for the part's previous transformation.
	 * 
	 * @return The last transformation which was fully applied to the part by
	 *         the graphics engine.
	 */
	Transformation getPreviousTransformation();

	/**
	 * Notifies the part that the rendering engine has graphically applied the
	 * newest transformation to it.
	 */
	void setSynched();

	/**
	 * Creates an object which represents the part's model in a native data type
	 * for the application associated with this view.
	 * 
	 */
	Object getRepresentation();

	/**
	 * Refreshes the representation of the model.
	 * 
	 * @param model
	 *            A reference to the view's model
	 */
	void refresh(IMesh model);

	void copy(IView otherObject);

}