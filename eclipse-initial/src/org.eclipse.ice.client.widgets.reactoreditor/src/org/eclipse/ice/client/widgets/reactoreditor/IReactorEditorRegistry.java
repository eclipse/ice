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
 * An IReactorEditorRegistry maintains a group of {@link ReactorFormEditor}s.
 * Implementations may expand the behavior to include additional features like
 * setting the {@link ReactorFormInput} for one of the editors.
 * 
 * @author Jordan
 * 
 */
public interface IReactorEditorRegistry {

	/**
	 * Adds a {@link ReactorFormEditor} to the registry with the specified ID.
	 * The ID is the same as the associated Item or Form.
	 * 
	 * @param editor
	 *            The ReactorFormEditor that has been created.
	 * @param id
	 *            The ID of the editor. This should be the same as its
	 *            associated Item or Form.
	 */
	public void addReactorEditor(ReactorFormEditor editor, int id);

	/**
	 * Removes a {@link ReactorFormEditor} from the registry.
	 * 
	 * @param id
	 *            The ID of the editor. This should be the same as its
	 *            associated Item or Form.
	 */
	public void removeReactorEditor(int id);

}
