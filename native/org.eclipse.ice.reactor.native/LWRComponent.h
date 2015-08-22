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

#ifndef LWRCOMPONENT_H
#define LWRCOMPONENT_H

#include "HDF5LWRTagType.h"
#include "FeatureSet.h"
#include <string>
#include "LWRData.h"
#include "HDF5LWRTagType.h"
#include <updateableComposite/Component.h>
#include "IDataProvider.h"
#include <IHdfWriteable.h>
#include <IHdfReadable.h>
#include <ICEObject/Identifiable.h>
#include <map>
#include <memory>
#include <H5Cpp.h>

namespace ICE_Reactor {

/**
  * <p>The LWRComponent class represents all reactor components that can be stored
  * in a LWRComposite. This class should be treated as an extension of the Java
  * Object class where identifying pieces of information are stored on this object.
  * This class also implements the IDataProvider interface in order to provide state
  * point data capabilities for individual, unique instances of this extended class.
  * A key note to understand about this implementation for IDataProvider is that for
  * individual changes on a particular position, say a Rod at a at a certain
  * coordinate within an assembly changes it's material density, then this data
  * should be stored at a higher level within the LWRDataProvider on the Assembly.
  * The IDataProvider implementation here is designed specifically for showing
  * changes across all named positions of the same type. If ,for example, the data
  * changed across the same assembly across all of the same rod types, then the
  * statepoint data should be stored on this class.</p>
  *
  * <p>This class implements
  * the ICE Component interface.</p>
 * <!-- end-UML-doc -->
 */
class LWRComponent: virtual public ICE_DS::Component, virtual public IDataProvider, virtual public ICE_IO::IHdfWriteable, virtual public ICE_IO::IHdfReadable {


private:

    /**
     * A TreeMap implementation of IData and features. Keep in mind that there can be multiple IData for the same feature.
     */
    std::map<double, std::vector< std::shared_ptr <FeatureSet> > > dataTree;

    /**
     * The current time step. Can not be less than 0, and must be strictly less than the number of TimeSteps.  Defaults to 0.
     */
    double time;

    /**
     * A description of the source of information for this provider and its data.
     */
    std::string sourceInfo;

    /**
     * The time unit.
     */
    std::string timeUnit;

    /**
     * Names of Groups
     */
    std::string dataH5GroupName;
    std::string timeStepNamePrefix;

protected:

    /**
     * Classifies a LWRComponentType.  This should only be set on the lowest level constructor AND should represent the lowest level object.
     */
    HDF5LWRTagType HDF5LWRTag;

    /**
     * The description of this LWRComponent.
     */
    std::string description;

    /**
     * The id of this LWRComponent. Can not be less than zero.
     */
    int id;

    /**
     * Name of this LWRComponent.
     */
    std::string name;


public:

    /**
     * Struct used for HDF5
     */
    typedef struct {
        double value;
        double uncertainty;
        char * units;
        double position[3];
    } dataSet_t;

    /**
     * Copy Constructor
     */
    LWRComponent(LWRComponent & arg);

    /**
     * The Nullary Constructor
     */
    LWRComponent();
    /**
     * The Destructor
     */
    ~LWRComponent();

    /**
     * A parameterized Constructor.
     *
     * @param the name
     */
    LWRComponent(const std::string name);

    /**
     * Overrides the equals operator
     *
     * @param The object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const LWRComponent &other) const;

    /**
     * Sets the sourceInfo.  Can not be null or the empty string.  std::strings passed will be trimmed before being set.
     *
     * @param the sourceInfo to set
     */
    void setSourceInfo(const std::string sourceInfo);

    /**
     * Adds a IData piece, keyed on the feature and timeStep, to the dataTree. If the feature exists in the tree, it will append to the end of the list.
     *
     * @param The LWRData
     * @param The time
     */
    void addData(std::shared_ptr <LWRData> data, double time);

    /**
     * Removes the feature and all associated IData from the dataTree at all time steps. If a user wishes to remove a single piece of IData from the tree, then use the appropriate getData operation on that feature and manipulate the data that way.
     *
     * @param The feature
     */
    void removeAllDataFromFeature(const std::string feature);

    /**
     * Returns the current time step.
     *
     * @return the current time
     */
    double getCurrentTime();

    /**
     * Returns the LWRComponentType.
     *
     * @return The TagType
     */
    HDF5LWRTagType getHDF5LWRTag();

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @returns the newly instantiated object.
     */
    std::shared_ptr<ICE_DS::Identifiable> clone();

    /**
     * This operation registers a listener that realizes the IComponentListener interface with the Component so that it can receive notifications of changes to the Component if the Component publishes such information.
     *
     * @param The listener
     */
    void registerListener(std::shared_ptr<ICE_DS::IComponentListener> listener) {
        return;
    };

    /**
     * This operation directs the Component to call back to an
     * IComponentVisitor so that the visitor can perform its required actions
     * for the exact type of the Component.
     *
     * @param The visitor
     */
    void accept(std::shared_ptr<ICE_DS::IComponentVisitor> visitor) {
        return;
    };

