FROM maven:3.9.4 AS builder

WORKDIR /build

# copy pom and sources separately to leverage layer caching
COPY pom.xml ./
COPY src ./src

# build the application (skip tests for faster image builds)
RUN mvn -B clean package -DskipTests

FROM openjdk:17.0.2-jdk-slim

WORKDIR /app

# copy the built jar from the builder stage; use a wildcard to avoid hardcoding the artifactId
COPY --from=builder /build/target/*.jar ./app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

# docker build -t spring:v1 .
# docker run -p 8080:8080 --mount type=volume,source=mydata,target=/app/data,volume-nocopy spring:v1
# docker run -p 8080:8080 `
#   --mount type=volume,source=mydata,target=/app/volume `
#   --mount type=bind,source=C:\myconfig,target=/app/bind `
#   spring:v1