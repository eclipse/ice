/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>File System Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemConfiguration#getDirectory <em>Directory</em>}</li>
 * </ul>
 *
 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getFileSystemConfiguration()
 * @model
 * @generated
 */
public interface FileSystemConfiguration extends InstallerTypeConfiguration {
	/**
	 * Returns the value of the '<em><b>Directory</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Directory</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Directory</em>' attribute.
	 * @see #setDirectory(String)
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getFileSystemConfiguration_Directory()
	 * @model required="true"
	 * @generated
	 */
	String getDirectory();

	/**
	 * Sets the value of the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemConfiguration#getDirectory <em>Directory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Directory</em>' attribute.
	 * @see #getDirectory()
	 * @generated
	 */
	void setDirectory(String value);

} // FileSystemConfiguration
