# Multi-stage build
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /build
COPY pom.xml .
COPY order-service/pom.xml order-service/
RUN mvn -B -DskipTests -f order-service/pom.xml dependency:resolve

COPY order-service/src order-service/src
RUN mvn -B -DskipTests -f order-service/pom.xml package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/order-service/target/order-service-*.jar order-service.jar

EXPOSE 8083
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]

