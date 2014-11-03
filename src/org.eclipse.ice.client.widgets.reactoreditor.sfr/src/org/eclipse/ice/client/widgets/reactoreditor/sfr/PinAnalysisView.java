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
package org.eclipse.ice.client.widgets.reactoreditor.sfr;

import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.client.common.ActionTree;

import org.eclipse.ice.client.widgets.reactoreditor.AnalysisView;
import org.eclipse.ice.client.widgets.reactoreditor.Circle;
import org.eclipse.ice.client.widgets.reactoreditor.DataSource;
import org.eclipse.ice.client.widgets.reactoreditor.sfr.PinFigure.DisplayType;
import org.eclipse.ice.client.widgets.reactoreditor.sfr.properties.PropertySourceFactory;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.core.assembly.Ring;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRPin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * This class provides an IAnalysisView for {@linkplain SFRPin}s. It displays
 * the radial and axial structure of the pin and can also display the pin's
 * data.<br>
 * <br>
 * When a Ring inside the pin is clicked, it should send a selection event to
 * the ICE Properties View via its {@linkplain AnalysisView#selectionProvider
 * ISelectionProvider}.
 * 
 * @author Jordan
 * 
 */
public class PinAnalysisView extends AnalysisView {

	/**
	 * The name for this type of analysis view. This can be used for the display
	 * text of SWT widgets that reference this view.
	 */
	protected static final String name = "Fuel Pin";
	/**
	 * A brief description of this type of analysis view. This can be used for
	 * ToolTips for SWT widgets referencing this view.
	 */
	protected static final String description = "A view of a sodium-cooled fast reactor fuel pin.";

	/* ---- GUI Components ---- */
	/**
	 * The SashForm contains the radial canvas (left) and the axial level
	 * selectors (right).
	 */
	private SashForm sashForm;

	/**
	 * An SWT Canvas containing the radial image of the pin (concentric circles
	 * of different materials).
	 */
	private Canvas radialCanvas;
	/**
	 * The custom Draw2D figure for the Pin. It resides in the
	 * {@link PinAnalysisView#radialCanvas radialCanvas}.
	 */
	private LonePinFigure radialFigure;

	/**
	 * This Composite contains the widgets used to select the axial level. This
	 * includes a Scale, a Spinner, and a Figure.
	 */
	private Composite axialComposite;
	/**
	 * This canvas displays an axial view of the currently-selected component.
	 */
	private Canvas axialCanvas;
	/**
	 * This is the axial view Figure drawn on the right.
	 */
	// FIXME Implement an AxialPinFigure.
	private AxialPinFigure axialFigure;
	/**
	 * The Scale used to select the axial level.
	 */
	private Scale axialScale;
	/**
	 * The Spinner used to select the axial level.
	 */
	private Spinner axialSpinner;

	/**
	 * The ActionTree that holds the current features available in the assembly.
	 * This tree needs to be refreshed based on the current features available
	 * for the displayed pin.
	 */
	private final ActionTree featureTree;
	/**
	 * The ActionTree that lets the user select the rod to display its
	 * properties.
	 */
	private final ActionTree componentProperties;

	/**
	 * This enum is used to determine the source of changes to the axial level.
	 * It is particularly useful to smooth out the scale widget.
	 * 
	 * @author djg
	 * 
	 */
	public enum AxialLevelWidget {
		/**
		 * Some other input is setting the axial level.
		 */
		NONE,
		/*
		 * The scale is setting the axial level.
		 */
		SCALE,
		/**
		 * The spinner is setting the axial level.
		 */
		SPINNER;
	}

	/* ------------------------ */

	/* ---- Current State ---- */
	/**
	 * The currently selected assembly component.
	 */
	private SFRComponent component;
	/**
	 * The data provider for the currently selected assembly component.
	 */
	private IDataProvider dataProvider;

	/**
	 * A Map of the number of axial levels per feature.
	 */
	private final Map<String, Integer> featureMap;
	/**
	 * The current feature of data to display.
	 */
	private String feature;
	/**
	 * The current axial level of the feature data to display.
	 */
	private int axialLevel;
	/**
	 * The maximum value (0-indexed) for the axial level.
	 */
	private int maxAxialLevel;

	/* ----------------------- */

