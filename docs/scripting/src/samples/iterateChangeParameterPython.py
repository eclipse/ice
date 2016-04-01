 # **********************************************************************
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
 # creates several new Reflectivity Models and changes the thickness parameter
 # to show the effect that creates. 
 # **********************************************************************
 
# Load the Platform module for accessing OSGi services
loadModule('/System/Platform')

# Get the core service from ICE for creating and accessing objects. 
coreService = getService(org.eclipse.ice.core.iCore.ICore);

# Set a initial value for the thickness of the nickel layer. This will be doubled
# for each iteration to show how this parameter effects the model
nickelThickness = 250;

for i in xrange(1, 5):
    # Create the reflectivity model to be used and get its reference. The create item 
    # method will return a string representing the number of that item, so use int() to 
    # convert it to an integer. 
    reflectModel = coreService.getItem(int(coreService.createItem("Reflectivity Model")))

    # Get the nickel layer from the model. It should be in the list, which is component 2,
    # and it is the third layer in that list (which is item 2 as the list is zero based). 
    listComp = reflectModel.getComponent(2);
    nickel = listComp.get(2);
    
    nickel.setProperty("Thickness (A)", nickelThickness);
    
    nickelThickness += 250;

    # Finally process the model to get the results. 
    coreService.processItem(reflectModel.getId(), "Calculate Reflectivity", 1);
    

    
    
    
    
    
    
