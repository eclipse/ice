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
package org.eclipse.ice.client.widgets.moose;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEFormPage;
import org.eclipse.ice.client.widgets.ICESectionPage;
import org.eclipse.ice.client.widgets.jme.ViewFactory;
import org.eclipse.ice.client.widgets.moose.components.PlantBlockManager;
import org.eclipse.ice.client.widgets.reactoreditor.plant.PlantAppState;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;
import org.eclipse.ice.item.nuclear.MOOSEModel;
import org.eclipse.ice.reactor.plant.CoreChannel;
import org.eclipse.ice.reactor.plant.IPlantComponentVisitor;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.ice.reactor.plant.Reactor;
import org.eclipse.ice.reactor.plant.SelectivePlantComponentVisitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This class extends the default {@link ICEFormEditor} to enable it to draw a
 * {@link PlantApplication}, a 3D view built on jME3 for viewing plant models.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class MOOSEFormEditor extends ICEFormEditor {

	/**
	 * ID for Eclipse, used for the bundle's editor extension point.
	 */
	public static final String ID = "org.eclipse.ice.client.widgets.moose.MOOSEFormEditor";

	/**
	 * The PlantAppState rendered on the Plant View page.
	 */
	private PlantAppState plantView;

	/**
	 * The factory responsible for synchronizing the current "Components"
	 * TreeComposite with the {@link #plantApplication}'s current
	 * {@link PlantComposite}.
	 */
	private final PlantBlockManager factory;

	/**
	 * The TreeComposite that contains the plant components. This is the
	 * "Components" TreeComposite that should be a child of the root
	 * TreeComposite.
	 */
	private TreeComposite components;

	/**
	 * The underlying plant model that is rendered in the {@link #plantView}.
	 * When {@link #requiresPlant} is true, any changes to the MOOSE Model's
	 * tree should be passed to this via the {@link #factory}.
	 */
	private final PlantComposite plant;

	/**
	 * The <code>Entry</code> that contains the list of allowed MOOSE apps and
	 * the currently selected MOOSE app.
	 */
	private Entry mooseAppEntry;

	/**
	 * This listener is used to set the {@link #requiresPlant} flag when the
	 * {@link #mooseAppEntry} is updated.
	 */
	private final IUpdateableListener mooseAppListener;

	/**
	 * Whether or not the current MOOSE app setting in the Model Builder
	 * requires the plant view. If it does, then each call to
	 * {@link #update(IUpdateable)} will look for the "Components" sub-tree.
	 */
	private boolean requiresPlant = false;

	/**
	 * Whether or not to render the plant view with wireframes.
	 */
	private boolean wireframe = false;

	/**
	 * The default constructor.
	 */
	public MOOSEFormEditor() {

		plant = new PlantComposite() {

			private final ArrayList<CoreChannel> coreChannels = new ArrayList<CoreChannel>();
			private final List<Reactor> reactors = new ArrayList<Reactor>();

			public void addPlantComponent(PlantComponent component) {
				if (component != null
						&& getPlantComponent(component.getId()) == null) {

					// Add the component in the usual manner.
					super.addPlantComponent(component);

					// Create a visitor that, when adding a component, will do
					// the following:
					// For new Reactors, add all existing CoreChannels to it.
					// For new CoreChannels, add it to all existing Reactors.
					IPlantComponentVisitor visitor = new SelectivePlantComponentVisitor() {
						@Override
						public void visit(Reactor plantComp) {
							reactors.add(plantComp);
							plantComp.setCoreChannels(coreChannels);
						}

						@Override
						public void visit(CoreChannel plantComp) {

							boolean found = false;
							int size = coreChannels.size();
							for (int i = 0; !found && i < size; i++) {
								found = (plantComp == coreChannels.get(i));
							}
							if (!found) {
								coreChannels.add(plantComp);
								for (Reactor reactor : reactors) {
									reactor.setCoreChannels(coreChannels);
								}
							}
							return;
						}
					};
					component.accept(visitor);
				}

				return;
			}

			public void removeComponent(int childId) {
				PlantComponent component = getPlantComponent(childId);
				if (component != null) {
					super.removeComponent(childId);

					// Create a visitor that, when adding a component, will do
					// the following:
					// For removed Reactors, update the list of Reactors.
					// For removed CoreChannels, update all existing Reactors.
					IPlantComponentVisitor visitor = new SelectivePlantComponentVisitor() {
						@Override
						public void visit(Reactor plantComp) {

							boolean found = false;
							int i, size = reactors.size();
							for (i = 0; !found && i < size; i++) {
								found = (plantComp == reactors.get(i));
							}
							if (found) {
								reactors.remove(i - 1);
							}
						}

						@Override
						public void visit(CoreChannel plantComp) {

							boolean found = false;
							int i, size = coreChannels.size();
							for (i = 0; !found && i < size; i++) {
								found = (plantComp == coreChannels.get(i));
							}
							if (found) {
								coreChannels.remove(i - 1);
								for (Reactor reactor : reactors) {
									reactor.setCoreChannels(coreChannels);
								}
							}
							return;
						}
					};
					component.accept(visitor);
				}

				return;
			}
		};

		// Initialize the PlantComponentFactory and PlantApplication.
		factory = new PlantBlockManager();
		factory.setPlant(plant);

		// Create a listener that updates the requiresPlant flag when the type
		// of MOOSE app actually supports a plant view.
		mooseAppListener = new IUpdateableListener() {
			@Override
			public void update(IUpdateable component) {
				if (component == mooseAppEntry) {
					requiresPlant = "relap".equals(mooseAppEntry.getValue());
				}
			}
		};

		return;
	}

	/**
	 * We need to know when the MOOSE Model Builder's tree supports the plant
	 * view. Override the default init() behavior to additionally update the
	 * {@link #mooseAppEntry} when the <code>Form</code>'s model is set.
	 */
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		updateMOOSEAppEntry();
	}

	/**
	 * We need to know when the MOOSE Model Builder's tree supports the plant
	 * view. Override the default setInput() behavior to additionally update the
	 * {@link #mooseAppEntry} when the <code>Form</code>'s model is changed.
	 */
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		updateMOOSEAppEntry();
	}

	/**
	 * We need to know when the MOOSE Model Builder's tree supports the plant
	 * view. Override the default setInput() behavior to additionally update the
	 * {@link #mooseAppEntry} when the <code>Form</code>'s model is changed.
	 */
	protected void setInputWithNotify(IEditorInput input) {
		super.setInputWithNotify(input);
		updateMOOSEAppEntry();
	}

	/**
	 * Updates the {@link #mooseAppEntry} to point to the correct
	 * <code>Entry</code> in the MOOSE Model Builder's <code>Form</code>.
	 */
	private void updateMOOSEAppEntry() {

		// Unregister the listener from the previous MOOSE app Entry.
		if (mooseAppEntry != null) {
			mooseAppEntry.unregister(mooseAppListener);
		}

		// Get the Entry that contains the available MOOSE apps.
		DataComponent dataComp = (DataComponent) iceDataForm
				.getComponent(MOOSEModel.fileDataComponentId);
		mooseAppEntry = dataComp.retrieveEntry("MOOSE-Based Application");
		// Listen to the MOOSE app Entry.
		mooseAppEntry.register(mooseAppListener);

		return;
	}

	/**
	 * Overrides the default <code>ICEFormEditor</code> header and adds the
	 * widgets for specifiying the output for the MOOSE model (i.e., the input
	 * file for the MOOSE-based simulation).
	 */
	@Override
	protected void createHeaderContents(IManagedForm headerForm) {

		// Local Declarations
		org.eclipse.ui.forms.widgets.Form pageForm = null;

		// Get the Form that provides the common header
		pageForm = headerForm.getForm().getForm();

		// Set the decoration
		headerForm.getToolkit().decorateFormHeading(pageForm);

		// Create a composite for the overall head layout
		Composite head = pageForm.getHead();
		Composite gridComposite = new Composite(head, SWT.NONE);
		Composite processComposite = null;

		// Set the composite's layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridComposite.setLayout(gridLayout);

		// Create a label to take up the first space and provide the
		// description of the Form.
		Label descLabel = new Label(gridComposite, SWT.WRAP);
		descLabel.setText(iceDataForm.getDescription());

		// Create the GridData for the label. It must take up all of the
		// available horizontal space, but resize with the shell to within
		// the widthHint.
		GridData labelGridData = new GridData(GridData.GRAB_HORIZONTAL);
		labelGridData.horizontalSpan = 1;
		labelGridData.widthHint = 10;
		labelGridData.horizontalAlignment = SWT.FILL;
		descLabel.setLayoutData(labelGridData);
		// The next trick actually gets it to work. It forces the Composite
		// to calculate the correct size and then updates the size of the label.
		gridComposite.pack();
		labelGridData.widthHint = descLabel.getBounds().width;
		gridComposite.pack();

		// Create the process label, button and dropdown if the action list is
		// available
		if (iceDataForm.getActionList() != null) {
			// Create the composite for containing the process widgets
			processComposite = new Composite(gridComposite, SWT.NONE);
			processComposite.setLayout(new RowLayout());

			// Create the output file label
			Label outputLabel = new Label(processComposite, SWT.NONE);
			outputLabel.setText("Output File Name:");

			// Create the output file text box
			final Text outputFileText = new Text(processComposite, SWT.LEFT
					| SWT.BORDER);
			IEditorInput editorInput = this.getEditorInput();
			Form form = ((org.eclipse.ice.client.widgets.ICEFormInput) editorInput)
					.getForm();
			DataComponent dataComp = (DataComponent) form
					.getComponent(MOOSEModel.fileDataComponentId);
			final org.eclipse.ice.datastructures.form.Entry entry = dataComp
					.retrieveEntry("Output File Name");
			outputFileText.setToolTipText(entry.getDescription());
			outputFileText.setText(entry.getValue());
			outputFileText.setBackground(Display.getCurrent().getSystemColor(
					SWT.COLOR_WHITE));
			// Add the Focus Listeners
			outputFileText.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					// Do nothing.
				};

				@Override
				public void focusLost(FocusEvent e) {
					// Set the value of the Entry
					entry.setValue(outputFileText.getText());
					// Notify the listeners that a change may have occurred.
					notifyUpdateListeners();
				};
			});

			// Create a label for the process buttons
			Label processLabel = new Label(processComposite, SWT.NONE);
			processLabel.setText("Process:");

			// Create the dropdown menu
			processDropDown = new Combo(processComposite, SWT.DROP_DOWN
					| SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
			for (String i : iceDataForm.getActionList()) {
				processDropDown.add(i);
			}
			// Set the default process
			processName = iceDataForm.getActionList().get(0);
			processDropDown.select(0);
			// Add the dropdown listener
			processDropDown.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					// Set the action value to use when processing
					processName = processDropDown.getItem(processDropDown
							.getSelectionIndex());
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// Set the action value to use when processing
					processName = processDropDown.getItem(processDropDown
							.getSelectionIndex());
				}
			});

			// Create the button to process the Form
			goButton = new Button(processComposite, SWT.PUSH);
			goButton.setText("Go!");

			// Set the button's listener and process command
			goButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					// Process the Form
					notifyProcessListeners(processName);
				}

			});

			// Create the button to cancel the process
			cancelButton = new Button(processComposite, SWT.PUSH);
			cancelButton.setText("Cancel");

			// Set the button's listener and process command
			cancelButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					// Process the Form
					notifyCancelListeners(processName);
				}

			});

		}
		// Set the processComposite as the Form's head client
		pageForm.setHeadClient(gridComposite);

		// Set Form name
		pageForm.setText(iceDataForm.getName() + " " + iceDataForm.getId());

		return;
	}

	/**
	 * Override the method in {@link ICEFormEditor} to prevent the DataComponent
	 * page from being rendered since this class has moved the output file text
	 * to the header and the app selection to the
	 * {@link MOOSETreeCompositeViewer}.
	 */
	@Override
	protected ArrayList<ICEFormPage> createDataTableAndMatrixComponentPages() {

		// Just create and return a blank page
		ArrayList<ICEFormPage> sectionPages = new ArrayList<ICEFormPage>();
		sectionPages.add(new ICESectionPage(this, "MOOSE Page", "MOOSE Page"));

		return sectionPages;
	}

	/**
	 * Provides a Plant View page with a single {@link PlantApplication} for use
	 * with RELAP-7.
	 */
	public void addPlantPage() {

		String id = "Plant";

		// Do not add more than one Plant page.
		if (findPage(id) == null) {

			// Add a page with a plant view.
			try {
				addPage(new ICEFormPage(this, id, "Plant View") {
					@Override
					protected void createFormContent(IManagedForm managedForm) {
						// begin-user-code

						// Local Declarations
						final ScrolledForm form = managedForm.getForm();
						form.getBody().setLayout(new FillLayout());
						form.setMinWidth(10);

						// ---- Create the Section with the PlantView. ---- //
						// Get the toolkit used to create Composites, Sections,
						// etc.
						FormToolkit formToolkit = managedForm.getToolkit();

						// Create a single Section with a single SectionPart.
						// When the form updates, it calls the SectionPart's
						// refresh() method. This method should call this class'
						// refreshContent() method.
						int style = Section.NO_TITLE | Section.EXPANDED;
						Section section = formToolkit.createSection(
								form.getBody(), style);
						SectionPart sectionPart = new SectionPart(section);
						// Add the section part to the form so that updates will
						// be sent to the part (and thus will call
						// refreshContent()).
						managedForm.addPart(sectionPart);

						// Get the background color to use later.
						Color background = section.getBackground();

						// Create an analysis composite to contain a ToolBar and
						// an analysis-based view.
						Composite analysisComposite = new Composite(section,
								SWT.NONE);
						section.setClient(analysisComposite);
						analysisComposite.setBackground(background);
						analysisComposite.setLayout(new GridLayout(1, false));

						// Create a ToolBarManager so we can add JFace Actions
						// to it.
						ToolBarManager toolBarManager = new ToolBarManager();
						// Add an action that toggles the wireframe boolean.
						// Also clear the wireframe setting.
						wireframe = false;
						toolBarManager.add(new Action("Wireframe") {
							@Override
							public void run() {
								wireframe = !wireframe;
								plantView.setWireframe(wireframe);
							}
						});
						// Create the ToolBar and set its layout.
						ToolBar toolBar = toolBarManager
								.createControl(analysisComposite);
						toolBar.setBackground(background);
						toolBar.setLayoutData(new GridData(SWT.FILL,
								SWT.CENTER, true, false));

						// Create the plant view.
						plantView = new ViewFactory().createPlantView(plant);

						// Render the plant view in the analysis Composite.
						Composite plantComposite = plantView
								.createComposite(analysisComposite);
						plantComposite.setBackground(background);
						plantComposite.setLayoutData(new GridData(SWT.FILL,
								SWT.FILL, true, true));
						// ------------------------------------------------ //

						return;
						// end-user-code
					}
				});
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}

		return;
	}

	/**
	 * Removes the Plant View page if possible.
	 */
	public void removePlantPage() {

		String id = "Plant";

		// Do not add more than one Plant page.
		if (findPage(id) != null) {
			removePage(1);
		}
		return;
	}

	/**
	 * In addition to the parent class' behavior, this method sets the
	 * "Components" TreeComposite for the plant factory.
	 */
	@Override
	public void update(IUpdateable component) {
		super.update(component);

		// If the root TreeComposite has been updated, we should look for the
		// "Components" TreeComposite and send it to the PlantComponentFactory.
		if (component == iceDataForm
				.getComponent(MOOSEModel.mooseTreeCompositeId) && requiresPlant) {
			updateComponents();
		}

		return;
	}

	/**
	 * Queries the current MOOSE Model for changes to the "Components" sub-tree.
	 */
	private void updateComponents() {

		// Get the root TreeComposite from the form.
		TreeComposite root = (TreeComposite) iceDataForm
				.getComponent(MOOSEModel.mooseTreeCompositeId);

		// Find the "Components" TreeComposite and set componentNode. We
		// need to register with it so we can listen for added
		// PlantComponents.
		for (int i = 0; i < root.getNumberOfChildren(); i++) {
			TreeComposite child = root.getChildAtIndex(i);
			if ("Components".equals(child.getName())) {
				// Break from the loop.
				i = root.getNumberOfChildren();
				// Set the "Components" TreeComposite.
				components = child;
				factory.setTree(components);
			}
		}

		return;
	}

	/**
	 * Tells the FormEditor to use the {@link MOOSETreeCompositeViewer} instead
	 * of the default TreeCompositeViewer.
	 * 
	 * @return The String ID of the MOOSETreeCompositeViewer
	 */
	@Override
	protected String getTreeCompositeViewerID() {
		return MOOSETreeCompositeView.ID;
	}

}
