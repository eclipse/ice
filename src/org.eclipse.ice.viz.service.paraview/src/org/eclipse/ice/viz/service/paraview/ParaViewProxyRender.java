/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan H. Deyton (UT-Battelle, LLC.) - Initial API and implementation 
 *   and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;

import org.eclipse.ice.viz.service.connections.ConnectionPlotRender;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.kitware.vtk.web.VtkWebClient;
import com.kitware.vtk.web.util.InteractiveRenderPanel;

/**
 * TODO Documentation
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewProxyRender extends ConnectionPlotRender<VtkWebClient> {

	// TODO If the data source (i.e. IParaViewProxy) is changed, then a new
	// render panel will need to be created and pointed to that proxy's view ID.

	/**
	 * A reference to the plot conveniently cast to its actual type.
	 */
	private final ParaViewPlot plot;

	/**
	 * The current proxy rendered by this class. The lifecycle of the
	 * {@link #renderPanel} is dependent on this proxy as it is tied to the
	 * proxy's underlying view ID.
	 */
	private IParaViewProxy proxy;

	// ---- UI Components ---- //
	/**
	 * The embedded panel that is used to render the remote ParaView/VTK view.
	 */
	private InteractiveRenderPanel renderPanel;

	// ----------------------- //

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            The parent Composite that contains the plot render.
	 * @param plot
	 *            The rendered ConnectionPlot. This cannot be changed.
	 */
	public ParaViewProxyRender(Composite parent, ParaViewPlot plot) {
		super(parent, plot);

		// Set the reference to the ParaViewPlot.
		this.plot = plot;
	}

	/*
	 * Overrides a method from ConnectionPlotRender.
	 */
	@Override
	protected String getPreferenceNodeID() {
		return "org.eclipse.ice.viz.service.paraview.preferences";
	}

	/*
	 * Overrides a method from ConnectionPlotRender.
	 */
	@Override
	protected Composite createPlotComposite(Composite parent, int style,
			VtkWebClient connection) throws Exception {

		// Get the current proxy used to open the rendered ParaView file.
		IParaViewProxy proxy = plot.getParaViewProxy();

		// If the proxy is not set, throw an exception.
		if (proxy == null) {
			throw new Exception("The file could not be opened and rendered.");
		}

		// ---- Create a container for the render panel. ---- //
		// Create the overall container.
		Composite plotContainer = new Composite(parent, style);
		plotContainer.setBackground(parent.getBackground());
		plotContainer.setFont(parent.getFont());
		GridLayout gridLayout = new GridLayout();
		// Get rid of the default margins (5 px on top, bottom, left, right).
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		plotContainer.setLayout(gridLayout);

		// Since the ParaView widget is built on AWT, we will need to use the
		// SWT_AWT bridge below.

		// Create the Composite that will contain the embedded ParaView widget.
		Composite composite = new Composite(plotContainer, SWT.EMBEDDED
				| SWT.DOUBLE_BUFFERED);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Create the AWT Frame to contain the ParaView widget.
		Frame frame = SWT_AWT.new_Frame(composite);
		frame.setLayout(new BorderLayout());

		// When the Composite is resized, the ParaView widget will need to be
		// refreshed.
		composite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				if (renderPanel != null) {
					renderPanel.dirty();
				}
			}
		});
		// -------------------------------------------------- //

		// Create the render panel.
		this.proxy = proxy;
		renderPanel = new InteractiveRenderPanel(connection, proxy.getViewId(),
				4, 80, 1);
		frame.add(renderPanel, BorderLayout.CENTER);

		// Return the overall container.
		return plotContainer;
	}

	/*
	 * Overrides a method from ConnectionPlotRender.
	 */
	@Override
	protected void updatePlotComposite(Composite plotComposite,
			VtkWebClient connection) throws Exception {

		// Get the current proxy used to open the rendered ParaView file.
		IParaViewProxy proxy = plot.getParaViewProxy();

		// If the proxy is not set, throw an exception.
		if (proxy == null) {
			throw new Exception("The file could not be opened and rendered.");
		}

		// If the proxy is changed, we need to re-create the render panel. This
		// is because the render panel is linked to a specific view ID.
		if (proxy != this.proxy) {
			// Get the parent Frame from the render panel.
			Container frame = renderPanel.getParent();

			// Remove the previous render panel.
			frame.remove(renderPanel);

			// Update the reference to the proxy.
			this.proxy = proxy;

			// Create a new render panel.
			renderPanel = new InteractiveRenderPanel(connection,
					proxy.getViewId(), 4, 80, 1);
			frame.add(renderPanel, BorderLayout.CENTER);
		}

		// Otherwise, we should be able to update the render panel.

		// TODO We'll need to do more than this! We also should do anything if
		// the value hasn't changed.
		proxy.setFeature(getPlotCategory(), getPlotType());

		return;
	}

	/*
	 * Overrides a method from ConnectionPlotRender.
	 */
	@Override
	protected void clearCache() {
		// TODO Auto-generated method stub

	}
}
