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
package org.eclipse.ice.viz.service.geometry.shapes;

import static com.jme3.util.BufferUtils.createShortBuffer;
import static com.jme3.util.BufferUtils.createVector2Buffer;
import static com.jme3.util.BufferUtils.createVector3Buffer;

import java.io.IOException;
import java.nio.FloatBuffer;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;

/**
 * Computes the mesh of a tube to produce a valid JME3 Spatial
 * 
 * Class created by InShadow and revised by kotoko on the JMonkeyEngine forums 
 * http://jmonkeyengine.org/groups/contribution-depot-jme3/forum/topic/tube-from-jme2/
 *
 */
public class Tube extends Mesh {
	private int axisSamples;
	private int radialSamples;

	private float outerRadius;
	private float innerRadius;
	private float height;
	private float centralAngle;

	/**
	 * Default constructor for serialization only. Do not use.
	 */
	public Tube() {
	}

	/**
	 * Creates a new, fully drawn (whole circle) MyTube, with 2 axis samples and
	 * 20 radial samples. This shape is similar to disc, but it has hole in the
	 * middle. Its hole is by default on Y axis.
	 * 
	 * @param outerRadius
	 *            outer edge, where solid part ends.
	 * @param innerRadius
	 *            inner radius, where the hole in the middle ends. If it is 0,
	 *            this shape looks identical to disc (note that it has more
	 *            vertexes and triangles!).
	 * @param height
	 *            height of tube.
	 */
	public Tube(float outerRadius, float innerRadius, float height) {
		this(outerRadius, innerRadius, height, 2, 20, FastMath.TWO_PI);
	}

	/**
	 * Creates a new MyTube, with 2 axis samples and some proper amount of
	 * radial samples (based on central angle). This shape is similar to disc,
	 * but it has hole in the middle. Its hole is by default on Y axis.
	 * 
	 * @param outerRadius
	 *            outer edge, where solid part ends.
	 * @param innerRadius
	 *            inner radius, where the hole in the middle ends. If it is 0,
	 *            this shape looks identical to disc (note that it has more
	 *            vertexes and triangles!).
	 * @param height
	 *            height of tube.
	 * @param centralAngle
	 *            amount of tube to be drawn (FastMath.TWO_PI is for whole
	 *            circle, everything less draws just a part of it).
	 */
	public Tube(float outerRadius, float innerRadius, float height,
			float centralAngle) {
		this(outerRadius, innerRadius, height, 2, (int) (20 * FastMath
				.ceil(centralAngle * FastMath.INV_TWO_PI)), centralAngle);
	}

	/**
	 * Creates a new MyTube, which is fully drawn (whole circle). This shape is
	 * similar to disc, but it has hole in the middle. Its hole is by default on
	 * Y axis.
	 * 
	 * @param outerRadius
	 *            outer edge, where solid part ends.
	 * @param innerRadius
	 *            inner radius, where the hole in the middle ends. If it is 0,
	 *            this shape looks identical to disc (note that it has more
	 *            vertexes and triangles!).
	 * @param height
	 *            height of tube.
	 * @param axisSamples
	 *            number of triangle samples along the axis.
	 * @param radialSamples
	 *            number of triangle samples along the radial.
	 */
	public Tube(float outerRadius, float innerRadius, float height,
			int axisSamples, int radialSamples) {
		this(outerRadius, innerRadius, height, axisSamples, radialSamples,
				FastMath.TWO_PI);
	}

	/**
	 * Creates a new MyTube. This shape is similar to disc, but it has hole in
	 * the middle. Its hole is by default on Y axis.
	 * 
	 * @param outerRadius
	 *            outer edge, where solid part ends.
	 * @param innerRadius
	 *            inner radius, where the hole in the middle ends. If it is 0,
	 *            this shape looks identical to disc (note that it has more
	 *            vertexes and triangles!).
	 * @param height
	 *            height of tube.
	 * @param axisSamples
	 *            number of triangle samples along the axis.
	 * @param radialSamples
	 *            number of triangle samples along the radial.
	 * @param centralAngle
	 *            amount of tube to be drawn (FastMath.TWO_PI is for whole
	 *            circle, everything less draws just a part of it).
	 */
	public Tube(float outerRadius, float innerRadius, float height,
			int axisSamples, int radialSamples, float centralAngle) {
		super();
		updateGeometry(outerRadius, innerRadius, height, axisSamples,
				radialSamples, centralAngle);
	}

