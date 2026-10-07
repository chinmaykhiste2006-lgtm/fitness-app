FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY target/activityservice-0.0.1-SNAPSHOT.jar /app/activityservice.jar

ENTRYPOINT ["java", "-jar", "/app/activityservice.jar"]