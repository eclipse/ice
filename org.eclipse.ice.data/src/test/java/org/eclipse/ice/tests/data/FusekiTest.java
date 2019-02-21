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
import org.apache.jena.query.ResultSetFormatter;
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

		// Create a connection builder for connecting to the Fuseki triple store.
		String fusekiConnURI = "http://localhost:3030/dataset.html#query";
		RDFConnectionRemoteBuilder connBuilder = RDFConnectionFuseki.create()
				.destination(fusekiConnURI);
		
		Query query = QueryFactory.create("SELECT * { BIND('Hello'as ?text) }");

		// In this variation, a connection is built each time.
		try (RDFConnectionFuseki conn = (RDFConnectionFuseki) connBuilder.build()) {
			Model model = FileManager.get().loadModel("data/ice-owl.ttl", null, "TURTLE");
			conn.load(model);
			
//			conn.queryResultSet(query, ResultSetFormatter::out);
		}

	}

}
