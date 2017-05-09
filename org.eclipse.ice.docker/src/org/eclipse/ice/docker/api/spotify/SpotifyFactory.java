/**
 */
package org.eclipse.ice.docker.api.spotify;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.ice.docker.api.spotify.SpotifyPackage
 * @generated
 */
public interface SpotifyFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SpotifyFactory eINSTANCE = org.eclipse.ice.docker.api.spotify.impl.SpotifyFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Docker Client</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Docker Client</em>'.
	 * @generated
	 */
	SpotifyDockerClient createSpotifyDockerClient();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	SpotifyPackage getSpotifyPackage();

} //SpotifyFactory
