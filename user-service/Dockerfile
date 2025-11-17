# Multi-stage build
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /build
COPY pom.xml .
COPY user-service/pom.xml user-service/
RUN mvn -B -DskipTests -f user-service/pom.xml dependency:resolve

COPY user-service/src user-service/src
RUN mvn -B -DskipTests -f user-service/pom.xml package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/user-service/target/user-service-*.jar user-service.jar

EXPOSE 8084
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]

