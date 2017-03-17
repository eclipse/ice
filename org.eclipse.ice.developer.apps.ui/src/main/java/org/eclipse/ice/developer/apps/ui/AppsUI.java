package org.eclipse.ice.developer.apps.ui;

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
	private BeanContainer<String, Environment> envContainer;
	private BeanContainer<String, OSPackage> osPackageContainer;
	private BeanContainer<String, SourcePackage> sourcePackageContainer;
	private BeanContainer<String, SpackPackage> spackPackageContainer;
	private BeanContainer<String, Docker> dockerContainer;
	private BeanContainer<String, Folder> folderContainer;
	private EnvironmentManager manager = AppsFactory.eINSTANCE.createEnvironmentManager();
	
	/**
	 * Binders for folder and docker configurations data 
	 */
	private BeanFieldGroup<Docker> dockerBinder;
	private BeanFieldGroup<Folder> folderBinder;
	private BeanFieldGroup<Environment> envBinder;
	
	//BeanFieldGroup<SpackPackage> binder = new BeanFieldGroup<SpackPackage>(SpackPackage.class);
	
	@Override
    protected void init(VaadinRequest vaadinRequest) {
    	buildLayout();
    	addEnvNameLayout();
    	addSpackPackagesLayout();
    	addOtherPackagesLayout();
    	addAdvancedView();
    	addValidateCancelButtons();    	
    }

	
    /**
     * Creates 'environment' field
     */
    private void addEnvNameLayout() {
    	// create layout that'll contain environment field
    	Environment environmentLayout = new Environment();
    	// create binder
    	envBinder = new BeanFieldGroup<Environment>(Environment.class);
		// set data source of the binder
		envBinder.setItemDataSource(environmentLayout);
		// bind member field fields of Environment
		envBinder.bindMemberFields(environmentLayout);
		// set the binder's buffer
		envBinder.setBuffered(true);
		// add layout to main view
    	mainLayout.addComponent(environmentLayout);
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
	 * Validate user input (need to check for empty fields/containers)
	 */
	private void validateFields() {
		
		// instantiate folder, docker, and environment containers
		dockerContainer = new BeanContainer<String, Docker>(Docker.class);
		folderContainer = new BeanContainer<String, Folder>(Folder.class);
		envContainer = new BeanContainer<String, Environment>(Environment.class);
		
		// adding data to docker container
		try {
			// validate and get data, if validation fails commitException is thrown
			dockerBinder.commit();
			
			// after successful validation add data to its container
			dockerContainer.addItem("containerId", dockerBinder.getItemDataSource().getBean());
			
		} catch (CommitException e) {
			Notification.show("Something is wrong with container configurations! :(",
					Notification.Type.WARNING_MESSAGE);
		}
		
		// adding data to folder container
		try {
			folderBinder.commit();
			folderContainer.addItem("folderId", folderBinder.getItemDataSource().getBean());
		} catch (CommitException e) {
			Notification.show("Something is wrong with directory! :(",
					Notification.Type.WARNING_MESSAGE);
		}
    	
		// adding data to environment container
		try {
			envBinder.commit();
			envContainer.addItem("environmentId", envBinder.getItemDataSource().getBean());
		} catch (CommitException e) {
			Notification.show("Something is wrong with environment name! :(",
					Notification.Type.WARNING_MESSAGE);
		}
		
    	persistData();  // persisting data
	}


	/**
	 * Persist data entered by user
	 */
	private void persistData() {
    	
    	IEnvironment environment = manager.createEmpty("Docker");
    	manager.setEnvironmentStorage(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentStorage());
    	manager.setConsole(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentConsole());
    	environment.setName((String) envContainer.getContainerProperty("environmentId", "name").getValue());
    	
    	// Set primary app
    	apps.Package primaryApp = AppsFactory.eINSTANCE.createSourcePackage();
    	environment.setPrimaryApp(primaryApp);
    	
    	// OS packages
    	for (Object beanId : osPackageContainer.getItemIds()) {
        	apps.OSPackage osPackage = AppsFactory.eINSTANCE.createOSPackage();
    		Item bean = osPackageContainer.getItem(beanId);
    		osPackage.setName((String) bean.getItemProperty("name").getValue());
//    		p.setType();
//    		p.setVersion();
    		environment.getDependentPackages().add(osPackage);
    	}
    	
    	// Source packages
    	apps.SourcePackage sourcePackage = AppsFactory.eINSTANCE.createSourcePackage();
    	sourcePackage.setName((String) sourcePackageContainer.getContainerProperty("sourceId", "name").getValue());
    	sourcePackage.setRepoURL((String) sourcePackageContainer.getContainerProperty("sourceId", "link").getValue());
    	sourcePackage.setBranch((String) sourcePackageContainer.getContainerProperty("sourceId", "branch").getValue());
    	
    	// Container configurations (need to add 'additional commands' field)
    	DockerEnvironment dockerEnv = (DockerEnvironment) environment;
    	ContainerConfiguration config = DockerFactory.eINSTANCE.createContainerConfiguration();
    	config.setEphemeral((boolean) dockerContainer.getContainerProperty("containerId", "ephemeral").getValue());
    	config.setName((String) dockerContainer.getContainerProperty("containerId", "name").getValue());
    	config.setVolumesConfig((String) dockerContainer.getContainerProperty("containerId", "volumes").getValue());
    	dockerEnv.setContainerConfiguration(config);

    	manager.persistEnvironments();
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
		AddOSWindow osWindow = new AddOSWindow(pkgDescrLayout);
		
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
		if (sourcePackageContainer.size() > 0) {
			for (Object itemId : sourcePackageContainer.getItemIds()) {
				Label label = new Label();
				Item item = sourcePackageContainer.getItem(itemId);
				label.setCaption((String) item.getItemProperty("name").getValue());
				pkgDescrLayout.addComponent(label);
			}
		}
		
		
		gitAndOsBtnsLayout.addComponents(addRepoButton, addOSButton);
		gitAndOsBtnsLayout.setSpacing(true);
		gitAndOsBtnsLayout.setMargin(new MarginInfo(false,true,false,true));
		mainLayout.addComponent(gitAndOsBtnsLayout);
		
	}

	private VerticalLayout pkgDescrLayout = new VerticalLayout();
	
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
					pkgDescrLayout.addComponent(pkgDescItem);

				} else if (!checkBoxChecked) {
					// if a package is de-selected, remove it from the basket
					findAndRemoveFromBasket(checkBoxCaption, pkgDescrLayout);
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
	 * Deletes a package from the basket if it's there.
	 * @param packageName name of the package
	 * @param basket the layout (basket)
	 */
	private boolean findAndRemoveFromBasket(String packageName, VerticalLayout basket) {
		for (Component label : basket) {
			if (label.getCaption().equals(packageName)) {
				basket.removeComponent(label);
				return true;
			}
		}
		return false;
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