	/**
	 * @return central angle, which defines amount of tube drawn.
	 */
	public float getCentralAngle() {
		return centralAngle;
	}

	/**
	 * @return number of samples along tube's axis.
	 */
	public int getAxisSamples() {
		return axisSamples;
	}

	/**
	 * @return number of samples along the radial.
	 */
	public int getRadialSamples() {
		return radialSamples;
	}

	/**
	 * @return height of tube.
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @return inner radius, where hole ends and solid part begins.
	 */
	public float getInnerRadius() {
		return innerRadius;
	}

	/**
	 * @return outer radius, where solid part ends.
	 */
	public float getOuterRadius() {
		return outerRadius;
	}

	/**
	 * Updates this tube mesh with new parameters.
	 * 
	 * @param outerRadius
	 *            outer edge, where solid part ends.
	 * @param innerRadius
	 *            inner radius, where the hole in the middle ends. If it is 0,
	 *            this shape looks identical to disc (note that it has more
	 *            vertexes and triangles!).
	 * @param height
	 *            height of tube.
	 * @param axisSamples
	 *            number of triangle samples along the axis.
	 * @param radialSamples
	 *            number of triangle samples along the radial.
	 * @param centralAngle
	 *            amount of tube to be drawn (FastMath.TWO_PI is for whole
	 *            circle, everything less draws just a part of it).
	 */
	public void updateGeometry(float outerRadius, float innerRadius,
			float height, int axisSamples, int radialSamples, float centralAngle) {
		this.outerRadius = outerRadius;
		this.innerRadius = innerRadius;
		this.height = height;
		this.axisSamples = axisSamples;
		this.radialSamples = radialSamples;
		this.centralAngle = FastMath.normalize(centralAngle, -FastMath.TWO_PI,
				FastMath.TWO_PI);

		int vertCount = 2 * (axisSamples + 1) * (radialSamples + 1) + 4
				* (radialSamples + 1) + 4 * (axisSamples + 1);
		setBuffer(Type.Position, 3,
				createVector3Buffer(getFloatBuffer(Type.Position), vertCount));

		setBuffer(
				Type.Normal,
				3,
				createVector3Buffer(getFloatBuffer(Type.Normal),
						getVertexCount()));
		setBuffer(Type.TexCoord, 2, createVector2Buffer(getVertexCount()));
		int triangleCount = 4 * (radialSamples + 1) * (axisSamples + 1);
		setBuffer(
				Type.Index,
				3,
				createShortBuffer(getShortBuffer(Type.Index), 3 * triangleCount));

		setGeometryData();
		setIndexData();

		updateBound();
		updateCounts();
	}

