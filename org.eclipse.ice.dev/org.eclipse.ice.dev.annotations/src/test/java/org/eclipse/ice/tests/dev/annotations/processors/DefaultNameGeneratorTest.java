package org.eclipse.ice.tests.dev.annotations.processors;

import static org.junit.Assert.assertEquals;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.Persisted;
import org.eclipse.ice.dev.annotations.processors.DefaultNameGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for the DefaultNameGenerator implementation
 * 
 * @author Michael Walsh
 *
 */
public class DefaultNameGeneratorTest {
	
	DefaultNameGenerator nameGenerator = new DefaultNameGenerator();
	
	Element elem;
	DataElement dataElementAnnotation;
	Persisted persistedAnnotation;
	
	@Before
	public void setup() {
		elem = Mockito.mock(TypeElement.class);
		persistedAnnotation = Mockito.mock(Persisted.class);
		dataElementAnnotation = Mockito.mock(DataElement.class);
		
		Mockito.when(dataElementAnnotation.name()).thenReturn("testName");
		Mockito.when(persistedAnnotation.collection()).thenReturn("testCollection");
		
		Mockito.when(elem.getAnnotation(DataElement.class)).thenReturn(dataElementAnnotation);
		Mockito.when(elem.getAnnotation(Persisted.class)).thenReturn(persistedAnnotation);
	}
	
	@Test
	public void testExtractName() {
		assertEquals("testName", nameGenerator.extractName(elem));
	}
	
	@Test
	public void testExtractCollectionName() {
		assertEquals("testCollection", nameGenerator.extractCollectionName(elem));
	}
	
	@Test
	public void testGetImplName() {
		assertEquals("TestImplementation", nameGenerator.getImplName("Test"));
	}
	
	@Test
	public void testGetQualifiedImplName() {
		assertEquals("org.ornl.TestImplementation", nameGenerator.getQualifiedImplName("org.ornl.Test"));
	}
	
	@Test
	public void testGetPersistenceHandlerName() {
		assertEquals("TestPersistenceHandler", nameGenerator.getPersistenceHandlerName("Test"));
	}
	
	@Test
	public void testGetQualifiedPersistenceHandlerName() {
		assertEquals("org.ornl.TestPersistenceHandler", nameGenerator.getQualifiedPersistenceHandlerName("org.ornl.Test"));
	}
	
	@Test
	public void testGetPersistenceHandlerInterfaceName() {
		assertEquals(org.eclipse.ice.data.IPersistenceHandler.class.getSimpleName(), nameGenerator.getPersistenceHandlerInterfaceName());
	}
}
