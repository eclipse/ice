/*******************************************************************************
 * Copyright (c) 2012, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jay Jay Billings - Initial API and implementation and/or initial documentation
 *   Jordan H. Deyton - Added SWTBot UI tests.
 *******************************************************************************/
package org.eclipse.ice.client.widgets.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEResourcePage;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.resource.VizResource;
import org.eclipse.ice.viz.service.AbstractVizService;
import org.eclipse.ice.viz.service.BasicVizServiceFactory;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.IVizServiceFactory;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.IEditorReference;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class is responsible for testing the ICEResourcePage.
 * 
 * @author Jay Jay Billings
 */
public class ICEResourcePageTester extends AbstractWorkbenchTester {

	// ---- Resources shared between tests. ---- //
	/**
	 * The {@code ICEResourcePage} that can be shared among these test cases.
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
	 * the {@code ICEFormEditor}'s input.
	 */
	private static ResourceComponent sharedResources;
	// ----------------------------------------- //

	// ---- Resources re-allocated each test. ---- //
	// ------------------------------------------- //

	// ---- Resources re-allocated only when necessary. ---- //
	/**
	 * A factory that contains {@link IVizService}s used by the
	 * {@code ICEResourcePage}.
	 */
	private IVizServiceFactory vizServiceFactory;

	// ----------------------------------------------------- //

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractWorkbenchTester#beforeAllTests
	 * ()
	 */
	@Override
	public void beforeAllTests() {
		super.beforeAllTests();
		// Initialize static or otherwise shared resources here.

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
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractWorkbenchTester#beforeEachTest
	 * ()
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

		ICEResourcePage page;
		Form form;
		ResourceComponent resources;
		IEditorReference editorRef;
		ICEFormEditor editor;

		// Creating a new ICEFormEditor with no ResourceComponent means there
		// should be no ICEResourcePage.
		form = new Form();
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
		resources = new ResourceComponent();
		form = new Form();
		form.addComponent(resources);
		editorRef = openICEFormEditor(form);
		editor = (ICEFormEditor) editorRef.getPart(false);
		page = editor.getResourcePage();
		// In this case, the page is not null, and the ResourceComponent is
		// shared between the page and the form.
		assertNotNull(page);
		assertSame(resources, page.getResourceComponent());
		// Close the editor.
		getBot().editor(getExactMatcher(editorRef)).close();

		return;
	}

