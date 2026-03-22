FROM eclipse-temurin:17-jre-focal
COPY target/flight-service.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080