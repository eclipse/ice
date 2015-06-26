/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.client.widgets.EclipseErrorBoxWidget;
import org.eclipse.ice.client.widgets.EclipseExtraInfoWidget;
import org.eclipse.ice.client.widgets.EclipseFormWidget;
import org.eclipse.ice.client.widgets.EclipseStreamingTextWidget;
import org.eclipse.ice.client.widgets.EclipseTextEditor;
import org.eclipse.ice.client.widgets.EclipseUIWidgetFactory;
import org.eclipse.ice.client.widgets.IFormWidgetBuilder;
import org.eclipse.ice.iclient.uiwidgets.IErrorBox;
import org.eclipse.ice.iclient.uiwidgets.IExtraInfoWidget;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.iclient.uiwidgets.IStreamingTextWidget;
import org.eclipse.ice.iclient.uiwidgets.ITextEditor;
import org.junit.Test;

/**
 * <p>
 * This class is responsible for testing the EclipseUIWidgetFactory. It only
 * checks the instance type of the widgets and makes sure that they are not
 * null.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class EclipseUIWidgetFactoryTester {

	/**
	 * This is a utility class for testing the declarative services ability of
	 * the EclipseUIWidgetFactory.
	 * 
	 * @author Jay Jay Billings
	 * 
	 */
	private class FakeFormWidgetBuilder implements IFormWidgetBuilder {

		/** Boolean to mark whether or not the widget was built. **/
		private boolean built = false;

		/**
		 * The name of the target Form
		 */
		private String name = "Fake Item";

		@Override
		public String getTargetFormName() {
			return name;
		}

		@Override
		public IFormWidget build() {
			// Flag that the widget was built
			built = true;
			return new EclipseFormWidget();
		}

		public boolean wasBuilt() {
			return built;
		}

		public void setName(String formName) {
			name = formName;
		}
	}

	/**
	 * 
	 */
	private EclipseUIWidgetFactory eclipseUIWidgetFactory;

	/**
	 * <p>
	 * This operation checks the Widget types and makes sure that they are not
	 * null.
	 * </p>
	 * 
	 */
	@Test
	public void checkWidgetTypes() {

		// Setup the widget factory
		eclipseUIWidgetFactory = new EclipseUIWidgetFactory();

		// Check the Error Box Widget
		IErrorBox errorBox = eclipseUIWidgetFactory.getErrorBox();
		assertNotNull(errorBox);
		assertTrue(errorBox instanceof EclipseErrorBoxWidget);

		// Check the Extra Info Widget
		IExtraInfoWidget extraInfo = eclipseUIWidgetFactory
				.getExtraInfoWidget();
		assertNotNull(extraInfo);
		assertTrue(extraInfo instanceof EclipseExtraInfoWidget);

		// Check the Form Widget
		IFormWidget formWidget = eclipseUIWidgetFactory.getFormWidget(null);
		assertNotNull(formWidget);
		assertTrue(formWidget instanceof EclipseFormWidget);

		// Check the Text Editor
		ITextEditor editor = eclipseUIWidgetFactory.getTextEditor();
		assertNotNull(editor);
		assertTrue(editor instanceof EclipseTextEditor);

		// Check the Streaming Text
		IStreamingTextWidget textWidget = eclipseUIWidgetFactory
				.getStreamingTextWidget();
		assertNotNull(textWidget);
		assertTrue(textWidget instanceof EclipseStreamingTextWidget);

		// Register two IFormWidgetBuilders with the factory
		FakeFormWidgetBuilder builder = new FakeFormWidgetBuilder();
		FakeFormWidgetBuilder secondBuilder = new FakeFormWidgetBuilder();
		secondBuilder.setName("Second Item");
		eclipseUIWidgetFactory.registerFormWidgetBuilder(builder);
		eclipseUIWidgetFactory.registerFormWidgetBuilder(secondBuilder);
		// Get a widget from the factory and make sure that builder was used.
		eclipseUIWidgetFactory.getFormWidget("Fake Item");
		assertTrue(builder.wasBuilt());
		// Get another widget and check the builder
		eclipseUIWidgetFactory.getFormWidget("Second Item");
		assertTrue(secondBuilder.wasBuilt());

		return;

	}
}