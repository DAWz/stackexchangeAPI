#Code Sample/ Stack Exchange Api

##Requirments

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
docker run -d abdelrahmanosama/code-sample:test
```

Finally you can use the `jar` file in `daw.dev.target`
