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
package org.eclipse.ice.client.widgets.reactoreditor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.client.widgets.reactoreditor.AnalysisWidgetRegistry;
import org.eclipse.ice.client.widgets.reactoreditor.DataSource;
import org.eclipse.ice.client.widgets.reactoreditor.IAnalysisView;
import org.eclipse.ice.client.widgets.reactoreditor.IAnalysisWidgetFactory;
import org.eclipse.ice.client.widgets.reactoreditor.IAnalysisWidgetRegistry;
import org.eclipse.ice.client.widgets.reactoreditor.IStateBrokerHandler;
import org.eclipse.jface.wizard.IWizard;
import org.junit.Test;

/**
 * This class tests the AnalysisWidgetRegistry implementation.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class AnalysisWidgetRegistryTester {

	/* ---- Test classes used by factories in the registry. ---- */
	private class Bologna {
	}

	private class HamAndSpam {
	}

	private class GreenEggs {
	}

	/* --------------------------------------------------------- */

	/* ---- Fake factories for the above classes. ---- */
	private IAnalysisWidgetFactory fakeBolognaFactory = new IAnalysisWidgetFactory() {
		public List<String> getAvailableViews(DataSource dataSource) {
			return null;
		}

		public IAnalysisView createView(String viewName, DataSource dataSource) {
			return null;
		}

		public List<Class<?>> getModelClasses() {
			List<Class<?>> classes = new ArrayList<Class<?>>(1);
			classes.add(Bologna.class);
			return classes;
		}

		public IStateBrokerHandler createStateBrokerHandler() {
			return null;
		}

		public IWizard createWizard(Object selection) {
			return null;
		}
	};
	private IAnalysisWidgetFactory fakeHamAndSpamFactory = new IAnalysisWidgetFactory() {
		public List<String> getAvailableViews(DataSource dataSource) {
			return null;
		}

		public IAnalysisView createView(String viewName, DataSource dataSource) {
			return null;
		}

		public List<Class<?>> getModelClasses() {
			List<Class<?>> classes = new ArrayList<Class<?>>(1);
			classes.add(HamAndSpam.class);
			return classes;
		}

		public IStateBrokerHandler createStateBrokerHandler() {
			return null;
		}

		public IWizard createWizard(Object selection) {
			return null;
		}
	};
	private IAnalysisWidgetFactory fakeGreenEggsFactory = new IAnalysisWidgetFactory() {
		public List<String> getAvailableViews(DataSource dataSource) {
			return null;
		}

		public IAnalysisView createView(String viewName, DataSource dataSource) {
			return null;
		}

		public List<Class<?>> getModelClasses() {
			List<Class<?>> classes = new ArrayList<Class<?>>(1);
			classes.add(GreenEggs.class);
			return classes;
		}

		public IStateBrokerHandler createStateBrokerHandler() {
			return null;
		}

		public IWizard createWizard(Object selection) {
			return null;
		}
	};
	/* ----------------------------------------------- */

	/* ---- This factory tries to use the Bologna class (again). ---- */
	private IAnalysisWidgetFactory fakeImitationBolognaFactory = new IAnalysisWidgetFactory() {
		public List<String> getAvailableViews(DataSource dataSource) {
			return null;
		}

		public IAnalysisView createView(String viewName, DataSource dataSource) {
			return null;
		}

		public List<Class<?>> getModelClasses() {
			List<Class<?>> classes = new ArrayList<Class<?>>(1);
			classes.add(Bologna.class);
			return classes;
		}

		public IStateBrokerHandler createStateBrokerHandler() {
			return null;
		}

		public IWizard createWizard(Object selection) {
			return null;
		}
	};
	/* -------------------------------------------------------------- */

	/* ---- This factory does not return a valid class. ---- */
	private IAnalysisWidgetFactory fakeBrokenFactory = new IAnalysisWidgetFactory() {
		public List<String> getAvailableViews(DataSource dataSource) {
			return null;
		}

		public IAnalysisView createView(String viewName, DataSource dataSource) {
			return null;
		}

		public List<Class<?>> getModelClasses() {
			return null;
		}

		public IStateBrokerHandler createStateBrokerHandler() {
			return null;
		}

		public IWizard createWizard(Object selection) {
			return null;
		}
	};

	/* ----------------------------------------------------- */

	/**
	 * This case tests the registration of the factories defined above.
	 */
	@Test
	public void checkAnalysisWidgetFactoryRegistration() {

		// Initialize a registry.
		IAnalysisWidgetRegistry registry = new AnalysisWidgetRegistry();

		// Initialize an instance of Bologna.
		Bologna bologna = new Bologna();

		// There should be no entry in the registry, so expect null return
		// values.
		assertNull(registry.getAnalysisWidgetFactory(null));
		assertNull(registry.getAnalysisWidgetFactory(Bologna.class));
		assertNull(registry.getAnalysisWidgetFactory(bologna.getClass()));

		// Try adding the bologna factory.
		registry.addAnalysisWidgetFactory(fakeBolognaFactory);
		assertEquals(fakeBolognaFactory,
				registry.getAnalysisWidgetFactory(Bologna.class));
		assertEquals(fakeBolognaFactory,
				registry.getAnalysisWidgetFactory(bologna.getClass()));

		// Try adding the broken factory (which returns null for its class).
		registry.addAnalysisWidgetFactory(fakeBrokenFactory);
		assertNull(registry.getAnalysisWidgetFactory(null));

		// Try adding two more factories.
		registry.addAnalysisWidgetFactory(fakeHamAndSpamFactory);
		registry.addAnalysisWidgetFactory(fakeGreenEggsFactory);
		assertEquals(fakeBolognaFactory,
				registry.getAnalysisWidgetFactory(Bologna.class));
		assertEquals(fakeBolognaFactory,
				registry.getAnalysisWidgetFactory(bologna.getClass()));
		assertEquals(fakeHamAndSpamFactory,
				registry.getAnalysisWidgetFactory(fakeHamAndSpamFactory
						.getModelClasses().get(0)));
		assertEquals(fakeGreenEggsFactory,
				registry.getAnalysisWidgetFactory(GreenEggs.class));

		// Try overwriting the fakeBolognaFactory.
		registry.addAnalysisWidgetFactory(fakeImitationBolognaFactory);
		assertNotSame(fakeBolognaFactory,
				registry.getAnalysisWidgetFactory(Bologna.class));
		assertEquals(fakeImitationBolognaFactory,
				registry.getAnalysisWidgetFactory(Bologna.class));
		assertEquals(fakeImitationBolognaFactory,
				registry.getAnalysisWidgetFactory(bologna.getClass()));

		return;
	}

}
