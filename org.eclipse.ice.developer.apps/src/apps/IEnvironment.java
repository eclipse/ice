/**
 */
package apps;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IEnvironment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The IEnvironment interface provides the data attributes and methods necessary to describe a scientific application environment. It describes the name and type of this environment, the operation system it uses, the application it contains, and a list of dependent packages it requires. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.IEnvironment#getType <em>Type</em>}</li>
 *   <li>{@link apps.IEnvironment#getName <em>Name</em>}</li>
 *   <li>{@link apps.IEnvironment#getOs <em>Os</em>}</li>
 *   <li>{@link apps.IEnvironment#getDependentPackages <em>Dependent Packages</em>}</li>
 *   <li>{@link apps.IEnvironment#isDevelopmentEnvironment <em>Development Environment</em>}</li>
 *   <li>{@link apps.IEnvironment#isGenerateProject <em>Generate Project</em>}</li>
 *   <li>{@link apps.IEnvironment#getPrimaryApp <em>Primary App</em>}</li>
 * </ul>
 *
 * @see apps.AppsPackage#getIEnvironment()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IEnvironment extends EObject {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link apps.EnvironmentType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The type of this IEnvironment
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see apps.EnvironmentType
	 * @see #setType(EnvironmentType)
	 * @see apps.AppsPackage#getIEnvironment_Type()
	 * @model
	 * @generated
	 */
	EnvironmentType getType();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see apps.EnvironmentType
	 * @see #getType()
	 * @generated
	 */
	void setType(EnvironmentType value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The name of this environment
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see apps.AppsPackage#getIEnvironment_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Os</b></em>' attribute.
	 * The default value is <code>"fedora"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This environment's operating system. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Os</em>' attribute.
	 * @see #setOs(String)
	 * @see apps.AppsPackage#getIEnvironment_Os()
	 * @model default="fedora"
	 * @generated
	 */
	String getOs();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#getOs <em>Os</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Os</em>' attribute.
	 * @see #getOs()
	 * @generated
	 */
	void setOs(String value);

	/**
	 * Returns the value of the '<em><b>Dependent Packages</b></em>' containment reference list.
	 * The list contents are of type {@link apps.SpackPackage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The list of packages that the application in this environment depends on. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Dependent Packages</em>' containment reference list.
	 * @see apps.AppsPackage#getIEnvironment_DependentPackages()
	 * @model containment="true"
	 * @generated
	 */
	EList<SpackPackage> getDependentPackages();

	/**
	 * Returns the value of the '<em><b>Development Environment</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Indicates if this environment is for the development of a specific ScienceApp.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Development Environment</em>' attribute.
	 * @see #setDevelopmentEnvironment(boolean)
	 * @see apps.AppsPackage#getIEnvironment_DevelopmentEnvironment()
	 * @model default="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean isDevelopmentEnvironment();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#isDevelopmentEnvironment <em>Development Environment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Development Environment</em>' attribute.
	 * @see #isDevelopmentEnvironment()
	 * @generated
	 */
	void setDevelopmentEnvironment(boolean value);

	/**
	 * Returns the value of the '<em><b>Generate Project</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generate Project</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generate Project</em>' attribute.
	 * @see #setGenerateProject(boolean)
	 * @see apps.AppsPackage#getIEnvironment_GenerateProject()
	 * @model default="false" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean isGenerateProject();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#isGenerateProject <em>Generate Project</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate Project</em>' attribute.
	 * @see #isGenerateProject()
	 * @generated
	 */
	void setGenerateProject(boolean value);

	/**
	 * Returns the value of the '<em><b>Primary App</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Primary App</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Primary App</em>' containment reference.
	 * @see #setPrimaryApp(SpackPackage)
	 * @see apps.AppsPackage#getIEnvironment_PrimaryApp()
	 * @model containment="true"
	 * @generated
	 */
	SpackPackage getPrimaryApp();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#getPrimaryApp <em>Primary App</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Primary App</em>' containment reference.
	 * @see #getPrimaryApp()
	 * @generated
	 */
	void setPrimaryApp(SpackPackage value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This method directs the Environment to launch itself with its provided data. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean launch();

} // IEnvironment
