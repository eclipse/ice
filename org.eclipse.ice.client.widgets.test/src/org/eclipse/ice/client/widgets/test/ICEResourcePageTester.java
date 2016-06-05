/*******************************************************************************
 * Copyright (c) 2012, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jay Jay Billings - Initial API and implementation and/or initial documentation
 *   Jordan Deyton - Added SWTBot UI tests.
 *   Kasper Gammeltoft - viz series refactor
 *   Jordan Deyton - removed code testing PlotGridComposite usage
 *******************************************************************************/
package org.eclipse.ice.client.widgets.test;

import java.io.File;
import java.io.IOException;

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEResourcePage;
import org.eclipse.ice.client.widgets.test.utils.AbstractWorkbenchTester;
import org.eclipse.january.form.DataComponent;
import org.eclipse.january.form.Form;
import org.eclipse.january.form.ICEResource;
import org.eclipse.january.form.ResourceComponent;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.IEditorReference;
import org.junit.Test;

/**
 * This class is responsible for testing the ICEResourcePage.
 *
 * @author Jay Jay Billings
 * @author Jordan H. Deyton
 * @author Kasper Gammeltoft - Removed a line to conform with changes to IPlot
 */
public class ICEResourcePageTester extends AbstractWorkbenchTester {

	// ---- Resources shared between tests. ---- //
	/**
	 * The {@code ICEResourcePage} that can be shared among these test cases.
	 * <p>
	 * <b>Note:</b> This page should <i>not</i> have a viz factory assigned to
	 * it.
	 * </p>
	 */
	private static ICEResourcePage sharedPage;

	/**
	 * A reference to the {@link #sharedPage}'s {@code ICEFormEditor}.
	 * <p>
	 * This must be an {@code IEditorReference} for more convenient interaction
	 * with an {@code SWTBot}. To get the {@code ICEFormEditor}, use the
	 * following code:
	 * </p>
	 * <p>
	 * {@code (ICEFormEditor) editorRef.getEditor(false)}.
	 * </p>
	 */
	private static IEditorReference sharedEditorRef;

	/**
	 * The {@link #sharedPage}'s {@code ResourceComponent} that was passed in
	 * the {@code ICEFormEditor}'s input. To show a new {@code ICEResource}, add
	 * it to this component, then {@link #doubleClickResource(ICEResource)
	 * double-click it}.
	 */
	private static ResourceComponent sharedResources;
	// ----------------------------------------- //

	// ---- Resources re-allocated each test. ---- //
	// ------------------------------------------- //

	// ---- Resources re-allocated only when necessary. ---- //
	/**
	 * An {@code ICEResourcePage} to be used in individual tests and
	 * re-allocated before use.
	 */
	private ICEResourcePage page;
	/**
	 * The {@code ICEFormEditor} containing the {@link #page} (wrapped as an
	 * {@code IEditorReference} for convenience when dealing with SWTBot).
	 */
	private IEditorReference editorRef;
	/**
	 * The {@link #page}'s {@code ResourceComponent} that will contain any
	 * resources to be opened by the page. To show a new {@code ICEResource},
	 * add it to this component, then {@link #doubleClickResource(ICEResource)
	 * double-click it}.
	 */
	private ResourceComponent resources;

