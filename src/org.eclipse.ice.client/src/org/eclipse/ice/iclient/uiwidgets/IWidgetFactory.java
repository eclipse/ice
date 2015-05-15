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
package org.eclipse.ice.iclient.uiwidgets;

/** 
 * <p>The IWidgetFactory interface defines the operations used to create widgets for implementations of IClient. It is implemented separately from the client to separate the code needed for drawing to the screen from the code needed to communicate with instances of ICore. An instance of this class must be set before implementations of IClient can be used.</p>
 * @author Jay Jay Billings
 */
public interface IWidgetFactory {
	/** 
	 * <p>This operation returns an IFormWidget that is rendered by the underlying graphics package.</p>
	 * @param formName <p>The name of the Form that will be rendered with the widget. This option can be used by classes that realize the IWidgetFactory interface for further customization or special checks, but it is not required (and may be null).</p>
	 * @return <p>An IFormWidget</p>
	 */
	public IFormWidget getFormWidget(String formName);

	/** 
	 * <p>This operation returns an IErrorBox that is rendered by the underlying graphics package.</p>
	 * @return <p>The IErrorBox</p>
	 */
	public IErrorBox getErrorBox();

	/** 
	 * <p>This operation returns an ITextEditor that is rendered by the underlying graphics package.</p>
	 * @return <p>The ITextEditor</p>
	 */
	public ITextEditor getTextEditor();

	/** 
	 * <p>This operation returns an IExtraInfoWidget.</p>
	 * @return <p>The IExtraInfoWidget.</p>
	 */
	public IExtraInfoWidget getExtraInfoWidget();

	/** 
	 * <p>This operation returns a widget that can be used to stream text to a client.</p>
	 * @return <p>The IStreamingTextWidget that can post messages to be viewed by a client.</p>
	 */
	public IStreamingTextWidget getStreamingTextWidget();
}