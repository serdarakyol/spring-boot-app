# Spring-boot-app

I created this application to learn Spring Boot and PostgreSQL. In addition, I implemented the singleton design pattern, unit testing, performance testing, test coverage reporting, bash script to aid developers/users, continuous integration (CI), and Docker deployment. Furthermore, I migrated the application from Spring Boot 2.x to 3.x. I started this project by following the [Spring Boot Tutorial | Full Course [2022] [NEW]](https://www.youtube.com/watch?v=9SGDpanrc8U&ab_channel=Amigoscode) from [Amigoscode](https://www.youtube.com/@amigoscode) and improved upon it. I would like to thank the author for inspiring me to start this project.


Here you can see the difference between the **IMPROVED** and **OLD VERSIONS** of this application.
```
IMPROVED                                                        | OLD VERSION                                  
----------------------------------------------------------------|------------------------------------------------
$ `tree --gitignore`                                            |$ `tree --gitignore`
.                                                               |.
├── docker-compose.yml                                          |├── mvnw
├── Dockerfile                                                  |├── mvnw.cmd
├── mvnw                                                        |├── pom.xml
├── mvnw.cmd                                                    |├── README.md
├── performance-testing                                         |└── src
│   └── locust.py                                               |    ├── main
├── pom.xml                                                     |    │   ├── java
├── project-dev                                                 |    │   │   └── com
├── README.md                                                   |    │   │       └── example
└── src                                                         |    │   │           └── demo
    ├── main                                                    |    │   │               ├── DemoApplication.java
    │   ├── java                                                |    │   │               └── Student
    │   │   └── com                                             |    │   │                   ├── StudentConfig.java
    │   │       └── example                                     |    │   │                   ├── StudentController.java
    │   │           └── demo                                    |    │   │                   ├── Student.java
    │   │               ├── controller                          |    │   │                   ├── StudentRepository.java
    │   │               │   ├── BodyResponses.java              |    │   │                   └── StudentService.java
    │   │               │   ├── CommonResponses.java            |    │   └── resources
    │   │               │   ├── StudentController.java          |    │       └── application.properties
    │   │               │   └── TeacherController.java          |    └── test
    │   │               ├── DemoApplication.java                |        └── java
    │   │               ├── dto                                 |            └── com
    │   │               │   ├── StudentDTO.java                 |                └── example
    │   │               │   └── TeacherDTO.java                 |                    └── demo
    │   │               ├── entity                              |                        └── DemoApplicationTests.java
    │   │               │   ├── Student.java                    |
    │   │               │   └── Teacher.java                    |
    │   │               ├── exception                           |
    │   │               │   ├── BadRequestException.java        |
    │   │               │   └── NotFoundException.java          |                                                 
    │   │               ├── mapper                              |
    │   │               │   ├── StudentMapper.java              |
    │   │               │   └── TeacherMapper.java              |
    │   │               ├── repository                          |
    │   │               │   ├── StudentRepository.java          |
    │   │               │   └── TeacherRepository.java          |
    │   │               ├── service                             |
    │   │               │   ├── StudentService.java             |
    │   │               │   └── TeacherService.java             |
    │   │               ├── serviceIml                          |
    │   │               │   ├── StudentServiceImpl.java         |
    │   │               │   └── TeacherServiceImpl.java         |
    │   │               └── utils                               |
    │   │                   └── Utils.java                      |
    │   └── resources                                           |
    │       ├── application.properties                          |
    │       ├── log4j2.xml                                      |
    │       ├── static                                          |
    │       └── templates                                       |
    └── test                                                    |
        └── java                                                |
            ├── com                                             |
            │   └── example                                     |
            │       └── demo                                    |
            │           ├── DemoApplicationTests.java           |
            │           ├── repository                          |
            │           │   └── StudentRepositoryTest.java      |
            │           ├── service                             |
            │           │   └── StudentServiceTest.java         |
            │           └── utils                               |
            │               └── UtilsTest.java                  |
            └── resources                                       |
                └── application.properties                      |
                                                                |
28 directories, 35 files                                        |13 directories, 12 files
```

## Usage
Docker Desktop must be installed. To run the app just use the below command.

```
$ sudo ./project-dev code-run
```

Ready to go. Just click [here](http://localhost:8080/swagger-ui/index.html) and test the API.

### About `project-dev` file
This bash script was created to make it easier for people to use this API. To access the help menu, simply run `$ project-dev help` and the output should be as shown below:
```
Usage: ./project-dev <command>
<command>:
    help
        Display this help message.
    build-jar
        Build jar file
    code-run
        Runs containerized app
    performance-test
        Stress testing for the API
    test-coverage-report
        Shows unit test coverage reports
```

## Test
To run unit tests, first navigate to the root directory of this project and execute the command $` mvn test`.
Note: PR #42 improved the test coverage up to 65%

[Locust](https://github.com/locustio/locust) has been used for performance testing. Please ensure that the API is running before executing the performance test command `./project-dev performance-test`.

## Please do not hesitate to create an issue to help me improve my skills.
