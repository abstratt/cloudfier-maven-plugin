# cloudfier-maven-plugin

A Maven plug-in that exposes Cloudfier's functionality to Maven-based builds.

The following goals are available:
- publish
- test
- generate

#### cloudfier-maven-plugin:publish

Publishes a local project to a Cloudfier server, validating the application, and making it available for model-based testing and code generation.

Parameters:
- kirra.uri - the URI of the Cloudfier server (defaults to "http://develop.cloudfier.com")
- kirra.project.slug - the unique identifier for a project (defaults to "${project.groupId}-${project.artifactId}-${project.version}")
- kirra.project.sourcedir - the location where to find source files (defaults to "$basedir/src/main/textuml")

Example:

```
mvn com.abstratt:cloudfier-maven-plugin:publish
```

#### cloudfier-maven-plugin:test

Runs the test suite for a project currently deployed on a Cloudfier server. Does not re-deploy the application, but does re-deploy the data set supporting the Cloudfier application. 

Parameters:
- kirra.uri - the URI of the Cloudfier server (defaults to "http://develop.cloudfier.com")
- kirra.project.slug - the unique identifier for a project (defaults to "${project.groupId}-${project.artifactId}-${project.version}")

```
mvn com.abstratt:cloudfier-maven-plugin:test
```

#### cloudfier-maven-plugin:generate

Generates code locally from a project currently deployed at a Cloudfier server.

Parameters:
- kirra.uri - the URI of the Cloudfier server (defaults to "http://develop.cloudfier.com")
- kirra.project.slug - the unique identifier for a project (defaults to "${project.groupId}-${project.artifactId}-${project.version}")
- kirra.generator.override - whether to silently override existing files (defaults to false)
- kirra.target.platform - the target platform (available: "jse" and "jee", defaults to "jse")
- kirra.target.dir - the base directory where to generate the code, defaults to the current dir

The Cloudfier project must have been deployed (using the 'cloudfier full-deploy' command in Cloudfier).

Example:

```
mvn com.abstratt:cloudfier-maven-plugin:generate \
    -Dkirra.target.dir=/tmp/test8 \
    -Dkirra.target.platform=jee \
```

which on my machine generates:
```
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building Maven Stub Project (No POM) 1
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- cloudfier-maven-plugin:0.11.2:generate (default-cli) @ standalone-pom ---
PLATFORM: jee
[INFO] Generating at /tmp/test8/pom.xml
[INFO] Generating at /tmp/test8/src/main/java/invoicer/InvoiceIssuedEvent.java
[INFO] Generating at /tmp/test8/src/main/java/invoicer/Invoicer.java
[INFO] Generating at /tmp/test8/src/main/java/resource/timetracker/ClientElement.java
[INFO] Generating at /tmp/test8/src/main/java/resource/timetracker/ClientResource.java
[INFO] Generating at /tmp/test8/src/main/java/resource/timetracker/InvoiceElement.java
[INFO] Generating at /tmp/test8/src/main/java/resource/timetracker/InvoiceResource.java
[INFO] Generating at /tmp/test8/src/main/java/resource/timetracker/TaskElement.java
[INFO] Generating at /tmp/test8/src/main/java/resource/timetracker/TaskResource.java
[INFO] Generating at /tmp/test8/src/main/java/resource/timetracker/WorkElement.java
[INFO] Generating at /tmp/test8/src/main/java/resource/timetracker/WorkResource.java
[INFO] Generating at /tmp/test8/src/main/java/system/System.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/AlreadyInvoicedException.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/Client.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/ClientService.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/Invoice.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/InvoiceNotOpenException.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/InvoicePaidEvent.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/InvoiceService.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/MustBePositiveException.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/MustHaveWorkException.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/Task.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/TaskService.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/Work.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/WorkService.java
[INFO] Generating at /tmp/test8/src/main/java/timetracker/WrongClientException.java
[INFO] Generating at /tmp/test8/src/main/java/util/PersistenceHelper.java
[INFO] Generating at /tmp/test8/src/main/resources/META-INF/orm.xml
[INFO] Generating at /tmp/test8/src/main/resources/META-INF/persistence.xml
[INFO] Generating at /tmp/test8/src/main/resources/META-INF/sql/create.sql
[INFO] Generating at /tmp/test8/src/main/resources/META-INF/sql/drop.sql
[INFO] Generating at /tmp/test8/src/main/resources/log4j.properties
[INFO] Generating at /tmp/test8/src/main/webapp/WEB-INF/web.xml
[INFO] Generating at /tmp/test8/src/test/java/tests/test/ClientScenarios.java
[INFO] Generating at /tmp/test8/src/test/java/tests/test/Examples.java
[INFO] Generating at /tmp/test8/src/test/java/tests/test/InvoiceScenarios.java
[INFO] Generating at /tmp/test8/src/test/java/tests/test/TaskScenarios.java
[INFO] Generating at /tmp/test8/src/test/java/tests/test/WorkScenarios.java
[INFO] Generating at /tmp/test8/src/test/java/timetracker/ClientCRUDTest.java
[INFO] Generating at /tmp/test8/src/test/java/timetracker/InvoiceCRUDTest.java
[INFO] Generating at /tmp/test8/src/test/java/timetracker/TaskCRUDTest.java
[INFO] Generating at /tmp/test8/src/test/java/timetracker/WorkCRUDTest.java
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 0.656s
[INFO] Finished at: Thu Apr 23 18:16:26 BRT 2015
[INFO] Final Memory: 11M/490M
[INFO] ------------------------------------------------------------------------
```
#### Building the generated code

You need to have Maven 3, Java 8 and Postgres 9 installed. In order to run the tests, you also need a database named "cloudfier" accessible to a user named "cloudfier" with no password. You can build and test the generated code:

```
mvn clean install -f /tmp/test8/pom.xml
```
