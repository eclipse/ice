/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   Jordan Deyton - relocated to viz.service.widgets
 *******************************************************************************/
package org.eclipse.eavp.viz.service.widgets;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * This class provides a JFace-style {@link ControlContribution} for an SWT
 * {@link Label}. As such, an instance of a {@code LabelContribution} can be
 * added to JFace {@code ToolBarManager}s, or really any JFace
 * {@code IContributionManager}.
 * 
 * @author Jordan Deyton
 *
 */
public class LabelContribution extends ControlContribution {

	/**
	 * The underlying SWT {@code Label} widget.
	 */
	private Label label;

	/**
	 * The style to use for the {@link #label}.
	 */
	private final int style;

	/**
	 * The [initial] text of the label.
	 */
	private String text;

	/**
	 * The default constructor. The {@link #label} will be created with the
	 * style {@code NONE}.
	 * 
	 * @param id
	 *            The ID of the contribution item.
	 */
	public LabelContribution(String id) {
		this(id, SWT.NONE);
	}

	/**
	 * The full constructor.
	 * 
	 * @param id
	 *            The ID of the contribution item.
	 * @param style
	 *            The style to use for the {@link #label}.
	 */
	public LabelContribution(String id, int style) {
		super(id);

		this.style = style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.action.ControlContribution#createControl(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		// We have to wrap the Label in a Composite with a GridLayout so that it
		// will be centered vertically (something which Windows does not support
		// out of the box).
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(parent.getBackground());
		composite.setLayout(new GridLayout());

		Label label = new Label(composite, style);
		label.setBackground(parent.getBackground());
		label.setFont(parent.getFont());
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

		if (text != null) {
			label.setText(text);
		}

		return composite;
	}

	/**
	 * Gets the underlying SWT {@code Label} widget.
	 * 
	 * @return The {@code Label}, or {@code null} if it has not yet been
	 *         created.
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * Sets the [initial] text for the label. This will be set either at the
	 * next available opportunity or when the widget is created.
	 * 
	 * @param text
	 *            The label text.
	 */
	public void setText(final String text) {
		if (label != null) {
			label.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					label.setText(text);
					label.getParent().layout();
				}
			});
		} else {
			this.text = text;
		}
		return;
	}

}
