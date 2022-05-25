FROM openjdk:17-jdk-alpine AS builder
WORKDIR /app
ARG ACTIVE_PROFILE
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ["./mvnw", "package", "-P", "$ACTIVE_PROFILE"]
FROM openjdk:17-jdk-alpine
ARG PASSWORD
ARG ACTIVE_PROFILE
ENV SPRING_PROFILES_ACTIVE=$ACTIVE_PROFILE
ENV SPRING_DATASOURCE_PASSWORD=$PASSWORD
WORKDIR /app
COPY entrypoint.sh /entrypoint.sh
COPY --from=builder /app/target/rbc-library-catalog-0.0.1-SNAPSHOT.jar rbc-library-catalog-0.0.1-SNAPSHOT.jar
EXPOSE 8080
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]