/*******************************************************************************
* Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.test;

import org.eclipse.ice.iclient.uiwidgets.IErrorBox;
import org.eclipse.ice.iclient.uiwidgets.IExtraInfoWidget;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.iclient.uiwidgets.IStreamingTextWidget;
import org.eclipse.ice.iclient.uiwidgets.ITextEditor;
import org.eclipse.ice.iclient.uiwidgets.IWidgetFactory;

/** 
 * <p>The FakeUIWidgetFactory is a fake subclass of the UIWidgetFactory that creates FakeUIWidgets. It is used for testing.</p>
 * @author Jay Jay Billings
 */
public class FakeUIWidgetFactory implements IWidgetFactory {
	/** 
	 * <p>The last IFormWidget created by the Factory.</p>
	 */
	private FakeFormWidget lastFormWidget;

	/** 
	 * <p>The last ITextEditor retrieved from the Factory.</p>
	 */
	private FakeTextEditor lastTextEditor;

	/** 
	 * <p>A state variable for holding the request state.</p>
	 */
	private boolean widgetRetrieved;

	/** 
	 * <p>The last IErrorBox retrieved from the Factory.</p>
	 */
	private FakeErrorBoxWidget lastErrorBoxWidget;

	/** 
	 * <p>The last IExtraInfoWidget retrieved from the Factory.</p>
	 */
	private FakeExtraInfoWidget lastExtraInfoWidget;

	/** 
	 * <p>The last IStreamingTextWidget retrieved from the Factory.</p>
	 */
	private FakeStreamingTextWidget lastStreamingTextWidget;

	/** 
	 * <p>This operation returns true if a widget was retrieved from the FakeUIWidgetFactory.</p>
	 * @return
	 */
	public boolean widgetRequested() {

		// Local Declarations
		boolean retVal = widgetRetrieved;

		// Reset the state
		widgetRetrieved = false;

		return retVal;
	}

	/** 
	 * <p>This operation returns the last IFormWidget requested from the factory.</p>
	 * @return
	 */
	public FakeFormWidget getLastFormWidget() {
		return lastFormWidget;
	}

	/** 
	 * <p>This operation returns the last IErrorBox requested from the factory.</p>
	 * @return
	 */
	public FakeErrorBoxWidget getLastErrorBoxWidget() {
		// TODO Auto-generated method stub
		return lastErrorBoxWidget;
	}

	/** 
	 * <p>This operation returns the last ITextEditor requested from the factory.</p>
	 * @return <p>The ITextEditor</p>
	 */
	public FakeTextEditor getLastTextEditor() {
		return lastTextEditor;
	}

	/** 
	 * <p>This operation returns the FakeUIWidgetFactory to its initial, uncalled state.</p>
	 */
	public void reset() {

		// Reset everything
		lastFormWidget = null;
		lastErrorBoxWidget = null;
		lastTextEditor = null;
		lastExtraInfoWidget = null;
		widgetRetrieved = false;

	}

	/** 
	 * <p>This operation returns the last FakeExtraInfoWidget that was created.</p>
	 * @return
	 */
	public FakeExtraInfoWidget getLastExtraInfoWidget() {
		return lastExtraInfoWidget;
	}

	/** 
	 * <p>This operation returns the last FakeStreamingTextWidget that was created.</p>
	 * @return <p>The last IStreamingTextWidget</p>
	 */
	public FakeStreamingTextWidget getLastStreamingTextWidget() {
		return lastStreamingTextWidget;
	}

	/** 
	 * (non-Javadoc)
	 * @see IWidgetFactory#getFormWidget(String formName)
	 */
	@Override
	public IFormWidget getFormWidget(String formName) {

		// Set the state variable and last widget
		this.widgetRetrieved = true;
		lastFormWidget = new FakeFormWidget();

		return lastFormWidget;
	}

	@Override
	public IErrorBox getErrorBox() {

		// Set the state variable and last widget
		this.widgetRetrieved = true;
		lastErrorBoxWidget = new FakeErrorBoxWidget();

		return lastErrorBoxWidget;
	}

	/** 
	 * (non-Javadoc)
	 * @see IWidgetFactory#getTextEditor()
	 */
	@Override
	public ITextEditor getTextEditor() {

		// Set the state variable and last widget
		this.widgetRetrieved = true;
		lastTextEditor = new FakeTextEditor();

		return lastTextEditor;
	}

	/** 
	 * (non-Javadoc)
	 * @see IWidgetFactory#getExtraInfoWidget()
	 */
	@Override
	public IExtraInfoWidget getExtraInfoWidget() {

		// Set the state variable and last widget
		this.widgetRetrieved = true;
		lastExtraInfoWidget = new FakeExtraInfoWidget();

		return lastExtraInfoWidget;
	}

	/** 
	 * (non-Javadoc)
	 * @see IWidgetFactory#getStreamingTextWidget()
	 */
	@Override
	public IStreamingTextWidget getStreamingTextWidget() {

		// Set the state variable and last widget
		this.widgetRetrieved = true;
		lastStreamingTextWidget = new FakeStreamingTextWidget();

		return lastStreamingTextWidget;
	}
}