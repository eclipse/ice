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

import org.eclipse.ice.client.widgets.ICEFormInput;

import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.reactorAnalyzer.ReactorAnalyzer;

/**
 * This class provides input for {@link ReactorFormEditor}s. It requires both an
 * {@link IAnalysisWidgetRegistry} and a {@link StateBroker}. All
 * ReactorFormEditors share the same analysis widget registry, but they do not
 * necessarily share the same StateBroker. Two editors can be linked together by
 * making them share the same StateBroker.
 * 
 * @author djg
 * 
 */
public class ReactorFormInput extends ICEFormInput {

	/**
	 * The registry that provides the available {@link IAnalysisView}s to the
	 * reactor editor.
	 */
	private final IAnalysisWidgetRegistry registry;

	/**
	 * The StateBroker that manages selections in the reactor editor.
	 */
	private final StateBroker broker;

	/**
	 * The input reactor component.
	 */
	private IReactorComponent inputReactor;
	/**
	 * The source file for {@link #inputReactor}.
	 */
	private ICEResource inputSource;

	/**
	 * The reference reactor component.
	 */
	private IReactorComponent referenceReactor;
	/**
	 * The source file for {@link #referenceReactor}.
	 */
	private ICEResource referenceSource;

	/**
	 * The default constructor. This creates input for a
	 * {@link ReactorFormEditor} and creates a new StateBroker for the editor.
	 * 
	 * @param inputForm
	 *            The form associated with the Reactor Analyzer item.
	 * @param registry
	 *            The registry that provides the available {@link IAnalysisView}
	 *            s to the reactor editors.
	 */
	public ReactorFormInput(Form inputForm, IAnalysisWidgetRegistry registry) {
		this(inputForm, registry, new StateBroker());
	}

	/**
	 * This constructor provides the specified StateBroker for the
	 * {@link ReactorFormEditor}.
	 * 
	 * @param inputForm
	 *            The form associated with the Reactor Analyzer item.
	 * @param registry
	 *            The registry that provides the available {@link IAnalysisView}
	 *            s to the reactor editors.
	 * @param broker
	 *            The StateBroker that manages selections in the reactor editor.
	 */
	public ReactorFormInput(Form inputForm, IAnalysisWidgetRegistry registry,
			StateBroker broker) {
		super(inputForm);

		this.registry = registry;
		this.broker = broker;

		return;
	}

	/**
	 * Gets the registry that provides the available {@link IAnalysisView}s to
	 * the reactor editors.
	 * 
	 * @return The input's IAnalysisWidgetRegistry.
	 */
	public IAnalysisWidgetRegistry getRegistry() {
		return registry;
	}

	/**
	 * Gets the StateBroker that manages selections in the reactor editor.
	 * 
	 * @return The input's StateBroker.
	 */
	public StateBroker getStateBroker() {
		return broker;
	}

	/**
	 * Sets the input reactor component used by the {@link ReactorAnalyzer}.
	 * 
	 * @param input
	 *            The new input reactor component.
	 * @param source
	 *            The source file for the input reactor component.
	 */
	public void setInputReactorComponent(IReactorComponent input,
			ICEResource source) {

		if (input != null && source != null) {
			inputReactor = input;
			inputSource = source;
		}

		return;
	}

	/**
	 * Gets the input IReactorComponent. This will be used to update the form
	 * for the {@link ReactorFormEditor}.
	 * 
	 * @return The new input reactor component.
	 */
	public IReactorComponent getInputReactorComponent() {
		return inputReactor;
	}

	/**
	 * Gets the ICEResource for the file that contains the input
	 * IReactorComponent.
	 * 
	 * @return The source file for the input reactor component.
	 */
	public ICEResource getInputReactorComponentSource() {
		return inputSource;
	}

	/**
	 * Sets the reference reactor component used by the {@link ReactorAnalyzer}.
	 * This updates the underlying form!
	 * 
	 * @param reference
	 *            The new reference reactor component.
	 * @param source
	 *            The source file for the reference reactor component.
	 */
	public void setReferenceReactorComponent(IReactorComponent reference,
			ICEResource source) {

		if (reference != null && source != null) {
			referenceReactor = reference;
			referenceSource = source;
		}

		return;
	}

	/**
	 * Gets the reference IReactorComponent. This will be used to update the
	 * form for the {@link ReactorFormEditor}.
	 * 
	 * @return The new reference reactor component.
	 */
	public IReactorComponent getReferenceReactorComponent() {
		return referenceReactor;
	}

	/**
	 * Gets the ICEResource for the file that contains the reference
	 * IReactorComponent.
	 * 
	 * @return The source file for the reference reactor component.
	 */
	public ICEResource getReferenceReactorComponentSource() {
		return referenceSource;
	}

}
