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
package org.eclipse.eavp.viz.service.widgets.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.widgets.BinarySearchTree;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class checks the API provided by the {@link BinarySearchTree} for
 * correctness by comparing it with a complex, pre-constructed tree.
 * 
 * @author Jordan
 *
 */
public class BinarySearchTreeTester {

	/**
	 * The list of values used to construct the {@link #tree}. Includes nulls
	 * and repeated values, and is not ordered in ascending order.
	 */
	private static List<Double> inputValues;
	/**
	 * The expected list of values in the tree's underlying array. This includes
	 * the values 0 to 70 in increments of 10.
	 */
	private static List<Double> values;
	/**
	 * The binary search tree to test.
	 */
	private static BinarySearchTree tree;
	/**
	 * The epsilon used to compare doubles in the test.
	 */
	private static double epsilon;

	/**
	 * Set up the shared static variables.
	 */
	@BeforeClass
	public static void beforeAllTests() {

		// Set up the tree's input. Add all required values in reverse order.
		inputValues = new ArrayList<Double>();
		for (double i = 70.0; i >= 0.0; i -= 10.0) {
			inputValues.add(i);
		}
		// Add some null values.
		inputValues.add(2, null);
		inputValues.add(5, null);
		// Add some repeated values.
		inputValues.add(0, 10.0);
		inputValues.add(60.0);

		// Set up a list containing the expected ordering of the values.
		values = new ArrayList<Double>();
		for (double i = 0.0; i <= 70.0; i += 10.0) {
			values.add(i);
		}

		// Construct the tree.
		tree = new BinarySearchTree(inputValues);

		// Set the double comparison value.
		epsilon = 1e-7;

		return;
	}

	/**
	 * Eliminates the shared static variables after all tests have completed.
	 */
	@AfterClass
	public static void afterAllTests() {
		inputValues = null;
		values.clear();
		values = null;
		tree = null;
	}

