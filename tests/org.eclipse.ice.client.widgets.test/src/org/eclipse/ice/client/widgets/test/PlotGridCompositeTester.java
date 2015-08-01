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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.client.widgets.PlotGridComposite;
import org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.csv.CSVSeries;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
 * @author Kasper Gammeltoft - Refactored to conform with <code>IPlot</code>'s
 *         new usage of <code>ISeries</code>.
 *
 */
public class PlotGridCompositeTester extends AbstractSWTTester {

	/**
	 * The shared {@link PlotGridComposite} that will be tested.
	 */
	private static PlotGridComposite composite;

	/**
	 * A non-static, non-shared {@link PlotGridComposite} for testing.
	 */
	private PlotGridComposite testComposite;

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

		// Create the composite that will be tested.
		getDisplay().syncExec(new Runnable() {
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
	 * org.eclipse.ice.client.widgets.test.AbstractUITester#beforeEachTest()
	 */
	@Override
	public void beforeEachTest() {
		super.beforeEachTest();

		// Initialize per-test resources here.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.test.AbstractUITester#afterEachTest()
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
	 * @see org.eclipse.ice.client.widgets.test.AbstractUITester#afterAllTests()
	 */
	@Override
	public void afterAllTests() {
		// Dispose static or otherwise shared resources here.

		// Dispose the composite.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				composite.dispose();
			}
		});
		composite = null;

		// Proceed with the default post-tests cleanup.
		super.afterAllTests();
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

		// Create a new composite for testing.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				testComposite = new PlotGridComposite(getShell(), SWT.NONE);
			}
		});
		PlotGridComposite composite = testComposite;

		SWTBot bot = new SWTBot(composite);
		SWTBotSpinner spinner;
		SWTBotLabel label;
		ToolBar toolBar;

		Control[] children = getChildren(composite);

		// Check the existence of the ToolBar.
		assertTrue(children[0] instanceof ToolBar);
		toolBar = (ToolBar) children[0];
		// Get the contents of the ToolBar.
		children = getChildren(toolBar);

		// Check the order of the row label.
		label = bot.label("Rows:");
		// Note: We have to wrap the label in a Composite for proper alignment.
		assertSame(children[0], getParent(label.widget));

		// Check the order of the row spinner.
		spinner = bot.spinner(0);
		assertSame(children[1], spinner.widget);
		// Check the spinner's specifications.
		assertEquals(1, spinner.getIncrement());
		assertEquals(2, spinner.getSelection());
		assertEquals(1, spinner.getMinimum());
		assertEquals(4, spinner.getMaximum());

		// Check the order of the row label.
		label = bot.label("Columns:");
		// Note: We have to wrap the label in a Composite for proper alignment.
		assertSame(children[2], getParent(label.widget));

		// Check the order of the row spinner.
		spinner = bot.spinner(1);
		assertSame(children[3], spinner.widget);
		// Check the spinner's specifications.
		assertEquals(1, spinner.getIncrement());
		assertEquals(2, spinner.getSelection());
		assertEquals(1, spinner.getMinimum());
		assertEquals(4, spinner.getMaximum());

		// Check that the clear button exists.
		final SWTBotToolbarButton button = bot.toolbarButton("Clear");
		// Check that it is in the PlotGridComposite's ToolBar.
		final AtomicReference<ToolBar> parentToolBar = new AtomicReference<ToolBar>();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				parentToolBar.set(button.widget.getParent());
			}
		});
		assertSame(toolBar, parentToolBar.get());

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
			index = addPlot(plot);
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
			index = addPlot(plot);
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
			index = addPlot(fakePlot2);
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
			index = addPlot(plot);
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "An exception was thrown when adding a valid plot.");
		}
		assertEquals(expectedIndex, index);
		// Make sure its child composite does not exists.
		assertEquals(2, fakePlot.getDrawCount());

		// Clear the plots.
		clearPlots();

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
			assertEquals(-1, addPlot(plot));
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "Exception thrown when adding null plot. "
					+ "Should just return -1.");
		}

		// Check addPlot(...) when getPlotTypes() is empty.
		fakePlot = new FakePlot();
		plot = fakePlot;
		try {
			assertEquals(-1, addPlot(plot));
		} catch (Exception e) {
			fail("PlotGridCompositeTester error: "
					+ "Exception thrown when adding plot with empty plot type map. "
					+ "Should just return -1.");
		}
		// Make sure the plot's draw(...) method wasn't called.
		assertEquals(0, fakePlot.getDrawCount());

		// Check addPlot(...) when draw(...) throws an exception.
		fakePlot = new FakePlot() {
			@Override
			public Composite draw(Composite parent) throws Exception {
				// Draw something. This should create a new Composite.
				super.draw(parent);
				// Throw an exception now after a Composite was created.
				Exception e = new Exception("nunchaku");
				eRef.set(e);
				throw e;
			}
		};
		// The plot needs to have some types...
		fakePlot.addDependentSeries("preferred", getSeries("katana"));
		fakePlot.addDependentSeries("preferred", getSeries("sai"));
		fakePlot.addDependentSeries("preferred", getSeries("bo staff"));
		fakePlot.addDependentSeries("preferred", getSeries("nunchaku"));

		plot = fakePlot;
		try {
			addPlot(plot);
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
			index = addPlot(plot);
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
			index = addPlot(plot);
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
			index = addPlot(fakePlot2);
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
		removePlot(1);
		assertTrue(fakePlot.children.get(1).isDisposed());
		// ------------------------------------------------------------ //

		// ---- Add another plot. ---- //
		// Try to add another plot.
		expectedIndex = 2;
		try {
			index = addPlot(fakePlot2);
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
		removePlot(2);
		assertTrue(fakePlot2.children.get(1).isDisposed());
		// Then remove the first one.
		removePlot(0);
		assertTrue(fakePlot.children.get(0).isDisposed());
		// THen remove the "middle" one (now the only one left).
		removePlot(0);
		assertTrue(fakePlot2.children.get(0).isDisposed());
		// ------------------------------------ //

		// Trying to remove any more should do nothing.
		removePlot(0);
		removePlot(-1);

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
			index = addPlot(plot);
		} catch (Exception e) {
			e.printStackTrace();
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
			index = addPlot(plot);
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
			index = addPlot(fakePlot2);
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
		removePlots(plot);
		assertTrue(fakePlot.children.get(0).isDisposed());
		assertTrue(fakePlot.children.get(1).isDisposed());
		// ----------------------------------------------------- //

		// ---- Remove the remaining plots. ---- //
		removePlots(fakePlot2);
		assertTrue(fakePlot.children.get(0).isDisposed());
		// ------------------------------------- //

		// Trying to remove any more should do nothing.
		removePlots(plot);
		removePlots(fakePlot2);
		removePlots(nullPlot);

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
			index = addPlot(plot);
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
			index = addPlot(fakePlot2);
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
		clearPlots();

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
			index = addPlot(plot);
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
			index = addPlot(fakePlot2);
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
			index = addPlot(plot);
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
			index = addPlot(plot);
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
			index = addPlot(fakePlot2);
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
		clearPlots();

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
	 * A wrapper around {@link PlotGridComposite#addPlot(IPlot)} that uses
	 * {@code syncExec(...)} to perform the operation on the UI thread.
	 * 
	 * @throws Exception
	 *             This catches the Exception (if one occurs) on the UI thread
	 *             and throws it on the test thread.
	 */
	private int addPlot(final IPlot plot) throws Exception {
		final AtomicInteger index = new AtomicInteger();
		final AtomicReference<Exception> eRef = new AtomicReference<Exception>();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					int i = composite.addPlot(plot);
					index.set(i);
				} catch (Exception e) {
					eRef.set(e);
				}
			}
		});
		Exception e = eRef.get();
		if (e != null) {
			throw e;
		}
		return index.get();
	}

	/**
	 * Convince method to create a new series with the specified name
	 * 
	 * @param lbl
	 *            The new label or name for the series
	 * @return Returns a new series with the given label or name
	 */
	private CSVSeries getSeries(String lbl) {
		CSVSeries series = new CSVSeries();
		series.setLabel(lbl);
		return series;
	}

	/**
	 * A wrapper around {@link PlotGridComposite#removePlot(int)} that uses
	 * {@code syncExec(...)} to perform the operation on the UI thread.
	 */
	private void removePlot(final int index) {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				composite.removePlot(index);
			}
		});
	}

	/**
	 * A wrapper around {@link PlotGridComposite#removePlots(IPlot))} that uses
	 * {@code syncExec(...)} to perform the operation on the UI thread.
	 */
	private void removePlots(final IPlot plot) {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				composite.removePlots(plot);
			}
		});
	}

	/**
	 * A wrapper around {@link PlotGridComposite#clearPlots()} that uses
	 * {@code syncExec(...)} to perform the operation on the UI thread.
	 */
	private void clearPlots() {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				composite.clearPlots();
			}
		});
	}

	/**
	 * Returns true if the child is in the ancestor's tree of child composites.
	 * The arguments are assumed not to be null.
	 */
	private boolean childOfComposite(final Composite child,
			final Composite ancestor) {
		final AtomicBoolean isDescendant = new AtomicBoolean();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				Composite parent = child.getParent();
				while (parent != null && parent != ancestor) {
					parent = parent.getParent();
				}
				isDescendant.set(ancestor != null && parent == ancestor);
			}
		});
		return isDescendant.get();
	}

	/**
	 * Creates a FakePlot that will draw something and has a set of plot types.
	 */
	private FakePlot createValidPlot() {
		FakePlot plot = new FakePlot();
		// Add some random series with different categories to the plot
		plot.addDependentSeries("tmnt", getSeries("leo"));
		plot.addDependentSeries("tmnt", getSeries("donnie"));
		plot.addDependentSeries("tmnt", getSeries("raph"));
		plot.addDependentSeries("tmnt", getSeries("mikey"));

		plot.addDependentSeries("joes", getSeries("duke"));
		plot.addDependentSeries("joes", getSeries("snake eyes"));
		plot.addDependentSeries("joes", getSeries("sgt. slaughter"));
		plot.addDependentSeries("joes", getSeries("roadblock"));
		plot.addDependentSeries("joes", getSeries("stalker"));

		plot.addDependentSeries("autobots", getSeries("optimus"));
		plot.addDependentSeries("autobots", getSeries("bumblebee"));
		plot.addDependentSeries("autobots", getSeries("jazz"));
		plot.addDependentSeries("autobots", getSeries("ironhide"));

		plot.setIndependentSeries(getSeries("timeRating"));

		return plot;
	}

	/**
	 * Gets the parent {@code Composite} for the specified {@code Control}.
	 * 
	 * @param control
	 *            The control whose parent will be found.
	 * @return The parent of the control. May be {@code null} if it has no
	 *         parent.
	 */
	private Composite getParent(final Control control) {
		final AtomicReference<Composite> parentRef = new AtomicReference<Composite>();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				parentRef.set(control.getParent());
			}
		});
		return parentRef.get();
	}

	/**
	 * Gets the array of children from the specified {@code Composite}. This
	 * must be done on the UI thread.
	 * 
	 * @param composite
	 *            The composite whose children are to be found.
	 * @return The array of children.
	 */
	private Control[] getChildren(final Composite composite) {
		final AtomicReference<Control[]> arrayRef = new AtomicReference<Control[]>();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				arrayRef.set(composite.getChildren());
			}
		});
		return arrayRef.get();
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
}