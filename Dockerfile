FROM openjdk:17.0.2-slim
COPY . .
ENTRYPOINT ["./mvnw", "spring-boot:run"]
