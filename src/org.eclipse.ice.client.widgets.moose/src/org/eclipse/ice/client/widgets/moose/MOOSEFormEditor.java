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
import org.eclipse.ice.client.widgets.moose.components.PlantBlockManager;
import org.eclipse.ice.client.widgets.reactoreditor.plant.PlantApplication;
import org.eclipse.ice.client.widgets.reactoreditor.plant.PlantCompositeFactory;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.item.nuclear.MOOSEModel;
import org.eclipse.ice.reactor.plant.Boundary;
import org.eclipse.ice.reactor.plant.Branch;
import org.eclipse.ice.reactor.plant.CoreChannel;
import org.eclipse.ice.reactor.plant.DownComer;
import org.eclipse.ice.reactor.plant.FlowJunction;
import org.eclipse.ice.reactor.plant.GeometricalComponent;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.IPlantComponentVisitor;
import org.eclipse.ice.reactor.plant.IdealPump;
import org.eclipse.ice.reactor.plant.Inlet;
import org.eclipse.ice.reactor.plant.Junction;
import org.eclipse.ice.reactor.plant.MassFlowInlet;
import org.eclipse.ice.reactor.plant.OneInOneOutJunction;
import org.eclipse.ice.reactor.plant.Outlet;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PipeToPipeJunction;
import org.eclipse.ice.reactor.plant.PipeWithHeatStructure;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.ice.reactor.plant.PointKinetics;
import org.eclipse.ice.reactor.plant.Pump;
import org.eclipse.ice.reactor.plant.Reactor;
import org.eclipse.ice.reactor.plant.SeparatorDryer;
import org.eclipse.ice.reactor.plant.SolidWall;
import org.eclipse.ice.reactor.plant.SpecifiedDensityAndVelocityInlet;
import org.eclipse.ice.reactor.plant.Subchannel;
import org.eclipse.ice.reactor.plant.SubchannelBranch;
import org.eclipse.ice.reactor.plant.TDM;
import org.eclipse.ice.reactor.plant.TimeDependentJunction;
import org.eclipse.ice.reactor.plant.TimeDependentVolume;
import org.eclipse.ice.reactor.plant.Turbine;
import org.eclipse.ice.reactor.plant.Valve;
import org.eclipse.ice.reactor.plant.VolumeBranch;
import org.eclipse.ice.reactor.plant.WetWell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.ui.IEditorInput;
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
 * @author djg
 * 
 */
public class MOOSEFormEditor extends ICEFormEditor {

	/**
	 * ID for Eclipse, used for the bundle's editor extension point.
	 */
	public static final String ID = "org.eclipse.ice.client.widgets.moose.MOOSEFormEditor";