	private void setGeometryData() {
		float inverseRadial = 1.0f / radialSamples;
		float axisStep = height / axisSamples;
		float axisTextureStep = 1.0f / axisSamples;
		float halfHeight = 0.5f * height;
		float innerOuterRatio = innerRadius / outerRadius;
		float[] sin = new float[radialSamples + 1];
		float[] cos = new float[radialSamples + 1];

		for (int radialCount = 0; radialCount <= radialSamples; radialCount++) {
			float angle = centralAngle * inverseRadial * radialCount;
			cos[radialCount] = FastMath.cos(angle);
			sin[radialCount] = FastMath.sin(angle);
		}

		FloatBuffer pb = getFloatBuffer(Type.Position);
		FloatBuffer nb = getFloatBuffer(Type.Normal);
		FloatBuffer tb = getFloatBuffer(Type.TexCoord);

		// outer cylinder
		for (int radialCount = 0; radialCount <= radialSamples; radialCount++) {
			for (int axisCount = 0; axisCount <= axisSamples; axisCount++) {
				pb.put(cos[radialCount] * outerRadius)
						.put(axisStep * axisCount - halfHeight)
						.put(sin[radialCount] * outerRadius);
				nb.put(cos[radialCount]).put(0).put(sin[radialCount]);
				tb.put((radialSamples - radialCount) * inverseRadial).put(
						axisTextureStep * axisCount);
			}
		}
		// inner cylinder
		for (int radialCount = 0; radialCount <= radialSamples; radialCount++) {
			for (int axisCount = 0; axisCount <= axisSamples; axisCount++) {
				pb.put(cos[radialCount] * innerRadius)
						.put(axisStep * axisCount - halfHeight)
						.put(sin[radialCount] * innerRadius);
				nb.put(-cos[radialCount]).put(0).put(-sin[radialCount]);
				tb.put(radialCount * inverseRadial).put(
						axisTextureStep * axisCount);
			}
		}
		// bottom edge
		for (int radialCount = 0; radialCount <= radialSamples; radialCount++) {
			pb.put(cos[radialCount] * outerRadius).put(-halfHeight)
					.put(sin[radialCount] * outerRadius);
			pb.put(cos[radialCount] * innerRadius).put(-halfHeight)
					.put(sin[radialCount] * innerRadius);
			nb.put(0).put(-1).put(0);
			nb.put(0).put(-1).put(0);
			tb.put(0.5f + 0.5f * cos[radialCount]).put(
					0.5f + 0.5f * sin[radialCount]);
			tb.put(0.5f + innerOuterRatio * 0.5f * cos[radialCount]).put(
					0.5f + innerOuterRatio * 0.5f * sin[radialCount]);
		}
		// top edge
		for (int radialCount = 0; radialCount <= radialSamples; radialCount++) {
			pb.put(cos[radialCount] * outerRadius).put(halfHeight)
					.put(sin[radialCount] * outerRadius);
			pb.put(cos[radialCount] * innerRadius).put(halfHeight)
					.put(sin[radialCount] * innerRadius);
			nb.put(0).put(1).put(0);
			nb.put(0).put(1).put(0);
			tb.put(0.5f + 0.5f * cos[radialCount]).put(
					0.5f + 0.5f * sin[radialCount]);
			tb.put(0.5f + innerOuterRatio * 0.5f * cos[radialCount]).put(
					0.5f + innerOuterRatio * 0.5f * sin[radialCount]);
		}
		// vertical edges
		for (int radialCount = 0; radialCount <= radialSamples; radialCount += radialSamples) {
			for (int axisCount = 0; axisCount <= axisSamples; axisCount++) {
				pb.put(cos[radialCount] * outerRadius)
						.put(axisStep * axisCount - halfHeight)
						.put(sin[radialCount] * outerRadius);
				pb.put(cos[radialCount] * innerRadius)
						.put(axisStep * axisCount - halfHeight)
						.put(sin[radialCount] * innerRadius);
				nb.put(cos[radialCount]).put(0).put(sin[radialCount]);
				nb.put(-cos[radialCount]).put(0).put(-sin[radialCount]);
				tb.put(radialCount * inverseRadial).put(
						axisTextureStep * axisCount);
				tb.put((radialSamples - radialCount) * inverseRadial).put(
						axisTextureStep * axisCount);
			}
		}

	}

