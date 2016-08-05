package org.eclipse.ice.datastructures.jaxbclassprovider;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;

public class ICEXMLSchemaGenerator {

	public static void main(String[] args) {

		ICEJAXBClassProvider provider = new ICEJAXBClassProvider();
		List<Class> classes = provider.getClasses();
		classes.toArray(new Class[classes.size()]);
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(classes.toArray(new Class[classes.size()]));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		System.out.println("HELLO WORLD");
		SchemaOutputResolver sor = new MySchemaOutputResolver();
		try {
			jaxbContext.generateSchema(sor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
