FROM openjdk:oraclelinux8
WORKDIR /app

COPY .mvn/ .mvn

COPY pom.xml ./
COPY mvnw ./

RUN sed -i -e 's/\r$//' mvnw

RUN ./mvnw dependency:go-offline

COPY src ./src

CMD [ "./mvnw", "spring-boot:run" ]