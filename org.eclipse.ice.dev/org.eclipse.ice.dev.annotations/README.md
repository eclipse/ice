Eclipse ICE Annotations
=======================

> TODO Intro to annotations included in this bundle

## Annotations
### Data Elements

> TODO Data Element Annotation walkthrough, what is generated, examples

### Persistence

> TODO Persistence Annotation walkthrough, what is generated, examples

## Creating New Annotations

Eclipse ICE Dev Annotations provides tools and interfaces to split Annotation
Processing into two phases: (1) metadata extraction from the element and
annotations in process and (2) file generation.

Currently, using these tools the two phases are occurring manually and the flow
through the "pipeline" is conceptual. This leaves the door open for implementing
an actual pipeline in future revisions.

As an example, suppose we would like to create an annotation that writes some
helpful information to a generated file. Let's call it `@Debug`. Such an
annotation's definition might look something like the following:

```java
/**
 * Simple annotation for printing metadata about the annotated element.
 */
@Retention(SOURCE)
@Target({
	TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE
})
public @interface Debug {
	/**
	 * Value prepended to printed metadata.
	 * @return prefix.
	 */
	public String value() default "Debug:";
}
```

### Phase 1: Metadata Extraction

Metadata extraction occurs through the use of classes extending the
`AbstractAnnotationExtractor<T>` abstract class. Classes extending it must meet
the `AnnotationExtractor<T>` interface which defines the `extract` method:

```java
/**
 * Extract information from element and annotations found on or within
 * element. The subclass of element is dependent on the annotation and
 * implementation of the extractor.
 * @param element from which information will be extracted.
 * @return extracted information
 * @throws InvalidElementException if element is not annotated as expected
 *         for this annotation extractor.
 */
public T extract(Element element) throws InvalidElementException;
```

In this interface, the type parameter `T` is the type of the metadata returned
by the Extractor. The `AbstractAnnotationExtractor` abstract class contains
methods that might be helpful while extracting metadata but it is not strictly
required that Annotation Extractors extend this abstract base class as long as
it implements the `AnnotationExtractor` interface.

When creating a new annotation, you will likely need to define a new
`AnnotationExtractor` and POJO to hold the metadata. Continuing the `@Debug`
example, a `DebugExtractor` might look something like the following:

```java
/**
 * Extract metadata from {@link Debug} annotated elements.
 */
public class DebugExtractor extends AbstractAnnotationExtractor<DebugMetadata> {

	public DebugExtractor(Elements elementUtils) {
		super(elementUtils);
	}

	@Override
	public DebugMetadata extract(Element element) throws InvalidElementException {
		Optional<Debug> debug = getAnnotation(element, Debug.class);
		if (debug.isEmpty()) {
			throw new InvalidElementException(
				"Element is not annotated with @Debug"
			);
		}
		DebugMetadata data = DebugMetadata.builder()
			.prefix(debug.get().value())
			.name(element.getSimpleName().toString())
			.type(element.getKind().toString())
			.annotations(extractAnnotations(element))
			.parentElement(element.getEnclosingElement().getSimpleName().toString())
			.build();
		return data;
	}

	private Set<String> extractAnnotations(Element element) {
		return element.getAnnotationMirrors().stream()
			.map(AnnotationMirror::toString)
			.collect(Collectors.toSet());
	}
}
```

And the corresponding `DebugMetadata` POJO might look something like:

```java
/**
 * Container for extracted metadata associated with the Debug annotation.
 */
@Getter
@Builder
public class DebugMetadata {

	/**
	 * Prefix defined in annotation.
	 */
	private String prefix;

	/**
	 * Name of the element.
	 */
	private String name;

	/**
	 * Type of the element
	 */
	private String type;

	/**
	 * Annotations on the element (excluding Debug).
	 */
	private Set<String> annotations;

	/**
	 * Parent element of the annotated element.
	 */
	private String parentElement;
}
```

### Phase 2: File Generation

To streamline file generation, the `WriterGenerator` interface is used which
defines the `generate` method:

```java
/**
 * Interface for classes that generate one or more file writers from a given
 * set of data.
 *
 * @author Michael Walsh
 * @author Daniel Bluhm
 */
public interface WriterGenerator {

	/**
	 * Generate one or more FileWriters from the passed data.
	 * @return
	 */
	public List<GeneratedFileWriter> generate();
}
```

