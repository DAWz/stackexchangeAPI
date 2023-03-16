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
docker run -d abdelrahmanosama1/code-sample:latest
```

Finally you can use the `jar` file in `daw.dev.target`

## Description

- The application is meant to call the stack exchange upon start to fetch the 20 latest featured questions by date from [StackOverflow.com Featured Questions API](https://api.stackexchange.com/docs/featured-questions)
- The result of the call is stored in an in memory H2 SQL database (the database runs in memory with no presistence configuration)
- The application exposes REST APIs to query/ delete the stored data, the expose APIs are:
  - `HTTP GET` endpoint to get all the questions in the database
  - `HTTP GET` endpoint to retrieve a single question by id
  - `HTTP DELETE` endpoint to remove a single question by id from the database
  - `HTTP GET` endpoint to retrieve all questions that have specified tags with the choice of conjunction/ disjunction (make sure to use the url parameter operation and set to either all/ any)
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

## Architectural decisisions/ tech stack

### [Spring Boot 3](https://spring.io)
Spring boot is most popular Java Framework now that is very powerful; providing annotations for almost everything and it has great community support.

### H2 SQL in-memory database:
H2 is an in-memory SQL database that has native support for Spring Boot, it made sense as a choice because the database needs to live for as long as the app is running and then has to be wiped with the next Stack Exchange API call which happens on app start.
Inaddition, a relational database made sense as choice to for the referential capabilities provided that helped a great deal when implementing the getQuestionsByTags functionallity handling the many-to-many relationship between Tags and Questions making it easier to get all the questions for a specific tag with a simple join.

### JPA & Hibernate
The most widley used ORM for SQL for Spring Boot that provids great support for almost all basic database queries which abstracted away the Data_Access_Layer (Repository Level) completley by just extending JpaRepository which reduces code maintenance and testing.

### JUnit 5, Mockito
JUnit and Mockito is a very powerful combo that makes writing unit tests and moking dependencies a very easy experience

### Lombok
A library that provides annotation for boiler plate code such as Getters, Setters & Constructors.

### Open API (Swagger)
A library that helps for automating API documentation providing a web page and an endpoint to view the available APIs in an aesthetically pleasing view.
