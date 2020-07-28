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

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.Persisted;

/**
 * Test DataElement to be used for testing generated PersistenceHandlers.
 * @author Daniel Bluhm
 */
@DataElement(name = "TestElement")
@Persisted(collection = TestElementPersistenceHandlerTest.COLLECTION)
public class TestElementSpec {
	@DataField private String test;
}
