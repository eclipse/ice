/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Robert Smith, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.viz.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.viz.service.internal.VizServiceFactoryHolder;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

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

public class PlotEditor extends EditorPart {
	/**
	 * Plot editor ID for external reference.
	 */
	public static final String ID = "org.eclipse.ice.viz.service.PlotEditor";

	/**
	 * The FileEditorInput containing the plot the editor contains.
	 */
	private FileEditorInput plot;

	/**
	 * Default constructor.
	 */
	public PlotEditor() {
		super();
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
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
		return;
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
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.
	 * widgets .Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {
		setPartName("Plot Editor");
		// form = new ManagedForm(parent);
		plot = (FileEditorInput) getEditorInput();
		final URI filepath = plot.getURI();

		// Get the VizServiceFactory and all Viz Services
		final IVizServiceFactory factory = (BasicVizServiceFactory) VizServiceFactoryHolder
				.getFactory();

		// An array of all registered service names.
		String[] fullServiceNames = factory.getServiceNames();

		// An ArrayList of all registered service names.
		final ArrayList<String> serviceNames = new ArrayList<String>();
		AbstractVizService service = null;

		// An ArrayList of PlotEditorInputs, one created with each VizService
		// capable of handling the file type.
		ArrayList<PlotEditorInput> inputArray = new ArrayList<PlotEditorInput>();

		for (int i = 0; i < fullServiceNames.length; i++) {

			service = (AbstractVizService) factory.get(fullServiceNames[i]);

			// If this service can handle the file extension, create a
			// PlotEditorInput and add its name to the list of applicable
			// services.
			if (service != null && service.extensionSupported(filepath)) {
				IPlot plot = null;
				try {
					plot = service.createPlot(filepath);
					inputArray.add(new PlotEditorInput(plot));
					serviceNames.add(fullServiceNames[i]);
				} catch (Exception e1) {
					System.out.println(
							"Problem creating plot with visualization service "
									+ fullServiceNames[i] + ".");
				}

			}

		}

		// If all available services failed to create a plot, give the user an
		// error message.
		if (serviceNames.isEmpty()) {
			System.out.println(
					"All available visualizaiton services failed to render a plot.");
			Status status = new Status(IStatus.ERROR, "org.eclipse.ice", 0,
					"No visualization service could render the file.", null);
			ErrorDialog.openError(Display.getCurrent().getActiveShell(),
					"Visualization Failed",
					"All visualization services failed to render a plot. \n"
							+ "If you are using an external rendering program, "
							+ "make sure it is connected to ICE.",
					status);
			return;
		}

		// The number of services which succeeded in creating PlotEditorInputs
		int numServices = serviceNames.size();

		// Set up the editor window.
		final Composite body = parent;// this. new
										// Composite();//form.getForm().getBody();
		GridLayout grid = new GridLayout();
		grid.marginHeight = 0;
		grid.marginWidth = 0;
		body.setLayout(grid);

		// Array of names of all services which succeeded in creating
		// PlotEditorInputs
		String[] serviceNamesArray = new String[serviceNames.size()];
		serviceNames.toArray(serviceNamesArray);

		// The PlotEditorInput containing the IPlot rendered with the service
		// selected for this editor.
		final PlotEditorInput selectedService;

		// If more than one service is applicable, create a dialog window to
		// prompt the user for which is to be used. Else, use the single
		// available service.
		if (numServices == 1) {
			selectedService = inputArray.get(0);
		} else {
			PlotEditorDialog dialog = new PlotEditorDialog(PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getShell());
			dialog.createDialogArea(new Shell(), serviceNamesArray);
			selectedService = inputArray.get(dialog.getSelection());
		}

		// Reference to this editor instance
		final IEditorPart thisEditor = this;

		// Finish loading and drawing the plot in a new thread.
		Job drawPlot = new Job("Plot Editor Loading and Rendering") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				setUpEditor(body, selectedService, thisEditor);
				return Status.OK_STATUS;
			}

		};

