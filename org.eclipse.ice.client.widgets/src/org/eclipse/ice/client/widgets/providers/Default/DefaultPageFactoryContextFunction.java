package org.eclipse.ice.client.widgets.providers.Default;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.ice.client.widgets.providers.IPageFactory;

public class DefaultPageFactoryContextFunction extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		IPageFactory factory = ContextInjectionFactory
				.make(DefaultPageFactory.class, context);
		// add the new object to the application context
		MApplication application = context.get(MApplication.class);
		IEclipseContext ctx = application.getContext();
		ctx.set(IPageFactory.class, factory);
		return factory;
	}
}
