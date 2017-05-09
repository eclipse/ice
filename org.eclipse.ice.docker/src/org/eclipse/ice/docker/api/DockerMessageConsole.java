/**
 */
package org.eclipse.ice.docker.api;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Docker Message Console</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.ice.docker.api.DockerapiPackage#getDockerMessageConsole()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface DockerMessageConsole extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void print(String msg);

} // DockerMessageConsole