	/**
	 * The default constructor.
	 * 
	 * @param dataSource
	 *            The data source associated with this view (input, reference,
	 *            comparison).
	 */
	public PinAnalysisView(DataSource dataSource) {
		super(dataSource);
		// Initialize the final lists, maps, etc.
		featureMap = new HashMap<String, Integer>();

		// Set some defaults.
		feature = null;
		axialLevel = 0;
		maxAxialLevel = 0;

		// Populate the list of actions (for ToolBar and context Menu).

		// Add the display types.
		ActionTree displayTypes = new ActionTree("Display Type");
		actions.add(displayTypes);
		for (final DisplayType type : DisplayType.values()) {
			Action action = new Action(type.name) {
				@Override
				public void run() {
					setDisplayType(type);
				}
			};
			displayTypes.add(new ActionTree(action));
		}

		// Create the feature tree.
		featureTree = new ActionTree("Data Feature");
		actions.add(featureTree);

		// Add an ActionTree (single button) for viewing the pin properties.
		componentProperties = new ActionTree(new Action("Pin Properties") {
			@Override
			public void run() {
				// If the reactor is set, get is properties.
				IPropertySource properties = new PropertySourceFactory()
						.getPropertySource(component);

				// If it has properties, set the properties in the ICE
				// Properties View.
				if (properties != null) {
					selectionProvider.setSelection(new StructuredSelection(
							properties));
				}
			}
		});
		actions.add(componentProperties);
		// Disable the pin properties button by default.
		componentProperties.setEnabled(false);

		// Add an ActionTree (single button) for saving the viewer image.
		ActionTree saveImage = new ActionTree(new Action("Save Image") {
			@Override
			public void run() {
				saveCanvasImage(radialCanvas);
			}
		});
		actions.add(saveImage);

		return;
	}

	/**
	 * Sets the component displayed in this view.
	 * 
	 * @param component
	 *            The currently selected assembly component.
	 */
	private void setComponent(SFRComponent component) {
		if (component != null) {
			// Update the stored reference to the component.
			this.component = component;

			// Enable the button for displaying the component's properties.
			componentProperties.setEnabled(true);

			// Pass on the component to the figures.
			radialFigure.setComponent(component);
			axialFigure.setComponent(component);
		}
		return;
	}

	/**
	 * Sets the data provider used to display data in this view.
	 * 
	 * @param dataProvider
	 *            The data provider for the currently selected assembly
	 *            component.
	 */
	private void setDataProvider(IDataProvider dataProvider) {
		if (dataProvider != this.dataProvider) {
			// Reset the axial level for new data providers.
			setAxialLevel(0, AxialLevelWidget.NONE);

			// Update the stored reference to the data provider.
			this.dataProvider = dataProvider;

			// If there is no data provider, use an empty one.
			if (dataProvider == null) {
				dataProvider = new SFRComponent();
			}
			// Pass on the data provider to the figures.
			radialFigure.setDataProvider(dataProvider);
			axialFigure.setDataProvider(dataProvider);

			/* ---- Update the feature ActionTree. ---- */
			// Clear the old features.
			featureTree.removeAll();
			featureMap.clear();

			String firstFeature = null;

			// Get the features available here. For each feature, get the max
			// number of axial levels supported.
			for (final String feature : dataProvider.getFeatureList()) {
				// Get the number of axial levels for this feature.
				int count = dataProvider.getDataAtCurrentTime(feature).size();

				// Put it in the map and create an ActionTree for it.
				featureMap.put(feature, count);
				ActionTree action = new ActionTree(new Action(feature) {
					@Override
					public void run() {
						setFeature(feature);
					}
				});
				featureTree.add(action);

				// If there are no axial levels, disable the ActionTree.
				if (count == 0) {
					action.setEnabled(false);
				} else if (firstFeature == null) {
					firstFeature = feature;
				}
			}

			// Select the first feature if possible.
			setFeature(firstFeature);

			// Refresh the Menus.
			updateActionManagers();
			/* ---------------------------------------- */
		}
		return;
	}

	/**
	 * Sets the display type for the radial and axial figures.
	 * 
	 * @param displayType
	 *            The display type for the radial and axial figures.
	 */
	private void setDisplayType(DisplayType displayType) {
		radialFigure.setDisplayType(displayType);
		axialFigure.setDisplayType(displayType);
	}

