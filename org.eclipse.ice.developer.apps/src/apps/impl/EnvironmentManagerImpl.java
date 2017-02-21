/**
 */
package apps.impl;

import apps.AppsPackage;
import apps.EnvironmentManager;
import apps.EnvironmentType;
import apps.IEnvironment;
import apps.docker.DockerFactory;
import apps.local.LocalFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Manager</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class EnvironmentManagerImpl extends MinimalEObjectImpl.Container implements EnvironmentManager {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnvironmentManagerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AppsPackage.Literals.ENVIRONMENT_MANAGER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * This method creates the correct IEnvironment instance 
	 * based on the user specified type String. Currently supported 
	 * are the Docker and Local File System Environment types.
	 * <!-- end-user-doc -->
	 */
	public IEnvironment createEnvironment(String type) {
		IEnvironment env = null;
		if (type.equals("Docker")) {
			 env = DockerFactory.eINSTANCE.createDockerEnvironment();
			 env.setType(EnvironmentType.DOCKER);
			 return env;
		} else if (type.equals("Local")) {
			env = LocalFactory.eINSTANCE.createLocalEnvironment();
			env.setType(EnvironmentType.LOCAL);
			return env;
		} else {
			throw new IllegalArgumentException("Invalid environment type ("+type+").");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> listExistingEnvironments() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEnvironment loadExistingEnvironment(String name) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public IEnvironment loadEnvironmentFromFile(String file) {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("environment", new XMIResourceFactoryImpl());
        // Obtain a new resource set
        ResourceSet resSet = new ResourceSetImpl();
        // Get the resource
        Resource resource = resSet.getResource(URI
                        .createURI(file), true);
        // Get the first model element and cast it to the right type, in my
        // example everything is hierarchical included in this first node
        IEnvironment environment = (IEnvironment) resource.getContents().get(0);
        return environment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public String persistToXMIString(IEnvironment environment) {
		 // Register the XMI resource factory for the .website extension

        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("env", new XMIResourceFactoryImpl());

        // Obtain a new resource set
        ResourceSet resSet = new ResourceSetImpl();

        // create a resource
        Resource resource = resSet.createResource(URI
                        .createURI("dummy.env"));
        
        // Get the first model element and cast it to the right type, in my
        // example everything is hierarchical included in this first node
        resource.getContents().add(environment);

        OutputStream output = new OutputStream()
        {
            private StringBuilder string = new StringBuilder();
            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b );
            }

            public String toString(){
                return this.string.toString();
            }
        };
        // now save the content.
        try {
                resource.save(output, Collections.EMPTY_MAP);
        } catch (IOException e) {
                e.printStackTrace();
        }
        
        return output.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void persistXMIToFile(IEnvironment environment, String fileName) {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("env", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

		// create a resource
		Resource resource = resSet.createResource(URI.createURI("fileName"));

		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		resource.getContents().add(environment);

		// now save the content.
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case AppsPackage.ENVIRONMENT_MANAGER___CREATE_ENVIRONMENT__STRING:
				return createEnvironment((String)arguments.get(0));
			case AppsPackage.ENVIRONMENT_MANAGER___LIST_EXISTING_ENVIRONMENTS:
				return listExistingEnvironments();
			case AppsPackage.ENVIRONMENT_MANAGER___LOAD_EXISTING_ENVIRONMENT__STRING:
				return loadExistingEnvironment((String)arguments.get(0));
			case AppsPackage.ENVIRONMENT_MANAGER___LOAD_ENVIRONMENT_FROM_FILE__STRING:
				return loadEnvironmentFromFile((String)arguments.get(0));
			case AppsPackage.ENVIRONMENT_MANAGER___PERSIST_TO_XMI_STRING__IENVIRONMENT:
				return persistToXMIString((IEnvironment)arguments.get(0));
			case AppsPackage.ENVIRONMENT_MANAGER___PERSIST_XMI_TO_FILE__IENVIRONMENT_STRING:
				persistXMIToFile((IEnvironment)arguments.get(0), (String)arguments.get(1));
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //EnvironmentManagerImpl
