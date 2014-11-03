/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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

import org.eclipse.ice.client.widgets.reactoreditor.DataSource;
import org.eclipse.ice.client.widgets.reactoreditor.IAnalysisView;
import org.eclipse.ice.client.widgets.reactoreditor.IAnalysisWidgetFactory;
import org.eclipse.ice.client.widgets.reactoreditor.IStateBrokerHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.bwr.BWReactor;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;
import org.eclipse.jface.wizard.IWizard;

/**
 * This class implements the IAnalysisWidgetFactory interface for Reactor
 * analysis.
 * 
 * @author djg
 * 
 */
public class AnalysisWidgetFactory implements IAnalysisWidgetFactory {

	/**
	 * An IViewFactory factory needs to be able to initialize an IAnalysisView.
	 * The necessary parameters for an IAnalysisView are passed into the method
	 * createView().
	 * 
	 * @author djg
	 * 
	 */
	private interface IViewFactory {
		/**
		 * Creates an IAnalysisView.
		 * 
		 * @param dataSource
		 *            The data source for the IAnalysisView.
		 * @return An implementation of IAnalysisView.
		 */
		public IAnalysisView createView(DataSource dataSource);
	}

	/**
	 * A manually ordered List of IAnalysisView names.
	 */
	private final List<String> viewNames;
	/**
	 * A Map of IViewFactories used to instantiate IAnalysisViews keyed on the
	 * IAnalysisView names.
	 */
	private final Map<String, IViewFactory> viewFactoryMap;

	/**
	 * The default constructor.
	 */
	public AnalysisWidgetFactory() {

		// The number of views we support.
		int viewCount = 1;

		// Populate the List of names in the order that we want them to appear.
		viewNames = new ArrayList<String>(viewCount);
		viewNames.add(PlantAnalysisView.name);
		viewNames.add(CoreAnalysisView.name);
		viewNames.add(AssemblyAnalysisView.name);
		viewNames.add(RodAnalysisView.name);
		viewNames.add(PlotAnalysisView.name);

		// Populate the Map of factories. For each view, add a new factory that
		// can create that particular view.
		viewFactoryMap = new HashMap<String, IViewFactory>(viewCount);
		viewFactoryMap.put(PlantAnalysisView.name, new IViewFactory() {
			public IAnalysisView createView(DataSource dataSource) {
				return new PlantAnalysisView(dataSource);
			}
		});
		viewFactoryMap.put(CoreAnalysisView.name, new IViewFactory() {
			public IAnalysisView createView(DataSource dataSource) {
				return new CoreAnalysisView(dataSource);
			}
		});
		viewFactoryMap.put(AssemblyAnalysisView.name, new IViewFactory() {
			public IAnalysisView createView(DataSource dataSource) {
				return new AssemblyAnalysisView(dataSource);
			}
		});
		viewFactoryMap.put(RodAnalysisView.name, new IViewFactory() {
			public IAnalysisView createView(DataSource dataSource) {
				return new RodAnalysisView(dataSource);
			}
		});
		viewFactoryMap.put(PlotAnalysisView.name, new IViewFactory() {
			public IAnalysisView createView(DataSource dataSource) {
				IAnalysisView view = null;
				if (dataSource == DataSource.Comparison) {
					// For comparison data, we must return a special view!
					view = new ComparisonPlotAnalysisView(dataSource);
				} else {
					view = new PlotAnalysisView(dataSource);
				}
				return view;
			}
		});

		return;
	}

	/* ---- Implements IAnalysisWidgetFactory. ---- */
	public List<String> getAvailableViews(DataSource dataSource) {
		// We don't want the main package to modify our original list, so make
		// the returned list unmodifiable.
		if (dataSource == DataSource.Comparison) {
			return new ArrayList<String>(Arrays.asList(PlotAnalysisView.name));
		} else {
			return Collections.unmodifiableList(viewNames);
		}
	}

	public IAnalysisView createView(String viewName, DataSource dataSource) {
		IAnalysisView view = null;

		// Grab the factory for this view type and create its IAnalysisView.
		IViewFactory factory = viewFactoryMap.get(viewName);
		if (factory != null) {
			view = factory.createView(dataSource);
		}
		return view;
	}

	public List<Class<?>> getModelClasses() {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(PressurizedWaterReactor.class);
		classes.add(BWReactor.class);
		classes.add(FuelAssembly.class);
		classes.add(RodClusterAssembly.class);
		classes.add(LWRRod.class);
		classes.add(ControlBank.class);
		classes.add(IncoreInstrument.class);
		classes.add(Tube.class);
		classes.add(PlantComposite.class);
		return classes;
	}

	public IStateBrokerHandler createStateBrokerHandler() {
		return new StateBrokerHandler();
	}

	public IWizard createWizard(Object selection) {
		// Use a WizardProvider to create the wizard
		WizardProvider provider = new WizardProvider();
		return provider.getWizard(selection);
	}
	/* -------------------------------------------- */

}
