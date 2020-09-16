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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.tools.JavaFileObject;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import com.google.testing.compile.Compilation;

import lombok.AllArgsConstructor;

/**
 * Test the output of the DataElement Annotation Processor.
 *
 * Most of the tests use the "golden file" test strategy with the exception that
 * the golden files are actually patterns. ASTs are parsed from the golden files
 * and compared with the ASTs of the files generated from the input.
 *
 * All test DataElements should use the name "Test" for simplicity, as in:
 * <pre>
 * {@code @DataElement(name = "Test")}
 * public class MyTestElement {
 * 	...
 * }
 * </pre>
 * @author Daniel Bluhm
 *
 */
class DataElementProcessorTest {

	/**
	 * Helper for testing DataElement related annotations.
	 */
	private static DataElementAnnotationTestHelper helper =
		new DataElementAnnotationTestHelper();

	/**
	 * Fully qualified name of the generated interface.
	 */
	private static final String INTERFACE = "Test";

	/**
	 * Fully qualified name of the generated implementation.
	 */
	private static final String IMPLEMENTATION = "TestImplementation";

	/**
	 * Enumeration of inputs used in testing.
	 * @author Daniel Bluhm
	 */
	@AllArgsConstructor
	private static enum Inputs implements JavaFileObjectResource{
		HELLO_WORLD("HelloWorld.java"),
		NAME_MISSING("DataElementNameMissing.java"),
		ON_ENUM("DataElementOnEnum.java"),
		ON_INTERFACE("DataElementOnInterface.java"),
		NO_DATAFIELDS("NoDataFields.java"),
		SINGLE("Single.java"),
		MANY("Many.java"),
		SINGLE_NON_PRIMITIVE("SingleNonPrimitive.java"),
		MANY_NON_PRIMITIVE("ManyNonPrimitive.java"),
		ACCESSIBILITY_PRESERVED("AccessibilityPreserved.java"),
		DATAFIELD_ON_CLASS("DataFieldOnClass.java"),
		DATAFIELD_ON_METHOD("DataFieldOnMethod.java"),
		DATAFIELD_GETTER("Getter.java"),
		DATAFIELD_SETTER("Setter.java"),
		DATAFIELD_MATCH("Match.java"),
		DEFAULT_NON_STRING("DefaultNonString.java"),
		DEFAULT_STRING("DefaultString.java");

		/**
		 * Parent directory of inputs. Prepended to all paths.
		 */
		private static final String PARENT = "input/DataElement/";

		/**
		 * Path to inputs.
		 */
		private String filename;

		@Override
		public String getPath() {
			return PARENT + this.filename;
		}
	}

	/**
	 * Enumeration of patterns used in testing.
	 * @author Daniel Bluhm
	 */
	@AllArgsConstructor
	private static enum Patterns implements JavaFileObjectResource {
		DEFAULTS_INT("Defaults.java"),
		DEFAULTS_IMPL("DefaultsImplementation.java"),
		SINGLE_INT("Single.java"),
		SINGLE_IMPL("SingleImplementation.java"),
		MANY_INT("Many.java"),
		MANY_IMPL("ManyImplementation.java"),
		SINGLE_NON_PRIMITIVE_INT("SingleNonPrimitive.java"),
		SINGLE_NON_PRIMITIVE_IMPL("SingleNonPrimitiveImplementation.java"),
		MANY_NON_PRIMITIVE_INT("ManyNonPrimitive.java"),
		MANY_NON_PRIMITIVE_IMPL("ManyNonPrimitiveImplementation.java"),
		ACCESSIBILITY_PRESERVED("AccessibilityPreserved.java"),
		DATAFIELD_GETTER_INT("Getter.java"),
		DATAFIELD_SETTER_INT("Setter.java"),
		DEFAULT_NON_STRING_IMPL("DefaultNonStringImplementation.java"),
		DEFAULT_STRING_IMPL("DefaultStringImplementation.java");

		/**
		 * Parent directory of inputs. Prepended to all paths.
		 */
		private static final String PARENT = "patterns/DataElement/";

		/**
		 * Path to inputs.
		 */
		private String filename;

		@Override
		public String getPath() {
			return PARENT + this.filename;
		}
	}

