# Commands Integration Tests

This README serves as additional information for setting up the more complicated tests for the Commands package. Additional information about Commands can be found in the ICE package `org.eclipse.ice.commands`. These tests can also serve as examples for how to use the Commands API.


### Dependencies
All dependencies are noted in the `pom` file, and all but one are within maven central. The only non-centralized dependency is the ICE package `org.eclipse.ice.tests.util.data`. To install it, perform the following commands (after cloning the ICE repositiory) so that the Commands package can build successfully:

```shell
$ cd org.eclipse.ice.tests
$ mvn clean install
```

### Notes about tests
The automated testing is performed with a dummy remote host, which has private credentials. Thus, if the tests are built with the package, a significant portion of the tests will fail due to the fact that the dummy remote host credentials are not distributed publicly. To solve this, one may enter any generic remote host credentials into the file `$TEST_DATA_PATH/commands/ice-remote-creds.txt` in the following order

```
username 
password
hostname
```

See the README in `org.eclipse.ice.tests/org.eclipse.ice.tests.util.data` for information regarding the `$TEST_DATA_PATH` environment variable; [this link](https://github.com/dbluhm/ice/blob/next/org.eclipse.ice.tests/org.eclipse.ice.tests.util.data/README.md) takes you to the README on the `next` branch.

The automated tests will then grab the necessary credentials from this file to run. Any valid ssh connection will work. If you still find that the tests fail, ensure that the ssh connection you are using has been logged into before from your host computer such that there is a key fingerprint associated to that host in your `~/.ssh/known_hosts` file. The Commands package requires that this key exists in order for authentication to proceed, no matter what form of authentication you use. In the event that tests fail on a host that already exists in `known_hosts` (e.g. with the error message `server key did not validate`, try deleting your `known_hosts` file (or the entries in your `known_hosts` that correspond to the host you are trying to run the tests on), logging in again to re-establish a fingerprint, and running the tests again. 

Alternatively, you can set `StrictHostKeyChecking` to false in the `ConnectionManager`, which is in general not advised as it is inherently unsecure. To do this for the static `ConnectionManager`, just write:

```java
ConnectionManagerFactory.getConnectionManager().setRequireStrictHostKeyChecking(false);

```

Note that this is also a way through which ssh validation can be performed in the package for running actual remote commands/file transfers.

#### EmailHandler test
To test the `EmailUpdateHandler` class, a similar file to the ssh credential file must be created. Instead, a file in the location `$TEST_DATA_PATH/commands/ice-email-creds.txt` must exist which contains the following information:

```
email@address
password
SmtpHost
```

The EmailHandler will send an email from your own address to the same address with updates on when the job finishes. In order for this to happen, the email address must be authenticated. In the case of the tests, and for CI purposes, these authentications are placed in the above text file. For developer use, one could simply enter this information as it is entered in EmailHandlerTest, or you could implement another method (e.g. through use of the text file).

#### KeyGen Tests and Connections

Connections may be established via a public/private key pair that is generated between the local and remote host. Commands can function with ECDSA or RSA type keys. To generate an RSA key, for example, use:

```bash
$ ssh-keygen -t rsa -m PEM
$ ssh-copy-id -i ~/.ssh/keyname.pub username@hostname
```

Then you should be able to remotely login via `ssh -i /path/to/key username@hostname` without a password requirement.

For the keygen connection tests to pass, you should also create a key to a remote host that the tests expect to find. This can be done with any arbitrary remote server that you have credential access to; however, the key must be named dummyhostkey and must exist in your home `.ssh` directory. In other words, the key must be here:

```
$HOME/.ssh/dummyhostkey
```

where `$HOME` is the result returned from `System.getProperty("user.home")`.

