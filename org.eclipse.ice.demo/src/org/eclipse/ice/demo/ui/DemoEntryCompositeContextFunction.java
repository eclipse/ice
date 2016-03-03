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

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.ice.client.widgets.providers.IEntryCompositeProvider;

/**
 * @author Jay Jay Billings
 *
 */
public class DemoEntryCompositeContextFunction extends ContextFunction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.core.contexts.IContextFunction#compute(org.eclipse.e4.core
	 * .contexts.IEclipseContext, java.lang.String)
	 */
	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		IEntryCompositeProvider provider = ContextInjectionFactory
				.make(DemoEntryCompositeProvider.class, context);
		// add the new object to the application context
		MApplication application = context.get(MApplication.class);
		IEclipseContext ctx = application.getContext();
		ctx.set(IEntryCompositeProvider.class, provider);
		return provider;
	}

}
