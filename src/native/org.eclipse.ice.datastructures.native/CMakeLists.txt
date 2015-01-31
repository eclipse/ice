 
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

#Set the lib name
SET(LIBNAME ICEDS)

#Set the package name
SET(PACKAGE_NAME "org.eclipse.ice.datastructures.native")

#Set the description
SET(PACKAGE_DESCRIPTION "Native implementations of the ICE Data Structures")

#Collect all header filenames in this project 
#and set it to HEADERS
file(GLOB HEADERS1 *.h)
file(GLOB HEADERS2 componentVisitor/*.h )
file(GLOB HEADERS3 ICEObject/*.h)
file(GLOB HEADERS4 updateableComposite/*.h)

#Install the ICEReactor header files
install(FILES ${HEADERS1} DESTINATION include)
install(FILES ${HEADERS2} DESTINATION include/componentVisitor)
install(FILES ${HEADERS3} DESTINATION include/ICEObject)
install(FILES ${HEADERS4} DESTINATION include/updateableComposite)

#Set cpack variables
set(CPACK_PACKAGE_DESCRIPTION_SUMMARY ${PACKAGE_NAME})
set(CPACK_PACKAGE_NAME ${PACKAGE_DESCRIPTION})
set(CPACK_PACKAGE_VERSION "2.0.0")
SET(CPACK_PACKAGE_VERSION_MAJOR "2")
SET(CPACK_PACKAGE_VERSION_MINOR "0")
SET(CPACK_PACKAGE_VERSION_PATCH "0")
set(CPACK_PACKAGE_VENDOR "Oak Ridge National Laboratory")
set(CPACK_PACKAGE_CONTACT "billingsjj@ornl.gov")
set(CPACK_RESOURCE_FILE_LICENSE "${CMAKE_SOURCE_DIR}/LICENSE")

#Set package generators
set(CPACK_GENERATOR "DEB;RPM;TGZ;ZIP")

#These include statements are required for CPack
include(InstallRequiredSystemLibraries)
include(CPack)
