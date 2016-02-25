/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith - Initial API and implementation and/or initial documentation
 *   Kasper Gammeltoft - viz series refactor
 *   Jordan Deyton - multi-series refactor, code cleanup
 *******************************************************************************/
package org.eclipse.eavp.viz.service;

import java.net.URI;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.eavp.viz.service.widgets.PlotDialogProvider;
import org.eclipse.eavp.viz.service.widgets.TreeSelectionDialogProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a plot editor. It takes as input a FileInput containing
 * a file of visualization data. It can make use of any VizService registered to
 * the BasicVizServiceFactory to create the plot. It can prompt the user in case
 * there are multiple applicable services for the file. The UI contains options
 * to redraw the plot using the IPlot's provided list of series. The IPlot's
 * provided context menu is also available to the user.
 * 
 * @author Robert Smith
 * @author Kasper Gammeltoft- Viz refactor for series
 *
 */
public class PlotEditor extends MultiPageEditorPart {
	/**
	 * Plot editor ID for external reference.
	 */
	public static final String ID = "org.eclipse.eavp.viz.service.PlotEditor";

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(PlotEditor.class);

	/**
	 * The plot acquired from the input, or {@code null} if it could not be
	 * created with an appropriate viz service.
	 */
	private IPlot plot;

	/**
	 * The Composite in which the plot was drawn.
	 */
	private Composite plotComposite;

	/**
	 * Whether the load process should be cancelled. Triggered by canceling the
	 * {@link #loadJob}.
	 */
	private boolean shouldCancelLoading;

	/**
	 * Creates the main content for the PlotEditor's UI. This includes a ToolBar
	 * above the plot's drawing.
	 * <p>
	 * <b>Note:</b> This method lays out the specified parent composite and thus
	 * does not return any values.
	 * </p>
	 * 
	 * @param parent
	 *            The parent Composite to contain the UI.
	 */
	private void createContent(Composite parent) {

		// Set up the layout of the parent. Although normally the parent's
		// layout is already set, in this case the parent is the same one passed
		// into createFormContent(...), but its layout was not already set.
		GridLayout grid = new GridLayout();
		grid.marginHeight = 0;
		grid.marginWidth = 0;
		parent.setLayout(grid);

		// Create a ToolBar.
		ToolBar toolBar = createToolBar(parent);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Create the plot content.
		plotComposite = null;
		try {
			plotComposite = getPlot().draw(parent);
			plotComposite.setLayoutData(
					new GridData(SWT.FILL, SWT.FILL, true, true));
		} catch (Exception e) {
			throwCriticalException("Error encountered while drawing plot.",
					"The selection could not be rendered by the selected "
							+ "visualization service. Please check the format "
							+ "of the file.",
					e);
		}

		parent.layout();

		return;
	}

