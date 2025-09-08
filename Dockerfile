# STAGE 1: Build the application using Maven
# This stage uses a full JDK and Maven to compile your Java code.
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw package -DskipTests

# STAGE 2: Create the final, lightweight container image
# This stage uses a slim JRE, which is much smaller, making your final image more efficient.
FROM openjdk:17-jdk-slim
WORKDIR /app
# This is the corrected line. Using a wildcard (*) makes the build more reliable.
# It will find any .jar file in the target directory and copy it.
COPY --from=build /app/target/*.jar ./app.jar
EXPOSE 8080
# Command to run the application when the container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]
