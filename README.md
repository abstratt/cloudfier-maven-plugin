# cloudfier-maven-plugin


### Generating code from a project deployed at develop.cloudier.com

```
mvn com.abstratt:cloudfier-maven-plugin:generate \
    -Dkirra.target.dir=/tmp/test8 \
    -Dkirra.target.platform=jee \
    -Dkirra.project.name=timetracker \
    -Dkirra.project.basepath=cloudfier-examples \    
    -Dkirra.project.owner=demo     
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
You can then build and test the generated code (assuming you have the required Postgres database created):

```
mvn clean install -f /tmp/test8/pom.xml
```
