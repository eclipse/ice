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
import org.eclipse.ice.iclient.uiwidgets.IStreamingTextWidget;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.iclient.uiwidgets.ITextEditor;
import org.eclipse.ice.iclient.uiwidgets.IWidgetFactory;

/** 
 * <!-- begin-UML-doc -->
 * <p>The FakeUIWidgetFactory is a fake subclass of the UIWidgetFactory that creates FakeUIWidgets. It is used for testing.</p>
 * <!-- end-UML-doc -->
 * @author Jay Jay Billings
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeUIWidgetFactory implements IWidgetFactory {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The last IFormWidget created by the Factory.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private FakeFormWidget lastFormWidget;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The last ITextEditor retrieved from the Factory.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private FakeTextEditor lastTextEditor;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>A state variable for holding the request state.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean widgetRetrieved;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The last IErrorBox retrieved from the Factory.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private FakeErrorBoxWidget lastErrorBoxWidget;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The last IExtraInfoWidget retrieved from the Factory.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private FakeExtraInfoWidget lastExtraInfoWidget;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The last IStreamingTextWidget retrieved from the Factory.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private FakeStreamingTextWidget lastStreamingTextWidget;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns true if a widget was retrieved from the FakeUIWidgetFactory.</p>
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean widgetRequested() {
		// begin-user-code

		// Local Declarations
		boolean retVal = widgetRetrieved;

		// Reset the state
		widgetRetrieved = false;

		return retVal;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the last IFormWidget requested from the factory.</p>
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FakeFormWidget getLastFormWidget() {
		// begin-user-code
		return lastFormWidget;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the last IErrorBox requested from the factory.</p>
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FakeErrorBoxWidget getLastErrorBoxWidget() {
		// begin-user-code
		// TODO Auto-generated method stub
		return lastErrorBoxWidget;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the last ITextEditor requested from the factory.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The ITextEditor</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FakeTextEditor getLastTextEditor() {
		// begin-user-code
		return lastTextEditor;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the FakeUIWidgetFactory to its initial, uncalled state.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void reset() {
		// begin-user-code

		// Reset everything
		lastFormWidget = null;
		lastErrorBoxWidget = null;
		lastTextEditor = null;
		lastExtraInfoWidget = null;
		widgetRetrieved = false;

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the last FakeExtraInfoWidget that was created.</p>
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FakeExtraInfoWidget getLastExtraInfoWidget() {
		// begin-user-code
		return lastExtraInfoWidget;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the last FakeStreamingTextWidget that was created.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The last IStreamingTextWidget</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FakeStreamingTextWidget getLastStreamingTextWidget() {
		// begin-user-code
		return lastStreamingTextWidget;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IWidgetFactory#getFormWidget(String formName)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public IFormWidget getFormWidget(String formName) {
		// begin-user-code

		// Set the state variable and last widget
		this.widgetRetrieved = true;
		lastFormWidget = new FakeFormWidget();

		return lastFormWidget;
		// end-user-code
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
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ITextEditor getTextEditor() {
		// begin-user-code

		// Set the state variable and last widget
		this.widgetRetrieved = true;
		lastTextEditor = new FakeTextEditor();

		return lastTextEditor;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IWidgetFactory#getExtraInfoWidget()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IExtraInfoWidget getExtraInfoWidget() {
		// begin-user-code

		// Set the state variable and last widget
		this.widgetRetrieved = true;
		lastExtraInfoWidget = new FakeExtraInfoWidget();

		return lastExtraInfoWidget;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IWidgetFactory#getStreamingTextWidget()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IStreamingTextWidget getStreamingTextWidget() {
		// begin-user-code

		// Set the state variable and last widget
		this.widgetRetrieved = true;
		lastStreamingTextWidget = new FakeStreamingTextWidget();

		return lastStreamingTextWidget;
		// end-user-code
	}
}