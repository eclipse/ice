package org.eclipse.ice.tests.dev.annotations.processors;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.google.testing.compile.Compiler.*;
import static com.google.testing.compile.CompilationSubject.*;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

import org.eclipse.ice.dev.annotations.processors.DataElementProcessor;
import org.junit.jupiter.api.Test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;

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
	private static enum Inputs {
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
		private String path;

		/**
		 * Retrieve the JavaFileObject corresponding to this input.
		 * @return input as a JavaFileObject
		 */
		public JavaFileObject get() {
			return JavaFileObjects.forResource(PARENT + this.path);
		}
	}

	/**
	 * Enumeration of patterns used in testing.
	 * @author Daniel Bluhm
	 */
	@AllArgsConstructor
	private static enum Patterns {
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
		private String path;

		/**
		 * Retrieve the JavaFileObject corresponding to this pattern.
		 * @return input as a JavaFileObject
		 */
		public JavaFileObject get() {
			return JavaFileObjects.forResource(PARENT + this.path);
		}
	}

	/**
	 * Retrieve an instance of Lombok's Annotation Processor.
	 *
	 * This is a nasty method that violates the accessibility of the Processor by
	 * reflection but is necessary to correctly process and test the generated code.
	 * @return lombok annotation processor
	 */
	private static Processor getLombokAnnotationProcessor() {
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
	private static Compilation compile(JavaFileObject... sources) {
		return javac()
			.withProcessors(
				getLombokAnnotationProcessor(),
				new DataElementProcessor()
			).compile(sources);
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
		Compilation compilation = compile(Inputs.HELLO_WORLD.get());
		assertThat(compilation).succeeded();
	}

	/**
	 * Test that omitting the name from the {@code @DataElement} annotation causes
	 * an error.
	 */
	@Test
	void testMissingNameFails() {
		Compilation compilation = compile(Inputs.NAME_MISSING.get());
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
		Compilation compilation = compile(Inputs.ON_INTERFACE.get());
		assertThat(compilation)
			.hadErrorContaining("DataElementSpec must be class");
	}

	/**
	 * Test that annotating an enum with {@code @DataElement} causes an error.
	 */
	@Test
	void testAnnotateEnumFails() {
		Compilation compilation = compile(Inputs.ON_ENUM.get());
		assertThat(compilation)
			.hadErrorContaining("DataElementSpec must be class");
	}

	/**
	 * Test that omitting any additional DataFields will result in at least the
	 * default fields.
	 */
	@Test
	void testNoDataFieldsSucceeds() {
		Compilation compilation = compile(Inputs.NO_DATAFIELDS.get());
		assertDefaultsPresent(compilation);
	}

	/**
	 * Test that a single DataField generates as expected.
	 */
	@Test
	void testWithSingleDataFieldSucceeds() {
		Compilation compilation = compile(Inputs.SINGLE.get());
		assertDefaultsPresent(compilation);
		assertInterfaceMatches(compilation, Patterns.SINGLE_INT.get());
		assertImplementationMatches(compilation, Patterns.SINGLE_IMPL.get());
	}

	/**
	 * Test that many DataFields generate as expected.
	 */
	@Test
	void testWithManyDataFieldsSucceeds() {
		Compilation compilation = compile(Inputs.MANY.get());
		assertDefaultsPresent(compilation);
		assertInterfaceMatches(compilation, Patterns.MANY_INT.get());
		assertImplementationMatches(compilation, Patterns.MANY_IMPL.get());
	}

	/**
	 * Test that a single Non-primitive DataField generates as expected.
	 */
	@Test
	void testSingleNonPrimitiveDataFieldSucceeds() {
		Compilation compilation = compile(Inputs.SINGLE_NON_PRIMITIVE.get());
		assertDefaultsPresent(compilation);
		assertInterfaceMatches(compilation, Patterns.SINGLE_NON_PRIMITIVE_INT.get());
		assertImplementationMatches(compilation, Patterns.SINGLE_NON_PRIMITIVE_IMPL.get());
	}

	/**
	 * Test that many non-primitive DataFields generate as expected.
	 */
	@Test
	void testManyNonPrimitiveDataFieldSucceeds() {
		Compilation compilation = compile(Inputs.MANY_NON_PRIMITIVE.get());
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
		Compilation compilation = compile(Inputs.SINGLE.get());
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
		Compilation compilation = compile(Inputs.ACCESSIBILITY_PRESERVED.get());
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
		Compilation compilation = compile(Inputs.DATAFIELD_ON_CLASS.get());
		assertThat(compilation)
			.hadErrorContaining("annotation type not applicable");
	}

	/**
	 * Test that annotating a class method with {@code @DataField} fails.
	 */
	@Test
	void testDataFieldOnMethodFails() {
		Compilation compilation = compile(Inputs.DATAFIELD_ON_METHOD.get());
		assertThat(compilation)
			.hadErrorContaining("annotation type not applicable");
	}

	/**
	 * Test DataField Getter option.
	 */
	@Test
	void testDataFieldGetterOption() {
		Compilation compilation = compile(Inputs.DATAFIELD_GETTER.get());
		assertThat(compilation)
			.generatedSourceFile(INTERFACE)
			.hasSourceEquivalentTo(Patterns.DATAFIELD_GETTER_INT.get());
	}

	/**
	 * Test DataField Setter option.
	 */
	@Test
	void testDataFieldSetterOption() {
		Compilation compilation = compile(Inputs.DATAFIELD_SETTER.get());
		assertThat(compilation)
			.generatedSourceFile(INTERFACE)
			.hasSourceEquivalentTo(Patterns.DATAFIELD_SETTER_INT.get());
	}

	/**
	 * Test DataField Match option.
	 */
	@Test
	void testDataFieldMatchOption() {
		Compilation compilation = compile(Inputs.DATAFIELD_MATCH.get());
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
		Compilation compilation = compile(Inputs.DEFAULT_NON_STRING.get());
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
		Compilation compilation = compile(Inputs.DEFAULT_STRING.get());
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
