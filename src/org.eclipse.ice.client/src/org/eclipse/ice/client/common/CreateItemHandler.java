package org.eclipse.ice.client.common;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ice.client.common.wizards.NewItemWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This is a handler for creating an {@link NewItemWizard}. Currently, it is
 * only used in the {@link ItemViewer}'s <code>ToolBar</code>. However, it could
 * be used in the main Eclipse UI <code>ToolBar</code> if a command is added to
 * the <code>org.eclipse.ui.menus</code> extension point.
 * 
 * @author Jordan
 * 
 */
public class CreateItemHandler extends AbstractHandler {

	/**
	 * Opens an {@link NewItemWizard} in a new {@link WizardDialog}.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Get the window and the shell.
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		Shell shell = window.getShell();

		// Create a dialog and open it. The wizard itself handles everything, so
		// we do not need to do anything special with the return value.
		WizardDialog dialog = new WizardDialog(shell, new NewItemWizard());
		dialog.open();

		return null;
	}

}
