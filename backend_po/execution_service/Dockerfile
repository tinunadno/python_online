FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app
COPY . .
RUN mvn install -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

RUN apt-get update && apt-get install -y python3 python3-pip && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar /app/runnable.jar

RUN python3 --version && pip3 --version

EXPOSE 8082

CMD ["java", "-jar", "runnable.jar"]

