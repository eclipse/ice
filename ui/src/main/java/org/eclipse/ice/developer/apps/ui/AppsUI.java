package org.eclipse.ice.developer.apps.ui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("apps-theme")
public class AppsUI extends UI {
	private VerticalLayout mainLayout = new VerticalLayout();
	private HorizontalLayout packagesLayout = new  HorizontalLayout();
	private HorizontalLayout gitAndOsBtnsLayout = new  HorizontalLayout();
	private HorizontalLayout cnlAndValBtnsLayout = new  HorizontalLayout();
	private HorizontalLayout advancedLayout = new  HorizontalLayout();
	private VerticalLayout envLayout = new  VerticalLayout();
	private VerticalLayout pkgPanelLayout = new VerticalLayout();
	private VerticalLayout pkgListLayout = new VerticalLayout();
	private VerticalLayout pkgDescrLayout = new VerticalLayout();
	private Panel pkgsListPanel = new Panel();
	private Panel pkgsDescrPanel = new Panel("Package(s) in basket:");
	private TextField searchTxtField = new TextField("Packages:");
	private Button addRepoButton = new Button("Add from Git repo...");
	private Button addOSButton = new Button("Add OS package...");
	private Button cancelButton = new Button("Cancel");
	private Button validateButton = new Button("Validate");
	private Button advancedButton = new Button(FontAwesome.CARET_RIGHT);
	private Label advancedLabel = new Label("Advanced");
	private AddRepoWindow repoWindow = new AddRepoWindow();
	private AddOSWindow osWindow = new AddOSWindow();
	private List<Object> pkgItems;
	private EnvironmentForm env = new EnvironmentForm();

	JSONParser parser = new JSONParser();
	JSONObject spackPackages;
	Object obj;
	
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	searchTxtField.setWidth("100%");
    	searchTxtField.setDescription("type filter text");
    	searchTxtField.setInputPrompt("type filter text");
    	
    	
		try {
			obj = parser.parse(new FileReader(
			        "/home/..../ui/src/main/resources/packages.json")); // add here your path to json file
					 
			spackPackages = (JSONObject) obj;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
    		
		JSONArray spackPkgs = (JSONArray) spackPackages.get("packages");
		Iterator<JSONObject> iterator = spackPkgs.iterator();
		while (iterator.hasNext()) {
			final PackageListItem pkg = new PackageListItem();
			List lst = new LinkedList();
			JSONObject jsonPkg = iterator.next();
			//System.out.println(jsonPkg);
			pkg.getPkgChBox().setCaption((String) jsonPkg.get("name"));
			pkg.getPkgDescrTxtField().setCaption((String) jsonPkg.get("description"));
			//pkg.getPkgDescrTxtField().setWidth("200px");
			pkg.getPkgChBox().addValueChangeListener( e -> {
				Label pkgDescItem = new Label(pkg.getPkgChBox().getCaption());
				Label temp = null;
				if (pkg.getPkgChBox().getValue().equals(true)) {
					lst.add(pkg.getPkgChBox().getCaption());
					pkgDescrLayout.addComponent(pkgDescItem);
					//System.out.println(pkgDescrLayout.getComponentIndex(pkgDescItem));
				} else if (lst.contains(pkg.getPkgChBox().getCaption())) {
					Iterator<Component> iter = pkgDescrLayout.iterator();
					while (iter.hasNext()) {
						Label lbl = (Label) iter.next();
//						System.out.println(lbl.getValue());
//						System.out.println(lbl.getCaption());					
//						System.out.println(pkg.getPkgChBox().getCaption());
						if (lbl.getValue().equals(pkg.getPkgChBox().getCaption())) {
							temp = lbl;
						}
					}
					if (!temp.equals(null))
						pkgDescrLayout.removeComponent(temp);
				}
			});
			pkgPanelLayout.addComponent(pkg);

		}
    	pkgsListPanel.setContent(pkgPanelLayout);
    	pkgsListPanel.setHeight("100%");
    	pkgsListPanel.setWidth("100%");
    	
    	pkgsListPanel.setResponsive(true);
    	
    	pkgsDescrPanel.setWidth("100%");
    	pkgsDescrPanel.setHeight("100%");
    	pkgsDescrPanel.setContent(pkgDescrLayout);
    	
    	pkgListLayout.addComponents(searchTxtField, pkgsListPanel);
    	pkgListLayout.setHeight("100%");
    	pkgListLayout.setExpandRatio(pkgsListPanel, 1.0f);
    	
    	packagesLayout.addComponents(pkgListLayout, pkgsDescrPanel);
    	packagesLayout.setSpacing(true);
    	packagesLayout.setMargin(new MarginInfo(true,true,false,true));
    	packagesLayout.setSizeFull();
    	packagesLayout.setHeight("450px");
    	mainLayout.addComponent(packagesLayout);
    	
    	addRepoButton.addClickListener( e -> {
    		repoWindow.setHeight("350");
    		repoWindow.setWidth("460");
    		addWindow(repoWindow);
    	});
    	addOSButton.addClickListener( e -> {
    		osWindow.setHeight("400");
    		osWindow.setWidth("500");
    		addWindow(osWindow);
    	});
    	
    	
    	gitAndOsBtnsLayout.addComponents(addRepoButton, addOSButton);
    	gitAndOsBtnsLayout.setSpacing(true);
    	gitAndOsBtnsLayout.setMargin(new MarginInfo(false,true,false,true));

    	advancedButton.addClickListener( e -> {
    		if (advancedButton.getIcon().equals(FontAwesome.CARET_RIGHT)) {
    			advancedButton.setIcon(FontAwesome.CARET_DOWN);
    			envLayout.addComponent(env);
    		} else {
    			advancedButton.setIcon(FontAwesome.CARET_RIGHT);
    			envLayout.removeComponent(env);
    		}
    	});
    	advancedButton.setWidth("25px");
		advancedButton.addStyleName("borderless");
		
		advancedLayout.addComponents(advancedButton, advancedLabel);
		advancedLayout.setComponentAlignment(advancedLabel, Alignment.MIDDLE_RIGHT);
		advancedLayout.setSpacing(true);
		advancedLayout.setMargin(new MarginInfo(false,true,false,true));
    	
		cancelButton.setWidth("130px");
		validateButton.setWidth("130px");
		cnlAndValBtnsLayout.addComponents(cancelButton, validateButton);
    	cnlAndValBtnsLayout.setMargin(new MarginInfo(false,true,false,false));

		cnlAndValBtnsLayout.setSpacing(true);
		mainLayout.addComponents(gitAndOsBtnsLayout, advancedLayout, envLayout, cnlAndValBtnsLayout);
		mainLayout.setComponentAlignment(cnlAndValBtnsLayout, Alignment.BOTTOM_RIGHT);
    	mainLayout.setMargin(true);
    	mainLayout.setSpacing(true);
    	
    	setContent(mainLayout);
    }

    @WebServlet(urlPatterns = "/*", name = "AppsUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AppsUI.class, productionMode = false)
    public static class AppsUIServlet extends VaadinServlet {
    }
}
