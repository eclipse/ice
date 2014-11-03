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

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ice.iclient.uiwidgets.IExtraInfoWidget;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import java.util.ArrayList;
import org.eclipse.ice.iclient.uiwidgets.IWidgetClosedListener;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class implements the IExtraInfoWidget interface for Eclipse. It creates
 * an ExtraInfoDialog in sync with the Eclipse workbench and it delegates all
 * listener management to the dialog.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EclipseExtraInfoWidget implements IExtraInfoWidget {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Form containing the extra information request.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Form iceForm;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The JFace dialog to use for rendering the DataComponent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ExtraInfoDialog infoDialog;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The IWidgetClosedListeners who have subscribed to this widget for
	 * updates. This list is maintained because the ExtraInfoDialog is created
	 * on a separate thread and the listeners can not be automatically forwarded
	 * to it.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IWidgetClosedListener> listeners;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public EclipseExtraInfoWidget() {
		// begin-user-code

		// Setup the listeners array
		listeners = new ArrayList<IWidgetClosedListener>();

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#display()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void display() {
		// begin-user-code

		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
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

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#setForm(Form form)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setForm(Form form) {
		// begin-user-code

		// Set the Form
		iceForm = form;

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#getForm()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Form getForm() {
		// begin-user-code
		return iceForm;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#setCloseListener(IWidgetClosedListener listener)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setCloseListener(IWidgetClosedListener listener) {
		// begin-user-code

		// Add the listener if it is not null
		if (listener != null) {
			listeners.add(listener);
		}
		return;

		// end-user-code
	}
}