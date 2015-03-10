package org.eclipse.ice.viz.service.paraview;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.kitware.vtk.web.VtkWebClient;
import com.kitware.vtk.web.util.InteractiveRenderPanel;

public class ParaViewPlotRender extends ConnectionPlotRender<VtkWebClient> {

	// TODO Use a thread to throttle the resize events.

	private List<ActionTree> actions;

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
		super(parent, plot);

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
				connection, 4, 100, 1);

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

		// TODO Update the contents of the ParaView widget if necessary.

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
