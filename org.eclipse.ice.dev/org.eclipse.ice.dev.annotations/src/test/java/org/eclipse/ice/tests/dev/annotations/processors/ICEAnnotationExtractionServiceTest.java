package org.eclipse.ice.tests.dev.annotations.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.processors.AnnotationExtractionRequest;
import org.eclipse.ice.dev.annotations.processors.DefaultFields;
import org.eclipse.ice.dev.annotations.processors.Field;
import org.eclipse.ice.dev.annotations.processors.Fields;
import org.eclipse.ice.dev.annotations.processors.ICEAnnotationExtractionService;
import org.eclipse.ice.dev.annotations.processors.MetaTemplateProperty;
import org.eclipse.ice.dev.annotations.processors.NameGenerator;
import org.eclipse.ice.dev.annotations.processors.PersistenceHandlerTemplateProperty;
import org.eclipse.ice.dev.annotations.processors.TemplateProperty;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ICEAnnotationExtractionServiceTest {
	
	TypeElement elem;
	Elements elementUtils;
	ObjectMapper mapper;
	ProcessingEnvironment processingEnv;
	NameGenerator nameGenerator;
	ICEAnnotationExtractionService extractionService;
	
	@Before
	public void setUp() {
		elem = Mockito.mock(TypeElement.class);
		Name name = Mockito.mock(Name.class);
		Mockito.when(name.toString()).thenReturn("org.java.test");
		Mockito.when(elem.getQualifiedName()).thenReturn(name);
		elementUtils = Mockito.mock(Elements.class);
		mapper = new ObjectMapper();
		processingEnv = Mockito.mock(ProcessingEnvironment.class);
		nameGenerator = Mockito.mock(NameGenerator.class);
		Mockito.when(nameGenerator.extractCollectionName(Mockito.any())).thenReturn("collectionName");
		Mockito.when(nameGenerator.getImplName(Mockito.any())).thenReturn("implName");
		Mockito.when(nameGenerator.getQualifiedImplName(Mockito.any())).thenReturn("qualifiedImplName");
		Mockito.when(nameGenerator.getQualifiedPersistenceHandlerName(Mockito.any())).thenReturn("qualifiedPersistenceHandlerName");
		Mockito.when(nameGenerator.getPersistenceHandlerName(Mockito.any())).thenReturn("persistenceHandlerName");
		Mockito.when(nameGenerator.getPersistenceHandlerInterfaceName()).thenReturn("persistenceHandlerInterfaceName");
		
		extractionService = new ICEAnnotationExtractionService(elementUtils, mapper, processingEnv, nameGenerator);
		
		extractionService.setFieldFilter(f -> true);
	}
	
	@Test
	public void testExtractFields() throws IOException {
		AnnotationExtractionRequest request = AnnotationExtractionRequest.builder()
				.element(elem)
				.className("test")
				.build();
		Fields fields = extractionService.extractFields(request);
		int size = 0;
		for(Field field : fields) {
			size++;
			assertTrue(DefaultFields.get().contains(field));
		}
		assertEquals(DefaultFields.get().size(), size);
	}

	@Test
	public void testExtractClassMetadata() {
		Fields fields = new Fields();
		AnnotationExtractionRequest request = AnnotationExtractionRequest.builder()
				.element(elem)
				.className("test")
				.build();
		Map<TemplateProperty, Object> metaExtractionResult = extractionService.extractClassMetadata(request, fields);
		assertNotNull(metaExtractionResult);
		assertEquals("org.java", metaExtractionResult.get(MetaTemplateProperty.PACKAGE));
		assertEquals("test", metaExtractionResult.get(MetaTemplateProperty.INTERFACE));
		assertEquals("qualifiedImplName", metaExtractionResult.get(MetaTemplateProperty.QUALIFIEDIMPL));
		assertEquals("persistenceHandlerName", metaExtractionResult.get(PersistenceHandlerTemplateProperty.CLASS));
		assertEquals("test", metaExtractionResult.get(PersistenceHandlerTemplateProperty.ELEMENT_INTERFACE));
		assertEquals("persistenceHandlerInterfaceName", metaExtractionResult.get(PersistenceHandlerTemplateProperty.INTERFACE));
		assertEquals("persistenceHandlerName", metaExtractionResult.get(PersistenceHandlerTemplateProperty.CLASS));
		assertEquals("implName", metaExtractionResult.get(MetaTemplateProperty.CLASS));
		assertEquals("collectionName", metaExtractionResult.get(PersistenceHandlerTemplateProperty.COLLECTION));
		assertEquals("qualifiedPersistenceHandlerName", metaExtractionResult.get(PersistenceHandlerTemplateProperty.QUALIFIED));
		assertEquals("org.java.test", metaExtractionResult.get(MetaTemplateProperty.QUALIFIED));
		assertEquals("implName", metaExtractionResult.get(PersistenceHandlerTemplateProperty.IMPLEMENTATION));
		
	}
}
