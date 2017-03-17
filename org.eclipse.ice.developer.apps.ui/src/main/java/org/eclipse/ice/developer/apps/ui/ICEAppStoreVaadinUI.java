package org.eclipse.ice.developer.apps.ui;

import java.util.function.Predicate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.eclipse.emf.common.util.EList;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import apps.AppsFactory;
import apps.EnvironmentManager;
import apps.IEnvironment;
import apps.Package;
import apps.docker.ContainerConfiguration;
import apps.docker.DockerEnvironment;
import apps.docker.DockerFactory;
import eclipseapps.EclipseappsFactory;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class ICEAppStoreVaadinUI extends UI {
	
	/**
	 * A serialization ID - if you remove this, OSGI DS will fail!
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The HttpService Reference for the OSGI framework.
	 */
	static private HttpService httpService;
	
	/**
	 * The main layout of the app
	 */
	private VerticalLayout mainLayout;
	
	private VerticalLayout packageBasket = new VerticalLayout();

	private EnvironmentManager manager;
	
	private IEnvironment createdEnvironment;

	@WebServlet(urlPatterns = "/*", name = "AppsUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = ICEAppStoreVaadinUI.class, productionMode = false)
	public static class AppsUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * OSGi bundle activator with annotation instead of activator class.
	 * 
	 * @param context
	 */
	@Activate
	public void start(BundleContext context) {
		System.out.println("App Store VAADIN bundle started.");
		try {
			ICEAppStoreVaadinUI.httpService.registerServlet("/", new AppsUIServlet(), null, null);
		} catch (ServletException | NamespaceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param httpService
	 */
	public void setService(HttpService httpService) {
		ICEAppStoreVaadinUI.httpService = httpService;
	}
	
	//BeanFieldGroup<SpackPackage> binder = new BeanFieldGroup<SpackPackage>(SpackPackage.class);

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		manager = AppsFactory.eINSTANCE.createEnvironmentManager();
		createdEnvironment = manager.createEmpty("Docker");
		manager.setEnvironmentStorage(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentStorage());
		manager.setConsole(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentConsole());
		createdEnvironment.setConsole(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentConsole());
		
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		setContent(mainLayout);

		mainLayout.addComponent(new Environment(createdEnvironment));

		addSpackPackagesLayout();
		
		addOtherPackagesLayout();
		
		addAdvancedView();
		
		addValidateCancelButtons();
	}

	/**
     * Creates validate and cancel buttons and adds them to layout
     */
    private void addValidateCancelButtons() {
    	HorizontalLayout cnlAndValBtnsLayout = new  HorizontalLayout();
    	Button validateButton = new Button("Validate");
    	
    	validateButton.setWidth("130px");
    	
    	validateButton.addClickListener( e -> {
        	manager.persistEnvironments();
        	validateButton.setCaption("Environment Validated!");
        	validateButton.setWidth("200px");
        	// FIXME NEED ACTUAL VALIDATION ROUTINE!!!
    	});
    	
    	cnlAndValBtnsLayout.addComponents(validateButton);
    	cnlAndValBtnsLayout.setMargin(new MarginInfo(false,true,false,false));
    	
    	cnlAndValBtnsLayout.setSpacing(true);
    	mainLayout.addComponents(cnlAndValBtnsLayout);
    	mainLayout.setComponentAlignment(cnlAndValBtnsLayout, Alignment.BOTTOM_RIGHT);		
	}

	/**
	 * Creates advanced button and it's functions and adds to main layout
	 */
	private void addAdvancedView() {
		HorizontalLayout advancedLayout = new  HorizontalLayout();
		VerticalLayout envLayout = new  VerticalLayout();
		Button advancedButton = new Button(FontAwesome.CARET_RIGHT);
		Label advancedLabel = new Label("Advanced");
		EnvironmentForm env = new EnvironmentForm(createdEnvironment);

		advancedButton.addClickListener( e -> {
			if (advancedButton.getIcon().equals(FontAwesome.CARET_RIGHT)) {
				advancedButton.setIcon(FontAwesome.CARET_DOWN);
				envLayout.addComponent(env);
//				dockerBinder = env.getDockerBinder();
//				folderBinder = env.getFolderBinder();
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
		mainLayout.addComponents(advancedLayout, envLayout);
		
	}

	/**
	 * Creates 'add from git repo' and 'add os packages' buttons
	 * and adds them to main layout.
	 */
	private void addOtherPackagesLayout() {
		HorizontalLayout gitAndOsBtnsLayout = new  HorizontalLayout();
		Button addRepoButton = new Button("Add from Git repo...");
		Button addOSButton = new Button("Add OS package...");
		AddRepoWindow repoWindow = new AddRepoWindow(createdEnvironment, packageBasket);
		AddOSWindow osWindow = new AddOSWindow(createdEnvironment, packageBasket);
		
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
		mainLayout.addComponent(gitAndOsBtnsLayout);
		
	}

	
	/**
	 * Creates Spack packages representation and adds it to main view.
	 */
	private void addSpackPackagesLayout() {
		HorizontalLayout packagesLayout = new  HorizontalLayout();
		VerticalLayout pkgPanelLayout = new VerticalLayout();
		VerticalLayout pkgListLayout = new VerticalLayout();
		Panel pkgsListPanel = new Panel();
		Panel pkgsDescrPanel = new Panel("Package(s) in basket:");
		TextField searchTxtField = new TextField("Packages:");
		
		// get list of spack packages from docker
		EList<String> spackPackages = manager.listAvailableSpackPackages();

		// iterate over every package and add it to the panel
		for (String spackPackageName : spackPackages) {
			
			// create layout for the package item
			final PackageListItem packageItem = new PackageListItem();
			
			// set the name of the package to package item
			packageItem.getPkgChBox().setCaption(spackPackageName);
			
			// set a listener to checkbox of the package item
			packageItem.getPkgChBox().addValueChangeListener(e -> {
				
				boolean checkBoxChecked = packageItem.getPkgChBox().getValue();
				String checkBoxCaption = packageItem.getPkgChBox().getCaption();

				// if a package is selected, add it to basket
				if (checkBoxChecked) {
					Label pkgDescItem = new Label();
					pkgDescItem.setCaption(checkBoxCaption);
					packageBasket.addComponent(pkgDescItem);
					
					apps.SpackPackage p = AppsFactory.eINSTANCE.createSpackPackage();
					p.setName(checkBoxCaption);
					createdEnvironment.getDependentPackages().add(p);

				} else if (!checkBoxChecked) {
					createdEnvironment.getDependentPackages().removeIf(new Predicate<apps.Package>() {
						@Override
						public boolean test(Package t) {
							return t.getName().equals(checkBoxCaption);
						}
					});
					for (Component label : packageBasket) {
						if (label.getCaption().equals(checkBoxCaption)) {
							packageBasket.removeComponent(label);
							break;
						}
					}
				}
			});
			pkgPanelLayout.addComponent(packageItem);
		}
		
		searchTxtField.setWidth("100%");
		searchTxtField.setDescription("type filter text");
		searchTxtField.setInputPrompt("type filter text");
		
		
    	pkgsListPanel.setContent(pkgPanelLayout);
    	pkgsListPanel.setHeight("100%");
    	pkgsListPanel.setWidth("100%");
    	
    	pkgsListPanel.setResponsive(true);
    	
    	pkgsDescrPanel.setWidth("100%");
    	pkgsDescrPanel.setHeight("100%");
    	pkgsDescrPanel.setContent(packageBasket);
    	
    	pkgListLayout.addComponents(searchTxtField, pkgsListPanel);
    	pkgListLayout.setHeight("100%");
    	pkgListLayout.setExpandRatio(pkgsListPanel, 1.0f);
    	
    	packagesLayout.addComponents(pkgListLayout, pkgsDescrPanel);
    	packagesLayout.setSpacing(true);
    	packagesLayout.setMargin(new MarginInfo(true,true,false,true));
    	packagesLayout.setSizeFull();
    	packagesLayout.setHeight("450px");
    	mainLayout.addComponent(packagesLayout);
		
	}
	
}
