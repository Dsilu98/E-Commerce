# Multi-stage build
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /build
COPY pom.xml .
COPY auth-service/pom.xml auth-service/
RUN mvn -B -DskipTests -f auth-service/pom.xml dependency:resolve

COPY auth-service/src auth-service/src
RUN mvn -B -DskipTests -f auth-service/pom.xml package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/auth-service/target/auth-service-*.jar auth-service.jar

EXPOSE 8081
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "/app/auth-service.jar"]

