/*******************************************************************************
 * Copyright (c) 2012, 2014- UT-Battelle, LLC.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.client.widgets.viz.service.IVizService;
import org.eclipse.ice.client.widgets.viz.service.IVizServiceFactory;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.resource.VizResource;
import org.eclipse.ice.iclient.uiwidgets.ISimpleResourceProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.FileStoreEditorInput;

/**
 * This class is a FormPage that creates a page with table and metadata viewing
 * area for an ICE ResourceComponent.
 * 
 * @authors Jay Jay Billings, Taylor Patterson, Anna Wojtowicz, Jordan Deyton
 */
public class ICEResourcePage extends ICEFormPage implements ISelectionListener,
		IUpdateableListener {
	
	/**
	 * The ResourceComponent drawn by this page.
	 */
	private ResourceComponent resourceComponent;

	/**
	 * The current resource being managed by this page.
	 */
	private ICEResource currentResource = null;

	/**
	 * The ICEResourceView that holds resources for this page to display.
	 */
	private ICEResourceView resourceView;
	
	/**
	 * The workbench page used by this ICEResourcePage.
	 */
	
	private IWorkbenchPage workbenchPage;
		
	/**
	 * The primary composite for rendering the page.
	 */
	private Composite pageComposite;
	
	/**
	 * A sub-composite of {@link #pageComposite}, used for drawing plots and 
	 * any associated widgets.
	 */
	private Composite drawingComposite;
	
	/**
	 * A sub-composite of {@link #drawingComposite}, used for arranging
	 * {@link #plotComposites} in a grid-like formation.
	 */
	private Composite gridComposite;
	
	private static Button closeButton;
	
	/**
	 * An Array storing the current dimensions of the plotComposite's grid.
	 * [0] = rows, [1] = columns
	 */
	private int[] gridDimensions;
	
	/**
	 * The layout that stacks the plot and browser composites. We use a
	 * StackLayout because we only show either the {@link #browser} or the
	 * {@link #plotComposite}.
	 */
	private final StackLayout stackLayout = new StackLayout();

	/**
	 * A browser to display the files/images from resources.
	 */
	private Browser browser;

	/**
	 * The service factory for visualization tools. This can be queried for
	 * visualization services.
	 */
	private IVizServiceFactory vizFactory;

	/**
	 * The map that holds any existing plots, keyed on the resource IDs.
	 */
	private final Map<String, IPlot> plots;
	
	/**
	 * The map that holds any drawn plots, keyed on the resource IDs. A
	 * Composite should only be added to this map if it already exists in
	 * {@link #plots}.
	 */
	private final Map<String, Composite> plotComposites;
	
	/**
	 * <p>
	 * A list that manages the currently rendered {@link #plotComposites} in 
	 * the {@link #gridComposite}. Plots in this list are ordered based on their
	 * tiling position (like a book, from left to right, and top to bottom).
	 * Note that this list differs from the {@link #plotComposites} map, as it
	 * only contains plots currently shown on the {@link #pageComposite}, while 
	 * {@link #plotComposites} can contain plots that have been drawn, but not
	 * currently rendered on the page.
	 * </p>
	 * <p>
	 * Since you cannot explicitly set the number of rows in GridLayouts, 
	 * this list can also contain empty Controls to meet the correct number
	 * of rows in the {@link #gridComposite}.
	 * </p>
	 */
	private ArrayList<Control> gridManager;
	
	/**
	 * A list of file extensions that the ICEResourcePage should be treat as 
	 * text files and opened via the default Eclipse text editor.
	 */
	private ArrayList<String> textFileExtensions;

	/**
	 * The default constructor.
	 * 
	 * @param editor
	 *            The FormEditor for which the Page should be constructed. This
	 *            should be an {@link ICEFormEditor}.
	 * @param id
	 *            The id of the page.
	 * @param title
	 *            The title of the page.
	 */
	public ICEResourcePage(FormEditor editor, String id, String title) {

		// Call the super constructor
		super(editor, id, title);

		// Set the ICEFormEditor
		if (!(editor instanceof ICEFormEditor)) {
			System.out.println("ICEResourcePage Message: Invalid FormEditor.");
		}

		// Setup the plot maps.
		plots = new HashMap<String, IPlot>();
		plotComposites = new HashMap<String, Composite>();
		
		// Create the list of text file extensions
		String[] extensions = {"txt", "sh", "i", "csv"};
		textFileExtensions = new ArrayList<String>(Arrays.asList(extensions));
		
		return;
	}

	/**
	 * This operation overrides the default/abstract implementation of
	 * FormPage.createFormContents to create the contents of the
	 * ICEResourcePage.
	 * 
	 * @param managedForm
	 *            The Form widget on which the ICEResourcePage exists.
	 */
	protected void createFormContent(IManagedForm managedForm) {

		// Local Declarations
		final FormToolkit toolkit = managedForm.getToolkit();

		// Try to show the Resource View.
		try {
			getSite().getWorkbenchWindow().getActivePage()
					.showView(ICEResourceView.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		// Get the Resource View and set its content to this page's
		// ResourceComponent.
		resourceView = (ICEResourceView) getSite().getWorkbenchWindow()
				.getActivePage().findView(ICEResourceView.ID);

		// Get the parent Composite for the Resource Page widgets and set its
		// layout to the StackLayout.
		pageComposite = managedForm.getForm().getBody();
		pageComposite.setLayout(stackLayout);

		// Register the page as a selection listener. The page returned by
		// getPage() is not the same as this page! There are some subtle UI
		// differences under the hood.
		getSite().getPage().addSelectionListener(this);
		// Add a dispose event listener on the parent. If disposed at any point,
		// remove it from the workbench's SelectionService listeners (or else it
		// will attempt to refresh disposed widgets).
		pageComposite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent event) {
				getSite().getWorkbenchWindow().getSelectionService()
						.removeSelectionListener(ICEResourcePage.this);
			}
		});

		// Create the browser.
		browser = createBrowser(pageComposite, toolkit);
		stackLayout.topControl = browser;
		pageComposite.layout();
		
		// Set the workbench page reference
		workbenchPage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		return;
	}

	/**
	 * This operation draws a browser on the page that displays the selected
	 * ICEResource.
	 * 
	 * @param formToolkit
	 *            The FormToolkit that is used to create SWT widgets for this
	 *            page.
	 * @param form
	 *            The IManagedForm on which the table should be drawn.
	 */
	protected Browser createBrowser(Composite parent, FormToolkit toolkit) {

		Browser browser = null;

		// It is possible that constructing the Browser throws an SWTError.
		// Thus, to create the browser, we must use a try-catch block.
		try {
			// Initialize the browser and apply the layout. It should use a
			// FillLayout so its contents take up all available space.
			browser = new Browser(parent, SWT.NONE);
			toolkit.adapt(browser);
			browser.setLayout(new FillLayout());

			// Display the default-selected Resource from the Resource View in
			// the browser, or a message.
			if (currentResource != null
					&& !(currentResource instanceof VizResource)) {
				browser.setUrl(currentResource.getPath().toString());
			} else {
				if (!resourceComponent.isEmpty()) {
					browser.setText("<html><body>"
							+ "<p style=\"font-family:Tahoma;font-size:x-small\" "
							+ "align=\"center\">Select a resource to view</p>"
							+ "</body></html>");
				} else {
					browser.setText("<html><body>"
							+ "<p style=\"font-family:Tahoma;font-size:x-small\" "
							+ "align=\"center\">No resources available</p>"
							+ "</body></html>");
				}
			}
		} catch (SWTError e) {
			System.out.println("Client Message: "
					+ "Could not instantiate Browser: " + e.getMessage());
		}

		return browser;
	}
	
	/**
	 * This operation overrides the default/abstract implementation of
	 * ISelectionListener.selectionChanged to display the resource selected in
	 * the ICEResourceView.
	 * 
	 * @param part
	 *            The IWorkbenchPart that called this function.
	 * @param selection
	 *            The ISelection chosen in the part parameter.
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		// If the selection comes from the Resource View, update the view.
		if (ICEResourceView.ID.equals(part.getSite().getId())) {
			ICEResource selectedResource = resourceView
					.getResourceFromSelection(selection);

			// Refresh the page's widgets based on the selected resource.
			if (selectedResource != null) {
				try {
					setCurrentResource(selectedResource);					
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}

		return;
	}

	/**
	 * This method creates and sets up the composite which will contain a grid
	 * of drawn IPlots.
	 */
	private void createDrawingComposite(Composite parent) {
		
		// Create the enclosing composite the gridComposite is contained in
		drawingComposite = new Composite(parent, SWT.NONE);
		drawingComposite.setLayout(new GridLayout());
		
		// Create a composite for the "rows" and "columns" buttons/spinners
		Composite buttonComposite = new Composite(drawingComposite, SWT.BORDER);
	    buttonComposite.setLayout(new GridLayout(2, true));
		buttonComposite.setLayoutData(new GridData(SWT.LEFT));
		
		// Create buttons/spinners and labels for the button composite
		Label rowsLabel = new Label(buttonComposite, SWT.NONE);
		rowsLabel.setText("Rows:");
		Label columnsLabel = new Label(buttonComposite, SWT.NONE);
		columnsLabel.setText("Columns:");
		Spinner rows = new Spinner(buttonComposite, SWT.READ_ONLY);
		rows.setMinimum(1);
		rows.setMaximum(4);
		rows.setSelection(2);
		rows.setIncrement(1);
		Spinner columns = new Spinner(buttonComposite, SWT.READ_ONLY);
		columns.setMinimum(1);
		columns.setMaximum(4);
		columns.setSelection(2);
		columns.setIncrement(1);
				
		// Set listeners on the rows and columns to update the plot grid when
		// the number of rows or columns is changed
		rows.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int rows = ((Spinner) e.widget).getSelection();
				updateGridComposite(rows, -1);
			}
		});
		columns.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int columns = ((Spinner) e.widget).getSelection();
				updateGridComposite(-1, columns);
			}
		});
			
		// Construct the plot composite that will hold IPlots
		gridComposite = new Composite(drawingComposite, SWT.NONE);
		gridComposite.setLayout(new GridLayout(columns.getSelection(), true));
		gridComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// Create the gridDimensions
		gridDimensions = new int[] {rows.getSelection(), columns.getSelection()};
		
		// Create the gridManager
		gridManager = new ArrayList<Control>(Arrays.asList(gridComposite.getChildren()));
		
		// Fill the gridComposite spaces with empty controls
		addEmptyControls(gridComposite);
		
		return;
	}
	
	/**
	 * This method updates the {@link #gridComposite} to reflect changes to the 
	 * number of columns and/or rows in the grid area. Passing a value of -1
	 * into the rows parameter indicates that the number of columns has been
	 * changed. Similarly, passing a value of -1 into the columns parameter 
	 * indicates that the number of rows has been changed. A value of -1 in both
	 * parameters means nothing will update.
	 * 
	 * @param columns	The number of columns. Must be a positive integer, or 
	 * 					-1 to indicate that the number of columns has not been 
	 * 					updated.
	 * @param rows		The number of rows. Must be a positive integer, or -1 
	 * 					to indicate that the number of rows has not been updated.
	 */
	private void updateGridComposite(int rows, int columns) {
		
		if (columns == -1 && rows > 0) {
			
			// Check if we're subtracting rows			
			if (rows < gridDimensions[0]) {

				// Check if there are more plots than there are available
				// spaces to tile them
				int numTiles = rows*gridDimensions[1];
				if (gridManager.size() > numTiles) {
					
					// Dispose of any excess Composites that there aren't room for
					disposeExcessControls(numTiles);
				}
			}
			
			// Update the gridDimensions (any extra spaces will be filled with
			// BlankControls near the end of this method)
			gridDimensions[0] = rows;
			
		} else if (rows == -1 && columns > 0) {
			
			// Set a new layout on the gridComposite with the number of columns
			gridComposite.setLayout(new GridLayout(columns, true));
						
			// Check if we're subtracting columns
			if (columns < gridDimensions[1]) {
				
				// Check if there are more plots than there are available
				// spaces to tile them
				int numTiles = columns*gridDimensions[0];
				if (gridManager.size() > numTiles) {
					
					// Dispose of any excess Composites that there aren't room for
					disposeExcessControls(numTiles);
				}
			}
			
			// Update the gridDimensions (any extra spaces will be filled with
			// BlankControls near the end of this method)
			gridDimensions[1] = columns;
		}
		
		// Fill in any remaining spaces with empty controls (if necessary)
		addEmptyControls(gridComposite);
		
		// Pack and lay out the new grid configuration
		gridComposite.pack();
		drawingComposite.pack();
		pageComposite.layout();
		
		return;
	}
	
	/**
	 * This method is responsible for adding empty widgets to the 
	 * {@link #gridComposite} (and {@link #gridManager} by proxy) if the grid 
	 * doesn't contain enough {@link #plotComposites} to create the appropriate 
	 * number of rows. This is necessary, as there is no way to specify the
	 * number of rows in a GridLayout, and must be forced.
	 * 
	 * @param parent	The GridLayout Composite the empty controls will be 
	 * 					added to.
	 */
	private void addEmptyControls(Composite parent) {
		
		// Figure out how many blank tiles we need to create
		int numEmpties = 
				(gridDimensions[0]*gridDimensions[1]) - gridManager.size();

		if (numEmpties > 0) {			
			// Add the empty controls to the gridComposite
			for (int i = 0; i < numEmpties; i++) {
				
				// Create an empty control and add it to the gridManager
				EmptyControl control = new EmptyControl(parent);
				gridManager.add(control);
				
				// Add a listener to remove the empty control from the
				// gridManager when it disposes
				control.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(DisposeEvent e) {
						Control c = (Control) e.widget;
						gridManager.remove(c);
					}
				});
			}
			
			// Pack the parent composite
			parent.pack();
		}
		
		return;		
	}
	
	/**
	 * This method goes through the list of currently displayed {@link Control}s
	 * and disposes any that are {@link EmptyControl}s.
	 */
	private void disposeEmptyControls() {
		
		for (int i = 0; i < gridManager.size(); i++) {
			
			Control currControl = gridManager.get(i);
			
			if (currControl instanceof EmptyControl) {
				currControl.dispose();
				i--;
			}
		}
		
		return;
	}
	
	/**
	 * This method will dispose of any Controls contained in the 
	 * {@link #gridManager} that exceed the number of spots available on the
	 * {@link #gridComposite} area. It begins with the very last Control
	 * contained in the {@link #gridManager} and works backwards.
	 * 
	 * @param numTiles	The number of tiles available for displaying plots.
	 */
	private void disposeExcessControls(int numTiles) {
		
		// Dispose of any excess plotComposites we can't fit,
		// starting by removing the last one
		for (int i = gridManager.size()-1; i >= numTiles; i--) {
			Composite plot = (Composite) gridManager.get(i);
			plot.dispose();
		}
		
		return;
	}
	
	/**
	 * Updates the Resource Page's widgets to render the specified Resource.
	 * <p>
	 * <b>Note:</b> This method assumes it is called from the UI thread.
	 * </p>
	 * 
	 * @param resource
	 *            The resource to render. Assumed not to be {@code null}.
	 * @throws PartInitException 
	 */
	private void setCurrentResource(ICEResource resource) throws PartInitException {

		if (resource != currentResource) {
			currentResource = resource;
			
			// VizResources should not use the browser. However, if it cannot be
			// rendered with available VizResources, we should try using the
			// browser.
			boolean useBrowser = !(resource instanceof VizResource);
			if (!useBrowser) {
				
				// Create the drawing composite for plots if it hasn't already been
				if (drawingComposite == null || drawingComposite.isDisposed()) {
					createDrawingComposite(pageComposite);
				}
				
				VizResource vizResource = (VizResource) resource;
				Composite plotComposite = null;

				// Dispose of any empty controls
				disposeEmptyControls();
				
				// Try to find the drawn plot for this resource.
				String key = getPlotKey(vizResource);
				plotComposite = plotComposites.get(key);
				
				// If the plot wasn't found and we have enough space on the
				// gridComposite, then create the plot and add it.
				if (plotComposite == null || plotComposite.isDisposed()) {
					
					// Check that there is enough empty space on the grid
					if ((gridDimensions[0]*gridDimensions[1]) > gridManager.size()) {
						plotComposite = 
								createPlotComposite(gridComposite, vizResource);
					} else {
						// Do nothing
						return;
					}
				}
								
				// Re-fill any blank controls (if necessary)
				addEmptyControls(gridComposite);

				// If the plot and its drawn Composite could be found, update
				// the StackLayout. Otherwise, the next section will attempt to
				// either open it in a text editor (if applicable), or the 
				// browser as a last resort.
				if (gridComposite != null && plotComposite != null) {
					stackLayout.topControl = drawingComposite;
					drawingComposite.pack();
					pageComposite.layout();
	
					// Reactivate the Item editor tab if it's not in the front			
					activateEditor();
										
					return;
				}
			}
			
			// Determine if the resource is a text file
			int extIndex = resource.getPath().toString().lastIndexOf(".");
			String fileExtension = resource.getPath().toString().substring(extIndex+1);
			boolean useEditor = textFileExtensions.contains(fileExtension);
			
			// If the resource is a text file, open it via the Eclipse default
			// text editor
			if (useEditor) {
				// Get the content of the file
				IFileStore fileOnLocalDisk = 
						EFS.getLocalFileSystem().getStore(resource.getPath());
				FileStoreEditorInput editorInput = 
						new FileStoreEditorInput(fileOnLocalDisk);

				// Open the contents in the text editor
				IWorkbenchWindow window = 
						PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();
				page.openEditor(editorInput, "org.eclipse.ui.DefaultTextEditor");
			}
			
			// If the Resource is a regular Resource or cannot be rendered via
			// the VizServices or a text editor, try to open it in the browser
			// as a last resort.
			if (useBrowser && !useEditor && browser != null && !browser.isDisposed()) {
				// Update the browser.
				browser.setUrl(resource.getPath().toString());
				stackLayout.topControl = browser;
				pageComposite.layout();
				
				// Reactivate the Item editor tab if it's not in the front			
				activateEditor();
			}
		}

		return;
	}
	
	/**
	 * Gets the resource's key for use in the plot maps.
	 * 
	 * @param resource
	 *            The resource whose key should be determined. Assumed not to be
	 *            {@code null}.
	 * @return The resource's key in the plot maps.
	 */
	private String getPlotKey(ICEResource resource) {
		return resource.getPath().toString();
	}

	/**
	 * This method queries each {@code IVizService} from the {@link #vizFactory}
	 * until it finds the first one that can create an {@link IPlot} for the
	 * specified resource. If one could be created, it will be added to the map
	 * of {@link #plots}.
	 * 
	 * @param resource
	 *            The resource that needs an {@link IPlot}.
	 * @return The plot, or {@code null} if one could not be created.
	 */
	private IPlot createPlot(VizResource resource) {
		IPlot plot = null;

		String[] serviceNames = vizFactory.getServiceNames();
		for (String serviceName : serviceNames) {
			// Get the next IVizService.
			IVizService service = vizFactory.get(serviceName);
			if (service != null) {
				// Try to create a plot with the service. If one was created, it
				// will need to go into the map of plots.
				try {
					plot = service.createPlot(resource.getPath());
					if (plot != null) {
						plots.put(getPlotKey(resource), plot);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return plot;
	}

	/**
	 * This method attempts to create a plot {@code Composite} for the specified
	 * resource. If one could be created, it will be added to the map of
	 * {@link #plotComposites}.
	 * <p>
	 * <b>Note (1):</b> This method assumes it is called from the UI thread.
	 * </p>
	 * <p>
	 * <b>Note (2):</b> If there is not a corresponding {@link IPlot} in the map
	 * of {@link #plots}, no {@code Composite} will be created.
	 * </p>
	 * 
	 * @param parent
	 *            The parent {@code Composite} in which to draw the new plot
	 *            {@code Composite}.
	 * @param resource
	 *            The resource that needs a plot {@code Composite}.
	 * @return The plot {@code Composite}, or {@code null} if one could not be
	 *         created.
	 */
	private Composite createPlotComposite(Composite parent, VizResource resource) {
		Composite plotComposite = null;

		// First, check the map of plot Composites.
		String key = getPlotKey(resource);

		IPlot plot = plots.get(key);
		if (plot != null) {

			// Try to get the available categories and plot types, then try to
			// plot the first available one.
			try {
				Map<String, String[]> plotTypes = plot.getPlotTypes();

				// Find the first category and plot type.
				String category = null;
				String[] types = null;
				Iterator<Entry<String, String[]>> iter = plotTypes.entrySet()
						.iterator();
				while (iter.hasNext() && (types == null || types.length == 0)) {
					Entry<String, String[]> entry = iter.next();
					category = entry.getKey();
					types = entry.getValue();
				}

				// TODO We will want to expose the categories and plot types to
				// the user.

				// If there is an available category and plot type, try to
				// render the plot.
				if (types != null && types.length > 0) {
					String plotType = types[0];

					// Try to draw the contents of the plot Composite. If
					// successfully drawn, add it to the map of plot Composites.
					try {
						// Set up the composite that will contain a plot
						FormToolkit toolkit = getManagedForm().getToolkit();
						plotComposite = toolkit.createComposite(parent);
						plotComposite.setLayout(new FillLayout());
						plotComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
											
						// Draw and pack the plot in the parent composite
						plot.draw(category, plotType, plotComposite);
						parent.pack();					
						
						// Store it in the plot map and grid manager
						plotComposites.put(key, plotComposite);
						gridManager.add(plotComposite);
						
						// Add a dispose listener to remove the plot from the
						// gridManager
						plotComposite.addDisposeListener(new DisposeListener() {
							@Override
							public void widgetDisposed(DisposeEvent e) {
								Composite c = (Composite) e.widget;
								gridManager.remove(c);

								drawingComposite.pack();
								pageComposite.layout();
							}
						});
						
					}	
					// If the plot could not be drawn, dispose the plot
					// Composite.
					catch (Exception drawException) {
						drawException.printStackTrace();
						plotComposite.dispose();
						plotComposite = null;
					}
				}
			} catch (Exception plotTypeException) {
				plotTypeException.printStackTrace();
			}
		}

		return plotComposite;
	}
	
	/**
	 * Reactivates the Item's editor and brings it to the front if any other
	 * editors have been opened on top of it.
	 */
	private void activateEditor() {
		
		// Check that the workbench page has been set correctly first
		if (workbenchPage != null) {
			
			// Reactivate the editor tab if it's not in the front		
			if (getEditor() != null	
					&& workbenchPage.getActiveEditor() != getEditor()) {
				workbenchPage.activate(getEditor());
			}
		} else {
			
			// Set the workbench page and try activating the editor again
			workbenchPage = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			activateEditor();
		}
		
		return;
	}

	/**
	 * This operation sets the IVizServiceFactory that should be used to create
	 * plots.
	 * 
	 * @param service
	 *            The service factory that should be used
	 */
	public void setVizService(IVizServiceFactory factory) {
		vizFactory = factory;
	}

	/**
	 * This operation sets the ResourceComponent that should be used by the
	 * ICEResourcePage. It also registers the ICEResourcePage with the
	 * ResourceComponent so that it can be notified of state changes through the
	 * IUpdateableListener interface.
	 * <p>
	 * <b>Note:</b> This method should only be called when the page is created.
	 * </p>
	 * 
	 * @param component
	 *            The ResourceComponent
	 */
	public void setResourceComponent(ResourceComponent component) {

		if (component != resourceComponent) {

			// Get the Display for updating the UI from the page's Composite.
			final Display display;
			if (pageComposite != null && !pageComposite.isDisposed()) {
				display = pageComposite.getDisplay();
			} else {
				display = null;
			}

			// If necessary, unregister from the old ResourceComponent.
			if (resourceComponent != null) {
				resourceComponent.unregister(this);
			}

			// ---- Clear the Resource Page widgets. ---- //
			// Clear out the old metadata that can be done from outside the UI
			// thread.
			plots.clear();
			currentResource = null;

			// Update the UI and dispose of any stale UI pieces.
			if (display != null) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						// Clear the browser and make it the top widget.
						browser.setText("<html><body>"
								+ "<p style=\"font-family:Tahoma;font-size:x-small\" "
								+ "align=\"center\">No resources available</p>"
								+ "</body></html>");
						stackLayout.topControl = browser;
						pageComposite.layout();

						// Dispose any plot Composites.
						for (Composite plotComposite : plotComposites.values()) {
							plotComposite.dispose();
						}
						plotComposites.clear();

						return;
					}
				});
			}
			// ------------------------------------------ //

			// Set the reference to the new ResourceComponent.
			this.resourceComponent = component;

			// If not null, register for updates from it and trigger an update
			// to sync the ResourceComponent.
			if (component != null) {
				resourceComponent.register(this);
				update(resourceComponent);

				// Set the default selection in the Resource View and update the
				// contents of the Resource Page's widgets. This must be done on
				// the UI thread.
				if (display != null) {
					// Get the current selection from the Resource View and set
					// the current Resource to it if possible.
					ISelection selection;
					selection = getSite().getPage().getSelection(
							ICEResourceView.ID);
					final ICEResource currentResource = resourceView
							.getResourceFromSelection(selection);
					if (currentResource != null) {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									setCurrentResource(currentResource);
								} catch (PartInitException e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			}
		}

		return;
	}

	/**
	 * This operation retrieves the ResourceComponent that has been rendered by
	 * the ICEResourcePage or null if the component does not exist.
	 * 
	 * @return The ResourceComponent or null if the component was not previously
	 *         set.
	 */
	public ResourceComponent getResourceComponent() {
		return resourceComponent;
	}

	/**
	 * This operation sets the ISimpleResource provider that should be used by
	 * the output page to load ICEResources.
	 * 
	 * @param provider
	 *            The ISimpleResourceProvider
	 */
	public void setResourceProvider(ISimpleResourceProvider provider) {

		// Set the provider if it is not null
		if (provider != null) {
			// TODO
		}
		return;
	}

	/**
	 * This method is called whenever the current {@link #resourceComponent} is
	 * updated. It adds any new plots to the map of {@link #plots} if possible.
	 */
	@Override
	public void update(IUpdateable component) {

		if (component != null && component == resourceComponent) {
			
			// Get a local copy of the ResouceComponent.
			ResourceComponent resourceComponent = (ResourceComponent) component;

			// Create plots for any VizResources in the ResourceComponent that
			// do not already have plots.
			for (ICEResource resource : resourceComponent.getResources()) {
				if (resource instanceof VizResource) {
					// Try to get the existing plot.
					IPlot plot = plots.get(getPlotKey(resource));
					// If there is no plot already, try to create one.
					if (plot == null) {
						plot = createPlot((VizResource) resource);
					}
				}
			}
		}

		return;
	}
	
	/**
	 * This class is used to create empty controls to fill the 
	 * {@link #gridComposite} when its number of rows must be forced. Although
	 * this class is no different than a {@link Composite}, it is safer for the 
	 * UI to check for instances of {@link EmptyControl}s than 
	 * {@link Composite}s when it is time to dispose them.
	 * 
	 * @author Anna Wojtowicz
	 */
	class EmptyControl extends Composite {
		public EmptyControl(Composite parent) {
			super(parent, SWT.NONE);
			setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		}
	};
}