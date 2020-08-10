# Builder

ARG BUILD_REGISTRY=registry-proxy.rnd.ftc.ru
FROM ${BUILD_REGISTRY}/maven:3.6-jdk-8-slim AS builder

WORKDIR /app
COPY pom.xml .
COPY src src
COPY ca ca

RUN ${JAVA_HOME}/bin/keytool -import -noprompt -trustcacerts \
    -alias CerberRootCA \
    -file ca/CerberRootCA.crt \
    -keystore ${JAVA_HOME}/jre/lib/security/cacerts \
    -storepass changeit

ENV MAVEN_CLI_OPTS="--batch-mode --errors"
RUN mvn ${MAVEN_CLI_OPTS} package -DskipTests

# Runtime

FROM ${BUILD_REGISTRY}/openjdk:8-slim

WORKDIR /app
COPY --from=builder /app/target/products.jar /app
RUN chmod -R 777 /tmp
RUN useradd app
USER app

ENV SERVER_PORT=8080
EXPOSE ${SERVER_PORT}

ENV JAVA_OPTS="-XX:+PrintFlagsFinal -XX:+UseContainerSupport -XX:MinRAMPercentage=50.0 -XX:MaxRAMPercentage=80.0"
ENTRYPOINT exec java ${JAVA_OPTS} -jar products.jar
