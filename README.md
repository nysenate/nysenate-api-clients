nysenate-api-clients
====================
Client libraries that pull data from the public NYSenate.gov API. Currently only has Java code.

Published as the Maven artifact `gov.nysenate:nysenate-java-client` (Java 21).
It consumes the public, unauthenticated feed at https://www.nysenate.gov/nys_feeds/senators

Usage
-----

```java
new NYSenateJSONClient().getSenators();         // default https://www.nysenate.gov
new NYSenateJSONClient(domain).getSenators();   // a given domain, e.g. a staging site
```
