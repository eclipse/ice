/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton (UT-Battelle, LLC.) - Initial API and implementation and/or
 *     initial documentation
 *******************************************************************************/
package org.eclipse.eavp.viz.service.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class provides a binary search tree of doubles that can be used to find
 * the <i>nearest</i> value in the tree to a specified value in O(log(n)) time.
 * This functionality is totally unique when compared to {@link TreeSet
 * TreeSet<Double>}s, which can only tell if a value is <i>in</i> the tree but
 * cannot find the nearest value.
 * <p>
 * This is useful when dealing with user input that may not be exact, as it
 * effectively clamps the input value to the nearest value in the tree.
 * </p>
 * <p>
 * <b>Note:</b> This tree structure does not currently support inserting or
 * removing values. The tree's contents are strictly tied to a list of input
 * values supplied to the constructor.
 * </p>
 * 
 * @author Jordan
 *
 */
public class BinarySearchTree {

	/**
	 * The index of this node.
	 */
	private final int index;
	/**
	 * The value stored at this node.
	 */
	private final double value;

	/**
	 * The left child tree. All values stored in this sub-tree are less than
	 * this node's value. May be {@code null}.
	 */
	private final BinarySearchTree left;
	/**
	 * The left child tree. All values stored in this sub-tree are greater than
	 * this node's value. May be {@code null}.
	 */
	private final BinarySearchTree right;

	/**
	 * The list of values stored in this tree. This will be non-null <i>only for
	 * the root node</i>.
	 */
	private final List<Double> values;

	/**
	 * The default constructor. Creates a binary search tree from the specified
	 * list.
	 * 
	 * @param contents
	 *            The list of numbers from which to create a binary tree.
	 * @throws NullPointerException
	 *             If the specified list is {@code null}.
	 * @throws IllegalArgumentException
	 *             If the specified list contains zero non-null values.
	 */
	public BinarySearchTree(List<Double> contents) throws NullPointerException,
			IllegalArgumentException {
		// Check for a null list.
		if (contents == null) {
			throw new NullPointerException("BinarySearchTree error: "
					+ "Cannot create tree from null list.");
		}

		// Add all of the specified values to a sorted set. Remove all nulls.
		SortedSet<Double> sortedSet = new TreeSet<Double>();
		for (Double i : contents) {
			if (i != null) {
				sortedSet.add(i);
			}
		}

		// Check for an empty input list. The list must contain some non-null
		// value(s).
		if (sortedSet.isEmpty()) {
			throw new IllegalArgumentException("BinarySearchTree error: "
					+ "Cannot create tree from empty list.");
		}

		// Construct the backing list of values.
		values = new ArrayList<Double>(sortedSet);

		// Fill out this node's data.
		int size = values.size();
		index = size / 2;
		value = values.get(index);

		// Create any required sub-trees.
		if (size > 1) {
			// The left sub-tree should include all elements in the list
			// strictly before this node (exclusive).
			left = new BinarySearchTree(values.subList(0, index), 0);
			// The right sub-tree should include all elements in the list
			// strictly after this node (exclusive). This may be null.
			right = size > 2 ? new BinarySearchTree(values.subList(index + 1,
					size), index + 1) : null;
		} else {
			left = null;
			right = null;
		}

		return;
	}

	/**
	 * The private constructor used to create sub-trees.
	 * 
	 * @param contents
	 *            The list used to construct this sub-tree.
	 * @param indexOffset
	 *            The index of the first element in the "contents" list.
	 */
	private BinarySearchTree(List<Double> contents, int indexOffset) {
		// This node will not store the list of values.
		values = null;

		// Get the size of this sub-tree.
		int size = contents.size();

		// Fill out this node's data.
		int i = size / 2;
		index = indexOffset + i;
		value = contents.get(i);

		// Create any required sub-trees.
		if (size > 1) {
			// The left sub-tree should include all elements in the list
			// strictly before this node (exclusive).
			left = new BinarySearchTree(contents.subList(0, i), indexOffset);
			// The right sub-tree should include all elements in the list
			// strictly after this node (exclusive). This may be null.
			right = size > 2 ? new BinarySearchTree(contents.subList(i + 1,
					size), indexOffset + i + 1) : null;
		} else {
			left = null;
			right = null;
		}

		return;
	}

	/**
	 * Finds the index of the value in the tree closest to the specified value.
	 * 
	 * @param d
	 *            The value to search for in the tree.
	 * @return The index of the element in the tree closest to the value.
	 */
	public int findNearestIndex(double d) {
		return findNearestIndex(d, null, null);
	}

	/**
	 * A recursive operation used to find the index of the nearest value in the
	 * tree.
	 * 
	 * @param d
	 *            The value for which we are searching.
	 * @param greater
	 *            The last node traversed whose value is greater than the search
	 *            value.
	 * @param lesser
	 *            The last node traversed whose value is less than the search
	 *            value.
	 * @return The index of the nearest node. This will be the index this node
	 *         or one of its descendants.
	 */
	private int findNearestIndex(double d, BinarySearchTree greater,
			BinarySearchTree lesser) {

		int index = this.index;

		int comparison = Double.compare(d, value);
		if (comparison < 0) {
			if (left != null) {
				index = left.findNearestIndex(d, this, lesser);
			} else if (lesser != null
					&& Double.compare(Math.abs(value - d),
							Math.abs(lesser.value - d)) >= 0) {
				index = lesser.index;
			}
		} else if (comparison > 0) {
			if (right != null) {
				index = right.findNearestIndex(d, greater, this);
			} else if (greater != null
					&& Double.compare(Math.abs(value - d),
							Math.abs(greater.value - d)) > 0) {
				index = greater.index;
			}
		}

		return index;
	}

	/**
	 * Finds the value in the tree closest to the specified value.
	 * 
	 * @param d
	 *            The value to search for in the tree.
	 * @return The value in the tree closest to the specified value.
	 */
	public double findNearestValue(double d) {
		int index = findNearestIndex(d);
		return values.get(index);
	}

	/**
	 * Gets the item at the specified index. Note that the tree contains the
	 * times sorted in ascending order.
	 * 
	 * @param i
	 *            The index of the requested element in the tree.
	 * @return The stored value.
	 * @throws IndexOutOfBoundsException
	 *             If the specified index is invalid.
	 */
	public double get(int i) throws IndexOutOfBoundsException {
		return values.get(i);
	}

	/**
	 * Gets the size of the tree. This will always be at least 1.
	 * 
	 * @return The size of the tree.
	 */
	public int size() {
		return values.size();
	}
}
