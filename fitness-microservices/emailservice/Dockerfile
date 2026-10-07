FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY target/emailservice-0.0.1-SNAPSHOT.jar /app/emailservice.jar

ENTRYPOINT ["java", "-jar", "/app/emailservice.jar"]