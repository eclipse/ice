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

package org.eclipse.ice.tests.dev.annotations.processors;

import static com.google.testing.compile.CompilationSubject.*;

import org.junit.jupiter.api.Test;

import com.google.testing.compile.Compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;

class PersistenceAnnotationTest {

	/**
	 * Helper for testing DataElement related annotations.
	 */
	private DataElementAnnotationTestHelper helper =
		new DataElementAnnotationTestHelper();

	private static final String INPUT = "input/Persistence/";
	private static final String PATTERN = "patterns/Persistence/";

	@AllArgsConstructor
	private static enum Input implements JavaFileObjectResource {
		NO_DATA_FIELDS(INPUT + "NoDataFields.java"),
		MANY(INPUT + "Many.java");
		@Getter
		private String path;
	}

	@AllArgsConstructor
	private static enum Pattern implements JavaFileObjectResource {
		DEFAULT(PATTERN + "DefaultImplementation.java");
		@Getter
		private String path;
	}

	@Test
	void testNoDataFields() {
		Compilation compile = helper.compile(Input.NO_DATA_FIELDS.get());
		assertThat(compile)
			.generatedSourceFile("TestPersistenceHandler")
			.containsElementsIn(Pattern.DEFAULT.get());
	}

	@Test
	void testManyDataFields() {
		Compilation compile = helper.compile(Input.MANY.get());
		assertThat(compile)
			.generatedSourceFile("TestPersistenceHandler")
			.containsElementsIn(Pattern.DEFAULT.get());
	}

}
