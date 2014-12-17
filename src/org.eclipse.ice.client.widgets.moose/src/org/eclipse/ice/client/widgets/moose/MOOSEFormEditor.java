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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEFormInput;
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
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
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
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.jme3.math.Vector3f;

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

		// Get the Form that provides the common header and decorate it.
		org.eclipse.ui.forms.widgets.Form form = headerForm.getForm().getForm();
		FormToolkit formToolkit = headerForm.getToolkit();
		formToolkit.decorateFormHeading(form);

		// Create a composite for the overall head layout.
		Composite headClient = new Composite(form.getHead(), SWT.NONE);

		// Set the layout to a GridLayout. It will contain separate columns for
		// the description and, if applicable, process widgets (a label, a
		// dropdown, and go/cancel buttons).
		GridLayout gridLayout = new GridLayout(1, false);
		headClient.setLayout(gridLayout);

		// Create a label to take up the first space and provide the
		// description of the Form.
		Label descLabel = new Label(headClient, SWT.WRAP);
		descLabel.setText(iceDataForm.getDescription());

		// Create the GridData for the label. It must take up all of the
		// available horizontal space, but capable of shrinking down to the
		// minimum width.
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		// For the minimum width, pick a length based on the average character
		// width with the label's font. Use, say, 35 characters.
		GC gc = new GC(descLabel);
		int widthOf50Chars = gc.getFontMetrics().getAverageCharWidth() * 35;
		gc.dispose();
		// We set the min width so the label won't shrink below that width. We
		// set the width hint to the same value so the widget won't compute its
		// size base on SWT.DEFAULT (if this is the case, it won't wrap).
		gridData.minimumWidth = widthOf50Chars;
		gridData.widthHint = widthOf50Chars;
		descLabel.setLayoutData(gridData);

		// Create the process label, button and dropdown if the action list is
		// available
		if (iceDataForm.getActionList() != null) {

			// Create the output file label
			Label outputLabel = new Label(headClient, SWT.NONE);
			outputLabel.setText("Output File Name:");

			// Create the output file text box
			final Text outputFileText = new Text(headClient, SWT.LEFT
					| SWT.BORDER);
			ICEFormInput formInput = (ICEFormInput) getEditorInput();
			Form dataForm = formInput.getForm();
			DataComponent dataComp = (DataComponent) dataForm
					.getComponent(MOOSEModel.fileDataComponentId);
			final Entry entry = dataComp.retrieveEntry("Output File Name");
			outputFileText.setToolTipText(entry.getDescription());
			outputFileText.setText(entry.getValue());
			// Adapt the text's visual appearance to Form defaults.
			formToolkit.adapt(outputFileText, true, false);
			// Add the Focus Listeners
			outputFileText.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					// Set the value of the Entry
					entry.setValue(outputFileText.getText());
					// Notify the listeners that a change may have occurred.
					notifyUpdateListeners();
				};
			});

			// Create a label for the process buttons
			Label processLabel = new Label(headClient, SWT.NONE);
			processLabel.setText("Process:");

			// Create the dropdown menu
			processDropDown = new Combo(headClient, SWT.DROP_DOWN | SWT.SINGLE
					| SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
			for (String i : iceDataForm.getActionList()) {
				processDropDown.add(i);
			}
			// Set the default process
			processName = iceDataForm.getActionList().get(0);
			processDropDown.select(0);
			// Add the dropdown listener
			processDropDown.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Set the action value to use when processing
					processName = processDropDown.getItem(processDropDown
							.getSelectionIndex());
				}
			});

			// Create the button to process the Form
			goButton = new Button(headClient, SWT.PUSH);
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
			cancelButton = new Button(headClient, SWT.PUSH);
			cancelButton.setText("Cancel");

			// Set the button's listener and process command
			cancelButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Process the Form
					notifyCancelListeners(processName);
				}
			});

			// Since we have more widgets, add more columns to the GridLayout.
			// All of these new widgets should grab what horizontal space they
			// need but be vertically centered.
			gridLayout.numColumns += 6;
			gridData = new GridData(SWT.FILL, SWT.CENTER, false, true);
			outputLabel.setLayoutData(gridData);
			gridData = new GridData(SWT.FILL, SWT.CENTER, false, true);
			outputFileText.setLayoutData(gridData);
			gridData = new GridData(SWT.FILL, SWT.CENTER, false, true);
			processLabel.setLayoutData(gridData);
			gridData = new GridData(SWT.FILL, SWT.CENTER, false, true);
			processDropDown.setLayoutData(gridData);
			gridData = new GridData(SWT.FILL, SWT.CENTER, false, true);
			goButton.setLayoutData(gridData);
			gridData = new GridData(SWT.FILL, SWT.CENTER, false, true);
			cancelButton.setLayoutData(gridData);
		}
		// Set the processComposite as the Form's head client
		form.setHeadClient(headClient);

		// Set Form name
		form.setText(iceDataForm.getName() + " " + iceDataForm.getId());

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
						ToolBarManager toolBarManager = new ToolBarManager(
								SWT.RIGHT);
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

						// Add a new menu with the following options:
						// Reset the camera - resets the camera's orientation
						// YZ - sets the camera to view the YZ plane
						// XY - sets the camera to view the XY plane
						// ZX - sets the camera to view the ZX plane
						ActionTree cameraTree = new ActionTree(
								"Camera Orientation");
						cameraTree.add(new ActionTree(new Action(
								"Reset to current default") {
							@Override
							public void run() {
								plantView.resetCamera();
							}
						}));
						cameraTree.add(new ActionTree(new Action(
								"YZ (Y right, Z up - initial default)") {
							@Override
							public void run() {
								Vector3f position = new Vector3f(10f, 0f, 0f);
								Vector3f dir = new Vector3f(-1f, 0f, 0f);
								Vector3f up = Vector3f.UNIT_Z;
								plantView.setDefaultCameraPosition(position);
								plantView.setDefaultCameraOrientation(dir, up);
								plantView.resetCamera();
							}
						}));
						cameraTree.add(new ActionTree(new Action(
								"XY (X right, Y up)") {
							@Override
							public void run() {
								Vector3f position = new Vector3f(0f, 0f, 10f);
								Vector3f dir = new Vector3f(0f, 0f, -1f);
								Vector3f up = Vector3f.UNIT_Y;
								plantView.setDefaultCameraPosition(position);
								plantView.setDefaultCameraOrientation(dir, up);
								plantView.resetCamera();
							}
						}));
						cameraTree.add(new ActionTree(new Action(
								"ZX (Z right, X up)") {
							@Override
							public void run() {
								Vector3f position = new Vector3f(0f, 10f, 0f);
								Vector3f dir = new Vector3f(0f, -1f, 0f);
								Vector3f up = Vector3f.UNIT_X;
								plantView.setDefaultCameraPosition(position);
								plantView.setDefaultCameraOrientation(dir, up);
								plantView.resetCamera();
							}
						}));
						toolBarManager.add(cameraTree.getContributionItem());

						Action action = new Action("Save Image") {
							@Override
							public void run() {
								plantView.exportImage();
							}
						};
						// Set the action's image (a camera).
						Bundle bundle = FrameworkUtil.getBundle(getClass());
						Path imagePath = new Path("icons"
								+ System.getProperty("file.separator")
								+ "camera.png");
						URL imageURL = FileLocator
								.find(bundle, imagePath, null);
						ImageDescriptor imageDescriptor = ImageDescriptor
								.createFromURL(imageURL);
						action.setImageDescriptor(imageDescriptor);
						ActionTree saveImageTree = new ActionTree(action);
						toolBarManager.add(saveImageTree.getContributionItem());

						// Create the ToolBar and set its layout.
						ToolBar toolBar = toolBarManager
								.createControl(analysisComposite);
						toolBar.setBackground(background);
						toolBar.setLayoutData(new GridData(SWT.FILL,
								SWT.BEGINNING, true, false));

						// Create the plant view.
						plantView = new ViewFactory().createPlantView(plant);

						// Render the plant view in the analysis Composite.
						Composite plantComposite = plantView
								.createComposite(analysisComposite);
						plantComposite.setBackground(background);
						plantComposite.setLayoutData(new GridData(SWT.FILL,
								SWT.FILL, true, true));
						
						fillPlantTools(toolBarManager);
						toolBarManager.update(true);
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

	private void fillPlantTools(ToolBarManager toolBar) {
		Action action;

		// Set the action's image (a camera).
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath;
		URL imageURL;
		ImageDescriptor image;

		// TODO Use an ImageRegistry instead.
		final float moveRate = 1f;
		final float rotateRate = (float) (Math.PI * 0.1);

		// ---- Movement Arrow Buttons ---- //
		// Strafe left
		action = new Action("Move left (A)") {
			@Override
			public void run() {
				plantView.getFlightCamera().strafeCamera(-1f);
			}
		};
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "arrow-left-perspective-16.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		image = ImageDescriptor.createFromURL(imageURL);
		action.setImageDescriptor(image);
		toolBar.add(action);

		// Move forward
		action = new Action("Move forward (W)") {
			@Override
			public void run() {
				plantView.getFlightCamera().thrustCamera(moveRate);				
			}
		};
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "arrow-up-perspective-16.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		image = ImageDescriptor.createFromURL(imageURL);
		action.setImageDescriptor(image);
		toolBar.add(action);

		// Move backward
		action = new Action("Move backward (S)") {
			@Override
			public void run() {
				plantView.getFlightCamera().thrustCamera(-moveRate);
			}
		};
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "arrow-down-perspective-16.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		image = ImageDescriptor.createFromURL(imageURL);
		action.setImageDescriptor(image);
		toolBar.add(action);

		// Strafe right
		action = new Action("Move right (D)") {
			@Override
			public void run() {
				plantView.getFlightCamera().strafeCamera(moveRate);
			}
		};
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "arrow-right-perspective-16.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		image = ImageDescriptor.createFromURL(imageURL);
		action.setImageDescriptor(image);
		toolBar.add(action);

		// Move up
		action = new Action("Move up (SPACE)") {
			@Override
			public void run() {
				plantView.getFlightCamera().raiseCamera(moveRate);
			}
		};
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "arrow-up-16.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		image = ImageDescriptor.createFromURL(imageURL);
		action.setImageDescriptor(image);
		toolBar.add(action);

		// Move down
		action = new Action("Move down (C)") {
			@Override
			public void run() {
				plantView.getFlightCamera().raiseCamera(-moveRate);
			}
		};
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "arrow-down-16.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		image = ImageDescriptor.createFromURL(imageURL);
		action.setImageDescriptor(image);
		toolBar.add(action);
		// -------------------------------- //

		toolBar.add(new Separator());
		
		// ---- Rotation Arrow Buttons ---- //
		// Roll left
		action = new Action("Roll Left (Q)") {
			@Override
			public void run() {
				plantView.getFlightCamera().rollCamera(-rotateRate);
			}
		};
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "arrow-roll-left-16.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		image = ImageDescriptor.createFromURL(imageURL);
		action.setImageDescriptor(image);
		toolBar.add(action);
		
		// Roll right
		action = new Action("Roll Right (E)") {
			@Override
			public void run() {
				plantView.getFlightCamera().rollCamera(rotateRate);
			}
		};
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "arrow-roll-right-16.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		image = ImageDescriptor.createFromURL(imageURL);
		action.setImageDescriptor(image);
		toolBar.add(action);
		
		// Pitch up
		action = new Action("Pitch Up (up arrow)") {
			@Override
			public void run() {
				plantView.getFlightCamera().pitchCamera(rotateRate);
			}
		};
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "arrow-pitch-up-16.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		image = ImageDescriptor.createFromURL(imageURL);
		action.setImageDescriptor(image);
		toolBar.add(action);
		// Pitch down
		action = new Action("Pitch down (down arrow)") {
			@Override
			public void run() {
				plantView.getFlightCamera().pitchCamera(-rotateRate);
			}
		};
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "arrow-pitch-down-16.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		image = ImageDescriptor.createFromURL(imageURL);
		action.setImageDescriptor(image);
		toolBar.add(action);
		
		// Yaw left
		action = new Action("Yaw Left (left arrow)") {
			@Override
			public void run() {
				plantView.getFlightCamera().yawCamera(-rotateRate);
			}
		};
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "arrow-yaw-left-16.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		image = ImageDescriptor.createFromURL(imageURL);
		action.setImageDescriptor(image);
		toolBar.add(action);
		// Yaw right
		action = new Action("Yaw Right (right arrow)") {
			@Override
			public void run() {
				plantView.getFlightCamera().yawCamera(rotateRate);
			}
		};
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "arrow-yaw-right-16.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		image = ImageDescriptor.createFromURL(imageURL);
		action.setImageDescriptor(image);
		toolBar.add(action);
		// -------------------------------- //

		// ---- Zoom Buttons ---- //
		// ---------------------- //

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

	/**
	 * Tells the FormEditor to use the MOOSE input tree instead of the first
	 * available tree added to the Form.
	 */
	@Override
	protected int getTreeCompositeViewerInputID() {
		return MOOSEModel.mooseTreeCompositeId;
	}

}
