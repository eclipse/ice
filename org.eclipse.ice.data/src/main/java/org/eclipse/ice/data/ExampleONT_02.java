/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.ice.tests.data.owl;

import java.util.Iterator;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.reasoner.ValidityReport.Report;
import org.apache.jena.util.FileManager;
import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.vocabulary.SH;

public class ExampleONT_02 {

	public static void main(String[] args) {
		FileManager.get().addLocatorClassLoader(ExampleONT_02.class.getClassLoader());

		// FIXME! Needs to be rewritten to point to the right class loader and resources
		// folder.
		Model tbox = FileManager.get().loadModel("/home/bkj/ICEIII/ice/org.eclipse.ice.data.owl/" + 
		//"ice-owl.rdf", null,"RDF/XML"); // http://en.wikipedia.org/wiki/Tbox
		"ice-owl.ttl", null, "TURTLE"); // http://en.wikipedia.org/wiki/Tbox
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner().bindSchema(tbox.getGraph());
		Model abox = FileManager.get().loadModel("/home/bkj/ICEIII/ice/org.eclipse.ice.data.owl/" +
		"test.ttl", null,"TURTLE"); // http://en.wikipedia.org/wiki/Abox
//		"test.rdf", null, "RDF/XML"); // http://en.wikipedia.org/wiki/Abox
		InfModel inf = ModelFactory.createInfModel(reasoner, abox);

		ValidityReport validityReport = inf.validate();

		System.out.println("----- Begin Jena Review -----");

		if (!validityReport.isValid()) {
			System.out.println("Inconsistent");
		} else {
			System.out.println("Valid");
		}

		Iterator<Report> iter = validityReport.getReports();
		while (iter.hasNext()) {
			Report report = iter.next();
			System.out.println(report);
		}

		System.out.println("----- End Jena Review -----");

		System.out.println("----- Begin TopBraid/SHACL Review -----");

		Resource reportResource = ValidationUtil.validateModel(abox, tbox, true);
		boolean conforms = reportResource.getProperty(SH.conforms).getBoolean();

		if (!conforms) {
			System.out.println("Inconsistent");
		} else {
			System.out.println("Valid");
		}

		System.out.println("----- End TopBraid/SHACL Review -----");

		return;
	}

}
