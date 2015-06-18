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
package org.eclipse.ice.materials.ui;

import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.datastructures.form.MaterialStack;
import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a content provider that reads an IMaterialsDatabase and lists the
 * materials in it.
 * 
 * @author Jay Jay Billings
 * 
 */
public class MaterialsDatabaseContentProvider implements ITreeContentProvider {

	/**
	 * The TreeViewer that is rendering the content provider.
	 */
	TreeViewer treeViewer;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		treeViewer = (TreeViewer) viewer;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		Object [] result = null;
		if (inputElement instanceof ArrayList<?>) {
			result = ((ArrayList<Material>) inputElement).toArray();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		
		// The return value
		Object [] retVal = null;
		
		// This changes depending on whether it is the initial input or one of
		// the Materials under it
		if(parentElement instanceof MaterialStack){
			Material material = ((MaterialStack)parentElement).getMaterial();
			retVal = material.getComponents().toArray();
		} else if (parentElement instanceof Material) {
			Material material = (Material) parentElement;
			// Only return the children of this Material
			retVal = material.getComponents().toArray();
		} else if (parentElement instanceof ArrayList<?>) {
			ArrayList<Material> materials = (ArrayList<Material>) parentElement;
			// Just return all of the contents as an array
			retVal = materials.toArray();
		}
			
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		
		// The return value
		boolean retVal = false;
		
		// This changes depending on whether it is the initial input or one of
		// the Materials under it
		if (element instanceof MaterialStack){
			Material material = ((MaterialStack)element).getMaterial();
			List<MaterialStack> children = material.getComponents();
			retVal = !children.isEmpty();
		}
		if (element instanceof Material) {
			Material material = (Material) element;
			List<MaterialStack> children = material.getComponents();
			retVal = !children.isEmpty();
		} else if (element instanceof ArrayList<?>) {
			ArrayList<Material> materials = (ArrayList<Material>) element;
			retVal = !materials.isEmpty();
		}
		
		return retVal;
	}

}
