Eclipse ICE Testing utilities - Data and Config Retrieval
=========================================================

This test utility module contains the following utilities for data and
configuration retrieval:

- `TestDataPath` - Construct paths to the test data directory and other helpful
  methods.
- `TestConfig` - Load a property file from the test data directory.

## `TestDataPath` Usage Example

For the full API, see the JavaDocs for `TestDataPath`. Below are a few general
examples.

### Constructing paths to test data

Consider the following example of manually loading data from ICE 2:

```java
// Local Declarations
String separator = System.getProperty("file.separator");
String userDir = System.getProperty("user.home") + separator
    + "ICETests" + separator + "datastructuresData";
String txtFilePath = userDir + separator + "txtResource.txt";   
String csvFilePath = userDir + separator + "csvResource.csv";
```

Using `TestDataPath` the above becomes:

```java
TestDataPath dataPath = new TestDataPath();
Path txtFilePath = dataPath.resolve("datastructuresData/txtResource.txt");
Path csvFilePath = dataPath.resolve("datastructuresData/csvResource.csv");
```

Note the usage of `/` as the name separator; this will typically be correctly
converted into the separator appropriate for your platform but it may be safer
to perform a double resolve
(`dataPath.resolve("datastructuresData").resolve("csvResource.csv")`) or
handle separators yourself directly.

### Using a different test data directory

By default, the test data directory is set to `$HOME/ICETests` (`$HOME` as
returned by `System.getProperty("user.home")`. To use a different directory,
use the `TEST_DATA_PATH` environment variable. For example, if running
integration tests from the `org.eclipse.ice.tests.integration` package:

```sh
$ TEST_DATA_PATH=/tmp/ICETestData/ mvn clean test
```

This will search for test data and configuration files within the
`/tmp/ICETestData` directory.

## `TestConfig` Usage Example

For the full API, see the JavaDocs for `TestConfig`. Below are a few general
examples.

### Using static method `from`

For convenience, a static method is provided for loading a `TestConfig` directly
from a file:

```java
TestConfig config = TestConfig.from("mongo.properties");
String host = config.getProperty("host", "localhost");
String port = config.getProperty("port", "27017");
String database = config.getProperty("database", "test");
```

It is important to note that `TestConfig.load` and `TestConfig.from` will
quietly fail (a warning is logged) when the file does not exist or is unreadable
as a property file. This is to support the pattern shown above of allowing for
defaults to be loaded statically from code when no configuration file is
present.

If configuration file non-existence should be a hard fail condition, use
`TestDataPath.exists` to check for the file prior to loading.

### Using `load`

The same example used above without the use of `from`:

```java
TestDataPath dataPath = new TestDataPath();
TestConfig config = new TestConfig(dataPath);
config.load("mongo.properties");
String host = config.getProperty("host", "localhost");
String port = config.getProperty("port", "27017");
String database = config.getProperty("database", "test");
```

This usage may be helpful for the scenario described above of file non-existence
being an error condition:

```java
TestDataPath dataPath = new TestDataPath();
TestConfig config = new TestConfig(dataPath);
if (!dataPath.exists("mongo.properties") {
    fail("mongo.properties missing!");
}
config.load("mongo.properties");
String host = config.getProperty("host");
String port = config.getProperty("port");
String database = config.getProperty("database");
```