	/**
	 * Sets the current feature of data to display. The feature determines some
	 * actions available in the ToolBar and the constraints for the scale and
	 * spinner.
	 * 
	 * @param feature
	 *            The new feature for which to display data from the
	 *            IDataProvider.
	 */
	private void setFeature(String feature) {
		if (feature != null && feature != this.feature) {
			// Get the number of axial levels.
			Integer count = featureMap.get(feature);
			if (count != null) {
				// Update the feature value.
				this.feature = feature;

				// Update the maximum axial level.
				maxAxialLevel = count - 1;

				// If necessary, reduce the current axial level.
				if (maxAxialLevel < axialLevel) {
					setAxialLevel(maxAxialLevel, AxialLevelWidget.NONE);
				}
				// Enable and configure the scale and spinner depending on the
				// maximum axial level.
				if (maxAxialLevel > 0) {
					axialScale.setEnabled(true);
					axialSpinner.setEnabled(true);
				} else {
					axialScale.setEnabled(false);
					axialSpinner.setEnabled(false);
				}
				axialScale.setMaximum(maxAxialLevel + 1);
				axialSpinner.setMaximum(maxAxialLevel + 1);

				// Pass on the new feature to the figures.
				radialFigure.setFeature(feature);
				axialFigure.setFeature(feature);
			}
		}
		return;
	}

	/**
	 * Sets the axial level for the view. This can affect either figure, the
	 * scale, and the spinner.
	 * 
	 * @param axialLevel
	 *            The new axial level.
	 * @param source
	 *            The widget that is being used to set the axial level.
	 */
	private void setAxialLevel(int axialLevel, AxialLevelWidget source) {
		// Don't do anything if it has not actually changed.
		if (axialLevel != this.axialLevel) {
			// Update the stored axial level.
			this.axialLevel = axialLevel;

			// Update the axial scale and spinner. To smooth the process a bit,
			// we know the source widget that is changing the axial level. Do
			// not send an update to it!
			if (source == AxialLevelWidget.SCALE) {
				axialSpinner.setSelection(axialLevel + 1);
			} else if (source == AxialLevelWidget.SPINNER) {
				axialScale.setSelection(maxAxialLevel - axialLevel + 1);
			} else {
				axialScale.setSelection(maxAxialLevel - axialLevel + 1);
				axialSpinner.setSelection(axialLevel + 1);
			}

			// Pass the new axial level to the figures.
			radialFigure.setAxialLevel(axialLevel);
			axialFigure.setAxialLevel(axialLevel);
		}
		return;
	}

	/**
	 * The PinAnalysisView listens for the MouseUp event on the PinFigure's
	 * Rings. This method provides that functionality.
	 * 
	 * @param ring
	 *            The Ring that has been clicked.
	 */
	public void clickRing(Ring ring) {
		// TODO - Currently, a PinFigure can add PinAnalysisViews as
		// listeners. At some point, it may make sense to create an interface
		// for listeners that want to be notified when a particular SFRComponent
		// is clicked or selected in a Figure representing a parent
		// SFRComponent.
		IPropertySource properties = new PropertySourceFactory()
				.getPropertySource(ring);
		selectionProvider.setSelection(new StructuredSelection(properties));
	}

