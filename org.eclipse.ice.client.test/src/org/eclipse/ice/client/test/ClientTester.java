/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.ice.client.internal.Client;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.junit.Before;
import org.junit.Test;

/**
 * ClientTester checks the behavior and functionality of Client. It checks for
 * proper location setting and file system querying from Core.
 * 
 * This classes uses both the {@link org.eclipse.ice.client.test.FakeCore} and
 * the {@link org.eclipse.ice.client.test.FakeItemProcessor}.
 *
 * @author Jay Jay Billings
 */
public class ClientTester {

	/**
	 * The client to test
	 */
	private Client iCEClient;

	/**
	 * The fake Core used in the test
	 */
	private FakeCore fakeCore;

	/**
	 * The fake widget factory used in the test
	 */
	private FakeUIWidgetFactory fakeUIWidgetFactory;

	/**
	 * A fake form widget to make sure the form is shown
	 */
	private FakeFormWidget fakeFormWidget;

	/**
	 * A fake error box to make sure the error box is shown
	 */
	private FakeErrorBoxWidget fakeErrorBoxWidget;

	/**
	 * This operation configures the client for the test.
	 */
	@Before
	public void BeforeClass() {

		// Instantiate needed classes
		this.fakeCore = new FakeCore();
		this.iCEClient = new Client(new FakeItemProcessor());
		this.fakeUIWidgetFactory = new FakeUIWidgetFactory();

		// Setup the client
		this.iCEClient.setCoreService(fakeCore);
		this.iCEClient.setUIWidgetFactory(fakeUIWidgetFactory);

		return;
	}

	/**
	 * This operation checks the Client by making sure that Items can be loaded
	 * and displayed.
	 */
	@Test
	public void checkItemLoading() {

		// Local Declarations
		FakeFormWidget testFormWidget = null;
		FakeErrorBoxWidget textErrorWidget = null;

		// Reset the Factory
		this.fakeUIWidgetFactory.reset();

		// Check loading more than once
		for (int i = 1; i < 5; i++) {
			// Load an Item
			iCEClient.loadItem(i);
			// Check the Factory to make sure it was called
			assertTrue(fakeUIWidgetFactory.widgetRequested());
			// Make sure the FormWidget was displayed
			testFormWidget = fakeUIWidgetFactory.getLastFormWidget();
			assertTrue(testFormWidget.widgetDisplayed());
		}

		// Reset the Factory
		fakeUIWidgetFactory.reset();

		// Check that trying to load invalid widgets fails
		for (int i = -10; i < -5; i++) {
			// Load an Item
			iCEClient.loadItem(i);
			// Check the Factory to make sure it was called
			assertTrue(fakeUIWidgetFactory.widgetRequested());
			// Make sure an ErrorBoxWidget was displayed
			textErrorWidget = fakeUIWidgetFactory.getLastErrorBoxWidget();
			if (textErrorWidget instanceof FakeErrorBoxWidget) {
				assertTrue(textErrorWidget.widgetDisplayed());
			}
		}
		return;

	}

	/**
	 * This operation checks that we can add an IFormWidget to the Client.s
	 */
	@Test
	public void checkAddFormWidget() {
		IFormWidget widget = new FakeFormWidget();
		Form form = new Form();
		form.setItemID(1);
		widget.setForm(form);
		iCEClient.addFormWidget(widget);
		assertNotNull(iCEClient.getFormWidget(1));
	}

	/**
	 * This operation checks the Client by making sure that errors can be
	 * dispatched to the UI system to be displayed to the user.
	 */
	@Test
	public void checkThrowingErrors() {

		// Reset the Factory
		fakeUIWidgetFactory.reset();

		// Check loading more than once
		for (int i = 1; i < 5; i++) {
			// Load an Item
			iCEClient.throwSimpleError("Throw error " + i + "!");
			// Check the Factory to make sure it was called
			assertTrue(fakeUIWidgetFactory.widgetRequested());
			// Make sure the Widget was displayed
			FakeErrorBoxWidget testWidget = fakeUIWidgetFactory
					.getLastErrorBoxWidget();
			assertNotNull(testWidget);
			assertTrue(testWidget.widgetDisplayed());
		}

		return;

	}

