FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/student-scoring-system-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]