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
package org.eclipse.ice.reactor.perspective;

import org.eclipse.ice.io.hdf.HdfIOFactory;
import org.eclipse.ice.reactor.ILWRComponentVisitor;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.bwr.BWReactor;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;
import org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.core.Material;
import org.eclipse.ice.reactor.sfr.core.MaterialBlock;
import org.eclipse.ice.reactor.sfr.core.SFReactor;
import org.eclipse.ice.reactor.sfr.core.assembly.PinAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.ReflectorAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRPin;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRRod;
import org.eclipse.ice.reactorAnalyzer.ReactorReaderFactory;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.updateableComposite.Composite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * This class fills out the tree in the {@link ReactorViewer}. Top level
 * elements are {@link ICEResource}s for HDF5 reactor files. Their children are
 * {@link IReactorComponent}s in the reactor file.
 * 
 * @author Jordan H. Deyton, tnp
 * 
 */
public class ReactorTreeContentProvider implements ITreeContentProvider {

	/**
	 * A ParentVisitor is used to get the parent object for an
	 * IReactorComponent.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private interface ParentVisitor extends ILWRComponentVisitor,
			ISFRComponentVisitor {

		// TODO This visitor may need to be implemented at some point.

		/**
		 * Get the parent object for an IReactorComponent.
		 * 
		 * @param component
		 *            The child IReactorComponent.
		 * @return An object for use in the ReactorTreeContentProvider, or null
		 *         if a parent could not be found.
		 */
		public Object getParent(IReactorComponent component);
	}

	/**
	 * A ChildrenVisitor is used to get the children of an IReactorComponent.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private interface ChildrenVisitor extends ILWRComponentVisitor,
			ISFRComponentVisitor {
		/**
		 * Get the child objects for an IReactorComponent.
		 * 
		 * @param component
		 *            The parent IReactorComponent.
		 * @return An array of objects corresponding to children of the
		 *         IReactorComponent.
		 */
		public Object[] getChildren(IReactorComponent component);
	}

	/**
	 * A visitor that determines the parent object for a particular
	 * IReactorComponent.
	 */
	private final ParentVisitor parentVisitor = new ParentVisitor() {

		/**
		 * The parent object. This should be set to null before visiting, and
		 * the visit operations should set it.
		 */
		private IReactorComponent parent;

		/**
		 * Gets the parent object for the IReactorComponent. It may be an
		 * LWRComponent or an SFRComponent.
		 */
		public Object getParent(IReactorComponent component) {
			parent = null;

			// Visit the component if possible.
			if (component instanceof LWRComponent) {
				((LWRComponent) component).accept(this);
			} else if (component instanceof SFRComponent) {
				((SFRComponent) component).accept(this);
			}

			return parent;
		}

		// ---- Implements ILWRComponentVisitor ---- //
		public void visit(PressurizedWaterReactor lwrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(BWReactor lwrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(FuelAssembly lwrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(RodClusterAssembly lwrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(LWRRod lwrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(ControlBank lwrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(IncoreInstrument lwrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(Tube lwrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(Ring lwrComp) {
			// TODO Auto-generated method stub

		}

		// ----------------------------------------- //

		// ---- Implements ISFRComponentVisitor ---- //
		public void visit(SFReactor sfrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(SFRAssembly sfrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(PinAssembly sfrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(ReflectorAssembly sfrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(SFRPin sfrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(SFRRod sfrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(MaterialBlock sfrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(Material sfrComp) {
			// TODO Auto-generated method stub

		}

		public void visit(org.eclipse.ice.reactor.sfr.core.assembly.Ring sfrComp) {
			// TODO Auto-generated method stub

		}
		// ----------------------------------------- //
	};

	/**
	 * A visitor that determines the child objects for a particular
	 * IReactorComponent.
	 */
	private final ChildrenVisitor childrenVisitor = new ChildrenVisitor() {

		/**
		 * The array of children of the current IReactorComponent. This should
		 * be set to null before visiting, and the visit operations should
		 * change it.
		 */
		private Object[] children;

		/**
		 * Gets the child objects for the IReactorComponent. If there are no
		 * children, the array will be empty.
		 */
		public Object[] getChildren(IReactorComponent component) {

			children = null;

			// Visit the component if possible.
			if (component instanceof LWRComponent) {
				((LWRComponent) component).accept(this);
			} else if (component instanceof SFRComponent) {
				((SFRComponent) component).accept(this);
			}

			// Make sure the return value is not null. If we have a Composite
			// implementation, we can get the children. This catches
			// LWRComposites and SFRComposites, which are not part of their
			// respective visitor interfaces.
			if (children == null) {
				if (component instanceof Composite) {
					children = ((Composite) component).getComponents()
							.toArray();
				} else {
					children = new Object[] {};
				}
			}

			return children;
		}

		// ---- Implements ILWRComponentVisitor ---- //
		public void visit(PressurizedWaterReactor lwrComp) {
			children = lwrComp.getComponents().toArray();
		}

		public void visit(BWReactor lwrComp) {
			children = lwrComp.getComponents().toArray();
		}

		public void visit(FuelAssembly lwrComp) {
			children = lwrComp.getComponents().toArray();
		}

		public void visit(RodClusterAssembly lwrComp) {
			children = lwrComp.getComponents().toArray();
		}

		public void visit(LWRRod lwrComp) {
			// Nothing to do... yet.
		}

		public void visit(ControlBank lwrComp) {
			// Nothing to do... yet.
		}

		public void visit(IncoreInstrument lwrComp) {
			// Nothing to do... yet.
		}

		public void visit(Tube lwrComp) {
			// Nothing to do... yet.
		}

		public void visit(Ring lwrComp) {
			// Nothing to do... yet.
		}

		// ----------------------------------------- //

		// ---- Implements ISFRComponentVisitor ---- //
		public void visit(SFReactor sfrComp) {
			children = sfrComp.getComponents().toArray();
		}

		public void visit(SFRAssembly sfrComp) {
			children = sfrComp.getComponents().toArray();
		}

		public void visit(PinAssembly sfrComp) {
			children = sfrComp.getComponents().toArray();
		}

		public void visit(ReflectorAssembly sfrComp) {
			children = sfrComp.getComponents().toArray();
		}

		public void visit(SFRPin sfrComp) {
			// Nothing to do... yet.
		}

		public void visit(SFRRod sfrComp) {
			// Nothing to do... yet.
		}

		public void visit(MaterialBlock sfrComp) {
			// Nothing to do... yet.
		}

		public void visit(Material sfrComp) {
			// Nothing to do... yet.
		}

		public void visit(org.eclipse.ice.reactor.sfr.core.assembly.Ring sfrComp) {
			// Nothing to do... yet.
		}
		// ----------------------------------------- //
	};

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		return;
	}

	public void dispose() {
		return;
	}

	/**
	 * Returns true if the element in the tree should have children, false
	 * otherwise. ICEResources and some IReactorComponents (plants, reactors,
	 * and assemblies) should have children.
	 */
	public boolean hasChildren(Object element) {

		boolean hasChildren = false;

		// Right now, only ICEResources and Composites have children.
		if (element instanceof Composite || element instanceof ICEResource) {
			hasChildren = true;
		}

		return hasChildren;
	}

	/**
	 * Returns the parent object so that this element's node can be expanded.
	 * For instance, an assembly's parent might be a reactor core.
	 */
	public Object getParent(Object element) {

		Object parent = null;

		if (element instanceof IReactorComponent) {
			parent = parentVisitor.getParent((IReactorComponent) element);
		}

		return parent;
	}

	/**
	 * Gets an array of elements to display for the given input. The input to
	 * this tree should be a list of {@link ICEResource}s.
	 */
	public Object[] getElements(Object inputElement) {

		// Local Declaration
		Object[] elements;

		// Convert the element to an ArrayList (it should be an
		// ArrayList<ICEResource>) and then get an Object array.
		if (inputElement instanceof ArrayList<?>) {
			elements = ((ArrayList<?>) inputElement).toArray();
		} else {
			elements = new Object[] {};
		}

		return elements;
	}

	/**
	 * Gets the child elements for the given object. ICEResources should have
	 * IReactorComponents, and some IReactorComponents will have other
	 * IReactorComponents as children (e.g., assemblies have pins).
	 */
	public Object[] getChildren(Object parentElement) {

		Object[] children;

		// If the parent is an ICEResource, we need to try to open the resource
		// and pull out any IReactorComponents.
		if (parentElement instanceof ICEResource) {
			ArrayList<IReactorComponent> childComponents = new ArrayList<>();

			// Get the the ICEResource
			ICEResource resource = (ICEResource) parentElement;

			// Create an IO factory to try to read in the file. Supported
			// objects in the file will be read into a list of objects.
			HdfIOFactory factory = new HdfIOFactory();
			List<Object> objects = factory.readObjects(resource.getPath());

			if (!objects.isEmpty()) {
				// Add all IReactorComponents read in from the file to the
				// tree viewer.
				for (Object object : objects) {
					if (object instanceof IReactorComponent) {
						childComponents.add((IReactorComponent) object);
					}
				}
			}
			// FIXME Temporary fall-back to the original method that uses direct
			// references to the LWR and SFR readers.
			else {
				// Read the first structure from the reactor file.
				ReactorReaderFactory reader = new ReactorReaderFactory();
				IReactorComponent parentRC = reader.readReactor(resource
						.getPath());

				// Add the parent component to the return structure
				childComponents.add(parentRC);
			}

			children = childComponents.toArray();
		}
		// If the parent is an IReactorComponent, we need to get all child
		// components by using an ISFRComponentVisitor or ILWRComponentVisitor.
		else if (parentElement instanceof IReactorComponent) {
			children = childrenVisitor
					.getChildren((IReactorComponent) parentElement);
		}
		// Ensure non-null result for something else.
		else {
			children = new Object[] {};
		}

		return children;
	}
}
