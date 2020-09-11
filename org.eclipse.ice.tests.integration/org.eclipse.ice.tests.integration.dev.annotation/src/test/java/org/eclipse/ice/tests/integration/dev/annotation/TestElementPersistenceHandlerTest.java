/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.tests.integration.dev.annotation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.bson.Document;
import org.eclipse.ice.tests.util.data.TestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Test Generated Persistence Handler works as intended against MongoDB.
 * @author Daniel Bluhm
 */
class TestElementPersistenceHandlerTest {
	/**
	 * Logger.
	 */
	public final Logger logger = LoggerFactory.getLogger(
		TestElementPersistenceHandlerTest.class
	);

	/**
	 * The name of the collection to use in testing.
	 */
	public static final String COLLECTION = "test";

	/**
	 * Config file name.
	 */
	public static final String CONFIG = "mongo.properties";

	/**
	 * Default hostname of MongoDB instance.
	 */
	public static final String DEFAULT_HOST = "localhost";

	/**
	 * Default port of MongoDB instance.
	 */
	public static final String DEFAULT_PORT = "27017";

	/**
	 * Default database to use in testing.
	 */
	public static final String DEFAULT_DB = "test";

	/**
	 * Connection to MongoDB.
	 */
	private MongoDatabase db;

	/**
	 * The collection used during testing.
	 */
	private MongoCollection<Document> collection;

	/**
	 * Set up our test environment.
	 */
	public TestElementPersistenceHandlerTest() {
		TestConfig config = TestConfig.from(CONFIG);
		String host = config.getProperty("host", DEFAULT_HOST);
		String port = config.getProperty("port", DEFAULT_PORT);
		String database = config.getProperty("database", DEFAULT_DB);
		this.db = MongoClients.create(
			String.format("mongodb://%s:%s", host, port)
		).getDatabase(database);
		this.collection = this.db.getCollection(COLLECTION);
	}

	/**
	 * Prepare the database with one doc corresponding to the returned element.
	 * @param handler to use to insert the document
	 * @return The element that was saved to the collection.
	 * @throws Exception on failure
	 */
	public TestElement setupOneDoc(TestElementPersistenceHandler handler)
		throws Exception
	{
		TestElement element = new TestElementImplementation();
		element.setTest("unique string for testing");
		handler.save(element);
		return element;
	}

	/**
	 * Clear out collection after every test.
	 */
	@AfterEach
	public void cleanup() {
		this.collection.drop();
	}

	/**
	 * Test that we can insert an element.
	 * @throws Exception on failure
	 */
	@Test
	void testCanInsert() throws Exception {
		TestElementPersistenceHandler handler = new TestElementPersistenceHandler(db);
		setupOneDoc(handler);
		assertEquals(1, this.collection.countDocuments());
	}

	/**
	 * Test that we can insert more than one element.
	 * @throws Exception on failure
	 */
	@Test
	void testCanInsertMany() throws Exception {
		TestElementPersistenceHandler handler = new TestElementPersistenceHandler(db);
		TestElement element1 = new TestElementImplementation();
		element1.setTest("unique string for testing");
		handler.save(element1);
		TestElement element2 = new TestElementImplementation();
		element2.setTest("Element 2 string");
		handler.save(element2);
		assertEquals(2, this.collection.countDocuments());
	}

	/**
	 * Test that we can clear the collection.
	 * @throws Exception on failure
	 */
	@Test
	void testCanClear() throws Exception {
		TestElementPersistenceHandler handler = new TestElementPersistenceHandler(db);
		TestElement element1 = new TestElementImplementation();
		element1.setTest("unique string for testing");
		handler.save(element1);
		TestElement element2 = new TestElementImplementation();
		element2.setTest("Element 2 string");
		handler.save(element2);
		assertEquals(2, this.collection.countDocuments());
		handler.clear();
		assertEquals(0, this.collection.countDocuments());
	}

	/**
	 * Test that we can retrieve all TestElements from the collection.
	 * @throws Exception on failure
	 */
	@Test
	void testCanFindAll() throws Exception {
		TestElementPersistenceHandler handler = new TestElementPersistenceHandler(db);
		TestElement element1 = new TestElementImplementation();
		element1.setTest("unique string for testing");
		handler.save(element1);
		TestElement element2 = new TestElementImplementation();
		element2.setTest("Element 2 string");
		handler.save(element2);
		Iterator<TestElement> results = handler.findAll().iterator();
		assertEquals(element1, results.next());
		assertEquals(element2, results.next());
	}

	/**
	 * Test all the findBy methods.
	 * @throws Exception on failure
	 */
	@Test
	void testCanFindByMethods() throws Exception {
		TestElementPersistenceHandler handler = new TestElementPersistenceHandler(db);
		TestElement element = setupOneDoc(handler);
		assertEquals(element, handler.findByUUID(element.getUUID()));
		assertEquals(element, handler.findById(
			element.getId()).iterator().next()
		);
		assertEquals(element, handler.findByName(
			element.getName()).iterator().next()
		);
		assertEquals(element, handler.findByDescription(
			element.getDescription()).iterator().next()
		);
		assertEquals(element, handler.findByComment(
			element.getComment()).iterator().next()
		);
		assertEquals(element, handler.findByContext(
			element.getContext()).iterator().next()
		);
		assertEquals(element, handler.findByRequired(
			element.isRequired()).iterator().next()
		);
		assertEquals(element, handler.findBySecret(
			element.isSecret()).iterator().next()
		);
		assertEquals(element, handler.findByTest(
			element.getTest()).iterator().next()
		);
	}
}
