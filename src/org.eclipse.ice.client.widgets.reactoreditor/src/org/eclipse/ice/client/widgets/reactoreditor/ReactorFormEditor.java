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
package org.eclipse.ice.client.widgets.reactoreditor;

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEFormPage;
import org.eclipse.ice.client.widgets.ICEMasterDetailsPage;
import org.eclipse.ice.client.widgets.ICEResourcePage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.reactorAnalyzer.ReactorAnalyzer;
import org.eclipse.ice.reactorAnalyzer.ReactorComposite;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

/**
 * This class extends the default ICEFormEditor to enable it to draw data from
 * the ReactorAnalyzer better.
 * 
 * @author Jay Jay Billings
 * 
 */
public class ReactorFormEditor extends ICEFormEditor {

	/**
	 * ID for Eclipse
	 */
	public static final String ID = "org.eclipse.ice.client.widgets.reactor.editor";

	/**
	 * The ReactorPage displayed by this ReactorFormEditor. There is only one
	 * AnalysisToolComposite for displaying reactor information.
	 */
	private ReactorPage reactorPage;

	/**
	 * The input to this form editor. It contains useful information, like the
	 * {@link IAnalysisWidgetRegistry} used to generate {@link IAnalysisView}s.
	 */
	private ReactorFormInput input;

