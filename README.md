Cucumber allows the re-running of failed tests by using the rerun formatter. As a result of this a new json report file is generated with passing and/or failing tests. This java implementation caters for merging of the cucumber json report from the 2nd run to the 1st run.

This becomes primarily useful for when using it on jenkins as a CI pipeline. Without the merging, jenkins shows both reports and as such, may result in duplication of test scenarios being displayed on the html publisher. By merging the two reports, we will have one json report which holds the latest results of the test scenarios.

# Usage

Set the path to cucumber report
```
export CUCUMBER_REPORT="<PATH_TO_CUCUMBER_REPORT>"
```

Set the path to cucumber rerun report
```
export CUCUMBER_RERUN_REPORT="<PATH_TO_CUCUMBER_RERUN_REPORT>"
```

# Adding this as a dependency to your project as a Jar file:

As this uses gradle as a build tool, the fatJar task in build.gradle creates a single Jar with all its dependencies. To create the Jar file:
```
gradle fatJar
```
This creates a Jar file under /build/libs
