# spring-test-unit-and-integration #

Following along with the blogpost here:

<https://dzone.com/articles/unit-and-integration-tests-in-spring-boot-2>

This shows how to do unit and integration tests with a basic sample
app.  It starts with a simple Spring Boot 2 webflux project and adds tests.
It's a slightly weird example.  They're using `CompleteableFuture` for 
async instead of Mono/Flux or Single/Observable.

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

NOTE: The app itself is broken.  It can't convert the MangaResult to a JSON
response.  I tried checking out the repo of the example and running it and
I had the same problem.  It's still a pretty good example of tests, so I'm
keeping it around.  The unit tests all run (since they mock out the service
calls), but the integration tests fail since the service isn't working right.
I left them in for comparison purposes.

IntelliJ complains about some of the Lombok generated methods not existing
too.  I hear Lombok works pretty well in Eclipse.  There's a Lombok plugin
for IntelliJ.  Maybe I should test that, if I ever decide to play more
with Project Lombok.  It's a cool idea.
