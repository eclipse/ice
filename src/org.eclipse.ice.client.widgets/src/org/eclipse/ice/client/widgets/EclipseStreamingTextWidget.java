/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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

import java.io.IOException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import org.eclipse.ice.iclient.uiwidgets.IStreamingTextWidget;

/**
 * 
 * @author Jay Jay Billings
 */
public class EclipseStreamingTextWidget implements IStreamingTextWidget {

	/**
	 * The label that will be used for the console. It is set by calling
	 * setLabel().
	 */
	private String consoleLabel = null;

	/**
	 * The console view in Eclipse.
	 */
	private IConsoleView consoleView = null;

	/**
	 * The console that will display text from this widget.
	 */
	private MessageConsole console = null;

	/**
	 * The message stream for the message console to which text should be
	 * streamed.
	 */
	private MessageConsoleStream msgStream = null;

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStreamingTextWidget#setLabel(String label)
	 */
	public void setLabel(String label) {

		// Set the label
		consoleLabel = label;

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStreamingTextWidget#postText(String sText)
	 */
	public void postText(final String sText) {

		// Must sync with the display thread
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// Push the text, if possible
				if (msgStream != null) {
					msgStream.println(sText);
				}
			}
		});

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStreamingTextWidget#display()
	 */
	public void display() {

		// Must sync with the display thread
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// Get the currently active page
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					// Load the console view
					consoleView = (IConsoleView) page
							.showView(IConsoleConstants.ID_CONSOLE_VIEW);
					// Create the console instance that will be used to display
					// text from this widget.
					console = new MessageConsole("CLI", null);
					// Add the console to the console manager
					ConsolePlugin.getDefault().getConsoleManager()
							.addConsoles(new IConsole[] { console });
					// Show the console in the view
					consoleView.display(console);
					console.activate();
					// Get an output stream for the console
					msgStream = console.newMessageStream();
					msgStream.setActivateOnWrite(true);
					msgStream.println("Streaming output console activated.");
				} catch (PartInitException e) {
					// Complain
					System.out
							.println("EclipseStreamingTextWidget Message: Unable to "
									+ "stream text!");
					e.printStackTrace();
				}

			}
		});

		return;
	}
}