	/**

	 * Assert that the interface generated in this compilation matches the given
	 * pattern.
	 * @param compilation about which the assertion is made
	 * @param inter interface pattern
	 */
	private static void assertInterfaceMatches(Compilation compilation, JavaFileObject inter) {
		assertThat(compilation)
			.generatedSourceFile(INTERFACE)
			.containsElementsIn(inter);
	}

	/**
	 * Assert that the implementation generated in this compilation matches the
	 * given pattern.
	 * @param compilation about which the assertion is made
	 * @param impl implementation pattern
	 */
	private static void assertImplementationMatches(
		Compilation compilation,
		JavaFileObject impl
	) {
		assertThat(compilation)
			.generatedSourceFile(IMPLEMENTATION)
			.containsElementsIn(impl);
	}

	/**
	 * Assert that the default fields were generated.
	 * @param compilation about which the assertion is made
	 */
	private static void assertDefaultsPresent(Compilation compilation) {
		assertInterfaceMatches(compilation, Patterns.DEFAULTS_INT.get());
		assertImplementationMatches(compilation, Patterns.DEFAULTS_IMPL.get());
	}

	/**
	 * Test that a class not annotated with {@code @DataElement} does not cause any
	 * errors.
	 */
	@Test
	void testNoAnnotationsToProcessSucceeds() {
		Compilation compilation = helper.compile(Inputs.HELLO_WORLD.get());
		assertThat(compilation).succeeded();
	}

	/**
	 * Test that omitting the name from the {@code @DataElement} annotation causes
	 * an error.
	 */
	@Test
	void testMissingNameFails() {
		Compilation compilation = helper.compile(Inputs.NAME_MISSING.get());
		assertThat(compilation)
			.hadErrorContaining(
				"missing a default value for the element 'name'"
			);
	}

	/**
	 * Test that annotating an interface with {@code @DataElement} causes an error.
	 */
	@Test
	void testAnnotateInterfaceFails() {
		Compilation compilation = helper.compile(Inputs.ON_INTERFACE.get());
		assertThat(compilation)
			.hadErrorContaining("DataElementSpec must be class");
	}

	/**
	 * Test that annotating an enum with {@code @DataElement} causes an error.
	 */
	@Test
	void testAnnotateEnumFails() {
		Compilation compilation = helper.compile(Inputs.ON_ENUM.get());
		assertThat(compilation)
			.hadErrorContaining("DataElementSpec must be class");
	}

	/**
	 * Test that omitting any additional DataFields will result in at least the
	 * default fields.
	 */
	@Test
	void testNoDataFieldsSucceeds() {
		Compilation compilation = helper.compile(Inputs.NO_DATAFIELDS.get());
		/*
		 * System.out.println(
		 * ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		 * System.out.println(assertThat(compilation) .generatedSourceFile(INTERFACE)
		 * .contentsAsUtf8String()); System.out.println(
		 * "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		 */
		
		assertDefaultsPresent(compilation);
	}

	/**
	 * Test that a single DataField generates as expected.
	 * @throws IOException 
	 */
	@Test
	void testWithSingleDataFieldSucceeds() {
		Compilation compilation = helper.compile(Inputs.SINGLE.get());
		assertDefaultsPresent(compilation);
		assertInterfaceMatches(compilation, Patterns.SINGLE_INT.get());
		assertImplementationMatches(compilation, Patterns.SINGLE_IMPL.get());
	}

	/**
	 * Test that many DataFields generate as expected.
	 */
	@Test
	void testWithManyDataFieldsSucceeds() {
		Compilation compilation = helper.compile(Inputs.MANY.get());
		assertDefaultsPresent(compilation);
		assertInterfaceMatches(compilation, Patterns.MANY_INT.get());
		assertImplementationMatches(compilation, Patterns.MANY_IMPL.get());
	}

	/**
	 * Test that a single Non-primitive DataField generates as expected.
	 */
	@Test
	void testSingleNonPrimitiveDataFieldSucceeds() {
		Compilation compilation = helper.compile(Inputs.SINGLE_NON_PRIMITIVE.get());
		assertDefaultsPresent(compilation);
		assertInterfaceMatches(compilation, Patterns.SINGLE_NON_PRIMITIVE_INT.get());
		assertImplementationMatches(compilation, Patterns.SINGLE_NON_PRIMITIVE_IMPL.get());
	}

