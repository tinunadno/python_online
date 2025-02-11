FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY . . 

RUN mvn clean install

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar /app/runnable.jar

EXPOSE 8761

CMD ["java", "-jar", "runnable.jar"]
