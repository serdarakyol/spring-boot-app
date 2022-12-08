FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

COPY .mvn/ /app/.mvn/
COPY mvnw mvnw.cmd pom.xml /app/
COPY src/ /app/src/

RUN /app/mvnw dependency:go-offline

CMD ["./mvnw", "spring-boot:run"]
