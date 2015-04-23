# cloudfier-maven-plugin


### Generating code from a project deployed at develop.cloudier.com

```
mvn com.abstratt:cloudfier-maven-plugin:generate \
    -Dkirra.target.dir=/tmp/test \
    -Dkirra.target.platform=jee \
    -Dkirra.project.name=timetracker \
    -Dkirra.project.basepath=cloudfier-examples \    
    -Dkirra.project.owner=demo     
```

