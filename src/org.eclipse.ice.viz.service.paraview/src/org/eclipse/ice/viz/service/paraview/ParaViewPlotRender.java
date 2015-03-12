package org.eclipse.ice.viz.service.paraview;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.viz.service.connections.ConnectionPlotRender;
import org.eclipse.ice.viz.service.connections.IConnectionAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kitware.vtk.web.VtkWebClient;
import com.kitware.vtk.web.util.InteractiveRenderPanel;

public class ParaViewPlotRender extends ConnectionPlotRender<VtkWebClient> {

	// TODO Use a thread to throttle the resize events.

	/**
	 * The associated plot's connection adapter.
	 */
	private final IConnectionAdapter<VtkWebClient> adapter;

	/**
	 * The {@code ToolBarManager} that will contain the plot actions that can
	 * update the plot widget.
	 */
	private ToolBarManager toolBar;

	/**
	 * The {@code ActionTree} that can be used to update the plot category and
	 * type.
	 */
	private ActionTree plotTypeTree;
	/**
	 * The {@code ActionTree} that can be used to update the plot
	 * representation.
	 */
	private ActionTree representationTree;

	/**
	 * The current plot category rendered in the associated rendering widget.
	 * <p>
	 * <b>Note:</b> This value should only be updated when the corresponding UI
	 * piece is updated.
	 * </p>
	 */
	private String plotCategory;
	/**
	 * The current plot type rendered in the associated rendering widget.
	 * <p>
	 * <b>Note:</b> This value should only be updated when the corresponding UI
	 * piece is updated.
	 * </p>
	 */
	private String plotType;
	/**
	 * The current plot representation for the associated rendering widget.
	 * <p>
	 * <b>Note:</b> This value should only be updated when the corresponding UI
	 * piece is updated.
	 * </p>
	 */
	private String plotRepresentation;

	/**
	 * The current plot representation. This may be out of sync with the
	 * associated rendering widget.
	 * <p>
	 * <b>Note:</b> To access this variable, <i>use its getter</i>. To update
	 * it, <i>use its setter</i>.
	 * </p>
	 * 
	 * @see #getPlotRepresentation()
	 * @see #setPlotRepresentation(String)
	 */
	private String representation;

	/**
	 * The set of valid plot representations.
	 * <p>
	 * <b>Note:</b> To access this variable, <i>use its getter</i>.
	 * </p>
	 * 
	 * @see #getPlotRepresentations()
	 */
	private Set<String> representations;

	/**
	 * The ID of the associated ParaView view.
	 */
	private int viewId;
	/**
	 * The ID of the associated ParaView file proxy.
	 */
	private int fileId;
	/**
	 * The ID of the associated ParaView representation proxy.
	 */
	private int repId;

	private InteractiveRenderPanel renderPanel;

	/**
	 * Contains the mapping from the plot category to the file proxy property
	 * that must be updated when a plot type from the category is selected. This
	 * map is keyed on the plot category.
	 */
	private final Map<String, String> plotTypeArrays;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            The parent Composite that contains the plot render.
	 * 
	 * @param plot
	 *            The rendered ConnectionPlot. This cannot be changed.
	 */
	public ParaViewPlotRender(Composite parent, ParaViewPlot plot) {
		this(parent, plot, -1, -1, -1);
	}

	/**
	 * The full constructor. <i>This should only be called once per plot so that
	 * the view created to read the file's contents is not wasted.</i>
	 * 
	 * @param parent
	 *            The parent Composite that contains the plot render.
	 * 
	 * @param plot
	 *            The rendered ConnectionPlot. This cannot be changed.
	 * @param viewId
	 *            The ID of the view that was created to read the file's
	 *            contents, or -1 to indicate that a view has not been created.
	 * @param repId
	 *            The ID of the representation (i.e., the render properties) of
	 *            the file.
	 */
	public ParaViewPlotRender(Composite parent, ParaViewPlot plot, int viewId,
			int fileId, int repId) {
		super(parent, plot);

		this.viewId = viewId;
		this.fileId = fileId;
		this.repId = repId;

		// Store the adapter so that we can access its connection later.
		this.adapter = plot.getParaViewConnectionAdapter();

		// Build the map of file proxy properties for the plot type/category.
		plotTypeArrays = new HashMap<String, String>();
		plotTypeArrays.put("Meshes", "MeshStatus");
		plotTypeArrays.put("Materials", "MaterialStatus");
		plotTypeArrays.put("Cell Arrays", "CellArrayStatus");
		plotTypeArrays.put("Point Arrays", "PointArrayStatus");

		return;
	}

