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

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertEnabled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.client.widgets.reactoreditor.AnalysisToolComposite;
import org.eclipse.ice.client.widgets.reactoreditor.AnalysisToolComposite.ViewPart;
import org.eclipse.ice.client.widgets.reactoreditor.AnalysisView;
import org.eclipse.ice.client.widgets.reactoreditor.AnalysisWidgetRegistry;
import org.eclipse.ice.client.widgets.reactoreditor.DataSource;
import org.eclipse.ice.client.widgets.reactoreditor.IAnalysisView;
import org.eclipse.ice.client.widgets.reactoreditor.IAnalysisWidgetFactory;
import org.eclipse.ice.client.widgets.reactoreditor.IAnalysisWidgetRegistry;
import org.eclipse.ice.client.widgets.reactoreditor.IStateBrokerHandler;
import org.eclipse.ice.client.widgets.reactoreditor.SelectionProvider;
import org.eclipse.ice.client.widgets.reactoreditor.StateBroker;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class tests what it can of the {@link AnalysisToolComposite}. The
 * AnalysisToolComposite is designed primarily with private fields and methods.
 * The purpose of this test class is to make sure these fields are updated
 * properly over the life of the ATC so that the interface seen by the users is
 * correct.
 * 
 * @author Jordan H. Deyton
 * 
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class AnalysisToolCompositeTester {

	/* --------------------------------------------------- */

	/* ---- Instances necessary for creating the ATC. ---- */
	private static StateBroker broker;
	private static SelectionProvider selectionProvider;
	private static IAnalysisWidgetRegistry registry;
	/* --------------------------------------------------- */

	private ViewPart viewPart1;
	private ViewPart viewPart2;
	private ViewPart viewPart3;

	/**
	 * The AnalysisToolComposite that we will be testing.
	 */
	private AnalysisToolComposite atc;

	/* ---- Fake views, models (data), and factories. ---- */
	// Fake views.
	private class View1 extends AnalysisView {
		public View1(DataSource dataSource) {
			super(dataSource);
		}
	}

	private class View2 extends AnalysisView {
		public View2(DataSource dataSource) {
			super(dataSource);
		}
	}

	private class View3 extends AnalysisView {
		public View3(DataSource dataSource) {
			super(dataSource);
		}
	}

	// Fake models.
	private class One {

	}

	private class Two {

	}

	// Fake factories for the above views and models.
	private final IAnalysisWidgetFactory factoryOne = new IAnalysisWidgetFactory() {
		@Override
		public List<String> getAvailableViews(DataSource dataSource) {
			List<String> views = new ArrayList<String>(2);
			views.add("View1");
			views.add("View2");
			return views;
		}

		@Override
		public IAnalysisView createView(String viewName, DataSource dataSource) {
			IAnalysisView view = null;
			if ("View1".equals(viewName)) {
				view = new View1(dataSource);
			} else {
				view = new View2(dataSource);
			}
			return view;
		}

		@Override
		public List<Class<?>> getModelClasses() {
			List<Class<?>> classes = new ArrayList<Class<?>>(1);
			classes.add(One.class);
			return classes;
		}

		@Override
		public IStateBrokerHandler createStateBrokerHandler() {
			return null;
		}

		@Override
		public IWizard createWizard(Object selection) {
			return null;
		}
	};
	private final IAnalysisWidgetFactory factoryTwo = new IAnalysisWidgetFactory() {
		@Override
		public List<String> getAvailableViews(DataSource dataSource) {
			List<String> views = new ArrayList<String>(2);
			views.add("View1");
			views.add("View2");
			views.add("View3");
			return views;
		}

		@Override
		public IAnalysisView createView(String viewName, DataSource dataSource) {
			IAnalysisView view = null;
			if ("View1".equals(viewName)) {
				view = new View1(dataSource);
			} else if ("View2".equals(viewName)) {
				view = new View2(dataSource);
			} else {
				view = new View3(dataSource);
			}
			return view;
		}

		@Override
		public List<Class<?>> getModelClasses() {
			List<Class<?>> classes = new ArrayList<Class<?>>(1);
			classes.add(Two.class);
			return classes;
		}

		@Override
		public IStateBrokerHandler createStateBrokerHandler() {
			return null;
		}

		@Override
		public IWizard createWizard(Object selection) {
			return null;
		}
	};

	@BeforeClass
	public static void setup() {

		// Setup the dependencies
		broker = new StateBroker();
		selectionProvider = new SelectionProvider();
		registry = new AnalysisWidgetRegistry();

	}

	/**
	 * Test the fields and widgets that need to be updated when views become
	 * available or data sources change.
	 */
	@Test
	public void testFieldsAndWidgets() {

		/**
		 * For this test, we use the fake models, views, and factories above to
		 * test the maintenance of the private widgets and Maps used to keep
		 * track of them.
		 * 
		 * This method first adds Input data for the model One and its
		 * associated views (View1 and View2). It then adds Reference data for
		 * model Two and its views (View1, View2, and View3). Lastly, it
		 * clobbers the Reference data with new data from model One.
		 * 
		 * After each addition, the test checks the sizes and values of the
		 * various Maps. It also checks the top widgets in the StackLayouts used
		 * after simulating SelectionEvents for the view Menu buttons.
		 */

		final SWTWorkbenchBot bot = new SWTWorkbenchBot();

		// Some of these are unused because SWTBot cannot be used for more than
		// one MenuItem click in context menus.
		SWTBotToolbarDropDownButton viewsButton;
		SWTBotMenu inputMenu;
		SWTBotMenu refMenu;
		SWTBotMenu compMenu;
		SWTBotMenu menuItem;

		// Create the ATC.
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				Shell parent = bot.activeShell().widget;
				atc = new AnalysisToolComposite(parent, broker, registry,
						selectionProvider);
			}
		});

		// FIXME - I really don't like doing this, but it makes little sense to
		// make the ATC's fields and methods non-private. We could make them
		// protected, extend ATC, and add our own getters to check things.
		// However, that makes them visible to other classes in the package.

		// Get the two maps used to keep track of the factories and views.
		Map<DataSource, IAnalysisWidgetFactory> factoryMap = (Map<DataSource, IAnalysisWidgetFactory>) getPrivateField(
				atc, "factoryMap");
		Map<String, ViewPart> viewPartMap = (Map<String, ViewPart>) getPrivateField(
				atc, "viewPartMap");
		final Map<DataSource, MenuItem> dataSourceItems = (Map<DataSource, MenuItem>) getPrivateField(
				atc, "dataSourceItems");

		// Get the widgets and variables used to keep track of the top view
		// widgets.
		final StackLayout leftToolBarStack = (StackLayout) getPrivateField(atc,
				"leftToolBarStack");
		final StackLayout viewCompositeStack = (StackLayout) getPrivateField(
				atc, "viewCompositeStack");

		// Initially, the factory and ViewPart maps should be empty.
		assertEquals(0, factoryMap.size());
		assertEquals(0, viewPartMap.size());

		// There should already be one MenuItem for each data source in the view
		// Menu (all with empty Menus and disabled).
		assertEquals(3, dataSourceItems.size());
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				for (MenuItem item : dataSourceItems.values()) {
					assertTrue(!item.getEnabled());
					assertNotNull(item.getMenu());
					assertEquals(0, item.getMenu().getItemCount());
				}
			}
		});

		// Register our two test factories.
		registry.addAnalysisWidgetFactory(factoryOne);
		registry.addAnalysisWidgetFactory(factoryTwo);

		/* ---- Test adding a model for factoryOne. ---- */
		// Add a model that uses factoryOne.
		final One modelOne = new One();
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				atc.setData(DataSource.Input.toString(), modelOne);
			}
		});

		// The factoryMap should now have factoryOne.
		assertEquals(1, factoryMap.size());
		assertSame(factoryOne, factoryMap.get(DataSource.Input));

		// The viewPartMap should now have 2 view parts. Check them.
		assertEquals(2, viewPartMap.size());
		viewPart1 = viewPartMap.get(DataSource.Input.toString() + "-View1");
		assertNotNull(viewPart1);
		assertTrue(viewPart1.getView() instanceof View1);
		viewPart2 = viewPartMap.get(DataSource.Input.toString() + "-View2");
		assertNotNull(viewPart2);
		assertTrue(viewPart2.getView() instanceof View2);

		// The view Menu's input item should have an entry for View1 and View2.
		final MenuItem inputItem = dataSourceItems.get(DataSource.Input);
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				Menu inputMenu = inputItem.getMenu();
				assertEquals(2, inputMenu.getItemCount());
				assertEquals("View1", inputMenu.getItem(0).getText());
				assertEquals("View2", inputMenu.getItem(1).getText());

				// Set the active view and check the top Composite/ToolBar.
				// This command simulates a button click on Input -> View1.
				inputMenu.getItem(0)
						.notifyListeners(SWT.Selection, new Event());
				assertSame(viewPart1.getToolBar(), leftToolBarStack.topControl);
				assertSame(viewPart1.getContainer(),
						viewCompositeStack.topControl);
				// Repeat this for View2.
				inputMenu.getItem(1)
						.notifyListeners(SWT.Selection, new Event());
				assertSame(viewPart2.getToolBar(), leftToolBarStack.topControl);
				assertSame(viewPart2.getContainer(),
						viewCompositeStack.topControl);
			}
		});
		/* --------------------------------------------- */

		/* ---- Test adding a model for factoryTwo. ---- */
		// Add a model that uses factoryTwo.
		final Two modelTwo = new Two();
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				atc.setData(DataSource.Reference.toString(), modelTwo);
			}
		});

		// The factoryMap should now have factoryTwo.
		assertEquals(2, factoryMap.size());
		assertSame(factoryOne, factoryMap.get(DataSource.Input));
		assertSame(factoryTwo, factoryMap.get(DataSource.Reference));

		// The viewPartMap should now have 5 view parts. Check the new ones.
		assertEquals(5, viewPartMap.size());
		viewPart1 = viewPartMap.get(DataSource.Reference.toString() + "-View1");
		assertNotNull(viewPart1);
		assertTrue(viewPart1.getView() instanceof View1);
		viewPart2 = viewPartMap.get(DataSource.Reference.toString() + "-View2");
		assertNotNull(viewPart2);
		assertTrue(viewPart2.getView() instanceof View2);
		viewPart3 = viewPartMap.get(DataSource.Reference.toString() + "-View3");
		assertNotNull(viewPart3);
		assertTrue(viewPart3.getView() instanceof View3);

		// The view Menu's ref item should have an entry for View1 and View2.
		final MenuItem refItem = dataSourceItems.get(DataSource.Reference);
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				Menu refMenu = refItem.getMenu();
				assertEquals(3, refMenu.getItemCount());
				assertEquals("View1", refMenu.getItem(0).getText());
				assertEquals("View2", refMenu.getItem(1).getText());
				assertEquals("View3", refMenu.getItem(2).getText());
			}
		});
		// Make sure the Input menu hasn't changed.
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				Menu inputMenu = inputItem.getMenu();
				assertEquals(2, inputMenu.getItemCount());
				assertEquals("View1", inputMenu.getItem(0).getText());
				assertEquals("View2", inputMenu.getItem(1).getText());
			}
		});

		// Set the active view and check the top Composite/ToolBar.
		// This command simulates a button click on Reference -> View1.
		// FIXME SWTBot will work on the first MenuItem click, but not on
		// subsequent ones. After this, we have to manually "simulate" mouse
		// clicks on the buttons.
		viewsButton = bot.toolbarDropDownButton("Views");
		refMenu = viewsButton.menuItem("Reference");
		assertEnabled(refMenu);
		menuItem = refMenu.menu("View1");
		assertEnabled(menuItem);
		menuItem.click();
		// Check that viewPart1 is at the front.
		assertSame(viewPart1.getToolBar(), leftToolBarStack.topControl);
		assertSame(viewPart1.getContainer(), viewCompositeStack.topControl);
		// Repeat this for View2.
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				Menu refMenu = refItem.getMenu();
				refMenu.getItem(1).notifyListeners(SWT.Selection, new Event());
			}
		});
		assertSame(viewPart2.getToolBar(), leftToolBarStack.topControl);
		assertSame(viewPart2.getContainer(), viewCompositeStack.topControl);
		// Repeat this for View3.
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				Menu refMenu = refItem.getMenu();
				refMenu.getItem(2).notifyListeners(SWT.Selection, new Event());
			}
		});
		assertSame(viewPart3.getToolBar(), leftToolBarStack.topControl);
		assertSame(viewPart3.getContainer(), viewCompositeStack.topControl);

		// Make sure we can still select something from the Input menu!
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				Menu inputMenu = inputItem.getMenu();
				inputMenu.getItem(1)
						.notifyListeners(SWT.Selection, new Event());
			}
		});
		assertSame(viewPartMap.get(DataSource.Input.toString() + "-View2")
				.getToolBar(), leftToolBarStack.topControl);
		assertSame(viewPartMap.get(DataSource.Input.toString() + "-View2")
				.getContainer(), viewCompositeStack.topControl);
		/* --------------------------------------------- */

		/* ---- Overwrite the Reference with new modelOne data. ---- */
		// Add a reference model using an instance of model One.
		final One clobberinTime = new One();
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				atc.setData(DataSource.Reference.toString(), clobberinTime);
			}
		});

		// The factoryMap should now have factoryOne twice.
		assertEquals(2, factoryMap.size());
		assertSame(factoryOne, factoryMap.get(DataSource.Input));
		assertSame(factoryOne, factoryMap.get(DataSource.Reference));

		// The viewPartMap should now have 4 view parts. Check the new ones.
		assertEquals(4, viewPartMap.size());
		assertNotSame(viewPart1,
				viewPartMap.get(DataSource.Reference.toString() + "-View1"));
		viewPart1 = viewPartMap.get(DataSource.Reference.toString() + "-View1");
		assertNotNull(viewPart1);
		assertTrue(viewPart1.getView() instanceof View1);
		assertNotSame(viewPart2,
				viewPartMap.get(DataSource.Reference.toString() + "-View2"));
		viewPart2 = viewPartMap.get(DataSource.Reference.toString() + "-View2");
		assertNotNull(viewPart2);
		assertTrue(viewPart2.getView() instanceof View2);

		// Make sure the ATC/factory is not reusing the views or ViewParts.
		assertNotSame(viewPart1,
				viewPartMap.get(DataSource.Input.toString() + "-View1"));
		assertNotSame(viewPart1.getView(),
				viewPartMap.get(DataSource.Input.toString() + "-View1")
						.getView());
		assertNotSame(viewPart2,
				viewPartMap.get(DataSource.Input.toString() + "-View2"));
		assertNotSame(viewPart2.getView(),
				viewPartMap.get(DataSource.Input.toString() + "-View2")
						.getView());

		// Make sure there is no longer a Reference-View3.
		assertNull(viewPartMap.get(DataSource.Reference.toString() + "-View3"));

		// The view Menu's ref item should have an entry for View1 and View2.
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				Menu refMenu = refItem.getMenu();
				assertEquals(2, refMenu.getItemCount());
				assertEquals("View1", refMenu.getItem(0).getText());
				assertEquals("View2", refMenu.getItem(1).getText());
			}
		});

		// Set the active view and check the top Composite/ToolBar.
		// This command simulates a button click on Reference -> View1.
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				refItem.getMenu().getItem(0)
						.notifyListeners(SWT.Selection, new Event());
			}
		});
		assertSame(viewPart1.getToolBar(), leftToolBarStack.topControl);
		assertSame(viewPart1.getContainer(), viewCompositeStack.topControl);
		// Make sure we can still select something from the Input menu!
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				inputItem.getMenu().getItem(0)
						.notifyListeners(SWT.Selection, new Event());
			}
		});
		assertSame(viewPartMap.get(DataSource.Input.toString() + "-View1")
				.getToolBar(), leftToolBarStack.topControl);
		assertSame(viewPartMap.get(DataSource.Input.toString() + "-View1")
				.getContainer(), viewCompositeStack.topControl);
		// Repeat the first step for View2.
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				refItem.getMenu().getItem(1)
						.notifyListeners(SWT.Selection, new Event());
			}
		});
		assertSame(viewPart2.getToolBar(), leftToolBarStack.topControl);
		assertSame(viewPart2.getContainer(), viewCompositeStack.topControl);
		/* --------------------------------------------------------- */

		return;
	}

	/**
	 * We can use this method to get private fields (for reading purposes) from
	 * the tested class.
	 * 
	 * @param from
	 *            The object whose private fields we will be accessing.
	 * @param fieldName
	 *            The name of the private field to access.
	 * @return An Object corresponding to the private field.
	 */
	private Object getPrivateField(Object from, String fieldName) {

		Object object = null;

		Field field;
		try {
			field = from.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			object = field.get(atc);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if (object == null) {
			fail("Could not retrieve private field \"" + fieldName
					+ "\" from the AnalysisToolComposite instance.");
		}
		return object;
	}
}
