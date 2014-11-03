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
#define BOOST_TEST_DYN_LINK
#define BOOST_TEST_MODULE LWReactorTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <LWReactor.h>
#include <HDF5LWRTagType.h>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <HdfFileFactory.h>
#include <UtilityOperations.h>
#include <H5Cpp.h>
#include <memory>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

using namespace ICE_Reactor;
using namespace ICE_IO;
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(LWReactorTester_testSuite)

BOOST_AUTO_TEST_CASE(beforeClass) {

    //Create folder testPath in local directory - relative!
    std::string testFolder = "ICEIOTestsDir/";
    mkdir(testFolder.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);

    //Delete file
    std::string testFile = testFolder + "test.h5";
    remove(testFile.c_str());

}

BOOST_AUTO_TEST_CASE(checkConstruction) {

    // begin-user-code

    //Local Declarations
    std::string defaultName = "LWReactor 1";
    std::string defaultDescription = "LWReactor 1's Description";
    int defaultId = 1;
    int defaultSize = 1;
    HDF5LWRTagType type = LWREACTOR;

    // This test is to show the default value for a reactor when it is
    // created with a negative value.
    LWReactor reactor(-1);
    BOOST_REQUIRE_EQUAL(defaultSize, reactor.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, reactor.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, reactor.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, reactor.getId());
    BOOST_REQUIRE_EQUAL(type, reactor.getHDF5LWRTag());

    // This test is to show the default value for a reactor when its created
    // with a zero value
    LWReactor reactor2(0);
    BOOST_REQUIRE_EQUAL(defaultSize, reactor2.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, reactor2.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, reactor2.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, reactor2.getId());
    BOOST_REQUIRE_EQUAL(type, reactor2.getHDF5LWRTag());

    // This is a test to show a valid creation of a reactor
    LWReactor reactor3(17);
    BOOST_REQUIRE_EQUAL(17, reactor3.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, reactor3.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, reactor3.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, reactor3.getId());
    BOOST_REQUIRE_EQUAL(type, reactor3.getHDF5LWRTag());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkCompositeImplementations) {

    // begin-user-code

    //Local Declarations
    int reactorSize = 17;
    LWReactor reactor(reactorSize);
    std::vector<std::string> compNames;
    std::vector< std::shared_ptr<Component> >components;
    int numberOfDefaultComponents = 0;
    std::shared_ptr<Component> newComponent1 (new LWRComposite());

    //Setup the default number of components
    numberOfDefaultComponents = components.size();

    //Check the default Composite size and attributes on PWRAssembly

    //Has a size of numberOfDefaultComponents
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());
    //Check general getters for the other pieces
    BOOST_REQUIRE_EQUAL(compNames==(reactor.getComponentNames()), true);
    BOOST_REQUIRE_EQUAL(components.size() == (reactor.getComponents()).size(), true);

    //These operations will show that these will not work for this class

    //Check addComponent
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());
    reactor.addComponent(newComponent1);
    //No size change!
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());

    //Check removeComponent - id
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());
    reactor.removeComponent(1);
    //No size change!
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());

    //Check remove component - name
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());

    //No size change!
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations

    int size = 5;
    int unEqualSize = 7;

    //Setup root object
    LWReactor object(size);

    //Setup equalObject equal to object
    LWReactor equalObject(size);

    //Setup transitiveObject equal to object
    LWReactor transitiveObject(size);

    // Set its data, not equal to object
    LWReactor unEqualObject(unEqualSize);

    // Assert that these two objects are equal
    BOOST_REQUIRE_EQUAL(object==(equalObject), true);

    // Assert that two unequal objects returns false
    BOOST_REQUIRE_EQUAL(object==(unEqualObject), false);

    // Check that equals() is Reflexive
    // x.equals(x) = true
    BOOST_REQUIRE_EQUAL(object==(object), true);

    // Check that equals() is Symmetric
    // x.equals(y) = true iff y.equals(x) = true
    BOOST_REQUIRE_EQUAL(object==(equalObject) && equalObject==(object), true);

    // Check that equals() is Transitive
    // x.equals(y) = true, y.equals(z) = true => x.equals(z) = true
    if (object==(equalObject) && equalObject==(transitiveObject)) {
        BOOST_REQUIRE_EQUAL(object==(transitiveObject), true);
    } else {
        BOOST_FAIL("FAILURE IN EQUALITY CHECKING!  EXITING!");
    }

    // Check the Consistent nature of equals()
    BOOST_REQUIRE_EQUAL(object==(equalObject) && object==(equalObject)
                        && object==(equalObject), true);
    BOOST_REQUIRE_EQUAL(!(object==(unEqualObject))
                        && !(object==(unEqualObject))
                        && !(object==(unEqualObject)), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkCopying) {

    // begin-user-code

    //Local declarations
    int size = 5;

    //Setup root object
    LWReactor object(size);


    //Run the copy routine
    LWReactor copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<LWReactor> castedObject (new LWReactor(*(dynamic_cast<LWReactor *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    // begin-user-code

    //Local Declarations
    int size = 5;
    LWReactor component(size);
    std::string name = "LIGHT REACTOR";
    std::string description = "DESCRIPTION!";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Setup Component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check to see if it has any children
    BOOST_REQUIRE_EQUAL((component.getWriteableChildren().size() == 0), true);

    //Check writing attributes
    std::shared_ptr<H5::Group> h5Group1 (new H5::Group(h5File.get()->createGroup("/Group1")));

    //Pass the group and file to the writer for attributes
    //See that it passes
    BOOST_REQUIRE_EQUAL(component.writeAttributes(h5File, h5Group1), true);

    //Bad pointer checks
    std::shared_ptr<H5::Group> badGroup;
    std::shared_ptr<H5::H5File> badFile;

    //Check dataSet.  Pass null to show it return false
    BOOST_REQUIRE_EQUAL(component.writeDatasets(badFile, badGroup), false);
    BOOST_REQUIRE_EQUAL(component.writeDatasets(badFile, h5Group1), false);
    BOOST_REQUIRE_EQUAL(component.writeDatasets(h5File, badGroup), false);

    //Check dataset
    BOOST_REQUIRE_EQUAL(component.writeDatasets(h5File, h5Group1), true);

    //Close group and then reopen
    try {
        h5File.get()->close();
        //free resources
        h5Group1.get()->close();

        //Open file
        h5File =  HdfFileFactory::openH5File(dataFile);

    } catch (...) {
        BOOST_FAIL("FAILED IN checkHdfWriteables!  Exiting");
    }

    //Get the group again
    std::shared_ptr<H5::Group> h5Group (new H5::Group(h5File.get()->openGroup("/Group1")));

    //Check attributes

    try {
        //Show that there is one group made at this time
        BOOST_REQUIRE_EQUAL(1, h5Group.get()->getNumObjs());


        //Check the meta data
        BOOST_REQUIRE_EQUAL(5, h5Group.get()->getNumAttrs());

        //Values to check

        //Get attributes by reader factory and check values
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "id"), id);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "name"), name);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "description"), description);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "HDF5LWRTag"), UtilityOperations::toStringHDF5Tag(tag));
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "size"), size);

        //Check the Group names
        BOOST_REQUIRE_EQUAL("State Point Data", h5Group.get()->getObjnameByIdx(0));

    } catch (...) {
        BOOST_FAIL("FAILURE IN CHECKHDF5WRITEABLES");
    }


    //Check Group Creation
    std::shared_ptr<H5::Group> h5Group3 = component.createGroup(h5File, h5Group);
    //See that the previous group has a group
    BOOST_REQUIRE_EQUAL(2, h5Group.get()->getNumObjs());
    //Check that it has the same name as the root component
    BOOST_REQUIRE_EQUAL(component.getName(), h5Group.get()->getObjnameByIdx(0));
    //Check that the returned group is a Group but no members
    BOOST_REQUIRE_EQUAL(0, h5Group3.get()->getNumObjs());

    //Delete file
    remove(dataFile.c_str());

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Readables) {


    // begin-user-code

    //Local Declarations
    int size = 5;
    LWReactor component(size);
    LWReactor newComponent(-1);
    std::shared_ptr<IHdfReadable> nullComponent;
    std::string name = "LIGHT REACTOR";
    std::string description = "DESCRIPTION!";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Setup Component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check reading attributes

    //Create a group
    std::shared_ptr<H5::Group> h5Group1 (new H5::Group (h5File.get()->createGroup("/Group1")));

    //Use the writers.
    //Yes, this creates a dependency on the writers to work before the readers, but for testing scalability concerns, this class needs to be edited as little as possible.
    //Since the java code will change drastically, this is the way I have chosen to do it.  "Don't re-invent the wheel" - SFH @ 02122012@1223 hrs

    try {
        //Write everything from component
        BOOST_REQUIRE_EQUAL(component.writeAttributes(h5File, h5Group1), true);
        BOOST_REQUIRE_EQUAL(component.writeDatasets(h5File, h5Group1), true);

        //Persist data
        h5File.get()->close();
        h5Group1.get()->close();

        //Reopen file
        h5File =  HdfFileFactory::openH5File(dataFile);

        //Call creator
        std::shared_ptr<H5::Group> h5Group2 (new H5::Group (h5File.get()->openGroup("/Group1")));

        //Read information
        BOOST_REQUIRE_EQUAL(newComponent.readAttributes(h5Group2), true);
        BOOST_REQUIRE_EQUAL(newComponent.readDatasets(h5Group2), true);

        //Check with setup component
        BOOST_REQUIRE_EQUAL(component==newComponent, true);

        //Now, lets try to set an erroneous H5Group with missing data
        h5Group2.get()->removeAttr("name");

        //Save, close, reopen
        //Persist data
        h5File.get()->close();
        h5Group1.get()->close();

        //Reopen file
        h5File =  HdfFileFactory::openH5File(dataFile);

        //Call creator
        std::shared_ptr<H5::Group> h5Group3 (new H5::Group (h5File.get()->openGroup("/Group1")));

        //Run it through
        BOOST_REQUIRE_EQUAL(newComponent.readAttributes(h5Group3), false);
        //Check it does not change
        BOOST_REQUIRE_EQUAL(component==newComponent, true);

        //Check for nullaries
        std::shared_ptr<H5::Group> badGroup;
        BOOST_REQUIRE_EQUAL(newComponent.readAttributes(badGroup), false);

        //Doesn't change anything
        BOOST_REQUIRE_EQUAL(component==newComponent, true);

        h5File.get()->close();
        h5Group3.get()->close();


    } catch (...) {
        BOOST_FAIL("FAILURE IN LWRCOMPONENTTESTER::checkReadables.  Exiting");
    }

    //Delete the file

    //Delete file
    remove(dataFile.c_str());

    return;

    //end-user-code

    /*    //TODO Auto-generated method stub
    		// begin-user-code

    		//Local Declarations
    		int size = 5;
    		LWReactor component = new LWReactor(size);
    		LWReactor newComponent = new LWReactor(-1);
    		String name = "Bob the Builder";
    		String description = "Can he fix it?";
    		int id = 4;
    		HDF5LWRTagType tag = component.getHDF5LWRTag();
    		H5Group subGroup = null;
    		double rodPitch = 4;
    		String sourceInfo = "ASDASDASD";
    		String timeUnits = "UNITS OF AWESOME";
    		double time = 1.0;


    		//Setup Component
    		component.setName(name);
    		component.setId(id);
    		component.setDescription(description);
    		component.setSourceInfo(sourceInfo);
    		component.setTime(time);
    		component.setTimeUnits(timeUnits);

    		//Setup the HDF5 File
    		String separator = System.getProperty("file.separator");
    		File dataFile = new File(System.getProperty("user.dir") + separator
    				+ "test.h5");
    		URI uri = dataFile.toURI();
    		H5File h5File = HdfFileFactory.createH5File(uri);
    		try {
    			h5File.open();
    		} catch (Exception e1) {
    			e1.printStackTrace();
    			fail();
    		}

    		//Check Readable Children
    		//assertTrue(component.readChild(null));

    		//Setup PWRAssembly with Data in the Group

    		H5Group parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
    				.getRootNode()).getUserObject();
    		try {
    			//Setup the subGroup
    			subGroup = (H5Group) h5File.createGroup(name, parentH5Group);

    			//Setup the subGroup's attributes

    			//Setup Tag Attribute
    			HdfWriterFactory.writeStringAttribute(h5File, subGroup, "HDF5LWRTag", tag.toString());

    			//Setup name attribute
    			HdfWriterFactory.writeStringAttribute(h5File, subGroup, "name", name);

    			//Setup id attribute
    			HdfWriterFactory.writeIntegerAttribute(h5File, subGroup, "id", id);

    			//Setup description attribute
    			HdfWriterFactory.writeStringAttribute(h5File, subGroup, "description", description);

    			//Setup size attribute
    			HdfWriterFactory.writeIntegerAttribute(h5File, subGroup, "size", size);

    			//Setup sourceInfo attribute
    			HdfWriterFactory.writeStringAttribute(h5File, subGroup, "sourceInfo", sourceInfo);

    			//Setup timeUnits attribute
    			HdfWriterFactory.writeStringAttribute(h5File, subGroup, "timeUnit", timeUnits);

    			//Setup time attribute
    			HdfWriterFactory.writeDoubleAttribute(h5File, subGroup, "time", time);

    			//Close group and then reopen
    			h5File.close();
    			h5File.open();
    			parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
    					.getRootNode()).getUserObject();

    			//Get the subGroup
    			subGroup = (H5Group) parentH5Group.getMemberList().get(0);

    			//Read information
    			assertTrue(newComponent.readAttributes(subGroup));
    			assertFalse(newComponent.readDatasets(null));

    			//Check with setup component
    			assertTrue(component.equals(newComponent));

    			//Now, lets try to set an erroneous H5Group with missing data
    			subGroup.getMetadata().remove(1);

    			//Run it through
    			assertFalse(newComponent.readAttributes(subGroup));
    			//Check it does not change
    			assertTrue(component.equals(newComponent));

    			//Check for nullaries
    			assertFalse(newComponent.readAttributes(null));
    			//Doesn't change anything
    			assertTrue(component.equals(newComponent));


    		} catch (Exception e) {
    			e.printStackTrace();
    			fail();
    		}

    		dataFile.delete();

    		// end-user-code
    		// end-user-code
        return;*/
}

BOOST_AUTO_TEST_CASE(afterClass) {

    //relative!
    std::string testFolder = "ICEIOTestsDir/";
    std::string testFile = testFolder + "test.h5";

    //Delete file
    remove(testFile.c_str());

    //Delete folder
    remove(testFolder.c_str());

}
BOOST_AUTO_TEST_SUITE_END()
