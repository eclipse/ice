package org.eclipse.ice.reflectivity.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.reflectivity.MaterialSelection;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractSectionDescriptor;
import org.eclipse.ui.views.properties.tabbed.AbstractTabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider;

/**
 * Provides custom tabs to the reflectivity model's custom tabbed properties
 * view. The first tab describes the data component and input values for its
 * entries. The second provides an interface for editing the values in the
 * table.</br>
 * </br>
 * 
 * 
 * TODO: Need to implement visitor pattern for Data and List components! This is
 * a much better way of handling new selections rather than computing everything
 * in the getTabDescriptors() method.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class ReflectivityTabDescriptorProvider
		implements ITabDescriptorProvider {

	/**
	 * The tab descriptors.
	 */
	ITabDescriptor[] descriptors;

	/**
	 * The data component that is currently being inspected
	 */
	DataComponent component;

	/**
	 * The list component for the current reflectivity model table
	 */
	ListComponent listComp;

	/**
	 * The table selection in the model
	 */
	MaterialSelection tableSelection;

	/**
	 * The constructor
	 */
	public ReflectivityTabDescriptorProvider() {
		// Local declarations
		component = null;
		descriptors = new ITabDescriptor[2];
	}

	private final IFilter filter = new IFilter() {
		@Override
		public boolean select(Object toTest) {
			return (toTest instanceof DataComponent
					|| toTest instanceof MaterialSelection
					|| toTest instanceof ListComponent);
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider#
	 * getTabDescriptors(org.eclipse.ui.IWorkbenchPart,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public ITabDescriptor[] getTabDescriptors(IWorkbenchPart part,
			ISelection selection) {

		// Make sure the selection is valid
		if (selection != null && selection instanceof IStructuredSelection) {

			Object[] selectedObjects = ((IStructuredSelection) selection)
					.toArray();
			// If there are objects in the selection
			if (selectedObjects.length >= 3) {
				// Set the data component if it is valid
				Object first = selectedObjects[0];
				if (first instanceof DataComponent) {
					component = (DataComponent) first;
				}
				Object second = selectedObjects[1];
				if (second instanceof ListComponent) {
					listComp = (ListComponent) second;
				}
				// Set the selection from the table if it is valid
				Object third = selectedObjects[2];
				if (third instanceof MaterialSelection) {
					tableSelection = (MaterialSelection) third;
				}
			}

			// Get a reference to the data component
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (obj instanceof DataComponent) {
				component = (DataComponent) obj;
			}

			// Create the tab first if it has not been done already
			if (descriptors[0] == null) {

				// New tab, generic and the same for all reflectivity models
				AbstractTabDescriptor inputTabDescriptor = new AbstractTabDescriptor() {

					@Override
					public String getCategory() {
						return "Reflectivity";
					}

					@Override
					public String getId() {
						return "Reflectivity.Input";
					}

					@Override
					public String getLabel() {
						return "Inputs";
					}

				};

				// Set the tab provider
				descriptors[0] = inputTabDescriptor;
			}

			if (descriptors[1] == null) {

				AbstractTabDescriptor editCellTab = new AbstractTabDescriptor() {

					@Override
					public String getCategory() {
						return "Reflectivity";
					}

					@Override
					public String getId() {
						return "Reflectivity.Cell";
					}

					@Override
					public String getLabel() {
						return "Cell Editor";
					}
				};

				descriptors[1] = editCellTab;
			}

			ITabDescriptor tab = descriptors[0];

			// Create a SectionDescriptor for the data component's inputs
			AbstractSectionDescriptor generalSection = new AbstractSectionDescriptor() {

				@Override
				public String getId() {
					return "Input:";
				}

				@Override
				public ISection getSectionClass() {
					ReflectivityDataPropertySection section;
					section = new ReflectivityDataPropertySection();
					section.setDataComponent(component);
					return section;
				}

				@Override
				public String getTargetTab() {
					return tab.getId();
				}

				@Override
				public IFilter getFilter() {
					return filter;
				}

			};

			// Add the section descriptor to the tab
			List<AbstractSectionDescriptor> sectionDescriptors = new ArrayList<AbstractSectionDescriptor>();
			sectionDescriptors.add(generalSection);

			((AbstractTabDescriptor) tab)
					.setSectionDescriptors(sectionDescriptors);

			ITabDescriptor tab2 = descriptors[1];

			// Create a SectionDescriptor for the data component's inputs
			AbstractSectionDescriptor cellSection = new AbstractSectionDescriptor() {

				@Override
				public String getId() {
					return "Cell:";
				}

				@Override
				public ISection getSectionClass() {
					ReflectivityCellEditorSection section;
					section = new ReflectivityCellEditorSection();
					section.setMaterialSelection(tableSelection);
					section.setListComponent(listComp);
					return section;

				}

				@Override
				public String getTargetTab() {
					return tab2.getId();
				}

				@Override
				public IFilter getFilter() {
					return filter;
				}

			};

			// Add the section descriptor to the tab
			sectionDescriptors = new ArrayList<AbstractSectionDescriptor>();
			sectionDescriptors.add(cellSection);

			((AbstractTabDescriptor) tab2)
					.setSectionDescriptors(sectionDescriptors);

		}

		return descriptors;
	}

}
