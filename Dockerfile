# ----------- Stage 1: Build JAR -----------------
FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /app

# Copy project files
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .
COPY src ./src

# Give execution permission to mvnw
RUN chmod +x mvnw

# Pre-fetch dependencies
RUN ./mvnw dependency:go-offline

# Build the application
RUN ./mvnw clean package -DskipTests

# ----------- Stage 2: Run App -----------------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
