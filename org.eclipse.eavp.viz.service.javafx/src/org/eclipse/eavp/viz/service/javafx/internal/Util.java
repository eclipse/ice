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
package org.eclipse.eavp.viz.service.javafx.internal;

import org.eclipse.eavp.viz.service.javafx.scene.model.INode;
import org.eclipse.eavp.viz.service.modeling.Transformation;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

/**
 * <p>
 * Contains various static utility functions and constants used throughout that
 * can't be refactored into specific classes..
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class Util {

	/** Default material for normal states. */
	public static final Material DEFAULT_MATERIAL = new PhongMaterial(
			Color.BLUE);

	/** Default material for selected, highlighted, etc. states. */
	public static final Material DEFAULT_HIGHLIGHTED_MATERIAL = new PhongMaterial(
			Color.RED);

	/** Default material for errors or inconsistent states. */
	public static final Material DEFAULT_ERROR_MATERIAL = new PhongMaterial(
			Color.YELLOW);

	/** */
	public static final String SHAPE_PROP_KEY = "shape"; //$NON-NLS-1$

	/**
	 * <p>
	 * Converts an ICE Geometry Transformation data structure to a JavaFX
	 * Transform.
	 * </p>
	 * 
	 * @param transform
	 *            ICE Transformation data structure
	 * @return a JavaFX transformation that is analagous to the Transformation
	 */
	public static Transform convertTransformation(
			Transformation transformation) {
		Affine transform = new Affine();

		if (transformation == null) {
			return transform;
		}

		double size = transformation.getSize();
		double[] scale = transformation.getScale();
		double[] rotation = transformation.getRotation();
		double[] translation = transformation.getTranslation();

		Scale sizeXform = new Scale(size, size, size);
		Scale scaleXform = new Scale(scale[0], scale[1], scale[2]);
		Rotate rotateXform = eulerToRotate(rotation[0], rotation[1],
				rotation[2]);
		Translate translateXform = new Translate(translation[0], translation[1],
				translation[2]);
		Transform transformOutput = transform
				.createConcatenation(translateXform)
				.createConcatenation(rotateXform).createConcatenation(sizeXform)
				.createConcatenation(scaleXform);

		return transformOutput;
	}

	/**
	 * <p>
	 * Converts Euler angle representation to Axis Angle that can be used by
	 * JavaFX.
	 * </p>
	 * 
	 * @param radX
	 * @param radY
	 * @param radZ
	 * 
	 * @return Rotate object that represents the euler angle orientation
	 */
	public static Rotate eulerToRotate(double radX, double radY, double radZ) {
		double invSqr = 1.0d / 2.0d;

		double cosX = Math.cos(radX * invSqr);
		double sinX = Math.sin(radX * invSqr);

		double cosY = Math.cos(radY * invSqr);
		double sinY = Math.sin(radY * invSqr);

		double cosZ = Math.cos(radZ * invSqr);
		double sinZ = Math.sin(radZ * invSqr);

		double cosSqr = cosX * cosY;
		double sinSqr = sinX * sinY;

		double x = (sinX * cosY * cosZ) + (cosX * sinY * sinZ);
		double y = (cosX * sinY * cosZ) - (sinX * cosY * sinZ);
		double z = (cosSqr * sinZ) + (sinSqr * cosZ);

		double w = (cosSqr * cosZ) - (sinSqr * sinZ);
		double angle = Math.acos(w) * 2.0d;

		double dist = x * x + y * y + z * z;

		if (dist < 0.0001d) {
			x = 1.0d;
			y = 0.0d;
			z = 0.0d;
		} else {
			dist = 1.0d / Math.sqrt(dist);
			x *= dist;
			y *= dist;
			z *= dist;
		}

		return new Rotate(Math.toDegrees(angle), new Point3D(x, y, z));
	}

	/**
	 * <p>
	 * Returns the JavaFX node associated with the supplied ICE model node.
	 * </p>
	 * 
	 * @param node
	 *            ICE Mode
	 * 
	 * @return JavaFX node associated with the supplied ICE model node
	 */
	public static Group getFxGroup(INode node) {
		return (Group) node.getProperty(INode.RENDERER_NODE_PROP);
	}
}