	/**
	 * This method creates the main ToolBar for the editor.
	 * 
	 * @param parent
	 *            The parent Composite that will hold the ToolBar.
	 * @return The new ToolBar widget.
	 */
	private ToolBar createToolBar(Composite parent) {
		// Create a ToolBarManager.
		final ToolBarManager toolBarManager = new ToolBarManager();

		// Create a provider that handles creating a tree based on the plot's
		// categories and dependent series.
		final TreeSelectionDialogProvider provider = new TreeSelectionDialogProvider() {
			@Override
			public Object[] getChildren(Object parent) {
				final Object[] children;
				IPlot plot = getPlot();
				// If the element is the plot itself, return either its
				// categories or, if there is only one category, its list of
				// series.
				if (parent == plot) {
					List<String> categories = plot.getCategories();
					if (categories.size() > 1) {
						children = categories.toArray();
					} else if (!categories.isEmpty()) {
						children = plot.getDependentSeries(categories.get(0))
								.toArray();
					} else {
						children = new Object[0];
					}
				}
				// If the element is a category, return its associated series.
				else if (parent instanceof String) {
					children = plot.getDependentSeries(parent.toString())
							.toArray();
				} else {
					children = new Object[0];
				}
				return children;
			}

			@Override
			public String getText(Object element) {
				// Get the text from the series' label.
				final String text;
				if (element instanceof ISeries) {
					text = ((ISeries) element).getLabel();
				} else {
					text = element.toString();
				}
				return text;
			}

			@Override
			public boolean isSelected(Object element) {
				// Only series should be checked/enabled.
				final boolean selected;
				if (element instanceof ISeries) {
					selected = ((ISeries) element).isEnabled();
				} else {
					selected = false;
				}
				return selected;
			}
		};
		provider.setTitle("Select a series");
		provider.setMessage("Please select a series to plot.");

		// Add an action to set what series are plotted.
		toolBarManager.add(new Action("Select series...") {
			@Override
			public void run() {
				// Get the parent composite in which the plot was drawn.
				Composite parent = getPlotComposite().getParent();
				IPlot plot = getPlot();

				// Open the dialog. Only allow one selection at a time.
				Shell shell = parent.getShell();
				if (provider.openDialog(shell, plot, false) == Window.OK) {
					// Disable all de-selected series.
					for (Object element : provider
							.getUnselectedLeafElements()) {
						if (element instanceof ISeries) {
							((ISeries) element).setEnabled(false);
						}
					}
					// Enable all newly selected series.
					for (Object element : provider.getSelectedLeafElements()) {
						if (element instanceof ISeries) {
							((ISeries) element).setEnabled(true);
						}
					}
					// Refresh the plot.
					try {
						plot.draw(parent);
					} catch (Exception e) {
						logger.error(getClass().getName() + " Exception! Error "
								+ "while refreshing the plot. ", e);
					}
				}
				return;
			}
		});

		// An action to close the current editor window
		toolBarManager.add(new Action("Close") {
			@Override
			public void run() {
				getEditorSite().getPage().closeEditor(PlotEditor.this, false);
			}
		});

		// Create and return the ToolBar widget.
		return toolBarManager.createControl(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// Nothing to do.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// Nothing to do.
	}

	/**
	 * Gets the current plot created from a viz service.
	 * 
	 * @return The current plot.
	 */
	private IPlot getPlot() {
		return plot;
	}

	/**
	 * Gets the Composite created by the plot.
	 * 
	 * @return The plot's Composite.
	 */
	private Composite getPlotComposite() {
		return plotComposite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// Nothing to do.
	}

	/**
	 * This method logs an error and prints out a (hopefully helpful) dialog to
	 * the user in an error dialog before closing the editor.
	 * 
	 * @param logMessage
	 *            The message to print to the log.
	 * @param dialogMessage
	 *            The message to show to the user.
	 * @param e
	 *            The exception, or {@code null} if not available.
	 */
	private void throwCriticalException(String logMessage, String dialogMessage,
			Exception e) {
		// Log an error.
		logger.error(getClass().getName() + " Exception! " + logMessage, e);

		final Shell shell = getEditorSite().getShell();

		// Print out an error dialog.
		final Status status = new Status(IStatus.ERROR, "org.eclipse.ice", 0,
				logMessage, e);
		final String message = dialogMessage;
		shell.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				// Open an error dialog.
				ErrorDialog.openError(shell, "Visualization Failed", message,
						status);
				// Close the editor.
				getEditorSite().getPage().closeEditor(PlotEditor.this, false);
			};
		});

		return;
	}

	/**
	 * Waits for the plot to have some available data. Returns true if the plot
	 * has data available before or at the end of the timeout.
	 * 
	 * @param timeout
	 *            The maximum period to wait for the plot to load.
	 * @return True if the plot has data available, false otherwise.
	 */
	private boolean waitForLoad(long timeout) {
		boolean loaded = false;

		IPlot plot = getPlot();

		// While loading is not yet complete, wait and periodically attempt to
		// read the plot again. Once the plot's independent series is set, it
		// has finished loading.
		long interval = 250;
		long time = 0;
		try {
			while (!loaded && time < timeout && !shouldCancelLoading) {
				if (plot.getIndependentSeries() != null) {
					loaded = true;
				} else {
					Thread.sleep(interval);
					time += interval;
				}
			}
		} catch (Exception e) {
			throwCriticalException("Error loading the plot.",
					"The selected visualization service encountered an error "
							+ " while loading the plot.",
					e);
		}

		return loaded;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {

		setPartName("Plot Editor");
		shouldCancelLoading = false;

		// Get the plot from the input.
		plot = null;
		IEditorInput editorInput = getEditorInput();
		// If the input is file input, we'll have to use the URI.
		if (editorInput instanceof FileEditorInput) {
			URI uri = ((FileEditorInput) editorInput).getURI();
			// Try to create a plot using the available viz services, prompting
			// the user if two or more services can create a plot.
			PlotDialogProvider provider = new PlotDialogProvider();
			if (provider.openDialog(getEditorSite().getShell(),
					uri) == Window.OK) {
				plot = provider.getSelectedPlot();

				// Put the plot in the first tab with the name Plot
				Composite mainPage = new Composite(getContainer(), SWT.NONE);
				createContent(mainPage);
				int index = addPage(mainPage);
				setPageText(index, "Plot");

				// Add any additional pages the service provides
				IVizService service = provider.getSelectedService();
				int numPages = service.getNumAdditionalPages();
				for (int i = 1; i <= numPages; i++) {
					String name = service.createAdditionalPage(this,
							(FileEditorInput) editorInput, i);
					setPageText(i, name);
				}
			}
		}
		// If the input is plot input, we can just get the plot from it.
		else if (editorInput instanceof PlotEditorInput) {
			plot = ((PlotEditorInput) editorInput).getPlot();
		}

		// If no plot could be created, close the editor.
		if (plot == null) {
			logger.error(getClass().getName()
					+ " No plot available from the input.");
			Status status = new Status(IStatus.ERROR, "org.eclipse.ice", 0,
					"The file could not be rendered.", null);
			ErrorDialog.openError(Display.getCurrent().getActiveShell(),
					"Visualization Failed",
					"The selection could not be rendered by any available "
							+ "visualization services. Please check the format "
							+ "of the file and that visualization services are "
							+ "available for that file type.",
					status);
			// Close the editor.
			getEditorSite().getPage().closeEditor(PlotEditor.this, false);
			return;
		}

	}

}