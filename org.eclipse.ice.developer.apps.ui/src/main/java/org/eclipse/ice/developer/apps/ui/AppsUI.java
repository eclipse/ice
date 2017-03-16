package org.eclipse.ice.developer.apps.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

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
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
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
import apps.docker.ContainerConfiguration;
import apps.docker.DockerEnvironment;
import apps.docker.DockerFactory;

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
public class AppsUI extends UI {
	
	/**
	 * A serialization ID - if you remove this, OSGI DS will fail!
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The HttpService Reference for the OSGI framework.
	 */
	static private HttpService httpService;
	
	/**
	 * The OSGi bundle for this running service.
	 */
	private static Bundle bundle;
	
	/**
	 * The main layout of the app
	 */
	private VerticalLayout mainLayout;
	
	
	/**
	 * Containers for beans
	 */
	private BeanContainer<String, OSPackage> osPackageContainer;
	private BeanContainer<String, SourcePackage> sourcePackageContainer;
	private BeanContainer<String, SpackPackage> spackPackageContainer;
	private BeanContainer<String, Docker> dockerContainer;
	private BeanContainer<String, Folder> folderContainer;
	
	/**
	 * Binders for folder and docker configurations data 
	 */
	private BeanFieldGroup<Docker> dockerBinder;
	private BeanFieldGroup<Folder> folderBinder;
	
	//BeanFieldGroup<SpackPackage> binder = new BeanFieldGroup<SpackPackage>(SpackPackage.class);
	
	@Override
    protected void init(VaadinRequest vaadinRequest) {
    	buildLayout();
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
    	Button cancelButton = new Button("Cancel");
    	Button validateButton = new Button("Validate");
    	
    	cancelButton.setWidth("130px");
    	validateButton.setWidth("130px");
    	//validateButton.setEnabled(false);
    	
    	validateButton.addClickListener( e -> {
    		validateFields();
    	});
    	
    	cnlAndValBtnsLayout.addComponents(cancelButton, validateButton);
    	cnlAndValBtnsLayout.setMargin(new MarginInfo(false,true,false,false));
    	
    	cnlAndValBtnsLayout.setSpacing(true);
    	mainLayout.addComponents(cnlAndValBtnsLayout);
    	mainLayout.setComponentAlignment(cnlAndValBtnsLayout, Alignment.BOTTOM_RIGHT);		
	}

	/**
	 * Validate user input
	 */
	private void validateFields() {
		
		// instantiate folder and docker containers
		dockerContainer = new BeanContainer<String, Docker>(Docker.class);
		folderContainer = new BeanContainer<String, Folder>(Folder.class);
		
		try {
			// validate and get data, if validation fails commitException is thrown
			dockerBinder.commit();
			
			// after successful validation add data to its container
			dockerContainer.addItem("containerId", dockerBinder.getItemDataSource().getBean());
			
		} catch (CommitException e) {
			Notification.show("Something is wrong with container configurations! :(",
					Notification.Type.WARNING_MESSAGE);
		}
		
		// same process as above
		try {
			folderBinder.commit();
			folderContainer.addItem("folderId", folderBinder.getItemDataSource().getBean());
		} catch (CommitException e) {
			Notification.show("Something is wrong with directory! :(",
					Notification.Type.WARNING_MESSAGE);
		}
    	
    	persistData();
	}


	/**
	 * Persist data entered by user
	 */
	private void persistData() {
    	EnvironmentManager manager = AppsFactory.eINSTANCE.createEnvironmentManager();
    	IEnvironment environment = manager.createEmpty("Docker");
    	//environment.setName("");
    	
    	// Set primary app
    	apps.Package primaryApp = AppsFactory.eINSTANCE.createSourcePackage();
    	environment.setPrimaryApp(primaryApp);
    	
    	// OS packages
    	apps.Package osPackage = AppsFactory.eINSTANCE.createOSPackage();
    	for (Object beanId : osPackageContainer.getItemIds()) {
    		Item bean = osPackageContainer.getItem(beanId);
    		osPackage.setName((String) bean.getItemProperty("name").getValue());
//    		p.setType();
//    		p.setVersion();
    		environment.getDependentPackages().add(osPackage);
    	}
    	
    	// Source packages
    	apps.Package sourcePackage = AppsFactory.eINSTANCE.createSourcePackage();
    	sourcePackage.setName((String) sourcePackageContainer.getContainerProperty("sourceId", "link").getValue());

    	// Container configurations (need to add 'additional commands' field)
    	DockerEnvironment dockerEnv = (DockerEnvironment) environment;
    	ContainerConfiguration config = DockerFactory.eINSTANCE.createContainerConfiguration();
    	dockerEnv.setContainerConfiguration(config);
    	config.setEphemeral((boolean) dockerContainer.getContainerProperty("containerId", "ephemeral").getValue());
    	config.setName((String) dockerContainer.getContainerProperty("containerId", "name").getValue());
    	config.setVolumesConfig((String) dockerContainer.getContainerProperty("containerId", "volumes").getValue());
    	
    	//manager.persistEnvironments();
	}


