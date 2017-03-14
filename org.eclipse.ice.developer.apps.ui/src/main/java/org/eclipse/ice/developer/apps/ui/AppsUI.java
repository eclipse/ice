package org.eclipse.ice.developer.apps.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import apps.AppsFactory;
import apps.EnvironmentManager;

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

	private VerticalLayout mainLayout = new VerticalLayout();
	private HorizontalLayout packagesLayout = new HorizontalLayout();
	private HorizontalLayout gitAndOsBtnsLayout = new HorizontalLayout();
	private HorizontalLayout cnlAndValBtnsLayout = new HorizontalLayout();
	private HorizontalLayout advancedLayout = new HorizontalLayout();
	private VerticalLayout envLayout = new VerticalLayout();
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
	private List<Package> selectedPackages = new LinkedList<Package>();

	/**
	 * The OSGi bundle for this running service.
	 */
	private static Bundle bundle;

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		EnvironmentManager factory = AppsFactory.eINSTANCE.createEnvironmentManager();
		// IEnvironment environment = factory.createEmpty("Docker");

		searchTxtField.setWidth("100%");
		searchTxtField.setDescription("type filter text");
		searchTxtField.setInputPrompt("type filter text");

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
		packagesLayout.setMargin(new MarginInfo(true, true, false, true));
		packagesLayout.setSizeFull();
		packagesLayout.setHeight("450px");
		mainLayout.addComponent(packagesLayout);

		addRepoButton.addClickListener(e -> {
			repoWindow.setHeight("350");
			repoWindow.setWidth("460");
			addWindow(repoWindow);
		});
		addOSButton.addClickListener(e -> {
			osWindow.setHeight("400");
			osWindow.setWidth("500");
			addWindow(osWindow);
		});

		gitAndOsBtnsLayout.addComponents(addRepoButton, addOSButton);
		gitAndOsBtnsLayout.setSpacing(true);
		gitAndOsBtnsLayout.setMargin(new MarginInfo(false, true, false, true));

		advancedButton.addClickListener(e -> {
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
		advancedLayout.setMargin(new MarginInfo(false, true, false, true));

		cancelButton.setWidth("130px");
		validateButton.setWidth("130px");

		validateButton.addClickListener(e -> {

		});

		cnlAndValBtnsLayout.addComponents(cancelButton, validateButton);
		cnlAndValBtnsLayout.setMargin(new MarginInfo(false, true, false, false));

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
