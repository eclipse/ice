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

import org.eclipse.ice.renderer.Renderer;
import org.eclipse.ice.renderer.Person;
import org.eclipse.ice.renderer.PersonImplementation;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

	private Renderer<VaadinRendererClient<Person>, Person> renderer;

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
		Person ross = new PersonImplementation();
		try {
			ross.setFirstName("Ross");
			ross.setLastName("Whitfield");
			ross.setDescription("Ross' name described by a generated IDataElement");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ross.toJson());

		renderer = new Renderer<VaadinRendererClient<Person>, Person>();
		renderer.setViewer(new VaadinRendererClient<Person>());
		BiConsumer<VaadinRendererClient<Person>, Person> drawViewFunc = (v, w) -> {
			v.setData(w);
			add(v);
		};
		renderer.setDataElement(ross);
		renderer.setDrawMethod(drawViewFunc);

		renderer.render();
	}

}