	private void setIndexData() {
		int axisSamplesPlusOne = axisSamples + 1;
		int innerCylinder = axisSamplesPlusOne * (radialSamples + 1);
		int bottomEdge = 2 * innerCylinder;
		int topEdge = bottomEdge + 2 * (radialSamples + 1);
		int verEdge1 = topEdge + 2 * (radialSamples + 1);
		int verEdge2 = verEdge1 + 2 * axisSamplesPlusOne;

		IndexBuffer ib = getIndexBuffer();
		int index = 0;

		// outer cylinder
		for (int radialCount = 0; radialCount < radialSamples; radialCount++) {
			for (int axisCount = 0; axisCount < axisSamples; axisCount++) {
				int index0 = axisCount + axisSamplesPlusOne * radialCount;
				int index1 = index0 + 1;
				int index2 = index0 + axisSamplesPlusOne;
				int index3 = index2 + 1;
				ib.put(index++, index0);
				ib.put(index++, index1);
				ib.put(index++, index2);
				ib.put(index++, index1);
				ib.put(index++, index3);
				ib.put(index++, index2);
			}
		}

		// inner cylinder
		for (int radialCount = 0; radialCount < radialSamples; radialCount++) {
			for (int axisCount = 0; axisCount < axisSamples; axisCount++) {
				int index0 = innerCylinder + axisCount + axisSamplesPlusOne
						* radialCount;
				int index1 = index0 + 1;
				int index2 = index0 + axisSamplesPlusOne;
				int index3 = index2 + 1;
				ib.put(index++, index0);
				ib.put(index++, index2);
				ib.put(index++, index1);
				ib.put(index++, index1);
				ib.put(index++, index2);
				ib.put(index++, index3);
			}
		}

		// bottom edge
		for (int radialCount = 0; radialCount < radialSamples; radialCount++) {
			int index0 = bottomEdge + 2 * radialCount;
			int index1 = index0 + 1;
			int index2 = index1 + 1;
			int index3 = index2 + 1;
			ib.put(index++, index0);
			ib.put(index++, index2);
			ib.put(index++, index1);
			ib.put(index++, index1);
			ib.put(index++, index2);
			ib.put(index++, index3);
		}

		// top edge
		for (int radialCount = 0; radialCount < radialSamples; radialCount++) {
			int index0 = topEdge + 2 * radialCount;
			int index1 = index0 + 1;
			int index2 = index1 + 1;
			int index3 = index2 + 1;
			ib.put(index++, index0);
			ib.put(index++, index1);
			ib.put(index++, index2);
			ib.put(index++, index1);
			ib.put(index++, index3);
			ib.put(index++, index2);
		}

		// vertical edge0
		for (int axisCount = 0; axisCount < axisSamples; axisCount++) {
			int index0 = verEdge1 + 2 * axisCount;
			int index1 = index0 + 1;
			int index2 = index1 + 1;
			int index3 = index2 + 1;
			ib.put(index++, index0);
			ib.put(index++, index2);
			ib.put(index++, index1);
			ib.put(index++, index1);
			ib.put(index++, index2);
			ib.put(index++, index3);
		}

		// vertical edge1
		for (int axisCount = 0; axisCount < axisSamples; axisCount++) {
			int index0 = verEdge2 + 2 * axisCount;
			int index1 = index0 + 1;
			int index2 = index1 + 1;
			int index3 = index2 + 1;
			ib.put(index++, index0);
			ib.put(index++, index2);
			ib.put(index++, index1);
			ib.put(index++, index1);
			ib.put(index++, index2);
			ib.put(index++, index3);
		}
	}

	@Override
	public void read(JmeImporter e) throws IOException {
		super.read(e);
		InputCapsule capsule = e.getCapsule(this);
		axisSamples = capsule.readInt("axisSamples", 0);
		radialSamples = capsule.readInt("radialSamples", 0);
		outerRadius = capsule.readFloat("outerRadius", 0);
		innerRadius = capsule.readFloat("innerRadius", 0);
		height = capsule.readFloat("height", 0);
		centralAngle = capsule.readFloat("centralAngle", FastMath.TWO_PI);
	}

	@Override
	public void write(JmeExporter e) throws IOException {
		super.write(e);
		OutputCapsule capsule = e.getCapsule(this);
		capsule.write(getAxisSamples(), "axisSamples", 0);
		capsule.write(getRadialSamples(), "radialSamples", 0);
		capsule.write(getOuterRadius(), "outerRadius", 0);
		capsule.write(getInnerRadius(), "innerRadius", 0);
		capsule.write(getHeight(), "height", 0);
		capsule.write(getCentralAngle(), "centralAngle", FastMath.TWO_PI);
	}
}