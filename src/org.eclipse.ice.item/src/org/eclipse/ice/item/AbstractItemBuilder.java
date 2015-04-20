/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.item;

import org.eclipse.core.resources.IProject;

/**
 * This is a conceptually abstract (but concretely implemented) realization of
 * the ItemBuiler interface that is designed to simply cut down on the amount of
 * code and knowledge needed to implement that interface. Implementations of
 * that interface are 90% redundant, so they are a good candidate for base
 * classes.
 * 
 * This class also provides built-in registration hooks for common services,
 * such as the IActionFactory, so that subclasses only have to point the OSGi to
 * their subclass. Registration of the services with the Item are handled
 * automatically in the build() operation.
 * 
 * Subclasses should override getInstance() and provide a new instance of their
 * Item. Subclasses that require new services should override setServices() and
 * call super.setServices() after they register their own services so that the
 * base class can perform its registrations.
 * 
 * @author Jay Jay Billings
 * 
 */
public class AbstractItemBuilder implements ItemBuilder {

	/**
	 * The name of the Item that will be built and the name of this builder.
	 */
	private String name;

	/**
	 * The type of the Item that will be built.
	 */
	private ItemType type;

	/**
	 * The IActionFactory that will provide Actions to the Items created by this
	 * Builder.
	 */
	private IActionFactory actionFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.ItemBuilder#getItemName()
	 */
	@Override
	public String getItemName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.ItemBuilder#getItemType()
	 */
	@Override
	public ItemType getItemType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.item.ItemBuilder#build(org.eclipse.core.resources.IProject
	 * )
	 */
	@Override
	public Item build(IProject projectSpace) {
		// TODO Auto-generated method stub

		// Create the Item and set its builder name
		Item item = getInstance(projectSpace);
		item.setItemBuilderName(this.getItemName());

		// Set the services
		setServices(item);

		// Tell the Item it can configure any data on its Form that requires the
		// services
		item.setupFormWithServices();

		return item;
	}

	/**
	 * This operation sets the reference to the IActionFactory service that
	 * should be used for creating Actions.
	 * 
	 * @param factory
	 *            The Action Factory
	 */
	public void setActionFactory(IActionFactory factory) {
		actionFactory = factory;
	}

	/**
	 * This operation registers all standard required services with the Item,
	 * including the IActionFactory and the IOServices.
	 * 
	 * @param item
	 *            The new Item with which the services should be registered.
	 */
	protected void setServices(Item item) {
		// Configure the Item to use the ActionFactory
		item.setActionFactory(actionFactory);
	}

	/**
	 * This operation takes the place of the build() operation for subclasses
	 * and acts as a factory method for new instances of the Item that is built,
	 * thereby allowing the AbstractItemBuilder to perform the other work
	 * without knowing the concrete type of the Item.
	 * 
	 * @param project
	 *            The Item's project space
	 * @return the newly constructed Item
	 */
	protected Item getInstance(IProject project) {
		return new Item(project);
	}

	/**
	 * This operation sets the name of the Item built by the builder.
	 * 
	 * @param name
	 *            The name
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * This operation sets the type of the Item built by the builder.
	 * 
	 * @param type
	 *            The ItemType
	 */
	protected void setType(ItemType type) {
		this.type = type;
	}

}
