FROM maven:3.8.6-eclipse-temurin-11-alpine AS maven

WORKDIR /app

COPY . .

RUN mvn clean package

ENTRYPOINT ["java","-jar","target/message-server-1.0.0.jar"]