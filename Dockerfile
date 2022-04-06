FROM openjdk:17-jdk-alpine AS builder
WORKDIR /app/pd-library
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ["./mvnw", "package"]
FROM openjdk:17-jdk-alpine
ARG PASSWORD
ARG ACTIVE_PROFILE
WORKDIR /app
COPY --from=builder /app/pd-library/target/rbc-library-0.0.1-SNAPSHOT.jar rbc-library-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["SPRING_DATASOURCE_PASSWORD=$PASSWORD", "SPRING_PROFILES_ACTIVE=$ACTIVE_PROFILE", "java", "-jar","rbc-library-0.0.1-SNAPSHOT.jar"]