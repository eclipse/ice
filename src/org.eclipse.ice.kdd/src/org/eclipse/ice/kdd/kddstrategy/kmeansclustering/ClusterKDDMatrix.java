/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.kdd.kddstrategy.kmeansclustering;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.ice.analysistool.IDataProvider;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.kdd.kddmath.KDDMatrix;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The ClusterKDDMatrix is a subclass of KDDMatrix that provides a cluster
 * method that produces a set of Clusters.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Alex McCaskey
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ClusterKDDMatrix extends KDDMatrix {

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private DistanceMeasure distanceMeasure;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the set of clusters after KMeans clustering is performed.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ArrayList<Cluster> clusters;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor, takes a valid set of IData and constructs this matrix.
	 * Initializes a EuclideanDistanceMeasure by default.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param data
	 * @throws IllegalArgumentException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ClusterKDDMatrix(IDataProvider data) throws IllegalArgumentException {
		// begin-user-code
		super(data);
		clusters = new ArrayList<Cluster>();
		distanceMeasure = new EuclideanDistanceMeasure();
		// end-user-code
	}

	public ClusterKDDMatrix(ArrayList<Double> elements, int nRows, int nCols) {
		super(elements, nRows, nCols);
		clusters = new ArrayList<Cluster>();
		distanceMeasure = new EuclideanDistanceMeasure();
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor, for injecting a realization of the DistanceMeasure
	 * interface.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param data
	 * @param measure
	 * @throws IllegalArgumentException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ClusterKDDMatrix(IDataProvider data, DistanceMeasure measure)
			throws IllegalArgumentException {
		// begin-user-code
		super(data);
		distanceMeasure = measure;
		clusters = new ArrayList<Cluster>();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method performs a KMeans cluster algorithm on this matrix-structured
	 * data set. It produces an ArrayList of Clusters, each holding a map of
	 * data indices to vectors in an N-dimensional space represented as
	 * KDDMatrices with number of columns equal to 1 and number of rows equal to
	 * N. It takes as argument the number of cluster centroids to produce, as
	 * well as the number of iterations to use in refining the clusters.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param nClusters
	 * @param nIterations
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void cluster(int nClusters, int nIterations) {
		// begin-user-code

		// Local Declarations
		ArrayList<KDDMatrix> centroids = new ArrayList<KDDMatrix>();
		ArrayList<Double> distances = new ArrayList<Double>();
		KDDMatrix vector = null;
		Double minDistance = 0.0;

		// Clear any old clusters from a previous run
		clusters.clear();

		// Create nClusters clusters, and calculate
		// a set of random means
		for (int i = 0; i < nClusters; i++) {
			// Create the cluster
			Cluster c = new Cluster(i);
			clusters.add(c);

			// Add the random center of mass vector to
			// the centroids list
			centroids.add(c.getRandomClusterMean(nCols, getMinMatrixElement(),
					getMaxMatrixElement()));
		}

		// Loop over the specified number of iterations
		// to refine the data clustering
		for (int i = 0; i < nIterations; i++) {
			// Clear the cluster data from the previous iteration
			for (Cluster c : clusters) {
				c.clearData();
			}

			// For each row vector we need to calculate its
			// distance to each centroid
			for (int j = 0; j < numberOfRows(); j++) {
				// Get the jth row vector from this matrix
				vector = getRow(j);

				// Transpose it so we only work with
				// column vectors
				vector.transpose();

				// Get a list of the distances from this row vector
				// to this centroid
				for (int k = 0; k < nClusters; k++) {
					// Add the distance between the jth row vector and
					// the kth centroid to the list of distances
					distances.add(distanceMeasure.getDistance(centroids.get(k),
							vector));
				}

				// Get the minimum distance
				minDistance = Collections.min(distances);

				// Find the index of the minDistance, which is also the index
				// of the correct centroid to add this vector to
				clusters.get(distances.indexOf(minDistance)).addVector(vector);

				// Clear the distances array
				distances.clear();
			}

			// Now we can calculate more accurate
			// cluster centroids for the next iteration
			centroids.clear();
			for (Cluster c : clusters) {
				centroids.add(c.getClusterMean(nCols));
			}
		}

		// Debug
		for (Cluster c : clusters) {
			System.out
					.println("\n[ICE KDD] Cluster "
							+ c.getClusterIndex()
							+ " has "
							+ c.numberOfElements()
							+ " data elements after \n\tKMeans clustering algorithm with "
							+ nIterations + " iterations.");
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the largest element in this matrix.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Double getMaxMatrixElement() {
		// begin-user-code
		return Collections.max(elements);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the smallest element in this matrix
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Double getMinMatrixElement() {
		// begin-user-code
		return Collections.min(elements);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the number of Clusters.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumberOfClusters() {
		// begin-user-code
		return clusters.size();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the cluster mean vectors.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<KDDMatrix> getClusterMeans() {
		// begin-user-code
		ArrayList<KDDMatrix> retMeans = new ArrayList<KDDMatrix>();
		for (Cluster c : clusters) {
			retMeans.add(c.getClusterMean(nCols));
		}
		return retMeans;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method allows this ClusterKDDMatrix to perform a multi-dimensional
	 * scaling algorithm to put the clustered data in a form suitable for
	 * visualization. It takes the number of dimensions to visualize the data
	 * in, which can be one, two, or three.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param dimensions
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void scaleData(int dimensions) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Plot this data and return the URI to the corresponding plot.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public URI plot() {
		// begin-user-code
		// Currently we are just gonna write the data
		// to file and let ICE plot it as it sees fit

		// Get the default project, which should be
		// the only element in getProjects()
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		if (root.getProjects().length == 0) {
			return null;
		}

		// Get the IProject
		IProject project = root.getProjects()[0];

		// Create a handle to the file we are going to write to
		IFile file = project.getFile("rawKMeansCluster.txt");

		// If this file exists, then we've already used
		// it to write cluster data, so lets create a file
		// with a different name
		if (file.exists()) {
			int counter = 1;
			while (file.exists()) {
				file = project.getFile("rawKMeansCluster_"
						+ String.valueOf(counter) + ".txt");
				counter++;
			}
		}

		// Local declarations to write data to file
		String line = "", contents = "";
		ArrayList<String> fileContents = new ArrayList<String>();

		// Loop over all the clusters...
		for (Cluster c : clusters) {
			// Add an initial label to indicate the following
			// data belongs to this cluster
			fileContents.add("Cluster " + c.getClusterIndex());

			// Loop over all the clusters data vectors
			for (KDDMatrix element : c.getDataElements()) {
				// For each element
				for (int i = 0; i < element.numberOfRows(); i++) {
					// Write the vector as a row of elements
					line = line + element.getElement(i, 0) + " ";
				}

				// Add the line to the arraylist
				fileContents.add(line);
				line = "";
			}
			fileContents.add("");
		}

		// Convert the arraylist to one string
		// so we can use the getBytes method
		for (String s : fileContents) {
			contents = contents + s + "\n";
		}

		// Create the IFile with a ByteArrayInputStream
		try {
			file.create(new ByteArrayInputStream(contents.getBytes()), false,
					null);
		} catch (CoreException e) {
			e.printStackTrace();
			// Maybe check if file is there, delete it...
			return null;
		}

		// Return the URI.
		return file.getLocationURI();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method indicates whether or not this matrix has been clustered.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isClustered() {
		// begin-user-code
		return (!clusters.isEmpty());
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the number of elements in the cluster indexed by the argument.
	 * Returns -1 if invalid index.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param index
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumberOfClusterElements(int index) {
		// begin-user-code
		if (index >= clusters.size()) {
			return -1;
		}

		return clusters.get(index).numberOfElements();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ClusterKDDMatrix() {
		// begin-user-code
		super();
		clusters = new ArrayList<Cluster>();
		distanceMeasure = new EuclideanDistanceMeasure();
		// end-user-code
	}

	@Override
	public void copy(KDDMatrix other) {
		// Make sure its not null
		if (other == null) {
			return;
		}

		// Copy the KDDMatrix data.
		super.copy(other);

		// Now make sure its a ClusterKDDMatrix
		ClusterKDDMatrix matrix;
		if (other instanceof ClusterKDDMatrix) {
			// Cast it
			matrix = (ClusterKDDMatrix) other;

			// Get its cluster set
			this.clusters = matrix.clusters;

			// Get its distance measure method
			this.distanceMeasure = matrix.distanceMeasure;
		}

		return;
	}

}