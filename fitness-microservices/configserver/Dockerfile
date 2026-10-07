FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY target/configserver-0.0.1-SNAPSHOT.jar /app/configserver.jar

ENTRYPOINT ["java", "-jar", "/app/configserver.jar"]