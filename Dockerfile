# Use official OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy everything
COPY . .

# Build the app
RUN ./mvnw clean package -DskipTests

# Expose the port Spring Boot runs on
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "target/EmployeeLeaveManagement-0.0.1-SNAPSHOT.jar"]
