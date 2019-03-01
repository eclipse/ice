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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BasicRequestLine;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.util.FileManager;
import org.apache.xerces.impl.io.UTF8Reader;

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
			
			String dbName = "test11", dbType = "tdb2";
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost("http://localhost:3030/$/datasets");
			List<NameValuePair> form = new ArrayList<NameValuePair>();
			form.add(new BasicNameValuePair("dbName",dbName));
			form.add(new BasicNameValuePair("dbType",dbType));
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(form, Consts.UTF_8);
			post.setEntity(formEntity);
			HttpResponse response = client.execute(post);
			System.out.println(response.toString());
			
			// Grab the file with the Jena FileManager.
			Model model = FileManager.get().loadModel("data/ice-owl.ttl",
					"https://www.eclipse.org/ice", "TURTLE");
			// Note that transactions must proceed with begin(), some operation(), and
			// commit().
//			uploadConn.begin(ReadWrite.WRITE);
//			uploadConn.load("ice-owl", model);
//			uploadConn.commit();
		} catch (Exception e) {
			System.err.println("Unable to upload ICE ontology.");
			e.printStackTrace();
		}

		// Pull down the full model and write it to System out. This should, in
		// principle, only pull back the ICE model it uploaded. However, it is pulling
		// back the entire 836 triple default RDF graph, because I am calling
		// fetch(null).
//		try (RDFConnectionFuseki getConn = (RDFConnectionFuseki) getConnBuilder.build()) {
//			getConn.begin(ReadWrite.READ);
//			Model model = getConn.fetch(null);
//			getConn.commit();
//			model.write(System.out);
//		} catch (Exception e) {
//			System.err.println("Unable to download ICE ontology.");
//			e.printStackTrace();
//		}

	}

}
