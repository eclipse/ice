# BATS

BATS, the Basic Artifact Tracking System (BATS), is a simple data management service for managing scientific data.

BATS is a standalone maven package that can be used within or outside of ICE.

BATS leverages [Apache Jena](https://jena.apache.org/index.html) to create [RDF Models](https://jena.apache.org/tutorials/rdf_api.html) and connects to [Apache Jena Fuseki](https://jena.apache.org/documentation/fuseki2/index.html) to publish these models.



## Build Instructions

### Prerequisites

BATS requires a full installation of Docker for building, executing, and storing images.


### Using Maven

Building the package can be performed like most other maven packages, where the end result is a jar file that can be included as a dependency
Due to the integration tests requiring a [Apache Jene Fuseki](https://jena.apache.org/documentation/fuseki2/index.html) server,
we need to first build the image using the Dockerfile located in `src/main/docker/Dockerfile`.
A test Fuseki container will be started and stopped during the tests using the [fabric8io docker-maven-plugin](https://dmp.fabric8.io/)
(GitHub repo link [here](https://github.com/fabric8io/docker-maven-plugin)

```
$ mvn clean install
```

This installs the jar file to the local repository in `~/.m2`. It is also possible to build the package without installing by running

```
$ mvn clean verify
```

In both cases one can skip the tests by including `-DskipTests` in your build. 

## BATS API

Below are examples of a few general use cases for the BATS API

### Upload a new DataSet

One can create a connection to a Fuseki server to upload a [Dataset](https://jena.apache.org/documentation/javadoc/arq/org/apache/jena/query/Dataset.html), or a collection of named graphs (called [Models](https://jena.apache.org/documentation/javadoc/jena/org/apache/jena/rdf/model/Model.html) in Apache Jena).

Here we upload a new, empty Dataset called `my-new-dataset`
to our Fuseki server `http://my-fuseki-server.org:3030`.

The upload takes place when we issue the `.create()` method
for "creating" the data on the server.

```
import org.eclipse.ice.bats.DataSet;

DataSet dataset = new DataSet();
dataset.setName("my-new-dataset");
dataset.setHost("http://my-fuseki-server.org");
dataset.setPort(3030);
dataset.create();
```

### Upload a new RDF Model to a DataSet

We can also add a RDF [Model](https://jena.apache.org/documentation/javadoc/jena/org/apache/jena/rdf/model/Model.html) in Apache Jena,
to the Dataset on the Fuseki server as follows:

```
import org.eclipse.ice.bats.DataSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

DataSet dataset = new DataSet();
dataset.setName("my-new-dataset");
dataset.setHost("http://my-fuseki-server.org");
dataset.setPort(3030);

Model model = ModelFactory.createDefaultModel();
dataset.updateModel("my-model", model);
```

### Pull a RDF Model from a DataSet

Following the previous example for uploading a RDF Model,
we can pull that same model using the following:

```
import org.eclipse.ice.bats.DataSet;
import org.apache.jena.rdf.model.Model;

DataSet dataset = new DataSet();
dataset.setName("my-new-dataset");
dataset.setHost("http://my-fuseki-server.org");
dataset.setPort(3030);

Model model = dataset.getModel("my-model");
```

### Get "raw" Jena Dataset

Sometimes we require the "raw" Jena dataset.
We can use the following to pull this dataset and check if it is "null"

```
import org.eclipse.ice.bats.DataSet;
import org.apache.jena.query.Dataset;

DataSet dataset = new DataSet();
dataset.setName("my-new-dataset");
dataset.setHost("http://my-fuseki-server.org");
dataset.setPort(3030);

Dataset rawDataset = dataset.getJenaDataset();
if ( rawDataset == null ) {
    throw new Exception("DataSet Not Found");
}
```

## How BATS got its name

Jay Jay Bilings had a discussion with his daughter, 17 months old at the time, about her favorite animal.
She picked the moose, but since that is already taken by several projects, they settled on her second favorite animal, the bat.
The name was then back-ronymed out of it.
