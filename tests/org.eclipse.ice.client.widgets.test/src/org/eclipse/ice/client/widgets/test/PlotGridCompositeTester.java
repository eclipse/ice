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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.client.widgets.PlotGridComposite;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotSpinner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class tests the {@link PlotGridComposite}'s UI features.
 * 
 * @author Jordan Deyton
 *
 */
public class PlotGridCompositeTester extends AbstractSWTTester {

	/**
	 * The shared {@link PlotGridComposite} that will be tested.
	 */
	private PlotGridComposite composite;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractSWTTester#createTestResources
	 * (org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void createTestResources(Shell shell) {
		// Create the composite that will be tested.
		shell.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				composite = new PlotGridComposite(getShell(), SWT.NONE);
			}
		});

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractSWTTester#disposeTestResources
	 * ()
	 */
	@Override
	protected void disposeTestResources() {
		// Dispose the composite.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				composite.dispose();
			}
		});
		composite = null;

		return;
	}

	/**
	 * Checks that the ToolBar properly contains these widgets:
	 * <ol>
	 * <li>The "Rows:" label.</li>
	 * <li>The rows spinner.</li>
	 * <li>The "Columns:" label.</li>
	 * <li>The columns spinner.</li>
	 * <li>The "Clear" button (no restriction on where it is located).</li>
	 * </ol>
	 */
	@Test
	public void checkToolBar() {

		SWTBot bot = getBot();
		SWTBotSpinner spinner;
		SWTBotLabel label;
		SWTBotToolbarButton button;
		ToolBar toolBar;

		// Check the existence of the ToolBar.
		assertTrue(composite.getChildren()[0] instanceof ToolBar);
		toolBar = (ToolBar) composite.getChildren()[0];

		// Check the order of the row label.
		label = bot.label("Rows:");
		// Note: We have to wrap the label in a Composite for proper alignment.
		assertSame(toolBar.getChildren()[0], label.widget.getParent());

		// Check the order of the row spinner.
		spinner = getRowSpinner();
		assertSame(toolBar.getChildren()[1], spinner.widget);
		// Check the spinner's specifications.
		assertEquals(1, spinner.getIncrement());
		assertEquals(2, spinner.getSelection());
		assertEquals(1, spinner.getMinimum());
		assertEquals(4, spinner.getMaximum());

		// Check the order of the row label.
		label = bot.label("Columns:");
		// Note: We have to wrap the label in a Composite for proper alignment.
		assertSame(toolBar.getChildren()[2], label.widget.getParent());

		// Check the order of the row spinner.
		spinner = getColumnSpinner();
		assertSame(toolBar.getChildren()[3], spinner.widget);
		// Check the spinner's specifications.
		assertEquals(1, spinner.getIncrement());
		assertEquals(2, spinner.getSelection());
		assertEquals(1, spinner.getMinimum());
		assertEquals(4, spinner.getMaximum());

		// Check that the clear button exists.
		button = bot.toolbarButton("Clear");
		assertSame(toolBar, button.widget.getParent());

		return;
	}

	/**
	 * Checks that the {@link PlotGridComposite#addPlot(IPlot)} method works for
	 * valid plots and only returns -1 when it is full.
	 */
	@Test
	public void checkAddPlot() {

		// Makes the grid 1x3, and tries to add 4 valid plots. The 4th plot
		// cannot be added because the grid is full.

		FakePlot fakePlot;
		FakePlot fakePlot2;
		IPlot plot;
		int index = -1;
		int expectedIndex = -1;

		// Make the grid 1x3.
		getRowSpinner().setSelection(1);
		getColumnSpinner().setSelection(3);

		// Create a plot to test adding to the composite.
		fakePlot = createValidPlot();
		fakePlot2 = createValidPlot();
		plot = fakePlot;

		// Add a plot. It should get the index 0.
		expectedIndex = 0;
		try {
			index = composite.addPlot(plot);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite exists.
		assertEquals(1, fakePlot.getDrawCount());
		assertTrue(childOfComposite(fakePlot.children.get(0), composite));

		// Add another plot. It should get the index 1.
		expectedIndex = 1;
		try {
			index = composite.addPlot(plot);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite exists.
		assertEquals(2, fakePlot.getDrawCount());
		assertTrue(childOfComposite(fakePlot.children.get(1), composite));

		// Try to add another plot.
		expectedIndex = 2;
		try {
			index = composite.addPlot(fakePlot2);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		assertEquals(1, fakePlot2.getDrawCount());
		assertTrue(childOfComposite(fakePlot2.children.get(0), composite));

		// Try to add another plot. It shouldn't be added.
		expectedIndex = -1;
		try {
			index = composite.addPlot(plot);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite does not exists.
		assertEquals(2, fakePlot.getDrawCount());

		// Clear the plots.
		composite.clearPlots();

		return;
	}

	/**
	 * Checks that exceptions are properly handled by
	 * {@link PlotGridComposite#addPlot(IPlot)}.
	 */
	@Test
	public void checkAddPlotExceptions() {
		// addPlot(null) should return -1.
		// addPlot(...) when getPlotTypes() is null should return -1.
		// addPlot(...) when getPlotTypes() is empty should return -1.
		// addPlot(...) should throw getPlotTypes()'s exceptions.
		// addPlot(...) should throw draw(...)'s exceptions.

		FakePlot fakePlot;
		IPlot plot;
		final IPlot nullPlot = null;
		final AtomicReference<Exception> eRef = new AtomicReference<Exception>();

		// Check addPlot(null).
		plot = nullPlot;
		try {
			assertEquals(-1, composite.addPlot(plot));
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "Exception thrown when adding null plot. "
					+ "Should just return -1.");
		}

		// Check addPlot(...) when getPlotTypes() is null.
		fakePlot = new FakePlot() {
			@Override
			public Map<String, String[]> getPlotTypes() throws Exception {
				return null;
			}
		};
		plot = fakePlot;
		try {
			assertEquals(-1, composite.addPlot(plot));
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "Exception thrown when adding plot with null plot type map. "
					+ "Should just return -1.");
		}
		// Make sure the plot's draw(...) method wasn't called.
		assertEquals(0, fakePlot.getDrawCount());

		// Check addPlot(...) when getPlotTypes() is empty.
		fakePlot = new FakePlot();
		plot = fakePlot;
		try {
			assertEquals(-1, composite.addPlot(plot));
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "Exception thrown when adding plot with empty plot type map. "
					+ "Should just return -1.");
		}
		// Make sure the plot's draw(...) method wasn't called.
		assertEquals(0, fakePlot.getDrawCount());

		// Check addPlot(...) when getPlotTypes() throws an exception.
		fakePlot = new FakePlot() {
			@Override
			public Map<String, String[]> getPlotTypes() throws Exception {
				Exception e = new Exception("katana");
				eRef.set(e);
				throw e;
			}
		};
		plot = fakePlot;
		try {
			composite.addPlot(plot);
			fail("PlotGridCompositeTester error: "
					+ "Exception from getPlotTypes() was not relayed when adding plot.");
		} catch (Exception e) {
			// Make sure it's the same exception thrown from getPlotTypes().
			assertSame(eRef.get(), e);
		}
		// Make sure the plot's draw(...) method wasn't called.
		assertEquals(0, fakePlot.getDrawCount());

		// Check addPlot(...) when draw(...) throws an exception.
		fakePlot = new FakePlot() {
			@Override
			public Composite draw(String category, String plotType,
					Composite parent) throws Exception {
				// Draw something. This should create a new Composite.
				super.draw(category, plotType, parent);
				// Throw an exception now after a Composite was created.
				Exception e = new Exception("nunchaku");
				eRef.set(e);
				throw e;
			}
		};
		// The plot needs to have some types...
		fakePlot.plotTypes.put("preferred", new String[] { "katana", "sai",
				"bo staff", "nunchaku" });
		plot = fakePlot;
		try {
			composite.addPlot(plot);
			fail("PlotGridCompositeTester error: "
					+ "Exception from draw(...) was not relayed when adding plot.");
		} catch (Exception e) {
			assertSame(eRef.get(), e);
		}
		// Make sure the plot's draw(...) method was called, but its child is
		// disposed by addPlot(...).
		assertEquals(1, fakePlot.getDrawCount());
		assertTrue(fakePlot.children.get(0).isDisposed());

		return;
	}

	/**
	 * Checks that the {@link PlotGridComposite#removePlot(int)} method works
	 * for valid indices.
	 */
	@Test
	public void checkRemovePlot() {

		// Make the grid 1x3.
		// Add 3 plots (two from the same IPlot) so that no more can be added.
		// Remove one of the two plots from the same IPlot.
		// We can now add another plot.
		// Remove all plots by index.

		FakePlot fakePlot;
		FakePlot fakePlot2;
		IPlot plot;
		int index = -1;
		int expectedIndex = -1;

		// Make the grid 1x3.
		getRowSpinner().setSelection(1);
		getColumnSpinner().setSelection(3);

		// Create a plot to test adding to the composite.
		fakePlot = createValidPlot();
		fakePlot2 = createValidPlot();
		plot = fakePlot;

		// ---- Add the three plots. ---- //
		// Add a plot. It should get the index 0.
		expectedIndex = 0;
		try {
			index = composite.addPlot(plot);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite exists.
		assertEquals(1, fakePlot.getDrawCount());
		assertTrue(childOfComposite(fakePlot.children.get(0), composite));

		// Add another plot. It should get the index 1.
		expectedIndex = 1;
		try {
			index = composite.addPlot(plot);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite exists.
		assertEquals(2, fakePlot.getDrawCount());
		assertTrue(childOfComposite(fakePlot.children.get(1), composite));

		// Try to add another plot.
		expectedIndex = 2;
		try {
			index = composite.addPlot(fakePlot2);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		assertEquals(1, fakePlot2.getDrawCount());
		assertTrue(childOfComposite(fakePlot2.children.get(0), composite));

		// No more plots can be added.
		// ------------------------------ //

		// ---- Remove one of the two plots with the shared IPlot. ---- //
		// Remove the middle plot and make sure its drawn child is disposed.
		composite.removePlot(1);
		assertTrue(fakePlot.children.get(1).isDisposed());
		// ------------------------------------------------------------ //

		// ---- Add another plot. ---- //
		// Try to add another plot.
		expectedIndex = 2;
		try {
			index = composite.addPlot(fakePlot2);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		assertEquals(2, fakePlot2.getDrawCount());
		assertTrue(childOfComposite(fakePlot2.children.get(1), composite));
		// --------------------------- //

		// ---- Remove all plots by index. ---- //
		// Remove the last one first,
		composite.removePlot(2);
		assertTrue(fakePlot2.children.get(1).isDisposed());
		// Then remove the first one.
		composite.removePlot(0);
		assertTrue(fakePlot.children.get(0).isDisposed());
		// THen remove the "middle" one (now the only one left).
		composite.removePlot(0);
		assertTrue(fakePlot2.children.get(0).isDisposed());
		// ------------------------------------ //

		// Trying to remove any more should do nothing.
		composite.removePlot(0);
		composite.removePlot(-1);

		return;
	}

	/**
	 * Checks that the {@link PlotGridComposite#removePlots(IPlot)} method
	 * properly removes all children associated with the specified plot.
	 */
	@Test
	public void checkRemovePlots() {

		// Make the grid 1x3.
		// Add 3 plots (two from the same IPlot) so that no more can be added.
		// Remove the two plots from the same IPlot.
		// Remove all plots by IPlot.

		FakePlot fakePlot;
		FakePlot fakePlot2;
		IPlot plot;
		int index = -1;
		int expectedIndex = -1;
		final IPlot nullPlot = null;

		// Make the grid 1x3.
		getRowSpinner().setSelection(1);
		getColumnSpinner().setSelection(3);

		// Create a plot to test adding to the composite.
		fakePlot = createValidPlot();
		fakePlot2 = createValidPlot();
		plot = fakePlot;

		// ---- Add the three plots. ---- //
		// Add a plot. It should get the index 0.
		expectedIndex = 0;
		try {
			index = composite.addPlot(plot);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite exists.
		assertEquals(1, fakePlot.getDrawCount());
		assertTrue(childOfComposite(fakePlot.children.get(0), composite));

		// Add another plot. It should get the index 1.
		expectedIndex = 1;
		try {
			index = composite.addPlot(plot);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite exists.
		assertEquals(2, fakePlot.getDrawCount());
		assertTrue(childOfComposite(fakePlot.children.get(1), composite));

		// Try to add another plot.
		expectedIndex = 2;
		try {
			index = composite.addPlot(fakePlot2);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		assertEquals(1, fakePlot2.getDrawCount());
		assertTrue(childOfComposite(fakePlot2.children.get(0), composite));

		// No more plots can be added.
		// ------------------------------ //

		// ---- Remove the two plots with the shared IPlot. ---- //
		composite.removePlots(plot);
		assertTrue(fakePlot.children.get(0).isDisposed());
		assertTrue(fakePlot.children.get(1).isDisposed());
		// ----------------------------------------------------- //

		// ---- Remove the remaining plots. ---- //
		composite.removePlots(fakePlot2);
		assertTrue(fakePlot.children.get(0).isDisposed());
		// ------------------------------------- //

		// Trying to remove any more should do nothing.
		composite.removePlots(plot);
		composite.removePlots(fakePlot2);
		composite.removePlots(nullPlot);

		return;
	}

	/**
	 * Checks that the {@link PlotGridComposite#clearPlots()} method works.
	 */
	@Test
	public void checkClearPlots() {

		// Add a couple of plots, then clear. Make sure the plots were disposed.

		FakePlot fakePlot;
		FakePlot fakePlot2;
		IPlot plot;
		int index = -1;
		int expectedIndex = -1;

		// Make the grid 1x3.
		getRowSpinner().setSelection(1);
		getColumnSpinner().setSelection(3);

		// Create some test plots.
		fakePlot = createValidPlot();
		fakePlot2 = createValidPlot();
		plot = fakePlot;

		// Reset the rows and columns to 2x2.
		getRowSpinner().setSelection(2);
		getColumnSpinner().setSelection(2);

		// ---- Add two plots. ---- //
		// Add a plot. It should get the index 0.
		expectedIndex = 0;
		try {
			index = composite.addPlot(plot);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite exists.
		assertEquals(1, fakePlot.getDrawCount());
		assertTrue(childOfComposite(fakePlot.children.get(0), composite));

		// Add another plot. It should get the index 1.
		expectedIndex = 1;
		try {
			index = composite.addPlot(fakePlot2);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite exists.
		assertEquals(1, fakePlot2.getDrawCount());
		assertTrue(childOfComposite(fakePlot2.children.get(0), composite));
		// ------------------------ //

		// Clear the plots.
		composite.clearPlots();

		// Make sure the children are disposed.
		assertTrue(fakePlot.children.get(0).isDisposed());
		assertTrue(fakePlot2.children.get(0).isDisposed());

		return;
	}

	/**
	 * Checks that the "Clear" button in the ToolBar works.
	 */
	@Test
	public void checkClearPlotsButton() {

		// Add a couple of plots, then clear. Make sure the plots were disposed.

		FakePlot fakePlot;
		FakePlot fakePlot2;
		IPlot plot;
		int index = -1;
		int expectedIndex = -1;

		// Make the grid 1x3.
		getRowSpinner().setSelection(1);
		getColumnSpinner().setSelection(3);

		// Create some test plots.
		fakePlot = createValidPlot();
		fakePlot2 = createValidPlot();
		plot = fakePlot;

		// Reset the rows and columns to 2x2.
		getRowSpinner().setSelection(2);
		getColumnSpinner().setSelection(2);

		// ---- Add two plots. ---- //
		// Add a plot. It should get the index 0.
		expectedIndex = 0;
		try {
			index = composite.addPlot(plot);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite exists.
		assertEquals(1, fakePlot.getDrawCount());
		assertTrue(childOfComposite(fakePlot.children.get(0), composite));

		// Add another plot. It should get the index 1.
		expectedIndex = 1;
		try {
			index = composite.addPlot(fakePlot2);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite exists.
		assertEquals(1, fakePlot2.getDrawCount());
		assertTrue(childOfComposite(fakePlot2.children.get(0), composite));
		// ------------------------ //

		// Clear the plots by clicking to ToolBar button.
		getBot().toolbarButton("Clear").click();

		// Make sure the children are disposed.
		assertTrue(fakePlot.children.get(0).isDisposed());
		assertTrue(fakePlot2.children.get(0).isDisposed());

		return;
	}

	/**
	 * Checks that drawn plots are removed when the dimensions of the grid are
	 * too small to display existing plots.
	 */
	@Test
	public void checkTrimmingPlots() {
		// Make the grid 3x1.
		// Add 3 plots.
		// Make the grid 2x1. The third plot should be disposed.

		FakePlot fakePlot;
		FakePlot fakePlot2;
		IPlot plot;
		int index = -1;
		int expectedIndex = -1;

		// Make the grid 3x1.
		getRowSpinner().setSelection(3);
		getColumnSpinner().setSelection(1);

		// ---- Add 3 plots to fill the grid. ---- //
		// Create the plots.
		fakePlot = createValidPlot();
		fakePlot2 = createValidPlot();
		plot = fakePlot;

		// Add a plot. It should get the index 0.
		expectedIndex = 0;
		try {
			index = composite.addPlot(plot);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite exists.
		assertEquals(1, fakePlot.getDrawCount());
		assertTrue(childOfComposite(fakePlot.children.get(0), composite));

		// Add another plot. It should get the index 1.
		expectedIndex = 1;
		try {
			index = composite.addPlot(plot);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite exists.
		assertEquals(2, fakePlot.getDrawCount());
		assertTrue(childOfComposite(fakePlot.children.get(1), composite));

		// Try to add another plot.
		expectedIndex = 2;
		try {
			index = composite.addPlot(fakePlot2);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		assertEquals(1, fakePlot2.getDrawCount());
		assertTrue(childOfComposite(fakePlot2.children.get(0), composite));
		// --------------------------------------- //

		// Make the grid 2x1.
		getRowSpinner().setSelection(2);

		// The third plot should have been disposed.
		assertTrue(fakePlot2.children.get(0).isDisposed());

		// Clear the plots.
		composite.clearPlots();

		return;
	}

	/**
	 * Checks that the default context menu is provided and includes the
	 * following:
	 * <ol>
	 * <li>Remove - Removes the drawn plot.</li>
	 * <li>Set Plot Type - Contains sub-menus for the plot categories and types.
	 * </li>
	 * </ol>
	 */
	@Ignore
	@Test
	public void checkContextMenu() {
		// TODO Figure out how to handle a generic, dynamic context menu.
		// There does not yet appear to be a way to do this built into SWTBot.
	}

	/**
	 * Checks that the close button works when the mouse hovers over a plot.
	 */
	@Ignore
	@Test
	public void checkCloseButton() {
		// TODO Figure out how to do this. There does not appear to be a way to
		// "hover" the mouse over a given Composite (in fact, there is no
		// Composite-related functionality in SWTBot that I am aware of).
	}

	/**
	 * Returns true if the child is in the ancestor's tree of child composites.
	 * The arguments are assumed not to be null.
	 */
	private boolean childOfComposite(Composite child, Composite ancestor) {
		Composite parent = child.getParent();
		while (parent != null && parent != ancestor) {
			parent = parent.getParent();
		}
		return ancestor != null && parent == ancestor;
	}

	/**
	 * Creates a FakePlot that will draw something and has a set of plot types.
	 */
	private FakePlot createValidPlot() {
		FakePlot plot = new FakePlot();
		plot.plotTypes.put("tmnt", new String[] { "leo", "donnie", "raph",
				"mikey" });
		plot.plotTypes.put("joes", new String[] { "duke", "snake eyes",
				"sgt. slaughter", "roadblock", "stalker" });
		plot.plotTypes.put("autobots", new String[] { "optimus", "bumblebee",
				"jazz", "ironhide" });
		return plot;
	}

	/**
	 * Gets the SWTBot-wrapped spinner for the number of rows in the plot grid.
	 */
	private SWTBotSpinner getRowSpinner() {
		return getBot().spinner(0);
	}

	/**
	 * Gets the SWTBot-wrapped spinner for the number of columns in the plot
	 * grid.
	 */
	private SWTBotSpinner getColumnSpinner() {
		return getBot().spinner(1);
	}

	/**
	 * A simple {@link IPlot} implementation for testing the plot grid.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class FakePlot implements IPlot {

		/**
		 * The map of plot types. This will not be populated with anything by
		 * default.
		 */
		public final Map<String, String[]> plotTypes = new HashMap<String, String[]>();

		/**
		 * A list of all child composites created when
		 * {@link #draw(String, String, Composite)} is called.
		 */
		public final List<Composite> children = new ArrayList<Composite>();

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ice.viz.service.IPlot#getPlotTypes()
		 */
		@Override
		public Map<String, String[]> getPlotTypes() throws Exception {
			return plotTypes;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ice.viz.service.IPlot#draw(java.lang.String,
		 * java.lang.String, org.eclipse.swt.widgets.Composite)
		 */
		@Override
		public Composite draw(String category, String plotType, Composite parent)
				throws Exception {
			Composite child = new Composite(parent, SWT.NONE);
			children.add(child);
			child.setMenu(parent.getMenu());
			return child;
		}

		/**
		 * Gets the number of times that
		 * {@link #draw(String, String, Composite)} was called.
		 */
		public int getDrawCount() {
			return children.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ice.viz.service.IPlot#getNumberOfAxes()
		 */
		@Override
		public int getNumberOfAxes() {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ice.viz.service.IPlot#getProperties()
		 */
		@Override
		public Map<String, String> getProperties() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ice.viz.service.IPlot#setProperties(java.util.Map)
		 */
		@Override
		public void setProperties(Map<String, String> props) throws Exception {
			// Do nothing.
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ice.viz.service.IPlot#getDataSource()
		 */
		@Override
		public URI getDataSource() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ice.viz.service.IPlot#getSourceHost()
		 */
		@Override
		public String getSourceHost() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ice.viz.service.IPlot#isSourceRemote()
		 */
		@Override
		public boolean isSourceRemote() {
			return false;
		}
	}
}