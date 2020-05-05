package org.eclipse.ice.dev.annotations.processors;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import javax.tools.Diagnostic;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.DataFields;

import com.google.auto.service.AutoService;

@SupportedAnnotationTypes("org.eclipse.ice.dev.annotations.DataElement")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DataElementProcessor extends AbstractProcessor {

	private static final String template = "templates/DataElement.vm";
	protected Messager messager;

	public class Fields {
		public class Field {
			String name;
			String className;
			public void setName(String name) {
				this.name = name;
			}
			public void setClassName(String className) {
				this.className = className;
			}
			public String getName() {
				return name;
			}
			public String getClassName() {
				return className;
			}
			@Override
			public String toString() {
				return "Field [name=" + name + ", className=" + className + "]";
			}
		}

		protected List<Field> fields;
		protected Field building;

		public Fields() {
			this.fields = new ArrayList<Field>();
			this.building = null;
		}

		public void begin() {
			this.building = new Field();
		}

		public void setName(String name) {
			this.building.setName(name);
		}

		public void setClassName(String className) {
			this.building.setClassName(className);
		}

		public void finish() {
			this.fields.add(this.building);
			this.building = null;
		}

		@Override
		public String toString() {
			return fields.toString();
		}

		public List<Field> getFields() {
			return fields;
		}
	}

	private class FieldVisitor extends SimpleAnnotationValueVisitor8<Void, Fields> {

		protected final Elements elementUtils;

		FieldVisitor(Elements elementUtils) {
			this.elementUtils = elementUtils;
		}

		@Override
		protected Void defaultAction(Object o, Fields f) {
			System.out.println("Default action: " + o.toString());
			return null;
		}

		@Override
		public Void visitString(String s, Fields f) {
			System.out.printf(">> stringValue: %s\n", s);
			f.setName(s);
			return null;
		}

		@Override
		public Void visitAnnotation(AnnotationMirror a, Fields f) {
			System.out.printf(">> annotationTypeValue: %s\n", a.toString());
			if (a.getAnnotationType().toString().equals(DataField.class.getCanonicalName())) {
				final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
						elementUtils.getElementValuesWithDefaults(a);
				f.begin();
				for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
						: elementValues.entrySet()) {
					entry.getValue().accept(this, f);
				}
				f.finish();
			}
			return null;
		}

		@Override
		public Void visitType(TypeMirror t, Fields f) {
			System.out.printf(">> classValue: %s\n", t.toString());
			f.setClassName(t.toString());
			return null;
		}

		@Override
		public Void visitArray(List<? extends AnnotationValue> vals, Fields f) {
			for (AnnotationValue val : vals) {
				val.accept(this, f);
			}
			return null;
		}
	}

	@Override
	public void init(ProcessingEnvironment env) {
		messager = env.getMessager();
		super.init(env);
	}

	private void writeClass(String className, Fields fields) {
		VelocityContext context = new VelocityContext();
		context.put("className", className);
		context.put("fields", fields.getFields());
		FileWriter writer;
		try {
			writer = new FileWriter("/tmp/test.txt");
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty(
				"class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader"
			);
			Velocity.init(p);
			Velocity.mergeTemplate(template, "UTF-8", context, writer);
			writer.flush();
			writer.close();
		} catch (IOException | ParseErrorException | MethodInvocationException | ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		final Elements elementUtils = this.processingEnv.getElementUtils();
		FieldVisitor valueVisitor = new FieldVisitor(elementUtils);

		for (TypeElement annotation : annotations) {
			for (Element elem : roundEnv.getElementsAnnotatedWith(annotation)) {
				if (!elem.getKind().isInterface()) {
					messager.printMessage(Diagnostic.Kind.ERROR, "DataElement annotation is only for interfaces");
					return false;
				}
				Fields fields = new Fields();
				List<? extends AnnotationMirror> annotationMirrors = elem.getAnnotationMirrors();
				for (AnnotationMirror annotationMirror : annotationMirrors) {
					final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
							elementUtils.getElementValuesWithDefaults(annotationMirror);
					for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
							: elementValues.entrySet()) {
						entry.getValue().accept(valueVisitor, fields);
					}
				}
				this.writeClass(elem.getSimpleName().toString(), fields);
			}
		}
		return true;
	}
}
