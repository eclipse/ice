package org.eclipse.ice.datastructures.form.geometry;

import org.eclipse.ice.viz.service.geometry.ShapeType;

/**
 * <p>
 * Represents types of primitive solids
 * </p>
 * 
 * @author Jay Jay Billings
 */
public enum ICEShapeType {
	/**
	 * <p>
	 * Default shape type
	 * </p>
	 * <p>
	 * When rendering, None should be taken to mean "invisible". A
	 * PrimitiveShape with this type should have no effect on its parent.
	 * </p>
	 * 
	 */
	None {
		ShapeType asShapeType() {
			return ShapeType.None;
		}
	},
	/**
	 * <p>
	 * A "half-unit" sphere with a radius of 0.5 (diameter of 1) with its origin
	 * at its center
	 * </p>
	 * 
	 */
	Sphere{
		ShapeType asShapeType() {
			return ShapeType.Sphere;
		}
	},
	/**
	 * <p>
	 * A 1x1x1 cube with its origin at its center (0.5, 0.5, 0.5)
	 * </p>
	 * 
	 */
	Cube{
		ShapeType asShapeType() {
			return ShapeType.Cube;
		}
	},
	/**
	 * <p>
	 * A cylinder with a radius of 0.5 (diameter of 1), a height of 1, and its
	 * origin at its center (0.5, 0.5, 0.5)
	 * </p>
	 * 
	 */
	Cylinder{
		ShapeType asShapeType() {
			return ShapeType.Cylinder;
		}
	},
	/**
	 * <p>
	 * A circular cone with a diameter of 1, height of 1, and its center at
	 * (0.5, 0.5, 0.5)
	 * </p>
	 * 
	 */
	Cone{
		ShapeType asShapeType() {
			return ShapeType.Cone;
		}
	},
	/**
	 * <p>
	 * A cylinder with an inner and outer radius, an extruded annulus
	 * </p>
	 * 
	 */
	Tube{
		ShapeType asShapeType() {
			return ShapeType.Tube;
		}
	}
}