/******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0,
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Marshall McDonnell - Added initial unit tests
 *****************************************************************************/
package org.eclipse.ice.tests.bats;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.eclipse.ice.bats.DataSet;

/**
 * These are the unit tests of the BATS Dataset class.
 * 
 * @author Marshall McDonnell
 *
 */
public class DataSetTest {

    /**
     * This operation checks for proper default DataSet object construction
     */
    @Test
    public void testConstruction() {
        DataSet dataset = new DataSet();
        assertEquals(dataset.getName(), "unnamed-dataset");
        assertEquals(dataset.getHost(), "http://localhost");
        assertEquals(dataset.getPort(), 3030);
        assertEquals(dataset.getFullURI(), "http://localhost:3030/unnamed-dataset");
    }

    /**
     * This operation checks we can set the dataset name 
     */
    @Test
    public void testSetName() {
        DataSet dataset = new DataSet();
        dataset.setName("foo");
        assertEquals(dataset.getName(), "foo");
    }
    
    /**
     * This operation checks we can set the hostname
     */
    @Test
    public void testSetHost() {
        DataSet dataset = new DataSet();
        dataset.setHost("http://foo.com");
        assertEquals(dataset.getHost(), "http://foo.com");
    }

    /**
     * This operation checks we can set the port
     */
    @Test
    public void testSetPort() {
        DataSet dataset = new DataSet();
        dataset.setPort(8080);
        assertEquals(dataset.getPort(), 8080);
    }

    /**
     * This operation checks we get the correct Fuseki Data API URL
     */
    @Test
    public void testGetFusekiDatasetsURI() {
        DataSet dataset = new DataSet();
        assertEquals(
            dataset.getFusekiDatasetsURI(),
            "http://localhost:3030/$/datasets");
    }

}
