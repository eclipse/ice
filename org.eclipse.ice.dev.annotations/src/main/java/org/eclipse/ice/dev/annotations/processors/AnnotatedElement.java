package org.eclipse.ice.dev.annotations.processors;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
 	
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.FieldInfo;
import org.eclipse.ice.dev.annotations.IDataElement;
import org.eclipse.ice.dev.annotations.Persisted;

import lombok.Getter;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

/**
 * Helper for accessing and working with Annotated Classes.
 * @author Daniel Bluhm
 */
public abstract class AnnotatedElement {
	
	/**
	 * The value appended to DataElement implementation class names.
	 */
	private final static String IMPL_SUFFIX = "Implementation";

	/**
	 * The value appended to DataElement Persistence Handler class names.
	 */
	private final static String PERSISTENCE_SUFFIX = "PersistenceHandler";

	/**
	 * The fully qualified name of this element.
	 */
	@Getter
	private String fullyQualifiedName;

	/**
	 * The name of the DataElement as extracted from the DataElement annotation.
	 */
	@Getter
	private String name;

	/**
	 * The package of this element represented as a String.
	 */
	@Getter
	private String packageName;

	/**
	 * The name of the collection as extracted from Persisted or null.
	 */
	@Getter
	private String collectionName;
	
	/**
	 * List of all annotation mirrors on this element.
	 */
	private List<? extends AnnotationMirror> mirrors;

	/**
	 * Elements used to retrieve defaults for annotation values.
	 */
	protected Elements elementUtils;

	/**
	 * The element representing an interface annotated with
	 * {@code @DataElement}.
	 */
	protected Element element;

