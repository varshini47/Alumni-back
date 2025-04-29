# ---- Build stage ----
    FROM maven:3.9.9-eclipse-temurin-17 AS build

    WORKDIR /app
    
    # Cache dependencies
    COPY pom.xml .
    RUN mvn dependency:go-offline
    
    # Copy source and build the app
    COPY . .
    RUN mvn clean package -DskipTests
    
    # ---- Runtime stage ----
    FROM eclipse-temurin:17-jdk-jammy
    
    WORKDIR /app
    COPY --from=build /app/target/*.jar app.jar
    
    EXPOSE 8080
    
    # Use Render's dynamic port or default to 8080
    ENV PORT=8080
    ENTRYPOINT ["java", "-jar", "app.jar"]
    