	/**
	 * This operation checks the Client by creating an Item. It makes sure that
	 * the Client uses the UIWidgetFactory, uses a UIWidget and registers a
	 * UIWidgetListener with the UIWidget. Fakes are used for the
	 * UIWidgetFactory and UIWidget.
	 * <p>
	 * This operation also checks that getAvailableItemTypes returns the same
	 * list of available Item types as that in FakeCore: Red, Orange, Yellow,
	 * Green.
	 * </p>
	 *
	 */
	@Test
	public void checkItemCreation() {

		// Local Declarations
		int itemId = -1;
		ArrayList<String> types = new ArrayList<String>();

		// Check the list of available Item types
		types = iCEClient.getAvailableItemTypes();
		assertTrue(types.contains("Red"));
		assertTrue(types.contains("Orange"));
		assertTrue(types.contains("Yellow"));
		assertTrue(types.contains("Green"));

		// Create the Item - make sure it is something that can actually be
		// created!
		itemId = iCEClient.createItem("Red");

		// Check the Item id
		assertTrue(itemId > 0);

		// Make sure that the Factory was called to get the Widget
		assertTrue(fakeUIWidgetFactory.widgetRequested());

		// Get the FormWidget back since this one is a valid request
		fakeFormWidget = fakeUIWidgetFactory.getLastFormWidget();

		// Make sure that the FormWidget was called, has a registered listener
		// and was displayed.
		assertNotNull(fakeFormWidget);
		assertTrue(fakeFormWidget.formRegistered());
		assertTrue(fakeFormWidget.listenerRegistered());
		assertTrue(fakeFormWidget.widgetDisplayed());

		// Create another Item - make sure it is something that can't be
		// created!
		itemId = iCEClient.createItem("Spray Starch");

		// Check the Item id
		assertTrue(itemId < 0);

		// Make sure that the Factory was called to get a widget
		assertTrue(fakeUIWidgetFactory.widgetRequested());

		// Get the ErrorBoxWidget since this was not valid and an error should
		// have been displayed
		fakeErrorBoxWidget = fakeUIWidgetFactory.getLastErrorBoxWidget();

		// Make sure that the FormWidget was called, has a registered listener
		// and was displayed.
		assertNotNull(fakeErrorBoxWidget);
		// FIXME - Should test for exact string
		assertNotNull(fakeErrorBoxWidget.getErrorString());
		assertTrue(fakeErrorBoxWidget.widgetDisplayed());

		// Get the list of ICEObjects that represents the Items that have been
		// created and check it. The FakeCore cheats and sends back an ICEObject
		// with id 2.
		ArrayList<Identifiable> items = iCEClient.getItems();
		assertNotNull(items);
		assertEquals(1, items.size());
		assertEquals(2, items.get(0).getId());

		// Delete all of the Items
		for (Identifiable iceObj : items) {
			iCEClient.deleteItem(iceObj.getId());
		}
		// Make sure that there are no items
		assertEquals(0, iCEClient.getItems().size());
		System.out.println(
				"Num ITEMS after delete = " + iCEClient.getItems().size());

		return;
	}

	/**
	 * Check that the Core gets invoked when we rename an Item.
	 */
	@Test
	public void checkItemRenaming() {
		int itemId = iCEClient.createItem("Red");
		iCEClient.renameItem(itemId, "HELLO");
		assertTrue(fakeCore.itemNameChanged());
		fakeCore.reset();
	}

