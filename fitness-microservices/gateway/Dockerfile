FROM eclipse-temurin:25-jre

WORKDIR /app

COPY target/gateway-0.0.1-SNAPSHOT.jar /app/gateway.jar

ENTRYPOINT ["java", "-jar", "/app/gateway.jar"]