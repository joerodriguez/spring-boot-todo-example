Spring Boot Example
===================

Setup
-----

Dependencies:
* java jdk 1.7
* maven
* postgres

See db schema in migration.sql

The environment variable DATABASE_URL must be set to run in any environment, including test.
This should be in the postgres/heroku format: postgres://username:password@host:port/db_name

Running tests:
mvn test

Running a server:
set the DATABASE_URL environment variable
mvn clean compile
mvn install -DskipTests=true
java -jar target/spring-boot-example.jar