		drawPlot.schedule();
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
	}

	/**
	 * Checks that the plot has finished loading and prepares variables for use
	 * in UI creation. Intended to be called on a non-UI thread.
	 * 
	 * @param body
	 *            Composite in which the PlotEditor will be drawn
	 * @param selectedService
	 *            The service to be used in drawing the plot
	 * @param thisEditor
	 *            A reference to the Editor calling the function
	 */
	public void setUpEditor(final Composite body,
			final PlotEditorInput selectedService,
			final IEditorPart thisEditor) {
		// Temporary holder for the plot series available from the service
		List<ISeries> tempSeries = null;
		// Temporary holder for the independent series for the plot
		ISeries tempIndSeries = null;
		try {
			tempSeries = selectedService.getPlot().getAllDependentSeries();
			tempIndSeries = selectedService.getPlot().getIndependentSeries();
		} catch (Exception e2) {
			System.out.println("Error reading plot types.");
		}

		// While loading is not yet complete, wait and periodically
		// attempt to read the plot types again.
		while (tempSeries == null || tempIndSeries == null
				|| tempSeries.isEmpty()) {
			try {
				Thread.sleep(500);
				if (tempSeries == null || tempSeries.isEmpty()) {
					tempSeries = selectedService.getPlot()
							.getAllDependentSeries();
				}
				if (tempIndSeries == null) {
					tempIndSeries = selectedService.getPlot()
							.getIndependentSeries();
				}

			} catch (Exception e1) {
				System.out.println("Error reading plot types.");
			}
		}

		// Get final references to use in the new thread.
		final List<ISeries> depSeries = tempSeries;
		final ISeries indSeries = tempIndSeries;

		// Toolbar for the editor window
		final ToolBarManager barManager = new ToolBarManager();

		// Thread for creating the editor UI
		body.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				createUI(barManager, body, selectedService, thisEditor,
						depSeries, indSeries);

			}
		});
	}

	/**
	 * Creates the UI for the PlotEditor and calls the visualization service to
	 * draw the opened plot.
	 * 
	 * @param barManager
	 *            Manager for the editor's toolbar
	 * @param body
	 *            The composite in which the editor will be drawn
	 * @param selectedService
	 *            A PlotEditorInput containing the IPlot created with the
	 *            selected visualization service
	 * @param thisEditor
	 *            A reference to the editor being created.
	 * @param seriesToPlot
	 *            The list of the valid series for this editor to be able to
	 *            plot. should have already checked the validity (at least one
	 *            element) in this list.
	 * @param selectedPlotType
	 *            The type of the initial plot to draw
	 */
	private void createUI(ToolBarManager barManager, Composite body,
			final PlotEditorInput selectedService, final IEditorPart thisEditor,
			final List<ISeries> seriesToPlot, final ISeries independentSeries) {

		// Finish setting up the editor window
		ToolBar bar = barManager.createControl(body);
		final Composite plotComposite = new Composite(body, SWT.NONE);

		// Menu manager for toolbar
		MenuManager menu = new MenuManager("Menu");

		// Top level menu
		ActionTree menuTree = new ActionTree("Menu");

		// A second level menu that will hold the series to plot
		ActionTree seriesTree = new ActionTree("Plot Series");
		menuTree.add(seriesTree);

		// Set the independent series for this editor
		selectedService.getPlot().setIndependentSeries(independentSeries);

		// Add the first series in the list so that we are plotting something
		selectedService.getPlot().addDependentSeries(seriesToPlot.get(0));

		for (final ISeries series : seriesToPlot) {

			// A menu item to redraw the plot with the
			// selected category and plot type
			Action tempAction = new Action(series.getLabel()) {
				@Override
				public void run() {
					try {
						// Adds the series to the editor and sets the plot to
						// redraw.
						selectedService.getPlot().addDependentSeries(series);
						selectedService.getPlot().draw(plotComposite);
					} catch (Exception e) {
						System.out.println("Error while drawing plot.");
					}
				}

			};
			// Add the new menu entry
			seriesTree.add(new ActionTree(tempAction));

		}

		// An action to close the current editor window
		Action close = new Action("Close") {
			@Override
			public void run() {
				thisEditor.getEditorSite().getPage().closeEditor(thisEditor,
						false);
			}
		};

		// Add close action directly under menu
		menuTree.add(new ActionTree(close));

		// Update menu
		seriesTree.getContributionItem().fill(menu.getMenu(), -1);
		menu.updateAll(true);
		barManager.add(menuTree.getContributionItem());

		bar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		// managedForm.getToolkit().adapt(bar);
		barManager.update(true);

		plotComposite.setBackground(body.getBackground());
		plotComposite
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		plotComposite.setLayout(new FillLayout());

		// Draw the plot
		try {
			selectedService.getPlot().draw(plotComposite);
		} catch (Exception e) {
			System.err.println("PlotEditor: Error while drawing plot.");
		}

		body.layout();
	}
}
