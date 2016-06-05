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
package org.eclipse.ice.item.action;

import java.util.Dictionary;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.january.form.Form;
import org.eclipse.january.form.FormStatus;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Action class performs actions, such as launching a job or script, for
 * ICE. It requires a Form with relevant information to execute properly and it
 * may, at its own discretion, request additional information using a secondary,
 * Action-specific Form. This Form should not be created with list of Actions
 * since it is not used by an Item. This is needed to retrieve unexpected
 * information, such as login usernames and passwords, and contains a single
 * DataComponent with Entries for each additional bit of requested information.
 * This additional Form should not be used to retrieve large amounts of
 * additional information. Instead, it should be considered for
 * "emergency use only" and used for those types of information that are truly
 * unpredictable or not time-independent.
 * 
 * Actions may update the dictionary passed to execute() at their discretion, so
 * keep in mind that it may change if you depend on it in the client class.
 * 
 * @author Jay Jay Billings, Alex McCaskey
 */
public abstract class Action {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(Action.class);

	/**
	 * The console view in Eclipse.
	 */
	private static IConsoleView consoleView = null;

	/**
	 * The console that will display text from this widget.
	 */
	private static MessageConsole console = null;

	/**
	 * The message stream for the message console to which text should be
	 * streamed.
	 */
	private static MessageConsoleStream msgStream = null;

	/**
	 * <p>
	 * A Form that is used to by the Action if it requires additional
	 * information, such as a login username and password, that is not available
	 * on the Form submitted by the Item that executed the action. This Form
	 * contains one DataComponent with Entries for each additional piece of
	 * required information.
	 * </p>
	 * 
	 */
	protected Form actionForm;

	/**
	 * <p>
	 * A Form that contains data from the Item that initiated the Action.
	 * </p>
	 * 
	 */
	protected Form dataForm;

	/**
	 * <p>
	 * The current status of the Action.
	 * </p>
	 * 
	 */
	protected FormStatus status;

	/**
	 * This static method initializes the data structures necessary for Eclipse
	 * Console output for the Action.execute method.
	 */
	protected static void initializeConsoleOutput() {
		// Open the Console for Action output text
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// Get the currently active page
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					// Load the console view
					consoleView = (IConsoleView) page.showView(IConsoleConstants.ID_CONSOLE_VIEW);
					// Create the console instance that will be used to display
					// text from this widget.
					if (console == null) {
						console = new MessageConsole("CLI", null);
						// Add the console to the console manager
						ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
						// Show the console in the view
						consoleView.display(console);
						console.activate();
						// Get an output stream for the console
						msgStream = console.newMessageStream();
						msgStream.setActivateOnWrite(true);
						msgStream.println("Streaming output console activated.");
					}
				} catch (PartInitException e) {
					// Complain
					logger.error("Action Message: " + "Unable to stream text!");
					logger.error(getClass().getName() + " Exception!", e);
				}

			}
		});

	}

	/**
	 * This operation allows subclasses to post errors and return the
	 * appropriate status to the Item executing the Action.
	 * 
	 * @param errorMessage
	 *            The descriptive message for the error.
	 * @param exception
	 *            A possible exception to print to the Logger.
	 * @return status The error status.
	 */
	protected FormStatus actionError(String errorMessage, Exception exception) {
		if (exception == null) {
			logger.error(errorMessage);
		} else {
			logger.error(errorMessage, exception);
		}
		status = FormStatus.InfoError;
		return status;
	}

	/**
	 * This operation can be used by subclasses to post text to the Eclipse
	 * Console.
	 * 
	 * @param sText
	 *            The string to print to the Console.
	 */
	protected void postConsoleText(final String sText) {

		if (msgStream == null) {
			initializeConsoleOutput();
		}

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
	 * This operation clears the Eclipse console.
	 */
	public static void clearConsole() {
		if (console != null) {
			console.clearConsole();
		}
	}

	/**
	 * <p>
	 * This operation retrieves a Form from the Action that is used to request
	 * additional, unexpected information from the user such as a login username
	 * and password.
	 * </p>
	 * 
	 * @return
	 *         <p>
	 *         The second Form created by the Action for retrieving, for
	 *         example, a username and password.
	 *         </p>
	 */
	public Form getForm() {
		return actionForm;
	}

	/**
	 * <p>
	 * This operation submits a Form to the Action that contains additional,
	 * unexpected information from the user such as a login username and
	 * password. This Form was originally created by the Action and posted by
	 * the calling Item.
	 * </p>
	 * 
	 * @param form
	 *            <p>
	 *            The second Form created by the Action for retrieving, for
	 *            example, a username and password.
	 *            </p>
	 * @return
	 *         <p>
	 *         The ItemStatus value that specifies whether or not the secondary
	 *         Form was accepted by the Action. By default it is
	 *         FormStatus.Processing for any non-null Form and
	 *         FormStatus.InfoError otherwise.
	 *         </p>
	 */
	public FormStatus submitForm(Form form) {

		// Accept or reject the Form
		if (form != null) {
			actionForm = form;
			return FormStatus.Processing;
		} else {
			return FormStatus.InfoError;
		}
	}

	/**
	 * <p>
	 * This operation executes the Action based on the information provided in
	 * the dictionary. Subclasses must implement this operation and should
	 * publish the exact keys and values that they require to perform their
	 * function in their documentation.
	 * </p>
	 * 
	 * @param dictionary
	 *            <p>
	 *            A dictionary that contains key-value pairs used by the action
	 *            to perform its function.
	 *            </p>
	 * @return
	 *         <p>
	 *         The status of the Action.
	 *         </p>
	 */
	public abstract FormStatus execute(Dictionary<String, String> dictionary);

	/**
	 * <p>
	 * This operation cancels the Action, if possible.
	 * </p>
	 * 
	 * @return
	 *         <p>
	 *         The ItemStatus value that specifies whether or not the Action was
	 *         canceled successfully.
	 *         </p>
	 */
	public abstract FormStatus cancel();

	/**
	 * <p>
	 * This operation returns the current status of the Action.
	 * </p>
	 * 
	 * @return
	 *         <p>
	 *         The status
	 *         </p>
	 */
	public FormStatus getStatus() {
		return status;
	}

	/**
	 * Return the name of this Action. This name will be used by the
	 * IActionFactory to reference and create new Actions.
	 * 
	 * @return name The name of this Action.
	 */
	public abstract String getActionName();

	/**
	 * Return the available Actions provided by the Extension Registry.
	 * 
	 * @return actions All Actions exposed in the Extension Registry.
	 * 
	 * @throws CoreException
	 */
	public static Action[] getAvailableActions() throws CoreException {
		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(Action.class);

		Action[] actions = null;
		String id = "org.eclipse.ice.item.actions";
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(id);

		// If the point is available, create all the builders and load them into
		// the array.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			actions = new Action[elements.length];
			for (int i = 0; i < elements.length; i++) {
				actions[i] = (Action) elements[i].createExecutableExtension("class");
			}
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return actions;
	}

}