	/**
	 * This operation checks the Client by insuring that Items and Forms can be
	 * updated. It calls through the IUpdateWidgetListener interface. Review the
	 * documentation on the FakeCore class to determine the proper Form ids to
	 * use for the test.
	 */
	@Test
	public void checkItemUpdates() {

		// Local Declarations
		Form testForm = new Form();
		FakeFormWidget testWidget = new FakeFormWidget();

		// Set the Form for the FormWidget.
		testForm.setId(4);
		testForm.setName("Fluffy");
		testWidget.setForm(testForm);

		// Send the update
		iCEClient.formUpdated(testForm);

		// The Form should be accepted by the FakeCore since it has a positive
		// id. Make sure its name was changed to "passed" as specified by the
		// FakeCore class description.
		System.out.println("Name = " + testForm.getName());

		assertEquals("passed", testForm.getName());

		// Setup a Form for the update that will cause the FakeCore to spit out
		// a FormStatus.InfoError status.
		testForm.setId(8675309);
		testForm.setName("Bunny");
		testWidget.setForm(testForm);

		// Reset the widget factory
		fakeUIWidgetFactory.reset();

		// Run the update request again
		iCEClient.formUpdated(testForm);

		// The Client should throw up an ErrorBoxWidget since the FakeCore
		// will return FormStatus.InfoError for a negative Form id. Check an
		// make sure an error box was displayed and that the error string at
		// least contained some content.
		assertTrue(fakeUIWidgetFactory.widgetRequested());
		fakeErrorBoxWidget = fakeUIWidgetFactory.getLastErrorBoxWidget();
		assertNotNull(fakeErrorBoxWidget);
		assertTrue(fakeErrorBoxWidget.widgetDisplayed());
		assertNotNull(fakeErrorBoxWidget.getErrorString());
		assertTrue(fakeErrorBoxWidget.getErrorString().length() > 6);

		return;
	}

	/**
	 * This operation checks the ability of the client to correctly handle calls
	 * through the IProcessEventListener interface and by directly calling
	 * IClient.processItem().
	 */
	@Test
	public void checkItemProcessing() {

		// Local Declarations
		int itemId = -1;
		Form testForm = new Form();
		// Create the Item - make sure it is something that can actually be
		// created!
		itemId = iCEClient.createItem("Red");
		testForm.setItemID(itemId);

		// Check the Item id
		assertTrue(itemId > 0);

		// Process the Item through the IProcessEventListener interface
		iCEClient.processSelected(testForm, "blend");

		// Check the process status with the core to make sure the event was
		// processed
		assertEquals(FormStatus.Processed, fakeCore.getLastProcessStatus());

		// Try telling the client to cancel a process
		iCEClient.cancelRequested(testForm, "blend");

		// Check it
		assertTrue(fakeCore.wasCancelled());

		// Reset the FakeCore's Process state
		fakeCore.reset();

		// Process the Item through the IClient.processItem operation
		iCEClient.processItem(itemId, "blend");

		// Check the process status with the core to make sure the event was
		// processed
		assertEquals(FormStatus.Processed, fakeCore.getLastProcessStatus());

		// Reset the FakeCore's Process state
		fakeCore.reset();

		// Reset the FakeUIWidgetFactory's state
		fakeUIWidgetFactory.reset();

		// Process the Item through the IClient.processItem operation. This
		// time we are going to test the case where more information is needed.
		// Set the Action name to "NeedsInfo" so that the FakeCore will return
		// the proper return code, FormStatus.NeedsInfo.
		iCEClient.processItem(itemId, "NeedsInfo");

		// Check the process status to make sure the client made the call
		assertEquals(FormStatus.NeedsInfo, fakeCore.getItemStatus(0));
		assertEquals(FormStatus.NeedsInfo, fakeCore.getLastProcessStatus());

		// Make sure the widget was requested from the factory
		assertTrue(fakeUIWidgetFactory.widgetRequested());

		// Retrieve the info widget and get its Form
		FakeExtraInfoWidget infoWidget = fakeUIWidgetFactory
				.getLastExtraInfoWidget();
		assertNotNull(infoWidget);
		Form infoForm = infoWidget.getForm();

		// Check the Form and make sure the widget was displayed
		assertNotNull(infoForm);
		assertTrue(infoWidget.widgetDisplayed());

		// Make sure the Form was resubmitted to the core
		assertTrue(fakeCore.itemUpdated());

		// Get the streaming text widget and check it
		FakeStreamingTextWidget textWidget = fakeUIWidgetFactory
				.getLastStreamingTextWidget();
		assertNotNull(textWidget);
		// Check that the streaming text widget was displayed
		assertTrue(textWidget.widgetDisplayed());
		// Check that the output file was retrieved from the core
		assertTrue(fakeCore.outputFileRetrieved());
		// Check that the text was pushed to the streaming text widget
		assertTrue(textWidget.textPushed());

		return;
	}

