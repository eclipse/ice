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
#ifndef PINASSEMBLY_H
#define PINASSEMBLY_H
#include "SFRAssembly.h"
#include "PinType.h"
#include "SFRPin.h"
#include <string>
#include "GridDataManager.h"
#include "SFRComponent.h"

namespace ICE_SFReactor {

// Class representing any assemblies in a SFR that contain pins. This includes
// both fuel pins (core and blanket), in addition to control assemblies
// (primary or secondary/shutdown), test assemblies, and shield assemblies. The
// distinction between the pin assembly type is made using the pinType attribute.
class PinAssembly : public SFRAssembly {

    private:

        // Shortest distance between centers of adjacent pins. 
        double pinPitch;

        // The pinType of the pin assembly; can be either core fuel, blanket
        // fuel, primary control or secondary control.
        PinType pinType;

        // The physical size of the inner duct within the assembly. Defined as
        // the distance from one outer duct surface to the outer surface
        // parallel to it. Since control assemblies (PrimaryControl,
        // SecondaryControl) are the only PinAssembly type with inner ducts,
        // this attribute will be 0 for all other PinAssembly types.
        double innerDuctFlatToFlat;

        // Thickness of the assembly's inner duct wall. Since control assemblies
        // (PrimaryControl, SecondaryControl) are the only PinAssembly type with
        // inner ducts, this attribute will be 0 for all other PinAssembly types.
        double innerDuctThickness;

        // A GridDataManager used to manage the locations of pins within this
        // assembly.
        std::shared_ptr<GridDataManager> pinManager;

    public:

        // Parameterized constructor with the assembly size (number of pins)
        // specified.
        PinAssembly(int size); 

        // Parameterized constructor with the assembly size (number of pins),
        // pin type, and name specified.
        PinAssembly(std::string name, int size, PinType pinType);

        // Deep copies the contents of the object from another object.
        PinAssembly(const PinAssembly & otherPinAssembly);

        // Sets the pin pitch (shortest distance between a pin center to an
        // adjacent pin center).
        void setPinPitch(double pinPitch);

        // Returns the pin pitch (shortest distance between a pin center to an
        // adjacent pin center) as a double.
        double getPinPitch(); 

        // Sets the inner duct's (outer) flat-to-flat distance.
        void setInnerDuctFlatToFlat(double flatToFlat);

        // Returns the inner duct's (outer) flat-to-flat distance.
        double getInnerDuctFlatToFlat();

        // Sets the inner duct's thickness.
        void setInnerDuctThickness(double innerDuctThickness);

        // Returns the inner duct's thickness.
        double getInnerDuctThickness();

        // Returns the pin type (primary control, secondary control, core fuel
        // or blanket fuel).
        PinType getPinType(); 

        // Adds the specified SFRPin to the assembly; returns true if the
        // operation was successful.
        bool addPin(std::shared_ptr<SFRPin> pin);

        // Removes the specified SFRPin from the assembly; returns true if the
        // operation was successful.
        bool removePin(std::string name);

        // Removes the SFRPin from the specified location (x, y coordinates);
        // returns true if operation was successful.
        bool removePinFromLocation(int row, int column);

        // Returns an std::vector of SFRPin names in the assembly. 
        std::vector<std::string> getPinNames();

        // Returns the SFRPin by the specified name. 
        std::shared_ptr<SFRPin> getPinByName(std::string name);

        // Returns the SFRPin at the specified location (x, y coordinates) in the assembly. 
        std::shared_ptr<SFRPin> getPinByLocation(int row, int column);

        // Returns an std::vector of locations within the assembly that are
        // occupied by the pin matching the specified name.
        std::vector<int> getPinLocations(std::string name);

        // Deep copies and returns a newly instantiated object. 
        std::shared_ptr<Identifiable> clone();

        // Overrides the equals operation to check the attributes on this object
        // with another object of the same type. Returns true if the objects are
        // equals. False otherwise.
        bool operator==(const PinAssembly & otherPinAssembly);

        // Overrides the equality operation for SFRComponents. This is necessary
        // when the code calling this operation does not know the assembly's
        // type and cannot call the more specific equality operation.
        virtual bool operator==(const SFRComponent & component);

        // Adds the pin with the specified name to the assembly in the specified
        // location. If the pin exists and the location is valid and is not
        // occupied by the same pin, this returns true.
        bool setPinLocation(std::string name, int row, int column);

        // Returns the number of pin in the assembly. 
        int getNumberOfPins(); 

        // Gets the IDataProvider (in this case, an SFRComponent) for a location
        // in the assembly. Data can be added to this provider. If the location
        // does not have an associated component, the return value will be null.
        std::shared_ptr<SFRComponent> getDataProviderByLocation(int row, int column);

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

};  //end class PinAssembly

} // end namespace

#endif
