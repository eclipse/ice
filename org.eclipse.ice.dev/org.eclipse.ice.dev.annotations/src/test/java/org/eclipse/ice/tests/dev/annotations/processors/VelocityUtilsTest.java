package org.eclipse.ice.tests.dev.annotations.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import org.eclipse.ice.data.IDataElement;
import org.eclipse.ice.dev.annotations.processors.Field;
import org.eclipse.ice.dev.annotations.processors.VelocityUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class VelocityUtilsTest {
	
	private VelocityUtils utils;
	private Types typeUtils;
	
	@Before
	public void setup() {
		ProcessingEnvironment env = Mockito.mock(ProcessingEnvironment.class);
		typeUtils = Mockito.mock(Types.class);
		Mockito.when(env.getTypeUtils()).thenReturn(typeUtils);
		utils = new VelocityUtils(env);
	}
	
	@Test
	public void testGetCollectionParameterType() {
		DeclaredType type = Mockito.mock(DeclaredType.class);
		TypeMirror mirror = Mockito.mock(TypeMirror.class);
		List typeArguments = Arrays.asList(mirror);
		Mockito.when(type.getTypeArguments()).thenReturn(typeArguments);
		
		Assert.assertEquals(mirror, utils.getCollectionParameterType(type));
	}
	
	@Test
	public void testGetClass() throws ClassNotFoundException {
		DeclaredType type = Mockito.mock(DeclaredType.class);
		Element elem = Mockito.mock(Element.class);
		Mockito.when(elem.toString()).thenReturn(Element.class.getCanonicalName());
		Mockito.when(type.asElement()).thenReturn(elem);
		
		Assert.assertEquals(Element.class, VelocityUtils.getClass(type));
	}
	
	@Test
	public void testCollectAllDataElementFields() {
		List<Field> fields = new ArrayList<>();
		Field field = Mockito.mock(Field.class);
		TypeMirror type = Mockito.mock(TypeMirror.class);
		Mockito.when(type.toString()).thenReturn(IDataElement.class.getSimpleName());
		Mockito.when(field.getMirror()).thenReturn(type);
		fields.add(field);
		Assert.assertEquals(field, utils.collectAllDataElementFields(fields).get(0));
	}
	
	@Test
	public void testAnyDataElementsExist() {
		List<Field> fields = new ArrayList<>();
		Field field = Mockito.mock(Field.class);
		TypeMirror type = Mockito.mock(TypeMirror.class);
		Mockito.when(type.toString()).thenReturn(IDataElement.class.getSimpleName());
		Mockito.when(field.getMirror()).thenReturn(type);
		fields.add(field);
		Assert.assertTrue(utils.anyDataElementsExist(fields));
	}
	
	@Test 
	public void testIsIDataElement() {
		TypeMirror baseType = Mockito.mock(TypeMirror.class);
		DeclaredType type = Mockito.mock(DeclaredType.class);
		Element elem = Mockito.mock(Element.class);
		Mockito.when(baseType.toString()).thenReturn("nuh uh");
		Mockito.when(type.toString()).thenReturn(IDataElement.class.getSimpleName());
		Mockito.when(elem.asType()).thenReturn(type);
		Mockito.when(elem.toString()).thenReturn(Element.class.getCanonicalName());
		Mockito.when(type.asElement()).thenReturn(elem);
		
		List typeArguments = Arrays.asList(type);
		
		Mockito.when(typeUtils.directSupertypes(Mockito.any())).thenReturn(typeArguments);
		
		Assert.assertTrue(utils.isIDataElement(baseType));
	}

}
