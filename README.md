## About

Demo to show a bug with LocalDate (ex: "2023-09-28") and Postgres.

A LocalDate is always saved incorrectly as the previous day, ex: "2023-09-27".

Logs show the right value being sent:

```shell
TRACE io.micronaut.data.query - Binding parameter at position 1 to value 2023-09-28 with data type: DATE
```

# Execute

Configure then run main class: `./gradlew run`


# Setup

* Update the data source in `application.yml`:

```yml
    url: "jdbc:postgresql://localhost:5432/demo"
    username: "demo"
    password: "demo123"
```

You can also use the following to create a new user & db:

```shell
createuser demo --login --pwprompt
createdb demo --owner=demo
```