	/**
	 * This operation overrides init so that the ICE Form, passed as an
	 * IEditorInput, can be stored. Furthermore, it gathers any input that is
	 * specifically associated with a ReactorFormEditor.
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) {

		// Check the input. We *must* have a ReactorFormInput.
		if (!(input instanceof ReactorFormInput)) {
			throw new RuntimeException(
					"Input passed to ReactorFormEditor.init()"
							+ " is not of type ReactorFormInput.");
		}
		// Store a reference to the form input.
		this.input = (ReactorFormInput) input;

		// Continue with the super-class' init() behavior.
		super.init(site, input);

		// Register with the Form shared with the ReactorAnalyzer Item. This is
		// so we can receive notifications when new components are added.
		iceDataForm.register(this);

		return;
	}

	/**
	 * This operation overrides the default drawing routines of ICE's Form
	 * Editor to load the custom analysis widgets for nuclear reactors.
	 */
	@Override
	protected void addPages() {

		// Local Declaration
		ArrayList<ICEFormPage> formPages = new ArrayList<ICEFormPage>();
		ResourceComponent resourceComponent;
		ArrayList<Component> components;

		// Loop over the DataComponents and get them into the map
		for (Component i : iceDataForm.getComponents()) {
			System.out.println("ReactorFormEditor Message: "
					+ "Adding component " + i.getName() + " " + i.getId());
			i.accept(this);
		}

		// ---- Create the ReactorPage. ---- //

		// Get the reactor Components. If the IReactorEditor initialized
		// properly, there should be an input and reference reactor component.
		components = componentMap.get("reactor");
		if (!(components.isEmpty())) {
			// Get the ReactorComposite. It should be the only IReactorComponent
			// held by the form.
			ReactorComposite reactorComposite = (ReactorComposite) components
					.get(0);

			// Create the SelectionProvider used to feed the current selection
			// from all the AnalysisViews to the Editor's IWorkbenchPartSite.
			// FIXME - I'm not sure how this will work with multiple reactor
			// editors open!
			SelectionProvider provider = new SelectionProvider();
			getSite().setSelectionProvider(provider);

			// Create the page itself.
			reactorPage = new ReactorPage(this, "Reactor", "Reactor Page");

			// Set the registry, broker, selection provider, and reactor
			// composite for the ReactorPage.
			reactorPage.setAnalysisWidgetRegistry(input.getRegistry());
			reactorPage.setStateBroker(input.getStateBroker());
			reactorPage.setSelectionProvider(provider);
			reactorPage.setReactorComposite(reactorComposite);

			// Add the resource component for comparison data.
			reactorPage.addComponent((ResourceComponent) (componentMap
					.get("output").get(0)));

			// Add the data Components (input and reference loaders) to the
			// first page only.
			components = componentMap.get("data");
			if (!(components.isEmpty())) {
				// Add the data Components to the page.
				for (Component comp : components) {
					reactorPage.addComponent(comp);
				}
			}
			// Add the ReactorPages to the Editor's FormPages.
			formPages.add(reactorPage);
		} else {
			System.out.println("ReactorFormEditor Message: "
					+ "No IReactorEditor present. IReactorComponents will not "
					+ "be rendered.");
		}
		/* ---------------------------------- */

		/* ---- Create the Analysis Configuration page. ---- */
		// This page allows the user to configure KDD strategies and run them.

		// Create pages for the MasterDetailsComponents
		if (!(componentMap.get("masterDetails").isEmpty())) {
			// Local Declarations
			MasterDetailsComponent masterDetailsComponent = null;

			// Get the MasterDetailsComponent and create the MasterDetails page.
			if (!(componentMap.get("masterDetails").isEmpty())) {
				masterDetailsComponent = (MasterDetailsComponent) (componentMap
						.get("masterDetails").get(0));
				if (masterDetailsComponent != null) {
					// Get the name
					String name = masterDetailsComponent.getName();
					// Make the page
					ICEMasterDetailsPage iCEMasterDetailsPage = new ICEMasterDetailsPage(
							this, "MDPid", name);

					// Set the MasterDetailsComponent
					iCEMasterDetailsPage
							.setMasterDetailsComponent(masterDetailsComponent);

					formPages.add(iCEMasterDetailsPage);
				}
			}
		}
		/* ------------------------------------------------- */

		/* ---- Create the Analysis Artifacts page. ---- */
		// This page allows the user to view resources (results) from the KDD
		// strategies on the previous page.

		// Create the page for ResourceComponents
		if (!(componentMap.get("output").isEmpty())) {
			resourceComponent = (ResourceComponent) (componentMap.get("output")
					.get(0));
			if (resourceComponent != null) {
				// Make the page
				resourceComponentPage = new ICEResourcePage(this,
						resourceComponent.getName(),
						resourceComponent.getName());
				// Set the ResourceComponent
				resourceComponentPage.setResourceComponent(resourceComponent);
			}
			formPages.add(resourceComponentPage);
		}
		/* --------------------------------------------- */

		// Add the pages created above to the ReactorFormEditor.
		try {
			for (ICEFormPage i : formPages) {
				addPage(i);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * Overrides the doSave() behavior from ICEFormEditor to ensure that all
	 * ReactorSectionParts (AnalysisToolComposites) in rendered ReactorPages
	 * refresh with the new data.
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// Perform the standard doSave behavior.
		super.doSave(monitor);

		// We need to refresh the other reactor pages, too! Otherwise, if they
		// have been rendered, they will not refresh their data immediately.

		// Get the currently-selected page.
		ICEFormPage selectedPage = (ICEFormPage) this.getSelectedPage();
		// For the other ReactorPages, refresh all of their parts.
		if (reactorPage != null && reactorPage != selectedPage) {
			// Check to make sure the ReactorPage has been rendered.
			IManagedForm managedForm = reactorPage.getManagedForm();
			if (managedForm != null) {
				for (IFormPart part : managedForm.getParts()) {
					part.refresh();
				}

			}
		}

		/*
		 * To re-create the bug that necessitates this override, comment out the
		 * above code (except for the super call). Open a new Reactor Analyzer
		 * in ICE. Briefly open the second Reactor Page (Fuel Assembly/Pin),
		 * then go back to the first page (Reactor/Fuel Assembly).
		 * 
		 * Now, load an SFReactor file. The ICEFormEditor.doSave() method calls
		 * refresh on the currently-opened ReactorPage. However, the second
		 * ReactorPage will only be updated when the reactor SFRComponent
		 * notifies its listeners, which does not happen until another SFR file
		 * is loaded.
		 * 
		 * What this means is that the second page will still display an LWR
		 * grid until you load a second SFR file!
		 * 
		 * The third page is OK, because it's not been rendered yet.
		 */
	}

	@Override
	public void setInput(IEditorInput editorInput) {
		// // Get the old ReactorFormInput if possible.

		super.setInput(editorInput);

		// Update the form with any new input/reference components and pass on
		// any new information required by the ReactorPage.
		if (editorInput instanceof ReactorFormInput) {
			// Get the ReactorFormInput and the Form associated with this
			// editor/Reactor Analyzer.
			ReactorFormInput formInput = (ReactorFormInput) editorInput;
			Form form = formInput.getForm();

			// Get the ReactorComposite from the form.
			ReactorComposite reactorComposite = (ReactorComposite) form
					.getComponent(ReactorAnalyzer.reactorCompositeId);

			// If necessary, update the input reactor component in the form.
			IReactorComponent input = formInput.getInputReactorComponent();
			if (input != null) {

				// Get the ID used for the input reactor component.
				int id = ReactorAnalyzer.inputReactorComponentId;

				// If the component is different from the previous one, we need
				// to update the form.
				IReactorComponent oldInput = reactorComposite
						.getReactorComponent(id);
				if (input != oldInput) {
					// Update the ReactorComposite.
					reactorComposite.setComponent(id, input);
				}
			}

			IReactorComponent reference = formInput
					.getReferenceReactorComponent();
			if (reference != null) {

				// Get the ID used for the reference reactor component.
				int id = ReactorAnalyzer.referenceReactorComponentId;

				// If the component is different from the previous one, we need
				// to update the form.
				IReactorComponent oldReference = reactorComposite
						.getReactorComponent(id);
				if (reference != oldReference) {
					// Update the ReactorComposite.
					reactorComposite.setComponent(id, reference);
				}
			}

			// Set the reactor page's StateBroker if the page has been created.
			StateBroker newBroker = formInput.getStateBroker();
			if (reactorPage != null) {
				reactorPage.setStateBroker(newBroker);
			}
			// Tell the core that the form has been updated.
			notifyUpdateListeners();
		}

		return;
	}

}
