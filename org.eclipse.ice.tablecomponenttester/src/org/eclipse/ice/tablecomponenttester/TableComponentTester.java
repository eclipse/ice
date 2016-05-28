/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.tablecomponenttester;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.eavp.viz.modeling.base.BasicView;
import org.eclipse.eavp.viz.modeling.ShapeController;
import org.eclipse.eavp.viz.modeling.Shape;
import org.eclipse.ice.datastructures.entry.ContinuousEntry;
import org.eclipse.ice.datastructures.entry.DiscreteEntry;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsPair;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.item.Item;

@XmlRootElement(name = "TableComponentTester")
public class TableComponentTester extends Item {

	public TableComponentTester() {
		this(null);
	}

	public TableComponentTester(IProject project) {
		super(project);
	}

	@Override
	public void setupForm() {

		// Local Declarations
		MasterDetailsComponent mDetailsComp = new MasterDetailsComponent();
		DataComponent detailsComp1, detailsComp2, detailsComp3;
		MasterDetailsPair mdpair1, mdpair2, mdpair3;
		ArrayList<MasterDetailsPair> template;
		ArrayList<DataComponent> dataCompTemplate;
		ArrayList<String> masterTypeTemplate;
		IEntry entry1, entry2, entry3, entry4;
		ResourceComponent resourceComp = new ResourceComponent();
		TreeComposite parent = null, child1 = null, child2 = null, child3 = null;

		// Create the Form
		form = new Form();

		// Set the details of this Item
		setName("ICE Demonstration Component");
		setDescription("This component demonstrates the different types of "
				+ "data structures available in ICE and their visual "
				+ "representations.");

		// create a new instance of MasterDetailsComponent
		mDetailsComp = new MasterDetailsComponent();

		// Setup list for templates
		// Create Entries

		entry1 = new ContinuousEntry();
		entry1.setAllowedValues(Arrays.asList("0","50"));
		entry1.setDefaultValue("1");
		entry1.setValue("1");
		entry1.setName("Generic 1");

		entry2 = new DiscreteEntry();
		entry2.setAllowedValues(Arrays.asList("Check", "Not Checked"));
		entry2.setDefaultValue("Check");
		entry2.setValue("Check");
		entry2.setName("Generic 2");

		entry3 = new StringEntry();
		entry3.setDefaultValue("Text");
		entry3.setName("Generic 3");

		entry4 = new ContinuousEntry();
		entry4.setAllowedValues(Arrays.asList("0", "10000"));
		entry4.setDefaultValue("9001");
		entry4.setValue("9001");
		entry4.setName("Generic 4");
			
		// Create DataComponents
		detailsComp1 = new DataComponent();
		detailsComp2 = new DataComponent();
		detailsComp3 = new DataComponent();

		// Add entries
		detailsComp1.addEntry(entry1);
		detailsComp2.addEntry(entry2);
		detailsComp3.addEntry(entry3);
		// detailsComp3.addEntry(entry4);

		// Set names
		detailsComp1.setName("Generic #1");
		detailsComp2.setName("Generic #2");
		detailsComp3.setName("Generic #3");
		detailsComp1.setDescription("Generic #1 Description");
		detailsComp2.setDescription("Generic #2 Description");
		detailsComp3.setDescription("Generic #3 Description");

		// Create pairs
		mdpair1 = new MasterDetailsPair("Generic 1", detailsComp1);
		mdpair2 = new MasterDetailsPair("Generic 2", detailsComp2);
		mdpair3 = new MasterDetailsPair("Generic 3", detailsComp3);
		mdpair1.setMasterDetailsPairId(0);
		mdpair2.setMasterDetailsPairId(1);
		mdpair3.setMasterDetailsPairId(2);

		// Add pairs to template
		template = new ArrayList<MasterDetailsPair>();
		template.add(mdpair1);
		template.add(mdpair2);
		template.add(mdpair3);

		// create a page, set template
		mDetailsComp = new MasterDetailsComponent();
		mDetailsComp.setName("MultiLauncher");
		mDetailsComp.setDescription("A Lion");

		// check that the template needs to have contents
		mDetailsComp.setTemplates(template);
		form.addComponent(mDetailsComp);

		// ================================== NEXT PAGE: TableComponent
		// =============================================================================

		TableComponent table = new TableComponent();

		IEntry column1 = new StringEntry();
		column1.setDefaultValue("hello1");
		
		IEntry column2 = new DiscreteEntry();
		column2.setAllowedValues(Arrays.asList("Hello", "World"));

		IEntry column3 = new ContinuousEntry();
		column3.setAllowedValues(Arrays.asList("0.0", "2.0"));
		column3.setDefaultValue("0.0");

		column1.setName("Column 1 - Undefined");
		column2.setName("Column 2 - Discrete");
		column3.setName("Column 3 - Continues");

		column1.setDescription("desc 1");
		column2.setDescription("desc 2");
		column3.setDescription("desc 3");

		ArrayList<IEntry> template1 = new ArrayList<IEntry>();
		template1.add(column1);
		template1.add(column2);
		template1.add(column3);

		table.setRowTemplate(template1);

		table.addRow();
		table.addRow();
		table.addRow();

		table.setName("TableComponent");
		table.setDescription("Table Component description");
		table.setId(1);
		form.addComponent(table);

		// Setup and add a data component
		DataComponent component = new DataComponent();
		component.setDescription("This will describe the DataComponent.");
		component.setId(20120516);
		component.setName("DataComponent");
		component.addEntry(entry4);
		form.addComponent(component);

		// ===========================================================================Try
		// making geometry page
		Shape geometryModel = new Shape();
		BasicView geometryView = new BasicView();
		ShapeController geometryShape = new ShapeController(geometryModel,
				geometryView);
		GeometryComponent geometryComponent = new GeometryComponent();
		geometryComponent.setGeometry(geometryShape);
		geometryComponent.setId(108);
		geometryComponent.setName("ICE Geometry Editor");
		geometryComponent.setDescription("Create or edit a geometry in 3D.");
		// geometryComponent.addShape(shape);
		// geometryComponent.setShapes(shapes);
		form.addComponent(geometryComponent);

		// ===== Resource Component Page ===== //
		resourceComp.setName("Resource component");
		resourceComp.setId(109);
		resourceComp
				.setDescription("This component contains the files, resources "
						+ "and data available to this plug-in.");
		// Add files from the project space to the Resource Component
		if (project != null) {
			try {
				IResource[] resources = project.members();
				// Search the project members
				for (int i = 0; i < resources.length; i++)
					// Look for files
					if (resources[i].getType() == IResource.FILE) {
						IFile file = (IFile) resources[i];
						// Create resource
						ICEResource iceResource = new ICEResource(new File(
								file.getRawLocationURI()));
						iceResource.setId(i);
						iceResource.setName("File " + i);
						iceResource.setPath(file.getLocationURI());
						// Setup some entries for testing properties
						IEntry entryClone = (IEntry) entry1.clone();
						ArrayList<IEntry> properties = new ArrayList<IEntry>();
						properties.add(entryClone);
						properties.add(entry4);
						properties.add(entry3);
						properties.add(entry2);
						properties.add(entry1);
						// Give the clone a different name so that we can check
						// changes
						entryClone.setName("File " + i + " Property");
						// Add the properties
						iceResource.setProperties(properties);
						// Add it to the component
						resourceComp.addResource(iceResource);
					}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Add the component to the Form
		form.addComponent(resourceComp);

		// ===== TreeComposite =====//

		// Setup the parent
		parent = new TreeComposite();
		parent.setName("Parent Node");
		parent.setId(1);
		parent.setDescription("The parent");

		// Create the children
		child1 = new TreeComposite();
		child1.setName("Child Node");
		child1.setId(1);
		child1.setDescription("The first child");
		child2 = new TreeComposite();
		child2.setName("Child Node");
		child2.setId(2);
		child2.setDescription("The second child");
		child3 = new TreeComposite();
		child3.setName("Child Node");
		child3.setId(3);
		child3.setDescription("The second child");

		// Add the children to the parent
		parent.setNextChild(child1);
		parent.setNextChild(child2);

		// Add the third child to the second
		child2.setNextChild(child3);

		// Add data nodes to the children
		child1.addComponent(detailsComp1);
		child2.addComponent(detailsComp2);
		child2.addComponent(detailsComp3);
		child3.addComponent(mDetailsComp);

		// Add the TreeComposite to the Form
		form.addComponent(parent);

		return;
	}
}