	/**
	 * Creates advanced button and it's functions and adds to main layout
	 */
	private void addAdvancedView() {
		HorizontalLayout advancedLayout = new  HorizontalLayout();
		VerticalLayout envLayout = new  VerticalLayout();
		Button advancedButton = new Button(FontAwesome.CARET_RIGHT);
		Label advancedLabel = new Label("Advanced");
		EnvironmentForm env = new EnvironmentForm();

		advancedButton.addClickListener( e -> {
			if (advancedButton.getIcon().equals(FontAwesome.CARET_RIGHT)) {
				advancedButton.setIcon(FontAwesome.CARET_DOWN);
				envLayout.addComponent(env);
				dockerBinder = env.getDockerBinder();
				folderBinder = env.getFolderBinder();
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
		AddRepoWindow repoWindow = new AddRepoWindow();
		AddOSWindow osWindow = new AddOSWindow();
		
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
		
		// get container with OS packages
		osPackageContainer = osWindow.getContainer();
		
		// get container with a source package
		sourcePackageContainer = repoWindow.getContainer();
		
		gitAndOsBtnsLayout.addComponents(addRepoButton, addOSButton);
		gitAndOsBtnsLayout.setSpacing(true);
		gitAndOsBtnsLayout.setMargin(new MarginInfo(false,true,false,true));
		mainLayout.addComponent(gitAndOsBtnsLayout);
		
	}

	/**
	 * Creates Spack packages representation and add it to main view.
	 */
	private void addSpackPackagesLayout() {
		HorizontalLayout packagesLayout = new  HorizontalLayout();
		VerticalLayout pkgPanelLayout = new VerticalLayout();
		VerticalLayout pkgListLayout = new VerticalLayout();
		VerticalLayout pkgDescrLayout = new VerticalLayout();
		Panel pkgsListPanel = new Panel();
		Panel pkgsDescrPanel = new Panel("Package(s) in basket:");
		TextField searchTxtField = new TextField("Packages:");
		
    	searchTxtField.setWidth("100%");
    	searchTxtField.setDescription("type filter text");
    	searchTxtField.setInputPrompt("type filter text");
    	
    	String path = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    	
		
		// This is just a dummy list - it is empty for now. Don't use a data
		// structure like this, use an EMF class! ~JJB
		List<Map<String, String>> spackPackages = new ArrayList<Map<String, String>>();

		for (Map<String, String> spackPackage : spackPackages) {
			final PackageListItem pkg = new PackageListItem();
			List lst = new LinkedList(); // Why is this untyped? ~JJB 20170314
			pkg.getPkgChBox().setCaption((String) spackPackage.get("name"));
			pkg.getPkgDescrTxtField().setCaption((String) spackPackage.get("description"));
			pkg.getPkgChBox().addValueChangeListener(e -> {
				Label pkgDescItem = new Label(pkg.getPkgChBox().getCaption());
				Label temp = null;
				if (pkg.getPkgChBox().getValue().equals(true)) {
					// SpackPackageImpl spackPackageImpl = new
					// SpackPackageImpl();

					lst.add(pkg.getPkgChBox().getCaption());
					pkgDescrLayout.addComponent(pkgDescItem);
				} else if (lst.contains(pkg.getPkgChBox().getCaption())) {
					Iterator<Component> iter = pkgDescrLayout.iterator();
					while (iter.hasNext()) {
						Label lbl = (Label) iter.next();
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
		
	}

	/**
	 * Creates main layout for the app
	 */
	private void buildLayout() {
		 mainLayout = new VerticalLayout();
		 mainLayout.setMargin(true);
		 mainLayout.setSpacing(true);
		 setContent(mainLayout);
	}

	@WebServlet(urlPatterns = "/*", name = "AppsUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AppsUI.class, productionMode = false)
    public static class AppsUIServlet extends VaadinServlet {
    }	
	

	/**
	 * OSGi bundle activator with annotation instead of activator class.
	 * 
	 * @param context
	 */
	@Activate
	public void start(BundleContext context) {
		System.out.println("App Store VAADIN bundle started.");
		bundle = context.getBundle();
		try {
			AppsUI.httpService.registerServlet("/", new AppsUIServlet(), null, null);
		} catch (ServletException | NamespaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setService(HttpService httpService) {
		AppsUI.httpService = httpService;
	}
}
