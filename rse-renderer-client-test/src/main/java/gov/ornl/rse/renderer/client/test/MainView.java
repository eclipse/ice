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

import java.util.function.BiConsumer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.ice.renderer.DataElement;
import org.eclipse.ice.renderer.Renderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

	@Autowired
	private Renderer<VaadinRendererClient<String>, String> renderer;

	/**
	 * Constructor
	 */
	public MainView() {
	}

	/**
	 * This operation draws the main view. It must be executed after construction
	 * because it depends on dependencies that area injected into the class.
	 */
	@PostConstruct
	public void render() {

		// Nothing to do here - just sample setup
		DataElement<String> nameElem = new DataElement<String>();
		try {
			nameElem.setName("Ross' name");
			nameElem.setDescription("Ross' name described by a data element");
			nameElem.setData("Ross Whitfield");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BiConsumer<VaadinRendererClient<String>, DataElement<String>> drawViewFunc = (v, w) -> {
			v.setData(w);
			add(v);
		};
		renderer.setDataElement(nameElem);
		renderer.setDrawMethod(drawViewFunc);

		renderer.render();
	}

}