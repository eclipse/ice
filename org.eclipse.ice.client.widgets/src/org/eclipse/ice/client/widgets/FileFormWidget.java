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
 * 
 * @author Alex McCaskey
 *
 */
public class FileFormWidget implements IFormWidget {

	/**
	 * 
	 */
	private Form form;

	/**
	 * 
	 */
	private ICEFormEditor editor;

	/**
	 * 
	 */
	public FileFormWidget() {

	}

	@Override
	public void setForm(Form form) {
		this.form = form;

	}

	@Override
	public Form getForm() {
		return form;
	}

	@Override
	public void updateStatus(String statusMessage) {
//		// Sync with the display
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

	@Override
	public void registerUpdateListener(IUpdateEventListener listener) {
	}

	@Override
	public void registerProcessListener(IProcessEventListener listener) {
	}

	@Override
	public void registerResourceProvider(ISimpleResourceProvider provider) {
	}

	@Override
	public void notifyUpdateListeners() {
	}

	@Override
	public void notifyProcessListeners(String process) {
	}

	@Override
	public void notifyCancelListeners(String process) {
	}

	@Override
	public void display() {
	}

	@Override
	public void disable(boolean state) {
	}

}
