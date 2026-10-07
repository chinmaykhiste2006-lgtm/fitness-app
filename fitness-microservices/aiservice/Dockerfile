FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY target/aiservice-0.0.1-SNAPSHOT.jar /app/aiservice.jar

ENTRYPOINT ["java", "-jar", "/app/aiservice.jar"]