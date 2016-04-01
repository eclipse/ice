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

import org.eclipse.ice.client.widgets.AbstractEntryComposite;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Jay Jay Billings
 *
 */
public class DemoEntryComposite extends AbstractEntryComposite {

	public DemoEntryComposite(Composite parent, IEntry refEntry, int style) {
		super(parent, refEntry, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.AbstractEntryComposite#render()
	 */
	@Override
	public void render() {

		Button button = new Button(this, SWT.PUSH);
		button.setText("My button");

		Label label = new Label(this, SWT.FLAT);
		label.setText(super.getEntry().getValue() + " in new Entry widget.");
		setLayout(new FillLayout());
		this.layout();

		return;
	}

}
