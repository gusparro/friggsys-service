FROM eclipse-temurin:25-jdk-alpine
LABEL authors="gustavoparro"

WORKDIR /friggsys

COPY target/friggsys-service-*.jar friggsys-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "friggsys-service.jar"]