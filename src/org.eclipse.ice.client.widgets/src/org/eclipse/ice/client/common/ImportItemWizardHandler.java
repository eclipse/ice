package org.eclipse.ice.client.common;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ice.client.widgets.wizards.ImportItemWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This is a handler for creating an {@link ImportItemWizard} from the main
 * Eclipse UI ToolBar.
 * 
 * @author Jordan
 * 
 */
public class ImportItemWizardHandler extends AbstractHandler {

	/**
	 * Opens an {@link ImportItemWizard} in a new {@link WizardDialog}.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Get the window and the shell.
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		Shell shell = window.getShell();

		// Create a dialog and open it. The wizard itself handles everything, so
		// we do not need to do anything special with the return value.
		WizardDialog dialog = new WizardDialog(shell, new ImportItemWizard());
		dialog.open();

		return null;
	}

}
