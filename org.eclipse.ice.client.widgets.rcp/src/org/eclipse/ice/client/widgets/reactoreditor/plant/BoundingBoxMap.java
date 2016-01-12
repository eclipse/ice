/// *******************************************************************************
// * Copyright (c) 2014 UT-Battelle, LLC.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// * Initial API and implementation and/or initial documentation - Jay Jay
/// Billings,
// * Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
// * Claire Saunders, Matthew Wang, Anna Wojtowicz
// *******************************************************************************/
// package org.eclipse.ice.client.widgets.reactoreditor.plant;
//
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Iterator;
// import java.util.List;
// import java.util.Map;
// import java.util.TreeMap;
//
// import com.jme3.bounding.BoundingBox;
// import com.jme3.math.Vector3f;
//
/// **
// * This class maintains a collection of {@link BoundingBox}es keyed on integer
// * IDs. It provides methods for adding and removing them as well as getters
/// for
// * minimum and maximum x, y, and z values across all stored BoundingBoxes.
// *
// * @author Jordan H. Deyton
// *
// */
// public class BoundingBoxMap {
//
// // FIXME We might want to make a thread-safe version of this class.
//
// /**
// * A HashMap of {@link BoundingBox}es stored in this {@link BoundingBoxMap}
// * keyed on their IDs.
// */
// private final Map<Integer, BoundingBox> boxes;
//
// /**
// * A TreeMap of X values for all {@link BoundingBox}es stored in this
// * {@link BoundingBoxMap}. There may be multiple boxes that share the same X
// * location, hence the values in this TreeMap are Lists of BoundingBox IDs.
// */
// private final TreeMap<Float, List<Integer>> xTree;
// /**
// * A TreeMap of Y values for all {@link BoundingBox}es stored in this
// * {@link BoundingBoxMap}. There may be multiple boxes that share the same X
// * location, hence the values in this TreeMap are Lists of BoundingBox IDs.
// */
// private final TreeMap<Float, List<Integer>> yTree;
// /**
// * A TreeMap of Z values for all {@link BoundingBox}es stored in this
// * {@link BoundingBoxMap}. There may be multiple boxes that share the same X
// * location, hence the values in this TreeMap are Lists of BoundingBox IDs.
// */
// private final TreeMap<Float, List<Integer>> zTree;
//
// private Float minX, minY, minZ;
// private Float maxX, maxY, maxZ;
//
// /**
// * The default constructor. Initializes an empty list. Returned min/max
// * values are
// */
// public BoundingBoxMap() {
// boxes = new HashMap<Integer, BoundingBox>();
// xTree = new TreeMap<Float, List<Integer>>();
// yTree = new TreeMap<Float, List<Integer>>();
// zTree = new TreeMap<Float, List<Integer>>();
//
// minX = null;
// minY = null;
// minZ = null;
// maxX = null;
// maxY = null;
// maxZ = null;
// }
//
// /**
// * Removes the {@link BoundingBox} associated with an ID from this
// * {@link BoundingBoxMap}.
// *
// * @param id
// * The ID of the BoundingBox to remove.
// * @return True if the min or max bounds over all contained BoundingBoxes
// * changed, false otherwise.
// */
// public boolean remove(int id) {
//
// // Get the previous min and max values.
// Vector3f previousMin = getMin();
// Vector3f previousMax = getMax();
//
// // Remove the box.
// delete(id);
//
// // Return whether or not the bounds have changed.
// return differentBounds(previousMin, previousMax);
// }
//
// /**
// * Removes multiple {@link BoundingBox}es associated with IDs from this
// * {@link BoundingBoxMap}.
// *
// * @param ids
// * A List of IDs of the BoundingBoxes to remove.
// * @return True if the min or max bounds over all contained BoundingBoxes
// * changed, false otherwise.
// */
// public boolean removeAll(List<Integer> ids) {
// boolean boundsChanged = false;
//
// if (ids != null) {
// // Get the previous min and max values.
// Vector3f previousMin = getMin();
// Vector3f previousMax = getMax();
//
// // Remove all of the specified IDs.
// for (Integer id : ids) {
// if (id != null) {
// delete(id);
// }
// }
//
// // Determine whether or not the bounds have changed.
// boundsChanged = differentBounds(previousMin, previousMax);
// }
//
// return boundsChanged;
// }
//
// /**
// * This private method deletes the specified ID from the map of
// * {@link #boxes}, {@link #xTree}, {@link #yTree}, and {@link zTree}. It
// * does not perform any check on the change in overall bounds.
// *
// * @param id
// * The ID of the BoundingBox to remove.
// */
// private void delete(int id) {
// // Get the box corresponding to the ID and remove its values from the
// // maps.
// BoundingBox box = boxes.remove(id);
// if (box != null) {
//
// // A temporary vector.
// Vector3f tmp = new Vector3f();
//
// // Remove the values for the box from the maps.
// removeValues(box.getMin(tmp), id);
// removeValues(box.getMax(tmp), id);
// }
//
// return;
// }
//
// /**
// * Adds a {@link BoundingBox} associated with an ID to this
// * {@link BoundingBoxMap} .
// *
// * @param id
// * The ID of the BoundingBox to remove.
// * @return True if the min or max bounds over all contained BoundingBoxes
// * changed, false otherwise.
// */
// public boolean put(int id, BoundingBox box) {
// // Get the previous min and max values.
// Vector3f previousMin = getMin();
// Vector3f previousMax = getMax();
//
// // Add the box.
// insert(id, box);
//
// // Return whether or not the bounds have changed.
// return differentBounds(previousMin, previousMax);
// }
//
// /**
// * Adds multiple {@link BoundingBox}es associated with IDs to this
// * {@link BoundingBoxMap}.
// *
// * @param ids
// * A List of IDs of the BoundingBoxes to remove.
// * @return True if the min or max bounds over all contained BoundingBoxes
// * changed, false otherwise.
// */
// public boolean putAll(List<Integer> ids, List<BoundingBox> boxes) {
// boolean boundsChanged = false;
//
// if (ids != null && boxes != null && ids.size() >= boxes.size()) {
// // Get the previous min and max values.
// Vector3f previousMin = getMin();
// Vector3f previousMax = getMax();
//
// // Add all of the specified IDs and boxes.
// for (int i = 0; i < ids.size(); i++) {
// Integer id = ids.get(i);
// if (id != null) {
// insert(id, boxes.get(i));
// }
// }
//
// // Determine whether or not the bounds have changed.
// boundsChanged = differentBounds(previousMin, previousMax);
// }
//
// return boundsChanged;
// }
//
// /**
// * This private method adds the specified ID and {@link BoundingBox} to the
// * map of {@link #boxes}, {@link #xTree}, {@link #yTree}, and {@link zTree}.
// * It does not perform any check on the change in overall bounds.
// *
// * @param id
// * The ID of the BoundingBox to add.
// * @param box
// * The BoudingBox to add.
// */
// private void insert(int id, BoundingBox box) {
//
// if (box != null) {
// // A temporary vector.
// Vector3f tmp = new Vector3f();
//
// // Put the new box into the hash map of boxes. If there was already
// // a box there, we need to remove its values from the x, y, and z
// // maps.
// BoundingBox oldBox = boxes.put(id, box);
// if (oldBox != null) {
// removeValues(oldBox.getMin(tmp), id);
// removeValues(oldBox.getMax(tmp), id);
// }
//
// // Add the min and max values for the new box to the maps.
// addValues(box.getMin(tmp), id);
// addValues(box.getMax(tmp), id);
// }
//
// return;
// }
//
// /**
// * Removes the x, y, and z values from the x, y, and z TreeMaps for the
// * BoundingBox with the specified ID.
// *
// * @param values
// * The x, y, and z values for a BoundingBox. Usually, this is the
// * BoundingBox's min or max values.
// * @param id
// * The ID of the BoundingBox in {@link #boxes}.
// */
// private void removeValues(Vector3f values, int id) {
// // Remove the box's x values from the xTree. Also update the min and max
// // x values accordingly.
// if (removeValue(values.x, id, xTree)) {
// if (!xTree.isEmpty()) {
// minX = xTree.firstKey();
// maxX = xTree.lastKey();
// } else {
// minX = null;
// maxX = null;
// }
// }
// // Remove the box's y values from the yTree. Also update the min and max
// // y values accordingly.
// if (removeValue(values.y, id, yTree)) {
// if (!yTree.isEmpty()) {
// minY = yTree.firstKey();
// maxY = yTree.lastKey();
// } else {
// minY = null;
// maxY = null;
// }
// }
// // Remove the box's z values from the zTree. Also update the min and max
// // z values accordingly.
// if (removeValue(values.z, id, zTree)) {
// if (!zTree.isEmpty()) {
// minZ = zTree.firstKey();
// maxZ = zTree.lastKey();
// } else {
// minZ = null;
// maxZ = null;
// }
// }
// }
//
// /**
// * Removes a value associated with a BoundingBox ID from the map. This
// * method removes the entry from the map if there are no more associated
// * BoundingBoxes.
// *
// * @param value
// * The float value used as the key in the map.
// * @param id
// * The ID of the BoundingBox that is being detached from the
// * value.
// * @param map
// * The map keyed on the float value and containing BoundingBox
// * IDs.
// * @return True if there are no remaining BoundingBoxes with the key value.
// */
// private boolean removeValue(float value, int id,
// Map<Float, List<Integer>> map) {
//
// // Get the list of IDs corresponding to the value. Remove the ID from
// // the list.
// List<Integer> ids = map.get(value);
// // Make sure the list is valid! This can happen if the min and max are
// // the same.
// boolean listEmpty = (ids == null);
// if (!listEmpty) {
// Iterator<Integer> iterator;
// for (iterator = ids.iterator(); iterator.hasNext();) {
// if (iterator.next() == id) {
// iterator.remove();
// break;
// }
// }
//
// // If the list is empty, remove it from the map.
// listEmpty = ids.isEmpty();
// if (listEmpty) {
// map.remove(value);
// }
// }
//
// return listEmpty;
// }
//
// /**
// * Adds the x, y, and z values from the x, y, and z TreeMaps for the
// * BoundingBox with the specified ID.
// *
// * @param values
// * The x, y, and z values for a BoundingBox. Usually, this is the
// * BoundingBox's min or max values.
// * @param id
// * The ID of the BoundingBox in {@link #boxes}.
// */
// private void addValues(Vector3f values, int id) {
// // Add the box's x value to the xTree. Update the min and max x values.
// if (addValue(values.x, id, xTree)) {
// minX = xTree.firstKey();
// maxX = xTree.lastKey();
// }
// // Add the box's y value to the yTree. Update the min and max y values.
// if (addValue(values.y, id, yTree)) {
// minY = yTree.firstKey();
// maxY = yTree.lastKey();
// }
// // Add the box's z value to the zTree. Update the min and max z values.
// if (addValue(values.z, id, zTree)) {
// minZ = zTree.firstKey();
// maxZ = zTree.lastKey();
// }
// }
//
// /**
// * Adds a value associated with a BoundingBox ID to the map. This method
// * creates a List for containing multiple BoundingBoxes that share the same
// * float value if necessary.
// *
// * @param value
// * The float value used as a key in the map.
// * @param id
// * The ID of the BoundingBox that is being attached to the value.
// * @param map
// * The map keyed on the float value and containing BoundingBox
// * IDs.
// * @return True if the value is a new key in the map.
// */
// private boolean addValue(float value, int id, Map<Float, List<Integer>> map)
/// {
//
// // See if the list of IDs with that value already exists. Create it and
// // put it in the map if necessary.
// List<Integer> ids = map.get(value);
// boolean listNew = (ids == null);
// if (listNew) {
// ids = new ArrayList<Integer>();
// map.put(value, ids);
// }
//
// // Add the new ID to the list.
// if (!ids.contains(id)) {
// ids.add(id);
// }
//
// return listNew;
// }
//
// /**
// * Compares the specified min and max vectors with the current min and max
// * vectors that define the overall bounds of this collection of
// * {@link BoundingBox}es.
// *
// * @param min
// * A Vector3f containing minimum values for comparison.
// * @param max
// * A Vector3f containing maximum values for comparison.
// * @return True if the current bounds (either min or max) defined by this
// * collection do not match the specified bounds, false otherwise.
// */
// private boolean differentBounds(Vector3f min, Vector3f max) {
// boolean different = false;
//
// // If there are boxes currently in the map, then the bounds are
// // different if there weren't boxes before (both null values) or if the
// // bounds have changed.
// if (!boxes.isEmpty()) {
// different = (min == null || max == null || (min.x != getMinX()
// || min.y != getMinY() || min.z != getMinZ()
// || max.x != getMaxX() || max.y != getMaxY() || max.z != getMaxZ()));
// }
// // If there are no boxes currently in the map, then the bounds are
// // different only if there were bounds before.
// else {
// different = (min != null && max != null);
// }
// return different;
// }
//
// /**
// * Gets the smallest (most negative) X value in this {@link BoundingBoxMap}.
// *
// * @return The minimum X value of all stored {@link BoundingBox}es, or null
// * if there are no boxes.
// */
// public Float getMinX() {
// return minX;
// }
//
// /**
// * Gets the smallest (most negative) Y value in this {@link BoundingBoxMap}.
// *
// * @return The minimum Y value of all stored {@link BoundingBox}es, or null
// * if there are no boxes.
// */
// public Float getMinY() {
// return minY;
// }
//
// /**
// * Gets the smallest (most negative) Z value in this {@link BoundingBoxMap}.
// *
// * @return The minimum Z value of all stored {@link BoundingBox}es, or null
// * if there are no boxes.
// */
// public Float getMinZ() {
// return minZ;
// }
//
// /**
// * Gets the largest (most positive) X value in this {@link BoundingBoxMap}.
// *
// * @return The maximum X value of all stored {@link BoundingBox}es, or null
// * if there are no boxes.
// */
// public Float getMaxX() {
// return maxX;
// }
//
// /**
// * Gets the largest (most positive) Y value in this {@link BoundingBoxMap}.
// *
// * @return The maximum Y value of all stored {@link BoundingBox}es, or null
// * if there are no boxes.
// */
// public Float getMaxY() {
// return maxY;
// }
//
// /**
// * Gets the largest (most positive) Z value in this {@link BoundingBoxMap}.
// *
// * @return The maximum Z value of all stored {@link BoundingBox}es, or null
// * if there are no boxes.
// */
// public Float getMaxZ() {
// return maxZ;
// }
//
// /**
// * Gets the minimum X, Y, and Z values in this {@link BoundingBoxMap}.
// *
// * @return A {@link Vector3f} containing the minimum X, Y, and Z values of
// * all stored {@link BoundingBox}es, or null if there are no boxes.
// */
// public Vector3f getMin() {
// return (!boxes.isEmpty() ? new Vector3f(getMinX(), getMinY(), getMinZ())
// : null);
// }
//
// /**
// * Gets the maximum X, Y, and Z values in this {@link BoundingBoxMap}.
// *
// * @return A {@link Vector3f} containing the maximum X, Y, and Z values of
// * all stored {@link BoundingBox}es, or null if there are no boxes.
// */
// public Vector3f getMax() {
// return (!boxes.isEmpty() ? new Vector3f(getMaxX(), getMaxY(), getMaxZ())
// : null);
// }
//
// /**
// * Gets a new {@link BoundingBox} that contains all BoundingBoxes stored in
// * this {@link BoundingBoxMap}. It is defined by the minimum and maximum X,
// * Y, and Z values and is oriented along the three default axes.
// *
// * @return A BoundingBox containing all other BoundingBoxes in this
// * collection, or null if there are no boxes.
// */
// public BoundingBox getBoundingBox() {
// return (!boxes.isEmpty() ? new BoundingBox(getMin(), getMax()) : null);
// }
// }
