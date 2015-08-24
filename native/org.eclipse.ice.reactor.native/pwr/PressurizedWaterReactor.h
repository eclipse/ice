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

#ifndef PRESSURIZEDWATERREACTOR_H
#define PRESSURIZEDWATERREACTOR_H

#include "../LWReactor.h"
#include "../LWRComposite.h"
#include "../LWRGridManager.h"
#include "../GridLabelProvider.h"
#include "ControlBank.h"
#include "FuelAssembly.h"
#include "IncoreInstrument.h"
#include "RodClusterAssembly.h"
#include "../GridLocation.h"
#include <H5Cpp.h>
#include <vector>
#include <string>
#include <memory>
#include "../AssemblyType.h"
#include <map>

namespace ICE_Reactor {

/**
 * <p>The PressurizedWaterReactor (PWReactor) class represents any Pressurized
 * Water Reactor. This class extends the LWReactor and provides a defined
 * utility for representing a PWReactor.  It can store specific assemblies
 * in their own grids, so that one type of assembly can share the same position
 * as another type.</p>
 *
 * <p>Keep in mind although there are many ways to prevent
 * erroneous PressurizedWaterReactors from being built, this class should not
 * manage the deeper logic behind building a reactor.  For example, there is
 * no logic to stop a user from setting all assemblies in all locations
 * (although it does have logic to stop two  assemblies of the same type to
 * be stored at the same location).  The deeper types of logic are up to the
 * user of this class who delegates the conversion between their reactor model
 * to this model.  This is to allow flexibility and portability to many
 * applications so that other users can apply this model to earlier or later
 * versions of a PressurizedWaterReactor with minimal restrictions.</p>
 *
 * <p>Please note that when the add&lt;Component&gt;() operation is used, if a
 * &lt;Component&gt; with the same name exists in the &lt;Component&gt;
 * collection, then the &lt;Component&gt; will not be added. When using the
 * set&lt;Component&gt;Location() operation, if a &lt;Component&gt;with the
 * same name exists at the provided location, then the current
 * &lt;Component&gt; name at the provided location will be overwritten. Here,
 * &lt;Component&gt; can be (but not limited to) "ControlBanks", "FuelAssembly",
 * "IncoreInstrument", or "RodClusterAssembly".  Please check the enumeration
 * called AssemblyTypes for a complete list of Assemblies that could be managed
 * by this class.</p>
 */
class PressurizedWaterReactor : public LWReactor {

private:

    /**
     * The distance between assemblies in the core, determined by the seating location in the core plates.
     */
    double fuelAssemblyPitch;

    /**
     * The GridLabelProvider for this PressurizedWaterReactor.
     */
    std::shared_ptr<GridLabelProvider> gridLabelProvider;

    /**
     * Used in HDF5 Naming conventions
     */
    std::string CONTROL_BANK_COMPOSITE_NAME;

    /**
     * Used in HDF5 Naming conventions
     */
    std::string FUEL_ASSEMBLY_COMPOSITE_NAME;

    /**
     * Used in HDF5 Naming conventions
     */
    std::string INCORE_INSTRUMENT_COMPOSITE_NAME;

    /**
     * Used in HDF5 Naming conventions
     */
    std::string ROD_CLUSTER_ASSEMBLY_COMPOSITE_NAME;

    /**
     * Used in HDF5 Naming conventions
     */
    std::string CONTROL_BANK_GRID_MANAGER_NAME;

    /**
     * Used in HDF5 Naming conventions
     */
    std::string FUEL_ASSEMBLY_GRID_MANAGER_NAME;

    /**
     * Used in HDF5 Naming conventions
     */
    std::string INCORE_INSTRUMENT_GRID_MANAGER_NAME;

    /**
     * Used in HDF5 Naming conventions
     */
    std::string ROD_CLUSTER_ASSEMBLY_GRID_MANAGER_NAME;

    /**
     * Used in HDF5 Naming conventions
     */
    std::string GRID_LABEL_PROVIDER_NAME;

    /**
      * The map of grid managers, keyed by AssemblyType, that manage state point
      * data for the assemblies.
      */
    std::shared_ptr< std::map<AssemblyType, std::shared_ptr< LWRGridManager > > > managers;
    /**
      * The map of assembly composites, keyed by AssemblyType, that represent the
      * collections of the different assemblies in this reactor.
      *
      */
    std::shared_ptr< std::map<AssemblyType, std::shared_ptr< LWRComposite > > > assemblyComposites;

public:

    /**
     * The Copy Constructor
     */
    PressurizedWaterReactor(PressurizedWaterReactor & arg);

    /**
     * The Destructor
     */
    virtual ~PressurizedWaterReactor();

    /**
     * A parameterized Constructor.
     *
     * @param the size
     */
    PressurizedWaterReactor(int size);

    /**
     * Returns the number of fuel assemblies across the core.
     *
     * @return the size
     */
    int getSize();

    /**
     * Returns the GridLabelProvider for this PressurizedWaterReactor.
     *
     * @return the grid label provider
     */
    std::shared_ptr<GridLabelProvider> getGridLabelProvider();

    /**
     * Sets the GridLabelProvider for this PressurizedWaterReactor.
     *
     * @param the grid label provider to set
     */
    void setGridLabelProvider(std::shared_ptr<GridLabelProvider> gridLabelProvider);

    /**
     * Returns the distance between assemblies in the core, determined by the seating location in the core plates.
     *
     * @return the fuelAssemblyPitch
     */
    double getFuelAssemblyPitch();

