# build
FROM maven:3.8.6-openjdk-17 AS builder
WORKDIR /usr/src/bcsm
COPY . .
RUN mvn clean package -DskipTests

# package
FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /usr/src/bcsm/target/*.jar /app/app.jar
EXPOSE 9009
ENTRYPOINT ["java", "-jar", "/app/app.jar"]