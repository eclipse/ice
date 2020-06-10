package org.eclipse.ice.dev.annotations.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.DataFieldJson;
import org.eclipse.ice.dev.annotations.DataFields;
import org.eclipse.ice.dev.annotations.Persisted;

import lombok.Getter;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

/**
 * Helpers for working with Elements that form the "root" of a DataElement.
 *
 * @author Daniel Bluhm
 */
public class DataElementRoot {

	/**
	 * The Set of possible annotation classes we expect to see
	 * on DataElements.
	 */
	private final static Set<Class<?>> ANNOTATION_CLASSES = Set.of(
		DataElement.class,
		DataField.class,
		DataFields.class,
		DataFieldJson.class,
		Persisted.class
	);

	/**
	 * The value appended to DataElement implementation class names.
	 */
	private final static String IMPL_SUFFIX = "Implementation";

	/**
	 * The value appended to DataElement Persistence Handler class names.
	 */
	private final static String PERSISTENCE_SUFFIX = "PersistenceHandler";

	/**
	 * List of all annotation mirrors on this element.
	 */
	private List<? extends AnnotationMirror> mirrors;

	/**
	 * Elements used to retrieve defaults for annotation values.
	 */
	private Elements elementUtils;

	/**
	 * The element representing an interface annotated with
	 * <code>@DataElement</code>.
	 */
	private Element element;

	/**
	 * A Map of Annotation Class to AnnotationMirrors on this element.
	 */
	private Map<Class<?>, AnnotationMirror> annotations;

	/**
	 * The fully qualified name of this element.
	 */
	private String fullyQualifiedName;

	/**
	 * The name of the DataElement as extracted from the DataElement annotation.
	 */
	@Getter private String name;

	/**
	 * The package of this element represented as a String.
	 */
	@Getter private String packageName;

	@Getter private String collectionName;

	/**
	 * Construct a DataElementRoot from an Element.
	 * @param elementUtils
	 * @param element
	 * @throws InvalidDataElementRoot
	 */
	public DataElementRoot(Elements elementUtils, Element element) throws InvalidDataElementRoot {
		if (!element.getKind().isInterface()) {
			throw new InvalidDataElementRoot(
				"DataElementRoots must be interface, found "
					+ element.toString()
			);
		}
		this.element = element;
		this.elementUtils = elementUtils;

		// Construct annotations map
		this.annotations = new HashMap<>();
		for (Class<?> cls : ANNOTATION_CLASSES) {
			AnnotationMirror mirror = getAnnotationMirror(cls);
			if (mirror != null) {
				this.annotations.put(cls, mirror);
			}
		}

		// Names
		this.name = this.extractName();
		String elementFQN = ((TypeElement) element).getQualifiedName().toString();
		this.packageName = null;
		final int lastDot = elementFQN.lastIndexOf('.');
		if (lastDot > 0) {
			this.packageName = elementFQN.substring(0, lastDot);
			this.fullyQualifiedName = this.packageName + "." + this.name;
		} else {
			this.fullyQualifiedName = this.name;
		}
		this.collectionName = this.extractCollectionName();
	}

	/**
	 * Return the element name as extracted from the DataElement annotation.
	 * @param element
	 * @return
	 */
	public String extractName() {
		AnnotationValue value = this.getAnnotationValues(DataElement.class)
			.stream()
			.findAny()
			.orElse(null);
		 if (value == null) {
			return null;
		 }
		 return (String) value.getValue();
	}


	/**
	 * Return the collection name as extracted from the Persisted annotation.
	 * @param element
	 * @return
	 */
	public String extractCollectionName() {
		if (!this.hasAnnotation(Persisted.class)) {
			return null;
		}
		AnnotationValue value = this.getAnnotationValues(Persisted.class)
			.stream()
			.findAny()
			.orElse(null);
		 if (value == null) {
			return null;
		 }
		 return (String) value.getValue();
	}


	/**
	 * Get the name of the Implementation to be generated.
	 * @return
	 */
	public String getImplName() {
		return this.name + IMPL_SUFFIX;
	}

	/**
	 * Get the fully qualified name of the Implementation to be generated.
	 * @return
	 */
	public String getQualifiedImplName() {
		return this.fullyQualifiedName + IMPL_SUFFIX;
	}

	/**
	 * Get the name of the Persistence Handler to be generated.
	 * @return
	 */
	public String getPersistenceHandlerName() {
		return this.name + PERSISTENCE_SUFFIX;
	}

	/**
	 * Get the fully qualified name of the Persistence Handler to be generated.
	 * @return
	 */
	public String getQualifiedPersistenceHandlerName() {
		return this.fullyQualifiedName + PERSISTENCE_SUFFIX;
	}

	/**
	 * Find and return annotation of type cls on this element or return null.
	 * @param cls
	 * @return
	 */
	private AnnotationMirror getAnnotationMirror(Class<?> cls) {
		if (this.mirrors == null) {
			this.mirrors = this.element.getAnnotationMirrors();
		}
		return this.mirrors.stream()
			.filter(m -> m.getAnnotationType().toString().equals(cls.getCanonicalName()))
			.findAny()
			.orElse(null);
	}

	/**
	 * Get a map of annotation value names to the value identified by that name.
	 * @param annotation the class of the annotation from which values will be retrieved.
	 * @return Map of String to unwrapped AnnotationValue (Object)
	 */
	public Map<String, Object> getAnnotationValueMap(Class<?> annotation) {
		if (!annotations.containsKey(annotation)) {
			return null;
		}
		final AnnotationMirror mirror = annotations.get(annotation);
		return DataElementRoot.getAnnotationValueMap(elementUtils, mirror);
	}

	/**
	 * Get a map of annotation value names to the value identified by that name.
	 * @param elementUtils
	 * @param mirror
	 * @return Map of String to unwrapped AnnotationValue (Object)
	 */
	public static Map<String, Object> getAnnotationValueMap(Elements elementUtils, AnnotationMirror mirror) {
		return (Map<String, Object>) elementUtils.getElementValuesWithDefaults(mirror).entrySet().stream()
			.collect(Collectors.toMap(
				entry -> entry.getKey().getSimpleName().toString(),
				entry -> entry.getValue().getValue()
			));
	}

	/**
	 * Get a list of annotation values from an annotation mirror of a given type.
	 * @param annotation the class of the annotation from which values will be retrieved.
	 * @return list of AnnotationValue
	 */
	public List<AnnotationValue> getAnnotationValues(Class<?> annotation) {
		if (!annotations.containsKey(annotation)) {
			return null;
		}
		final AnnotationMirror mirror = annotations.get(annotation);
		return elementUtils.getElementValuesWithDefaults(mirror).entrySet().stream()
			.map(entry -> entry.getValue())
			.collect(Collectors.toList());
	}

	/**
	 * Determine if an annotation of a given type decorates this element.
	 * @param cls
	 * @return
	 */
	public boolean hasAnnotation(Class<?> cls) {
		return this.annotations.containsKey(cls);
	}

	/**
	 * Get the AnnotationMirror of a given type if present on the element.
	 * @param cls
	 * @return AnnotationMirror or null if not found
	 */
	public AnnotationMirror getAnnotation(Class<?> cls) {
		return this.annotations.get(cls);
	}
}