	/**
	 * Test that many non-primitive DataFields generate as expected.
	 */
	@Test
	void testManyNonPrimitiveDataFieldSucceeds() {
		Compilation compilation = helper.compile(Inputs.MANY_NON_PRIMITIVE.get());
		assertDefaultsPresent(compilation);
		assertInterfaceMatches(compilation, Patterns.MANY_NON_PRIMITIVE_INT.get());
		assertImplementationMatches(compilation, Patterns.MANY_NON_PRIMITIVE_IMPL.get());
	}

	/**
	 * Test that Doc Comments are preserved on elements annotated with
	 * {@code @DataField}.
	 */
	@Test
	void testDocStringsPreserved() {
		Compilation compilation = helper.compile(Inputs.SINGLE.get());
		assertThat(compilation).generatedSourceFile(IMPLEMENTATION)
			.contentsAsUtf8String()
			.contains("* A UNIQUE STRING IN THE DOC STRING.");
		assertThat(compilation).generatedSourceFile(IMPLEMENTATION)
			.contentsAsUtf8String()
			.contains("* AND ANOTHER ON A NEW LINE.");
	}

	/**
	 * Test that the accessiblity level is preserved on elements annotated with
	 * {@code @DataField}.
	 */
	@Test
	void testAccessibilityPreserved() {
		Compilation compilation = helper.compile(Inputs.ACCESSIBILITY_PRESERVED.get());
		assertImplementationMatches(compilation, Patterns.ACCESSIBILITY_PRESERVED.get());
	}

	/**
	 * Test that annotating a class with {@code @DataField} fails.
	 *
	 * This should be enough to also ensure that it will fail for other types (enum,
	 * interface, etc.).
	 */
	@Test
	void testDataFieldOnClassFails() {
		Compilation compilation = helper.compile(Inputs.DATAFIELD_ON_CLASS.get());
		assertThat(compilation)
			.hadErrorContaining("annotation type not applicable");
	}

	/**
	 * Test that annotating a class method with {@code @DataField} fails.
	 */
	@Test
	void testDataFieldOnMethodFails() {
		Compilation compilation = helper.compile(Inputs.DATAFIELD_ON_METHOD.get());
		assertThat(compilation)
			.hadErrorContaining("annotation type not applicable");
	}

	/**
	 * Test DataField Getter option.
	 */
	@Test
	void testDataFieldGetterOption() {
		Compilation compilation = helper.compile(Inputs.DATAFIELD_GETTER.get());
		assertThat(compilation)
			.generatedSourceFile(INTERFACE)
			.hasSourceEquivalentTo(Patterns.DATAFIELD_GETTER_INT.get());
	}

	/**
	 * Test DataField Setter option.
	 */
	@Test
	void testDataFieldSetterOption() {
		Compilation compilation = helper.compile(Inputs.DATAFIELD_SETTER.get());
		assertThat(compilation)
			.generatedSourceFile(INTERFACE)
			.hasSourceEquivalentTo(Patterns.DATAFIELD_SETTER_INT.get());
	}

	/**
	 * Test DataField Match option.
	 */
	@Test
	void testDataFieldMatchOption() {
		Compilation compilation = helper.compile(Inputs.DATAFIELD_MATCH.get());
		assertThat(compilation)
			.generatedSourceFile(IMPLEMENTATION)
			.contentsAsUtf8String()
			.contains("Matching toBeMatched");
		assertThat(compilation)
			.generatedSourceFile(IMPLEMENTATION)
			.contentsAsUtf8String()
			.doesNotContain("Matching toNotBeMatched");
	}

	/**
	 * Test DataField.Default generation for Non-String values.
	 */
	@Test
	void testDataFieldDefaultNonString() {
		Compilation compilation = helper.compile(Inputs.DEFAULT_NON_STRING.get());
		assertImplementationMatches(
			compilation,
			Patterns.DEFAULT_NON_STRING_IMPL.get()
		);
	}

	/**
	 * Test DataField.Default generation for String values.
	 */
	@Test
	void testDataFieldDefaultString() {
		Compilation compilation = helper.compile(Inputs.DEFAULT_STRING.get());
		assertImplementationMatches(
			compilation,
			Patterns.DEFAULT_STRING_IMPL.get()
		);
	}

	// TODO rework DataFieldJson? Merge into DataElement Annotation?
//	/**
//	 * Test DataFieldJson annotation.
//	 */
//	@Test
//	void testDataFieldJson() {
//		fail("DataFieldJson not yet implemented");
//	}
}
