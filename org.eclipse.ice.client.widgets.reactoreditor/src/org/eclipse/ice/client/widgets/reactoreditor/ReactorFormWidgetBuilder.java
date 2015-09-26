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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.client.widgets.IFormWidgetBuilder;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides the declarative service that registers the Reactor Editor
 * Form Widget with ICE's service for dynamically extending the widget factory.
 * 
 * @author Jay Jay Billings
 * 
 */
public class ReactorFormWidgetBuilder implements IFormWidgetBuilder {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ReactorFormWidgetBuilder.class);

	/**
	 * The name of the items/forms used to create {@link ReactorFormEditor}s.
	 * This should be the same as the name in the Reactor Analyzer item class.
	 */
	public static final String name = "Reactor Analyzer";

	/**
	 * The current registry used to generate analysis widgets for customized
	 * analysis views.
	 */
	private IAnalysisWidgetRegistry widgetRegistry = null;

	/**
	 * The current registry of ReactorFormEditors.
	 */
	private IReactorEditorRegistry editorRegistry = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.IFormWidgetBuilder#getTargetFormName
	 * ()
	 */
	@Override
	public String getTargetFormName() {
		return name;
	}

	/**
	 * This operation builds the FormWidget.
	 * 
	 * @return The FormWidget for the Reactor Editor.
	 */
	@Override
	public IFormWidget build() {

		ReactorEclipseFormWidget widget = null;

		if (widgetRegistry == null || editorRegistry == null) {
			logger.info("ReactorFormWidgetBuilder error: "
					+ "No analysis widget registry or editor registry "
					+ "available for creating a ReactorEclipseFormWidget!");
		} else {
			widget = new ReactorEclipseFormWidget(widgetRegistry,
					editorRegistry);
		}

		return widget;
	}

	/**
	 * Sets the IAnalysisWidgetRegistry used by {@link AnalysisToolComposite}s
	 * in {@link ReactorPage}s.
	 * 
	 * @param registry
	 *            The widget registry.
	 */
	public void setAnalysisWidgetRegistry(IAnalysisWidgetRegistry registry) {
		logger.info("ReactorFormWidgetBuilder: "
				+ "Adding new IAnalysisWidgetRegistry.");
		if (registry != null) {
			this.widgetRegistry = registry;
		}
		return;
	}

	/**
	 * Sets the IReactorEditorRegistry to which {@link ReactorFormEditor}s
	 * should register when they are created. These editors are contacted from
	 * the ReactorViewer in the reactors perspective.
	 * 
	 * @param registry
	 *            The editor registry.
	 */
	public void setReactorEditorRegistry(IReactorEditorRegistry registry) {
		logger.info("ReactorFormWidgetBuilder: "
				+ "Adding new IReactorEditorRegistry.");
		if (registry != null) {
			this.editorRegistry = registry;
		}

		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(
						"org.eclipse.ice.client.widgets.reactoreditor.ireactoreditorregistry");
		System.out.println(
				"Available configuration elements(in org.eclipse.ice.client.widgets.reactoreditor.ReactorFormInputFactory.java):");
		for (IConfigurationElement element : elements) {
			System.out.println(
					element.getNamespaceIdentifier() + " " + element.getName());
		}

		return;
	}
}