	// ----------------------------------------------------- //

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ice.client.widgets.test.AbstractWorkbenchTester#
	 * beforeAllTests ()
	 */
	@Override
	public void beforeAllTests() {
		super.beforeAllTests();
		// Initialize static or otherwise shared resources here.
		SWTBotPreferences.PLAYBACK_DELAY = 100;
		openView("Other", "Resources");

		// ---- Open an ICEFormEditor with an ICEResourcePage. ---- //
		ICEFormEditor editor;
		Form form;

		// Create a new Form with a ResourceComponent.
		sharedResources = new ResourceComponent();
		form = new Form();
		form.addComponent(sharedResources);

		// Create the ICEFormEditor (it should have an ICEResourcePage).
		sharedEditorRef = openICEFormEditor(form);
		editor = (ICEFormEditor) sharedEditorRef.getEditor(false);
		// The editor should not be null.
		assertNotNull(editor);

		// Get the ICEResourcePage from the editor.
		sharedPage = editor.getResourcePage();
		// -------------------------------------------------------- //

		return;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ice.client.widgets.test.AbstractWorkbenchTester#
	 * beforeEachTest ()
	 */
	@Override
	public void beforeEachTest() {
		super.beforeEachTest();
		// Initialize per-test resources here.

		// Activate the main test editor.
		getBot().editor(getExactMatcher(sharedEditorRef)).show();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractWorkbenchTester#afterEachTest
	 * ()
	 */
	@Override
	public void afterEachTest() {
		// Dispose per-test resources here.

		// Proceed with the default post-test cleanup.
		super.afterEachTest();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractWorkbenchTester#afterAllTests
	 * ()
	 */
	@Override
	public void afterAllTests() {

		// Close the page/editor.
		getBot().editor(getExactMatcher(sharedEditorRef)).close();

		closeView("Resources");

		// Unset all of the static variables.
		sharedPage = null;
		sharedEditorRef = null;
		sharedResources = null;

		// Proceed with the default post-tests cleanup.
		super.afterAllTests();
	}

	/**
	 * This checks that, when a new {@link ICEFormEditor} has a
	 * {@link ResourceComponent} in its input, that it creates a new
	 * {@code ICEResourcePage} and gives it the same {@code ResourceComponent}.
	 */
	@Test
	public void checkConstruction() {
		// This test should be performed on a unique editor.

		ICEFormEditor editor;

		// Creating a new ICEFormEditor with no ResourceComponent means there
		// should be no ICEResourcePage.
		Form form = new Form();
		// Give the form a DataComponent so at least one page will be added,
		// otherwise we will get an error creating the editor.
		form.addComponent(new DataComponent());
		editorRef = openICEFormEditor(form);
		editor = (ICEFormEditor) editorRef.getPart(false);
		page = editor.getResourcePage();
		// The page is null.
		assertNull(page);
		// Close the editor.
		getBot().editor(getExactMatcher(editorRef)).close();

		// Creating a new ICEFormEditor with a ResourceComponent means there
		// should be an ICEResourcePage whose ResourceComponent is set to the
		// input Form's ResourceComponent.
		openNewEditor();
		// In this case, the page is not null, and the ResourceComponent is
		// shared between the page and the form.
		assertNotNull(page);
		assertSame(resources, page.getResourceComponent());
		// Close the editor.
		closeEditor();

		return;
	}

	/**
	 * This operation checks that the {@code ICEResourcePage}'s
	 * ResourceComponent can be changed.
	 */
	@Test
	public void checkResourceComponents() {
		// This test can use the shared editor.

		// Create a new ResourceComponent.
		ResourceComponent resources = new ResourceComponent();
		resources.setId(1992);
		resources.setName("First year of Eugenics War");
		resources.setDescription("The Eugenics War started in 1992 and ran "
				+ "until 1996 on Earth. It resulted in the deaths of 36 "
				+ "million people.");

		// Initially, the page's ResourceComponent is the shared
		// ResourceComponent.
		assertSame(sharedResources, sharedPage.getResourceComponent());
		assertNotSame(resources, sharedPage.getResourceComponent());

		// Change it to the new ResourceComponent.
		sharedPage.setResourceComponent(resources);
		assertNotSame(sharedResources, sharedPage.getResourceComponent());
		assertSame(resources, sharedPage.getResourceComponent());

		// Now return it to the original ResourceComponent.
		sharedPage.setResourceComponent(sharedResources);
		assertSame(sharedResources, sharedPage.getResourceComponent());
		assertNotSame(resources, sharedPage.getResourceComponent());

		return;
	}

	/**
	 * Ensures that text files, by default, are opened in a separate text
	 * editor, provided there is no IVizService that handles them.
	 */
	@Test
	public void checkTextEditorFiles() {
		// This test can use the shared editor.

		// These are the extensions for files that should be opened in the
		// default text editor (provided there's no viz service that uses them).
		String[] extensions = new String[] { "txt", "sh", "i", "csv" };

		SWTWorkbenchBot bot = getBot();

		for (String extension : extensions) {
			// Create a temporary ICEResource with the current file extension.
			String filename = "ajensen." + extension;
			File file = null;
			try {
				file = createTemporaryFile(filename, "mandrake");
			} catch (IOException e) {
				fail("ICEResourcePageTester error: "
						+ "Failed to create temporary test file \"" + filename
						+ "\".");
			}
			ICEResource resource = createICEResource(file);

			// Add and show the resource.
			sharedResources.add(resource);
			doubleClickResource(resource);

			// Close the new editor. This has the added bonus of verifying that
			// a text editor was opened.
			bot.editorByTitle(filename).close();

			// Re-activate the ICEFormEditor.
			bot.editor(getExactMatcher(sharedEditorRef)).show();

			// Remove the resource from the Resources View.
			sharedResources.remove(resource);

			// Delete the temporary file (optional, as they are removed after
			// each test).
		}

		return;
	}

	/**
	 * Checks that non-text files that lack an associated IVizService are
	 * rendered in the browser.
	 */
	@Test
	public void checkBrowser() {
		// This test can use the shared editor.

		// Create a temporary ICEResource that should be opened in the
		// ICEResourcePage's browser.
		File file = null;
		try {
			file = createTemporaryFile("dsarif.html", "frneticpny");
		} catch (IOException e) {
			fail("ICEResourcePageTester error: "
					+ "Failed to create temporary test file \"" + "dsarif.html"
					+ "\".");
		}
		ICEResource resource = createICEResource(file);

		// Add and show the resource.
		sharedResources.add(resource);
		doubleClickResource(resource);

		// Verify that the file was opened in the browser.
		SWTBot bot = new SWTBot(sharedPage.getPartControl());

		// Remove the resource from the Resources View.
		sharedResources.remove(resource);

		// Delete the temporary file (optional, as they are removed after each
		// test).

		return;
	}

	/**
	 * Simulates a double-click of the specified resource in the Resources View.
	 *
	 * @param resource
	 *            The resource to click.
	 */
	private void doubleClickResource(ICEResource resource) {
		String resourceName = resource.getName();

		// Activate the Resources View.
		SWTBotView resourcesView = getBot().viewByTitle("Resources");
		resourcesView.show();
		// Find the corresponding resource in the view, then double-click it.
		SWTBotTreeItem node = resourcesView.bot().tree()
				.expandNode(resourceName);
		node.doubleClick();

		return;
	}

	/**
	 * Opens the {@link #page} in a new {@code ICEFormEditor}. After returning,
	 * the following instance variables will be set:
	 * <ul>
	 * <li>{@link #page}</li>
	 * <li>{@link #resources}</li>
	 * <li>{@link #editorRef}</li>
	 * </ul>
	 */
	private void openNewEditor() {
		// Set up the input used to create an editor with an ICEResourcePage.
		Form form = new Form();
		resources = new ResourceComponent();
		form.addComponent(resources);
		// Open the editor and get the ICEResourcePage.
		editorRef = openICEFormEditor(form);
		ICEFormEditor editor = (ICEFormEditor) editorRef.getPart(false);
		page = editor.getResourcePage();
	}

	/**
	 * Closes the {@link #editorRef} and {@link #page}.
	 */
	private void closeEditor() {

		// Close the page/editor.
		getBot().editor(getExactMatcher(editorRef)).close();

		// Unset all of the instance variables.
		page = null;
		editorRef = null;
		resources = null;

		return;
	}

}
