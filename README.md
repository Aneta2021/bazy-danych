# JDBC & Hibernate

## Running app
After import to IDE choose main class: `pl.szczepanski.marek.demo.databases.DatabasesApplication`.
Compile and run using main class.

## Using
Go to: http://localhost:8080/h2-console/
- driver: `org.h2.Driver`
- jdbc url: `jdbc:h2:mem:testdb`
- user: `sa`
- password: 

## JDBC examples
- http://localhost:8080/example1
- http://localhost:8080/example2
- http://localhost:8080/example3
- http://localhost:8080/example4
- http://localhost:8080/example5
- http://localhost:8080/example6
- http://localhost:8080/example7

## Initial configuration
Application initially is configured to use H2 database engine. 

Database schema is defined in file: `src/main/resources/schema-h2.sql`

Initial database content is loaded using file: `src/main/resources/data-h2.sql`

## Other databases
This is Spring Boot application so you can configure it using `application.properties` file.
To change H2 database to MySql database remove one line:
```properties
spring.datasource.platform=h2
```
and add following lines:
```properties
spring.datasource.url = jdbc:mysql://localhost:3306/sda?useSSL=false
spring.datasource.username = test
spring.datasource.password = marek123
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect
``` 

