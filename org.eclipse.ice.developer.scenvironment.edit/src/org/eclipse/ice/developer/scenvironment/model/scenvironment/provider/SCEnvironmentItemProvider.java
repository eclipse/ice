/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentFactory;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SCEnvironmentItemProvider 
	extends ItemProviderAdapter
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SCEnvironmentItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addImageNamePropertyDescriptor(object);
			addAvailableOSsPropertyDescriptor(object);
			addAvailablePackagesPropertyDescriptor(object);
			addSelectedPackagesPropertyDescriptor(object);
			addAddedFilesPropertyDescriptor(object);
			addInstallerTypePropertyDescriptor(object);
			addConfigurationTypePropertyDescriptor(object);
			addSpackpackagePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Image Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addImageNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SCEnvironment_imageName_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SCEnvironment_imageName_feature", "_UI_SCEnvironment_type"),
				 ScenvironmentPackage.Literals.SC_ENVIRONMENT__IMAGE_NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Available OSs feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAvailableOSsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SCEnvironment_availableOSs_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SCEnvironment_availableOSs_feature", "_UI_SCEnvironment_type"),
				 ScenvironmentPackage.Literals.SC_ENVIRONMENT__AVAILABLE_OSS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Available Packages feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAvailablePackagesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SCEnvironment_availablePackages_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SCEnvironment_availablePackages_feature", "_UI_SCEnvironment_type"),
				 ScenvironmentPackage.Literals.SC_ENVIRONMENT__AVAILABLE_PACKAGES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Selected Packages feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSelectedPackagesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SCEnvironment_selectedPackages_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SCEnvironment_selectedPackages_feature", "_UI_SCEnvironment_type"),
				 ScenvironmentPackage.Literals.SC_ENVIRONMENT__SELECTED_PACKAGES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Added Files feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAddedFilesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SCEnvironment_addedFiles_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SCEnvironment_addedFiles_feature", "_UI_SCEnvironment_type"),
				 ScenvironmentPackage.Literals.SC_ENVIRONMENT__ADDED_FILES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Installer Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInstallerTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SCEnvironment_installerType_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SCEnvironment_installerType_feature", "_UI_SCEnvironment_type"),
				 ScenvironmentPackage.Literals.SC_ENVIRONMENT__INSTALLER_TYPE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Configuration Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addConfigurationTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SCEnvironment_configurationType_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SCEnvironment_configurationType_feature", "_UI_SCEnvironment_type"),
				 ScenvironmentPackage.Literals.SC_ENVIRONMENT__CONFIGURATION_TYPE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Spackpackage feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSpackpackagePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SCEnvironment_spackpackage_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SCEnvironment_spackpackage_feature", "_UI_SCEnvironment_type"),
				 ScenvironmentPackage.Literals.SC_ENVIRONMENT__SPACKPACKAGE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(ScenvironmentPackage.Literals.SC_ENVIRONMENT__CONFIGURATION_TYPE);
			childrenFeatures.add(ScenvironmentPackage.Literals.SC_ENVIRONMENT__SPACKPACKAGE);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns SCEnvironment.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/SCEnvironment"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((SCEnvironment)object).getImageName();
		return label == null || label.length() == 0 ?
			getString("_UI_SCEnvironment_type") :
			getString("_UI_SCEnvironment_type") + " " + label;
	}
	

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(SCEnvironment.class)) {
			case ScenvironmentPackage.SC_ENVIRONMENT__IMAGE_NAME:
			case ScenvironmentPackage.SC_ENVIRONMENT__AVAILABLE_OSS:
			case ScenvironmentPackage.SC_ENVIRONMENT__AVAILABLE_PACKAGES:
			case ScenvironmentPackage.SC_ENVIRONMENT__SELECTED_PACKAGES:
			case ScenvironmentPackage.SC_ENVIRONMENT__ADDED_FILES:
			case ScenvironmentPackage.SC_ENVIRONMENT__INSTALLER_TYPE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__CONFIGURATION_TYPE:
			case ScenvironmentPackage.SC_ENVIRONMENT__SPACKPACKAGE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(ScenvironmentPackage.Literals.SC_ENVIRONMENT__CONFIGURATION_TYPE,
				 ScenvironmentFactory.eINSTANCE.createContainerConfiguration()));

		newChildDescriptors.add
			(createChildParameter
				(ScenvironmentPackage.Literals.SC_ENVIRONMENT__CONFIGURATION_TYPE,
				 ScenvironmentFactory.eINSTANCE.createFileSystemConfiguration()));

		newChildDescriptors.add
			(createChildParameter
				(ScenvironmentPackage.Literals.SC_ENVIRONMENT__SPACKPACKAGE,
				 ScenvironmentFactory.eINSTANCE.createSpackPackage()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return ScenvironmentEditPlugin.INSTANCE;
	}

}
