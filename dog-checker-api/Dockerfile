# Stage 1: Build with Maven
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

# Stage 2: Run with lightweight JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/quarkus-app/lib/ lib/
COPY --from=build /app/target/quarkus-app/*.jar ./
COPY --from=build /app/target/quarkus-app/app/ app/
COPY --from=build /app/target/quarkus-app/quarkus/ quarkus/
EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar"]