    /**
     * Sets the distance between assemblies in the core, determined by the seating location in the core plates.
     *
     * @param the fuelAssemblyPitch to set
     */
    void setFuelAssemblyPitch(double fuelAssemblyPitch);

    /**
     * This operation writes HDF5 Attributes to the metadata of h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Attributes, then false is returned. Otherwise, true is returned.
     *
     * @param The File
     * @param The Group
     *
     * @return True if successful, false otherwise
     */
    bool writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group);

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const PressurizedWaterReactor& otherObject) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return the newly instantiated object
     */
    std::shared_ptr<ICE_DS::Identifiable> clone();

    /**
     * Returns the list of writeable childen.
     *
     * @return List of writeable chidlren
     */
    std::vector<std::shared_ptr<ICE_IO::IHdfWriteable> > getWriteableChildren();

    /**
     * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
     *
     * @param  The Group
     *
     * @return True if successful, false otherwise
     */
    bool readAttributes(std::shared_ptr<H5::Group> h5Group);

    /**
     * This operation reads IHdfReadable child objects. If this IHdfReadable has no IHdfReadable child objects, false is returned.
     *
     * @param the readable child
     *
     * @return true if successful, false otherwise
     */
    bool readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation adds an assembly of the specified AssemblyType to the
	 * reactor and return true. If an assembly of the same name and type already
	 * exists in the reactor, then the new assembly will not be added and this
	 * operation will return false.
	 * </p>
	 * <!-- end-UML-doc -->
	 *
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param assembly
	 *            <p>
	 *            The assembly to add to the collection of FuelAssemblies.
	 *            </p>
	 * @return <p>
	 *         True, if the assembly was added successfully.
	 *         </p>
	 * @generated
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	bool addAssembly(AssemblyType type, std::shared_ptr<LWRComponent> assembly);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes an assembly of the specified type from the collection of
	 * assemblies. Returns true if the operation was successful, false
	 * otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 *
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param assemblyName
	 *            <p>
	 *            The name of the assembly to be removed.
	 *            </p>
	 * @return <p>
	 *         True, if the assembly was removed successfully.
	 *         </p>
	 * @generated
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	bool removeAssembly(AssemblyType type, std::string assemblyName);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an list of names for each assembly in the reactor of the
	 * specified type.
	 * </p>
	 * <!-- end-UML-doc -->
	 *
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @return <p>
	 *         An ArrayList of names for each element of the collection of
	 *         assemblies.
	 *         </p>
	 * @generated
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	std::vector<std::string> getAssemblyNames(AssemblyType type);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the assembly of the specified type with the provided name or null
	 * if an assembly of that type and name does not exist.
	 * </p>
	 * <!-- end-UML-doc -->
	 *
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param name
	 *            <p>
	 *            The name of the assembly to find.
	 *            </p>
	 * @return <p>
	 *         The assembly
	 *         </p>
	 * @generated
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	std::shared_ptr<LWRComponent> getAssemblyByName(AssemblyType type, std::string name);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the assembly of the specified type at the specified column and
	 * row in the reactor or null if one is not found at the provided location.
	 * </p>
	 * <!-- end-UML-doc -->
	 *
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param row
	 *            <p>
	 *            The row id.
	 *            </p>
	 * @param column
	 *            <p>
	 *            The column id.
	 *            </p>
	 * @return <p>
	 *         The assembly corresponding to the provided type, column and row
	 *         or null if one is not found at the provided location.
	 *         </p>
	 * @generated
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	std::shared_ptr<LWRComponent> getAssemblyByLocation(AssemblyType type, int row,
			int column);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the location for the assembly of the specified type with the
	 * provided name. Overrides the location of another assembly as required.
	 * Returns true if this operation was successful, false otherwise. Note it
	 * will return true if the same name is overridden.
	 * </p>
	 * <!-- end-UML-doc -->
	 *
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param assemblyName
	 *            <p>
	 *            The name of the assembly.
	 *            </p>
	 * @param row
	 *            <p>
	 *            The row id.
	 *            </p>
	 * @param column
	 *            <p>
	 *            The column id.
	 *            </p>
	 * @return <p>
	 *         True, if the location of the assembly was set successfully.
	 *         </p>
	 * @generated
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	bool setAssemblyLocation(AssemblyType type, std::string assemblyName,
			int row, int column);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the assembly at the provided location and of the specified if it
	 * exists. Returns true if the removal was successful, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 *
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param row
	 *            <p>
	 *            The row id.
	 *            </p>
	 * @param column
	 *            <p>
	 *            The column id.
	 *            </p>
	 * @return <p>
	 *         True, if the assembly removal was successful.
	 *         </p>
	 * @generated
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	bool removeAssemblyFromLocation(AssemblyType type, int row,
			int column);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the data provider for the assembly of the specified type at the
	 * given location or null if it does not exist.
	 * </p>
	 * <!-- end-UML-doc -->
	 *
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param row
	 *            <p>
	 *            The row id.
	 *            </p>
	 * @param column
	 *            <p>
	 *            The column id.
	 *            </p>
	 * @return <p>
	 *         The DataProvider that manages state point data for the specified
	 *         assembly.
	 *         </p>
	 * @generated
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	std::shared_ptr<LWRDataProvider> getAssemblyDataProviderAtLocation(AssemblyType type,
			int row, int column);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the number of assemblies of the specified type.
	 * </p>
	 * <!-- end-UML-doc -->
	 *
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @return <p>
	 *         The number of assemblies of the specified type.
	 *         </p>
	 * @generated
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	int getNumberOfAssemblies(AssemblyType type);

};

}

#endif
