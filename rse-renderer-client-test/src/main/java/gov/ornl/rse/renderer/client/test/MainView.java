/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package gov.ornl.rse.renderer.client.test;

import org.eclipse.ice.renderer.DataElement;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

	public MainView() {
		
		DataElement<String> nameElem = new DataElement<String>();
		nameElem.setName("Ross' name");
		nameElem.setDescription("Ross' name described by a data element");
		nameElem.setData("Ross Whitfield");
		
        VaadinRendererClient<String> client = new VaadinRendererClient<String>();
        client.setData(nameElem);

        add(client);
		
	}

}