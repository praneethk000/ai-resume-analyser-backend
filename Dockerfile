# Stage 1: Build the application
FROM amazoncorretto:17 AS build
WORKDIR /app

# Copy the entire project
COPY . .

# Grant execution rights and build the JAR
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# Stage 2: Setup the runtime environment
FROM amazoncorretto:17
WORKDIR /app

# Copy the built JAR from the 'build' stage
COPY --from=build /app/build/libs/ai-resume-analyser-backend-0.0.1-SNAPSHOT.jar /app/ai-resume-analyser-backend.jar

EXPOSE 8080
CMD ["java", "-jar", "ai-resume-analyser-backend.jar"]