	/**
	 * The PlantApplication rendered on the Plant View page.
	 */
	private final PlantApplication plantApplication;

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
	 * The default constructor.
	 */
	public MOOSEFormEditor() {

		PlantComposite plantComposite = new PlantComposite() {

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
					IPlantComponentVisitor visitor = new IPlantComponentVisitor() {
						public void visit(PlantComposite plantComp) {
						}

						public void visit(GeometricalComponent plantComp) {
						}

						public void visit(Junction plantComp) {
						}

						public void visit(Reactor plantComp) {
							reactors.add(plantComp);
							plantComp.setCoreChannels(coreChannels);
						}

						public void visit(PointKinetics plantComp) {
						}

						public void visit(HeatExchanger plantComp) {
						}

						public void visit(Pipe plantComp) {
						}

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

						public void visit(Subchannel plantComp) {
						}

						public void visit(PipeWithHeatStructure plantComp) {
						}

						public void visit(Branch plantComp) {
						}

						public void visit(SubchannelBranch plantComp) {
						}

						public void visit(VolumeBranch plantComp) {
						}

						public void visit(FlowJunction plantComp) {
						}

						public void visit(WetWell plantComp) {
						}

						public void visit(Boundary plantComp) {
						}

						public void visit(OneInOneOutJunction plantComp) {
						}

						public void visit(Turbine plantComp) {
						}

						public void visit(IdealPump plantComp) {
						}

						public void visit(Pump plantComp) {
						}

						public void visit(Valve plantComp) {
						}

						public void visit(PipeToPipeJunction plantComp) {
						}

						public void visit(Inlet plantComp) {
						}

						public void visit(MassFlowInlet plantComp) {
						}

						public void visit(
								SpecifiedDensityAndVelocityInlet plantComp) {
						}

						public void visit(Outlet plantComp) {
						}

						public void visit(SolidWall plantComp) {
						}

						public void visit(TDM plantComp) {
						}

						public void visit(TimeDependentJunction plantComp) {
						}

						public void visit(TimeDependentVolume plantComp) {
						}

						public void visit(DownComer plantComp) {
						}

						public void visit(SeparatorDryer plantComp) {
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
					IPlantComponentVisitor visitor = new IPlantComponentVisitor() {
						public void visit(PlantComposite plantComp) {
						}

						public void visit(GeometricalComponent plantComp) {
						}

						public void visit(Junction plantComp) {
						}

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

						public void visit(PointKinetics plantComp) {
						}

						public void visit(HeatExchanger plantComp) {
						}

						public void visit(Pipe plantComp) {
						}

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

						public void visit(Subchannel plantComp) {
						}

						public void visit(PipeWithHeatStructure plantComp) {
						}

						public void visit(Branch plantComp) {
						}

						public void visit(SubchannelBranch plantComp) {
						}

						public void visit(VolumeBranch plantComp) {
						}

						public void visit(FlowJunction plantComp) {
						}

						public void visit(WetWell plantComp) {
						}

						public void visit(Boundary plantComp) {
						}

						public void visit(OneInOneOutJunction plantComp) {
						}

						public void visit(Turbine plantComp) {
						}

						public void visit(IdealPump plantComp) {
						}

						public void visit(Pump plantComp) {
						}

						public void visit(Valve plantComp) {
						}

						public void visit(PipeToPipeJunction plantComp) {
						}

						public void visit(Inlet plantComp) {
						}

						public void visit(MassFlowInlet plantComp) {
						}

						public void visit(
								SpecifiedDensityAndVelocityInlet plantComp) {
						}

						public void visit(Outlet plantComp) {
						}

						public void visit(SolidWall plantComp) {
						}

						public void visit(TDM plantComp) {
						}

						public void visit(TimeDependentJunction plantComp) {
						}

						public void visit(TimeDependentVolume plantComp) {
						}

						public void visit(DownComer plantComp) {
						}

						public void visit(SeparatorDryer plantComp) {
						}
					};
					component.accept(visitor);
				}

				return;
			}
		};

		// Initialize the PlantComponentFactory and PlantApplication.
		factory = new PlantBlockManager();
		factory.setPlant(plantComposite);
		plantApplication = new PlantApplication();
		plantApplication.setPlant(plantComposite);

		return;
	}

	/**
	 * 
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

						// ---- Create the Section with the PlantApplication
						// ---- //
						// Get the toolkit used to create Composites, Sections,
						// etc.
						FormToolkit formToolkit = managedForm.getToolkit();

						// Create a single Section with a single SectionPart.
						// When
						// the form updates, it calls the SectionPart's
						// refresh()
						// method. This method should call this class'
						// refreshContent() method.
						Section section = formToolkit.createSection(
								form.getBody(), Section.NO_TITLE
										| Section.EXPANDED);
						SectionPart sectionPart = new SectionPart(section);
						// Add the section part to the form so that updates will
						// be
						// sent to the part (and thus will call
						// refreshContent()).
						managedForm.addPart(sectionPart);

						// Render the PlantApplication in the section.
						PlantCompositeFactory factory = new PlantCompositeFactory();
						Composite composite = factory.renderPlantComposite(
								section, plantApplication);
						section.setClient(composite);

						// Set the background color for the ATC to be the same
						// as
						// the section.
						composite.setBackground(section.getBackground());
						// ------------------------------------------------------
						// //

						return;
						// end-user-code
					}
				});
			} catch (PartInitException e) {
				e.printStackTrace();
			}

			// Update the plant with the Components in the TreeComposite.
			updateComponents();
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

		System.out.println("MOOSEFormEditor message: "
				+ "Received update from component " + component.getName() + "("
				+ System.identityHashCode(component) + ").");

		// If the root TreeComposite has been updated, we should look for the
		// "Components" TreeComposite and send it to the PlantComponentFactory.
		if (component == iceDataForm
				.getComponent(MOOSEModel.mooseTreeCompositeId)) {
			updateComponents();
		}

		return;
	}

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
		return MOOSETreeCompositeViewer.ID;
	}

}
