package org.eclipse.ice.viz.service.paraview;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.viz.service.connections.ConnectionPlotRender;
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

	private List<ActionTree> actions;

	private final ReadLock readLock;
	private final WriteLock writeLock;

	private String plotCategory;
	private String plotType;

	private int viewId;
	private int fileId;
	private int repId;

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

		ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
		readLock = lock.readLock();
		writeLock = lock.writeLock();

		this.viewId = viewId;
		this.fileId = fileId;
		this.repId = repId;

		return;
	}

	/**
	 * Overrides the default behavior so that the rendering widget is only
	 * updated if the category changes to a new value.
	 */
	@Override
	public void setPlotCategory(String category) {
		writeLock.lock();
		try {
			// if (category != null && !category.equals(getPlotCategory())) {
			// plotChanged = true;
			// super.setPlotCategory(category);
			// }
			super.setPlotCategory(category);
		} finally {
			writeLock.unlock();
		}
		return;
	}

	/**
	 * Overrides the default behavior so that the rendering widget is only
	 * updated if the type changes to a new value.
	 */
	@Override
	public void setPlotType(String type) {
		writeLock.lock();
		try {
			// if (type != null && !type.equals(getPlotType())) {
			// plotChanged = true;
			// super.setPlotType(type);
			// }
			super.setPlotType(type);
		} finally {
			writeLock.unlock();
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
		ToolBar toolBar = toolBarManager.createControl(plotContainer);
		toolBar.setBackground(parent.getBackground());
		toolBar.setFont(parent.getFont());
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		fillToolBar(toolBarManager);

		// Since the ParaView widget is built on AWT, we will need to use the
		// SWT_AWT bridge below.

		// Create the Composite that will contain the embedded ParaView widget.
		final Composite composite = new Composite(plotContainer, SWT.EMBEDDED
				| SWT.DOUBLE_BUFFERED);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Create the ParaView widget.
		final InteractiveRenderPanel renderPanel = new InteractiveRenderPanel(
				connection, viewId, 4, 80, 1);

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

		// FIXME Should this be done here on the UI thread, or elsewhere?

		// TODO Update the contents of the ParaView widget if necessary.
		boolean plotTypeChanged = false;
		String category = null;
		String type = null;
		readLock.lock();
		try {
			category = getPlotCategory();
			type = getPlotType();

			if (category != null && !category.equals(plotCategory)) {
				plotCategory = category;
				plotTypeChanged = true;
			}
			if (type != null && !type.equals(plotType)) {
				plotType = type;
				plotTypeChanged = true;
			}
		} finally {
			readLock.unlock();
		}

		final String plotType = "Battery_/TemperatureP1";
		
		// if (plotTypeChanged) {
		List<Object> args = new ArrayList<Object>();
		JSONObject object;

		// Make sure this view is the active one.
		args.add(viewId);
		connection.call("activateView", args).get();
		
		// Set the representation proxy to show the mesh as
		// "Surface With Edges".
		args.clear();
		JSONArray updatedProperties = new JSONArray();
		JSONObject repProperty = new JSONObject();
		repProperty.put("id", Integer.toString(repId));
		repProperty.put("name", "Representation");
		repProperty.put("value", "Surface");
		updatedProperties.put(repProperty);

		// Update the "status" of the mesh/material/cell/point arrays.
		// For the silo file, we want to select Battery_/TemperatureP1.
		JSONObject pointStatusProperty = new JSONObject();
		pointStatusProperty.put("id", Integer.toString(fileId));
		pointStatusProperty.put("name", "PointArrayStatus");
		JSONArray pointArrays = new JSONArray();
		pointArrays.put(plotType);
		pointStatusProperty.put("value", pointArrays);
		updatedProperties.put(pointStatusProperty);

		// Update the properties that were configured.
		args.add(updatedProperties);
		object = connection.call("pv.proxy.manager.update", args).get();
		if (!object.getBoolean("success")) {
			System.out.println("Failed to set the representation: ");
			JSONArray array = object.getJSONArray("errorList");
			for (int i = 0; i < array.length(); i++) {
				System.out.println(array.get(i));
			}
		}

		// Set the representation to color based on the plot type.
		args.clear();
		args.add(Integer.toString(repId));
		args.add("ARRAY");
		args.add("POINTS");
		args.add(plotType);
		args.add("Magnitude");
		args.add(0);
		args.add(true);
		object = connection.call("pv.color.manager.color.by", args).get();

		// Set the visibility of the legend to true.
		args.clear();
		JSONObject legendVisibilities = new JSONObject();
		legendVisibilities.put(Integer.toString(fileId), true);
		args.add(legendVisibilities);
		object = connection.call(
				"pv.color.manager.scalarbar.visibility.set", args)
				.get();
		System.out.println(object.toString(4));

		// Auto-scale the color map to the data.
		args.clear();
		JSONObject scaleOptions = new JSONObject();
		scaleOptions.put("type", "data");
		scaleOptions.put("proxyId", fileId);
		args.add(scaleOptions);
		object = connection.call(
				"pv.color.manager.rescale.transfer.function", args)
				.get();
		System.out.println(object.toString(4));

		// // ---- Set the plot representation style. ---- //
		// // Set the representation proxy to show the mesh as
		// // "Surface With Edges".
		// args.clear();
		// JSONArray updatedProperties = new JSONArray();
		// JSONObject repProperty = new JSONObject();
		// repProperty.put("id", Integer.toString(repId));
		// repProperty.put("name", "Representation");
		// repProperty.put("value", "Surface");
		// updatedProperties.put(repProperty);
		//
		// // Update the "status" of the mesh/material/cell/point arrays.
		// // For the silo file, we want to select Battery_/TemperatureP1.
		// JSONObject pointStatusProperty = new JSONObject();
		// pointStatusProperty.put("id", Integer.toString(fileId));
		// pointStatusProperty.put("name", "PointArrayStatus");
		// JSONArray pointArrays = new JSONArray();
		// pointArrays.put("Battery_/TemperatureP1");
		// pointStatusProperty.put("value", pointArrays);
		// updatedProperties.put(pointStatusProperty);
		//
		// // Update the properties that were configured.
		// args.add(updatedProperties);
		// object = connection.call("pv.proxy.manager.update", args).get();
		// if (!object.getBoolean("success")) {
		// System.out.println("Failed to set the representation: ");
		// JSONArray array = object.getJSONArray("errorList");
		// for (int i = 0; i < array.length(); i++) {
		// System.out.println(array.get(i));
		// }
		// }
		// // -------------------------------------------- //
		//
		// // ---- Set the plot type via the ColorManager. ---- //
		// args.clear();
		// args.add(Integer.toString(repId)); // The rep ID to update.
		// args.add("ARRAY"); // The "color mode".
		// args.add("POINTS"); // The "array location".
		// args.add("Battery_/TemperatureP1"); // The name of the array to plot,
		// in this case the
		// // plot type. The category does not need to be
		// // specified.
		// args.add("Magnitude"); // The default "vector mode".
		// args.add(0); // The default "vector component".
		// args.add(true); // This is supposed to rescale the colors.
		//
		// // Send these arguments to update the data set that the view plots.
		// connection.call("pv.color.manager.color.by", args).get();
		//
		// // Auto-scale the color map to the data.
		// // args.clear();
		// // JSONObject scaleOptions = new JSONObject();
		// // scaleOptions.put("type", "data");
		// // scaleOptions.put("proxyId", fileId);
		// // args.add(scaleOptions);
		// // connection.call("pv.color.manager.rescale.transfer.function",
		// // args)
		// // .get();
		// // ------------------------------------------------- //
		// }

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
	private void fillToolBar(ToolBarManager toolBar) {
		actions = new ArrayList<ActionTree>();

		ActionTree plotTypesTree = new ActionTree("Plot Types");
		actions.add(plotTypesTree);

		// Create an ActionTree for the available plot categories and types.
		// Selecting one of the leaf nodes should set the category and type for
		// the associated plot.
		try {
			Map<String, String[]> plotTypes = plot.getPlotTypes();
			for (Entry<String, String[]> entry : plotTypes.entrySet()) {
				// Create a tree for the category and add it to the main plot
				// type tree. It should contain all of the available types for
				// the category.
				final String category = entry.getKey();
				ActionTree categoryTree = new ActionTree(category);
				plotTypesTree.add(categoryTree);

				// For each plot type in this category, add an ActionTree that,
				// when clicked, updates the plot category and type.
				for (final String type : entry.getValue()) {
					ActionTree typeTree = new ActionTree(new Action(type) {
						@Override
						public void run() {
							try {
								plot.draw(category, type, parent);
							} catch (Exception e) {
								System.err
										.println("IPlot error: "
												+ "Failed to update the plot type to category \""
												+ category + "\" and type \""
												+ type + "\".");
							}
						}
					});
					categoryTree.add(typeTree);
				}
			}
		} catch (Exception e) {
			plotTypesTree.setEnabled(false);
		}

		// TODO Add widgets to change the representation.

		// Populate the ToolBarManager with the ActionTrees.
		for (ActionTree tree : actions) {
			toolBar.add(tree.getContributionItem());
		}
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
		if (actions != null) {
			for (ActionTree tree : actions) {
				tree.dispose();
			}
			actions.clear();
		}

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

}
