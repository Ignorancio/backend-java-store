FROM maven:3.9.9-amazoncorretto-17-al2023

WORKDIR /app
COPY . .
RUN mvn package -DskipTests

CMD ["mvn", "spring-boot:run"]