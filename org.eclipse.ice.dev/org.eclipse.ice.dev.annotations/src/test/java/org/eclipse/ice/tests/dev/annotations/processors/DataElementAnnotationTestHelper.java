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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

import org.eclipse.ice.dev.annotations.processors.DataElementProcessor;

import com.google.testing.compile.Compilation;

import static com.google.testing.compile.Compiler.*;

/**
 * Helper class for testing DataElement related annotations.
 * @author Daniel Bluhm
 */
public class DataElementAnnotationTestHelper {

	public DataElementAnnotationTestHelper() { }

	/**
	 * Retrieve an instance of Lombok's Annotation Processor.
	 *
	 * This is a nasty method that violates the accessibility of the Processor by
	 * reflection but is necessary to correctly process and test the generated code.
	 * @return lombok annotation processor
	 */
	private Processor getLombokAnnotationProcessor() {
		Processor p = null;
		try {
			Class<?> c = Class.forName("lombok.launch.AnnotationProcessorHider$AnnotationProcessor");
			Constructor<?> constructor = c.getConstructor();
			constructor.setAccessible(true);
			p = (Processor) constructor.newInstance();
		} catch (
			ClassNotFoundException | InstantiationException |
			IllegalAccessException | IllegalArgumentException |
			InvocationTargetException | NoSuchMethodException |
			SecurityException e
		) {
			System.err.println("Failed to get Lombok AnnotationProcessor!");
			e.printStackTrace();
		}
		return p;
	}

	/**
	 * Compile the sources with needed processors.
	 * @param sources to compile
	 * @return Compilation result
	 */
	public Compilation compile(JavaFileObject... sources) {
		Compilation compilation = javac()
			.withProcessors(
				getLombokAnnotationProcessor(),
				new DataElementProcessor()
			).compile(sources);
		return compilation;
	}
}
