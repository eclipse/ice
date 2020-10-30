/******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0,
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jay Jay Billings - Initial API and implementation and/or initial docs
 *   Marshall McDonnell - Added delete method
 *****************************************************************************/
package org.eclipse.ice.bats;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.riot.Lang;
import org.apache.jena.update.Update;
import org.apache.jena.util.FileManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

/**
 * This class represents a set of data describing a topic or item of interest.
 * In BATS, data sets are natively distributed across one or more servers. The
 * initial hostname and port of an Apache Jena Fuseki server must be provided in
 * order to pull the root RDF model that describes this data set, as well as an
 * associated metadata models.
 * 
 * Data sets are organized in the Apache Jena style, with each set containing
 * one or more subsets called "Models." This class largely wraps those
 * operations into a more convenient interface that masks Jena's HTTP-based
 * transfer routines, and fits the intended use better. However, advanced users
 * may retrieve the Jena dataset by calling getJenaDataset().
 * 
 * By default, DataSet only create Jena TDB2 persistent triple stores on the
 * remote server for RDF models. Instances do not hold copies or handles to any
 * data that they represent because the size of the data is not known in
 * advance. Instead, this class interacts directly with the remote triple store.
 * 
 * @author Jay Jay Billings
 *
 */
public class DataSet {

    /**
     * This is the default name used as the base for all unnamed instances of
     * DataSet.
     */
    public static final String DEFAULT_NAME = "unnamed-dataset";

    /**
     * Log utility
     */
    protected static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(DataSet.class.getName());

    /**
     * The default host which holds the dataset.
     */
    private String host = "http://localhost";

    /**
     * The default port of the host which holds the dataset.
     */
    private int port = 3030;

    /**
     * The default name for a dataset.
     */
    private String name = DEFAULT_NAME;


    /**
     * This operation sets the name of the data set. The name of the data set is the
     * name recognized by the host, not the local machine. It must be set prior to
     * calling create() or load(), but calling it after those operations does not
     * change it.
     *
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * This operation returns the name of the data set.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * This operation returns the host of the data set.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * This operation sets the host at which the data set should be created or from
     * which it should be loaded.
     *
     * @param host the URI of the remote Fuseki host that hosts the data set
     */
    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * This operation returns the port of the host of this data set.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * This operation sets the expected port of the host of this data set.
     *
     * @param port
     */
    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * This operation returns the full URI identifying this data set on the remote
     * server, including hostname, port, and set name.
     *
     * @return the full URI including all parts
     */
    public String getFullURI() {
        return getHost() + ":" + getPort() + "/" + getName();
    }

    /**
     * This operation returns the full URI of the Fuseki Data API location
     * for this data set on the remote server
     * using the hostname, port, and set name.
     *
     * @return the Fuseki Data API location
     */
    public String getFusekiDatasetsURI() {
        String fusekiLocation = host + ":" + port + "/";
        String fusekiDataAPILoc = "$/datasets";
        return  getHost() + ":" + getPort() + "/$/datasets";
    }

    /**
     * This operation creates a dataset with the given name. If no name is provided
     * to setName(), the default name with a UUID appended to it will be used such
     * that the form of the name will be "unnamed-dataset_<UUID>." Note that
     * creation does not imply retrieval, and that the getRootModel() or getModel()
     * functions still need to be called. Likewise (and obviously), if the model
     * already exists on the remote server it can just be retrieved without calling
     * create().
     *
     * @throws Exception this exception is thrown if the data set cannot be created
     *                   for any reason.
     */
    public void create() throws Exception {

        // Configure the name
        String dbName = DEFAULT_NAME;
        if (name == DEFAULT_NAME) {
            name += "_" + UUID.randomUUID().toString();
        }
        dbName = name;
        // Per the spec, always use tdb2.
        String dbType = "tdb2";

        // Connect the HTTP client
        HttpClient client = HttpClientBuilder.create().build();
        String fusekiDataLocation = getFusekiDatasetsURI();
        HttpPost post = new HttpPost(fusekiDataLocation);

        // Add the database parameters into the form with UTF_8 encoding.
        List<NameValuePair> form = new ArrayList<NameValuePair>();
        form.add(new BasicNameValuePair("dbName", dbName));
        form.add(new BasicNameValuePair("dbType", dbType));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(form, Consts.UTF_8);

        // Create the data set
        post.setEntity(formEntity);
        HttpResponse response = client.execute(post);
        logger.debug(response.toString());

        return;
    }

