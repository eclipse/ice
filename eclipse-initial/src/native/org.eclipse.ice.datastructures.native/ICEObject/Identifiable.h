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

#ifndef IDENTIFIABLE_H
#define IDENTIFIABLE_H
#include <string>
#include <memory>

namespace ICE_DS {

/**
 * This interface describes operations that would make a class uniquely identifiable to ICE.
 */
class Identifiable  {

public:

    /**
     * This operation sets the identification number of the Identifiable entity. It must be greater than zero.
     *
     * @param the id to set
     */
    virtual void setId(int id) = 0;

    /**
     * This operation retrieves the description of the Identifiable entity.
     *
     * @return the description
     */
    virtual std::string getDescription() const = 0;

    /**
     * This operation retrieves the identification number of the Identifiable entity.
     *
     * @return the id
     */
    virtual int getId() const = 0;

    /**
     * This operation sets the name of the Identifiable entity.
     *
     * @param the name to set
     */
    virtual void setName(const std::string name) = 0;

    /**
     * This operation retrieves the name of the Identifiable entity.
     *
     * @return the name
     */
    virtual std::string getName() const = 0;

    /**
     * This operation sets the description of the Identifiable entity.
     *
     * @param the description to set
     */
    virtual void setDescription(const std::string description) = 0;

    /**
     * Returns a clone of the object
     *
     * @return the cloned object
     */
    virtual std::shared_ptr<Identifiable> clone() = 0;

};  //end class Identifiable

}

#endif
