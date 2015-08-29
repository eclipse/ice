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

#ifndef PERSISTABLE_H
#define PERSISTABLE_H

namespace ICE_DS {

/**
 * This class defines the operations that ICE expects for units that are persisted to disc outside of a database.
 */
class Persistable {

public:



    /**
     * This operation loads the ICEObject from persistent storage as XML. This operation will throw an IOException if it fails.
     */
    //virtual void loadFromXML(InputStream inputStream) = 0;



    /**
     * This operation saves the ICEObject in persistent storage as XML. This operation will throw an IOException if it fails.
     */
    //virtual void persistToXML(OutputStream outputStream) = 0;



};
}
#endif
