# Spring-boot-app

This application created to learn Spring boot and PostgreSQL. Additionally, I included design pattern, unit testing, performance testing, test coverage report, binary file help to developers, CI, and Docker. I have started to this application from [Spring Boot Tutorial | Full Course [2022] [NEW]](https://www.youtube.com/watch?v=9SGDpanrc8U&ab_channel=Amigoscode) by [Amigoscode](https://www.youtube.com/@amigoscode) and improved. Thanks to him for giving an opinion to start a project.


Here you can see the difference between **IMPROVED** and **OLD VERSION** of this application
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
    │   │               │   ├── StudentController.java          |    │       ├── application.properties
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
Docker Desktop must be installed. To run the app use below command.

```
$ sudo ./project-dev code-run
```

Ready to go. Just click [here](http://localhost:8080/swagger-ui/index.html) and test the API. Do not hesitate to create an issue to improve my skills, please.
### About `project-dev` file

This bash script created to people can use this API easily. You can just run $`project-dev help` and the output should be as below:
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
[Locust](https://github.com/locustio/locust) have been used for performans testing. To test the API, run `./project-dev performans-test`
