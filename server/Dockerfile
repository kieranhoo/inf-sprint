FROM openjdk:oraclelinux8
WORKDIR /app

COPY .mvn/ .mvn

COPY pom.xml ./
COPY mvnw ./

RUN sed -i -e 's/\r$//' mvnw

RUN ./mvnw dependency:go-offline

COPY src ./src
COPY deployments/application.properties /src/main/resources/application.properties

CMD [ "./mvnw", "spring-boot:run" ]