	/**
	 * This operation checks the ability of the Client to load Resources when
	 * signaled to do so by FormWidgets and other classes to which it may
	 * subscribe.
	 */
	@Test
	public void checkResourceLoading() {

		// Post a resource load request to the Client
		try {
			iCEClient.loadResource(new ICEResource(new File("Insurrection")));
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		// Grab the FakeTextEditor and check it. It should have been displayed
		// and the Resource should have been set.
		assertTrue(fakeUIWidgetFactory.widgetRequested());
		FakeTextEditor lastEditor = fakeUIWidgetFactory.getLastTextEditor();
		assertNotNull(lastEditor);
		assertNotNull(lastEditor.getResource());
		assertTrue(lastEditor.widgetDisplayed());

		return;
	}

	/**
	 * This operation checks the Client to make sure that if the connectToCore()
	 * operation is called that the Client tries to load an ExtraInfoWidget.
	 */
	@Test
	public void checkRemoteConnection() {

		// Make sure passing null for the hostname returns false
		assertTrue(!iCEClient.connectToCore(null, 0));

		/*
		 * Further testing disabled for now until more requirements are
		 * gathered. ~JJB 20120525 15:36
		 *
		 * // Tell the client to connect to www. That is, no where. boolean
		 * status = iCEClient.connectToCore("www", 80);
		 *
		 * // Make sure the widget was requested from the factory
		 * assertTrue(fakeUIWidgetFactory.widgetRequested());
		 *
		 * // Retrieve the info widget and get its Form FakeExtraInfoWidget
		 * infoWidget = fakeUIWidgetFactory .getLastExtraInfoWidget();
		 * assertNotNull(infoWidget); Form infoForm = infoWidget.getForm();
		 *
		 * // The connection should have returned false assertTrue(!status);
		 */
		// FIXME - What happens when the request is cancelled?

		return;

	}

	/**
	 * This operation checks that the client can import a file and that it calls
	 * the Core to do so.
	 */
	@Test
	public void checkFileImport() {

		// Create a file to import
		File file = new File("file.test");

		// Direct the client to import the file
		iCEClient.importFile(file.toURI());

		// Check the fake core
		assertTrue(fakeCore.fileImported());

		// Reset the core
		fakeCore.reset();

		// Try importing the file as a input file
		int id = iCEClient.importFileAsItem(file.toURI(), "Red");

		// Make sure that the file was imported and the item was created
		assertTrue(fakeCore.fileImported());
		assertTrue(id > 0);

		// Reset the core
		fakeCore.reset();

		// Check we can import a file to
		// the given project
		IProject project = null;
		iCEClient.importFile(file.toURI(), project);
		assertTrue(fakeCore.fileImported());

		// Reset the core
		fakeCore.reset();

		// Check we can import to the project
		// with given name
		iCEClient.importFile(file.toURI(), "fakeProject");
		assertTrue(fakeCore.fileImported());

		// Reset the core
		fakeCore.reset();

		// Try importing the file as a input file to the project
		id = iCEClient.importFileAsItem(file.toURI(), "Red", project);

		// Make sure that the file was imported and the item was created
		assertTrue(fakeCore.fileImported());
		assertTrue(id > 0);

		// Reset the core
		fakeCore.reset();

		// Try importing the file as a input file to the project
		id = iCEClient.importFileAsItem(file.toURI(), "Red", "fakeProject");

		// Make sure that the file was imported and the item was created
		assertTrue(fakeCore.fileImported());
		assertTrue(id > 0);

		// Reset the core
		fakeCore.reset();

		IFile iFile = null;
		// Try importing the file as a input file to the project
		id = iCEClient.importFileAsItem(iFile, "Red");

		// Make sure that the file was imported and the item was created
		assertTrue(fakeCore.fileImported());
		assertTrue(id > 0);

		// Reset the core
		fakeCore.reset();

		return;

	}

	/**
	 * This operation tests
	 * {@link org.eclipse.ice.client.internal.Client.loadItem}.
	 */
	@Test
	public void checkLoad() {
		// Make sure the fake core is called on a load.
		IFile itemFile = new TestIFile();
		iCEClient.loadItem(itemFile);
		assertTrue(fakeCore.wasLoaded());
		return;
	}

	/**
	 * A dummy IFile for testing the loadItem function.
	 * 
	 * @author Jay Jay Billings
	 *
	 */
	public class TestIFile implements IFile {

		@Override
		public boolean isConflicting(ISchedulingRule rule) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean contains(ISchedulingRule rule) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void touch(IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setTeamPrivateMember(boolean isTeamPrivate)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setSessionProperty(QualifiedName key, Object value)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setResourceAttributes(ResourceAttributes attributes)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setReadOnly(boolean readOnly) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setPersistentProperty(QualifiedName key, String value)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public long setLocalTimeStamp(long value) throws CoreException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setLocal(boolean flag, int depth, IProgressMonitor monitor)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setHidden(boolean isHidden) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setDerived(boolean isDerived, IProgressMonitor monitor)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setDerived(boolean isDerived) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void revertModificationStamp(long value) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void refreshLocal(int depth, IProgressMonitor monitor)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void move(IProjectDescription description, boolean force,
				boolean keepHistory, IProgressMonitor monitor)
						throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void move(IProjectDescription description, int updateFlags,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void move(IPath destination, int updateFlags,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void move(IPath destination, boolean force,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isVirtual() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isTeamPrivateMember(int options) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isTeamPrivateMember() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isSynchronized(int depth) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isPhantom() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isLocal(int depth) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isLinked(int options) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isLinked() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isHidden(int options) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isHidden() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDerived(int options) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDerived() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isAccessible() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public IWorkspace getWorkspace() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getType() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getSessionProperty(QualifiedName key)
				throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<QualifiedName, Object> getSessionProperties()
				throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResourceAttributes getResourceAttributes() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public URI getRawLocationURI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IPath getRawLocation() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IPath getProjectRelativePath() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IProject getProject() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getPersistentProperty(QualifiedName key)
				throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<QualifiedName, String> getPersistentProperties()
				throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IPathVariableManager getPathVariableManager() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IContainer getParent() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getModificationStamp() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public IMarker getMarker(long id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public URI getLocationURI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IPath getLocation() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getLocalTimeStamp() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getFileExtension() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int findMaxProblemSeverity(String type, boolean includeSubtypes,
				int depth) throws CoreException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public IMarker[] findMarkers(String type, boolean includeSubtypes,
				int depth) throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IMarker findMarker(long id) throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean exists() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void deleteMarkers(String type, boolean includeSubtypes,
				int depth) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void delete(int updateFlags, IProgressMonitor monitor)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void delete(boolean force, IProgressMonitor monitor)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public IResourceProxy createProxy() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IMarker createMarker(String type) throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void copy(IProjectDescription description, int updateFlags,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void copy(IProjectDescription description, boolean force,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void copy(IPath destination, int updateFlags,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void copy(IPath destination, boolean force,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void clearHistory(IProgressMonitor monitor)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void accept(IResourceVisitor visitor, int depth, int memberFlags)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void accept(IResourceVisitor visitor, int depth,
				boolean includePhantoms) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void accept(IResourceProxyVisitor visitor, int depth,
				int memberFlags) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void accept(IResourceProxyVisitor visitor, int memberFlags)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void accept(IResourceVisitor visitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setContents(IFileState source, boolean force,
				boolean keepHistory, IProgressMonitor monitor)
						throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setContents(InputStream source, boolean force,
				boolean keepHistory, IProgressMonitor monitor)
						throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setContents(IFileState source, int updateFlags,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setContents(InputStream source, int updateFlags,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setCharset(String newCharset, IProgressMonitor monitor)
				throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setCharset(String newCharset) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void move(IPath destination, boolean force, boolean keepHistory,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isReadOnly() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IFileState[] getHistory(IProgressMonitor monitor)
				throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IPath getFullPath() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getEncoding() throws CoreException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public InputStream getContents(boolean force) throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public InputStream getContents() throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IContentDescription getContentDescription()
				throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getCharsetFor(Reader reader) throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getCharset(boolean checkImplicit) throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getCharset() throws CoreException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void delete(boolean force, boolean keepHistory,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void createLink(URI location, int updateFlags,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void createLink(IPath localLocation, int updateFlags,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void create(InputStream source, int updateFlags,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void create(InputStream source, boolean force,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void appendContents(InputStream source, boolean force,
				boolean keepHistory, IProgressMonitor monitor)
						throws CoreException {
			// TODO Auto-generated method stub

		}

		@Override
		public void appendContents(InputStream source, int updateFlags,
				IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method stub

		}
	};

}