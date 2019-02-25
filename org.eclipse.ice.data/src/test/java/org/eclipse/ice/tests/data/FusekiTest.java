/******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *****************************************************************************/
package org.eclipse.ice.tests.data;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.util.FileManager;

/**
 * This is a basic test code for connecting to an working with a Fuseki
 * database. It is a simple example for development purposes only.
 * 
 * @author Jay Jay Billings
 *
 */
public class FusekiTest {

	public static void main(String args[]) {

		// Create connection builders for connecting to the Fuseki triple store
		String fusekiURI = "http://localhost:3030/bats-dataset";
		String fusekiUploadURI = fusekiURI + "/data";
		String fusekiGetURI = fusekiURI + "/get";
		RDFConnectionRemoteBuilder uploadConnBuilder = RDFConnectionFuseki.create()
				.destination(fusekiUploadURI);
		RDFConnectionRemoteBuilder getConnBuilder = RDFConnectionFuseki.create()
				.destination(fusekiGetURI);

		// Open a connection to upload the ICE ontology.
		try (RDFConnectionFuseki uploadConn = (RDFConnectionFuseki) uploadConnBuilder.build()) {
			// Grab the file with the Jena FileManager.
			Model model = FileManager.get().loadModel("data/ice-owl.ttl", null, "TURTLE");
			// Note that transactions must proceed with begin(), some operation(), and
			// commit().
			uploadConn.begin(ReadWrite.WRITE);
			uploadConn.load(model);
			uploadConn.commit();
		} catch (Exception e) {
			System.err.println("Unable to upload ICE ontology.");
			e.printStackTrace();
		}

		// Pull down the full model and write it to System out. This should, in
		// principle, only pull back the ICE model it uploaded. However, it is pulling
		// back the entire 836 triple default RDF graph, because I am calling
		// fetch(null).
		try (RDFConnectionFuseki getConn = (RDFConnectionFuseki) getConnBuilder.build()) {
			getConn.begin(ReadWrite.READ);
			Model model = getConn.fetch(null);
			getConn.commit();
			model.write(System.out);
		}

	}

}
