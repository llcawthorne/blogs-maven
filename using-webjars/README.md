# spring-test-unit-and-integration #

Following along with the blogpost here:

<https://www.baeldung.com/maven-webjars>

Also read several other blogs and found the helpful tip about using 
the webjars-locator on the webjars site at:

<https://www.webjars.org/documentation#springboot>

Webjars and the /webjars path are automatically enabled by Spring Boot,
so we didn't need to do the config shown in the blogpost for 
SpringMVC.

## Requirements
For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

You can use the
[Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html)
plugin to run the app:

```shell
mvn spring-boot:run
```

Then you can browse to <http://localhost:8080/> to see the simple page
that is using Bootstrap and jQuery from WebJars.
