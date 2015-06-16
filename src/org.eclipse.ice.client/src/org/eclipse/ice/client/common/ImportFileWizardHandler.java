package org.eclipse.ice.client.common;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.client.common.wizards.ImportFileWizard;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This handler is used to import files into the ICE project space. It is used
 * in the main Eclipse UI ToolBar (see bundle extensions) and serves as a
 * shortcut to the {@link ImportFileWizard}.
 * <p>
 * <b>Note:</b> This class does not actually call the
 * <code>ImportFileWizard</code>. Instead, it opens a {@link FileDialog} and
 * processes the selected files as the <code>ImportFileWizard</code> normally
 * does.
 * </p>
 * 
 * @author Jordan
 * 
 */
public class ImportFileWizardHandler extends AbstractHandler {

	/**
	 * Opens a new {@link FileDialog} and imports any selected files into the
	 * ICE project space.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Get the window and the shell.
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		Shell shell = window.getShell();

		// Get the Client
		IClient client = ClientHolder.getClient();

		// Create the dialog and get the files
		FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
		fileDialog.setText("Select a file to import into ICE");
		fileDialog.open();

		// Import the files
		String filterPath = fileDialog.getFilterPath();
		for (String name : fileDialog.getFileNames()) {
			File importedFile = new File(filterPath, name);
			client.importFile(importedFile.toURI());
		}

		return null;
	}

}