	/**
	 * Checks that a search tree cannot be constructed from a null list or a
	 * list with no non-null values.
	 */
	@Test
	public void checkConstructorExceptions() {
		List<Double> invalidContents;

		// We cannot create a tree from a null list.
		invalidContents = null;
		try {
			new BinarySearchTree(invalidContents);
			fail("BinarySearchTree error: "
					+ "Did not throw exception for null input list.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		// We cannot create a tree from an empty list (null values are removed).
		invalidContents = new ArrayList<Double>();
		invalidContents.add(null);
		invalidContents.add(null);
		try {
			new BinarySearchTree(invalidContents);
			fail("BinarySearchTree error: "
					+ "Did not throw exception for input list with no non-null values.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}

		return;
	}

	/**
	 * Checks search operations on a single-node tree.
	 */
	@Test
	public void checkSingleNodeTree() {

		List<Double> contents = new ArrayList<Double>();
		contents.add(1.0);
		BinarySearchTree smallTree = new BinarySearchTree(contents);

		assertEquals(1, smallTree.size());
		assertEquals(1.0, smallTree.get(0), epsilon);
		assertEquals(0, smallTree.findNearestIndex(0.0));
		assertEquals(0, smallTree.findNearestIndex(1.0));
		assertEquals(0, smallTree.findNearestIndex(2.0));

		return;
	}

	/**
	 * Checks search operations on a small, two-node tree.
	 */
	@Test
	public void checkTwoNodeTree() {

		List<Double> contents = new ArrayList<Double>();
		contents.add(0.0);
		contents.add(1.0);
		BinarySearchTree smallTree = new BinarySearchTree(contents);

		assertEquals(2, smallTree.size());
		assertEquals(0.0, smallTree.get(0), epsilon);
		assertEquals(1.0, smallTree.get(1), epsilon);

		assertEquals(0, smallTree.findNearestIndex(-1.0));
		assertEquals(0, smallTree.findNearestIndex(0.0));
		assertEquals(0, smallTree.findNearestIndex(0.5));
		assertEquals(1, smallTree.findNearestIndex(0.5 + epsilon));
		assertEquals(1, smallTree.findNearestIndex(1.0));
		assertEquals(1, smallTree.findNearestIndex(2.0));

		return;
	}

	/**
	 * Checks search operations on a small, evenly balanced, three-node tree.
	 */
	@Test
	public void checkThreeNodeTree() {

		List<Double> contents = new ArrayList<Double>();
		contents.add(0.0);
		contents.add(1.0);
		contents.add(2.0);
		BinarySearchTree smallTree = new BinarySearchTree(contents);

		assertEquals(3, smallTree.size());
		assertEquals(0.0, smallTree.get(0), epsilon);
		assertEquals(1.0, smallTree.get(1), epsilon);
		assertEquals(2.0, smallTree.get(2), epsilon);

		assertEquals(0, smallTree.findNearestIndex(-1.0));
		assertEquals(0, smallTree.findNearestIndex(0.0));
		assertEquals(0, smallTree.findNearestIndex(0.5));
		assertEquals(1, smallTree.findNearestIndex(0.5 + epsilon));
		assertEquals(1, smallTree.findNearestIndex(1.0));
		assertEquals(1, smallTree.findNearestIndex(1.5));
		assertEquals(2, smallTree.findNearestIndex(1.5 + epsilon));
		assertEquals(2, smallTree.findNearestIndex(2.0));
		assertEquals(2, smallTree.findNearestIndex(2.5));

		return;
	}

	/**
	 * Checks that modifying the input list does not affect the structure of the
	 * tree.
	 */
	@Test
	public void checkInputIsSafe() {
		inputValues.clear();
		assertEquals(values.size(), tree.size());
	}

	/**
	 * Checks the expected size of the tree.
	 */
	@Test
	public void checkSize() {
		assertEquals(values.size(), tree.size());
	}

	/**
	 * Checks that the index-based getter returns the correct values (the tree
	 * should be backed by an ordered list).
	 */
	@Test
	public void checkGetValue() {
		// The list maintained by the tree should be sorted.
		for (int i = 0; i < values.size(); i++) {
			assertEquals(values.get(i), tree.get(i), epsilon);
		}
	}

	/**
	 * Checks that trying to get a value with an invalid index throws
	 * IndexOutOfBoundsExceptions.
	 */
	@Test
	public void checkGetValueIndexExceptions() {

		// Try an index that is too low.
		try {
			tree.get(-1);
			fail("BinarySearchTree error: "
					+ "IndexOutOfBoundsException not thrown for invalid index -1");
		} catch (IndexOutOfBoundsException e) {
			// Exception thrown as expected.
		}

		// Try an index that is too high.
		try {
			tree.get(values.size());
			fail("BinarySearchTree error: "
					+ "IndexOutOfBoundsException not thrown for invalid index "
					+ values.size());
		} catch (IndexOutOfBoundsException e) {
			// Exception thrown as expected.
		}

		return;
	}

	/**
	 * Checks that the smallest element (index 0) in the tree can be found with
	 * the appropriate search values (anything below 0.0 to 5.0).
	 */
	@Test
	public void checkFindZerothElement() {
		int expectedIndex = 0;
		assertEquals(expectedIndex, tree.findNearestIndex(-10.0));
		assertEquals(expectedIndex, tree.findNearestIndex(-1.0));
		assertEquals(expectedIndex, tree.findNearestIndex(0.0));
		assertEquals(expectedIndex, tree.findNearestIndex(1.0));
		assertEquals(expectedIndex, tree.findNearestIndex(5.0));
		double expectedValue = 0.0;
		assertEquals(expectedValue, tree.findNearestValue(-10.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(-1.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(0.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(1.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(5.0), epsilon);

		return;
	}

	/**
	 * Checks that the element at index 1 in the tree can be found with the
	 * appropriate search values (anything between 5.0--exclusive--to
	 * 15.0--inclusive).
	 */
	@Test
	public void checkFindFirstElement() {
		int expectedIndex = 1;
		assertEquals(expectedIndex, tree.findNearestIndex(6.0));
		assertEquals(expectedIndex, tree.findNearestIndex(9.0));
		assertEquals(expectedIndex, tree.findNearestIndex(10.0));
		assertEquals(expectedIndex, tree.findNearestIndex(11.0));
		assertEquals(expectedIndex, tree.findNearestIndex(15.0));
		double expectedValue = 10.0;
		assertEquals(expectedValue, tree.findNearestValue(6.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(9.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(10.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(11.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(15.0), epsilon);

		// Try values very close to the edge of the previous index.
		assertEquals(expectedIndex, tree.findNearestIndex(5.0 + epsilon));

		return;
	}

	/**
	 * Checks that the element at index 2 in the tree can be found with the
	 * appropriate search values (anything between 15.0--exclusive--to
	 * 25.0--inclusive).
	 */
	@Test
	public void checkFindSecondElement() {
		int expectedIndex = 2;
		assertEquals(expectedIndex, tree.findNearestIndex(16.0));
		assertEquals(expectedIndex, tree.findNearestIndex(19.0));
		assertEquals(expectedIndex, tree.findNearestIndex(20.0));
		assertEquals(expectedIndex, tree.findNearestIndex(21.0));
		assertEquals(expectedIndex, tree.findNearestIndex(25.0));
		double expectedValue = 20.0;
		assertEquals(expectedValue, tree.findNearestValue(16.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(19.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(20.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(21.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(25.0), epsilon);

		// Try values very close to the edge of the previous index.
		assertEquals(expectedIndex, tree.findNearestIndex(15.0 + epsilon));

		return;
	}

	/**
	 * Checks that the element at index 3 in the tree can be found with the
	 * appropriate search values (anything between 25.0--exclusive--to
	 * 35.0--inclusive).
	 */
	@Test
	public void checkFindThirdElement() {
		int expectedIndex = 3;
		assertEquals(expectedIndex, tree.findNearestIndex(26.0));
		assertEquals(expectedIndex, tree.findNearestIndex(29.0));
		assertEquals(expectedIndex, tree.findNearestIndex(30.0));
		assertEquals(expectedIndex, tree.findNearestIndex(31.0));
		assertEquals(expectedIndex, tree.findNearestIndex(35.0));
		double expectedValue = 30.0;
		assertEquals(expectedValue, tree.findNearestValue(26.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(29.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(30.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(31.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(35.0), epsilon);

		// Try values very close to the edge of the previous index.
		assertEquals(expectedIndex, tree.findNearestIndex(25.0 + epsilon));

		return;
	}

	/**
	 * Checks that the element at index 4 in the tree can be found with the
	 * appropriate search values (anything between 35.0--exclusive--to
	 * 45.0--inclusive).
	 */
	@Test
	public void checkFindFourthElement() {
		int expectedIndex = 4;
		assertEquals(expectedIndex, tree.findNearestIndex(36.0));
		assertEquals(expectedIndex, tree.findNearestIndex(39.0));
		assertEquals(expectedIndex, tree.findNearestIndex(40.0));
		assertEquals(expectedIndex, tree.findNearestIndex(41.0));
		assertEquals(expectedIndex, tree.findNearestIndex(45.0));
		double expectedValue = 40.0;
		assertEquals(expectedValue, tree.findNearestValue(36.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(39.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(40.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(41.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(45.0), epsilon);

		// Try values very close to the edge of the previous index.
		assertEquals(expectedIndex, tree.findNearestIndex(35.0 + epsilon));

		return;
	}

	/**
	 * Checks that the element at index 5 in the tree can be found with the
	 * appropriate search values (anything between 45.0--exclusive--to
	 * 55.0--inclusive).
	 */
	@Test
	public void checkFindFifthElement() {
		int expectedIndex = 5;
		assertEquals(expectedIndex, tree.findNearestIndex(46.0));
		assertEquals(expectedIndex, tree.findNearestIndex(49.0));
		assertEquals(expectedIndex, tree.findNearestIndex(50.0));
		assertEquals(expectedIndex, tree.findNearestIndex(51.0));
		assertEquals(expectedIndex, tree.findNearestIndex(55.0));
		double expectedValue = 50.0;
		assertEquals(expectedValue, tree.findNearestValue(46.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(49.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(50.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(51.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(55.0), epsilon);

		// Try values very close to the edge of the previous index.
		assertEquals(expectedIndex, tree.findNearestIndex(45.0 + epsilon));

		return;
	}

	/**
	 * Checks that the element at index 6 in the tree can be found with the
	 * appropriate search values (anything between 55.0--exclusive--to
	 * 65.0--inclusive).
	 */
	@Test
	public void checkFindSixthElement() {
		int expectedIndex = 6;
		assertEquals(expectedIndex, tree.findNearestIndex(56.0));
		assertEquals(expectedIndex, tree.findNearestIndex(59.0));
		assertEquals(expectedIndex, tree.findNearestIndex(60.0));
		assertEquals(expectedIndex, tree.findNearestIndex(61.0));
		assertEquals(expectedIndex, tree.findNearestIndex(65.0));
		double expectedValue = 60.0;
		assertEquals(expectedValue, tree.findNearestValue(56.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(59.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(60.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(61.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(65.0), epsilon);

		// Try values very close to the edge of the previous index.
		assertEquals(expectedIndex, tree.findNearestIndex(55.0 + epsilon));

		return;
	}

	/**
	 * Checks that the greatest element at index 7 in the tree can be found with
	 * the appropriate search values (anything greater than 65.0--exclusive).
	 */
	@Test
	public void checkFindSeventhElement() {
		int expectedIndex = 7;
		assertEquals(expectedIndex, tree.findNearestIndex(66.0));
		assertEquals(expectedIndex, tree.findNearestIndex(69.0));
		assertEquals(expectedIndex, tree.findNearestIndex(70.0));
		assertEquals(expectedIndex, tree.findNearestIndex(71.0));
		assertEquals(expectedIndex, tree.findNearestIndex(75.0));
		double expectedValue = 70.0;
		assertEquals(expectedValue, tree.findNearestValue(66.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(69.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(70.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(71.0), epsilon);
		assertEquals(expectedValue, tree.findNearestValue(75.0), epsilon);

		// Try values very close to the edge of the previous index.
		assertEquals(expectedIndex, tree.findNearestIndex(65.0 + epsilon));

		return;
	}
}
