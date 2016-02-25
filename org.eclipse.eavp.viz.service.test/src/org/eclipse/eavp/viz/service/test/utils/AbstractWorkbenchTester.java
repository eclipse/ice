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
package org.eclipse.eavp.viz.service.test.utils;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.MultiPartInitException;
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
public abstract class AbstractWorkbenchTester
		extends AbstractICEUITester<SWTWorkbenchBot> {

	/**
	 * The {@code SWTBot} for this test class instance. This bot is to aid in
	 * performing UI tests that simulate user interaction with workbench-based
	 * plug-ins.
	 */
	private static SWTWorkbenchBot bot;

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
	 * @see org.eclipse.ice.client.widgets.test.AbstractUITester#getBot()
	 */
	@Override
	protected SWTWorkbenchBot getBot() {
		return bot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractUITester#beforeAllTests()
	 */
	@Override
	public void beforeAllTests() {
		super.beforeAllTests();

		// Initialize static or otherwise shared resources here.

		// Set up the SWTBot for the workbench.
		bot = new SWTWorkbenchBot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractUITester#beforeEachTest()
	 */
	@Override
	public void beforeEachTest() {
		super.beforeEachTest();

		// Initialize per-test resources here.
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.test.AbstractUITester#afterEachTest()
	 */
	@Override
	public void afterEachTest() {
		// Dispose per-test resources here.
		// Nothing to do yet.

		// Proceed with the default post-test cleanup.
		super.afterEachTest();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.test.AbstractUITester#afterAllTests()
	 */
	@Override
	public void afterAllTests() {
		// Dispose static or otherwise shared resources here.
		// Nothing to do yet.

		// Proceed with the default post-tests cleanup.
		super.afterAllTests();
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
	protected IEditorReference openEditor(final IEditorInput input,
			final String id) {
		final AtomicReference<IEditorReference> editorRef = new AtomicReference<IEditorReference>();

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
					// IEditorPart editor = page.openEditor(input, id);
					IEditorReference[] refs = page.openEditors(
							new IEditorInput[] { input }, new String[] { id },
							IWorkbenchPage.MATCH_NONE);
					if (refs.length > 0) {
						editorRef.set(refs[0]);
					}
				} catch (MultiPartInitException e) {
					// Nothing to do.
				}

				return;
			}
		});

		return editorRef.get();
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