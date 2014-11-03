 
 # Copyright (c) 2012, 2014 UT-Battelle, LLC.
 # All rights reserved. This program and the accompanying materials
 # are made available under the terms of the Eclipse Public License v1.0
 # which accompanies this distribution, and is available at
 # http://www.eclipse.org/legal/epl-v10.html
 #
 # Contributors:
 #   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 #   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 #   Claire Saunders, Matthew Wang, Anna Wojtowicz
 
#Collect all cpp filenames in this project 
#and set it to ICEUPDATER_SRCS
file(GLOB ICEUPDATER_SRCS *.cpp)

#Collect all header filenames in this project 
#and set it to ICEUPDATER_HEADERS
file(GLOB ICEUPDATER_HEADERS *.h)

#Add a static library to this project using the specified source files.
add_library(updater-static STATIC ${ICEUPDATER_SRCS})

#Add a static library to this project using the specified source files.
add_library(updater-shared SHARED ${ICEUPDATER_SRCS})

#Set this property so that we can create shared and static libraries with the same name
set_target_properties(updater-static PROPERTIES CLEAN_DIRECT_OUTPUT 1)

#Set this property so that we can create shared and static libraries with the same name
set_target_properties(updater-shared PROPERTIES CLEAN_DIRECT_OUTPUT 1)

#Set the name of the static library to updater
set_target_properties(updater-static PROPERTIES OUTPUT_NAME updater)

#Set the name of the shared library to updater
set_target_properties(updater-shared PROPERTIES OUTPUT_NAME updater)

#Install the updater static library
install(TARGETS updater-static ARCHIVE DESTINATION lib)

#Install the updater shared library
install(TARGETS updater-shared DESTINATION lib)

#Install the updater header files
install(FILES ${ICEUPDATER_HEADERS} DESTINATION include)

#Set cpack variables
set(CPACK_PACKAGE_DESCRIPTION_SUMMARY "Updater")
set(CPACK_PACKAGE_NAME "org.eclipse.ice.updater")
set(CPACK_PACKAGE_VERSION "2.0.0")
SET(CPACK_PACKAGE_VERSION_MAJOR "2")
SET(CPACK_PACKAGE_VERSION_MINOR "0")
SET(CPACK_PACKAGE_VERSION_PATCH "0")
set(CPACK_PACKAGE_VENDOR "Oak Ridge National Laboratory")
set(CPACK_PACKAGE_CONTACT "billingsjj@ornl.gov")
set(CPACK_RESOURCE_FILE_LICENSE "${CMAKE_CURRENT_SOURCE_DIR}/LICENSE")

#Set package generators
set(CPACK_GENERATOR "DEB;RPM;TGZ;ZIP")

#These include statements are required for CPack
include(InstallRequiredSystemLibraries)
include(CPack)