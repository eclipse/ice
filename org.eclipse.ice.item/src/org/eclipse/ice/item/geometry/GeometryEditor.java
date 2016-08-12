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
package org.eclipse.ice.item.geometry;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.january.geometry.Geometry;
import org.eclipse.january.geometry.GeometryFactory;
import org.eclipse.january.geometry.INode;
import org.eclipse.january.geometry.xtext.obj.importer.OBJGeometryImporter;

import model.IRenderElement;

/**
 * <p>
 * This is a subclass of Item that provides 3D geometry editing services to ICE.
 * It overrides the setupForm() operation and provides a Form that contains a
 * GeometryComponent. More information about the exact contents of the Form is
 * available on the setupForm() operation description below.
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlRootElement(name = "GeometryEditor")
public class GeometryEditor extends Item {
	/**
	 * <p>
	 * An alternative nullary constructor used primarily for serialization. The
	 * setProject() operation must be called if this constructor is used!
	 * </p>
	 * 
	 */
	public GeometryEditor() {

		// Punt to the other Constructor
		this(null);

	}

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 * @param project
	 *            <p>
	 *            The Eclipse project used by the GeometryEditor.
	 *            </p>
	 */
	public GeometryEditor(IProject project) {

		// Call the super constructor
		super(project);

		// Remove the action from the list that allows for writing key-value
		// pairs to a file. The GeometryComponent in the Form can't be written
		// like that.
		allowedActions.remove(taggedExportActionString);

		return;
	}

	/**
	 * <p>
	 * This operation overrides setupForm() to provide a Form that contains a
	 * single GeometryComponent. This component has id=1 and is named
	 * "Geometry Data."
	 * </p>
	 * 
	 */
	@Override
	protected void setupForm() {

		// Set the name, description and type
		setName("Geometry Editor");
		itemType = ItemType.Geometry;
		setDescription(
				"This tool allows you to create and edit a 3D geometry.");

		// Instantiate the Form. It's just a regular Form for this Item.
		form = new Form();

		// Create a GeometryComponent to hold the Geometry
		GeometryComponent geometryComp = new GeometryComponent();

		geometryComp.setGeometry(GeometryFactory.eINSTANCE.createGeometry());
		geometryComp.setName("Geometry Data");
		geometryComp.setId(1);
		geometryComp.setDescription(getDescription());

		// Add the component to the Form
		form.addComponent(geometryComp);

		return;

	}
	
	@Override
	public void loadInput(String file) {
		
		// Only import if a valid stl file
		if (file != null && (file.toLowerCase(Locale.ENGLISH).endsWith(".stl") || file.toLowerCase(Locale.ENGLISH).endsWith(".obj"))) {
			
			//Convert the partial file path string into a full path
			IFile inputFile = project.getFile(file);
			IPath inputPath = inputFile.getProjectRelativePath();
			Path path = Paths.get(project.getLocation().toOSString() + System.getProperty("file.separator") + inputPath.toOSString());
			
			//TODO Why 1? Remove this magic number
			GeometryComponent comp = (GeometryComponent) form.getComponent(1);
			Geometry geom = comp.getGeometry();
			
			//Import the file according to its type
			Geometry imported = null;
			if(file.toLowerCase(Locale.ENGLISH).endsWith(".stl")){
				imported = GeometryFactory.eINSTANCE.createSTLGeometryImporter().load(path);
			} else{
				imported = new OBJGeometryImporter().load(path);
			}

			//Add each of the new nodes to the root geometry
			synchronized (geom) {
				for(int i=0; i<imported.getNodes().size(); i++) {
					INode node = (INode) imported.getNodes().get(i).clone();
					geom.addNode(node);
				}
			}
		}
	}
}