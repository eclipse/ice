/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides the default implementation of IPageFactory for the
 * default widget configuration in Eclipse ICE.
 * 
 * @author Jay Jay Billings
 *
 */
public class DefaultPageFactory implements IPageFactory {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(DefaultPageFactory.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.providers.IPageFactory#
	 * getResourceComponentPages(org.eclipse.ui.forms.editor.FormEditor,
	 * java.util.ArrayList)
	 */
	@Override
	public ArrayList<IFormPage> getResourceComponentPages(FormEditor editor,
			ArrayList<Component> components) {
		
		ArrayList<IFormPage> pages = new ArrayList<IFormPage>();

		// Create resource page using IResourcePageProvider
		try {
			// get all of the registered ResourcePageProviders
			ArrayList<IResourcePageProvider> resourcePageProviders = IResourcePageProvider
					.getProviders();
			if (resourcePageProviders != null
					&& resourcePageProviders.size() > 0) {

				// Use the default resource page provider
				String providerNameToUse = DefaultResourcePageProvider.PROVIDER_NAME;

				for (IResourcePageProvider currentProvider : resourcePageProviders) {
					if (providerNameToUse.equals(currentProvider.getName())) {
						pages.addAll(currentProvider.getPages(editor,
								components));
						break;
					}
				}
			} else {
				logger.error("No ResourcePageProvider registered");
			}

		} catch (CoreException e) {
			logger.error("Unable to get ResourcePageProvider", e);
		}

		return pages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.providers.IPageFactory#
	 * getListComponentPages(org.eclipse.ui.forms.editor.FormEditor,
	 * java.util.ArrayList)
	 */
	@Override
	public ArrayList<IFormPage> getListComponentPages(FormEditor editor,
			ArrayList<Component> components) {
		// Create the list of pages to return
		ArrayList<IFormPage> pages = new ArrayList<IFormPage>();

		try {
			// Get all of the registered ListPageProviders
			ArrayList<IListPageProvider> listPageProviders = IListPageProvider
					.getProviders();
			if (listPageProviders != null && listPageProviders.size() > 0) {
				// Use the default list page provider for now.
				String providerNameToUse = DefaultListPageProvider.PROVIDER_NAME;
				// Do a linear search over providers and pull the correct one.
				for (IListPageProvider currentProvider : listPageProviders) {
					if (providerNameToUse.equals(currentProvider.getName())) {
						pages.addAll(
								currentProvider.getPages(editor, components));
						break;
					}
				}
			} else {
				logger.error("No ListPageProviders registered");
			}
		} catch (CoreException e) {
			logger.error("Unable to get ListPageProviders", e);
		}

		return pages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.providers.IPageFactory#getErrorPage()
	 */
	@Override
	public IFormPage getErrorPage(FormEditor editor) {
		// Array for storing the pages
		ArrayList<IFormPage> pages = new ArrayList<IFormPage>();

		try {
			// get all of the registered ListPageProviders
			ArrayList<IErrorPageProvider> errorPageProviders = IErrorPageProvider
					.getProviders();
			if (errorPageProviders != null && errorPageProviders.size() > 0) {
				// Use the default error page provider
				String providerNameToUse = DefaultErrorPageProvider.PROVIDER_NAME;
				// Do a linear search to find the correct provider
				for (IErrorPageProvider currentProvider : errorPageProviders) {
					if (providerNameToUse.equals(currentProvider.getName())) {
						pages = currentProvider.getPages(editor, null);
						break;
					}
				}
			} else {
				logger.error("No ErrorPageProviders registered");
			}
		} catch (CoreException e) {
			logger.error("Unable to get ErrorPageProviders", e);
		}

		// There should only be one page since this is an error page, so just
		// get the first element.
		return pages.get(0);
	}
	

	@Override
	public ArrayList<IFormPage> getComponentPages(FormEditor editor,
			ArrayList<Component> components) {
		// Array for storing the pages
		ArrayList<IFormPage> pages = new ArrayList<IFormPage>();

		try {
			// get all of the registered basic component page providers
			ArrayList<IBasicComponentPageProvider> basicComponentProviders = 
					IBasicComponentPageProvider.getProviders();
			if (basicComponentProviders != null && basicComponentProviders.size() > 0) {
				// Use the default basic component page provider
				String providerNameToUse = DefaultBasicComponentPageProvider.PROVIDER_NAME;
				// Do a linear search to find the correct provider
				for (IBasicComponentPageProvider currentProvider : basicComponentProviders) {
					if (providerNameToUse.equals(currentProvider.getName())) {
						pages = currentProvider.getPages(editor, components);
						break;
					}
				}
			} else {
				logger.error("No BasicComponentPageProviders registered");
			}
		} catch (CoreException e) {
			logger.error("Unable to get BasicComponentPageProviders", e);
		}

		return pages;
	}
	
	@Override
	public ArrayList<IFormPage> getGeometryComponentPages(FormEditor editor,
			ArrayList<Component> components) {
		ArrayList<IFormPage> pages = new ArrayList<IFormPage>();
		try{
			ArrayList<IGeometryPageProvider> geometryComponentProviders = 
					IGeometryPageProvider.getProviders();
			if (geometryComponentProviders != null && geometryComponentProviders.size() > 0) {
				// Use the default error page provider
				String providerNameToUse = DefaultErrorPageProvider.PROVIDER_NAME;
				// Do a linear search to find the correct provider
				for (IGeometryPageProvider currentProvider : geometryComponentProviders) {
					if (providerNameToUse.equals(currentProvider.getName())) {
						pages = currentProvider.getPages(editor, null);
						break;
					}
				}
			}else{
				logger.error("No GeometryComponentProviders registered");
			}
			
		} catch (CoreException e){
			logger.error("Unable to get GeometryComponentPageProviders", e);
		}
		
		return pages;
	}

}