    /**
     * This operation notifies a class that has implemented IUpdateable that the value associated with the particular key has been updated.
     *
     * @param The key
     *
     * @param The value
     */
    void update(const std::string updatedKey,
                const std::string newValue) {
        return;
    };

    /**
     * This operation sets the identification number of the Identifiable entity. It must be greater than zero.
     *
     * @param The id to set
     */
    void setId(int id);

    /**
     * This operation retrieves the description of the Identifiable entity.
     *
     * @return The Description
     */
    std::string getDescription() const;

    /**
     * This operation retrieves the identification number of the Identifiable entity.
     *
     * @return The Id
     */
    int getId() const;

    /**
     * This operation sets the name of the Identifiable entity.
     *
     * @param The name to set
     */
    void setName(const std::string name);

    /**
     * This operation retrieves the name of the Identifiable entity.
     *
     * @return the name
     */
    std::string getName() const;

    /**
     * This operation sets the description of the Identifiable entity.
     *
     * @param the description
     */
    void setDescription(const std::string description);

    /**
     * Sets the time units.
     *
     * @param the time units
     */
    void setTimeUnits(const std::string timeUnit);

    /**
     * This operation returns the list of features available across all time steps (pin-power, temperature, etc).
     *
     * @return The list of features
     */
    virtual std::vector<std::string> getFeatureList();

    /**
     * This operation returns the total number of time steps.
     *
     * @return the number of time steps
     */
    virtual int getNumberOfTimeSteps();

    /**
     * This operation sets the current time step for which data should be retrieved.  It is 0-indexed such that time step 0 is the initial state and time step 1 is the state after the first time step.  This operation should be called to set the current time step before data is retrieved from the provider.  The provider will always default to the initial state.
     *
     * @param the step
     */
    virtual void setTime(double step);

    /**
     * This operation returns all of the data (as IData[*]) related to a particular feature for this provider at a specific time step.  This operation will return null if no data is available and such a situation will most likely signify an error.
     *
     * @param The feature
     *
     * @return the list of IData at the feature
     */
    virtual std::vector<std::shared_ptr<IData> > getDataAtCurrentTime(const std::string feature);

    /**
     * This operation is a description of the source of information for this provider and its data.
     *
     * @return the source info
     */
    virtual std::string getSourceInfo();

    /**
     * Returns the list of features at the current time.
     *
     * @return the list of features
     */
    virtual std::vector<std::string> getFeaturesAtCurrentTime();

    /**
     * Returns all the times in ascending order.
     *
     * @return the list of times
     */
    virtual std::vector<double> getTimes();

    /**
     *  Returns the integer time based upon the time step.  Returns -1 if the time does not exist.
     *
     *  @param time
     */
    virtual int getTimeStep(double time);

    /**
     * Returns the time units.
     *
     * @return the time units
     */
    virtual std::string getTimeUnits();

    /**
     * This operation creates and returns a child H5Group for the parentH5Group in the h5File. If h5File is null or can not be opened, then null is returned. If parentH5Group is null, then null is returned. If an exception is thrown, then null is returned.
     *
     * @param File
     * @param Group
     *
     * @return the group
     */
    virtual std::shared_ptr<H5::Group> createGroup(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> parentH5Group);

    /**
     * This operation returns an ArrayList of IHdfWriteable child objects. If this IHdfWriteable has no IHdfWriteable child objects, then null is returned.
     *
     * @return list of IHdfWriteable
     */
    virtual std::vector< std::shared_ptr<IHdfWriteable> >getWriteableChildren() {
        std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > group;
        return group;
    };

    /**
     * This operation writes HDF5 Attributes to the metadata of h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Attributes, then false is returned. Otherwise, true is returned.
     *
     * @param The File
     * @param The Group
     *
     * @return True if successful, false otherwise
     */
    virtual bool writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group);

    /**
     * This operation writes HDF5 Datasets to the h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Datasets, then false is returned. Otherwise, true is returned.
     *
     * @param The File
     * @param The Group
     *
     * @return True if successful, false otherwise
     */
    virtual bool writeDatasets(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group);

    /**
     * This operation assigns the reference of the provided iHdfReadable to a class variable. If iHdfReadable is null, then false is returned. If iHdfReadable fails casting, then false is returned. Otherwise, true is returned.
     *
     * @param The child to be read
     *
     * @return True if successful, false otherwise
     */
    virtual bool readChild(std::shared_ptr<IHdfReadable> iHdfReadable);

    /**
     * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
     *
     * @param  The Group
     *
     * @return True if successful, false otherwise
     */
    virtual bool readAttributes(std::shared_ptr<H5::Group> h5Group);

    /**
     * This operation reads Datasets from h5Group and assigns their values to class variables. If h5Group is null or an Exception is thrown, false is returned. If the Otherwise, true is returned.
     *
     * @param The Group
     *
     * @return True if successful, false otherwise
     */
    virtual bool readDatasets(std::shared_ptr<H5::Group> h5Group);

};
}

#endif
