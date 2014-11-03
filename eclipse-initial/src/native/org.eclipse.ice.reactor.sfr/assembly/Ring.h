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
#ifndef RING_H
#define RING_H

#include <SFRComponent.h>
#include <Material.h>
#include <string>
#include <memory>

namespace ICE_SFReactor {

// The ring class represents a single instance of a material at a particular
// radial coordinate within a cylindrical location of a SFRPin or SFRod. The
// height variable on this class should uniformly represent the height from the
// bottom of the MaterialBlock (or z-displacement) to help compensate for
// varying types of materials throughout a vertical segment of a pin or rod.
class Ring : public SFRComponent
{

    private:

        // Material the ring is composed of. 
        std::shared_ptr<Material> material;

        // Height of the ring within the material block (z=0 at the bottom of
        // the material block); must be equal to or greater than 0.
        double height;

        // Inner radius of the ring, must equal to or greater than zero. 
        double innerRadius;

        // Outer radius of the ring; must be greater than zero. 
        double outerRadius;

    public:

        // Nullary constructor. 
        Ring(); 

        // Parameterized constructor with name specified. 
        Ring(std::string name); 

        // Parameterized constructor with name, material, ring height, inner
        // radius and outer radius specified.
        Ring(std::string name, std::shared_ptr<Material> material, double height, double innerRadius, double outerRadius);

        // Copy constructor. Deep copies the contents of the object.
        Ring(const Ring & otherRing);

        // Sets the ring height (z-displacement within the material block). 
        void setHeight(double height);

        // Returns the ring height (z-displacement within the material block) as
        // a double.
        double getHeight(); 

        // Sets the ring inner radius. 
        void setInnerRadius(double innerRadius);

        // Returns the ring inner radius as a double. 
        double getInnerRadius(); 

        // Sets the ring's outer radius. 
        void setOuterRadius(double outerRadius);

        // Returns the ring's outer radius as a double. 
        double getOuterRadius();

        // Sets the ring material. 
        void setMaterial(std::shared_ptr<Material> material);

        // Returns the material of the ring. 
        std::shared_ptr<Material> getMaterial();

        // Deep copies and returns a newly instantiated object. 
        std::shared_ptr<ICE_DS::Identifiable> clone();

        // Compares the contents of objects and returns true if they are
        // identical, otherwise returns false.
        bool operator==(const Ring & otherRing);

};  //end class Ring

} // end namespace
#endif
