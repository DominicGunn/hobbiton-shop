[![Build Status](https://api.travis-ci.org/DominicGunn/hobbiton-shop.svg?branch=master)](https://api.travis-ci.org/DominicGunn/hobbiton-shop) [![Coverage Status](https://coveralls.io/repos/DominicGunn/hobbiton-shop/badge.svg?branch=master)](https://coveralls.io/r/DominicGunn/hobbiton-shop?branch=master) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/DominicGunn/hobbiton-shop/blob/master/license.txt)

# Hobbiton Shop Service

Hobbiton shop is an example packaging service that makes use of an online [product service](products-service.herokuapp.com/api/v1/products) to create and manage packages of products.

### Technology

Hobbiton Shop uses a number of technologies to work properly, and has a [JDK 8](www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) build requirement:

* [Spring Boot](https://projects.spring.io/spring-boot/) - Helps to create [Spring](Spring)-powered, production-grade applications and services with minimum fuss.
  * [Actuator](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-actuator) - Actuator includes a number of additional features to help you monitor and manage your application when it’s pushed to production, including useful metric, dump and health endpoints!  
* [Hibernate](http://hibernate.org/) - Provided and configured by Spring Boot, worth a shout because of how powerful the framework is.
* [PostgreSQL](https://www.postgresql.org/) - Used to persist package data within the shop service.
* [Mockito](http://site.mockito.org/) and [AssertJ](http://joel-costigliola.github.io/assertj/) - Powerful testing frameworks.
* [Docker](https://www.docker.com/) - For creating easily deployable containers!

And of course a bunch of other great libraries such as an embedded [Tomcat](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-starters/spring-boot-starter-tomcat) servlet, and [SLF4J](https://www.slf4j.org/) for logging.

## Setup and Execution
### Docker

Running the `docker` command will generate a versioned docker image (`hobbiton/shop:<VERSION`) that we can use to start the application. 

A `docker-compose.yml` with required services (e.x. `PostgreSQL`) has been provided.
```sh
$ ./gradlew docker
$ docker-compose up -d
```

#### From Source

Assuming `src\main\resources\application.properties` has been properly configured, running the service is as simple as

```sh
$ ./gradlew clean build
$ ./gradlew bootRun
```

### Environment Variables

Hobbiton Shop Service is considered a [12 Factor](https://12factor.net/) application, and as such is configurable with the following environment variables.

| Environment Variable | Default | Purpose |
| ------ | ------ | ------ |
| SPRING_DATASOURCE_URL | jdbc:postgresql://127.0.01:5432/shop | JDBC Url of our PostgreSQL Database.
| SPRING_DATASOURCE_USERNAME | postgres | Database user with R/W Permissions.
| SPRING_DATASOURCE_PASSWORD | postgres | Database users password.
| EXTERNAL_PRODUCT_SERVICE_USERNAME | user | Username for products service Basic HTTP Authentication.
| EXTERNAL_PRODUCT_SERVICE_PASSWORD | pass | Password for products service Basic HTTP Authentication.
| EXTERNAL_PRODUCT_SERVICE_URL | https://products-service.herokuapp.com/api/v1/products | URL of the Product Service.
| EXTERNAL_PRODUCT_SERVICE_AVAILABLE | true | If false, a static list of products will be used instead of calling the product service.