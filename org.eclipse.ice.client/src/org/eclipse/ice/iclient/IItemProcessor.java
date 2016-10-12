/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.iclient;

import org.eclipse.ice.core.iCore.ICore;
import org.eclipse.ice.iclient.uiwidgets.IExtraInfoWidget;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.iclient.uiwidgets.IStreamingTextWidget;
import org.eclipse.ice.iclient.uiwidgets.IWidgetClosedListener;

/**
 * <p>
 * This class is responsible for processing a Form for a specific Item. It
 * implements the Runnable interface and should be run on a separate thread from
 * the UI. However, it is not thread safe and all of its required information
 * must be set before it is launched. Setting the values while the thread is
 * running will cause it to fail. This strategy is employed to keep clients from
 * updating the process request mid-stream, which could cause problems much
 * worse than a thread exception in the ICE core, because the ItemProcessor is
 * only meant to be launched once per process request. It realizes the
 * IWidgetClosedListener interface so that it can be notified by
 * IExtraInfoWidgets when they are closed. The ItemProcessor will attempt to
 * push streaming input if it is available for an Item to the
 * IStreamingTextWidget that is supplied during configuration. It will also set
 * the label of the widget.
 * </p>
 * <p>
 * All of the set operations, with the exception of setPollTime() and
 * setStreamingOutputWidget(), must be called before the processor can be
 * launched. There is a default polling time configured in the processor (100ms)
 * and if a streaming text widget is not set the ItemProcessor will not push the
 * output.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public interface IItemProcessor {

	/**
	 * This operation retrieves the IExtraInfoWidget that is used by the
	 * ItemProcessor. If it has not been previously set, this operation returns
	 * null.
	 * 
	 * @return The IExtraInfoWidget
	 */
	IExtraInfoWidget getInfoWidget();

	/**
	 * This operation sets the IExtraInfoWidget that is used by the
	 * ItemProcessor.
	 * 
	 * @param widget
	 *            The IExtraInfoWidget
	 */
	void setInfoWidget(IExtraInfoWidget widget);

	/**
	 * This operation sets the IFormWidget that is updated by the ItemProcessor.
	 * 
	 * @param widget
	 *            The IFormWidget
	 */
	void setFormWidget(IFormWidget widget);

	/**
	 * This operation retrieves the IFormWidget that is updated by the
	 * ItemProcessor. If it has not been previously set, this operation returns
	 * null.
	 * 
	 * @return The IFormWidget
	 */
	IFormWidget getFormWidget();

	/**
	 * This operation sets the IStreamingTextWidget that is updated by the
	 * ItemProcessor.
	 * 
	 * @param widget
	 *            The IStreamingTextWidget
	 */
	void setStreamingTextWidget(IStreamingTextWidget widget);

	/**
	 * This operation retrieves the IStreamingTextWidget that is updated by the
	 * ItemProcessor. If it has not been previously set, this operation returns
	 * null.
	 * 
	 * @return The IStreamingTextWidget
	 */
	IStreamingTextWidget getStreamingTextWidget();

	/**
	 * This operation sets the name of the action that should be performed when
	 * the Item is processed.
	 * 
	 * @param name
	 *            The action name
	 */
	void setActionName(String name);

	/**
	 * This operation retrieves the name of the action that the ItemProcessor
	 * will use to process the Item. If it has not been previously set, this
	 * operation returns null.
	 * 
	 * @return The name of the action
	 */
	String getActionName();

	/**
	 * This operation sets the id of the Item that the ItemProcessor will
	 * process.
	 * 
	 * @param id
	 */
	void setItemId(int id);

	/**
	 * This operation retrieves the id of the Item that the ItemProcessor will
	 * process. If it has not been previously set, this operation returns -1.
	 * 
	 * @return The Item's id
	 */
	int getItemId();

	/**
	 * This operation sets the ICore to which the ItemProcessor directs its
	 * requests.
	 * 
	 * @param core
	 *            The ICore
	 */
	void setCore(ICore core);

	/**
	 * This operation retrieves the ICore to which the ItemProcessor directs its
	 * requests or returns null if it was not previously set.
	 * 
	 * @return The ICore
	 */
	ICore getCore();

	/**
	 * This operation sets the poll time of the ItemProcessor. It checks to make
	 * sure that the submitted poll time is positive, greater than zero and less
	 * than 30,000 milliseconds (30 seconds). If the submitted time is invalid,
	 * this operation sets it to the default polling time.
	 * 
	 * @param milliseconds
	 *            The poll time in milliseconds. This value must be positive,
	 *            greater than zero and less than 30,000.
	 */
	void setPollTime(int milliseconds);

	/**
	 * This operation retrieves the current poll time. The units of the returned
	 * value are milliseconds.
	 * 
	 * @return The current polling time in milliseconds.
	 */
	int getPollTime();

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWidgetClosedListener#closedOK()
	 */
	void closedOK();

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWidgetClosedListener#cancelled()
	 */
	void cancelled();

	/**
	 * This operation processes the Item. Classes that implement this should, in
	 * general, call a separate thread.
	 */
	void launch();

}