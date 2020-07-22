# The Eclipse Integrated Computational Environment (ICE) Main Repository

Please see [our wiki](https://wiki.eclipse.org/ICE) for detailed information about Eclipse ICE, including where to download binaries.

Build instructions are available [on our wiki too](http://wiki.eclipse.org/ICE_Build_Instructions).

Contributions follow the standard Eclipse mechanisms and you should check out our [CONTRIBUTING.md](https://github.com/eclipse/ice/blob/master/CONTRIBUTING.md) file for more information. If you are looking at this, then you are most likely using Git and can start the process by issuing 
a pull request on our GitHub repo.


# Getting started on development

If you're using Eclipse for development, you need to install Lombok into the Eclipse environment so that Eclipse knows about the bytecode changes that Lombok makes during the build process. Instructions are available here: https://projectlombok.org/setup/eclipse

Using the project generators
-

Instructions for generating a new project with the maven archeytpes.
pull the org.eclipse.ice.archetypes project
make sure that the data element renderer and annotations projects are built as well
build the package and it should build the archetypes as well.
then you can use mvn archetype:generate -DarchetypeGroupId=org.eclipse.ice -DarchetypeArtifactId=data-element-archetype -DarchetypeVersion=1.0-SNAPSHOT -DgroupId=com.test -DartifactId=testApp
Note* use -DarchetypeArtifactId=data-element-webform-archetype for the webform archetype
replace the group and artifact id's with whatever.