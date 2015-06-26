/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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

import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.iclient.uiwidgets.IExtraInfoWidget;
import org.eclipse.ice.iclient.uiwidgets.IWidgetClosedListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * <p>
 * This class implements the IExtraInfoWidget interface for Eclipse. It creates
 * an ExtraInfoDialog in sync with the Eclipse workbench and it delegates all
 * listener management to the dialog.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class EclipseExtraInfoWidget implements IExtraInfoWidget {
	/**
	 * <p>
	 * The Form containing the extra information request.
	 * </p>
	 * 
	 */
	private Form iceForm;

	/**
	 * <p>
	 * The JFace dialog to use for rendering the DataComponent.
	 * </p>
	 * 
	 */
	private ExtraInfoDialog infoDialog;

	/**
	 * <p>
	 * The IWidgetClosedListeners who have subscribed to this widget for
	 * updates. This list is maintained because the ExtraInfoDialog is created
	 * on a separate thread and the listeners can not be automatically forwarded
	 * to it.
	 * </p>
	 * 
	 */
	private ArrayList<IWidgetClosedListener> listeners;

	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 */
	public EclipseExtraInfoWidget() {

		// Setup the listeners array
		listeners = new ArrayList<IWidgetClosedListener>();

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#display()
	 */
	@Override
	public void display() {

		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {

				// Local declarations
				Display display;
				Shell shell;

				// Get the display and shell
				display = PlatformUI.getWorkbench().getDisplay();
				shell = display.getActiveShell();

				// Instantiate the dialog, set the DataComponent and set the
				// listeners
				infoDialog = new ExtraInfoDialog(shell);
				infoDialog.setDataComponent((DataComponent) iceForm
						.getComponents().get(0));
				for (IWidgetClosedListener i : listeners) {
					infoDialog.setCloseListener(i);
				}
				// Open the dialog
				infoDialog.open();
			}
		});

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#setForm(Form form)
	 */
	@Override
	public void setForm(Form form) {

		// Set the Form
		iceForm = form;

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#getForm()
	 */
	@Override
	public Form getForm() {
		return iceForm;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#setCloseListener(IWidgetClosedListener listener)
	 */
	@Override
	public void setCloseListener(IWidgetClosedListener listener) {

		// Add the listener if it is not null
		if (listener != null) {
			listeners.add(listener);
		}
		return;

	}
}