Maven Instructions
=====================

Testing
---------

Testing currently requires live access to the running API.

1. Copy src/test/resources/log4j-test.xml.example to src/test/resources/log4j-test.xml
2. Copy src/test/resources/test.properties.example to src/test/resources/test.properties
3. Fill in your apiDomain (you chose this when you signed up) and apiKey (we gave this to you).
4. mvn test

The test reports are stored in `target/surefire-reports` for future references and/or processing.


Packaging
------------

To run tests before packaging:

    mvn package

To skip the tests:

    mvn package -DskipTests
    
To package the dependencies:

    mvn dependency:copy-dependencies -DoutputDirectory=<where you want them to go>
