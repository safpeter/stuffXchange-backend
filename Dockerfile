FROM openjdk:12-alpine

WORKDIR /app

COPY target/test-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java" , "-jar" , "app.jar" ]