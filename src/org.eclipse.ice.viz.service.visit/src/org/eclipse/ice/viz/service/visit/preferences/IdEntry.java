/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit.preferences;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;

public class IdEntry extends Entry {

	private final IdManager idManager;

	public IdEntry(IEntryContentProvider contentProvider, IdManager manager) {
		super(contentProvider);
		idManager = manager;
	}

	public IdEntry(IdManager manager) {
		idManager = manager;
	}

	@Override
	public boolean setValue(String value) {
		boolean availableId = idManager.idAvailable(value);
		if (availableId) {
			super.setValue(value);
		} else {
			this.errorMessage = "The value \"" + value
					+ "\" is invalid or not unique.";
		}
		return availableId;
	}

}
