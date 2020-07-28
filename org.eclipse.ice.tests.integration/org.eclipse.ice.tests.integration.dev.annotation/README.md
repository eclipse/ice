Persistence Handler Integration Tests
=====================================

This module implements integration tests for Persistence Handlers generated
using the `@Persisted` annotation.

**Test Prerequisites:**

- MongoDB instance running locally or accessible by host name and port to the
  test runner.

**Environment Variables:**

- `MONGO_HOST` - Host name of MongoDB instance. Defaults to `localhost`.
- `MONGO_PORT` - Port MongoDB is running on on host. Defaults to `27017`.
- `MONGO_DB` - The database to use for testing. Defaults to `test`.

### Running tests

#### Running with local MongoDB

To run these tests, you will need a running instance of MongoDB. A quick method
to get a running instance is to launch a container using docker or podman (if
using a RHEL based Linux distribution):

```sh
$ podman run -d -p 27017:27017 mongo
```

OR

```sh
$ sudo docker run -d -p 27017:27017 mongo
```

Then you can execute tests from this directory with the standard:

```sh
$ mvn clean test
```

#### Running with remote MongoDB

If there is an already running instance of MongoDB available, use environment
variables to configure the tests to use that instance:

```sh
$ MONGO_HOST=mymongoserver \
  MONGO_PORT=27017 \
  MONGO_DB=testing_db \
  mvn clean test
```