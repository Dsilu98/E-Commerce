# Multi-stage build
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /build
COPY pom.xml .
COPY product-service/pom.xml product-service/
RUN mvn -B -DskipTests -f product-service/pom.xml dependency:resolve

COPY product-service/src product-service/src
RUN mvn -B -DskipTests -f product-service/pom.xml package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/product-service/target/product-service-*.jar product-service.jar

EXPOSE 8082
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]