	/**
	 * This operation checks that the {@code ICEResourcePage}'s
	 * ResourceComponent can be changed.
	 */
	@Test
	public void checkResourceComponents() {

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
	 * This checks that the {@code ICEResourcePage} properly handles different
	 * resource file types based on the viz services available in its
	 * {@link IVizServiceFactory}.
	 */
	@Test
	public void checkVizChainOfCommand() {

		ICEResource resource;

		// Create a new IVizServiceFactory with fake CSVVizService and
		// TXTVizServices.
		vizServiceFactory = new BasicVizServiceFactory();
		FakeCSVVizService csvVizService = new FakeCSVVizService();
		FakeTXTVizService txtVizService = new FakeTXTVizService();
		vizServiceFactory.register(csvVizService);
		vizServiceFactory.register(txtVizService);
		// Set the shared page's factory.
		sharedPage.setVizService(vizServiceFactory);

		// In the beginning, no plots should have been created.
		assertFalse(csvVizService.createWasCalled.getAndSet(false));
		assertFalse(csvVizService.createCompleted.getAndSet(false));
		assertFalse(txtVizService.createWasCalled.getAndSet(false));
		assertFalse(txtVizService.createCompleted.getAndSet(false));

		// ---- Add a CSV file. ---- //
		// Add a csv file Resource, then show it. The CSVVizService should have
		// attempted to create a plot.
		resource = createFakeVizResource("blah.csv");
		sharedResources.add(resource);
		doubleClickResource(resource);
		// If the txt VizService was queried, it should not have completed.
		if (txtVizService.createWasCalled.getAndSet(false)) {
			assertFalse(txtVizService.createCompleted.getAndSet(false));
		}
		// The csv VizService should have been queried and completed.
		assertTrue(csvVizService.createWasCalled.getAndSet(false));
		assertTrue(csvVizService.createCompleted.getAndSet(false));
		// The plot should have been drawn once.
		assertEquals(1, csvVizService.plots.get(0).getDrawCount());
		// ------------------------- //

		// ---- Add a TXT file. ---- //
		// Add a txt file Resource, then show it. The TXTVizService should have
		// attempted to create a plot.
		resource = createFakeVizResource("blah.txt");
		sharedResources.add(resource);
		doubleClickResource(resource);
		// If the csv VizService was queried, it should not have completed.
		if (csvVizService.createWasCalled.getAndSet(false)) {
			assertFalse(csvVizService.createCompleted.getAndSet(false));
		}
		// The txt VizService should have been queried and completed.
		assertTrue(txtVizService.createWasCalled.getAndSet(false));
		assertTrue(txtVizService.createCompleted.getAndSet(false));
		// The plot should have been drawn once.
		assertEquals(1, txtVizService.plots.get(0).getDrawCount());
		// ------------------------- //

		// ---- Add a CSV file. ---- //
		// Add another csv file Resource, then show it. The CSVVizService should
		// have attempted to create a plot.
		resource = createFakeVizResource("blah2.csv");
		sharedResources.add(resource);
		doubleClickResource(resource);
		// If the txt VizService was queried, it should not have completed.
		if (txtVizService.createWasCalled.getAndSet(false)) {
			assertFalse(txtVizService.createCompleted.getAndSet(false));
		}
		// The csv VizService should have been queried and completed.
		assertTrue(csvVizService.createWasCalled.getAndSet(false));
		assertTrue(csvVizService.createCompleted.getAndSet(false));
		// The plot should have been drawn once.
		assertEquals(1, csvVizService.plots.get(0).getDrawCount());
		// ------------------------- //
		return;
	}

	/**
	 * Checks that when the same resource is double-clicked in the Resources
	 * View, the same editor or plot is opened. In the case of {@code IPlot}s,
	 * the plot should be re-used, although it should be drawn a second time.
	 */
	@Ignore
	@Test
	public void checkReuse() {
		// TODO
	}

	/**
	 * Ensures that text files, by default, are opened in a separate text
	 * editor, provided there is no IVizService that handles them.
	 */
	@Ignore
	@Test
	public void checkTextEditorFiles() {
		// TODO
		// String[] extensions = new String[] { "txt", "sh", "i", "csv" };
		//
		// ICEResource resource;
		//
		// // Create a new IVizServiceFactory with no IVizServices.
		// vizServiceFactory = new BasicVizServiceFactory();
		// sharedPage.setVizService(vizServiceFactory);

		return;
	}

	/**
	 * Checks that non-text files that lack an associated IVizService are
	 * rendered in the browser.
	 */
	@Ignore
	@Test
	public void checkBrowser() {
		// TODO
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
	 * Creates a fake {@link VizResource}. The file may not actually exist, as
	 * this is intended only for testing extension handling.
	 * 
	 * @param filename
	 *            The name of the file, including its extension.
	 * @return A new {@code VizResource}.
	 */
	private VizResource createFakeVizResource(String filename) {
		VizResource vizResource = null;
		File file = new File(filename);
		try {
			vizResource = new VizResource(file);
		} catch (IOException e) {
			fail("ICEResourcePageTester error: "
					+ "Error while attempting to create fake viz resource \""
					+ file.getName() + "\".");
		}
		return vizResource;
	}

	/**
	 * An abstract base class for fake viz services. This just helps eliminate
	 * some empty interface implementations.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private abstract class AbstractFakeVizService extends AbstractVizService {

		public final List<FakePlot> plots = new ArrayList<FakePlot>();

		public final AtomicBoolean createWasCalled = new AtomicBoolean();
		public final AtomicBoolean createCompleted = new AtomicBoolean();

		@Override
		public abstract String getName();

		@Override
		public String getVersion() {
			return null;
		}

		@Override
		public boolean hasConnectionProperties() {
			return false;
		}

		@Override
		public Map<String, String> getConnectionProperties() {
			return null;
		}

		@Override
		public void setConnectionProperties(Map<String, String> props) {
			return;
		}

		@Override
		public boolean connect() {
			return false;
		}

		@Override
		public boolean disconnect() {
			return false;
		}

		@Override
		public IPlot createPlot(URI file) throws Exception {
			// Set the flag that createPlot() was called before proceeding with
			// the default plot creation (which checks the extension).
			createWasCalled.set(true);
			super.createPlot(file);

			// If the extension was valid, create a new FakePlot.
			FakePlot plot = new FakePlot();
			plot.plotTypes.put("category", new String[] { "type" });
			plots.add(plot);
			// Set the flag that the plot was created successfully.
			createCompleted.set(true);

			return plot;
		}
	}

	private class FakeCSVVizService extends AbstractFakeVizService {
		public FakeCSVVizService() {
			supportedExtensions.add("csv");
		}

		@Override
		public IPlot createPlot(URI file) throws Exception {
			// TODO Auto-generated method stub
			return super.createPlot(file);
		}

		@Override
		public String getName() {
			return "Fake CSV Viz Service";
		}
	}

	private class FakeTXTVizService extends AbstractFakeVizService {
		public FakeTXTVizService() {
			supportedExtensions.add("txt");
		}

		@Override
		public String getName() {
			return "Fake Text File Viz Service";
		}
	}
}