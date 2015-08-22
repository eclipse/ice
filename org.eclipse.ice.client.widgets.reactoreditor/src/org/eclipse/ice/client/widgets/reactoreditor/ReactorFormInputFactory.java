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
package org.eclipse.ice.client.widgets.reactoreditor;

import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;

/**
 * This factory is used to help construct {@link ReactorFormInput} for a
 * {@link ReactorFormEditor}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ReactorFormInputFactory {

	/**
	 * The default method for creating {@link ReactorFormInput}.
	 * 
	 * @param inputForm
	 *            The form associated with the Reactor Analyzer item.
	 * @param registry
	 *            The registry that provides the available {@link IAnalysisView}
	 *            s to the reactor editor.
	 * @return A new ReactorFormInput if the parameters are not null, null
	 *         otherwise.
	 */
	public ReactorFormInput createInput(Form inputForm,
			IAnalysisWidgetRegistry registry) {

		ReactorFormInput input = null;

		if (inputForm != null && registry != null) {
			input = new ReactorFormInput(inputForm, registry);
		}

		return input;
	}

	/**
	 * This method creates a {@link ReactorFormInput} based on previous input
	 * and a new {@link StateBroker}.
	 * 
	 * @param oldInput
	 *            The previous input for the Reactor Analyzer.
	 * @param broker
	 *            The new StateBroker that should be used to manage selections
	 *            in the reactor editor.
	 * @return A new ReactorFormInput if the parameters are not null, null
	 *         otherwise.
	 */
	public ReactorFormInput createInput(ReactorFormInput oldInput,
			StateBroker broker) {

		ReactorFormInput input = null;

		if (oldInput != null && broker != null) {
			// Create the basic ReactorFormInput.
			Form inputForm = oldInput.getForm();
			IAnalysisWidgetRegistry registry = oldInput.getRegistry();
			input = new ReactorFormInput(inputForm, registry, broker);
		}

		return input;
	}

	/**
	 * This method creates a {@link ReactorFormInput} based on previous input
	 * and a {@link ITreeSelection} that will be used to generate a new
	 * {@link StateBroker}.
	 * 
	 * @param oldInput
	 *            The previous input for the Reactor Analyzer.
	 * @param selection
	 *            A selection of {@link IReactorComponent}s that will be fed
	 *            into a new StateBroker.
	 * @param dataSource
	 *            the DataSource that the selection is being sent to, e.g.,
	 *            Input or Reference.
	 * @return A new ReactorFormInput if the parameters are not null, null
	 *         otherwise.
	 */
	public ReactorFormInput createInput(ReactorFormInput oldInput,
			ITreeSelection selection, DataSource dataSource) {
		ReactorFormInput input = null;

		if (oldInput != null && selection != null) {
			// Create the basic ReactorFormInput with a new StateBroker.
			StateBroker broker = new StateBroker();
			broker.copyValues(oldInput.getStateBroker());
			Form inputForm = oldInput.getForm();
			IAnalysisWidgetRegistry registry = oldInput.getRegistry();

			// Reset all keys associated with this data source.
			broker.resetSource(dataSource.toString());

			IReactorComponent rootComponent = null;

			// Create the form input.
			input = new ReactorFormInput(inputForm, registry, broker);

			// Update the StateBroker based on the ITreeSelection.
			if (!selection.isEmpty()) {
				TreePath path = selection.getPaths()[0];

				IAnalysisWidgetFactory factory;
				IStateBrokerHandler handler;

				// Loop over the segments in the TreePath. Each segment is a
				// node in the tree from the root to the selected object.
				int size = path.getSegmentCount();
				Object lastObject = null;
				for (int i = 0; i < size; i++) {
					Object object = path.getSegment(i);

					// Get the IAnalysisWidgetFactory for the object, then get
					// an IStateBrokerKeyProvider from the factory. Use the key
					// provider to get a key, then update the StateBroker with
					// the object.
					factory = registry.getAnalysisWidgetFactory(object
							.getClass());
					if (factory != null) {
						handler = factory.createStateBrokerHandler();
						handler.setDataSource(dataSource);
						if (handler.addValue(object, lastObject, broker)) {
							lastObject = object;

							// If possible, set the input reactor and its
							// resource for the input.
							if (rootComponent == null) {
								rootComponent = (IReactorComponent) object;
								ICEResource source = (ICEResource) path
										.getSegment(0);

								if (dataSource == DataSource.Input) {
									input.setInputReactorComponent(
											rootComponent, source);
								} else {
									input.setReferenceReactorComponent(
											rootComponent, source);
								}
							}
						}
					}
				}
			}
		}

		return input;
	}

}
