package org.eclipse.ice.datastructures.form.geometry;

/**
 * <p>
 * Represents the set operator applied to a ComplexShape
 * </p>
 * 
 * @author Jay Jay Billings
 */
public enum ICEOperatorType {
	/**
	 * <p>
	 * Default operator type
	 * </p>
	 * <p>
	 * When rendering, None should be taken to mean "invisible". A ComplexShape
	 * with this type should have no effect on its parent.
	 * </p>
	 * 
	 */
	None,
	/**
	 * <p>
	 * Union of any number of sets
	 * </p>
	 * <p>
	 * The result of a union may be disjoint, meaning that the child shapes may
	 * be separated with no points in common.
	 * </p>
	 * 
	 */
	Union,
	/**
	 * <p>
	 * Intersection of any number of sets
	 * </p>
	 * <p>
	 * The intersection of more than 2 sets is defined as
	 * </p>
	 * <p>
	 * ((A_1 intersection A_2) intersection A_3) ...
	 * </p>
	 * 
	 */
	Intersection,
	/**
	 * <p>
	 * Complement of any number of sets
	 * </p>
	 * <p>
	 * Unlike the union and intersection operators, the order of shapes is
	 * important when applying the multi-valued complement operator. For more
	 * than two shapes, the multi-valued complement is defined as
	 * </p>
	 * <p>
	 * A_1 / A_2 / A_3 / ..
	 * </p>
	 * <p>
	 * where "/" is the complement operator. The first shape has significance as
	 * the only additive shape in the final result of the operation.
	 * </p>
	 * 
	 */
	Complement
}