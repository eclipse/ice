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
package org.eclipse.ice.client.widgets.moose.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.SelectiveComponentVisitor;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.reactor.plant.Boundary;
import org.eclipse.ice.reactor.plant.Branch;
import org.eclipse.ice.reactor.plant.CoreChannel;
import org.eclipse.ice.reactor.plant.DownComer;
import org.eclipse.ice.reactor.plant.FlowJunction;
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
import org.eclipse.ice.reactor.plant.Pump;
import org.eclipse.ice.reactor.plant.Reactor;
import org.eclipse.ice.reactor.plant.SelectivePlantComponentVisitor;
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

/**
 * This class connects the MOOSE Component block (in the form of a
 * {@link TreeComposite} with a {@link PlantComposite}. Any changes made to the
 * block should be reflected in the {@code PlantComposite}.
 * 
 * @author Jordan Deyton
 *
 */
public class PlantBlockManager implements IUpdateableListener {

	/**
	 * The tree of Components. This comes from a root TreeComposite read in from
	 * MOOSE input files.
	 */
	private TreeComposite tree;

	/**
	 * The plant that is rendered in an editor. {@link PlantComponent}s are
	 * added to and removed from this plant based on the contents of the
	 * Components {@link #tree}.
	 */
	private final PlantComposite plant;

	/**
	 * This is a Map used to contain all of the PlantComponents added to the
	 * {@link #plant}. It is keyed on the unique TreeComposites that are
	 * children of the components {@link #tree}.
	 */
	private final IdentityHashMap<TreeComposite, PlantComponent> componentMap;

	/**
	 * An ID counter for adding to the PlantComposite.
	 */
	// TODO Link this to the PlantComposite.
	private int id = 0;

	/**
	 * A linker for associating Pipes with a DataComponent from the
	 * {@link #tree}.
	 */
	private final PipeLinker pipeLinker;
	/**
	 * A linker for associating HeatExchangers with a DataComponent from the
	 * {@link #tree}.
	 */
	private final HeatExchangerLinker heatExchangerLinker;
	/**
	 * A linker for associating Junctions with a DataComponent from the
	 * {@link #tree}.
	 */
	private final JunctionLinker junctionLinker;

	/**
	 * A factory used to instantiate new, default PlantComponents.
	 */
	private final PlantComponentFactory componentFactory;

	/**
	 * A list of all created EntryListeners. This list is only required when
	 * loading a new tree of PlantComponents.
	 */
	private final List<EntryListener> entryListeners;

	/**
	 * The default constructor.
	 */
	public PlantBlockManager() {
		// Initialize a default plant and tree.
		tree = new TreeComposite();
		plant = new MOOSEPlantComposite();

		// Initialize the Map of PlantComponents.
		componentMap = new IdentityHashMap<TreeComposite, PlantComponent>();

		// Initialize the PlantComponentLinkers.
		pipeLinker = new PipeLinker(this);
		heatExchangerLinker = new HeatExchangerLinker(this);
		junctionLinker = new JunctionLinker(this);

		// Initialize the PlantComponentFactory.
		componentFactory = new PlantComponentFactory();

		// Initialize the list of EntryListeners.
		entryListeners = new ArrayList<EntryListener>();

		return;
	}

	/**
	 * Sets the current Components {@link #tree}. TreeComposites corresponding
	 * to valid {@link #PlantComponent}s should be found under this tree.
	 * 
	 * @param tree
	 *            The new tree of components.
	 */
	public void setTree(TreeComposite tree) {
		// Check the parameter before proceeding.
		if (tree != null && tree != this.tree) {
			// Unregister from the old components tree.
			this.tree.unregister(this);

			// Remove all components from the plant and this class' meta data.
			for (PlantComponent plantComp : componentMap.values()) {
				if (plantComp != null) {
					plant.removeComponent(plantComp.getId());
				}
			}
			componentMap.clear();

			// Clear the list of EntryListeners.
			entryListeners.clear();

			// Reset the ID counter.
			id = 0;

			// Update the reference to the new components tree.
			this.tree = tree;
			// Update the plant and this class' meta data from the new tree.
			update(tree);
			// Register with the new components tree.
			tree.register(this);
		}

		return;
	}

	/**
	 * Gets the current {@link #plant} connected to the MOOSE Component block.
	 * 
	 * @return The {@link #plant}.
	 */
	public PlantComposite getPlant() {
		return plant;
	}

