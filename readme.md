# Code Sample/ Stack Exchange Api

## Requirments

For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run the application on your local machine. One way is to execute the `main` method in the `dev.daw.springbootsample.Application` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

Alternativley you can pull the [docker container](https://hub.docker.com/repository/docker/abdelrahmanosama1/code-sample/general) using:

```shell
docker pull abdelrahmanosama1/code-sample:latest
```
And to run it use:
```shell
docker run -d abdelrahmanosama1/code-sample:test
```

Finally you can use the `jar` file in `daw.dev.target`

## Description

- The application is meant to call the stack exchange upon start to fetch the 20 latest featured questions by date from [StackOverflow.com Featured Questions API](https://api.stackexchange.com/docs/featured-questions)
- The result of the call is stored in an in memory H2 SQL database (the database runs in memory with no presistence configuration)
- The application exposes REST APIs to query/ delete the stored data, the expose APIs are:
  - `HTTP GET` endpoint to get all the questions in the database
  - `HTTP GET` endpoint to retrieve a single question by id
  - `HTTP DELETE` endpoint to remove a single question by id from the database
  - `HTTP GET` endpoint to retrieve all questions that have specified tags with the choice of conjunction/ disjunction
- The fields returned for each question are:
  - id (Number)
  - tags (Array of Strings)
  - is_answered (Boolean)
  - answer_count (Number)
  - creation_date (datetime in ISO8601 format as String)
  - user_id (Number) of the user who asked the question
*You can head to /swagger-ui.html upon running the app to find detailed documentation for the APIs

Addiotionally the exposes a `HTTP GET` endpoint that returns details about a user by id acting as a proxy to [StackOverflow.com Users Api](https://api.stackexchange.com/docs/users-by-ids) the resuslt of this prixy call is then cached in Simple Cache so that the same query does not lead to multiple calls to the StackOverflow API
The following fields are returned for each user:
- user_id (Number)
- creation_date (datetime in ISO8601 format as String)
- display_name (String)

