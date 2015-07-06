
 # ****************************************************************************
 # Copyright (c) 2015 UT-Battelle, LLC.
 # All rights reserved. This program and the accompanying materials
 # are made available under the terms of the Eclipse Public License v1.0
 # which accompanies this distribution, and is available at
 # http://www.eclipse.org/legal/epl-v10.html
 #
 # Contributors:
 #   Initial API and implementation and/or initial documentation - Kasper
 # Gammeltoft.
 #
 # This is an example script designed to show how to use ease with ICE. It 
 # creates a new Reflectivity Model and processes it, but also edits the input
 # to the table beforehand. 
 # ****************************************************************************

# Usual imports
from org.eclipse.ease.modules import EnvironmentModule
EnvironmentModule().loadModule("/System/Environment")
import java
import org
import com

# Load the Platform module for accessing OSGi services
loadModule('/System/Platform')

# Get the core service from ICE for creating and accessing objects. 
coreService = getService(org.eclipse.ice.core.iCore.ICore);

# Create the reflectivity model to be used and get its reference. The create item 
# method will return a string representing the number of that item, so use int() to 
# convert it to an integer. 
reflectModel = coreService.getItem(int(coreService.createItem("Reflectivity Model")))

# Gets the list component used as the data for the table (is on tab 2)
listComp = reflectModel.getComponent(2)

# Gets the third material and sets its thickness to 400
mat1 = listComp.get(2)
mat1.setProperty("Thickness (A)", 400)

# Get the total thickness and set the second material's thickness to depend
# on the thicknesses of the other materials
totThickness = 0
for i in xrange(0, listComp.size() - 1):
    if(i != 1):
        totThickness += listComp.get(i).getProperty("Thickness (A)")
    
# Set the thickness of the second material so that the total sums to 1000 (A)
listComp.get(1).setProperty("Thickness (A)", 1000-totThickness);
        

# Finally process the model to get the results. 
coreService.processItem(reflectModel.getId(), "Calculate Reflectivity", 1);
