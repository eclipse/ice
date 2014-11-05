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
package org.eclipse.ice.client.widgets.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * This class provides a {@link CellEditor} that uses a {@link Button}. The
 * style of the <code>Button</code> can be set on initialization. For instance,
 * to get a checkbox <code>Button</code>, use
 * {@code new ButtonCellEditor(parent, SWT.CHECK);}
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ButtonCellEditor extends CellEditor {

	/**
	 * The underlying <code>Button</code> widget.
	 */
	private Button button;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            The parent <code>Control</code>.
	 * @param style
	 *            The style bit to use for the <code>CellEditor</code>'s button,
	 *            e.g., <code>SWT.PUSH</code> or </code>SWT.CHECK</code>.
	 */
	public ButtonCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, getStyle());
		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#dispose()
	 */
	@Override
	public void dispose() {
		// The super method disposes of the button (rather, it disposes the
		// return value from createControl(Composite)).
		super.dispose();
		button = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		return button.getSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	@Override
	protected void doSetFocus() {
		button.setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(Object value) {
		button.setSelection((Boolean) value);
	}

}
