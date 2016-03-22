/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.demo.ui;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.ice.client.widgets.providers.IEntryCompositeProvider;
import org.eclipse.ice.client.widgets.providers.IPageProvider;

/**
 * This class is an e4 Processor that publishes the demo providers to the e4
 * context.
 * 
 * This processor is invoked before the e4 context is provided to the
 * application
 * 
 * @author Jay Jay Billings
 *
 */
public class DemoWidgetsProcessor {

	/**
	 * This operation executes the instructions required to register the demo
	 * widgets with the e4 workbench.
	 * 
	 * @param context
	 *            The e4 context
	 * @param app
	 *            the model application
	 */
	@Execute
	public void execute(IEclipseContext context, MApplication app) {

		// Add the geometry provider
		IPageProvider provider = ContextInjectionFactory
				.make(DemoGeometryPageProvider.class, context);
		context.set("demo-geometry", provider);

		// Add the EntryComposite provider
		IEntryCompositeProvider compProvider = ContextInjectionFactory
				.make(DemoEntryCompositeProvider.class, context);
		context.set("demo-entry", compProvider);

	}

}
