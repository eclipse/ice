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

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEFormInput;
import org.eclipse.ice.client.widgets.ICEFormPage;
import org.eclipse.ice.client.widgets.jme.ViewFactory;
import org.eclipse.ice.client.widgets.moose.components.PlantBlockManager;
import org.eclipse.ice.client.widgets.reactoreditor.plant.PlantAppState;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.item.nuclear.MOOSE;
import org.eclipse.ice.item.nuclear.MOOSEModel;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import com.jme3.math.Vector3f;

/**
 * This class extends the default {@link ICEFormEditor} to enable it to draw a
 * {@link PlantAppState}, a 3D view built on jME3 for viewing plant models.
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
	 * The Eclipse IFormPage ID used for the Plant View page.
	 */
	private static final String PLANT_PAGE_ID = "Plant View";

	/**
	 * The {@link Entry} corresponding to the available apps in the MOOSE Model
	 * Builder.
	 */
	private Entry appsEntry;

	private DataComponent postProcessors;

	// ---- Plant Page variables ---- //
	/**
	 * The PlantAppState rendered on the Plant View page.
	 */
	private PlantAppState plantView;

	/**
	 * The factory responsible for synchronizing the current "Components"
	 * TreeComposite with the {@link #plantApplication}'s current
	 * {@link PlantComposite}.
	 */
	private final PlantBlockManager factory = new PlantBlockManager();

	/**
	 * Whether or not to render the plant view with wireframes.
	 */
	private boolean wireframe;

	// ------------------------------ //

	/**
	 * In addition to the default behavior, this method registers with the MOOSE
	 * form's mesh {@link ResourceComponent} to listen for add/insert/remove
	 * events. The intent is to pull the current {@link #meshURI} from the model
	 * and render it in the {@link #plot} on the mesh page.
	 * 
	 * @param input
	 *            The editor's input. This should be of the type
	 *            {@link ICEFormInput}.
	 */
	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);

		if (input instanceof ICEFormInput) {
			// Get the ResourceComponent from the MOOSE Model.
			Form form = ((ICEFormInput) input).getForm();

			// Get the Entry that contains the available apps.
			DataComponent dataComp = (DataComponent) form
					.getComponent(MOOSEModel.fileDataComponentId);
			appsEntry = dataComp.retrieveEntry("MOOSE-Based Application");
			postProcessors = (DataComponent) form.getComponent(MOOSE.ppDataId);
		}

		return;
	}

	/*
	 * This method is overridden to provide an additional SelectionAdapter to
	 * the Go button that automatically displays requested Postprocessors.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.ICEFormEditor#createHeaderContents(org
	 * .eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createHeaderContents(IManagedForm headerForm) {

		// Do the regular create Header Contents
		super.createHeaderContents(headerForm);

		// Add another SelectionAdapter that kicks off a new
		// thread to handle automatically showing specified
		// Postprocessors.
		goButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if ("Launch the Job".equals(processName)) {
					Thread ppThread = new Thread(new Runnable() {
						@Override
						public void run() {

							// Local Declarations
							ArrayList<ICEResource> resourceList = new ArrayList<ICEResource>();
							ArrayList<String> enabledPPs = new ArrayList<String>();
							ResourceComponent resources;

							// / Loop over the Postprocessors and add their
							// names
							// to the String list if they are enabled by the
							// user
							for (Entry postProcessor : postProcessors
									.retrieveAllEntries()) {
								if ("yes".equals(postProcessor.getValue())) {
									enabledPPs.add(postProcessor.getName());
								}
							}

							// If we have enabled Postprocessors, then let's
							// display
							// them now that the user has selected Go
							if (!enabledPPs.isEmpty()) {

								// Kick off a little event loop that loads up a
								// list of the Resources corresponding to the
								// enabled
								// Postprocessors.
								while (resourceList.size() != enabledPPs.size()) {

									// Grab the ResourceComponent
									resources = resourceComponentPage
											.getResourceComponent();

									// Sleep a little bit
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										logger.error(getClass().getName() + " Exception!",e);
									}

									// Loop over the ICEResources and add them
									// to the list if they correspond to enabled
									// Postprocessors and have valid data
									for (ICEResource r : resources
											.getResources()) {
										if (enabledPPs.contains(FilenameUtils
												.removeExtension(r.getName()))
												&& hasValidPostprocessorData(r)) {
											resourceList.add(r);
										}
									}
								}

								// Now that we have the total list of
								// ICEResources
								// let's show the Output FormPage, which should
								// be index 1,
								// and then show the ICEResource on the
								// ICEResourcePage.
								for (final ICEResource r : resourceList) {

									// Kick off on UI thread
									PlatformUI.getWorkbench().getDisplay()
											.asyncExec(new Runnable() {
												@Override
												public void run() {
													try {
														MOOSEFormEditor.this
																.setActivePage(1);

														resourceComponentPage
																.showResource(r);
													} catch (PartInitException e) {
														logger.error(getClass().getName() + " Exception!",e);
													}
												}

											});

								}
							}
						}
					});

					// Start that thread.
					ppThread.start();
				}

			}
		});
		
		return;
	}

	/**
	 * This private method is used to decide whether or not the given
	 * ICEResource contains valid Postprocessor data to plot. Basically, for now
	 * it naively checks that there is more than one line in the file, because
	 * if there was 1 or less, then we would have no data or just the feature
	 * line describing the data.
	 * 
	 * @param r
	 * @return validData Whether or not there is valid data in the resource
	 */
	private boolean hasValidPostprocessorData(ICEResource r) {
		
		// Simply count the number of lines in the resource file
		try {
			LineNumberReader reader = new LineNumberReader(new FileReader(r
					.getPath().getPath()));
			int cnt = 0;
			String lineRead = "";
			while ((lineRead = reader.readLine()) != null) {
			}

			cnt = reader.getLineNumber();
			reader.close();

			if (cnt <= 1) {
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			logger.error(getClass().getName() + " Exception!",e);
			return false;
		}
	}

	/**
	 * Provides a Plant View page with a single {@link PlantApplication} for use
	 * with RELAP-7.
	 */
	private void addPlantPage() {
		// Do not add more than one plant page.
		if (findPage(PLANT_PAGE_ID) == null) {

			// Add a page with a plant view.
			try {
				addPage(new ICEFormPage(this, PLANT_PAGE_ID, "Plant View") {
					@Override
					protected void createFormContent(IManagedForm managedForm) {

						// The plant view should consume the whole page.
						Section section;
						FormToolkit toolkit = managedForm.getToolkit();

						// Set up the overall layout (FillLayout).
						Composite body = managedForm.getForm().getBody();
						body.setLayout(new FillLayout());

						// Create a Section for the plant view.
						section = toolkit.createSection(body, ExpandableComposite.NO_TITLE
								| ExpandableComposite.EXPANDED);
						populatePlantViewSection(section, toolkit);
						// No layout data to set for FillLayouts.

						return;
					}
				});
			} catch (PartInitException e) {
				logger.error(getClass().getName() + " Exception!",e);
			}
		}

		return;
	}

	/**
	 * Creates the content used for the plant view.
	 * 
	 * @param section
	 *            The {@code Section} that should contain the plant view.
	 * @param toolkit
	 *            The {@code FormToolkit} used to decorate widgets as necessary.
	 */
	private void populatePlantViewSection(Section section, FormToolkit toolkit) {
		// Get the background color to use later.
		Color background = section.getBackground();

		// Create an analysis composite to contain a ToolBar and an
		// analysis-based view.
		Composite analysisComposite = new Composite(section, SWT.NONE);
		analysisComposite.setBackground(background);
		analysisComposite.setLayout(new GridLayout(1, false));
		// Set the overall client of the plant view's Section.
		section.setClient(analysisComposite);

		// Create a ToolBarManager so we can add JFace Actions to it.
		ToolBarManager toolBarManager = new ToolBarManager(SWT.RIGHT);
		// Fill the ToolBar with customized controls.
		fillPlantViewToolBar(toolBarManager);
		toolBarManager.update(true);
		// Add it to the view.
		ToolBar toolBar = toolBarManager.createControl(analysisComposite);
		toolBar.setBackground(background);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

		// Create the plant view.
		TreeComposite components = findComponentBlock();
		factory.setTree(components);
		PlantComposite plant = factory.getPlant();
		plantView = new ViewFactory().createPlantView(plant);

		// Render the plant view in the analysis Composite.
		Composite plantComposite = plantView.createComposite(analysisComposite);
		plantComposite.setBackground(background);
		plantComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));

		// Make sure the factory/plant is reset when the plant view is disposed.
		plantComposite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				factory.setTree(new TreeComposite());
			}
		});

		return;
	}

	/**
	 * Fills the plant view's {@code ToolBar} with supported actions.
	 * 
	 * @param toolBar
	 *            The plant view's {@code ToolBar}'s manager.
	 */
	private void fillPlantViewToolBar(ToolBarManager toolBar) {
		Action action;

		// Set the action's image (a camera).
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath;
		URL imageURL;
		ImageDescriptor image;

		// TODO Use an ImageRegistry instead of hard-coded ImageDescriptors.

		final float moveRate = 1f;
		final float rotateRate = (float) (Math.PI * 0.1);

		// Add an action that toggles the wireframe boolean.
		// Also clear the wireframe setting.
		wireframe = false;
		toolBar.add(new Action("Wireframe") {
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
		ActionTree cameraTree = new ActionTree("Camera Orientation");
		cameraTree.add(new ActionTree(new Action("Reset to current default") {
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
		cameraTree.add(new ActionTree(new Action("XY (X right, Y up)") {
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
		cameraTree.add(new ActionTree(new Action("ZX (Z right, X up)") {
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
		toolBar.add(cameraTree.getContributionItem());

		// TODO Move this elsewhere in the ToolBar.
		action = new Action("Save Image") {
			@Override
			public void run() {
				plantView.exportImage();
			}
		};
		// Set the action's image (a camera).
		imagePath = new Path("icons" + System.getProperty("file.separator")
				+ "camera.png");
		imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		action.setImageDescriptor(imageDescriptor);
		ActionTree saveImageTree = new ActionTree(action);
		toolBar.add(saveImageTree.getContributionItem());

		toolBar.add(new Separator());

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
	private void removePlantPage() {
		// Dispose any resources required for the plant view.
		plantView = null;

		// Finally, remove the page itself.
		removePageWithID(PLANT_PAGE_ID);
	}

	/**
	 * Removes the page with the specified ID.
	 * 
	 * @param id
	 *            The ID of the page, e.g. {@link #PLANT_PAGE_ID} or
	 *            {@link #MESH_PAGE_ID}.
	 */
	private void removePageWithID(String id) {
		IFormPage page = findPage(id);
		if (page != null) {
			removePage(page.getIndex());
		}
	}

	/**
	 * Finds the "Components" block in the MOOSE tree.
	 * 
	 * @return The "Components" block, or an empty, default tree if one could
	 *         not be found.
	 */
	private TreeComposite findComponentBlock() {
		// This is a convenience method. Currently, it is very similar to
		// findMeshBlock(), so the code has been relocated and shared in
		// findNamedRootBlock(), although this may change soon.
		return findNamedRootBlock("Components");
	}

	/**
	 * Finds a block with the specified name under the top level of the MOOSE
	 * data tree.
	 * 
	 * @param name
	 *            The name of the block to find. This is not checked for null
	 *            since this is a private method.
	 * @return The first block ({@code TreeComposite}) with a matching name, or
	 *         a default, empty TreeComposite.
	 */
	private TreeComposite findNamedRootBlock(String name) {
		TreeComposite namedRootBlock = null;

		// Get the root TreeComposite from the form.
		TreeComposite root = (TreeComposite) iceDataForm
				.getComponent(MOOSEModel.mooseTreeCompositeId);

		// Find the "Mesh" TreeComposite. We will need to pull the mesh from
		// this node as a file resource.
		for (int i = 0; i < root.getNumberOfChildren(); i++) {
			TreeComposite child = root.getChildAtIndex(i);
			if (name.equals(child.getName())) {
				namedRootBlock = child;
				// Break from the loop.
				i = root.getNumberOfChildren();
			}
		}

		return (namedRootBlock != null ? namedRootBlock : new TreeComposite());
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

	/**
	 * In additon to the default behavior when saved, this makes sure the plant
	 * and/or mesh view pages are visible depending on the current MOOSE app and
	 * input file settings.
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);

		// If the selection is for RELAP-7, create a Plant View page
		// if one doesn't already exist. If the selection is NOT for
		// RELAP-7, delete any existing Plant View page.
		String appStr = appsEntry.getValue().toLowerCase();
		if ("relap".equals(appStr)) {
			addPlantPage();
		} else {
			removePlantPage();
		}

		return;
	}
}
