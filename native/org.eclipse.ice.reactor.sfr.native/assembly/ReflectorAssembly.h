/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
#ifndef REFLECTORASSEMBLY_H
#define REFLECTORASSEMBLY_H
#include "SFRAssembly.h"
#include <string>
#include "SFRRod.h"
#include "GridDataManager.h"
#include "SFRComponent.h"

namespace ICE_SFReactor {

// Class representing radial reflector assemblies. Differentiated from
// PinAssembly, as radial reflectors contain solid rods rather than pins.
class ReflectorAssembly : public SFRAssembly {

    private:

		// The shortest distance between centers of adjacent reflector rods.
        double rodPitch;

        // A GridDataManager used to manage the locations of rods within this assembly.
        std::shared_ptr<GridDataManager> rodManager;

    public:

        // Parameterized constructor with the size (number of rods) specified. 
        ReflectorAssembly(int size); 

        // Parameterized constructor with the name and size (number of rods) specified. 
        ReflectorAssembly(std::string name, int size); 

        // Deep copies the contents of the object from another object.
        ReflectorAssembly(const ReflectorAssembly & otherReflector);

        // Sets the rod pitch (shortest distance from rod center to an adjacent
        // rod center).
        void setRodPitch(double rodPitch);

        // Returns the rod pitch (shortest distance from rod center to an adjacent
        // rod center) as a double.
        double getRodPitch(); 

        // Adds the specified SFRRod to the Reflector assembly; returns true if
        // the operation is successful.
        bool addRod(std::shared_ptr<SFRRod> rod);

        // Removes the rod with the specified name from the ReflectorAssembly;
        // returns true if the operation is successful.
        bool removeRod(std::string name);

        // Removes the rod with the specified location (x, y coordinates) from
        // the ReflectorAssembly; returns true operation is successful.
        bool removeRodFromLocation(int row, int column);

        // Returns a std::string std::vector of all rod names contained within
        // the assembly.
        std::vector<std::string> getRodNames();

        // Returns the SFRRod with the specified name in the assembly. 
        std::shared_ptr<SFRRod> getRodByName(std::string name);

        // Returns the SFRRod at the specified location (x, y coordinates) in
        // the assembly.
        std::shared_ptr<SFRRod> getRodByLocation(int row, int column);

        // Returns an std::vector of locations within the assembly that are
        // occupied by the rod matching the specified name.
        std::vector<int> getRodLocations(std::string name);

        // An operation that overrides the SFRComposite's operation. This
        // operation does nothing and requires that the appropriate, more defined,
        // associated operation to be utilized on this class.
        void addComponent(std::shared_ptr<Component> child);

        // An operation that overrides the SFRComposite's operation. This
        // operation does nothing and requires that the appropriate, more defined,
        // associated operation to be utilized on this class.
        void removeComponent(int childId);

        // An operation that overrides the SFRComposite's operation. This
        // operation does nothing and requires that the appropriate, more defined,
        // associated operation to be utilized on this class.
        void removeComponent(std::string name);

        // Adds the rod with the specified name to the assembly in the specified
        // location. If the rod exists and the location is valid and is not
        // occupied by the same rod, this returns true.
        bool setRodLocation(std::string name, int row, int column);

        // Returns the number of rods in the assembly. 
        int getNumberOfRods(); 

        // Gets the IDataProvider (in this case, an SFRComponent) for a location
		// in the assembly. Data can be added to this provider. If the location
		// does not have an associated component, the return value will be null.
		std::shared_ptr<SFRComponent> getDataProviderByLocation(int row, int column);

        // Deep copies and returns a newly instantiated object. 
        std::shared_ptr<Identifiable> clone();

        // Overrides the equals operation to check the attributes on this object
        // with another object of the same type. Returns true if the objects are
        // equals. False otherwise.
        bool operator==(const ReflectorAssembly & otherReflector);

        // Overrides the equality operation for SFRComponents. This is necessary
        // when the code calling this operation does not know the assembly's
        // type and cannot call the more specific equality operation.
        virtual bool operator==(const SFRComponent & component);

};  //end class ReflectorAssembly

} // end namespace
#endif
