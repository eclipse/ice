package gov.ornl.rse.renderer.client.test;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import gov.ornl.rse.renderer.DataElement;

@Route
public class MainView extends VerticalLayout {

	public MainView() {
		
		DataElement<String> nameElem = new DataElement<String>();
		nameElem.setName("Ross' name");
		nameElem.setDescription("Ross' name described by a data element");
		nameElem.setData("Ross Whitfield");
		
        VaadinRendererClient<String> client = new VaadinRendererClient<String>();
        client.setData(nameElem);

        add(client);
		
	}

}