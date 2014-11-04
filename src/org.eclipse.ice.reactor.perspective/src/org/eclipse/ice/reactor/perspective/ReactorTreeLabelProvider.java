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
package org.eclipse.ice.reactor.perspective;

import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.resource.ICEResource;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * This class provides the labels for the {@link ReactorViewer}'s TreeView. It
 * uses the visitor pattern for reactor components to generate the labels and
 * displays their names and IDs. For other objects, it returns uses the object's
 * toString() method.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ReactorTreeLabelProvider extends LabelProvider {

	// TODO When we have a visitor for the reactor components, we might need to
	// revisit this interface.
	
	/**
	 * Creates a String that represents an object in the tree constructed by
	 * {@link ReactorTreeContentProvider}.
	 */
	@Override
	public String getText(Object element) {

		// Get a String from the ICEResource if possible.
		String text = null;
		if (element instanceof ICEResource) {
			text = ((ICEResource) element).getName();
		}
		// Get a String for Identifiables.
		else if (element instanceof Identifiable) {
			text = getLabel((Identifiable) element);
		}
		// If the element isn't an ICEResource, convert it to a String.
		else {
			text = element.toString();
		}

		return text;
	}

	/**
	 * Gets a standard label for an {@link Identifiable} object. Most--if not
	 * all--reactor components should implement the interface, usually by
	 * extending ICEObject. If we want to change the default label, we should
	 * do that here.
	 * 
	 * @param identifiable
	 *            The identifiable object that needs a default label.
	 * @return A string containing the identifiable's name and ID, e.g.
	 *         "Name 1".
	 */
	private String getLabel(Identifiable identifiable) {
		return identifiable.getName() + " " + identifiable.getId();
	}
}
