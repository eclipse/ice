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
 * <!-- begin-UML-doc -->
 * <p>The IWidgetFactory interface defines the operations used to create widgets for implementations of IClient. It is implemented separately from the client to separate the code needed for drawing to the screen from the code needed to communicate with instances of ICore. An instance of this class must be set before implementations of IClient can be used.</p>
 * <!-- end-UML-doc -->
 * @author jaybilly
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IWidgetFactory {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns an IFormWidget that is rendered by the underlying graphics package.</p>
	 * <!-- end-UML-doc -->
	 * @param formName <p>The name of the Form that will be rendered with the widget. This option can be used by classes that realize the IWidgetFactory interface for further customization or special checks, but it is not required (and may be null).</p>
	 * @return <p>An IFormWidget</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IFormWidget getFormWidget(String formName);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns an IErrorBox that is rendered by the underlying graphics package.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The IErrorBox</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IErrorBox getErrorBox();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns an ITextEditor that is rendered by the underlying graphics package.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The ITextEditor</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ITextEditor getTextEditor();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns an IExtraInfoWidget.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The IExtraInfoWidget.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IExtraInfoWidget getExtraInfoWidget();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns a widget that can be used to stream text to a client.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The IStreamingTextWidget that can post messages to be viewed by a client.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IStreamingTextWidget getStreamingTextWidget();
}