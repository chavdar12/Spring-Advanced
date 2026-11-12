# Build:  docker image build -t courses-api .
# Run:    docker container run -p 8080:8080 courses-api   (or: docker compose up)
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:25-jre
LABEL org.opencontainers.image.title="courses-api"
WORKDIR /app
COPY --from=build /app/build/libs/containerization-and-documentation-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
