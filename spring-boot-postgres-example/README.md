# Spring Boot Postgress Example #

This example goes along with the blogpost
<https://springframework.guru/configuring-spring-boot-for-postgresql>.
It's a pretty clean example of how to get PostgreSQL working with Spring Boot.

Start postgres with user postgres and password Password1 setup 
(TODO: make this not use postgres user.  also, consider including a 
dockerfile to start postgres locally with user setup),
then you can use the
[Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html)
plugin to run the app:

```shell
mvn spring-boot:run
```

Then you can browse to <http://localhost:8080/> to see the app.