Writer Generators produce a list of `GeneratedFileWriter`s representing a file
to be generated, ready for writing. All of the current writers are subclasses of
`VelocitySourceWriters` which provide tooling for using Apache Velocity to
generate a file from a template.

For the `@Debug` annotation, the writer generator and generated file writer
might look like this:

```java
public class DebugWriterGenerator implements WriterGenerator {

	/**
	 * Extracted metadata.
	 */
	private DebugMetadata data;

	public DebugWriterGenerator(DebugMetadata data) {
		this.data = data;
	}

	@Override
	public List<GeneratedFileWriter> generate() {
		List<GeneratedFileWriter> writers = List.of(
			new DebugWriter(data)
		);
		return writers;
	}
}
```

```java
public class DebugWriter
	extends VelocitySourceWriter
	implements GeneratedFileWriter
{
	private static final String DEBUG_TEMPLATE = "templates/Debug.vm";
	private static final String DEBUG_KEY = "debug";
	private DebugMetadata data;
	public DebugWriter(DebugMetadata data) {
		super(DEBUG_TEMPLATE);
		this.data = data;
		this.context.put(DEBUG_KEY, data);
	}

	@Override
	public Writer openWriter(Filer filer) throws IOException {
		return filer.createResource(
			StandardLocation.SOURCE_OUTPUT,
			"",
			String.format("debug/%s.debug.txt", data.getName())
		).openWriter();
	}
}
```

### Tying it all together

These elements can be combined in a number of ways. You might add this to an
already existing processor or create a new one. Here's the outline of an
Annotation Processor for the `@Debug` annotation:

```java
package org.eclipse.ice.dev.annotations.processors;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import com.google.auto.service.AutoService;

@SupportedAnnotationTypes({
	"org.eclipse.ice.dev.annotations.Debug"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class DebugProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		// TODO Auto-generated method stub
		return false;
	}
}
```

The simplest way to add the extractors and writers is to just manually wire up
these different pieces:

```java
@Override
public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
	// Setup extractor
	Elements elementUtils = processingEnv.getElementUtils();
	DebugExtractor extractor = new DebugExtractor(elementUtils);
	try {
		for (Element element : roundEnv.getElementsAnnotatedWith(Debug.class)) {
			DebugMetadata debug = extractor.extract(element);
			DebugWriter debugWriter = new DebugWriter(debug);
			try(Writer writer = debugWriter.openWriter(processingEnv.getFiler())) {
				debugWriter.write(writer);
			}
		}
	} catch (IOException | InvalidElementException e) {
		processingEnv.getMessager()
			.printMessage(Kind.ERROR, e.getMessage());
	}
	return false;
}
```

However, it is also likely that we might want to combine multiple extraction
steps and multiple writers with overlapping metadata for different writers. To
simplify this scenario, `FromDataBuilder` (a sort of runtime dependency
injector) can be used:

```java
@Override
public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
	// Setup extractors
	Elements elementUtils = processingEnv.getElementUtils();
	DebugExtractor extractor = new DebugExtractor(elementUtils);

	// Setup FromDataBuilder
	FromDataBuilder<WriterGenerator> builder = new FromDataBuilder<>(
		Set.of(DebugWriterGenerator.class)
	);
	try {
		for (Element element : roundEnv.getElementsAnnotatedWith(Debug.class)) {
			DebugMetadata debug = extractor.extract(element);
			List<GeneratedFileWriter> files = builder.create(debug).stream()
				.flatMap(generators -> generators.generate().stream())
				.collect(Collectors.toList());
			for (GeneratedFileWriter file : files) {
				try (Writer writer = file.openWriter(processingEnv.getFiler())){
					file.write(writer);
				}
			}
		}
	} catch (IOException | InvalidElementException e) {
		processingEnv.getMessager()
			.printMessage(Kind.ERROR, e.getMessage());
	}
	return false;
}
```

For just one extractor and writer, this is almost certainly overkill. But this
setup can be easily combined into a pipeline runner that performs many
extraction steps and writing steps. The exact implementation of such a pipeline
is left as an exercise for the reader (and for future revisions).