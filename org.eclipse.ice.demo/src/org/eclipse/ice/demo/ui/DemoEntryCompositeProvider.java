/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.demo.ui;

import org.eclipse.ice.client.widgets.IEntryComposite;
import org.eclipse.ice.client.widgets.providers.IEntryCompositeProvider;
import org.eclipse.january.form.IEntry;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Jay Jay Billings
 *
 */
public class DemoEntryCompositeProvider implements IEntryCompositeProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.providers.IEntryCompositeProvider#getName(
	 * )
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "demo-entry";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.providers.IEntryCompositeProvider#
	 * getEntryComposite(org.eclipse.swt.widgets.Composite,
	 * org.eclipse.ice.datastructures.entry.IEntry, int,
	 * org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	public IEntryComposite getEntryComposite(Composite parent, IEntry entry,
			int style, FormToolkit toolKit) {
		// TODO Auto-generated method stub
		return new DemoEntryComposite(parent, entry, style);
	}

}
