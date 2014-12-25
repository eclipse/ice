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
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.reactorAnalyzer.ReactorAnalyzer;
import org.eclipse.ice.reactorAnalyzer.ReactorComposite;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This class extends the default ICEFormPage to provide custom page composed of
 * widgets for reactors.
 * 
 * This class only cares about the IReactorComponents and DataComponents that
 * are passed to it and all other visitation operations do nothing.
 * 
 * @author Jay Jay Billings
 * 
 */
public class ReactorPage extends ICEFormPage implements IComponentVisitor,
		IUpdateableListener {

	/**
	 * The list of DataComponents that is formed by visiting the Components
	 * assigned to this page. These are typically the input and reference data.
	 */
	private final ArrayList<DataComponent> dataComponents;

	/**
	 * This Composite contains the input and reference reactor components.
	 */
	private ReactorComposite reactorComposite;

	/**
	 * The resource Component. This is typically the "Comparison Reactor".
	 */
	private ResourceComponent resourceComponent;

	/**
	 * The SectionPart that contains the {@link #analysisComposite}.
	 */
	private SectionPart sectionPart;

	/**
	 * The AnalysisToolComposite that renders the reactor data for this page.
	 */
	private AnalysisToolComposite analysisComposite;
	/**
	 * The registry maintains a Map of factories for analysis widgets.
	 */
	private IAnalysisWidgetRegistry registry;
	/**
	 * The StateBroker that manages state information about the different views.
	 */
	private StateBroker stateBroker;
	/**
	 * The ISelectionProvider that manages the current selection in the
	 * ReactorEditor. The current selection has its properties displayed in the
	 * ICE Properties View.
	 */
	private ISelectionProvider selectionProvider;

	/**
	 * The constructor
	 * 
	 * @param editor
	 *            The FormEditor that is constructing this page
	 * @param id
	 *            The unique identifier of this page.
	 * @param title
	 *            The title that this page should display in the editor.
	 */
	public ReactorPage(FormEditor editor, String id, String title) {

		// Call the super constructor
		super(editor, id, title);

		// Set the FormEditor if it is not null and throw an exception
		// otherwise.
		if (editor != null && editor instanceof ICEFormEditor) {
			this.editor = (ICEFormEditor) editor;
		} else {
			throw new RuntimeException("Editor in ReactorPage "
					+ " constructor cannot be null.");
		}
		// Setup the list for Components
		dataComponents = new ArrayList<DataComponent>();
		reactorComposite = null;

		return;
	}

	/**
	 * Sets the IAnalysisWidgetRegistry used to generate {@link IAnalysisView}s
	 * for the {@link #analysisComposite}.
	 * 
	 * @param registry
	 *            The IAnalysisWidgetRegistry to use.
	 */
	public void setAnalysisWidgetRegistry(IAnalysisWidgetRegistry registry) {
		if (registry != null) {
			this.registry = registry;
		}
		return;
	}

	/**
	 * Sets the ISelectionProvider that manages the current selection in the
	 * ReactorEditor. The current selection has its properties displayed in the
	 * ICE Properties View.
	 * 
	 * @param provider
	 *            The new ISelectionProvider.
	 */
	public void setSelectionProvider(ISelectionProvider provider) {
		// Only set the provider if it is not null.
		if (provider != null) {
			selectionProvider = provider;
		}
		return;
	}

	/**
	 * This operation sets the state broker that will be shared by the parts of
	 * the ReactorEditor to maintain view state.
	 * 
	 * @param broker
	 *            The state broker
	 */
	public void setStateBroker(StateBroker broker) {
		// Only set the broker if it is not null.
		if (broker != null) {
			stateBroker = broker;

			// Update the section parts.
			if (analysisComposite != null) {
				analysisComposite.setStateBroker(broker);
			}
		}

		return;
	}

	/**
	 * Sets the ReactorComposite that contains the input and reference reactor
	 * components for this reactor page.
	 * 
	 * @param reactorComposite
	 *            The new ReactorComposite.
	 */
	public void setReactorComposite(ReactorComposite reactorComposite) {
		// Only proceed if the new reactor composite is not null.
		if (reactorComposite != null) {
			// If necessary, unregister from the old reactor composite.
			if (this.reactorComposite != null) {
				this.reactorComposite.unregister(this);
			}

			// Set the new one and register with it.
			this.reactorComposite = reactorComposite;
			reactorComposite.register(this);

			// Update the page based on the new reactor composite.
			update(reactorComposite);
		}

		return;
	}

	/**
	 * This operation adds a Component to the set of Components that are managed
	 * by this page.
	 * 
	 * @param component
	 *            A new Component for this page to manage
	 */
	public void addComponent(Component component) {
		// begin-user-code

		if (component != null) {
			// Visit the components so that they can be sorted
			component.accept(this);
		}

		return;
		// end-user-code
	}

	/**
	 * This operation overrides the parent operation to create the graphical
	 * content of the Form.
	 * 
	 * @param managedForm
	 *            The IManagedForm from the parent FormEditor.
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		// begin-user-code

		// Local Declarations
		final ScrolledForm form = managedForm.getForm();
		form.getBody().setLayout(new FillLayout());
		form.setMinWidth(10);

		// ---- Create the AnalysisToolComposite ---- //
		// Get the toolkit used to create Composites, Sections, etc.
		FormToolkit formToolkit = managedForm.getToolkit();

		// Create a single Section with a single SectionPart. When the form
		// updates, it calls the SectionPart's refresh() method. This method
		// should call this class' refreshContent() method.
		Section section = formToolkit.createSection(form.getBody(),
				Section.NO_TITLE | Section.EXPANDED);
		sectionPart = new SectionPart(section) {
			@Override
			public void refresh() {
				super.refresh();

				// Call the method in the ReactorPage. There's no need for a
				// separate class just for this behavior.
				refreshContent();
			}
		};
		// Add the section part to the form so that updates will be sent to the
		// part (and thus will call refreshContent()).
		managedForm.addPart(sectionPart);

		// Create the ATC inside the section, and set the ATC as the section's
		// primary client Composite.
		analysisComposite = new AnalysisToolComposite(section, stateBroker,
				registry, selectionProvider);
		section.setClient(analysisComposite);

		// Set the background color for the ATC to be the same as the section.
		analysisComposite.setBackground(section.getBackground());

		// Refresh the information displayed in the ATC.
		refreshContent();
		// ------------------------------------------ //

		return;
		// end-user-code
	}

	/**
	 * This method refreshes {@link #analysisComposite}. Normally, this would be
	 * taken care of by the {@link IFormPart}s on this page. However, we only
	 * have one part and do not need to have a separate class to handle the
	 * part's behavior (which is just to wrap an {@link AnalysisToolComposite}.
	 */
	protected void refreshContent() {

		// If possible, update the ATC.
		if (analysisComposite != null) {
			// Get the input and reference reactor components from the
			// composite.
			IReactorComponent input = reactorComposite
					.getReactorComponent(ReactorAnalyzer.inputReactorComponentId);
			IReactorComponent reference = reactorComposite
					.getReactorComponent(ReactorAnalyzer.referenceReactorComponentId);

			// Set the input and reference reactor for the ATC.
			analysisComposite.setData(DataSource.Input.toString(), input);
			analysisComposite.setData(DataSource.Reference.toString(),
					reference);
		}

		// If the ResourceComponent has been populated with
		// ICEResources, then give it to the AnalysisComposite.
		if (resourceComponent != null) {
			ArrayList<ICEResource> resources = resourceComponent.getResources();
			if (resources != null && !resources.isEmpty()) {
				analysisComposite.setData(DataSource.Comparison.toString(),
						resourceComponent);
			}
		}
		return;
	}

	/**
	 * This is activated by the {@link IReactorComponent}s in
	 * {@link #reactorComposite} and by {@link #resourceComponent}.
	 */
	public void update(IUpdateable component) {

		// Note that this is called by the reactor composite AND the resource
		// component.

		// Redraw - note that we must use the UI Thread
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				// Refresh the section part. This will ultimately call
				// refreshContent().
				sectionPart.refresh();
			}
		});
	}

	// ---- Implements IComponentVisitor ---- //
	// These operations should update the references to components that are
	// important to displaying reactor information in the AnalysisToolComposite,
	// and, if necessary, register with them.

	public void visit(DataComponent component) {
		// Add the data component to the list
		dataComponents.add(component);
	}

	public void visit(ResourceComponent component) {
		// Set the resource component and register with it.
		resourceComponent = component;
		resourceComponent.register(this);
	}

	public void visit(TableComponent component) {
		// Nothing to do
	}

	public void visit(MatrixComponent component) {
		// Nothing to do
	}

	public void visit(IShape component) {
		// Nothing to do
	}

	public void visit(GeometryComponent component) {
		// Nothing to do
	}

	public void visit(MasterDetailsComponent component) {
		// Nothing to do
	}

	public void visit(TreeComposite component) {
		// Nothing to do
	}

	public void visit(IReactorComponent component) {
		// Nothing to do
	}

	public void visit(TimeDataComponent component) {
		// Nothing to do
	}

	public void visit(MeshComponent component) {
		// Nothing to do
	}


	public void visit(AdaptiveTreeComposite component) {
		// Nothing to do

	}
	// -------------------------------------- //

	@Override
	public void visit(EMFComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ListComponent component) {
		// TODO Auto-generated method stub
		
	}

}
