# Reactive spring security with mysql and kotlin

- Create a database table with following script

```sql
create table app_user
(
    id       bigint auto_increment
        primary key,
    email    text null,
    password text null,
    roles    text null
);
```
- Insert following data into that table

```sql
INSERT INTO demo.app_user (id, email, password, roles) VALUES (1, 'sazzad', '$2a$10$eBZ5It7oVQwrui7dQhG0vOTky0B9bAMqzpjCJx20t4mM0ztVoHFDm', 'ROLE_USER');
```

- Change db config in `application.yml` file
- Run the application with following command

```shell
./gradlew bootRun
```

- Call http end point with following curl command

```
curl --location --request GET 'http://localhost:8080/private'



curl --location --request GET 'http://localhost:8080/private' \
--header 'Authorization: Basic c2F6emFkOjEyMzQ1Ng=='
```

- curl basic auth user `sazzad` password `123456`