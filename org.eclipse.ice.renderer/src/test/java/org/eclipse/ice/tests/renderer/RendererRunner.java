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
package org.eclipse.ice.tests.renderer;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import javax.inject.Inject;

import org.eclipse.ice.renderer.DataElement;
import org.eclipse.ice.renderer.Renderer;

//import gov.ornl.rse.renderer.client.test.Offset;

/**
 * @author Jay Jay Billings
 *
 */
public class RendererRunner {

	@Inject
	DataElement<Boolean> data;

	@Inject
	DataElement<String> dataString;

	//@Inject
	//DataElement<Offset> sampleOffset;

	@Inject
	Renderer<HTMLView, Boolean> renderer;

	@Inject
	Renderer<HTMLView, String> stringRenderer;

	//@Inject
	//Renderer<HTMLView, Offset> osRenderer;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public void run() throws Exception {

		// Configure a basic data element with a boolean value
		data.setName("Injected.");
		data.setData(true);
		System.out.println("Data = " + data);

		// Create a handler to tell the renderer how to draw to the screen
		BiConsumer<HTMLView, DataElement<Boolean>> drawViewFunc = (v, w) -> v.draw(w);
		BiConsumer<HTMLView, DataElement<String>> stringViewFunc = (v, w) -> v.draw(w);
		//BiConsumer<HTMLView, DataElement<Offset>> offsetViewFunc = (v, w) -> v.draw(w);

		// Setup some test values and test some stuff
		testSomeStuff();

		// Setup the renderer
		renderer.setDataElement(data);
		renderer.setDrawMethod(drawViewFunc);

		// Draw the render
		renderer.render();

		// Configure another basic data element with a string value that is the same as
		// the last
		dataString.setData("true");
		stringRenderer.setDataElement(dataString);
		stringRenderer.setDrawMethod(stringViewFunc);
		stringRenderer.render();

		// Configure one with Pojo data
		//osRenderer.setDrawMethod(offsetViewFunc);
		//osRenderer.setDataElement(sampleOffset);
		//osRenderer.render();
	}

	private void testSomeStuff() throws Exception {

		// Decorated property - description
		//sampleOffset.setDescription("Sample Offset in mm");
		// Data property - use
		//sampleOffset.setData(new Offset());
//				System.out.println("Offset");
		//sampleOffset.getData().setUsed(true);
//				System.out.println(sampleOffset);

		//String offset = sampleOffset.toString();
		//sampleOffset.getData().setValue(5.0);
		//sampleOffset.getData().setUsed(false);
		//System.out.println(sampleOffset);

		System.out.println("----- From String -----");
		//sampleOffset.fromString(offset);
		System.out.println("----- -----");
		//System.out.println(sampleOffset.toString());

		// Read in and configure defaults
		DataElement<Double> scaleFactor = new DataElement<Double>();
		scaleFactor.setData(1.0);
		scaleFactor.setDescription("Absolute Scale Factor");
		scaleFactor.setName(scaleFactor.getDescription());
		DataElement<Double> apertureSize = new DataElement<Double>();
		apertureSize.setData(1.0);
		DataElement<String> binType = new DataElement<String>();
		binType.setData("1");
		DataElement<Integer> OneDNumberOfBins = new DataElement<Integer>();
		OneDNumberOfBins.setData(1);
		DataElement<Integer> TwoDNumberOfBins = new DataElement<Integer>();
		TwoDNumberOfBins.setData(1);

		// Load data model
		ArrayList<DataElement> dataModel = new ArrayList<DataElement>();
		dataModel.add(scaleFactor);
		dataModel.add(apertureSize);
		dataModel.add(binType);
		dataModel.add(OneDNumberOfBins);
		dataModel.add(TwoDNumberOfBins);

//				System.out.println(dataModel);

//				System.out.println(dataModel.get(4));

		DataElement<Integer> gpSANSTwoDNumberOfBins = new DataElement<Integer>();
		gpSANSTwoDNumberOfBins.setData(8);
		dataModel.set(4, gpSANSTwoDNumberOfBins);

//				System.out.println(dataModel);

//				dataRenderer.render(model);
	}

}
