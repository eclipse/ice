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

#ifndef ICOMPONENTVISITOR_H
#define ICOMPONENTVISITOR_H
#include"../updateableComposite/Component.h"
#include <memory>

namespace ICE_DS {

/**
 * This interface defines the "visitation" routines that implementations of Component may use to reveal their types to visitors. It is one part of the Visitor pattern.
 */
class IComponentVisitor {

private:

    //std::vector< std::shared_ptr<Component> > components;

public:

    /*

        // This operation directs a visitor to perform its actions on the Component as a DataComponent.
        virtual void visit(DataComponent component) = 0;



        // This operation directs a visitor to perform its actions on the Component as an OutputComponent.
        virtual void visit(ResourceComponent component) = 0;



        // This operation directs a visitor to perform its actions on the Component as a VisitorComponent.
        virtual void visit(TableComponent component) = 0;



        // This operation directs a visitor to perform its actions on the Component as a MatrixComponent.
        virtual void visit(MatrixComponent component) = 0;



        // This operation directs a visitor to perform its actions on the Component as an IShape.
        virtual void visit(IShape component) = 0;



        // This operation directs a visitor to perform its actions on the Component as a GeometryComponent.
        virtual void visit(GeometryComponent component) = 0;



        // This operation directs a visitor to perform its actions on the Component as a MasterDetailsComponent.
        virtual void visit(MasterDetailsComponent component) = 0;



        // This operation directs informs the visitor that it is actually working with a TreeComposite and should operate accordingly.
        virtual void visit(TreeComposite component) = 0;*/


    /**
     * Visits a component
     *
     * @param the component to visit.
     */
    virtual void visit(std::shared_ptr<Component> component) = 0;

};
}

#endif
