# Springframework Guru: Using JdbcTemplate with Spring Boot and Thymeleaf

Just a place to store my work as I follow the tutorial from
<https://springframework.guru/using-jdbctemplate-with-spring-boot-and-thymeleaf/>.

I mostly just followed the tutorial, except I updated the spring-boot-starter-parent version in the pom.xml so it would pull in a new version of the MySQL driver to work with version 8 of MySQL.

The main thing of interest in this tutorial is the various uses of JdbcTemplate in /src/main/java/guru/springframework/service/DogServiceImpl.java.
Given the availability of JPA, I don't see much point in displaying how to make
repo's using JdbcTemplate.  For better examples of small uses of JDBCTemplate,
check out my jdbctemplate-for-queries tutorial in the blogs-gradle repository.

Note: before running this, update the mysql username and password in application.properties and do a `create database dogrescue` in MySQL.

You can use the
[Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html)
plugin to run the app:

```shell
mvn spring-boot:run
```

Then you can browse to <http://localhost:8080/> to see the app.
