package org.eclipse.ice.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.ListComposite;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.junit.Test;

public class ListCompositeTester {

	/** Local Declarations */
	ListComposite listComp;
	ArrayList<Component> list;
	
	@Test
	public void checkConstruction() {
		
		// Use the null constructor and make sure that 
		// the list is initialized, but empty
		listComp = new ListComposite();
		assertNotNull(listComp.getComponents());
		assertEquals(listComp.getNumberOfComponents(),0);
		
		// Provide the constructor a predefined ArrayList
		// and make sure no large disasters occur
		list = new ArrayList<Component>();
		list.add(new DataComponent());
		listComp = new ListComposite(list);
		assertNotNull(listComp.getComponents());
		assertNotNull(listComp.getComponent(0));
		assertEquals(listComp.getNumberOfComponents(),1);
	}
	
	@Test
	public void checkFunctionality() {
		
		// Add some various Component types and check that
		// they all work as advertised
		listComp = new ListComposite();
		listComp.addComponent(new DataComponent());
		listComp.addComponent(new TableComponent());
		listComp.addComponent(new MasterDetailsComponent());
		assertEquals(listComp.getNumberOfComponents(),3);
		for (int i = 0; i < listComp.getNumberOfComponents(); i++) {
			assertNotNull(listComp.getComponent(i));
		}
		
		// Construct using a predefined list, then tell 
		// ourselves that everything's gonna be okay.
		list = new ArrayList<Component>();
		list.add(new DataComponent());
		list.add(new MasterDetailsComponent());
		list.add(new TableComponent());
		list.add(new DataComponent());
		listComp = new ListComposite(list);
		assertEquals(listComp.getNumberOfComponents(),4);
		for (int i = 0; i < listComp.getNumberOfComponents(); i++) {
			assertNotNull(listComp.getComponent(i));
		}

		// Compare the internal size to the external size
		assertEquals(listComp.getNumberOfComponents(),listComp.getComponents().size());
		
		// Take one away
		listComp.removeComponent(listComp.getNumberOfComponents()-1);
		assertEquals(listComp.getNumberOfComponents(),3);

	}
	
}