	/* ---- Implements IAnalysisView ---- */
	/**
	 * Fills out the parent Composite with information and widgets related to
	 * this particular IAnalysisView.
	 * 
	 * @param parent
	 *            The Composite containing this IAnalysisView.
	 */
	@Override
	public void createViewContent(Composite container) {
		super.createViewContent(container);

		// A single SashForm composes this AVC. So, use a FillLayout on top.
		container.setLayout(new FillLayout());

		/* ---- Create the SashForm containing the radial/axial views. ---- */
		// Create the SashForm and its components.
		sashForm = new SashForm(container, SWT.HORIZONTAL | SWT.SMOOTH);
		radialCanvas = new Canvas(sashForm, SWT.DOUBLE_BUFFERED);
		axialComposite = new Composite(sashForm, SWT.NONE);

		// The left side of the SashForm is the radial view of the Rod. The
		// right side is the axial view + widgets for selecting the axial view.
		// Give the SashForm a 3:1 size ratio (radial:axial).
		sashForm.setWeights(new int[] { 3, 1 });
		/* ---------------------------------------------------------------- */

		/* ---- Create the radial view. ---- */
		// Initialize the radial figure.
		radialFigure = new LonePinFigure();

		// Set the radial figure as the content for the radial canvas.
		LightweightSystem lws = new LightweightSystem(radialCanvas);
		lws.setContents(radialFigure);

		// Add the RodAnalysisView as a listener for Ring click events.
		radialFigure.addRingListener(this);
		/* --------------------------------- */

		/* ---- Create the axial view. ---- */
		// Create the axial view widgets.
		axialScale = new Scale(axialComposite, SWT.VERTICAL);
		axialCanvas = new Canvas(axialComposite, SWT.DOUBLE_BUFFERED);
		axialSpinner = new Spinner(axialComposite, SWT.NONE);

		// Disable the scale and spinner.
		axialScale.setEnabled(false);
		axialSpinner.setEnabled(false);

		// Set the increment for the scale.
		axialScale.setIncrement(1);

		// Set the initial values for the scale and spinner to "zero".
		axialScale.setMinimum(1);
		axialScale.setSelection(axialScale.getMaximum());
		axialSpinner.setMinimum(1);
		axialSpinner.setMinimum(1);
		axialSpinner.setSelection(1);

		// Create the axial figure in the axial canvas.
		// FIXME AxialPinFigure.
		axialFigure = new AxialPinFigure();
		lws = new LightweightSystem(axialCanvas);
		lws.setContents(axialFigure);

		// Add the RodAnalysisView as a listener for Ring click events.
		axialFigure.addRingListener(this);

		// Add SelectionListeners to the Scale and Spinner.
		axialScale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Scale scale = (Scale) e.widget;
				setAxialLevel(scale.getMaximum() - scale.getSelection(),
						AxialLevelWidget.SCALE);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		axialSpinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Spinner spinner = (Spinner) e.widget;
				setAxialLevel(spinner.getSelection() - 1,
						AxialLevelWidget.SPINNER);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		// Give the axial view widgets a 2-column GridLayout.
		axialComposite.setLayout(new GridLayout(2, false));
		// The Scale (1st cell) should grab excess vertical space.
		axialScale.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		// The Canvas (2nd cell) should get all excess space.
		axialCanvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		// The Spinner (3rd cell) will go directly underneath the scale. No
		// code needed.
		/* -------------------------------- */

		/* ---- Configure any Menus for the container. ---- */
		// Set the rightClickMenu for any Composites that will need to use it.
		radialCanvas.setMenu(actionMenuManager.createContextMenu(container));
		axialCanvas.setMenu(actionMenuManager.createContextMenu(container));
		axialScale.setMenu(actionMenuManager.createContextMenu(container));
		/* ------------------------------------------------ */

		return;
	}

	/**
	 * Gets the name for this type of analysis view. This can be used for the
	 * display text of SWT widgets that reference this view.
	 * 
	 * @return The IAnalysisView's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets a brief description of this type of analysis view. This can be used
	 * for ToolTips for SWT widgets referencing this view.
	 * 
	 * @return The IAnalysisView's description.
	 */
	public String getDescription() {
		return description;
	}

	/* ---------------------------------- */

	/* ---- Implements IStateListener ---- */
	/**
	 * Registers any keys of interest with the current broker.
	 */
	@Override
	public void registerKeys() {
		String key = dataSource + "-" + "pin";
		SFRComponentInfo info = (SFRComponentInfo) broker.register(key, this);
		if (info != null) {
			setComponent(info.sfrComponent);
			setDataProvider(info.dataProvider);
		}
	}

	/**
	 * Unregisters any keys from the current broker.
	 */
	@Override
	public void unregisterKeys() {
		broker.unregister(dataSource + "-" + "pin", this);
	}

	/**
	 * This is called by the broker when a key of interest has changed.
	 */
	@Override
	public void update(String key, Object value) {
		SFRComponentInfo info = (SFRComponentInfo) value;
		if (info != null) {
			setComponent(info.sfrComponent);
			setDataProvider(info.dataProvider);
		}
	}

	/* ----------------------------------- */

	/**
	 * This simple extension overrides the default behavior of {@link PinFigure}
	 * to replace the default figure, a hexagon, with a circle.
	 * 
	 * @author Jordan
	 * 
	 */
	private class LonePinFigure extends PinFigure {

		public LonePinFigure() {
			// Call the super constructor. This creates the standard figures.
			super();

			// Replace the default figure (a RectangleFigure) with a circle.
			remove(defaultFigure);
			defaultFigure = new Circle(1.0, 1.0);
			defaultFigure.setVisible(false);
			defaultFigure.setBackgroundColor(ColorConstants.white);
			add(defaultFigure);

			// Put the data label back on top.
			remove(data);
			add(data);

			return;
		}
	}
}
