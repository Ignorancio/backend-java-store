FROM maven:3.9.9-amazoncorretto-17-al2023 AS builder

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:17-al2023

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]