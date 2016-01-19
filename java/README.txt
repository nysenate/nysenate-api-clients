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

To do a more granular test use a command in the following format:

    mvn test -Dtest=NYSenateClientTest#testGetCommittee


Packaging
------------

To run tests before packaging:

    mvn package

To skip the tests:

    mvn package -DskipTests
    
To package the dependencies:

    mvn dependency:copy-dependencies -DoutputDirectory=<where you want them to go>

ChangeLog
--------------

2.0.0 - 2016-01-19
~~~~~~~~~~~~~~~~~~~~~~~~

* Retrieve senators from new nysenate.gov site using JSON
* Minor modifications to senator model

1.0.2 - 2013-11-25
~~~~~~~~~~~~~~~~~~~~~~~~

* Added node ids to committee, member, and senator objects.
* Implemented Comparable interface for committee, member, district, and senator objects.

1.0.1 - 2013-04-03
~~~~~~~~~~~~~~~~~~~~~~~~

* Fail gracefully when nid is invalid.

1.0.0 - 2013-01-15
~~~~~~~~~~~~~~~~~~~~~~~~

Initial Release.
