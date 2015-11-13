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

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.iclient.uiwidgets.IProcessEventListener;
import org.eclipse.ice.iclient.uiwidgets.ISimpleResourceProvider;
import org.eclipse.ice.iclient.uiwidgets.IUpdateEventListener;

/**
 * The FileFormWidget is a realization of the IFormWidget to 
 * be used when the Item being displayed is from an IFile. It 
 * wraps the Form and updates status'
 * 
 * @author Alex McCaskey
 *
 */
public class FileFormWidget implements IFormWidget {

	/**
	 * Reference to the Form for the displayed Item.
	 */
	private Form form;

	/**
	 * Reference to the ICEFormEditor for this Form 
	 * and Item
	 */
	private ICEFormEditor editor;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.iclient.uiwidgets.IFormWidget#setForm(org.eclipse.ice.datastructures.form.Form)
	 */
	@Override
	public void setForm(Form form) {
		this.form = form;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.iclient.uiwidgets.IFormWidget#getForm()
	 */
	@Override
	public Form getForm() {
		return form;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.iclient.uiwidgets.IFormWidget#updateStatus(java.lang.String)
	 */
	@Override
	public void updateStatus(String statusMessage) {
//		// Sync with the display
		// FIXME THIS DOES NOT WORK...
//		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				if (editor == null) {
//
//					
//					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//
//					for (IEditorReference part : page.getEditorReferences()) {
//						System.out.println(
//								"HELLO: " + part.getName() + ", " + form.getName().replaceAll("\\s+", "_") + ".xml");
//						String fileName = form.getName().replaceAll("\\s+", "_") + ".xml";
//						if (fileName.equals(part.getName())) {
//							editor = (ICEFormEditor) part.getEditor(false);
//							System.out.println("EDITOR NULL: " + (editor == null));
//							break;
//						}
//					}
//
//					if (editor != null) {
//						System.out.println("Updating the Item Status");
//						editor.updateStatus(statusMessage);
//					} else {
//						System.out.println("Not Updating the Item Status");
//					}
//				}
//			}
//		});

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.iclient.uiwidgets.IObservableWidget#registerUpdateListener(org.eclipse.ice.iclient.uiwidgets.IUpdateEventListener)
	 */
	@Override
	public void registerUpdateListener(IUpdateEventListener listener) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.iclient.uiwidgets.IObservableWidget#registerProcessListener(org.eclipse.ice.iclient.uiwidgets.IProcessEventListener)
	 */
	@Override
	public void registerProcessListener(IProcessEventListener listener) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.iclient.uiwidgets.IObservableWidget#registerResourceProvider(org.eclipse.ice.iclient.uiwidgets.ISimpleResourceProvider)
	 */
	@Override
	public void registerResourceProvider(ISimpleResourceProvider provider) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.iclient.uiwidgets.IObservableWidget#notifyUpdateListeners()
	 */
	@Override
	public void notifyUpdateListeners() {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.iclient.uiwidgets.IObservableWidget#notifyProcessListeners(java.lang.String)
	 */
	@Override
	public void notifyProcessListeners(String process) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.iclient.uiwidgets.IObservableWidget#notifyCancelListeners(java.lang.String)
	 */
	@Override
	public void notifyCancelListeners(String process) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.iclient.uiwidgets.IFormWidget#display()
	 */
	@Override
	public void display() {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.iclient.uiwidgets.IFormWidget#disable(boolean)
	 */
	@Override
	public void disable(boolean state) {
		// Do nothing
	}

}
