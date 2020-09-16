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

/**
 * Class that runs a battery of tests through the methods of the 
 * utility class {@link org.eclipse.ice.dev.annotations.processors.VelocityUtils}
 *
 */
@SuppressWarnings("deprecation")
public class VelocityUtilsTest {
	
	/**
	 * Class to be tested
	 */
	private VelocityUtils utils;
	/**
	 * Mocked class used in VelocityUtils
	 */
	private Types typeUtils;
	
	/**
	 * Performs mocks/setup necessary for most tests
	 */
	@Before
	public void setup() {
		ProcessingEnvironment env = Mockito.mock(ProcessingEnvironment.class);
		typeUtils = Mockito.mock(Types.class);
		Mockito.when(env.getTypeUtils()).thenReturn(typeUtils);
		utils = new VelocityUtils(env);
	}
	
	/**
	 * Test for {@link org.eclipse.ice.dev.annotations.processors.VelocityUtils#getCollectionParameterType(TypeMirror)}
	 * This test ensures the expected result of parameter extraction executes successfully
	 */
	@Test
	public void testGetCollectionParameterType() {
		DeclaredType type = Mockito.mock(DeclaredType.class);
		TypeMirror mirror = Mockito.mock(TypeMirror.class);
		List typeArguments = Arrays.asList(mirror);
		Mockito.when(type.getTypeArguments()).thenReturn(typeArguments);
		
		Assert.assertEquals(mirror, utils.getCollectionParameterType(type));
	}
	
	/**
	 * Test for {@link org.eclipse.ice.dev.annotations.processors.VelocityUtils#getClass(TypeMirror)}
	 * Ensures that, if given a {@link javax.lang.model.type.DeclaredType} it will return the class extracted from it.
	 * @throws ClassNotFoundException from {@link org.eclipse.ice.dev.annotations.processors.VelocityUtils#getClass(TypeMirror)}
	 */
	@Test
	public void testGetClass() throws ClassNotFoundException {
		DeclaredType type = Mockito.mock(DeclaredType.class);
		Element elem = Mockito.mock(Element.class);
		Mockito.when(elem.toString()).thenReturn(Element.class.getCanonicalName());
		Mockito.when(type.asElement()).thenReturn(elem);
		
		Assert.assertEquals(Element.class, VelocityUtils.getClass(type));
	}
	
	/**
	 * Test for {@link org.eclipse.ice.dev.annotations.processors.VelocityUtils#collectAllDataElementFields(List)}
	 * Given a list of {@link org.eclipse.ice.dev.annotations.processors.Field}, test that it returns all fields of type 
	 * {@link org.eclipse.ice.data.IDataElement} from the list
	 */
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
	
	/**
	 * Test for {@link org.eclipse.ice.dev.annotations.processors.VelocityUtils#anyDataElementsExist(List)}
	 * Given a list of {@link org.eclipse.ice.dev.annotations.processors.Field}, test that it returns true if there 
	 * exists at least one {@link org.eclipse.ice.data.IDataElement} in the list
	 */
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
	
	/**
	 * Test for {@link org.eclipse.ice.dev.annotations.processors.VelocityUtils#isIDataElement(TypeMirror)}
	 * Given a {@link javax.lang.model.type.TypeMirror}, test that the method correctly looks at the supertypes to determine
	 * that a class implements {@link org.eclipse.ice.data.IDataElement}
	 */
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
