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

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

public class ExampleAPI_ICE_01 {

    public static void main(String[] args) {
        FileManager.get().addLocatorClassLoader(ExampleAPI_ICE_01.class.getClassLoader());
        Model model = FileManager.get().loadModel("/home/bkj/ICEIII/ice/org.eclipse.ice.data.owl/test.rdf", null, "RDF/XML");

//        String queryString = "PREFIX example: <http://example.org/>" +
//            "PREFIX foaf: <h 	ttp://xmlns.com/foaf/0.1/>" +
//            "SELECT * WHERE {" +
//            "?person foaf:name ?x ." + "}";
//        Query query = QueryFactory.create(queryString);
//        QueryExecution qExec = QueryExecutionFactory.create(query,model);
//        try {
//            System.out.println("Testing queries.");
//            ResultSet results = qExec.execSelect();
//            while (results.hasNext()) {
//            QuerySolution soln = results.nextSolution();
//            Literal name = soln.getLiteral("x");
//            System.out.println("name = " + name);
//            }
//        } finally {
//            qExec.close();
//        }


        StmtIterator iter = model.listStatements();
        try {
            while ( iter.hasNext() ) {
                Statement stmt = iter.next();
                
                System.out.println(stmt.toString());
                
                Resource s = stmt.getSubject();
                Resource p = stmt.getPredicate();
                RDFNode o = stmt.getObject();
                
//                System.out.println(s.toString());
//                System.out.println(p.toString());
//                System.out.println(o.toString());
                
                if ( s.isURIResource() ) {
                    System.out.print("URI");
                } else if ( s.isAnon() ) {
                    System.out.print("blank");
                }
                
                if ( p.isURIResource() ) 
                    System.out.print(" URI ");
                
                if ( o.isURIResource() ) {
                    System.out.print("URI");
                } else if ( o.isAnon() ) {
                    System.out.print("blank");
                } else if ( o.isLiteral() ) {
                    System.out.print("literal");
                }
                
                System.out.println();                
            }
        } finally {
            if ( iter != null ) iter.close();
        }
    }

}
