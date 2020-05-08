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

	public class UnexpectedValueError extends Exception {
		private static final long serialVersionUID = -8486833574190525020L;

		public UnexpectedValueError(String message) {
			super(message);
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
		public class Field {
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

		protected List<Field> fields;
		protected Field building;

		public Fields() {
			this.fields = new ArrayList<>();
			this.building = null;
		}

		public boolean isBuilding() {
			return this.building != null;
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

	/**
	 * Visitor that accumulates DataField information from AnnotationValues.
	 * This Visitor is only intended for use on the AnnotationValues of
	 * DataFields and DataField AnnotationMirrors.
	 */
	private class DataFieldsVisitor extends SimpleAnnotationValueVisitor8<Optional<Exception>, Fields> {

		/**
		 * Return error as default action for unhandled annotation values.
		 */
		@Override
		protected Optional<Exception> defaultAction(final Object o, final Fields f) {
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
		public Optional<Exception> visitAnnotation(final AnnotationMirror a, final Fields f) {
			if (!a.getAnnotationType().toString().equals(DataField.class.getCanonicalName())) {
				return Optional.of( new UnexpectedValueError(
					"Found AnnotationMirror not of type DataField"
				));
			}

			f.begin();
			for (AnnotationValue value : getAnnotationValuesForMirror(a)) {
				Optional<Exception> result = value.accept(this, f);
				if (result.isPresent()) {
					return result;
				}
			}
			f.finish();
			return Optional.empty();
		}

		/**
		 * Visit DataFields.value array of DataField.
		 */
		@Override
		public Optional<Exception> visitArray(final List<? extends AnnotationValue> vals, final Fields f) {
			for (final AnnotationValue val : vals) {
				Optional<Exception> result = val.accept(this, f);
				if (result.isPresent()) {
					return result;
				}
			}
			return Optional.empty();
		}

		/**
		 * Visit DataField.fieldName.
		 */
		@Override
		public Optional<Exception> visitString(final String s, final Fields f) {
			if (!f.isBuilding()) {
				return Optional.of(
					new UnexpectedValueError(
						"Found String while still expecting DataField AnnotationMirror"
					)
				);
			}
			f.setName(s);
			return Optional.empty();
		}

		/**
		 * Visit DataField.fieldType.
		 */
		@Override
		public Optional<Exception> visitType(final TypeMirror t, final Fields f) {
			if (!f.isBuilding()) {
				return Optional.of(
					new UnexpectedValueError(
						"Found type while still expecting DataField Annotation Mirror"
					)
				);
			}
			f.setClassName(t.toString());
			return Optional.empty();
		}
	}

	/**
	 * Location of DataElement template for use with velocity.
	 */
	private static final String template = "templates/DataElement.vm";

	protected Messager messager;
	protected Elements elementUtils;
	protected DataFieldsVisitor fieldVisitor;

	@Override
	public void init(final ProcessingEnvironment env) {
		messager = env.getMessager();
		elementUtils = env.getElementUtils();
		fieldVisitor = new DataFieldsVisitor();
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
			final List<? extends AnnotationValue> fieldAnnotationValues = elem.getAnnotationMirrors().stream()
				.filter(
					mirror -> mirror.getAnnotationType().toString().equals(
						DataFields.class.getCanonicalName()
					)
				)
				.map(mirror -> getAnnotationValuesForMirror(mirror))
				.flatMap(List::stream)
				.collect(Collectors.toList());
			for (AnnotationValue value : fieldAnnotationValues) {
				Optional<Exception> result = value.accept(fieldVisitor, fields);
				if (result.isPresent()) {
					messager.printMessage(Diagnostic.Kind.ERROR, stackTracetoString(result.get()));
					return false;
				}
			}
			try {
				this.writeClass(((TypeElement) elem).getQualifiedName().toString(), fields);
			} catch (final IOException e) {
				messager.printMessage(Diagnostic.Kind.ERROR, stackTracetoString(e));
				return false;
			}
		}
		return true;
	}

	/**
	 * Return stack trace as string.
	 * @param e subject exception
	 * @return stack trace as string
	 */
	private String stackTracetoString(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

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
