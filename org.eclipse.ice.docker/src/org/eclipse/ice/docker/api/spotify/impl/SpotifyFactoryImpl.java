/**
 */
package org.eclipse.ice.docker.api.spotify.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.ice.docker.api.spotify.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SpotifyFactoryImpl extends EFactoryImpl implements SpotifyFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SpotifyFactory init() {
		try {
			SpotifyFactory theSpotifyFactory = (SpotifyFactory)EPackage.Registry.INSTANCE.getEFactory(SpotifyPackage.eNS_URI);
			if (theSpotifyFactory != null) {
				return theSpotifyFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SpotifyFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotifyFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case SpotifyPackage.SPOTIFY_DOCKER_CLIENT: return createSpotifyDockerClient();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotifyDockerClient createSpotifyDockerClient() {
		SpotifyDockerClientImpl spotifyDockerClient = new SpotifyDockerClientImpl();
		return spotifyDockerClient;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotifyPackage getSpotifyPackage() {
		return (SpotifyPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SpotifyPackage getPackage() {
		return SpotifyPackage.eINSTANCE;
	}

} //SpotifyFactoryImpl
