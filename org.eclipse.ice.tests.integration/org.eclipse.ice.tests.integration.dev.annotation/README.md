Persistence Handler Integration Tests
=====================================

This module implements integration tests for Persistence Handlers generated
using the `@Persisted` annotation.

**Test Prerequisites:**

- MongoDB instance running locally or accessible by host name and port to the
  test runner.

### Configuration

**Property file location:** `$TEST_DATA_DIR/mongo.properties`

**Properties:**

- `host` - Host name of MongoDB instance. Defaults to `localhost` if unset.
- `port` - Port MongoDB is running on on host. Defaults to `27017` if unset.
- `database` - The database to use for testing. Defaults to `test` if unset.

## Running tests

### Running with local MongoDB

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

### Running with remote MongoDB

If there is an already running instance of MongoDB available, place a property
file like the following in your test data directory with your values filled in:

```properties
host=mymongoserver
port=27017
database=testing_db
```

If your test data directory is not `$HOME/ICETests`, you can set it using the
`TEST_DATA_PATH` environment variable:

```sh
$ TEST_DATA_PATH=/tmp/ICETests \
  mvn clean test
```