# Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /build

COPY pom.xml .
COPY common common
COPY iot-processor iot-processor
COPY iot-frontend iot-frontend
COPY iot-data-generator iot-data-generator

# Maven Build nur für iot-data-generator
RUN mvn -pl iot-data-generator -am clean package -DskipTests \
    -Dmaven.compiler.source=17 -Dmaven.compiler.target=17 -Dmaven.compiler.release=17

# Run Stage
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /build/iot-data-generator/target/*.jar iot-data-generator.jar

ENTRYPOINT ["java","-jar","iot-data-generator.jar"]