	/**
	 * Gets the current plot representation.
	 * 
	 * @return The current plot representation.
	 */
	protected String getPlotRepresentation() {
		return representation;
	}

	/**
	 * Sets the current plot representation (e.g., sufrace, wireframe, etc.).
	 * <p>
	 * <b>Note:</b> A subsequent call to {@link #refresh()} will be necessary to
	 * sync the UI with this call's changes.
	 * </p>
	 * 
	 * @param representation
	 *            The new representation.
	 */
	protected void setPlotRepresentation(String representation) {
		this.representation = representation;
	}

	/**
	 * Gets the available plot representations from the ParaView connection.
	 * 
	 * @return The list of allowed plot representations, or an empty list if
	 *         there are none or an exception could not be read.
	 */
	protected Set<String> getPlotRepresentations() {
		if (representations == null) {
			// Create a LinkedHashSet so the order from ParaView is preserved.
			representations = new LinkedHashSet<String>();

			VtkWebClient connection = adapter.getConnection();
			List<Object> args = new ArrayList<Object>(1);
			JSONObject object;

			// We need to call pv.proxy.manager.get to get the representation
			// proxy's properties. We then must traverse its "ui" properties (a
			// JSON
			// array) to find the "Representation" list.
			args.add(repId);
			try {
				// Get the "ui" properties for the representation.
				object = connection.call("pv.proxy.manager.get", args).get();
				JSONArray array = object.getJSONArray("ui");
				// Find the "Representation" object from the "ui" properties. We
				// have to check each JSON object's "name" tag.
				for (int i = 0; i < array.length(); i++) {
					object = array.getJSONObject(i);
					// When the "Representation" object is found, add all of its
					// values to the list.
					if ("Representation".equals(object.get("name"))) {
						array = object.getJSONArray("values");
						for (i = 0; i < array.length(); i++) {
							representations.add(array.getString(i));
						}
						break;
					}
				}
			} catch (Exception e) {
				// If there was an error, make sure the list of representations
				// is
				// empty.
				representations.clear();
			}
		}
		// Return the set.
		return representations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.PlotRender#clearCache()
	 */
	@Override
	protected void clearCache() {
		// Clear and unset the set of representations, so the next time
		// getPlotRepresentations() is called, it will be rebuilt.
		if (representations != null) {
			representations.clear();
			representations = null;
		}

		// Clear the ActionTrees. These will be refreshed the next time the plot
		// widget is updated.
		if (plotTypeTree != null) {
			plotTypeTree.removeAll();
		}
		if (representationTree != null) {
			representationTree.removeAll();
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionPlotRender#
	 * createPlotComposite(org.eclipse.swt.widgets.Composite, int,
	 * java.lang.Object)
	 */
	@Override
	protected Composite createPlotComposite(Composite parent, int style,
			VtkWebClient connection) throws Exception {

		// Create a new view on the ParaView server if one does not already
		// exist. We will need the corresponding view, file proxy, and
		// representation proxy IDs.
		if (viewId < 0 || fileId < 0 || repId < 0) {
			String fullPath = ((ParaViewPlot) plot)
					.getParaViewConnectionAdapter().findRelativePath(
							plot.getDataSource().getPath());

			// Create a new view from the file and get its associated IDs.
			List<Object> args = new ArrayList<Object>();
			JSONObject object;
			args.add(fullPath);
			object = connection.call("createView", args).get();
			viewId = object.getInt("viewId");
			fileId = object.getInt("proxyId");
			repId = object.getInt("repId");

			// Throw an exception if a view could not be created for the file.
			if (viewId < 0 || fileId < 0 || repId < 0) {
				throw new Exception("IPlot error: "
						+ "The file could not be rendered with "
						+ plot.getVizService().getName() + ".");
			}
		}

		// Create a container to hold a ToolBar and the ParaView widget.
		Composite plotContainer = new Composite(parent, style);
		plotContainer.setBackground(parent.getBackground());
		plotContainer.setFont(parent.getFont());
		GridLayout gridLayout = new GridLayout();
		// Get rid of the default margins (5 px on top, bottom, left, right).
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		plotContainer.setLayout(gridLayout);

		// Create a ToolBar.
		ToolBarManager toolBarManager = new ToolBarManager();
		this.toolBar = toolBarManager;
		ToolBar toolBar = toolBarManager.createControl(plotContainer);
		toolBar.setBackground(parent.getBackground());
		toolBar.setFont(parent.getFont());
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		fillToolBar(toolBarManager, connection);

		// Since the ParaView widget is built on AWT, we will need to use the
		// SWT_AWT bridge below.

		// Create the Composite that will contain the embedded ParaView widget.
		final Composite composite = new Composite(plotContainer, SWT.EMBEDDED
				| SWT.DOUBLE_BUFFERED);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Create the ParaView widget.
		renderPanel = new InteractiveRenderPanel(connection, viewId, 4, 80, 1);

		// Create an AWT Frame to contain the ParaView widget.
		Frame frame = SWT_AWT.new_Frame(composite);
		frame.setLayout(new BorderLayout());
		frame.add(renderPanel, BorderLayout.CENTER);

		// When the Composite is resized, the ParaView widget will need to be
		// refreshed.
		composite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				renderPanel.dirty();
			}
		});

		// Refresh the ParaView widget, forcing a re-render in case the
		// "low interactive quality" (as in extremely low quality) image was the
		// most recent one sent from the server.
		refreshWidget(true, connection);

		return plotContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionPlotRender#
	 * updatePlotComposite(org.eclipse.swt.widgets.Composite, java.lang.Object)
	 */
	@Override
	protected void updatePlotComposite(Composite plotComposite,
			VtkWebClient connection) throws Exception {

		// Note: When checking the current settings for the rendering, we have
		// to validate them after fetching them, as it is possible to enter an
		// invalid state when two threads update the same values at the same
		// time. For example, thread 1 updates the category and type and
		// refreshes, but just before the refresh thread 2 changes the category.

		// Refresh the ToolBar as necessary.
		refreshToolBar(toolBar);

		// See if the representation has been updated since the last refresh. We
		// should also make sure it is a valid representation.
		final String representation = getPlotRepresentation();
		boolean representationChanged = (representation != null
				&& !representation.equals(plotRepresentation) && getPlotRepresentations()
				.contains(representation));

		// See if the plot category and type have been updated since the last
		// refresh. We should also make sure the current plot category and type
		// are valid before we try to update them.
		final String category = getPlotCategory();
		final String type = getPlotType();
		boolean plotTypeChanged = ((category != null && !category
				.equals(plotCategory)) || (type != null && !type
				.equals(plotType)));
		if (plotTypeChanged && type != null) {
			plotTypeChanged = false;
			String[] types = plot.getPlotTypes().get(category);
			if (types != null) {
				for (int i = 0; !plotTypeChanged && i < types.length; i++) {
					if (type.equals(types[i])) {
						plotTypeChanged = true;
					}
				}
			}
		}

		// We always need to refresh the widget to make sure it's the active
		// one, otherwise we could update the wrong ParaView view.
		if (representationChanged || plotTypeChanged) {
			refreshWidget(false, connection);
		}

		// If the representation changed, update the reference to the currently
		// drawn representation and update the widget.
		if (representationChanged) {
			refreshRepresentation(representation, connection);
			plotRepresentation = representation;
		}

		// If the plot category or type changed (and they are both valid),
		// update the reference to the currently drawn category and type and
		// update the widget.
		if (plotTypeChanged) {
			refreshPlotType(category, type, connection);
			if (category != null) {
				plotCategory = category;
			}
			if (type != null) {
				plotType = type;
			}
		}

		// We always need to refresh the widget after making changes so that
		// those changes appear.
		if (representationChanged || plotTypeChanged) {
			refreshWidget(true, connection);
		}

		return;
	}

	/**
	 * Fills the specified {@code ToolBar} with actions that can be used to
	 * update the rendered plot.
	 * <p>
	 * <b>Note:</b> This method should only be called once, and should be called
	 * at plot creation time.
	 * </p>
	 * 
	 * @param toolBar
	 *            The {@code ToolBarManager} that will be populated.
	 */
	private void fillToolBar(ToolBarManager toolBar,
			final VtkWebClient connection) {

		plotTypeTree = new ActionTree("Plot Types");
		toolBar.add(plotTypeTree.getContributionItem());

		// Add widgets to change the representation.
		representationTree = new ActionTree("Representations");
		toolBar.add(representationTree.getContributionItem());

		// Refresh the ToolBar.
		toolBar.update(true);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.PlotRender#disposePlotComposite(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected void disposePlotComposite(Composite plotComposite) {
		// Dispose of the ActionTrees if necessary.
		if (plotTypeTree != null) {
			plotTypeTree.dispose();
			plotTypeTree = null;
		}
		if (representationTree != null) {
			representationTree.dispose();
			representationTree = null;
		}

		renderPanel = null;

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionPlotRender#
	 * getPreferenceNodeID()
	 */
	@Override
	protected String getPreferenceNodeID() {
		return "org.eclipse.ice.viz.service.paraview.preferences";
	}

	/**
	 * Refreshes (if necessary) the widgets in the {@code ToolBar}.
	 * <p>
	 * Specifically, this method updates both the {@link #plotTypeTree} and
	 * {@link #representationTree}.
	 * </p>
	 * 
	 * @param toolBar
	 *            The {@code ToolBar} to update.
	 */
	private void refreshToolBar(ToolBarManager toolBar) {
		// Create an ActionTree for the available plot categories and types.
		// Selecting one of the leaf nodes should set the category and type for
		// the associated plot.

		if (plotTypeTree.getChildren().isEmpty()) {
			// Get the available plot categories and types. If there is a
			// problem, create an empty map.
			Map<String, String[]> plotTypes;
			try {
				plotTypes = plot.getPlotTypes();
			} catch (Exception e) {
				plotTypes = new HashMap<String, String[]>(0);
			}
			// Loop over the available categories/types and add widgets to set
			// them.
			for (Entry<String, String[]> entry : plotTypes.entrySet()) {
				// Create a tree for the category and add it to the main plot
				// type tree. It should contain all of the available types for
				// the category.
				final String category = entry.getKey();
				ActionTree categoryTree = new ActionTree(category);
				plotTypeTree.add(categoryTree);

				// For each plot type in this category, add an ActionTree that,
				// when clicked, updates the plot category and type.
				for (final String type : entry.getValue()) {
					ActionTree typeTree = new ActionTree(new Action(type) {
						@Override
						public void run() {
							// Update the plot category and type.
							setPlotCategory(category);
							setPlotType(type);
							// Trigger a refresh of the render widget.
							refresh();
						}
					});
					categoryTree.add(typeTree);
				}
			}
		}

		if (representationTree.getChildren().isEmpty()) {
			// Add widgets to change the representation tree.
			// Loop over the available representations and add widgets to set
			// them.
			for (final String representation : getPlotRepresentations()) {
				representationTree.add(new ActionTree(
						new Action(representation) {
							@Override
							public void run() {
								// Update the representation.
								setPlotRepresentation(representation);
								// Trigger a refresh of the render widget.
								refresh();
							}
						}));
			}
		}

		// Refresh the ToolBar.
		toolBar.update(true);

		return;
	}

	/**
	 * Refreshes the widget by activating the associated view (and, of course,
	 * it's related file and representation proxies).
	 * <p>
	 * This method handles the proper JSON RPC to activate the associated view.
	 * </p>
	 * 
	 * @param render
	 *            If true, the widget will be re-rendered. If false, then the
	 *            associated view will only be "activated".
	 * @param connection
	 *            The current connection.
	 * @throws Exception
	 *             If the connection is invalid or the command could not be
	 *             completed.
	 */
	private void refreshWidget(boolean render, VtkWebClient connection)
			throws Exception {
		List<Object> args = new ArrayList<Object>(1);
		args.add(viewId);
		connection.call("activateView", args).get();

		// Force a refresh of the ParaView rendering widget if necessary.
		if (render) {
			System.out.println("Rendering!!!");
			renderPanel.dirty();
		}

		return;
	}

	/**
	 * Refreshes the current plot category and type.
	 * <p>
	 * This method handles the proper JSON RPC to change the plot contents for
	 * the associated view.
	 * </p>
	 * 
	 * @param plotType
	 *            The new plot type.
	 * @param connection
	 *            The current connection.
	 * @throws Exception
	 *             If the connection is invalid or the command could not be
	 *             completed.
	 */
	private void refreshPlotType(String plotCategory, String plotType,
			VtkWebClient connection) throws Exception {

		List<Object> args = new ArrayList<Object>();
		JSONObject object;

		JSONArray updatedProperties = new JSONArray();
		JSONObject statusProperty;
		JSONArray array;

		// Disable the previously selected plot category and type if one was
		// selected necessary.
		if (this.plotCategory != null) {
			statusProperty = new JSONObject();
			statusProperty.put("id", Integer.toString(fileId));
			statusProperty.put("name", plotTypeArrays.get(this.plotCategory));
			array = new JSONArray();
			statusProperty.put("value", array);
			updatedProperties.put(statusProperty);
		}

		// Update the "status" of the mesh/material/cell/point arrays.
		// For the silo file, we want to select Battery_/TemperatureP1.
		statusProperty = new JSONObject();
		statusProperty.put("id", Integer.toString(fileId));
		statusProperty.put("name", plotTypeArrays.get(plotCategory));
		array = new JSONArray();
		array.put(plotType);
		statusProperty.put("value", array);
		updatedProperties.put(statusProperty);

		// Update the properties that were changed.
		args.add(updatedProperties);
		object = connection.call("pv.proxy.manager.update", args).get();
		if (!object.getBoolean("success")) {
			System.out.println("Failed to set the representation: ");
			array = object.getJSONArray("errorList");
			for (int i = 0; i < array.length(); i++) {
				System.out.println(array.get(i));
			}
		}

		// Set the representation to color based on the plot type.
		/*
		 * FIXME The documentation for ParaView is a little unclear about most
		 * of these parameters. The only obvious ones are the representation
		 * proxy ID and the variable name. The rescale option does not appear to
		 * work as expected.
		 */
		args.clear();
		args.add(Integer.toString(repId));
		args.add("ARRAY"); // TODO Not sure when to use SOLID here.
		args.add("POINTS"); // TODO Not sure when to use CELLS here.
		args.add(plotType);
		args.add("Magnitude"); // TODO Not sure when to use Component here.
		args.add(0); // TODO Not sure when to use another value here.
		args.add(true);
		object = connection.call("pv.color.manager.color.by", args).get();

		// Refresh the scalar bars. If this is not done here, then the next call
		// will place the new scalar bar in a different location.
		args.clear();
		args.add(viewId);
		connection.call("refreshScalarBars", args).get();

		// Set the visibility of the legend to true.
		args.clear();
		JSONObject legendVisibilities = new JSONObject();
		legendVisibilities.put(Integer.toString(fileId), true);
		args.add(legendVisibilities);
		object = connection.call("pv.color.manager.scalarbar.visibility.set",
				args).get();
		System.out.println(object.toString(4));

		// Auto-scale the color map to the data.
		args.clear();
		JSONObject scaleOptions = new JSONObject();
		scaleOptions.put("type", "data");
		scaleOptions.put("proxyId", fileId);
		args.add(scaleOptions);
		object = connection.call("pv.color.manager.rescale.transfer.function",
				args).get();
		System.out.println(object.toString(4));

		return;
	}

	/**
	 * Refreshes the current plot representation.
	 * <p>
	 * This method handles the proper JSON RPC to change the plot representation
	 * for the associated view.
	 * </p>
	 * 
	 * @param representation
	 *            The new plot representation.
	 * @param connection
	 *            The current connection.
	 * @throws Exception
	 *             If the connection is invalid or the command could not be
	 *             completed.
	 */
	private void refreshRepresentation(String representation,
			VtkWebClient connection) throws Exception {

		List<Object> args = new ArrayList<Object>();

		// Set the representation proxy to show the mesh as a surface.
		JSONArray updatedProperties = new JSONArray();
		JSONObject repProperty = new JSONObject();
		repProperty.put("id", Integer.toString(repId));
		repProperty.put("name", "Representation");
		repProperty.put("value", representation);
		updatedProperties.put(repProperty);

		// Update the properties that were configured.
		args.add(updatedProperties);
		connection.call("pv.proxy.manager.update", args).get();

		return;
	}

}
