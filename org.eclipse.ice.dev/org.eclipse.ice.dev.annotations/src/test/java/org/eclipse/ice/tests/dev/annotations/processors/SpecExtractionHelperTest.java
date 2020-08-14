package org.eclipse.ice.tests.dev.annotations.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.Persisted;
import org.eclipse.ice.dev.annotations.processors.Field;
import org.eclipse.ice.dev.annotations.processors.SpecExtractionHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import lombok.Data;

public class SpecExtractionHelperTest {
	
	SpecExtractionHelper extractionHelper = new SpecExtractionHelper();

	
	Element elem;
	Elements elementUtils;
	DataElement dataElementAnnotation;
	Persisted persistedAnnotation;
	
	@Before
	public void setup() {
		elem = Mockito.mock(TypeElement.class);
		elementUtils = Mockito.mock(Elements.class);
		persistedAnnotation = Mockito.mock(Persisted.class);
		dataElementAnnotation = Mockito.mock(DataElement.class);
		Name mockName = Mockito.mock(Name.class);
		TypeMirror typeMirror = Mockito.mock(TypeMirror.class);
		TypeKind typeKind = TypeKind.BOOLEAN;
		
		Element enclosedField = Mockito.mock(TypeElement.class);
		
		Mockito.when(typeMirror.getKind()).thenReturn(typeKind);
		
		Mockito.when(mockName.toString()).thenReturn("simpleName");
		
		AnnotationMirror annotationMirror = Mockito.mock(AnnotationMirror.class);
		Mockito.when(annotationMirror.toString()).thenReturn("annotationMirror");
		Mockito.when(annotationMirror.getAnnotationType()).thenReturn(Mockito.mock(DeclaredType.class));
		List<? extends AnnotationMirror> mirrorList = Arrays.asList(annotationMirror);
		
		DataField.Default defaults = Mockito.mock(DataField.Default.class);
		Mockito.when(defaults.isString()).thenReturn(true);
		Mockito.when(defaults.value()).thenReturn("defaultVal");
		
		Mockito.when(dataElementAnnotation.name()).thenReturn("testName");
		Mockito.when(persistedAnnotation.collection()).thenReturn("testCollection");
		
		
		Mockito.when(elem.getAnnotation(DataElement.class)).thenReturn(dataElementAnnotation);
		Mockito.when(elem.getAnnotation(Persisted.class)).thenReturn(persistedAnnotation);
		Mockito.when(elem.getModifiers()).thenReturn(Stream.of(Modifier.FINAL).collect(Collectors.toSet()));
		Mockito.when(elem.getAnnotationMirrors()).thenReturn((List)mirrorList);
		Mockito.when(elem.getAnnotation(DataField.Default.class)).thenReturn(defaults);
		Mockito.when(elem.getSimpleName()).thenReturn(mockName);
		Mockito.when(elem.toString()).thenReturn("ElementString");
		Mockito.when(elem.asType()).thenReturn(typeMirror);
		Mockito.when(elem.getEnclosedElements()).thenReturn((List)Arrays.asList(enclosedField));
	
		Mockito.when(elementUtils.getConstantExpression(Mockito.any())).thenReturn("retval");
	}
	
	@Test
	public void testConvertElementToField() {
		Field field = extractionHelper.convertElementToField(elem, elementUtils, Field.builder().build(), new ArrayList<>());
		assertNotNull(field);
		assertEquals("annotationMirror", field.getAnnotations().get(0));
		assertEquals(false, field.isDefaultField());
		assertEquals("retval", field.getDefaultValue());
		assertEquals(true, field.isGetter());
		assertEquals(true, field.isMatch());
		assertEquals("final", field.getModifiers().stream().findFirst().get());
		assertEquals("simpleName", field.getName());
		assertEquals(false, field.isNullable());
		assertEquals(true, field.isPrimitive());
		assertEquals(true, field.isSearchable());
		assertEquals(true, field.isSetter());
		assertEquals(false, field.isUnique());
		
	}
	
	@Test
	public void testHasAnnotation() {
		assertTrue(extractionHelper.hasAnnotation(elem, DataElement.class));
		assertFalse(extractionHelper.hasAnnotation(elem, Data.class));
	}
	
	@Test
	public void testExtractModifiers() {
		assertEquals(1, extractionHelper.extractModifiers(elem).size());
	}
	
	@Test
	public void testExtractAnnotations() {
		assertEquals(Arrays.asList("annotationMirror"), extractionHelper.extractAnnotations(elem, new ArrayList<>()));
	}
	
	@Test
	public void testExtractDefaultValue() {
		assertEquals("retval", extractionHelper.extractDefaultValue(elem, elementUtils));
	}
	
	@Test
	public void testGetAllFields() {
		List<Field> fields = extractionHelper.getAllFields(elem, elementUtils, f->true, new ArrayList());
		assertNotNull(fields);
		assertEquals(0, fields.size());
	}
}
