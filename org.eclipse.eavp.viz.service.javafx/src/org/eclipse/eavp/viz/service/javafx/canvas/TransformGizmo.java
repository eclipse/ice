/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com)
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.canvas;

import javafx.collections.ObservableList;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 *
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class TransformGizmo extends Group {

	/** */
	private Box handleX;
	private Box handleY;
	private Box handleZ;

	/** */
	private Box axisX;
	private Box axisY;
	private Box axisZ;

	/** */
	private float axisWidth = 2f;

	/** */
	private Node owner;

	/**
	 *
	 * @param axisSize
	 */
	public TransformGizmo(double axisSize) {
		super();

		if (axisSize > 0) {
			PhongMaterial axisXMaterial = new PhongMaterial();
			axisXMaterial.setDiffuseColor(Color.RED);

			PhongMaterial axisYMaterial = new PhongMaterial();
			axisYMaterial.setDiffuseColor(Color.GREEN);

			PhongMaterial axisZMaterial = new PhongMaterial();
			axisZMaterial.setDiffuseColor(Color.BLUE);

			PhongMaterial handleMaterial = new PhongMaterial();
			handleMaterial.setDiffuseColor(Color.BLUE);

			setDepthTest(DepthTest.DISABLE);

			handleX = new Box(15, 15, 15);
			handleX.setDepthTest(DepthTest.DISABLE);
			handleX.setMaterial(handleMaterial);
			handleX.setTranslateX(axisSize);
			handleX.setTranslateY(0);
			handleX.setTranslateZ(0);

			axisX = new Box(axisSize, axisWidth, axisWidth);
			axisX.setDepthTest(DepthTest.DISABLE);
			axisX.setMaterial(axisXMaterial);
			axisX.setTranslateX(axisSize / 2f);
			axisX.setTranslateY(0);
			axisX.setTranslateZ(0);

			handleY = new Box(15, 15, 15);
			handleY.setDepthTest(DepthTest.DISABLE);
			handleY.setMaterial(handleMaterial);
			handleY.setTranslateX(0);
			handleY.setTranslateY(-axisSize);
			handleY.setTranslateZ(0);

			axisY = new Box(axisWidth, axisSize, axisWidth);
			axisY.setDepthTest(DepthTest.DISABLE);
			axisY.setMaterial(axisYMaterial);
			axisY.setTranslateX(0);
			axisY.setTranslateY((axisSize / 2f));
			axisY.setTranslateZ(0);

			handleZ = new Box(15, 15, 15);
			handleZ.setDepthTest(DepthTest.DISABLE);
			handleZ.setMaterial(handleMaterial);
			handleZ.setTranslateX(0);
			handleZ.setTranslateY(0);
			handleZ.setTranslateZ(axisSize);

			axisZ = new Box(axisWidth, axisWidth, axisSize);
			axisZ.setDepthTest(DepthTest.DISABLE);
			axisZ.setMaterial(axisZMaterial);
			axisZ.setTranslateX(0);
			axisZ.setTranslateY(0);
			axisZ.setTranslateZ(-axisSize / 2f);

			ObservableList<Node> children = getChildren();

			children.add(handleX);
			children.add(handleY);
			children.add(handleZ);

			children.add(axisX);
			children.add(axisY);
			children.add(axisZ);
		}
	}

	/**
	 *
	 * @param handles
	 */
	public void showHandles(boolean handles) {

		// Set the handles' visibility, if they exist
		if (handleX != null) {
			handleX.setVisible(handles);
			handleY.setVisible(handles);
			handleZ.setVisible(handles);
		}
	}

	public Node getOwner() {
		return owner;
	}

	public void setOwner(Node owner) {
		this.owner = owner;
	}
}
