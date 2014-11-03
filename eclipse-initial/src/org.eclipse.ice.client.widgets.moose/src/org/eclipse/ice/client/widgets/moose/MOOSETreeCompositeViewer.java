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
package org.eclipse.ice.client.widgets.moose;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.eclipse.ice.client.common.TreeCompositeViewer;
import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEFormInput;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.item.nuclear.MOOSEModel;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
 * @author tnp, djg, w5q
 * 
 */
public class MOOSETreeCompositeViewer extends TreeCompositeViewer implements
		IPartListener2 {

	/**
	 * The ID of this view
	 */
	public static final String ID = "org.eclipse.ice.client.widgets.moose.MOOSETreeCompositeViewer";

	/**
	 * The SWT Combo for selecting the MOOSE tool responsible for the tree
	 * contents
	 */
	private Combo appCombo;

	/**
	 * The ICEFormEditor using this viewer
	 */
	private ICEFormEditor editor;

	/**
	 * The the input to the ICEFormEditor using this viewer
	 */
	private ICEFormInput formInput;

	/**
	 * The Form contained by the ICEFormInput
	 */
	private Form form;

	/**
	 * The DataComponent in the Form in the editor using this viewer
	 */
	private DataComponent dataComponent;

	/**
	 * The MOOSE-Based Application Entry of the DataComponent.
	 */
	private Entry appsEntry;

	/**
	 * The the name and ID of the active MOOSEEditor.
	 */
	private String activeEditorNameAndId;

	/**
	 * A map of the Strings in the Combo to their associated app name Strings.
	 */
	private HashMap<String, String> comboAppMap;

	/**
	 * A reference to the currently active MOOSEFormEditor.
	 */
	private IWorkbenchPartReference activeEditorRef;

	/**
	 * The constructor
	 */
	public MOOSETreeCompositeViewer() {

		// Initialize the maps
		comboAppMap = new HashMap<String, String>();
	}
	
	/**
	 * Create the tree viewer that shows the TreeComposite for the current Form
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 *      .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Create and add the MOOSE app selection combo box to the toolbar
		IToolBarManager toolBarMgr = getViewSite().getActionBars()
				.getToolBarManager();
		toolBarMgr.add(new ComboContributionItem());

		// Call TreeCompositeViewer's createPartControl method
		super.createPartControl(parent);

		// Change the add and delete child actions to listen for selections from
		// the MOOSETreeCompositeViewer.
		addAction.setPartId(MOOSETreeCompositeViewer.ID);
		deleteAction.setPartId(MOOSETreeCompositeViewer.ID);

		// Create a MenuManager that will enable a context menu in the
		// TreeViewer.
		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				MOOSETreeCompositeViewer.this.fillContextMenu(manager);
			}
		});
		Control control = treeViewer.getControl();
		Menu menu = menuManager.createContextMenu(control);
		control.setMenu(menu);

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
						String dialogTitle = "Plant Component Name";
						String initialValue = node.getName();
						String dialogMessage = "Please enter a new name "
								+ "for the component \"" + initialValue + "\".";

						// Create an InputValidator for the InputDialog. This
						// validator does not allow empty strings, long strings,
						// or most special characters.
						IInputValidator validator = new IInputValidator() {
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

	/**
	 * This operation sets the contents on the MOOSE-Based Application Combo to
	 * the allowed values collected from the active MOOSEFormEditor.
	 */
	public void updateComboBox() {

		// Local declaration
		String selectAppText = "Select MOOSE app";

		// Check that the widget isn't disposed first
		if (appCombo.isDisposed()) {
			return;
		}
		// Remove any existing contents in the combo box
		appCombo.removeAll();

		// Get the editor. Use the editor to extract the DataComponent.
		editor = (ICEFormEditor) getSite().getWorkbenchWindow().getActivePage()
				.getActiveEditor();
		if (editor == null) {
			return;
		}
		formInput = (ICEFormInput) editor.getEditorInput();
		form = formInput.getForm();
		dataComponent = (DataComponent) form
				.getComponent(MOOSEModel.fileDataComponentId);

		// Get the Entry from the DataComponent.
		appsEntry = dataComponent.retrieveEntry("MOOSE-Based Application");

		// Get the active editor name and ID.
		activeEditorNameAndId = form.getName() + " " + form.getId();

		// Get the Strings of allowed values (apps)
		for (String appName : appsEntry.getAllowedValues()) {
			if (appName.equals(selectAppText)) {
				appCombo.add(selectAppText);
				comboAppMap.put(selectAppText, selectAppText);
			} else {
				String comboContentStr = activeEditorNameAndId
						+ " - "
						+ ("relap".equals(appName) ? appName.toUpperCase()
								+ "-7" : appName.toUpperCase());
				appCombo.add(comboContentStr);
				comboAppMap.put(comboContentStr, appName);
			}
		}

		String currApp = ("relap".equals(appsEntry.getValue()) ? appsEntry
				.getValue().toUpperCase() + "-7" : appsEntry.getValue()
				.toUpperCase());
		// Set the combo box text to the current app
		if (currApp.equalsIgnoreCase(selectAppText)) {
			appCombo.setText(selectAppText);
		} else {
			appCombo.setText(activeEditorNameAndId + " - " + currApp);
		}
		// If the selection is for RELAP-7, create a Plant View page if one
		// doesn't already exist. If the selection is NOT for RELAP-7, delete
		// any existing Plant View page.
		if ("relap".equals(appsEntry.getValue())) {
			((MOOSEFormEditor) editor).addPlantPage();
		} else {
			((MOOSEFormEditor) editor).removePlantPage();
		}

		return;
	}

	// ---- Implements IPartListener2 ---- //
	/*
	 * Listen to the PartService for the currently opened MOOSEModelBuilder. The
	 * contents of the tree AND the currently selected MOOSE app combo should be
	 * updated based on the currently opened MOOSEModelBuilder.
	 */
	public void partActivated(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(MOOSEFormEditor.ID)) {
			System.out.println("MOOSETreeCompositeViewer message: "
					+ "MOOSEFormEditor part activated.");

			// Update the combo box based on the activated MOOSEFormEditor.
			updateComboBox();

			// Update the TreeViewer with the current TreeComposite.
			setInput((TreeComposite) form
					.getComponent(MOOSEModel.mooseTreeCompositeId));
		}

		return;
	}

	/**
	 * Clear the tree viewer when a MOOSEFormEditor is closed.
	 */
	public void partClosed(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(MOOSEFormEditor.ID)) {
			System.out.println("MOOSETreeCompositeViewer message: "
					+ "MOOSEFormEditor part closed.");

			try {
				// Clear the combo box
				appCombo.removeAll();
				comboAppMap.clear();

				// Clear the tree
				setInput(new TreeComposite());
			} catch (SWTException e) {
				// Do nothing. Workbench is disposed.
			}

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
	 * If that part is a MOOSEFormEditor, keep track of it as the active editor.
	 */
	public void partBroughtToTop(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(MOOSEFormEditor.ID)) {
			System.out.println("MOOSETreeCompositeViewer message: "
					+ "MOOSEFormEditor part brought to top.");

			// Set this editor as the active one
			activeEditorRef = partRef;
		}

		return;
	}

	public void partDeactivated(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(MOOSEFormEditor.ID)) {
			System.out.println("MOOSETreeCompositeViewer message: "
					+ "MOOSEFormEditor part deactivated.");

			// If the active editor closed, reset the active editor reference.
			if (partRef == activeEditorRef) {
				activeEditorRef = null;
			}
		}

		return;
	}

	public void partOpened(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(MOOSEFormEditor.ID)) {
			System.out.println("MOOSETreeCompositeViewer message: "
					+ "MOOSEFormEditor part opened.");
		}

		return;
	}

	public void partHidden(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(MOOSEFormEditor.ID)) {
			System.out.println("MOOSETreeCompositeViewer message: "
					+ "MOOSEFormEditor part hidden.");
		}

		return;
	}

	public void partVisible(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(MOOSEFormEditor.ID)) {
			System.out.println("MOOSETreeCompositeViewer message: "
					+ "MOOSEFormEditor part visible.");
		}

		return;
	}

	public void partInputChanged(IWorkbenchPartReference partRef) {

		if (partRef.getId().equals(MOOSEFormEditor.ID)) {
			System.out.println("MOOSETreeCompositeViewer message: "
					+ "MOOSEFormEditor part input changed.");
		}

		return;
	}

	// ----------------------------------- //

	/**
	 * This class extends ControlContribution (a standard implementation of
	 * IContributionItem) to create a Combo for selecting MOOSE applications.
	 * This allows this Combo to be added to the tool bar of the view since only
	 * IActions and IContributionItems can be added to the ToolBarManager.
	 * 
	 * @author tnp
	 * 
	 */
	private class ComboContributionItem extends ControlContribution {

		/**
		 * The constructor
		 */
		protected ComboContributionItem() {
			super("MOOSEAppCombo");
		}

		/**
		 * This operation populates the combo box
		 * 
		 * @param parent
		 *            The parent Composite
		 */
		@Override
		protected Control createControl(Composite parent) {

			// Initialize the Combo
			appCombo = new Combo(parent, SWT.READ_ONLY | SWT.V_SCROLL);
			appCombo.setSize(200, 50);

			// Add a SelectionListener to the Combo
			appCombo.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

					// Get the text of the Combo to determine the selection.
					String selectionStr = appCombo.getText();

					// Set the Entry selection to match the selection made in
					// the Combo.
					String appStr = comboAppMap.get(selectionStr);
					appsEntry.setValue(appStr);

					// If the selection is for RELAP-7, create a Plant View page
					// if one doesn't already exist. If the selection is NOT for
					// RELAP-7, delete any existing Plant View page.
					if ("relap".equals(appsEntry.getValue())) {
						((MOOSEFormEditor) editor).addPlantPage();
					} else {
						((MOOSEFormEditor) editor).removePlantPage();
					}

					// Notify the editor's listeners that it's underlying Form
					// has changed and update the tree.
					editor.notifyUpdateListeners();

					// Refresh the "treeViewer's" selection for the add and
					// delete actions.
					addAction.selectionChanged(getSite().getPart(),
							new ISelection() {
								@Override
								public boolean isEmpty() {
									return true;
								}
							});
					

					return;
				}
			});

			return appCombo;
		}

		/**
		 * Set the width of the Combo.
		 */
		@Override
		protected int computeWidth(Control control) {
			return 300;
		}

	}

}
