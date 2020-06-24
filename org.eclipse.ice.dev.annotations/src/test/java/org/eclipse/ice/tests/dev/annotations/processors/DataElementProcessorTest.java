package org.eclipse.ice.tests.dev.annotations.processors;

import static org.junit.jupiter.api.Assertions.*;

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

class DataElementProcessorTest {

	private static final String INTERFACE = "Test";
	private static final String IMPLEMENTATION = "TestImplementation";

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
		ACCESSIBILITY_PRESERVED("AccessibilityPreserved.java");

		private static final String PARENT = "input/";
		private String path;
		public JavaFileObject get() {
			return JavaFileObjects.forResource(PARENT + this.path);
		}
	}

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
		ACCESSIBILITY_PRESERVED("AccessibilityPreserved.java");

		private static final String PARENT = "patterns/";
		private String path;
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
		} catch (ClassNotFoundException | InstantiationException |
			IllegalAccessException | IllegalArgumentException |
			InvocationTargetException | NoSuchMethodException |
			SecurityException e) {
			System.err.println("Failed to get Lombok AnnotationProcessor!");
			e.printStackTrace();
		}
		return p;
	}

	private static Compilation compile(JavaFileObject... sources) {
		return javac()
			.withProcessors(
				getLombokAnnotationProcessor(),
				new DataElementProcessor()
			).compile(sources);
	}

	private static void assertInterfaceMatches(Compilation compilation, JavaFileObject inter) {
		assertThat(compilation)
			.generatedSourceFile(INTERFACE)
			.containsElementsIn(inter);
	}

	private static void assertImplementationMatches(Compilation compilation, JavaFileObject impl) {
		assertThat(compilation)
			.generatedSourceFile(IMPLEMENTATION)
			.containsElementsIn(impl);
	}

	private static void assertDefaultsPresent(Compilation compilation) {
		assertInterfaceMatches(compilation, Patterns.DEFAULTS_INT.get());
		assertImplementationMatches(compilation, Patterns.DEFAULTS_IMPL.get());
	}

	@Test
	void testNoAnnotationsToProcessSucceeds() {
		Compilation compilation = compile(Inputs.HELLO_WORLD.get());
		assertThat(compilation).succeeded();
	}

	@Test
	void testMissingNameFails() {
		Compilation compilation = compile(Inputs.NAME_MISSING.get());
		assertThat(compilation)
			.hadErrorContaining(
				"missing a default value for the element 'name'"
			);
	}

	@Test
	void testAnnotateInterfaceFails() {
		Compilation compilation = compile(Inputs.ON_INTERFACE.get());
		assertThat(compilation)
			.hadErrorContaining("DataElementSpec must be class");
	}

	@Test
	void testAnnotateEnumFails() {
		Compilation compilation = compile(Inputs.ON_ENUM.get());
		assertThat(compilation)
			.hadErrorContaining("DataElementSpec must be class");
	}

	@Test
	void testNoDataFieldsSucceeds() {
		Compilation compilation = compile(Inputs.NO_DATAFIELDS.get());
		assertDefaultsPresent(compilation);
	}

	@Test
	void testWithSingleDataFieldSucceeds() {
		Compilation compilation = compile(Inputs.SINGLE.get());
		assertDefaultsPresent(compilation);
		assertInterfaceMatches(compilation, Patterns.SINGLE_INT.get());
		assertImplementationMatches(compilation, Patterns.SINGLE_IMPL.get());
	}

	@Test
	void testWithManyDataFieldsSucceeds() {
		Compilation compilation = compile(Inputs.MANY.get());
		assertDefaultsPresent(compilation);
		assertInterfaceMatches(compilation, Patterns.MANY_INT.get());
		assertImplementationMatches(compilation, Patterns.MANY_IMPL.get());
	}

	@Test
	void testSingleNonPrimitiveDataFieldSucceeds() {
		Compilation compilation = compile(Inputs.SINGLE_NON_PRIMITIVE.get());
		assertDefaultsPresent(compilation);
		assertInterfaceMatches(compilation, Patterns.SINGLE_NON_PRIMITIVE_INT.get());
		assertImplementationMatches(compilation, Patterns.SINGLE_NON_PRIMITIVE_IMPL.get());
	}

	@Test
	void testManyNonPrimitiveDataFieldSucceeds() {
		Compilation compilation = compile(Inputs.MANY_NON_PRIMITIVE.get());
		assertDefaultsPresent(compilation);
		assertInterfaceMatches(compilation, Patterns.MANY_NON_PRIMITIVE_INT.get());
		assertImplementationMatches(compilation, Patterns.MANY_NON_PRIMITIVE_IMPL.get());
	}

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

	@Test
	void testAccessibilityPreserved() {
		Compilation compilation = compile(Inputs.ACCESSIBILITY_PRESERVED.get());
		assertImplementationMatches(compilation, Patterns.ACCESSIBILITY_PRESERVED.get());
	}
}
