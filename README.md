## Business case
School registration system
Design and implement simple school registration system
- Assuming you already have a list of students
- Assuming you already have a list of courses
- A student can register to multiple courses
- A course can have multiple students enrolled in it.
- A course has 50 students maximum
- A student can register to 5 course maximum

## Tech stack
So this repository built with love with following tech stack
- Java
- Maven
- Spring Boot
- Docker (docker-compose)
- JUnit
- MySQL

All the configuration is done in the **docker-compose.yaml** file.

## How to run entire application with docker?
In order to start the process, all you need to do is:
- Pre-requisite you have Maven installed on the machine in which you are running this, for compilation.
- Go to the terminal and change the directory where you have this repository, when you do hit command "ls" you should see **deploy.sh** file in the list.
- Execute the **deploy.sh** inside your terminal.
- Depending on your internet, docker containers and maven dependencies will be downloaded. Grab a cup of coffee! :)

## How to test the application with endpoints and payloads?

So the application is hosted on a docker container which is accessible at the **http://localhost/** on port **8080**.
You can use this cURL request to test one of the endpoint:

```
curl --location --request POST 'http://localhost:8080/student/insertOne' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name":"Student1243"
}'
```

There is a postman collection in folder, **postman-collection** it contains all the request and payloads.
To import these in postman, use these steps https://learning.postman.com/docs/getting-started/importing-and-exporting-data/

## How to test the database?

In order to access the database, you can connect to it using the following details:
- Hostname: localhost
- Port: 3301
- Username: root
- Password: root

There are two property files, one for docker and other one for when running this application without docker.
For both have changed credentials, incase you are testing it locally you need the update application.properties file.



