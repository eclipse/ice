/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.reactoreditor.lwr;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.client.widgets.moose.ViewFactory;
import org.eclipse.ice.client.widgets.reactoreditor.AnalysisView;
import org.eclipse.ice.client.widgets.reactoreditor.DataSource;
import org.eclipse.ice.client.widgets.reactoreditor.plant.PlantAppState;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.ice.viz.service.jme3.application.ViewAppState;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * This class provides a IAnalysisView for a 3D display of an LWR plant.<br>
 * <br>
 * When a reactor is selected, this View should update the StateBroker with the
 * current selection. It should also feed the properties of the last clicked
 * reactor or the plant itself to the ICE Properties View via
 * {@link AnalysisView#selectionProvider}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PlantAnalysisView extends AnalysisView {

	/**
	 * The name for this type of analysis view. This can be used for the display
	 * text of SWT widgets that reference this view.
	 */
	protected static final String name = "Plant";
	/**
	 * A brief description of this type of analysis view. This can be used for
	 * ToolTips for SWT widgets referencing this view.
	 */
	protected static final String description = "A view of a light water reactor plant.";

	// ---- GUI Components ---- //
	/**
	 * The jME3 <code>ViewAppState</code> used to display a 3D view of an LWR
	 * plant.
	 */
	private final PlantAppState plantApp;
	/**
	 * The ActionTree that lets the user select the plant to display its
	 * properties.
	 */
	private final ActionTree plantProperties;
	// ------------------------ //

	// ---- Current State ---- //
	private static final PlantComposite defaultPlant = new PlantComposite();
	/**
	 * The current plant displayed in the {@link #plantApp}.
	 */
	private PlantComposite plant;

	// ----------------------- //

	/**
	 * The default constructor.
	 * 
	 * @param dataSource
	 *            The data source associated with this view (input, reference,
	 *            comparison).
	 */
	public PlantAnalysisView(DataSource dataSource) {
		super(dataSource);

		// Set the initial plant to the default one.
		plant = defaultPlant;

		// Create the PlantApplication.
		plantApp = new ViewFactory().createPlantView(plant);

		// Populate the list of actions (for ToolBar and context Menu).

		// Add an ActionTree (single button) for viewing the plant's properties.
		plantProperties = new ActionTree(new Action("Plant Properties") {
			@Override
			public void run() {
				// TODO
				// // If the reactor is set, get is properties.
				// IPropertySource properties = new PropertySourceFactory().
				// getPropertySource(reactor);
				//
				// // If it has properties, set the properties in the ICE
				// // Properties View.
				// if (properties != null) {
				// selectionProvider.setSelection(new StructuredSelection(
				// properties));
				// }
			}
		});
		// actions.add(plantProperties);
		// Disable the properties button by default.
		plantProperties.setEnabled(false);

		actions.add(new ActionTree(new Action("Wireframe") {

			private boolean wireframe = false;

			@Override
			public void run() {
				wireframe = !wireframe;
				plantApp.setWireframe(wireframe);
			}
		}));

		return;
	}

	/**
	 * Sets the plant that is displayed in the {@link #plantApp}.
	 * 
	 * @param plant
	 *            The new plant. If null, the view will revert to an empty
	 *            plant.
	 */
	private void setPlant(PlantComposite plant) {

		// If the incoming value is null, set it to the default, empty plant.
		if (plant == null) {
			plant = defaultPlant;
		}

		// We only need to do anything if the value has actually changed.
		if (plant != this.plant) {
			this.plant = plant;

			// Set the plant for the jME3 app.
			plantApp.setPlant(plant);
		}

		return;
	}

	// ---- Implements IAnalysisView ---- //
	/**
	 * Fills out the parent Composite with information and widgets related to
	 * this particular IAnalysisView.
	 * 
	 * @param container
	 *            The Composite containing this IAnalysisView.
	 */
	@Override
	public void createViewContent(Composite container) {
		super.createViewContent(container);

		container.setLayout(new FillLayout());

		// Create the jME3 view of the plant.
		plantApp.createComposite(container);

		return;
	}

	/**
	 * Gets the name for this type of analysis view. This can be used for the
	 * display text of SWT widgets that reference this view.
	 * 
	 * @return The IAnalysisView's name.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Gets a brief description of this type of analysis view. This can be used
	 * for ToolTips for SWT widgets referencing this view.
	 * 
	 * @return The IAnalysisView's description.
	 */
	@Override
	public String getDescription() {
		return description;
	}
	// ---------------------------------- //

	// ---- Implements IStateListener ---- //
	/**
	 * Registers any keys of interest with the current broker.
	 */
	@Override
	public void registerKeys() {

		String key = dataSource + "-" + "plant";
		setPlant((PlantComposite) broker.register(key, this));

		return;
	}

	/**
	 * Unregisters any keys from the current broker.
	 */
	@Override
	public void unregisterKeys() {

		String key = dataSource + "-" + "plant";
		broker.unregister(key, this);

		return;
	}

	/**
	 * This is called by the broker when a key of interest has changed.
	 */
	@Override
	public void update(String key, Object value) {

		logger.info("PlantAnalysisView message: "
				+ "Receiving update for key " + key + ": " + value.toString());

		// Currently, the only value this class listens to is the one that holds
		// the current PlantComposite.
		setPlant((PlantComposite) value);

		return;
	}
	// ----------------------------------- //
}