    /**
     * This operation deletes the data set with the given name.
     *
     * @throws Exception
     */
    public void delete() throws Exception {
        // Connect the HTTP client
        HttpClient client = HttpClientBuilder.create().build();
        String fusekiDataLocation = getFusekiDatasetsURI();
        HttpDelete delete = new HttpDelete(fusekiDataLocation + "/" + name);

        // Delete the data set
        HttpResponse response = client.execute(delete);
        logger.debug(response.toString());

        return;
    }

    /**
     * This operation directs the data set to update and persist any remotely stored
     * versions of this model with this version of the model. This action is a
     * complete re-write of the data, with out a merge or any checks.
     * 
     * @param modelName the name of the model that will be updated
     * @param model     the model that will be updated remotely
     */
    public void updateModel(final String modelName, Model model) {

        RDFConnectionRemoteBuilder uploadConnBuilder = RDFConnectionFuseki.create()
                .destination(getFullURI() + "/data");

        // Open a connection to upload the ICE ontology.
        try (RDFConnectionFuseki uploadConn = (RDFConnectionFuseki) uploadConnBuilder.build()) {
            // Note that transactions must proceed with begin(), some operation(), and
            // commit().
            uploadConn.begin(ReadWrite.WRITE);
            System.out.println(model.toString());
//            uploadConn.load(modelName, model);
            uploadConn.put(modelName, model);
            uploadConn.commit();
            logger.debug("Committed model " + modelName + " to data set" + getName());
        } catch (Exception e) {
            logger.error("Unable to update model " + modelName + " in data set " + getName()
                    + " on the remote Fuseki server.", e);
        }
    }

    /**
     * This operation returns the root model in the data set, which is called the
     * default graph in the Jena jargon. It is referred to as the root model here to
     * denote that it is the root model in a hierarchy of models describing the same
     * set. This is a convenience method identically equal to calling getModel(null)
     * or getModel("default").
     *
     * @return the root model if the data set exists, otherwise null
     */
    public Model getRootModel() {
        return getModel(null);
    }

    /**
     * This operation returns the model with the given name if it exists in the data
     * set.
     *
     * @param modelName the name of the model that should be retrieved from the data
     *                  set. Note that like Jena, calling with an argument of
     *                  "default" or "null" will return the default graph/model.
     * @return the model if it exists in the data set, otherwise null
     */
    public Model getModel(final String modelName) {
        Model model = null;
        RDFConnectionRemoteBuilder getConnBuilder = RDFConnectionFuseki.create()
                .destination(getFullURI() + "/data");

        try (RDFConnectionFuseki getConn = (RDFConnectionFuseki) getConnBuilder.build()) {
            getConn.begin(ReadWrite.READ);
            model = getConn.fetch(modelName);
            getConn.commit();
            logger.debug("Retrieved model " + modelName + " from data set" + getName());
        } catch (Exception e) {
            logger.error("Unable to find model " + modelName + " in data set " + getName(), e);
        }

        return model;
    }

    /**
     * This operation returns the raw Jena data set pulled from Fuseki. This could
     * be a long-running operation depending on the size of the remote data. This
     * operation is intended purely as a convenience to advanced users who want to
     * manipulate the data set directly.
     *
     * @return the raw Jena data set
     */
    public Dataset getJenaDataset() {
        Dataset set = null;
        RDFConnectionRemoteBuilder getConnBuilder = RDFConnectionFuseki.create()
                .destination(getFullURI() + "/get");

        try (RDFConnectionFuseki getConn = (RDFConnectionFuseki) getConnBuilder.build()) {
            getConn.begin(ReadWrite.READ);
            set = getConn.fetchDataset();
            getConn.commit();
            logger.debug("Retrieved data set" + getName());
        } catch (Exception e) {
            logger.error("Unable to find data set " + getName(), e);
        }

        return set;
    }

}
