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
package org.eclipse.ice.client.widgets;

import java.util.regex.Pattern;

import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

/**
 * This class extends {@link TreeCompositeViewer} to add a {@link Combo} for
 * selecting MOOSE tools.
 * 
 * @author Taylor Patterson, Jordan H. Deyton, Anna Wojtowicz
 * 
 */
public class EMFTreeCompositeViewer extends TreeCompositeViewer implements
		IPartListener2 {

	/**
	 * The ID of this view
	 */
	public static final String ID = "org.eclipse.ice.client.widgets.EMFTreeCompositeViewer";

	/**
	 * A reference to the currently active FormEditor.
	 */
	private IWorkbenchPartReference activeEditorRef;

	/**
	 * The constructor
	 */
	public EMFTreeCompositeViewer() {
		super();
		// Initialize the maps
		// comboAppMap = new HashMap<String, String>();
	}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	/**
	 * Create the tree viewer that shows the TreeComposite for the current Form
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Create and add the MOOSE app selection combo box to the toolbar
		// IToolBarManager toolBarMgr = getViewSite().getActionBars()
		// .getToolBarManager();
		// toolBarMgr.add(new ComboContributionItem());

		// Call TreeCompositeViewer's createPartControl method
		super.createPartControl(parent);
		/*
		// Change the add and delete child actions to listen for selections from
		// the EMFTreeCompositeViewer.
		addAction.setPartId(EMFTreeCompositeViewer.ID);
		deleteAction.setPartId(EMFTreeCompositeViewer.ID);

		// Create a MenuManager that will enable a context menu in the
		// TreeViewer.
		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				EMFTreeCompositeViewer.this.fillContextMenu(manager);
			}
		});
		Control control = treeViewer.getControl();
		Menu menu = menuManager.createContextMenu(control);
		control.setMenu(menu);
		*/
		// Register with the PartService to tell which MOOSEModelBuilder is the
		// most recently activated one.
		getSite().getWorkbenchWindow().getPartService().addPartListener(this);

		return;
	}

	/**
	 * This operation populates the context menu for this view.
	 * 
	 * @param menuManager
	 *            The IMenuManager instance for the context menu to be populated
	 */
	@Override
	protected void fillContextMenu(IMenuManager menuManager) {

		ISelection iSelection = treeViewer.getSelection();
		if (!iSelection.isEmpty() && iSelection instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) iSelection;

			Object object = selection.getFirstElement();
			if (object instanceof TreeComposite) {
				final TreeComposite node = (TreeComposite) object;

				// FIXME This is a temporary solution so that multiple plant
				// components can be added. It is also useful for branches,
				// whose inputs and outputs are based on the names of the
				// components. However, we need to add a better way to set the
				// branch inputs (probably with their own dialogs with a list of
				// available inputs/outputs and check boxes).
				menuManager.add(new Action("Change name of \"" + node.getName()
						+ "\"") {
					@Override
					public void run() {
						// Get the required arguments for creating an
						// InputDialog. This includes a shell, a title and
						// message, and a validator for the dialog's String
						// value.
						Shell shell = Display.getCurrent().getActiveShell();
						String dialogTitle = "EMF Component Name";
						String initialValue = node.getName();
						String dialogMessage = "Please enter a new name "
								+ "for the component \"" + initialValue + "\".";

						// Create an InputValidator for the InputDialog. This
						// validator does not allow empty strings, long strings,
						// or most special characters.
						IInputValidator validator = new IInputValidator() {
							@Override
							public String isValid(String newText) {
								// error is the error message. If null by the
								// end of the method, then newText is accepted.
								String error = null;

								// Check for a null string.
								if (newText == null || newText.isEmpty()) {
									error = "Invalid name: "
											+ "Cannot have empty string names.";
								}
								// Check for a string that is too long.
								else if (newText.length() > 50) {
									error = "Invalid name: "
											+ "Please use less than 50 characters";
								}
								// Check for a string that is too short.
								// FIXME This is a temporary workaround so that
								// the digits 1 and 2 can be used to connect
								// branches to primary/secondary heat exchanger
								// pipes.
								else if (newText.length() <= 1) {
									error = "Invalid name: Please use at least "
											+ "2 characters";
								}
								// Check for invalid characters. We allow
								// alphanumeric characters and a small set of
								// special characters.
								else {
									String specials = "\\-\\_\\+\\(\\)\\[\\]\\{\\}\\:";
									Pattern pattern = Pattern
											.compile("[^a-zA-Z0-9" + specials
													+ "]");
									if (pattern.matcher(newText).find()) {
										error = "Invalid name: Please use only "
												+ "letters, numbers, and the "
												+ "following special "
												+ "characters: \n"
												+ "-_+()[]{}:";
									}
								}
								return error;
							}
						};

						// Create the InputDialog and open it.
						InputDialog dialog = new InputDialog(shell,
								dialogTitle, dialogMessage, initialValue,
								validator);
						// If the dialog was accepted and the string was valid,
						// set the name of the TreeComposite.
						if (dialog.open() == Window.OK) {
							node.setName(dialog.getValue());
						}
					}
				});

				// Add a separator and the regular actions.
				menuManager.add(new Separator());
				menuManager.add(addAction);
				menuManager.add(deleteAction);
			}
		}

		return;
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			logger.info("EMFTreeCompositeViewer message: "
					+ "EMFFormEditor part activated.");

			// Update the TreeViewer with the current TreeComposite.
			setInput(inputTree, getFormEditor());
		}

		return;
	}

	/**
	 * Clear the tree viewer when a FormEditor is closed.
	 */
	@Override
	public void partClosed(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			logger.info("EMFTreeCompositeViewer message: "
					+ "EMFFormEditor part closed.");

			setInput(new TreeComposite(), null);

			// If the active editor closed, reset the active editor reference.
			if (partRef == activeEditorRef) {
				activeEditorRef = null;
			}
			// Since this gets called after partActivated with a potentially
			// different partRef, call partActivated again to repopulate views.
			if (activeEditorRef != null) {
				partActivated(activeEditorRef);
			}
		}

		return;
	}

	/**
	 * This function is called whenever a workbench part is brought to the top.
	 * If that part is a EMFFormEditor, keep track of it as the active editor.
	 */
	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			logger.info("EMFTreeCompositeViewer message: "
					+ "MOOSEFormEditor part brought to top.");

			// Set this editor as the active one
			activeEditorRef = partRef;
		}

		return;
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			logger.info("EMFTreeCompositeViewer message: "
					+ "EMFFormEditor part deactivated.");

			// If the active editor closed, reset the active editor reference.
			if (partRef == activeEditorRef) {
				activeEditorRef = null;
			}
		}

		return;
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			logger.info("EMFTreeCompositeViewer message: "
					+ "EMFFormEditor part opened.");
		}

		return;
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			logger.info("EmFTreeCompositeViewer message: "
					+ "EMFFormEditor part hidden.");
		}

		return;
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			logger.info("EMFTreeCompositeViewer message: "
					+ "EMFFormEditor part visible.");
		}

		return;
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			logger.info("EMFTreeCompositeViewer message: "
					+ "EMFFormEditor part input changed.");
		}

		return;
	}
}
