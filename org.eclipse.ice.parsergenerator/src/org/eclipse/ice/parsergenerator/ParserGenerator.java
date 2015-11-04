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
package org.eclipse.ice.parsergenerator;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.Item;

/**
 * Description
 * 
 * @author Andrew Bennett
 */
public class ParserGenerator extends Item {

	/**
	 * Constructor
	 */
	public ParserGenerator() {
		return;
	}
	
	
	/**
	 * Constructor
	 */
	public ParserGenerator(IProject projectSpace) {
		return;
	}
	
	
	/**
	 * <p>
	 * This operation overrides the Item.setupForm() operation.
	 * </p>
	 */
	@Override
	public void setupForm() {
		// Create a fresh form to start with
		form = new Form();

		// If loading from the new item button we should just
		// load up the default case 6 file by passing in null
		if (project != null) {
			loadInput(null);
		}
	}
	
	/**
	 * <p>
	 * This operation overrides the Item.setupItemInfo() operation.
	 * </p>
	 */
	@Override
	protected void setupItemInfo() {
		return;
	}

	/**
	 * <p>
	 * Overrides the reviewEntries operation. This will still call
	 * super.reviewEntries, but will handle the dependencies after all other dep
	 * handing is finished.
	 * </p>
	 * 
	 * @return the status of the form
	 */
	@Override
	protected FormStatus reviewEntries(Form preparedForm) {
		FormStatus retStatus = FormStatus.ReadyToProcess;
		return retStatus;
	}

	/**
	 * <p>
	 * Overrides item's process by adding a customTaggedExportString (ini).
	 * Still utilizes Item's process functionality for all other calls.
	 * </p>
	 */
	@Override
	public FormStatus process(String actionName) {
		FormStatus retStatus = FormStatus.Processed;

		return retStatus;
	}

	/**
	 * <p>
	 * This operation loads the given example into the Form.
	 * </p>
	 * 
	 * @param name
	 *            The path name of the example file name to load.
	 */
	@Override
	public void loadInput(String name) {
		return;
	}
	
}

