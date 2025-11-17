# Multi-stage build
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /build
COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/
RUN mvn -B -DskipTests -f api-gateway/pom.xml dependency:resolve

COPY api-gateway/src api-gateway/src
RUN mvn -B -DskipTests -f api-gateway/pom.xml package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/api-gateway/target/api-gateway-*.jar api-gateway.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]

