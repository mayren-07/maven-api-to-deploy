# build package
FROM jelastic/maven:3.9.4-openjdk-22.ea-b17 AS build
COPY . .
RUN mvn clean package -DskipTests

# package stage
FROM openjdk:22-jdk-slim
COPY --from=build /target/ApiEstoque-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
