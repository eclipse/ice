/**
 */
package org.eclipse.ice.docker.api;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.ice.docker.api.DockerapiPackage
 * @generated
 */
public interface DockerapiFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DockerapiFactory eINSTANCE = org.eclipse.ice.docker.api.impl.DockerapiFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Container Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Container Configuration</em>'.
	 * @generated
	 */
	ContainerConfiguration createContainerConfiguration();

	/**
	 * Returns a new object of class '<em>Std Out Console</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Std Out Console</em>'.
	 * @generated
	 */
	StdOutConsole createStdOutConsole();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DockerapiPackage getDockerapiPackage();

} //DockerapiFactory
