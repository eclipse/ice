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
 *   <li>{@link apps.IEnvironment#getSpackpackage <em>Spackpackage</em>}</li>
 *   <li>{@link apps.IEnvironment#getScienceapp <em>Scienceapp</em>}</li>
 * </ul>
 *
 * @see apps.EnvironmentPackage#getIEnvironment()
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
	 * @see apps.EnvironmentPackage#getIEnvironment_Type()
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
	 * @see apps.EnvironmentPackage#getIEnvironment_Name()
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This environment's operating system. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Os</em>' attribute.
	 * @see #setOs(String)
	 * @see apps.EnvironmentPackage#getIEnvironment_Os()
	 * @model
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
	 * Returns the value of the '<em><b>Spackpackage</b></em>' containment reference list.
	 * The list contents are of type {@link apps.SpackPackage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The list of packages that the application in this environment depends on. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Spackpackage</em>' containment reference list.
	 * @see apps.EnvironmentPackage#getIEnvironment_Spackpackage()
	 * @model containment="true"
	 * @generated
	 */
	EList<SpackPackage> getSpackpackage();

	/**
	 * Returns the value of the '<em><b>Scienceapp</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The primary application that this environment provides. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Scienceapp</em>' reference.
	 * @see #setScienceapp(ScienceApp)
	 * @see apps.EnvironmentPackage#getIEnvironment_Scienceapp()
	 * @model
	 * @generated
	 */
	ScienceApp getScienceapp();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#getScienceapp <em>Scienceapp</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scienceapp</em>' reference.
	 * @see #getScienceapp()
	 * @generated
	 */
	void setScienceapp(ScienceApp value);

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
