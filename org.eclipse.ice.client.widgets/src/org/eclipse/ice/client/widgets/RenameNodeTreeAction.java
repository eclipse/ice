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
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This <code>Action</code> is used to rename nodes ({@link TreeComposite}s) in
 * the {@link TreeCompositeViewer}. Nodes cannot be renamed if they are
 * root-level (not counting the single root of the tree).
 * 
 * @author Jordan H. Deyton
 * 
 */
public class RenameNodeTreeAction extends AbstractTreeAction {

	/**
	 * The default constructor.
	 */
	public RenameNodeTreeAction() {

		// Set the default ID to the MOOSETreeCompositeViewer.
		setPartId(TreeCompositeViewer.ID);

		// Set the text and tool tip for the action.
		setText("Rename");
		setToolTipText("Rename the selected node.");

		return;
	}

	/**
	 * If the <code>Action</code> can be run, this prompts the user for a new
	 * name for the selected node and updates the node's name if the provided
	 * string is valid.
	 */
	@Override
	public void run() {
		if (isEnabled()) {
			TreeComposite selectedNode = getSelectedNode();

			// Get the required arguments for creating an
			// InputDialog. This includes a shell, a title and
			// message, and a validator for the dialog's String
			// value.
			Shell shell = Display.getCurrent().getActiveShell();
			String dialogTitle = "Block Name";
			String initialValue = selectedNode.getName();
			String dialogMessage = "Please enter a new name "
					+ "for the block \"" + initialValue + "\".";

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
								.compile("[^a-zA-Z0-9" + specials + "]");
						if (pattern.matcher(newText).find()) {
							error = "Invalid name: Please use only "
									+ "letters, numbers, and the "
									+ "following special " + "characters: \n"
									+ "-_+()[]{}:";
						}
					}
					return error;
				}
			};

			// Create the InputDialog and open it.
			InputDialog dialog = new InputDialog(shell, dialogTitle,
					dialogMessage, initialValue, validator);
			// If the dialog was accepted and the string was valid,
			// set the name of the TreeComposite.
			if (dialog.open() == Window.OK) {
				selectedNode.setName(dialog.getValue());
			}

		}

		return;
	}

}
