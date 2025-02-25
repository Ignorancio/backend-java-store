FROM amazoncorretto:17.0.14-alpine3.21

WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

CMD ["./mvnw", "spring-boot:run"]