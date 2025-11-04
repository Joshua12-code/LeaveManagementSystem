# Use a valid and supported JDK base image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy everything
COPY . .

# Give permission to Maven wrapper (important for Linux)
RUN chmod +x mvnw

# Build the app (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Expose the port Spring Boot runs on
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "target/employee-leave-management-0.0.1-SNAPSHOT.jar"]
