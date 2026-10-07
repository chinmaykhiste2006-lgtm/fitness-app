FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY target/userservice-0.0.1-SNAPSHOT.jar /app/userservice.jar

ENTRYPOINT ["java", "-jar", "/app/userservice.jar"]