	/**
	 * Implements IUpdateableListener. This method is called when the Components
	 * {@link #tree} updates. It looks for new and removed components and adds
	 * or removes them from the {@link #plant} as necessary.
	 */
	public void update(IUpdateable component) {

		// Update the plant and meta data based on the current children of the
		// components tree.
		if (component == tree) {
			// Create a set containing all TreeComposites in the component map.
			// When a TreeComposite is found to still be a child of the
			// "Components" TreeComposite, it will be removed from this set. Any
			// TreeComposites remaining in the set afterward have been removed
			// from the "Components" TreeComposite and should be removed from
			// both the component map and the PlantComposite.
			Set<TreeComposite> removedComponents = Collections
					.newSetFromMap(new IdentityHashMap<TreeComposite, Boolean>(
							componentMap.size()));
			removedComponents.addAll(componentMap.keySet());

			// Loop over the existing children in the tree.
			for (int i = 0; i < tree.getNumberOfChildren(); i++) {
				TreeComposite child = tree.getChildAtIndex(i);

				// If the child TreeComposite does not already have a
				// corresponding PlantComponent in the component Map, try to
				// create a new one and add it to the PlantComposite.
				if (!componentMap.containsKey(child)) {
					// Create a plant component for the child.
					PlantComponent plantComp = createPlantComponent(child);
					if (plantComp != null) {
						plantComp.setId(id++);
						plantComp.setName(child.getName());
						plantComp.setDescription(child.getDescription());
						// Only add the child to the plant if it is active.
						if (child.isActive()) {
							plant.addPlantComponent(plantComp);
						}
					}
					componentMap.put(child, plantComp);
				} else {
					// The child is still a child of "Components", so remove it
					// from the set of components.
					removedComponents.remove(child);
				}
			}

			// Remove all PlantComponents for TreeComposites that remain in the
			// set of removed TreeComposites.
			for (TreeComposite child : removedComponents) {
				PlantComponent plantComp = componentMap.remove(child);
				plant.removeComponent(plantComp.getId());
			}

			// TODO This is overkill, but the difficulty lies in making sure
			// Junctions (or HeatExchangers) are hooked up to their Pipes (or
			// Junctions) properly. Until we can come up with a better way to
			// link Entries and the model, this loop will have to suffice.
			// Sync all of the Entries with the plant model.
			for (EntryListener listener : entryListeners) {
				listener.updateEntry();
			}
		} else if (componentMap.containsKey(component)) {
			// If a node's active flag has changed, add or remove the associated
			// PlantComponent to or from the plant.
			TreeComposite treeNode = (TreeComposite) component;
			PlantComponent plantComp = componentMap.get(component);
			if (treeNode.isActive()) {
				plant.addPlantComponent(plantComp);
			} else {
				plant.removeComponent(plantComp.getId());
			}
		}

		return;
	}

	/**
	 * Gets an initialized PlantComponent corresponding to a
	 * {@link TreeComposite} with the specified name in the components
	 * {@link #tree}.
	 * 
	 * @param name
	 *            The name of the component to find.
	 * @return A PlantComponent if one could be found matching the name, null
	 *         otherwise.
	 */
	protected PlantComponent getPlantComponent(String name) {
		// TODO This could be optimized by keeping track of TreeComposite names.

		// Set the default return value.
		PlantComponent component = null;

		// Loop over the currently known TreeComposites and find the first one
		// whose name matches the specified name.
		for (TreeComposite composite : componentMap.keySet()) {
			if (composite.getName().equals(name)) {
				// Get the PlantComponent for the matching TreeComposite.
				component = componentMap.get(composite);
				break;
			}
		}

		return component;
	}

	/**
	 * Creates a PlantComponent for a particular TreeComposite.
	 * 
	 * @param tree
	 *            The TreeComposite that should have a PlantComponent created.
	 * @return A PlantComponent based on the TreeComposite, or null if one
	 *         cannot be created.
	 */
	private PlantComponent createPlantComponent(TreeComposite tree) {

		// Set the default return value.
		PlantComponent plantComp = null;

		// If the DataComponent can be found, then we can try to create a
		// corresponding PlantComponent.
		DataComponent dataComp = getDataComponent(tree);
		if (dataComp != null) {
			// Try to create a PlantComponent from the DataComponent.
			plantComp = componentFactory.createComponent(dataComp);
			// Try to link the PlantComponent with the DataComponent's Entries.
			// We also need to update the list of EntryListeners.
			entryListeners.addAll(createEntryListeners(plantComp, dataComp));
		}

		return plantComp;
	}

