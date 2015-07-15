
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
 # creates a new Reflectivity Model and processes it, using the default mock
 # data and inputs. 
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

# This is usually where you would do your own customization and automation regarding
# the reflectivity model you just created. Maybe change the layers, or do some custom
# calculations. 


# Finally process the model to get the results. 
coreService.processItem(reflectModel.getId(), "Calculate Reflectivity", 1);
