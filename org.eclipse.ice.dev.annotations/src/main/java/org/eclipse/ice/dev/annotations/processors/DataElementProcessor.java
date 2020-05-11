package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.DataFields;

import com.google.auto.service.AutoService;

/**
 * Processor for DataElement Annotations.
 *
 * This will generate an implementation for an interface annotated with
 * DataElement, populating the implementation with metadata and fields specified
 * with the DataField annotation.
 */
@SupportedAnnotationTypes("org.eclipse.ice.dev.annotations.DataElement")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DataElementProcessor extends AbstractProcessor {

	/**
	 * Visitor that accumulates DataField information from AnnotationValues. This
	 * Visitor is only intended for use on the AnnotationValues of DataFields
	 * AnnotationMirrors.
	 */
	private class DataFieldsVisitor extends SimpleAnnotationValueVisitor8<Optional<UnexpectedValueError>, Fields> {

		/**
		 * Return error as default action for unhandled annotation values.
		 */
		@Override
		protected Optional<UnexpectedValueError> defaultAction(final Object o, final Fields f) {
			return Optional.of(
				new UnexpectedValueError(
					"An unexpected annotation value was encountered: " + o.getClass().getCanonicalName()
				)
			);
		}

		/**
		 * Visit DataField AnnotationMirror.
		 */
		@Override
		public Optional<UnexpectedValueError> visitAnnotation(final AnnotationMirror a, final Fields f) {
			if (!a.getAnnotationType().toString().equals(DataField.class.getCanonicalName())) {
				return Optional.of(
					new UnexpectedValueError(
						"Found AnnotationMirror not of type DataField"
					)
				);
			}

			for (final AnnotationValue value : getAnnotationValuesForMirror(a)) {
				final Optional<UnexpectedValueError> result = value.accept(fieldVisitor, f);
				if (result.isPresent()) {
					return result;
				}
			}
			return Optional.empty();
		}

		/**
		 * Visit DataFields value (array of DataField AnnotationMirrors).
		 */
		@Override
		public Optional<UnexpectedValueError> visitArray(final List<? extends AnnotationValue> vals, final Fields f) {
			for (final AnnotationValue val : vals) {
				final Optional<UnexpectedValueError> result = val.accept(this, f);
				if (result.isPresent()) {
					return result;
				}
			}
			return Optional.empty();
		}
	}

	/**
	 * Visitor that accumulates DataField information from AnnotationValues. This
	 * Visitor is only intended for use on the AnnotationValues of DataField
	 * AnnotationMirrors.
	 */
	private class DataFieldVisitor extends SimpleAnnotationValueVisitor8<Optional<UnexpectedValueError>, Fields> {

		/**
		 * Return error as default action for unhandled annotation values.
		 */
		@Override
		protected Optional<UnexpectedValueError> defaultAction(final Object o, final Fields f) {
			return Optional.of(
				new UnexpectedValueError(
					"An unexpected annotation value was encountered: " + o.getClass().getCanonicalName()
				)
			);
		}

		/**
		 * Visit DataField.fieldName.
		 */
		@Override
		public Optional<UnexpectedValueError> visitString(final String s, final Fields f) {
			if (!f.isBuilding()) {
				f.begin();
			}
			f.setName(s);
			if (f.isComplete()) {
				f.finish();
			}
			return Optional.empty();
		}

		/**
		 * Visit DataField.fieldType.
		 */
		@Override
		public Optional<UnexpectedValueError> visitType(final TypeMirror t, final Fields f) {
			if (!f.isBuilding()) {
				f.begin();
			}
			f.setClassName(t.toString());
			if (f.isComplete()) {
				f.finish();
			}
			return Optional.empty();
		}
	}

	public static class Field {
		String name;
		String className;

		public String getClassName() {
			return className;
		}

		public String getName() {
			return name;
		}

		public void setClassName(final String className) {
			this.className = className;
		}

		public void setName(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Field (name=" + name + ", className=" + className + ")";
		}
	}

	/**
	 * A simple container for fields discovered on a DataElement.
	 */
	private static class Fields {
		/**
		 * Container for name and class name attributes of DataField in string
		 * representation.
		 */

		protected List<Field> fields;
		protected Field building;

		public Fields() {
			this.fields = new ArrayList<>();
			this.building = null;
		}

		public void begin() {
			this.building = new Field();
		}

		public void finish() {
			this.fields.add(this.building);
			this.building = null;
		}

		public List<Field> getFields() {
			return fields;
		}

		public boolean isBuilding() {
			return this.building != null;
		}

		public boolean isComplete() {
			return (this.building.getClassName() != null) && (this.building.getName() != null);
		}

		public void setClassName(final String className) {
			this.building.setClassName(className);
		}

		public void setName(final String name) {
			this.building.setName(name);
		}

		@Override
		public String toString() {
			return fields.toString();
		}
	}

	public static class UnexpectedValueError extends Exception {
		private static final long serialVersionUID = -8486833574190525020L;

		public UnexpectedValueError(final String message) {
			super(message);
		}
	}

	/**
	 * Location of DataElement template for use with velocity.
	 */
	private static final String template = "templates/DataElement.vm";

	/**
	 * Return stack trace as string.
	 * @param e subject exception
	 * @return stack trace as string
	 */
	private static String stackTracetoString(final Throwable e) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	/**
	 * Unwrap an optional exception.
	 * @param <T> Exception type
	 * @param e Optional exception to throw if present
	 * @throws T
	 */
	private static <T extends Throwable> void unwrap(final Optional<T> e) throws T {
		if (e.isPresent()) {
			throw e.get();
		}
	}

	protected Messager messager;
	protected Elements elementUtils;
	protected DataFieldsVisitor fieldsVisitor;
	protected DataFieldVisitor fieldVisitor;

	/**
	 * Get a list of annotation values from an annotation mirror.
	 * @param mirror the mirror from which to grab values.
	 * @return list of AnnotationValue
	 */
	private List<AnnotationValue> getAnnotationValuesForMirror(final AnnotationMirror mirror) {
		final Map<? extends ExecutableElement, ? extends AnnotationValue> values = elementUtils
			.getElementValuesWithDefaults(mirror);
		return values.entrySet().stream()
			.map(entry -> entry.getValue())
			.collect(Collectors.toList());
	}

	@Override
	public void init(final ProcessingEnvironment env) {
		messager = env.getMessager();
		elementUtils = env.getElementUtils();
		fieldsVisitor = new DataFieldsVisitor();
		fieldVisitor = new DataFieldVisitor();
		super.init(env);
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		for (final Element elem : roundEnv.getElementsAnnotatedWith(DataElement.class)) {
			if (!elem.getKind().isInterface()) {
				messager.printMessage(
					Diagnostic.Kind.ERROR,
					"DataElement annotation can only be applied to interfaces"
				);
				return false;
			}
			final Fields fields = new Fields();
			final List<? extends AnnotationMirror> mirrors = elem.getAnnotationMirrors();
			try {
				for (
					final AnnotationValue value : mirrors.stream()
						.filter(mirror -> mirror.getAnnotationType().toString().equals(
							DataFields.class.getCanonicalName()
						))
						.map(mirror -> getAnnotationValuesForMirror(mirror))
						.flatMap(List::stream)
						.collect(Collectors.toList())
				) {
					unwrap(value.accept(fieldsVisitor, fields));
				}
				for (
					final AnnotationValue value : mirrors.stream()
						.filter( mirror -> mirror.getAnnotationType().toString().equals(
							DataField.class.getCanonicalName()
						))
						.map(mirror -> getAnnotationValuesForMirror(mirror))
						.flatMap(List::stream)
						.collect(Collectors.toList())
				) {
					unwrap(value.accept(fieldVisitor, fields));
				}
				this.writeClass(((TypeElement) elem).getQualifiedName().toString(), fields);
			} catch (final IOException | UnexpectedValueError e) {
				messager.printMessage(Diagnostic.Kind.ERROR, stackTracetoString(e));
				return false;
			}
		}
		return true;
	}

	/**
	 * Write the implementation of DataElement annotated class to file.
	 * @param className the annotated class name
	 * @param fields the fields extracted from DataField annotations on class
	 * @throws IOException
	 */
	private void writeClass(final String className, final Fields fields) throws IOException {
		String packageName = null;
		final int lastDot = className.lastIndexOf('.');
		if (lastDot > 0) {
			packageName = className.substring(0, lastDot);
		}

		final String interfaceName = className.substring(lastDot + 1);
		final String generatedClassName = className + "Implementation";
		final String generatedSimpleClassName = generatedClassName.substring(lastDot + 1);

		final Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty(
			"class.resource.loader.class",
			"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader"
		);
		Velocity.init(p);
		final VelocityContext context = new VelocityContext();
		context.put("package", packageName);
		context.put("interface", interfaceName);
		context.put("class", generatedSimpleClassName);
		context.put("fields", fields.getFields());
		final JavaFileObject generatedClassFile = processingEnv.getFiler().createSourceFile(generatedClassName);
		try (Writer writer = generatedClassFile.openWriter()) {
			Velocity.mergeTemplate(template, "UTF-8", context, writer);
		}
	}
}