	/**
	 * Creates {@link EntryListener}s for all supported {@link Entry} types
	 * based on the specified PlantComponent and its associated DataComponent
	 * 
	 * @param plantComp
	 *            The PlantComponent whose properties should be tied with the
	 *            Entries.
	 * @param dataComp
	 *            The DataComponent that contains the Entries.
	 * @return A list of EntryListeners successfully created and linked with the
	 *         PlantComponent.
	 */
	private List<EntryListener> createEntryListeners(PlantComponent plantComp,
			DataComponent dataComp) {

		// Create the default list of listeners to return.
		List<EntryListener> listeners = new ArrayList<EntryListener>(1);

		if (plantComp != null) {
			// Use a list wrapping a single linker so we can use a visitor to
			// determine which of the available linkers to use.
			final List<PlantComponentLinker> linker = new ArrayList<PlantComponentLinker>(
					1);

			// Create a use a visitor that determines the linker to use for the
			// plant component.
			IPlantComponentVisitor visitor = new SelectivePlantComponentVisitor() {
				// ---- Base classes ---- //
				public void visit(Pipe plantComp) {
					linker.add(pipeLinker);
				}

				public void visit(HeatExchanger plantComp) {
					linker.add(heatExchangerLinker);
				}

				public void visit(Junction plantComp) {
					linker.add(junctionLinker);
				}

				// ---- Pipe sub-classes ---- //
				public void visit(CoreChannel plantComp) {
					visit((Pipe) plantComp);
				}

				public void visit(Subchannel plantComp) {
					visit((Pipe) plantComp);
				}

				public void visit(PipeWithHeatStructure plantComp) {
					visit((Pipe) plantComp);
				}

				// ---- Junction sub-classes ---- //
				public void visit(Branch plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(SubchannelBranch plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(VolumeBranch plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(FlowJunction plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(WetWell plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(Boundary plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(OneInOneOutJunction plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(Turbine plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(IdealPump plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(Pump plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(Valve plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(PipeToPipeJunction plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(Inlet plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(MassFlowInlet plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(SpecifiedDensityAndVelocityInlet plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(Outlet plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(SolidWall plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(TDM plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(TimeDependentJunction plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(TimeDependentVolume plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(DownComer plantComp) {
					visit((Junction) plantComp);
				}

				public void visit(SeparatorDryer plantComp) {
					visit((Junction) plantComp);
				}
			};
			plantComp.accept(visitor);

			// If a linker was found for the plant component, get the
			// EntryListeners created to link the two components.
			if (!linker.isEmpty()) {
				listeners = linker.get(0).linkComponents(plantComp, dataComp);
			}
		}
		return listeners;
	}

	/**
	 * Searches the specified TreeComposite, which corresponds to a
	 * PlantComponent, and finds the first DataComponent. The DataComponent
	 * should contain Entries, some of which are linked to properties of the
	 * PlantComponent.
	 * 
	 * @param tree
	 *            The TreeComposite to search. This should be a child of the
	 *            Components {@link #tree}.
	 * @return The first DataComponent in the specified tree or null of none
	 *         exists.
	 */
	private DataComponent getDataComponent(TreeComposite tree) {

		// Set the default return value.
		DataComponent dataComp = null;

		// Check the argument. We cannot work on a null TreeComposite!
		if (tree != null) {
			// Found should be set to true when the first DataComponent is
			// found, and i should be index at which the loop exits.
			int i = 0;
			final AtomicBoolean found = new AtomicBoolean(false);

			// Get the list of data nodes (Components) from the TreeComposite.
			ArrayList<Component> components = tree.getDataNodes();
			// Create a visitor that sets found to true only for DataComponents.
			if (components != null) {
				IComponentVisitor visitor = new SelectiveComponentVisitor() {
					@Override
					public void visit(DataComponent component) {
						found.set(true);
					}
				};

				// Loop over the components and break when the first
				// DataComponent is found.
				int size = components.size();
				for (i = 0; !found.get() && i < size; i++) {
					Component c = components.get(i);
					if (c != null) {
						c.accept(visitor);
					}
				}
				// If the DataComponent could be found, set the reference to it.
				if (found.get()) {
					dataComp = (DataComponent) components.get(i - 1);
				}
			}
		}
		return dataComp;
	}

	/**
	 * This version of a {@link PlantComposite} stores special references to
	 * {@link Reactor}s and {@link CoreChannel}s. When a reactor is added, all
	 * core channels are added to it. When a core channel is added, it is added
	 * to all reactors.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class MOOSEPlantComposite extends PlantComposite {

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
	}
}