	/**
	 * Construct an AnnotatedElement from an Element.
	 * @param element The annotated element
	 * @param elementUtils Elements helper from processing environment
	 */
	public AnnotatedElement(Element element, Elements elementUtils) throws InvalidDataElementSpec {
		this.element = element;
		this.elementUtils = elementUtils;
		
		if (!isValidAnnotatedElement(element, elementUtils)) {
			throw new InvalidDataElementSpec(
				"DataElementSpec must be class, found " + element.toString()
			);
		}
		
		// Names
		this.name = this.extractName();
		
		String elementFQN = (element instanceof TypeElement)
		        ? ((TypeElement)element).getQualifiedName().toString() : element.getClass().getName();
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
	 * Get the name of the Implementation to be generated.
	 * @return implementation name
	 */
	public String getImplName() {
		return this.name + IMPL_SUFFIX;
	}

	/**
	 * Get the fully qualified name of the Implementation to be generated.
	 * @return fully qualified implementation name
	 */
	public String getQualifiedImplName() {
		return this.fullyQualifiedName + IMPL_SUFFIX;
	}

	/**
	 * Get the name of the Persistence Handler to be generated.
	 * @return persistence handler name
	 */
	public String getPersistenceHandlerName() {
		return this.name + PERSISTENCE_SUFFIX;
	}

	/**
	 * Get the fully qualified name of the Persistence Handler to be generated.
	 * @return fully qualified persistence handler name
	 */
	public String getQualifiedPersistenceHandlerName() {
		return this.fullyQualifiedName + PERSISTENCE_SUFFIX;
	}
	
	/**
	 * Determine if an annotation of a given type decorates this element.
	 * @param cls class of annotation to check
	 * @return whether annotation is present or not
	 */
	public boolean hasAnnotation(Class<? extends Annotation> cls) {
		return this.element.getAnnotation(cls) != null;
	}

	/**
	 * Get the AnnotationMirror of a given type if present on the element.
	 * @param <T> Type of annotation to retrieve
	 * @param cls class of annotation to retrieve
	 * @return AnnotationMirror or null if not found
	 */
	public <T extends Annotation> Optional<T> getAnnotation(Class<T> cls) {
		T value = this.element.getAnnotation(cls);
		if (value == null) {
			return Optional.empty();
		}
		return Optional.of(value);
	}

	public abstract boolean isValidAnnotatedElement(Element element, Elements elementUtils);
	/**
	 * Return the element name as extracted from the DataElement annotation.
	 * @return the extracted name
	 */
	public String extractName() {
		return this.getAnnotation(DataElement.class)
			.map(e -> e.name())
			.orElse(null);
	}
	
	/**
	 * Return the collection name as extracted from the Persisted annotation.
	 * @return the extracted collection name
	 */
	public String extractCollectionName() {
		return this.getAnnotation(Persisted.class)
			.map(p -> p.collection())
			.orElse(null);
	}


	/**
	 * Get a map of annotation value names to the value identified by that name.
	 *
	 * This is useful when dealing with a complicated Annotation potentially
	 * containing a value that is a Class object. Otherwise, it is recommended to
	 * directly retrieve the value from an Annotation instance.
	 * @param annotationClass the class of the annotation from which values
	 *        will be retrieved.
	 * @return Map of String to unwrapped AnnotationValue (Object)
	 */
	public Map<String, Object> getAnnotationValueMap(Class<?> annotationClass) {
		return this.getAnnotationMirror(annotationClass)
			.map(mirror -> elementUtils.getElementValuesWithDefaults(mirror))
			.map(map -> map.entrySet().stream()
				.collect(Collectors.toMap(
					entry -> entry.getKey().getSimpleName().toString(),
					entry -> entry.getValue().getValue()
				))
			).orElse(Collections.emptyMap());
	}

	/**
	 * Get a list of annotation values from an annotation mirror of a given type.
	 *
	 * This is useful when dealing with a complicated Annotation potentially
	 * containing a value that is a Class object. Otherwise, it is recommended to
	 * directly retrieve the value from an Annotation instance.
	 * @param annotationClass the class of the annotation from which values will be
	 *        retrieved.
	 * @return list of AnnotationValue
	 */
	public List<AnnotationValue> getAnnotationValues(Class<?> annotationClass) {
		return this.getAnnotationMirror(annotationClass)
			.map(mirror -> elementUtils.getElementValuesWithDefaults(mirror))
			.map(map -> map.entrySet().stream()
				.map(entry -> (AnnotationValue) entry.getValue())
				.collect(Collectors.toList())
			).orElse(Collections.emptyList());
	}
	
	public Predicate<Element> fieldFilter(){
		return element -> true;//IDataElement.class.isAssignableFrom(element.getClass());
	}
	
	/**
	 * Return the set of access modifiers on this Field.
	 * @return extract field modifiers
	 * @see Modifier
	 */
	protected Set<Modifier> extractModifiers() {
		return this.element.getModifiers();
	}

	/**
	 * Return the set of annotations on this DataField, excepting the DataField
	 * Annotation itself.
	 * @return extracted annotations, excluding DataField related annotations
	 */
	protected abstract List<String> extractAnnotations();

	/**
	 * Return the class of this Field.
	 * @return extracted field type
	 */
	protected TypeMirror extractFieldType() {
		return this.element.asType();
	}

	/**
	 * Return the name of this Field.
	 * @return extracted field name
	 */
	protected String extractFieldName() {
		return this.element.getSimpleName().toString();
	}

	/**
	 * Return the DocString of this Field.
	 * @return extracted doc comment
	 */
	protected String extractDocString() {
		return this.elementUtils.getDocComment(this.element);
	}
	
	protected abstract String extractDefaultValue();
	
	//issue is here these methods should be using the element sent to them
	public Field convertElementToField(Element element, FieldInfo fieldInfo) {
			return Field.builder()
				.name(extractFieldName())
				.type(extractFieldType())
				.mirror(element.asType())
				.defaultValue(extractDefaultValue())
				.docString(extractDocString())
				.annotations(extractAnnotations())
				.modifiersToString(extractModifiers())
				.getter(fieldInfo.isGetter())
				.setter(fieldInfo.isSetter())
				.match(fieldInfo.isMatch())
				.unique(fieldInfo.isUnique())
				.search(fieldInfo.isSearch())
				.nullable(fieldInfo.isNullable())
				.build();
	}
	
	/**
	 * Find and return annotation of a given on this element.
	 * @param annotationClass Class of annotation mirror to retrieve
	 * @return {@link Optional} of annotation mirror
	 */
	private Optional<AnnotationMirror> getAnnotationMirror(Class<?> annotationClass) {
		if (this.mirrors == null) {
			this.mirrors = this.element.getAnnotationMirrors();
		}
		return this.mirrors.stream()
			.filter(m -> m.getAnnotationType()
				.toString().equals(annotationClass.getCanonicalName())
			).findAny()
			.map(m -> Optional.of((AnnotationMirror) m))
			.orElse(Optional.empty());
	}
}
