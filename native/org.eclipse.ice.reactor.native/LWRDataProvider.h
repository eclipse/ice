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

#ifndef LWRDATAPROVIDER_H
#define LWRDATAPROVIDER_H

#include <H5Cpp.h>
#include <vector>
#include <memory>
#include <ICEObject/Identifiable.h>
#include <IDataProvider.h>
#include "LWRData.h"
#include <map>
#include "FeatureSet.h"
#include <string>

namespace ICE_Reactor {

/**
 * <p>An implementation of the IDataProvider.  This class is used to store State
 * point data, usually for material decompositions or powers, that can be used to
 * store and display changes in value overtime across different features.</p>
 */
class LWRDataProvider : virtual public IDataProvider {

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

public:

    /**
     * Copy constructor
     */
    LWRDataProvider(LWRDataProvider & arg);

    /**
     * Destructor
     */
    virtual ~LWRDataProvider();

    /**
     * The constructor.
     */
    LWRDataProvider();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>Sets the sourceInfo.  Can not be null or the empty string.  Strings passed will be trimmed before being set.</p>
	 * <!-- end-UML-doc -->
	 * @param sourceInfo <p>The sourceInfo to set.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    void setSourceInfo(std::string sourceInfo);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>Adds a IData piece, keyed on the feature and timeStep, to the dataTree. If the feature exists in the tree, it will append to the end of the list.</p>
	 * <!-- end-UML-doc -->
	 * @param data <p>The data to add.</p>
	 * @param time
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    void addData(std::shared_ptr<LWRData> data, double time);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>Removes the feature and all associated IData from the dataTree at all time steps. If a user wishes to remove a single piece of IData from the tree, then use the appropriate getData operation on that feature and manipulate the data that way.</p>
	 * <!-- end-UML-doc -->
	 * @param feature <p>The feature.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    void removeAllDataFromFeature(std::string feature);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>Deep copies and returns a newly instantiated object.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The newly instantiated copied object.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	std::shared_ptr<LWRDataProvider> clone();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>Sets the time units.</p>
	 * <!-- end-UML-doc -->
	 * @param timeUnit <p>The time unit to be set.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	void setTimeUnits(std::string timeUnit);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>Equality check.  Returns true if equals, false otherwise.</p>
	 * <!-- end-UML-doc -->
	 * @param otherObject <p>Object to equate.</p>
	 * @return <p>True if equal, false otherwise.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	bool operator==(const LWRDataProvider & other) const;

	/**
	 * (non-Javadoc)
	 * @see IDataProvider#getFeatureList()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	virtual std::vector<std::string> getFeatureList();

	/**
	 * (non-Javadoc)
	 * @see IDataProvider#getNumberOfTimeSteps()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	virtual int getNumberOfTimeSteps();

	/**
	 * (non-Javadoc)
	 * @see IDataProvider#setTime(double step)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	virtual void setTime(double step);

	/**
	 * (non-Javadoc)
	 * @see IDataProvider#getDataAtCurrentTime(String feature)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	virtual std::vector< std::shared_ptr<IData> > getDataAtCurrentTime(const std::string feature);

	/**
	 * (non-Javadoc)
	 * @see IDataProvider#getSourceInfo()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	virtual std::string getSourceInfo();

	/**
	 * (non-Javadoc)
	 * @see IDataProvider#getFeaturesAtCurrentTime()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	virtual std::vector<std::string> getFeaturesAtCurrentTime();

	/**
	 * (non-Javadoc)
	 * @see IDataProvider#getTimes()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	virtual std::vector<double> getTimes();

	/**
	 * (non-Javadoc)
	 * @see IDataProvider#getTimeStep(double time)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	virtual int getTimeStep(double time);

	/**
	 * (non-Javadoc)
	 * @see IDataProvider#getTimeUnits()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	virtual std::string getTimeUnits();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>Returns the current time step.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The current time step.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	double getCurrentTime();

};  //end class LWRDataProvider

}

#endif
