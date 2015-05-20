/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation 
 *   
 *******************************************************************************/
package org.eclipse.ice.client.widgets.test;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEFormInput;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.junit.BeforeClass;

/**
 * This abstract class provides a basic framework for performing SWTBot tests
 * for Eclipse UI plug-ins inside a JUnit test framework. As such, test classes
 * sub-classing this abstract class can be run as JUnit plug-in tests <b>(off
 * the UI thread)</b> or SWTBot tests, but <i>not plain JUnit tests</i>, as the
 * workbench is required.
 * <p>
 * This class provides helpful methods to help navigate the ICE workbench and
 * open editors, views, or perspectives for testing.
 * </p>
 * <p>
 * An {@link SWTWorkbenchBot} is opened for each test class instance and should
 * be used for the actual UI tests. The bot can be acquired via
 * {@link #getBot()}.
 * </p>
 * <p>
 * <b>Note:</b> Changes to the UI must be coordinated with the main UI thread
 * using {@link Display#syncExec(Runnable)} or
 * {@link Display#asyncExec(Runnable)}. To get the shell's display, you may call
 * {@link #getDisplay()}.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractWorkbenchTester extends
		AbstractICEUITester<SWTWorkbenchBot> {

	/**
	 * Tries to close the "Welcome" view if it is open. This only needs to be
	 * done once, and nothing needs to be disposed afterward.
	 */
	@BeforeClass
	public static void beforeWorkbenchClass() {

		// Close the welcome view using a temporary SWTBot.
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		try {
			bot.viewByTitle("Welcome").close();
		} catch (WidgetNotFoundException e) {
			// Nothing to do.
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.test.AbstractICEUITester#createBot()
	 */
	@Override
	protected SWTWorkbenchBot createBot() {
		return new SWTWorkbenchBot();
	}

	/**
	 * Tries to open an Eclipse editor given the specified input and the ID of
	 * the editor.
	 * 
	 * @param input
	 *            The editor's input.
	 * @param id
	 *            The Eclipse editor's ID as defined in the plug-in extensions.
	 * @return The opened editor, or {@code null} if it could not be opened.
	 */
	protected IEditorPart openEditor(final IEditorInput input, final String id) {
		final AtomicReference<IEditorPart> editorRef = new AtomicReference<IEditorPart>();

		// This must be done on the UI thread. Use syncExec so that this method
		// will block until the editor can be opened.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				// Get the workbench window so we can open an editor.
				IWorkbenchWindow window = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();

				// Try to open the editor in the workbench.
				try {
					IEditorPart editor = page.openEditor(input, id);
					editorRef.set(editor);
				} catch (PartInitException e) {
					// Nothing to do.
				}

				return;
			}
		});

		return editorRef.get();
	}

	/**
	 * Tries to open an {@link ICEFormEditor} for the given ICE {@link Form}.
	 * 
	 * @param form
	 *            The form to open.
	 * @return The opened editor, or {@code null} if it could not be opened.
	 */
	protected ICEFormEditor openICEFormEditor(Form form) {
		return openICEFormEditor(form, ICEFormEditor.ID);
	}

	/**
	 * Tries to open an {@link ICEFormEditor} for the given ICE {@link Form}.
	 * 
	 * @param form
	 *            The form to open.
	 * @param id
	 *            The Eclipse editor's ID as defined in the plug-in extensions.
	 * @return The opened editor, or {@code null} if it could not be opened.
	 */
	protected ICEFormEditor openICEFormEditor(Form form, String id) {
		return openICEFormEditor(new ICEFormInput(form), id);
	}

	/**
	 * Tries to open an {@link ICEFormEditor} for the given ICE {@link Form}.
	 * 
	 * @param input
	 *            The ICE editor's input.
	 * @return The opened editor, or {@code null} if it could not be opened.
	 */
	protected ICEFormEditor openICEFormEditor(ICEFormInput input) {
		return openICEFormEditor(input, ICEFormEditor.ID);
	}

	/**
	 * Tries to open an {@link ICEFormEditor} for the given ICE {@link Form}.
	 * 
	 * @param input
	 *            The ICE editor's input.
	 * @param id
	 *            The Eclipse editor's ID as defined in the plug-in extensions.
	 * @return The opened editor, or {@code null} if it could not be opened.
	 */
	protected ICEFormEditor openICEFormEditor(ICEFormInput input, String id) {
		return (ICEFormEditor) openEditor(input, id);
	}

	/**
	 * Uses the {@link SWTWorkbenchBot} to open the Eclipse View with the
	 * specified category and name.
	 * <p>
	 * This is equivalent to the following user interaction:
	 * <ol>
	 * <li>click on "Window" > "Show View" > "Other..."</li>
	 * <li>select "category" > "name"</li>
	 * <li>press "OK"</li>
	 * </ol>
	 * </p>
	 * 
	 * @param category
	 *            The category for the view.
	 * @param name
	 *            The name of the view. This is the user-friendly view name as
	 *            seen in the plug-in extensions and the UI itself.
	 * @return The view wrapped by the {@code SWTBot}.
	 */
	protected SWTBotView openView(String category, String name) {

		SWTWorkbenchBot bot = getBot();

		// Open "Window" > "Show View" > "Other..."
		bot.menu("Window").menu("Show View").menu("Other...").click();
		// To pick from the dialog, we must activate it.
		bot.shell("Show View").activate();
		// Select "category" > "name"
		bot.tree().expandNode(category, false).select(name);
		// Close the dialog by clicking OK.
		bot.button("OK").click();
		// Return the view itself.
		return bot.viewByTitle(name);
	}

	/**
	 * Closes the view with the specified name.
	 * 
	 * @param name
	 *            The name of the view. This is the user-friendly view name as
	 *            seen in the plug-in extensions and the UI itself.
	 */
	protected void closeView(String name) {
		getBot().viewByTitle(name).close();
	}
}